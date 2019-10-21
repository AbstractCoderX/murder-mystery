package ru.abstractcoder.murdermystery.core.game.role.skin;

import com.fasterxml.jackson.annotation.JsonCreator;
import ru.abstractcoder.murdermystery.core.game.skin.Skin;
import ru.abstractcoder.murdermystery.core.game.skin.data.PurchasableSkinData;

import java.util.Collection;
import java.util.List;

public class PurchasableSkinResolver implements SkinResolver {

    private final Skin defaultSkin;
    private final List<PurchasableSkinData> purchasableSkins;

    @JsonCreator
    public PurchasableSkinResolver(Skin defaultSkin, List<PurchasableSkinData> purchasableSkins) {
        this.defaultSkin = defaultSkin;
        this.purchasableSkins = purchasableSkins;
    }

    @Override
    public boolean isPurchasable() {
        return true;
    }

    @Override
    public SkinPool getSkinPool() {
        throw new UnsupportedOperationException("Not supported for this implementation");
    }

    @Override
    public Skin getDefaultSkin() {
        return defaultSkin;
    }

    @Override
    public Collection<PurchasableSkinData> getPurchasableSkins() {
        return purchasableSkins;
    }

}
