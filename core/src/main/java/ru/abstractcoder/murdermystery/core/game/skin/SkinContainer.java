package ru.abstractcoder.murdermystery.core.game.skin;

import java.util.Map;
import java.util.UUID;

//TODO check about skin notifying
public class SkinContainer {

    private final UUID ownerId;
    private final Skin realSkin;
    private final Map<UUID, Skin> displayedSkinMap;

    public SkinContainer(UUID ownerId, Skin realSkin, Map<UUID, Skin> displayedSkinMap) {
        this.ownerId = ownerId;
        this.realSkin = realSkin;
        this.displayedSkinMap = displayedSkinMap;
    }

    public Skin resolveSkinFor(UUID playerId) {
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

    public void setDisplayedSkinFor(UUID playerId, Skin skin) {
        displayedSkinMap.put(playerId, skin);
    }

}