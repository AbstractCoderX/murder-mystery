package ru.abstractcoder.murdermystery.core.lobby;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.benioapi.function.UncheckedRunnable;
import ru.abstractcoder.murdermystery.core.config.GeneralConfig;
import ru.abstractcoder.murdermystery.core.config.Messages;
import ru.abstractcoder.murdermystery.core.game.arena.Arena;
import ru.abstractcoder.murdermystery.core.game.role.chance.RoleDataRepository;
import ru.abstractcoder.murdermystery.core.lobby.player.LobbyPlayer;
import ru.abstractcoder.murdermystery.core.statistic.StatisticRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class LobbyEngine {

    private final GeneralConfig.Lobby settings;
    private final Arena arena;
    private final LobbySidebarManager lobbySidebarManager;
    private final SlotBarItemProcessor slotBarItemProcessor;
    private final MsgConfig<Messages> msgConfig;
    private final RoleDataRepository roleDataRepository;
    private final StatisticRepository statisticRepository;

    private final Map<Player, LobbyPlayer> waitingPlayerMap = new HashMap<>();
    private final BossBar bossBar;
    private int secondsLeft = -1;
    private boolean isActive = true;
    private UncheckedRunnable shutdownAction;

    public LobbyEngine(GeneralConfig.Lobby settings, Arena arena, LobbySidebarManager lobbySidebarManager,
            MsgConfig<Messages> msgConfig, RoleDataRepository roleDataRepository, StatisticRepository statisticRepository) {
        this.settings = settings;
        this.arena = arena;
        this.lobbySidebarManager = lobbySidebarManager;
        this.msgConfig = msgConfig;
        slotBarItemProcessor = new SlotBarItemProcessor(settings.getSlotBarItemResolver());
        this.roleDataRepository = roleDataRepository;
        this.statisticRepository = statisticRepository;

        lobbySidebarManager.init(this);
        bossBar = Bukkit.createBossBar(
                msgConfig.get(Messages.lobby__waiting_bossbar).asSingleLine(),
                BarColor.RED,
                BarStyle.SOLID
        );
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

    public Collection<LobbyPlayer> getPlayers() {
        return waitingPlayerMap.values();
    }

    public int getPlayerCount() {
        return waitingPlayerMap.size();
    }

    public LobbyPlayer getPlayer(Player player) {
        return waitingPlayerMap.get(player);
    }

    public CompletableFuture<LobbyPlayer> loadPlayer(Player player) {
        if (waitingPlayerMap.containsKey(player)) {
            throw new IllegalArgumentException(String.format("Player %s already loaded", player.getName()));
        }

        lobbySidebarManager.showSidebar(player);
        slotBarItemProcessor.populatePlayerInventory(player);
        bossBar.addPlayer(player);
        checkIncrementedPlayerCount();

        return roleDataRepository.load(player.getName()).thenCombine(
                statisticRepository.loadStatistic(player.getName()),
                (roleDataMap, statistic) -> new LobbyPlayer(player, roleDataMap, statistic)
        );
    }

    private void checkIncrementedPlayerCount() {
        int playerCount = getPlayerCount();

        if (playerCount == arena.getMinPlayers()) {
            secondsLeft = settings.starting().getMinCountStartTime();
        } else if (playerCount == arena.getMaxPlayers()) {
            secondsLeft = Math.min(secondsLeft, settings.starting().getMaxCountStartTime());
        }
    }

    public void unloadPlayer(Player player) {
        if (waitingPlayerMap.remove(player) != null) {
            checkDecrementedPlayerCount();
        }
    }

    private void checkDecrementedPlayerCount() {
        int playerCount = getPlayerCount();

        if (playerCount == arena.getMinPlayers() - 1) {
            secondsLeft = -1;
            bossBar.setTitle(msgConfig.get(Messages.lobby__waiting_bossbar).asSingleLine());
        }
    }

    public BossBar getBossBar() {
        return bossBar;
    }

    public Arena getArena() {
        return arena;
    }

}