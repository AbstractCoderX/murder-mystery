package ru.abstractcoder.murdermystery.core.game.role.classed.template;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import ru.abstractcoder.benioapi.item.ItemData;
import ru.abstractcoder.benioapi.util.ColorUtils;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.role.classed.RoleClass;
import ru.abstractcoder.murdermystery.core.game.role.stored.AbstractStoredTemplate;

import java.util.Map;
import java.util.Objects;

public class DefaultRoleClassTemplate extends AbstractStoredTemplate implements RoleClassTemplate {

    @JsonProperty("type")
    private String tempTypeKey;
    @JsonIgnore
    private RoleClass.Type type;
    private final String name;

    @JsonCreator
    public DefaultRoleClassTemplate(
            String type, String name,
            @JsonProperty("items") Map<Integer, ItemData> itemDataMap) {
        super(itemDataMap);
        this.tempTypeKey = type;
        this.name = ColorUtils.color(name);
    }

    @Override
    public RoleClass.Type getType() {
        return type;
    }

    public void initType(GameRole.Type roleType) {
        Preconditions.checkState(this.type == null, "Type already initialized!");
        this.type = RoleClass.TypeResolver.resolve(roleType, tempTypeKey);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DefaultRoleClassTemplate)) return false;
        DefaultRoleClassTemplate that = (DefaultRoleClassTemplate) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}