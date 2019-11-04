package ru.abstractcoder.murdermystery.core.cosmetic.impl;

import com.fasterxml.jackson.annotation.JacksonInject;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.murdermystery.core.config.Messages;
import ru.abstractcoder.murdermystery.core.cosmetic.Cosmetic;
import ru.abstractcoder.murdermystery.core.cosmetic.CosmeticCategory;

import java.util.List;

import static ru.abstractcoder.benioapi.util.CollectionGeneraliser.generaliseList;

public abstract class AbstractCosmeticCategory implements CosmeticCategory {

    @JacksonInject
    protected final MsgConfig<Messages> msgConfig;

    protected final String id;
    protected final List<Cosmetic> cosmetics;

    protected AbstractCosmeticCategory(MsgConfig<Messages> msgConfig,
            String id, List<? extends Cosmetic> cosmetics) {
        this.msgConfig = msgConfig;
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
