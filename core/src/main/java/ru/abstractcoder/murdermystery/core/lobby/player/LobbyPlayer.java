package ru.abstractcoder.murdermystery.core.lobby.player;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.role.RoleTemplate;
import ru.abstractcoder.murdermystery.core.game.role.classed.RoleClass;
import ru.abstractcoder.murdermystery.core.statistic.PlayerStatistic;
import ru.abstractcoder.murdermystery.core.util.AbstractWrappedPlayer;

import java.util.Map;

public class LobbyPlayer extends AbstractWrappedPlayer {

    private RoleTemplate preferredRole;
    private final Map<GameRole.Type, RoleData> roleDataMap;
    private final PlayerStatistic statistic;

    private GameRole balancedRole;

    public LobbyPlayer(Player handle, Map<GameRole.Type, RoleData> roleDataMap, PlayerStatistic statistic) {
        super(handle);
        this.roleDataMap = roleDataMap;
        this.statistic = statistic;
    }

    public RoleTemplate getPreferredRole() {
        return preferredRole;
    }

    public void setPrederredRole(RoleTemplate prederredRole) {
        this.preferredRole = prederredRole;
    }

    public RoleData getRoleData(GameRole.Type type) {
        return roleDataMap.computeIfAbsent(type, (__) -> new RoleData());
    }

    public GameRole getBalancedRole() {
        return balancedRole;
    }

    public void setBalancedRole(GameRole balancedRole) {
        this.balancedRole = balancedRole;
    }

    public PlayerStatistic getStatistic() {
        return statistic;
    }

    public static class RoleData {

        private int chancePoints;
        @Nullable
        private RoleClass.Type selectedClassType;

        public RoleData(int chancePoints, @Nullable RoleClass.Type selectedClassType) {
            this.chancePoints = chancePoints;
            this.selectedClassType = selectedClassType;
        }

        public RoleData() {
        }

        public int getChancePoints() {
            return chancePoints;
        }

        public void incrementChancePoints() {
            chancePoints++;
        }

        public void resetChancePoints() {
            chancePoints = 0;
        }

        public boolean isClassTypeSelected() {
            return selectedClassType != null;
        }

        @Nullable
        public RoleClass.Type getSelectedClassType() {
            return selectedClassType;
        }

    }

}