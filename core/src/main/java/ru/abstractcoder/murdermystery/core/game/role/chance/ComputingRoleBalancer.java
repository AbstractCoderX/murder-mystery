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

import java.util.ArrayList;
import java.util.List;

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
                    int chancePoints = lobbyPlayer.getRoleData(roleTemplate.getType()).getChancePoints();
                    commonChance += probabilityFromPoints(chancePoints);
                }));
    }

    public double getRoleChance(LobbyPlayer lobbyPlayer, GameRole.Type roleType) {
        int roleChancePoints = lobbyPlayer.getRoleData(roleType).getChancePoints();
        double probability = onePartChance + probabilityFromPoints(roleChancePoints);
        return probability / commonChance;
    }

    private double probabilityFromPoints(int points) {
        return (points * 2) / 100.0;
    }

    public void applyRoles() {
        this.recompute();
        List<LobbyPlayer> players = new ArrayList<>(lobbyEngine.getPlayers());

        roleTemplateResolver.getAll().stream()
                .filter(RoleTemplate::isExtraPointable)
                .map(RoleTemplate::getType)
                .forEach(type -> {
                    ProbableList<PlayerProbable> playerProbables = new ProbableList<>();
                    players.forEach(lobbyPlayer -> {
                        Probability probability = Probability.fromValue(getRoleChance(lobbyPlayer, type));
                        PlayerProbable playerProbable = new PlayerProbable(lobbyPlayer, probability);
                        playerProbables.add(playerProbable);
                    });

                    LobbyPlayer selectedPlayer = playerProbables.getRandomly().getLobbyPlayer();

                    LobbyPlayer.RoleData roleData = selectedPlayer.getRoleData(type);
                    RoleClass.Type classType = roleData.isClassTypeSelected()
                                               ? roleData.getSelectedClassType()
                                               : roleClassTemplateResolver.getDefaultTemplate(type).getType();

                    RoleTemplate roleTemplate = roleTemplateResolver.getByType(type);

                    GameRole gameRole = roleResolver.resolveClassedRole(classType, roleTemplate);
                    selectedPlayer.setBalancedRole(gameRole);
                    players.remove(selectedPlayer);
                });

        RoleTemplate civilanTemplate = roleTemplateResolver.getByType(GameRole.Type.CIVILIAN);
        GameRole defaultRole = null;
        for (Profession.Type type : Profession.Type.values()) {
            LobbyPlayer lobbyPlayer = players.remove(0);

            GameRole gameRole;
            if (type == Profession.Type.DEFAULT) {
                if (defaultRole == null) {
                    defaultRole = roleResolver.resolveCivilianRole(type, civilanTemplate);
                }
                gameRole = defaultRole;
            } else {
                gameRole = roleResolver.resolveCivilianRole(type, civilanTemplate);
            }

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