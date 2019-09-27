package ru.abstractcoder.murdermystery.core.game.role.classed.template;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.abstractcoder.benioapi.item.ItemData;

import java.util.Map;

public class PurchasableRoleClassTemplateImpl extends DefaultRoleClassTemplate implements PurchasableRoleClassTemplate {

    private final int price;

    @JsonCreator
    public PurchasableRoleClassTemplateImpl(
            String type, String name,
            @JsonProperty("items") Map<Integer, ItemData> itemDataMap,
            int price) {
        super(type, name, itemDataMap);
        this.price = price;
    }

    @Override
    public int getPrice() {
        return price;
    }

}