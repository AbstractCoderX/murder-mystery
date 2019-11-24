package ru.abstractcoder.murdermystery.core.game.role.skin;

import ru.abstractcoder.murdermystery.core.game.skin.Skin;
import ru.abstractcoder.murdermystery.core.game.skin.data.PremiumSkinData;
import ru.abstractcoder.murdermystery.core.game.skin.data.SkinData;

import java.util.Collection;
import java.util.List;

public interface SkinResolver {

    Skin getDefaultSkin();

    List<PremiumSkinData> getPremiumSkins();

    Collection<SkinData> getAllSkins();

}
