package ru.abstractcoder.murdermystery.core.game.role.skin;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.abstractcoder.benioapi.util.CollectionGeneraliser;
import ru.abstractcoder.murdermystery.core.game.skin.data.PremiumSkinData;
import ru.abstractcoder.murdermystery.core.game.skin.data.SkinData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SimpleSkinResolver implements SkinResolver {

    private final Collection<SkinData> allSkins;
    private final SkinData defaultSkin;
    private final List<PremiumSkinData> purchasableSkins;

    @JsonCreator
    public SimpleSkinResolver(
            @JsonProperty("default") SkinData defaultSkin,
            @JsonProperty("premium") List<PremiumSkinData> purchasableSkins) {
        this.defaultSkin = defaultSkin;
        this.purchasableSkins = purchasableSkins;

        allSkins = new ArrayList<>(purchasableSkins.size() + 1);
        allSkins.add(defaultSkin);
        allSkins.addAll(purchasableSkins);
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
    public SkinData getDefaultSkin() {
        return defaultSkin;
    }

    @Override
    public List<SkinData> getPremiumSkins() {
        return CollectionGeneraliser.generaliseList(purchasableSkins);
    }

    @Override
    public Collection<SkinData> getAllSkins() {
        return allSkins;
    }

}
