package ru.abstractcoder.murdermystery.core.game.role.skin;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ru.abstractcoder.murdermystery.core.game.skin.Skin;
import ru.abstractcoder.murdermystery.core.game.skin.data.PremiumSkinData;
import ru.abstractcoder.murdermystery.core.game.skin.data.SkinData;

import java.util.Collection;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NONE, defaultImpl = SimpleSkinResolver.class)
public interface SkinResolver {

    Skin getDefaultSkin();

    List<PremiumSkinData> getPremiumSkins();

    Collection<SkinData> getAllSkins();

}
