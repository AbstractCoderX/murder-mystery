package ru.abstractcoder.murdermystery.core.lobby;

import dagger.Reusable;
import org.bukkit.entity.Player;
import ru.abstractcoder.benioapi.config.msg.Message;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.benioapi.util.NumberUtils;
import ru.abstractcoder.benioapi.util.temporal.SimpleTemporal;
import ru.abstractcoder.benioapi.util.ticking.Ticking;
import ru.abstractcoder.murdermystery.core.config.GeneralConfig;
import ru.abstractcoder.murdermystery.core.config.Messages;
import ru.abstractcoder.murdermystery.core.game.GameEngine;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.role.chance.ComputingRoleBalancer;
import ru.abstractcoder.murdermystery.core.lobby.settings.StartingSettings;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Reusable
public class LobbyTicking implements Ticking {

    private final GeneralConfig.Lobby lobby;
    private final LobbyEngine lobbyEngine;
    private final GameEngine gameEngine;
    private final MsgConfig<Messages> msgConfig;
    private final ComputingRoleBalancer roleBalancer;

    @Inject
    public LobbyTicking(
            LobbyEngine lobbyEngine, GameEngine gameEngine,
            GeneralConfig.Lobby lobby, MsgConfig<Messages> msgConfig,
            ComputingRoleBalancer roleBalancer) {
        this.lobby = lobby;
        this.lobbyEngine = lobbyEngine;
        this.gameEngine = gameEngine;
        this.msgConfig = msgConfig;
        this.roleBalancer = roleBalancer;
    }

    @Override
    public boolean doTick() {
        if (lobbyEngine.getPlayerCount() > 1) {
            roleBalancer.recompute();
            lobbyEngine.getPlayers().forEach(lobbyPlayer -> {
                Function<GameRole.Type, String> chanceResolver = type -> {
                    double chancePercent = roleBalancer.getRoleChance(lobbyPlayer, type) * 100;
                    return NumberUtils.formatDouble(chancePercent, 1);
                };
                msgConfig.get(Messages.game__murder_and_detective_chance,
                        chanceResolver.apply(GameRole.Type.MURDER),
                        chanceResolver.apply(GameRole.Type.DETECTIVE)
                ).sendActionBar(lobbyPlayer);
            });
        }

        if (lobbyEngine.isStarting()) {
            int timeLeft = lobbyEngine.decrementTimeLeft();

            if (timeLeft == 0) {
                lobbyEngine.shutdown();
                roleBalancer.applyRoles();
                gameEngine.startGame(lobbyEngine.getPlayers());
                return true;
            }
            String timeLeftFormatted = SimpleTemporal.of(timeLeft, TimeUnit.SECONDS).format();
            Message startingBossBarMsg = msgConfig.get(Messages.lobby__starting_bossbar, timeLeftFormatted);
            StartingSettings startingSettings = lobby.starting();

            lobbyEngine.getBossBar().setTitle(startingBossBarMsg.asSingleLine());
            lobbyEngine.getBossBar().setProgress(((double) timeLeft) / startingSettings.getMinCountStartTime());


            boolean isNotifyingPeriod = timeLeft % startingSettings.getStandartNotifyingPeriod() == 0;
            boolean isPerSecondNotifyingTime = timeLeft <= startingSettings.getPerSecondNotifyingTime();
            if (isNotifyingPeriod || isPerSecondNotifyingTime) {
                List<Player> players = lobby.getWorld().getPlayers();

                startingSettings.getPerSecondNitifyingSound().broadcast(players);
                msgConfig.get(Messages.lobby__starting_chat, timeLeftFormatted)
                        .broadcastSession().setPlayers(players).broadcastChat();

                if (isPerSecondNotifyingTime) {
                    msgConfig.get(Messages.lobby__starting_title, timeLeftFormatted)
                            .broadcastSession().setPlayers(players).broadcastTitle();
                }
            }
        }
        return false;
    }

    @Override
    public int getPeriod() {
        return 20;
    }

}