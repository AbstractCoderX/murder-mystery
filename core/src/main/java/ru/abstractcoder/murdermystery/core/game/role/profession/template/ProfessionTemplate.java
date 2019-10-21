package ru.abstractcoder.murdermystery.core.game.role.profession.template;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.abstractcoder.benioapi.item.ItemData;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.role.component.AbstractRoleComponentTemplate;
import ru.abstractcoder.murdermystery.core.game.role.icon.TemplateIcon;
import ru.abstractcoder.murdermystery.core.game.role.skin.SkinResolver;

import java.util.Map;

public class ProfessionTemplate extends AbstractRoleComponentTemplate {

    @JsonCreator
    public ProfessionTemplate(
            @JsonProperty("items") Map<Integer, ItemData> itemDataMap,
            TemplateIcon icon, SkinResolver skinResolver) {
        super(itemDataMap, icon, skinResolver);
    }

    @Override
    public GameRole.Type roleType() {
        return GameRole.Type.CIVILIAN;
    }

}
