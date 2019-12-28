package ru.abstractcoder.murdermystery.core.game.skin.view;

import ru.abstractcoder.murdermystery.core.game.role.holder.RoleHolder;
import ru.abstractcoder.murdermystery.core.game.skin.Skin;

public class SingleSkinView implements SkinView {

    private final Skin skin;

    public SingleSkinView(Skin skin) {
        this.skin = skin;
    }

    @Override
    public Skin getSimpleCivilianSkin() {
        return skin;
    }

    @Override
    public Skin getSkinFor(RoleHolder player) {
        return skin;
    }

    @Override
    public SkinView toSimpleCivilianView() {
        return this;
    }

}
