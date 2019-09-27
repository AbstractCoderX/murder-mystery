package ru.abstractcoder.murdermystery.core.game.player;

import org.bukkit.entity.Player;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.role.logic.RoleLogic;
import ru.abstractcoder.murdermystery.core.game.skin.SkinContainable;
import ru.abstractcoder.murdermystery.core.game.skin.SkinContainer;
import ru.abstractcoder.murdermystery.core.util.AbstractWrappedPlayer;

import java.util.UUID;

public class GamePlayer extends AbstractWrappedPlayer implements SkinContainable {

    private GameRole role;
    private SkinContainer skinContainer;
    private boolean roleShowed = false;

    public GamePlayer(Player player, GameRole role, SkinContainer skinContainer) {
        super(player);
        this.role = role;
        this.skinContainer = skinContainer;
    }

    public GameRole getRole() {
        return role;
    }

    public void setRole(GameRole role) {
        role.initLogic(this);
        this.role = role;
    }

    @Override
    public UUID getUniqueId() {
        return handle.getUniqueId();
    }

    @Override
    public SkinContainer getSkinContainer() {
        return skinContainer;
    }

    @Override
    public void setSkinContainer(SkinContainer skinContainer) {
        this.skinContainer = skinContainer;
    }

    public boolean isRoleShowed() {
        return roleShowed;
    }

    public void setRoleShowed(boolean roleShowed) {
        this.roleShowed = roleShowed;
    }

    public RoleLogic getRoleLogic() {
        return role.getLogic(this);
    }

}