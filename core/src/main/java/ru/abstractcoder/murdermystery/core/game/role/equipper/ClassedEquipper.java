package ru.abstractcoder.murdermystery.core.game.role.equipper;

import org.bukkit.entity.Player;
import ru.abstractcoder.murdermystery.core.game.role.classed.RoleClass;

public class ClassedEquipper implements PlayerEquipper {

    private final RoleClass roleClass;

    public ClassedEquipper(RoleClass roleClass) {
        this.roleClass = roleClass;
    }

    @Override
    public void equip(Player player) {
        roleClass.template().populateInventory(player.getInventory());
    }

}
