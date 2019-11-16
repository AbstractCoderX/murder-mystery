package ru.abstractcoder.murdermystery.core.game.role.chance;

import ru.abstractcoder.benioapi.util.probable.AbstractProbable;
import ru.abstractcoder.benioapi.util.probable.Probability;
import ru.abstractcoder.benioapi.util.probable.ProbableList;
import ru.abstractcoder.murdermystery.core.game.GameEngine;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.role.RoleResolver;
import ru.abstractcoder.murdermystery.core.game.role.RoleTemplate;
import ru.abstractcoder.murdermystery.core.game.role.RoleTemplateResolver;
import ru.abstractcoder.murdermystery.core.game.role.classed.RoleClass;
import ru.abstractcoder.murdermystery.core.game.role.classed.template.RoleClassTemplateResolver;
import ru.abstractcoder.murdermystery.core.game.role.profession.Profession;
import ru.abstractcoder.murdermystery.core.lobby.LobbyEngine;
import ru.abstractcoder.murdermystery.core.lobby.player.LobbyPlayer;

import java.util.ArrayDeque;
import java.util.Deque;

public class ComputingRoleBalancer {

    private final LobbyEngine lobbyEngine;
    private final RoleTemplateResolver roleTemplateResolver;
    private final RoleClassTemplateResolver roleClassTemplateResolver;
    private final RoleResolver roleResolver;

    private double onePartChance;
    private double commonChance = 1.0;

    public ComputingRoleBalancer(LobbyEngine lobbyEngine, GameEngine gameEngine) {
        this.lobbyEngine = lobbyEngine;
        this.roleTemplateResolver = gameEngine.settings().getRoleTemplateResolver();
        this.roleClassTemplateResolver = gameEngine.settings().getRoleClassTemplateResolver();
        this.roleResolver = gameEngine.getRoleResolver();
    }

    public void recompute() {
        onePartChance = 1.0 / lobbyEngine.getPlayerCount();

        commonChance = 1.0;
        roleTemplateResolver.getAll().stream()
                .filter(RoleTemplate::isExtraPointable)
                .forEach(roleTemplate -> lobbyEngine.getPlayers().forEach(lobbyPlayer -> {
                    int chancePoints = lobbyPlayer.getClassedRoleData(roleTemplate.getType()).getChancePoints();
                    commonChance += probabilityFromPoints(chancePoints);
                }));
    }

    public double getRoleChance(LobbyPlayer lobbyPlayer, GameRole.Type roleType) {
        int roleChancePoints = lobbyPlayer.getClassedRoleData(roleType).getChancePoints();
        double probability = onePartChance + probabilityFromPoints(roleChancePoints);
        return probability / commonChance;
    }

    private double probabilityFromPoints(int points) {
        return (points * 2) / 100.0;
    }

    public void applyRoles() {
        this.recompute();
        Deque<LobbyPlayer> playerDeque = new ArrayDeque<>(lobbyEngine.getPlayers());

        GameRole.Type.CLASSED_TYPES.forEach(type -> {
            ProbableList<PlayerProbable> playerProbables = new ProbableList<>();
            lobbyEngine.getPlayers().forEach(lobbyPlayer -> {
                Probability probability = Probability.fromValue(getRoleChance(lobbyPlayer, type));
                PlayerProbable playerProbable = new PlayerProbable(lobbyPlayer, probability);
                playerProbables.add(playerProbable);
            });

            LobbyPlayer selectedPlayer = playerProbables.getRandomly().getLobbyPlayer();

            LobbyPlayer.ClassedRoleData classedRoleData = selectedPlayer.getClassedRoleData(type);
            RoleClass.Type classType = classedRoleData.isClassTypeSelected()
                                       ? classedRoleData.getSelectedClassType()
                                       : roleClassTemplateResolver.getDefaultTemplate(type).getType();

            RoleTemplate roleTemplate = roleTemplateResolver.getByType(type);

            GameRole gameRole = roleResolver.resolveClassedRole(classType, roleTemplate);
            selectedPlayer.setBalancedRole(gameRole);
            playerDeque.remove(selectedPlayer);
        });

        for (Profession.Type type : Profession.Type.VALUES) {
            LobbyPlayer lobbyPlayer = playerDeque.remove();

            GameRole gameRole = roleResolver.resolveCivilianRole(type);
            lobbyPlayer.setBalancedRole(gameRole);
        }
    }

    private static class PlayerProbable extends AbstractProbable {

        private final LobbyPlayer lobbyPlayer;

        private PlayerProbable(LobbyPlayer lobbyPlayer, Probability probability) {
            super(probability);
            this.lobbyPlayer = lobbyPlayer;
        }

        private LobbyPlayer getLobbyPlayer() {
            return lobbyPlayer;
        }

    }

}