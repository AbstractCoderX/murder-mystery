package ru.abstractcoder.murdermystery.core.game.role.murder;

import org.bukkit.inventory.ItemStack;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.murdermystery.core.config.Messages;
import ru.abstractcoder.murdermystery.core.game.GameEngine;
import ru.abstractcoder.murdermystery.core.game.misc.SharedConstants;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.role.classed.AbstractRoleClass;
import ru.abstractcoder.murdermystery.core.game.role.classed.RoleClass;
import ru.abstractcoder.murdermystery.core.game.role.classed.template.RoleClassTemplate;

public abstract class MurderRoleClass extends AbstractRoleClass {

    protected MurderRoleClass(RoleClassTemplate template, GameEngine gameEngine, MsgConfig<Messages> msgConfig) {
        super(template, gameEngine, msgConfig);
    }

    public ItemStack getWeaponItem() {
        return template.getItem(SharedConstants.WEAPON_SLOT);
    }

    public enum Type implements RoleClass.Type {
        JACK,
        AUGUST_MUSIR,
        GRAVE_NICHOLAS,
        WOLF,
        VOODOO_DOLL

        ;

        @Override
        public GameRole.Type getRoleType() {
            return GameRole.Type.MURDER;
        }

    }

}