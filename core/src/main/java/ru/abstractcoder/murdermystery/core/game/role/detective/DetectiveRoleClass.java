package ru.abstractcoder.murdermystery.core.game.role.detective;

import org.bukkit.inventory.ItemStack;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.murdermystery.core.config.Messages;
import ru.abstractcoder.murdermystery.core.game.GameEngine;
import ru.abstractcoder.murdermystery.core.game.misc.SharedConstants;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.role.classed.AbstractRoleClass;
import ru.abstractcoder.murdermystery.core.game.role.classed.RoleClass;
import ru.abstractcoder.murdermystery.core.game.role.classed.template.RoleClassTemplate;

public abstract class DetectiveRoleClass extends AbstractRoleClass {

    protected DetectiveRoleClass(RoleClassTemplate template, GameEngine gameEngine, MsgConfig<Messages> msgConfig) {
        super(template, gameEngine, msgConfig);
    }

    public ItemStack getBowItem() {
        return template.getItem(SharedConstants.WEAPON_SLOT);
    }

    public enum Type implements RoleClass.Type {

        SHERLOCK_GNOMES,
        BENEDICT_CYBERSLOTZ,
        MISS_PURPLE,
        PAVEL_PINKERTON,
        EL_TERMU,

        ;

        Type() {
            TypeResolver.register(this);
        }

        @Override
        public GameRole.Type roleType() {
            return GameRole.Type.DETECTIVE;
        }

        @Override
        public String key() {
            return name();
        }
    }

}