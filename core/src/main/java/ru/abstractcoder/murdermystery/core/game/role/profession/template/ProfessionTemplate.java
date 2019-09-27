package ru.abstractcoder.murdermystery.core.game.role.profession.template;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.abstractcoder.benioapi.item.ItemData;
import ru.abstractcoder.murdermystery.core.game.role.stored.AbstractStoredTemplate;

import java.util.Map;

public class ProfessionTemplate extends AbstractStoredTemplate {

    private final String name;

    @JsonCreator
    public ProfessionTemplate(
            String name,
            @JsonProperty("items") Map<Integer, ItemData> itemDataMap) {
        super(itemDataMap);
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
