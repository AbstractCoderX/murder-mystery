package ru.abstractcoder.murdermystery.core.game.role.skin;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.abstractcoder.murdermystery.core.game.skin.data.SkinData;

import java.util.Collection;
import java.util.List;

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
    public SkinData getDefaultSkin() {
        throw new UnsupportedOperationException("Not supported for this implementation");
    }

    @Override
    public List<SkinData> getPremiumSkins() {
        throw new UnsupportedOperationException("Not supported for this implementation");
    }

    @Override
    public Collection<SkinData> getAllSkins() {
        return skinPool.asCollection();
    }

}
