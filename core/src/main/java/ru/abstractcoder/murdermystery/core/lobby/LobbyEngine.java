package ru.abstractcoder.murdermystery.core.lobby;

import dagger.Reusable;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.benioapi.function.UncheckedRunnable;
import ru.abstractcoder.benioapi.util.ticking.TickingService;
import ru.abstractcoder.murdermystery.core.config.GeneralConfig;
import ru.abstractcoder.murdermystery.core.config.Msg;
import ru.abstractcoder.murdermystery.core.data.PlayerDataService;
import ru.abstractcoder.murdermystery.core.game.arena.Arena;
import ru.abstractcoder.murdermystery.core.lobby.player.LobbyPlayer;
import ru.abstractcoder.murdermystery.core.lobby.player.LobbyPlayerResolver;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

@Reusable
public class LobbyEngine {

    private final GeneralConfig.Lobby settings;
    private final Arena arena;
    private final LobbySidebarManager lobbySidebarManager;
    private final SlotBarItemProcessor slotBarItemProcessor;
    private final MsgConfig<Msg> msgConfig;
    private final PlayerDataService playerDataService;

    private final LobbyPlayerResolver playerResolver;
    private final BossBar bossBar;
    private int secondsLeft = -1;
    private boolean isActive = true;
    private UncheckedRunnable shutdownAction;

    @Inject
    public LobbyEngine(GeneralConfig generalConfig, Arena arena, LobbyPlayerResolver playerResolver,
            LobbySidebarManager lobbySidebarManager,
            MsgConfig<Msg> msgConfig, PlayerDataService playerDataService,
            LobbyTicking lobbyTicking, TickingService tickingService) {
        this.arena = arena;
        this.playerResolver = playerResolver;
        this.lobbySidebarManager = lobbySidebarManager;
        this.msgConfig = msgConfig;
        this.playerDataService = playerDataService;

        settings = generalConfig.lobby();
        slotBarItemProcessor = new SlotBarItemProcessor(settings.getSlotBarItemResolver());

        lobbySidebarManager.init(this);
        bossBar = Bukkit.createBossBar(
                msgConfig.get(Msg.lobby__waiting_bossbar).asSingleLine(),
                BarColor.RED,
                BarStyle.SOLID
        );

        lobbyTicking.setLobbyEngine(this);
        tickingService.register(lobbyTicking);
    }

    public int getSecondsLeft() {
        return secondsLeft;
    }

    public void addShutdownHook(UncheckedRunnable shutdownHook) {
        shutdownAction = shutdownAction != null ? shutdownAction.andThen(shutdownHook) : shutdownHook;
    }

    public void shutdown() {
        isActive = false;
        lobbySidebarManager.unshowForAll();
        slotBarItemProcessor.clearAll();
        shutdownAction.run();
    }

    public GeneralConfig.Lobby settings() {
        return settings;
    }

    public boolean isStarting() {
        return isActive && secondsLeft != -1;
    }

    public int decrementTimeLeft() {
        return --secondsLeft;
    }

    public LobbyPlayerResolver getPlayerResolver() {
        return playerResolver;
    }

    public CompletableFuture<LobbyPlayer> loadPlayer(Player player) {
        if (playerResolver.hasPlayer(player)) {
            throw new IllegalArgumentException(String.format("Player %s already loaded", player.getName()));
        }

        lobbySidebarManager.showSidebar(player);
        slotBarItemProcessor.populatePlayerInventory(player);
        bossBar.addPlayer(player);
        checkIncrementedPlayerCount();

        return playerDataService.loadPlayerData(player)
                .thenApply(data -> new LobbyPlayer(player, data));
    }

    private void checkIncrementedPlayerCount() {
        int playerCount = playerResolver.getPlayerCount();

        if (playerCount == arena.getMinPlayers()) {
            secondsLeft = settings.starting().getMinCountStartTime();
        } else if (playerCount == arena.getMaxPlayers()) {
            secondsLeft = Math.min(secondsLeft, settings.starting().getMaxCountStartTime());
        }
    }

    public void unloadPlayer(Player player) {
        if (playerResolver.remove(player)) {
            checkDecrementedPlayerCount();
        }
    }

    private void checkDecrementedPlayerCount() {
        int playerCount = playerResolver.getPlayerCount();

        if (playerCount == arena.getMinPlayers() - 1) {
            secondsLeft = -1;
            bossBar.setTitle(msgConfig.get(Msg.lobby__waiting_bossbar).asSingleLine());
        }
    }

    public BossBar getBossBar() {
        return bossBar;
    }

    public Arena getArena() {
        return arena;
    }

}