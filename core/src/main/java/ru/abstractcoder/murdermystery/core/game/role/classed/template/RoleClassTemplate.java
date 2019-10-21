package ru.abstractcoder.murdermystery.core.game.role.classed.template;

import ru.abstractcoder.murdermystery.core.game.role.classed.RoleClass;
import ru.abstractcoder.murdermystery.core.game.role.component.RoleComponentTemplate;

public interface RoleClassTemplate extends RoleComponentTemplate {

    RoleClass.Type getType();

}