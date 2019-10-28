package ru.abstractcoder.murdermystery.core.game.skin;

import java.util.Map;
import java.util.UUID;

//TODO check about skin notifying
public class SkinContainer {

    private final UUID ownerId;
    private final Skin realSkin;
    private final Map<UUID, Skin> displayedSkinMap;
    private boolean displayRealSkin = false;

    public SkinContainer(UUID ownerId, Skin realSkin, Map<UUID, Skin> displayedSkinMap) {
        this.ownerId = ownerId;
        this.realSkin = realSkin;
        this.displayedSkinMap = displayedSkinMap;
    }

    public Skin resolveSkinFor(UUID playerId) {
        if (isDisplayRealSkin()) {
            return realSkin;
        }
        return displayedSkinMap.get(playerId);
    }

    public Skin getRealSkin() {
        return realSkin;
    }

    public Skin getOwnSkin() {
        return displayedSkinMap.get(ownerId);
    }

    public void setOwnSkin(Skin ownSkin) {
        displayedSkinMap.put(ownerId, ownSkin);
    }

    public void setDisplayedSkinForAll(Skin skin) {
        displayedSkinMap.entrySet().forEach(entry -> entry.setValue(skin));
    }

    public void setDisplayedSkinFor(UUID playerId, Skin skin) {
        displayedSkinMap.put(playerId, skin);
    }

    public boolean isDisplayRealSkin() {
        return displayRealSkin;
    }

    public void setDisplayRealSkin(boolean displayRealSkin) {
        this.displayRealSkin = displayRealSkin;
    }

}