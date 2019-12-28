package ru.abstractcoder.murdermystery.core.game.role.chance;

import dagger.Reusable;
import ru.abstractcoder.benioapi.util.probable.AbstractProbable;
import ru.abstractcoder.benioapi.util.probable.Probability;
import ru.abstractcoder.benioapi.util.probable.ProbableList;
import ru.abstractcoder.murdermystery.core.data.ClassedRoleData;
import ru.abstractcoder.murdermystery.core.game.GameEngine;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.role.RoleResolver;
import ru.abstractcoder.murdermystery.core.game.role.RoleTemplate;
import ru.abstractcoder.murdermystery.core.game.role.RoleTemplateResolver;
import ru.abstractcoder.murdermystery.core.game.role.classed.RoleClass;
import ru.abstractcoder.murdermystery.core.game.role.classed.template.RoleClassTemplateResolver;
import ru.abstractcoder.murdermystery.core.game.role.profession.Profession;
import ru.abstractcoder.murdermystery.core.lobby.player.LobbyPlayer;
import ru.abstractcoder.murdermystery.core.lobby.player.LobbyPlayerResolver;

import javax.inject.Inject;
import java.util.ArrayDeque;
import java.util.Deque;

@Reusable
public class ComputingRoleBalancer {

    // public static final int DEFAULT_PLAYER_POINTS = 10;
    private final LobbyPlayerResolver playerResolver;
    private final RoleTemplateResolver roleTemplateResolver;
    private final RoleClassTemplateResolver roleClassTemplateResolver;
    private final RoleResolver roleResolver;

    private double onePartChance;
    private double commonChance = 1.0;

    private int commonAdditionalPoints;

    @Inject
    public ComputingRoleBalancer(LobbyPlayerResolver playerResolver, GameEngine gameEngine) {
        this.playerResolver = playerResolver;
        this.roleTemplateResolver = gameEngine.settings().getRoleTemplateResolver();
        this.roleClassTemplateResolver = gameEngine.settings().getRoleClassTemplateResolver();
        this.roleResolver = gameEngine.getRoleResolver();
    }

    @Deprecated
    public void recompute() {
        commonAdditionalPoints = playerResolver.getPlayers().stream()
                .flatMapToInt(lobbyPlayer -> roleTemplateResolver.getExtraPointableTemplates().stream()
                        .map(RoleTemplate::getType)
                        .mapToInt(lobbyPlayer::getRolePoints)
                )
                .sum();
        // for (LobbyPlayer player : playerResolver.getPlayers()) {
        //     for (RoleTemplate roleTemplate : roleTemplateResolver.getExtraPointableTemplates()) {
        //         int rolePoints = player.getRolePoints(roleTemplate.getType());
        //     }
        // }

        // onePartChance = 1.0 / playerResolver.getPlayerCount();
        //
        // commonChance = 1.0;
        // roleTemplateResolver.getAll().stream()
        //         .filter(RoleTemplate::isExtraPointable)
        //         .forEach(roleTemplate -> playerResolver.getPlayers().forEach(lobbyPlayer -> {
        //             int chancePoints = lobbyPlayer.data().getClassedRoleData(roleTemplate.getType()).getChancePoints();
        //             commonChance += probabilityFromPoints(chancePoints);
        //         }));
    }

    public double getRoleChance(LobbyPlayer lobbyPlayer, GameRole.Type roleType) {
        // int totalDefaultPoints = playerResolver.getPlayerCount() * DEFAULT_PLAYER_POINTS;
        // int totalAdditionalPoints = playerResolver.getPlayers().stream()
        //         .mapToInt(lp -> lp.getRolePoints(roleType))
        //         .sum();
        // int totalPoints = totalDefaultPoints + totalAdditionalPoints;

        // return ((double) (DEFAULT_PLAYER_POINTS + rolePoints)) / totalPoints;

        int rolePoints = lobbyPlayer.getRolePoints(roleType);
        double averageRolePoints = playerResolver.getPlayers().stream()
                .mapToInt(lp -> lp.getRolePoints(roleType))
                .average()
                .orElseThrow(IllegalStateException::new);
        double devitationFromAverage = rolePoints - averageRolePoints;

        double defChance = 1.0 / playerResolver.getPlayerCount();

        return defChance + (devitationFromAverage / 10);

        // int roleChancePoints = lobbyPlayer.data().getClassedRoleData(roleType).getChancePoints();
        // double probability = onePartChance + probabilityFromPoints(roleChancePoints);
        // return probability / commonChance;
    }

    // private double probabilityFromPoints(int points) {
    //     return (points * 2) / 100.0;
    // }

    public void applyRoles() {
        // this.recompute();
        Deque<LobbyPlayer> playerDeque = new ArrayDeque<>(playerResolver.getPlayers());

        for (var type : GameRole.Type.CLASSED_TYPES) {
            ProbableList<PlayerProbable> playerProbables = new ProbableList<>();
            playerDeque.forEach(lobbyPlayer -> {
                Probability probability = Probability.fromValue(getRoleChance(lobbyPlayer, type));
                PlayerProbable playerProbable = new PlayerProbable(lobbyPlayer, probability);
                playerProbables.add(playerProbable);
            });

            LobbyPlayer selectedPlayer = playerProbables.getRandomly().getLobbyPlayer();

            ClassedRoleData classedRoleData = selectedPlayer.data().getClassedRoleData(type);
            RoleClass.Type classType = classedRoleData.isClassTypeSelected()
                                       ? classedRoleData.getSelectedClassType()
                                       : roleClassTemplateResolver.getDefaultTemplate(type).getType();

            RoleTemplate roleTemplate = roleTemplateResolver.getByType(type);

            GameRole gameRole = roleResolver.resolveClassedRole(classType, roleTemplate);
            selectedPlayer.setBalancedRole(gameRole);
            playerDeque.remove(selectedPlayer);
        }

        for (Profession.Type type : Profession.Type.VALUES) {
            LobbyPlayer lobbyPlayer = playerDeque.poll();
            if (lobbyPlayer == null) {
                break;
            }

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