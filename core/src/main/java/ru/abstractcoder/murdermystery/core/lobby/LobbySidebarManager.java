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
import ru.abstractcoder.murdermystery.core.config.Messages;
import ru.abstractcoder.murdermystery.economy.EconomyService;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

@Reusable
public class LobbySidebarManager {

    private SidebarTemplate sidebarTemplate;
    private final SidebarService sidebarService;
    private final EconomyService economyService;
    private final MsgConfig<Messages> msgConfig;

    @Inject
    public LobbySidebarManager(SidebarService sidebarService,
            EconomyService economyService, MsgConfig<Messages> msgConfig) {
        this.sidebarService = sidebarService;
        this.economyService = economyService;
        this.msgConfig = msgConfig;
    }

    public void init(LobbyEngine lobbyEngine) {
        sidebarTemplate = lobbyEngine.settings().getSidebarTemplate()
                .withLineUpdater(new PlaceholderTextUpdater()
                        .add("{desired_role}", player -> lobbyEngine.getPlayer(player)
                                .getPreferredRole().getName()
                        )
                        .add("{wins}", player -> lobbyEngine.getPlayer(player)
                                .getStatistic().getWins()
                        )
                        .add("{defeats}", player -> lobbyEngine.getPlayer(player)
                                .getStatistic().getDefeats()
                        )
                        .add("{rating}", player -> lobbyEngine.getPlayer(player)
                                .getStatistic().getRating()
                        )
                        .add("{lobby_state}", () -> {
                            if (lobbyEngine.isStarting()) {
                                String formattedTimeLeft = SimpleTemporal.of(lobbyEngine.getSecondsLeft(),
                                        TimeUnit.SECONDS).format();
                                return msgConfig.get(Messages.lobby__starting_sidebar_state, formattedTimeLeft);
                            } else {
                                return msgConfig.get(Messages.lobby__waiting_sidebar_state).asSingleLine();
                            }
                        })
                        .add("{player_count}", lobbyEngine::getPlayerCount)
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