package ru.abstractcoder.murdermystery.core.game;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import ru.abstractcoder.benioapi.BenioApiInstance;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.murdermystery.core.config.GeneralConfig;
import ru.abstractcoder.murdermystery.core.config.Messages;
import ru.abstractcoder.murdermystery.core.game.action.GameActionService;
import ru.abstractcoder.murdermystery.core.game.arena.Arena;
import ru.abstractcoder.murdermystery.core.game.bow.BowDropProcessor;
import ru.abstractcoder.murdermystery.core.game.corpse.CorpseService;
import ru.abstractcoder.murdermystery.core.game.npc.CitizensNpcService;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayerResolver;
import ru.abstractcoder.murdermystery.core.game.player.PlayerController;
import ru.abstractcoder.murdermystery.core.game.player.PlayerFactory;
import ru.abstractcoder.murdermystery.core.game.role.RoleFactory;
import ru.abstractcoder.murdermystery.core.game.role.RoleResolver;
import ru.abstractcoder.murdermystery.core.game.role.classed.RoleClassFactory;
import ru.abstractcoder.murdermystery.core.game.role.profession.template.ProfessionResolver;
import ru.abstractcoder.murdermystery.core.game.skin.SkinContainableRepository;
import ru.abstractcoder.murdermystery.core.game.time.GameTime;
import ru.abstractcoder.murdermystery.core.lobby.player.LobbyPlayer;
import ru.abstractcoder.murdermystery.core.scheduler.Scheduler;
import ru.abstractcoder.murdermystery.economy.EconomyService;

import java.util.Collection;
import java.util.Iterator;

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
    private final BenioApiInstance benio;
    private final Plugin plugin;
    private final CorpseService corpseService;
    private final CitizensNpcService npcService;

    private final SkinContainableRepository skinContainableRepository;
    private final GamePlayerResolver playerResolver;
    private final PlayerController playerController;
//    private final GameSidebarManager gameSidebarManager;
    private final BowDropProcessor bowDropProcessor;

    private final ProfessionResolver professionResolver;
    private final RoleResolver roleResolver;

    public GameEngine(Arena arena, GeneralConfig.Game settings, PlayerFactory playerFactory,
            EconomyService economyService, GoldManager goldManager, BenioApiInstance benio,
            Plugin plugin, CorpseService corpseService, CitizensNpcService npcService,
            MsgConfig<Messages> msgConfig) {
        gameTime = new GameTime(settings.general().getGameDuration());
        this.arena = arena;
        this.settings = settings;
        this.playerFactory = playerFactory;
        this.economyService = economyService;
        this.goldManager = goldManager;
        this.benio = benio;
        this.plugin = plugin;
        this.corpseService = corpseService;
        this.npcService = npcService;

        state = GameState.WAITING;
        gameActionService = new GameActionService();
        skinContainableRepository = new SkinContainableRepository();
        scheduler = new Scheduler(plugin);
        bowDropProcessor = new BowDropProcessor(this, msgConfig);

        professionResolver = new ProfessionResolver(this, msgConfig);
        RoleClassFactory roleClassFactory = new RoleClassFactory(this, msgConfig);
        RoleFactory roleFactory = new RoleFactory(roleClassFactory, professionResolver);
        roleResolver = new RoleResolver(this, roleFactory);
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

    public BenioApiInstance benio() {
        return benio;
    }

    public GoldManager getGoldManager() {
        return goldManager;
    }

    public enum WinningSide {
        MURDER,
        SURVIVERS
    }

}