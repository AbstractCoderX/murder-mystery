package ru.abstractcoder.murdermystery.core.cosmetic.impl;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.google.common.collect.Maps;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.benioapi.item.ItemData;
import ru.abstractcoder.murdermystery.core.config.Msg;
import ru.abstractcoder.murdermystery.core.cosmetic.Cosmetic;
import ru.abstractcoder.murdermystery.core.cosmetic.CosmeticCategory;
import ru.abstractcoder.murdermystery.core.cosmetic.PremiumCosmetic;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractCosmeticCategory implements CosmeticCategory {

    @JacksonInject
    protected final MsgConfig<Msg> msgConfig;

    private final ItemData icon;
    protected final Cosmetic defaultCosmetic;
    private final Map<String, PremiumCosmetic> premiumCosmeticMap;

    protected AbstractCosmeticCategory(MsgConfig<Msg> msgConfig,
            ItemData icon, List<? extends PremiumCosmetic> premiumCosmetics) {
        this.msgConfig = msgConfig;
        this.icon = icon;

        premiumCosmeticMap = Maps.newHashMapWithExpectedSize(premiumCosmetics.size());
        premiumCosmetics.forEach(c -> premiumCosmeticMap.put(c.getId(), c));

        defaultCosmetic = new DefaultCosmetic(createDefaultLogic());
    }

    @Override
    public Cosmetic getDefaultCosmetic() {
        return defaultCosmetic;
    }

    @Nullable
    protected Cosmetic.Logic createDefaultLogic() {
        return null;
    }

    public Collection<PremiumCosmetic> getPremiusCosmetics() {
        return premiumCosmeticMap.values();
    }

    @Override
    public ItemData getIcon() {
        return icon;
    }

    @Override
    public Cosmetic getCosmeticById(String id) {
        if (id.equals(DefaultCosmetic.ID)) {
            return getDefaultCosmetic();
        }
        return premiumCosmeticMap.get(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CosmeticCategory)) return false;
        CosmeticCategory that = (CosmeticCategory) obj;
        return this.getType() == that.getType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getType());
    }

    protected abstract class AbstractCosmetic implements Cosmetic {

        @Nullable
        private final Logic logic;

        protected AbstractCosmetic(@Nullable Logic logic) {
            this.logic = logic;
        }

        @Override
        public CosmeticCategory getCategory() {
            return AbstractCosmeticCategory.this;
        }

        @Override
        @Nullable
        public Logic getLogic() {
            return logic;
        }


        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Cosmetic)) return false;

            Cosmetic that = (Cosmetic) obj;

            return this.getCategory().equals(that.getCategory()) && this.getId().equals(that.getId());
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.getCategory(), this.getId());
        }

    }

    protected abstract class AbstractPremiumCosmetic extends AbstractCosmetic implements PremiumCosmetic {

        public static final String AVAILABILITY_PERMISSION_PARENT_NODE = "MurderMystery.cosmetic";

        private final String id;
        private final ItemData icon;

        protected AbstractPremiumCosmetic(String id, ItemData icon, Logic logic) {
            super(logic);
            this.id = id;
            this.icon = icon;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public boolean isAvailableFor(Player player) {
            String perm = AVAILABILITY_PERMISSION_PARENT_NODE + "." + getCategory().getType() + "." + getId();
            return player.hasPermission(perm);
        }

        @Override
        public ItemData getIconData() {
            return icon;
        }

    }

    protected class DefaultCosmetic extends AbstractCosmetic {

        public static final String ID = "default";

        public DefaultCosmetic(Logic logic) {
            super(logic);
        }

        @Override
        public final String getId() {
            return ID;
        }

    }

}