package ru.abstractcoder.murdermystery.core.game.role.component;

import ru.abstractcoder.murdermystery.core.game.role.icon.IconableTemplate;
import ru.abstractcoder.murdermystery.core.game.role.skin.SkinnableTemplate;
import ru.abstractcoder.murdermystery.core.game.role.stored.StoredTemplate;

public interface RoleComponentTemplate extends StoredTemplate, IconableTemplate, SkinnableTemplate {

    RoleComponent.Type getType();

}
