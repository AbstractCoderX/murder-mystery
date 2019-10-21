package ru.abstractcoder.murdermystery.core.game.role.classed.template;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import ru.abstractcoder.benioapi.item.ItemData;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.role.classed.RoleClass;
import ru.abstractcoder.murdermystery.core.game.role.component.AbstractRoleComponentTemplate;
import ru.abstractcoder.murdermystery.core.game.role.icon.TemplateIcon;
import ru.abstractcoder.murdermystery.core.game.role.skin.SkinResolver;

import java.util.Map;

public class SimpleRoleClassTemplate extends AbstractRoleComponentTemplate implements RoleClassTemplate {

    @JsonProperty("type")
    private String tempTypeKey;
    @JsonIgnore
    private RoleClass.Type type;

    @JsonCreator
    public SimpleRoleClassTemplate(
            String type,
            @JsonProperty("items") Map<Integer, ItemData> itemDataMap,
            TemplateIcon icon, SkinResolver skinResolver) {
        super(itemDataMap, icon, skinResolver);
        this.tempTypeKey = type;
    }

    public void initType(GameRole.Type roleType) {
        Preconditions.checkState(this.type == null, "Type already initialized!");
        this.type = RoleClass.TypeResolver.resolve(roleType, tempTypeKey);
    }

    @Override
    public RoleClass.Type getType() {
        return type;
    }

    @Override
    public GameRole.Type roleType() {
        return type.roleType();
    }

}