package ru.abstractcoder.murdermystery.core.game.role.classed;

import ru.abstractcoder.murdermystery.core.game.role.AbstractRole;
import ru.abstractcoder.murdermystery.core.game.role.RoleTemplate;
import ru.abstractcoder.murdermystery.core.game.role.component.RoleComponent;
import ru.abstractcoder.murdermystery.core.game.role.equipper.ClassedEquipper;

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
    protected RoleComponent getComponent() {
        return roleClass;
    }

}