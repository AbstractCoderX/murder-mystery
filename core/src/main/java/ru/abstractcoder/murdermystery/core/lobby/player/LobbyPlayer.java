package ru.abstractcoder.murdermystery.core.lobby.player;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import ru.abstractcoder.murdermystery.core.data.PlayerData;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.role.RoleTemplate;
import ru.abstractcoder.murdermystery.core.statistic.PlayerStatistic;
import ru.abstractcoder.murdermystery.core.util.AbstractWrappedPlayer;

//TODO extract interface
public class LobbyPlayer extends AbstractWrappedPlayer {

    private RoleTemplate preferredRole;
    @Nullable
    private GameRole balancedRole;

    private final PlayerData data;

    public LobbyPlayer(Player handle, PlayerData data) {
        super(handle);
        this.data = data;
    }

    public RoleTemplate getPreferredRole() {
        return preferredRole;
    }

    public void setPreferredRole(RoleTemplate preferredRole) {
        this.preferredRole = preferredRole;
    }

    public GameRole getBalancedRole() {
        Preconditions.checkState(balancedRole != null, "Role not balanced yet!");
        return balancedRole;
    }

    public void setBalancedRole(GameRole balancedRole) {
        this.balancedRole = Preconditions.checkNotNull(balancedRole, "balancedRole");
    }

    public PlayerData data() {
        return data;
    }

    public PlayerStatistic getStatistic() {
        return data.statistic();
    }

}