package ru.abstractcoder.murdermystery.core.cosmetic.impl;

import ru.abstractcoder.murdermystery.core.cosmetic.Cosmetic;
import ru.abstractcoder.murdermystery.core.cosmetic.CosmeticCategory;

import java.util.List;

import static ru.abstractcoder.benioapi.util.CollectionGeneraliser.generaliseList;

public abstract class AbstractCosmeticCategory implements CosmeticCategory {

    private final String id;
    private final List<Cosmetic> cosmetics;

    protected AbstractCosmeticCategory(String id, List<? extends Cosmetic> cosmetics) {
        this.id = id;
        this.cosmetics = generaliseList(cosmetics);
    }

    public String getId() {
        return id;
    }

    public List<Cosmetic> getCosmetics() {
        return cosmetics;
    }

    protected class AbstractCosmetic implements Cosmetic {

        @Override
        public CosmeticCategory getCategory() {
            return AbstractCosmeticCategory.this;
        }

    }

}
