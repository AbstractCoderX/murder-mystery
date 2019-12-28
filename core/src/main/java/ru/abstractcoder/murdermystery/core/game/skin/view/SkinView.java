package ru.abstractcoder.murdermystery.core.game.skin.view;

import ru.abstractcoder.murdermystery.core.game.role.holder.RoleHolder;
import ru.abstractcoder.murdermystery.core.game.skin.Skin;

public interface SkinView {

    Skin getSimpleCivilianSkin();

    Skin getSkinFor(RoleHolder player);

    default SkinView toSimpleCivilianView() {
        return new SingleSkinView(getSimpleCivilianSkin());
    }

}
