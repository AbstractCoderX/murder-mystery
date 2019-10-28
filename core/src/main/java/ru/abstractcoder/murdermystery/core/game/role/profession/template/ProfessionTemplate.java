package ru.abstractcoder.murdermystery.core.game.role.profession.template;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.abstractcoder.benioapi.item.ItemData;
import ru.abstractcoder.murdermystery.core.game.role.component.AbstractRoleComponentTemplate;
import ru.abstractcoder.murdermystery.core.game.role.icon.TemplateIcon;
import ru.abstractcoder.murdermystery.core.game.role.profession.Profession;
import ru.abstractcoder.murdermystery.core.game.role.skin.SkinResolver;

import java.util.Map;

public class ProfessionTemplate extends AbstractRoleComponentTemplate {

    private final Profession.Type type;

    @JsonCreator
    public ProfessionTemplate(
            Profession.Type type,
            @JsonProperty("items") Map<Integer, ItemData> itemDataMap,
            TemplateIcon icon, SkinResolver skinResolver) {
        super(itemDataMap, icon, skinResolver);
        this.type = type;
    }

    @Override
    public Profession.Type getType() {
        return type;
    }

}
