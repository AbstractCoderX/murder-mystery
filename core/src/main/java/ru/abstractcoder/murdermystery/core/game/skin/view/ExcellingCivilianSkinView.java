package ru.abstractcoder.murdermystery.core.game.skin.view;

import ru.abstractcoder.murdermystery.core.game.skin.Skin;

public class ExcellingCivilianSkinView extends RoleDependedSkinView {

    private final Skin realSkin;
    private final Skin fakeSkin;

    public ExcellingCivilianSkinView(Skin realSkin, Skin fakeSkin) {
        this.realSkin = realSkin;
        this.fakeSkin = fakeSkin;
    }

    @Override
    protected Skin getSkinForMurder() {
        return fakeSkin;
    }

    @Override
    protected Skin getSkinForSurvivors() {
        return realSkin;
    }

    @Override
    public Skin getSimpleCivilianSkin() {
        return fakeSkin;
    }

}
