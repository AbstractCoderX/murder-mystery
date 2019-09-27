package ru.abstractcoder.murdermystery.core.game.role.classed.template;

import ru.abstractcoder.murdermystery.core.game.role.classed.RoleClass;
import ru.abstractcoder.murdermystery.core.game.role.stored.StoredTemplate;

public interface RoleClassTemplate extends StoredTemplate {

    RoleClass.Type getType();

    String getName();

}