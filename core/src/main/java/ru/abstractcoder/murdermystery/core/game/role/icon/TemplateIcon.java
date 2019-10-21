package ru.abstractcoder.murdermystery.core.game.role.icon;

import com.fasterxml.jackson.annotation.JsonCreator;
import ru.abstractcoder.benioapi.item.ItemData;
import ru.abstractcoder.benioapi.util.ColorUtils;

public class TemplateIcon implements ItemData.Imposible {

    private final String name;
    private final String skull;

    @JsonCreator
    public TemplateIcon(String name, String skull) {
        this.name = ColorUtils.color(name);
        this.skull = skull;
    }

    public String getName() {
        return name;
    }

    @Override
    public void impose(ItemData itemData) {
        itemData
                .nameReplacements(s -> s.replace("{name}", name))
                .skullOwnerReplacements(s -> s.replace("{skull}", skull));
    }

}
