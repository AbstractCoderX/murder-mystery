package ru.abstractcoder.murdermystery.core.cosmetic.resolver;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.collect.Maps;
import ru.abstractcoder.murdermystery.core.cosmetic.CosmeticCategory;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CosmeticCategoryResolver {

    private final Map<CosmeticCategory.Type, CosmeticCategory> categories;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public CosmeticCategoryResolver(List<CosmeticCategory> categories) {
        this.categories = Maps.newHashMapWithExpectedSize(categories.size());
        categories.forEach(c -> this.categories.put(c.getType(), c));
    }

    public Collection<CosmeticCategory> getCategories() {
        return categories.values();
    }

    public CosmeticCategory resolve(CosmeticCategory.Type categoryType) {
        return categories.get(categoryType);
    }

}
