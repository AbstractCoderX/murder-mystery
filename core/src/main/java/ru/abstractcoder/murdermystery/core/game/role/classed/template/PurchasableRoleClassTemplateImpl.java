package ru.abstractcoder.murdermystery.core.game.role.classed.template;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.abstractcoder.benioapi.item.ItemData;
import ru.abstractcoder.murdermystery.core.game.role.icon.TemplateIcon;
import ru.abstractcoder.murdermystery.core.game.role.skin.SkinResolver;

import java.util.Map;

public class PurchasableRoleClassTemplateImpl extends SimpleRoleClassTemplate implements PurchasableRoleClassTemplate {

    private final int price;

    @JsonCreator
    public PurchasableRoleClassTemplateImpl(
            @JsonProperty("type") String typeKey,
            @JsonProperty("items") Map<Integer, ItemData> itemDataMap,
            TemplateIcon icon,
            int price, SkinResolver skinResolver) {
        super(typeKey, itemDataMap, icon, skinResolver);
        this.price = price;
    }

    @Override
    public int getPrice() {
        return price;
    }

}