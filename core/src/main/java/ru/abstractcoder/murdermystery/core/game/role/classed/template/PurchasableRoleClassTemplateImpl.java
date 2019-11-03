package ru.abstractcoder.murdermystery.core.game.role.classed.template;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.abstractcoder.benioapi.item.ItemData;
import ru.abstractcoder.murdermystery.core.game.role.icon.TemplateIcon;
import ru.abstractcoder.murdermystery.core.game.role.skin.SkinResolver;

import java.util.Map;

public class PurchasableRoleClassTemplateImpl extends SimpleRoleClassTemplate implements PurchasableRoleClassTemplate {

    private final int price;
    private final char shopMenuChar;

    @JsonCreator
    public PurchasableRoleClassTemplateImpl(
            @JsonProperty("type") String typeKey,
            @JsonProperty("items") Map<Integer, ItemData> itemDataMap,
            TemplateIcon icon, SkinResolver skinResolver,
            int price, char shopMenuChar) {
        super(typeKey, itemDataMap, icon, skinResolver);
        this.price = price;
        this.shopMenuChar = shopMenuChar;
    }

    @Override
    public int getPrice() {
        return price;
    }

    public char getShopMenuChar() {
        return shopMenuChar;
    }

}