package ru.abstractcoder.murdermystery.core.game;

import com.google.common.base.Preconditions;
import dagger.Reusable;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import ru.abstractcoder.benioapi.util.ticking.TickingService;
import ru.abstractcoder.murdermystery.core.config.GeneralConfig;
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
import ru.abstractcoder.murdermystery.core.game.skin.SkinContainableRepository;
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

    private final SkinContainableRepository skinContainableRepository;
    private final GamePlayerResolver playerResolver;
    private final PlayerController playerController;
//    private final GameSidebarManager gameSidebarManager;
    private final BowDropProcessor bowDropProcessor;

    private final ProfessionResolver professionResolver;
    private final RoleResolver roleResolver;
    private final Plugin plugin;

    @Inject
    public GameEngine(Arena arena, GeneralConfig generalConfig, GameActionService gameActionService,
            PlayerFactory playerFactory,
            EconomyService economyService, GoldManager goldManager,
            Scheduler scheduler, TickingService tickingService, CorpseService corpseService,
            CitizensNpcService npcService, SkinContainableRepository skinContainableRepository,
            GamePlayerResolver playerResolver, RoleResolver roleResolver, Plugin plugin,
            BowDropProcessor bowDropProcessor, ProfessionResolver professionResolver,
            RoleClassFactory roleClassFactory, PlayerController playerController) {
        this.arena = arena;
        this.playerFactory = playerFactory;
        this.economyService = economyService;
        this.goldManager = goldManager;
        this.tickingService = tickingService;
        this.scheduler = scheduler;
        this.corpseService = corpseService;
        this.npcService = npcService;
        this.skinContainableRepository = skinContainableRepository;
        this.playerResolver = playerResolver;
        this.roleResolver = roleResolver;
        this.plugin = plugin;
        this.bowDropProcessor = bowDropProcessor;
        this.professionResolver = professionResolver;
        this.gameActionService = gameActionService;

        settings = generalConfig.game();
        this.playerController = playerController;
        gameTime = new GameTime(settings.general().getGameDuration());

        professionResolver.init(this);
        roleClassFactory.init(this);

        state = GameState.WAITING;
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

    public SkinContainableRepository getSkinContainableRepository() {
        return skinContainableRepository;
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
            lobbyPlayer.getPlayer().teleport(spawnPointIterator.next());
            GamePlayer gamePlayer = playerFactory.fromLobbyPlayer(lobbyPlayer);
            gamePlayer.getRoleLogic().load();
        });

        state = GameState.PLAYING;

        gameActionService.handleStarting();
    }

    public void endGame(WinningSide winningSide) {
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

    public enum WinningSide {
        MURDER,
        SURVIVERS
    }

}