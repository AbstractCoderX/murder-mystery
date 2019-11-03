package ru.abstractcoder.murdermystery.core.game.skin.container;

import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.skin.Skin;
import ru.abstractcoder.murdermystery.core.game.skin.view.SingleSkinView;
import ru.abstractcoder.murdermystery.core.game.skin.view.SkinView;

//TODO check about skin notifying
public class SkinContainer {

    private final Skin realSkin;
    private SkinView skinView;
    private boolean displayRealSkin = false;

    public SkinContainer(Skin realSkin, SkinView skinView) {
        this.realSkin = realSkin;
        this.skinView = skinView;
    }

    public Skin getSkinFor(GamePlayer gamePlayer) {
        if (isDisplayRealSkin()) {
            return realSkin;
        }
        return skinView.getSkinFor(gamePlayer);
    }

    public Skin getRealSkin() {
        return realSkin;
    }

    public void setDisplayedSkinForAll(Skin skin) {
        skinView = new SingleSkinView(skin);
    }

    public boolean isDisplayRealSkin() {
        return displayRealSkin;
    }

    public void setDisplayRealSkin(boolean displayRealSkin) {
        this.displayRealSkin = displayRealSkin;
    }

    public SkinContainer toSimpleCivilianContainer() {
        return new SkinContainer(skinView.getSimpleCivilianSkin(), skinView.toSimpleCivilianView());
    }

}