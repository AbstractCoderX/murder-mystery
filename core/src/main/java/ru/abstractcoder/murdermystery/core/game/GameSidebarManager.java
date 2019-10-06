package ru.abstractcoder.murdermystery.core.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.abstractcoder.benioapi.board.SidebarService;
import ru.abstractcoder.benioapi.board.sidebar.SidebarTemplate;
import ru.abstractcoder.benioapi.board.text.updater.PlaceholderTextUpdater;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.murdermystery.core.config.Messages;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;

public class GameSidebarManager {

    private SidebarService sidebarService;
    private final SidebarTemplate sidebarTemplate;

    public GameSidebarManager(SidebarService sidebarService, GameEngine gameEngine,
            MsgConfig<Messages> msgConfig) {
        this.sidebarService = sidebarService;

        int[] timeLeft = {gameEngine.settings().general().getGameDuration()};
        sidebarTemplate = gameEngine.settings().getSidebarTemplate()
                .withLineUpdater(new PlaceholderTextUpdater()
                        .add("{role}", player -> {
                            GamePlayer gamePlayer = gameEngine.getPlayerResolver().resolve(player);
                            if (gamePlayer == null) {
                                return msgConfig.get(Messages.misc__spectator).asSingleLine();
                            }
                            if (!gamePlayer.isRoleShowed()) {
                                return "?";
                            }
                            return gamePlayer.getRole().getDisplayName();

                        })
                        .add("{survivors_amount}", () -> gameEngine.getPlayerResolver()
                                .getSurvivors().size()
                        )
                        .add("{detective_state}", () -> msgConfig.get(
                                gameEngine.getPlayerResolver().getDetective() == null
                                ? Messages.misc__died
                                : Messages.misc__alive
                                ).asSingleLine()
                        )
                        .add("{time}", () -> timeLeft[0]--)
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
