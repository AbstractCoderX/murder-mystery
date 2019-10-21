package ru.abstractcoder.murdermystery.core.game.role.component;

import ru.abstractcoder.benioapi.item.ItemData;
import ru.abstractcoder.murdermystery.core.game.role.icon.TemplateIcon;
import ru.abstractcoder.murdermystery.core.game.role.skin.SkinResolver;
import ru.abstractcoder.murdermystery.core.game.role.stored.AbstractStoredTemplate;

import java.util.Map;

public abstract class AbstractRoleComponentTemplate extends AbstractStoredTemplate implements RoleComponentTemplate {

    private final TemplateIcon icon;
    private final SkinResolver skinResolver;

    protected AbstractRoleComponentTemplate(Map<Integer, ItemData> itemDataMap, TemplateIcon icon, SkinResolver skinResolver) {
        super(itemDataMap);
        this.icon = icon;
        this.skinResolver = skinResolver;
    }

    @Override
    public TemplateIcon getIcon() {
        return icon;
    }

    @Override
    public SkinResolver getSkinResolver() {
        return skinResolver;
    }

}
