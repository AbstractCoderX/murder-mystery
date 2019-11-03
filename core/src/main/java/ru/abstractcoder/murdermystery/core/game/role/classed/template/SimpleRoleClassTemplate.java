package ru.abstractcoder.murdermystery.core.game.role.classed.template;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import ru.abstractcoder.benioapi.item.ItemData;
import ru.abstractcoder.benioapi.util.LazyFunction;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.role.classed.RoleClass;
import ru.abstractcoder.murdermystery.core.game.role.component.AbstractRoleComponentTemplate;
import ru.abstractcoder.murdermystery.core.game.role.icon.TemplateIcon;
import ru.abstractcoder.murdermystery.core.game.role.skin.SkinResolver;

import java.util.Map;

public class SimpleRoleClassTemplate extends AbstractRoleComponentTemplate implements RoleClassTemplate {

    private final LazyFunction<GameRole.Type, RoleClass.Type> type;

    @JsonCreator
    public SimpleRoleClassTemplate(
            @JsonProperty("type") String typeKey,
            @JsonProperty("items") Map<Integer, ItemData> itemDataMap,
            TemplateIcon icon, SkinResolver skinResolver) {
        super(itemDataMap, icon, skinResolver);
        this.type = LazyFunction.create(roleType -> RoleClass.TypeResolver.resolve(roleType, typeKey));
    }

    public void initType(GameRole.Type roleType) {
        Preconditions.checkState(type.initialize(roleType), "Type already initialized!");
    }

    @Override
    public RoleClass.Type getType() {
        return type.get();
    }

}