package ru.abstractcoder.murdermystery.core.game.skin.view;

import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.skin.Skin;

public interface SkinView {

    Skin getSimpleCivilianSkin();

    Skin getSkinFor(GamePlayer player);

    default SkinView toSimpleCivilianView() {
        return new SingleSkinView(getSimpleCivilianSkin());
    }

}
