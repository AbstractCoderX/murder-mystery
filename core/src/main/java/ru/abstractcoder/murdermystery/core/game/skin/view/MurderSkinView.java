package ru.abstractcoder.murdermystery.core.game.skin.view;

import ru.abstractcoder.murdermystery.core.game.skin.Skin;

public class MurderSkinView extends RoleDependedSkinView {

    private final Skin realSkin;
    private final Skin fakeSkin;

    public MurderSkinView(Skin realSkin, Skin fakeSkin) {
        this.realSkin = realSkin;
        this.fakeSkin = fakeSkin;
    }

    @Override
    protected Skin getSkinForMurder() {
        return realSkin;
    }

    @Override
    protected Skin getSkinForSurvivors() {
        return fakeSkin;
    }

    @Override
    public Skin getSimpleCivilianSkin() {
        return fakeSkin;
    }

}
