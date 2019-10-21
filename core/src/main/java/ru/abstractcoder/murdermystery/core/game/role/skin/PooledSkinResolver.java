package ru.abstractcoder.murdermystery.core.game.role.skin;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.abstractcoder.murdermystery.core.game.skin.Skin;
import ru.abstractcoder.murdermystery.core.game.skin.data.PurchasableSkinData;

import java.util.Collection;

public class PooledSkinResolver implements SkinResolver {

    private final SkinPool skinPool;

    @JsonCreator
    public PooledSkinResolver(@JsonProperty(value = "skins", required = true) SkinPool skinPool) {
        this.skinPool = skinPool;
    }

    @Override
    public boolean isPurchasable() {
        return false;
    }

    @Override
    public SkinPool getSkinPool() {
        return skinPool;
    }

    @Override
    public Skin getDefaultSkin() {
        throw new UnsupportedOperationException("Not supported for this implementation");
    }

    @Override
    public Collection<PurchasableSkinData> getPurchasableSkins() {
        throw new UnsupportedOperationException("Not supported for this implementation");
    }

}
