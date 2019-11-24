package ru.abstractcoder.murdermystery.core.lobby;

import com.google.common.base.Preconditions;
import dagger.Reusable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.abstractcoder.benioapi.board.SidebarService;
import ru.abstractcoder.benioapi.board.sidebar.SidebarTemplate;
import ru.abstractcoder.benioapi.board.text.updater.PlaceholderTextUpdater;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.benioapi.util.temporal.SimpleTemporal;
import ru.abstractcoder.murdermystery.core.config.Msg;
import ru.abstractcoder.murdermystery.core.lobby.player.LobbyPlayer;
import ru.abstractcoder.murdermystery.economy.EconomyService;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

@Reusable
public class LobbySidebarManager {

    private SidebarTemplate sidebarTemplate;
    private final SidebarService sidebarService;
    private final EconomyService economyService;
    private final MsgConfig<Msg> msgConfig;

    @Inject
    public LobbySidebarManager(SidebarService sidebarService,
            EconomyService economyService, MsgConfig<Msg> msgConfig) {
        this.sidebarService = sidebarService;
        this.economyService = economyService;
        this.msgConfig = msgConfig;
    }

    public void init(LobbyEngine lobbyEngine) {
        sidebarTemplate = lobbyEngine.settings().getSidebarTemplate()
                .withLineUpdater(new PlaceholderTextUpdater()
                        .prepare(LobbyPlayer.class, lobbyEngine.getPlayerResolver()::resolve)
                        .add("{desired_role}", LobbyPlayer.class, p -> p.getPreferredRole().getName())
                        .add("{wins}", LobbyPlayer.class, p -> p.getStatistic().getWins())
                        .add("{defeats}", LobbyPlayer.class, p -> p.getStatistic().getDefeats())
                        .add("{rating}", LobbyPlayer.class, p -> p.getStatistic().getRating())
                        .add("{lobby_state}", () -> {
                            if (lobbyEngine.isStarting()) {
                                String formattedTimeLeft = SimpleTemporal.of(lobbyEngine.getSecondsLeft(),
                                        TimeUnit.SECONDS).format();
                                return msgConfig.get(Msg.lobby__starting_sidebar_state, formattedTimeLeft);
                            } else {
                                return msgConfig.get(Msg.lobby__waiting_sidebar_state).asSingleLine();
                            }
                        })
                        .add("{player_count}", lobbyEngine.getPlayerResolver()::getPlayerCount)
                        .add("{money}", economyService::getCachedBalance)
                );
    }

    public void showSidebar(Player player) {
        Preconditions.checkState(sidebarTemplate != null, "Not initialized yet");
        sidebarService.createBukkitSidebar(player, sidebarTemplate);
    }

    public void unshowForAll() {
        Bukkit.getOnlinePlayers().forEach(sidebarService::removeSidebar);
    }

}