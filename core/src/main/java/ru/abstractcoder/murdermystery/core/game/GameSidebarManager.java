package ru.abstractcoder.murdermystery.core.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.abstractcoder.benioapi.board.SidebarService;
import ru.abstractcoder.benioapi.board.sidebar.SidebarTemplate;
import ru.abstractcoder.benioapi.board.text.updater.PlaceholderTextUpdater;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.murdermystery.core.config.Msg;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;

import javax.inject.Inject;

public class GameSidebarManager {

    private SidebarService sidebarService;
    private final SidebarTemplate sidebarTemplate;

    @Inject
    public GameSidebarManager(SidebarService sidebarService, GameEngine gameEngine,
            MsgConfig<Msg> msgConfig) {
        this.sidebarService = sidebarService;

        sidebarTemplate = gameEngine.settings().getSidebarTemplate()
                .withLineUpdater(new PlaceholderTextUpdater()
                        .prepare(GamePlayer.class, gameEngine.getPlayerResolver()::resolve)
                        .add("{role}", GamePlayer.class, gamePlayer -> {
                            if (gamePlayer == null) {
                                return msgConfig.get(Msg.misc__spectator).asSingleLine();
                            }
                            if (!gamePlayer.isRoleShowed()) {
                                return "?";
                            }
                            return gamePlayer.getRole().getDisplayName();

                        })
                        .add("{survivors_amount}", () -> gameEngine.getPlayerResolver()
                                .getSurvivors().size()
                        )
                        .add("{detective_state}", () -> msgConfig
                                .get(Msg.lifeState(gameEngine.getPlayerResolver().getDetective() != null))
                                .asSingleLine()
                        )
                        .add("{time}", gameEngine.getGameTime()::getFormattedTimeLeft)
                );
    }

    public void showFor(Player player) {
        sidebarService.createBukkitSidebar(player, sidebarTemplate);
    }

    public void showForAll() {
        Bukkit.getOnlinePlayers().forEach(this::showFor);
    }

    public void unshowForAll() {
        Bukkit.getOnlinePlayers().forEach(this::unshowFor);
    }

    public void unshowFor(Player player) {
        sidebarService.removeSidebar(player);
    }

}
