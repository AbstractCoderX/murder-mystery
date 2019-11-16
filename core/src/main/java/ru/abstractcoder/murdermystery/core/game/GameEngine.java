package ru.abstractcoder.murdermystery.core.game;

import com.google.common.base.Preconditions;
import dagger.Reusable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;
import ru.abstractcoder.benioapi.board.BoardApi;
import ru.abstractcoder.benioapi.board.SidebarService;
import ru.abstractcoder.benioapi.util.ticking.TickingService;
import ru.abstractcoder.murdermystery.core.caze.CaseRepository;
import ru.abstractcoder.murdermystery.core.config.GeneralConfig;
import ru.abstractcoder.murdermystery.core.cosmetic.responsible.VictoryResponsible;
import ru.abstractcoder.murdermystery.core.game.action.GameActionService;
import ru.abstractcoder.murdermystery.core.game.arena.Arena;
import ru.abstractcoder.murdermystery.core.game.bow.BowDropProcessor;
import ru.abstractcoder.murdermystery.core.game.corpse.CorpseService;
import ru.abstractcoder.murdermystery.core.game.npc.CitizensNpcService;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayerResolver;
import ru.abstractcoder.murdermystery.core.game.player.PlayerController;
import ru.abstractcoder.murdermystery.core.game.player.PlayerFactory;
import ru.abstractcoder.murdermystery.core.game.role.RoleResolver;
import ru.abstractcoder.murdermystery.core.game.role.classed.RoleClassFactory;
import ru.abstractcoder.murdermystery.core.game.role.profession.template.ProfessionResolver;
import ru.abstractcoder.murdermystery.core.game.side.GameSide;
import ru.abstractcoder.murdermystery.core.game.side.GameSideService;
import ru.abstractcoder.murdermystery.core.game.skin.container.SkinContainableResolver;
import ru.abstractcoder.murdermystery.core.game.time.GameTime;
import ru.abstractcoder.murdermystery.core.lobby.player.LobbyPlayer;
import ru.abstractcoder.murdermystery.core.scheduler.Scheduler;
import ru.abstractcoder.murdermystery.economy.EconomyService;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Iterator;

@Reusable
public class GameEngine {

    private GameState state;
    private final GameTime gameTime;
    private final Arena arena;
    private final GeneralConfig.Game settings;
    private final PlayerFactory playerFactory;
    private final EconomyService economyService;
    private final GoldManager goldManager;
    private final GameActionService gameActionService;
    private final Scheduler scheduler;
    private final TickingService tickingService;
    private final CorpseService corpseService;
    private final CitizensNpcService npcService;

    private final SkinContainableResolver skinContainableResolver;
    private final GamePlayerResolver playerResolver;
    private final PlayerController playerController;
    private final BoardApi boardApi;
    private final SidebarService sidebarService;
    //    private final GameSidebarManager gameSidebarManager;
    private final BowDropProcessor bowDropProcessor;

    private final ProfessionResolver professionResolver;
    private final RoleResolver roleResolver;
    private final GameSideService gameSideService;
    private final CaseRepository caseRepository;
    private final Plugin plugin;

    private GameTicking gameTicking = new GameTicking(this);

    @Inject
    public GameEngine(Arena arena, GeneralConfig generalConfig, GameActionService gameActionService,
            PlayerFactory playerFactory,
            EconomyService economyService, GoldManager goldManager,
            Scheduler scheduler, TickingService tickingService, CorpseService corpseService,
            CitizensNpcService npcService, SkinContainableResolver skinContainableResolver,
            GamePlayerResolver playerResolver, RoleResolver roleResolver, Plugin plugin,
            BowDropProcessor bowDropProcessor, ProfessionResolver professionResolver,
            RoleClassFactory roleClassFactory, PlayerController playerController,
            BoardApi boardApi, SidebarService sidebarService,
            GameSideService gameSideService, CaseRepository caseRepository) {
        this.arena = arena;
        this.playerFactory = playerFactory;
        this.economyService = economyService;
        this.goldManager = goldManager;
        this.tickingService = tickingService;
        this.scheduler = scheduler;
        this.corpseService = corpseService;
        this.npcService = npcService;
        this.skinContainableResolver = skinContainableResolver;
        this.playerResolver = playerResolver;
        this.roleResolver = roleResolver;
        this.plugin = plugin;
        this.bowDropProcessor = bowDropProcessor;
        this.professionResolver = professionResolver;
        this.gameActionService = gameActionService;

        settings = generalConfig.game();
        this.playerController = playerController;
        this.boardApi = boardApi;
        this.sidebarService = sidebarService;
        this.gameSideService = gameSideService;
        this.caseRepository = caseRepository;
        gameTime = new GameTime(settings.general().getGameDuration());

        professionResolver.init(this);
        roleClassFactory.init(this);

        state = GameState.WAITING;

        tickingService.register(gameTicking);
    }

