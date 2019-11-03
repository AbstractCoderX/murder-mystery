package ru.abstractcoder.murdermystery.core.game.role.skin;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.abstractcoder.murdermystery.core.game.skin.Skin;
import ru.abstractcoder.murdermystery.core.game.skin.data.PremiumSkinData;
import ru.abstractcoder.murdermystery.core.game.skin.data.SkinData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SimpleSkinResolver implements SkinResolver {

    private final Collection<SkinData> allSkins;
    private final Skin defaultSkin;
    private final List<PremiumSkinData> premiumSkins;

    @JsonCreator
    public SimpleSkinResolver(
            @JsonProperty("default") SkinData defaultSkinData,
            @JsonProperty("premium") List<PremiumSkinData> premiumSkins) {
        this.defaultSkin = defaultSkinData.getSkin();
        this.premiumSkins = premiumSkins;

        allSkins = new ArrayList<>(premiumSkins.size() + 1);
        allSkins.add(defaultSkinData);
        allSkins.addAll(premiumSkins);
    }

    @Override
    public Skin getDefaultSkin() {
        return defaultSkin;
    }

    @Override
    public List<PremiumSkinData> getPremiumSkins() {
        return premiumSkins;
    }

    @Override
    public Collection<SkinData> getAllSkins() {
        return allSkins;
    }

}
