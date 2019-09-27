package ru.abstractcoder.murdermystery.core.game.role.classed;

import ru.abstractcoder.murdermystery.core.game.role.AbstractRole;
import ru.abstractcoder.murdermystery.core.game.role.RoleTemplate;
import ru.abstractcoder.murdermystery.core.game.role.equipper.ClassedEquipper;
import ru.abstractcoder.murdermystery.core.game.role.logic.RoleLogic;
import ru.abstractcoder.murdermystery.core.game.role.logic.RoleLogicCreator;

public class ClassedRole extends AbstractRole {

    private final RoleClass roleClass;
    private final Type roleType;

    public ClassedRole(RoleTemplate template, RoleClass roleClass, Type roleType) {
        super(template, new ClassedEquipper(roleClass));
        this.roleClass = roleClass;
        this.roleType = roleType;
    }

    public RoleClass getRoleClass() {
        return roleClass;
    }

    @Override
    public Type getType() {
        return roleType;
    }

    @Override
    protected RoleLogicCreator getRoleLogicCreator() {
        return roleClass;
    }

}