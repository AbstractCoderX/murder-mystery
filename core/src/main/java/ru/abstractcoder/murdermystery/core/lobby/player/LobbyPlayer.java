package ru.abstractcoder.murdermystery.core.lobby.player;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import ru.abstractcoder.benioapi.util.primitive.IntWrapper;
import ru.abstractcoder.murdermystery.core.data.PlayerData;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.role.RoleTemplate;
import ru.abstractcoder.murdermystery.core.statistic.PlayerStatistic;
import ru.abstractcoder.murdermystery.core.util.AbstractWrappedPlayer;

import java.util.Map;

public class LobbyPlayer extends AbstractWrappedPlayer {

    private RoleTemplate preferredRole;
    @Nullable
    private GameRole balancedRole;

    private final PlayerData data;

    private final Map<GameRole.Type, IntWrapper> rolePoints = Maps.newEnumMap(GameRole.Type.class);

    public LobbyPlayer(Player handle, PlayerData data) {
        super(handle);
        this.data = data;

        for (GameRole.Type classedType : GameRole.Type.CLASSED_TYPES) {
            int chancePoints = data.getClassedRoleData(classedType).getChancePoints();
            IntWrapper points = IntWrapper.create(chancePoints);
            rolePoints.put(classedType, points);
        }
    }

    public RoleTemplate getPreferredRole() {
        return preferredRole;
    }

    public boolean hasPreferredRole() {
        return preferredRole != null;
    }

    public void setPreferredRole(RoleTemplate preferredRole) {
        Preconditions.checkArgument(preferredRole != this.preferredRole,
                "attempt to set same preferredRole: %s", preferredRole);
        unsetPreferredRole();
        this.preferredRole = preferredRole;

        rolePoints.forEach((type, points) -> {
            if (type == preferredRole.getType()) {
                points.getAndAdd(2);
            } else {
                points.getAndAdd(-1);
            }
        });
    }

    public void unsetPreferredRole() {
        if (hasPreferredRole()) {
            rolePoints.forEach((type, points) -> {
                if (type == preferredRole.getType()) {
                    points.getAndAdd(-2);
                } else {
                    points.getAndAdd(1);
                }
            });
            preferredRole = null;
        }
    }

    public GameRole getBalancedRole() {
        Preconditions.checkState(balancedRole != null, "Role not balanced yet!");
        return balancedRole;
    }

    public void setBalancedRole(GameRole balancedRole) {
        this.balancedRole = Preconditions.checkNotNull(balancedRole, "balancedRole");
    }

    public int getRolePoints(GameRole.Type roleType) {
        return rolePoints.get(roleType).get();
    }

    public PlayerData data() {
        return data;
    }

    public PlayerStatistic getStatistic() {
        return data.statistic();
    }

}