    public GameState getState() {
        return state;
    }

    public GameTime getGameTime() {
        return gameTime;
    }

    public Arena getArena() {
        return arena;
    }

    public SkinContainableResolver getSkinContainableResolver() {
        return skinContainableResolver;
    }

    public GamePlayerResolver getPlayerResolver() {
        return playerResolver;
    }

    public PlayerController getPlayerController() {
        return playerController;
    }

    public EconomyService getEconomyService() {
        return economyService;
    }

    public GeneralConfig.Game settings() {
        return settings;
    }

    public GameActionService getActionService() {
        return gameActionService;
    }

    public CorpseService getCorpseService() {
        return corpseService;
    }

    public CitizensNpcService getNpcService() {
        return npcService;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public PlayerFactory getPlayerFactory() {
        return playerFactory;
    }

    public ProfessionResolver getProfessionResolver() {
        return professionResolver;
    }

    public RoleResolver getRoleResolver() {
        return roleResolver;
    }

    public void startGame(Collection<LobbyPlayer> lobbyPlayers) {
        Preconditions.checkState(state == GameState.WAITING, "Game already started");

        Iterator<Location> spawnPointIterator = arena.getSpawnPoints().iterator();
        lobbyPlayers.forEach(lobbyPlayer -> {
            Player player = lobbyPlayer.getPlayer();
            player.teleport(spawnPointIterator.next());

            GamePlayer gamePlayer = playerFactory.createPlayer(lobbyPlayer);

            String skinName = gamePlayer.getSkinContainer().getRealSkin().data().getName();
            player.setDisplayName(skinName);
            player.setPlayerListName(skinName);
            boardApi.getNameTagService().setNameTagHidden(player, true);
        });

        state = GameState.PLAYING;

        gameActionService.handleStarting();
    }

    public void endGame(GameSide winnedSide, @Nullable GamePlayer endInitiator) {
        tickingService.unregister(gameTicking);

        GameSide losedSide = winnedSide.getOppositeSide();

        var winnedRatings = gameSideService.getRatings(winnedSide);
        var losedRatings = gameSideService.getRatings(losedSide);

        var winnedPlayers = gameSideService.getPlayers(winnedSide);
        var losedPlayers = gameSideService.getPlayers(losedSide);

        var aliveWinnedPlayers = gameSideService.getAlivePlayers(winnedSide);
        var aliveLosedPlayers = gameSideService.getAlivePlayers(losedSide);

        double wAverageRating = gameSideService.getAverageRating(winnedSide);
        double lAverageRating = gameSideService.getAverageRating(losedSide);

        winnedRatings.forEach(rating -> {
            int currentRating = rating.value();

            if (currentRating <= 500) {
                rating.incrementBy(25);
            } else {
                int amount = Math.min(5 - ((currentRating - 500) / 1000), 1);
                rating.incrementBy(amount);
            }

            if (wAverageRating < lAverageRating) {
                rating.incrementBy(5);
            }
        });

        losedRatings.forEach(rating -> {
            int currentRating = rating.value();

            if (currentRating >= 100) {
                int amount = Math.min(1 + ((currentRating - 100) / 100), 25);
                rating.decrementBy(amount);
            }

            if (wAverageRating < lAverageRating) {
                rating.decrementBy(5);
            }
        });

        aliveWinnedPlayers.forEach(gamePlayer -> gamePlayer.cosmetics(VictoryResponsible.class)
                .forEach(resp -> resp.onVictory(gamePlayer))
        );

        if (endInitiator != null) {
            caseRepository.giveMurderCase(endInitiator.getName(), 1);
        }

        scheduler.runSyncLater(4 * 20, () -> Bukkit.getServer().shutdown());
        //TODO
    }

    public BowDropProcessor getBowDropProcessor() {
        return bowDropProcessor;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public TickingService getTickingService() {
        return tickingService;
    }

    public GoldManager getGoldManager() {
        return goldManager;
    }

    public SidebarService getSidebarService() {
        return sidebarService;
    }

}