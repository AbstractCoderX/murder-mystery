package ru.abstractcoder.murdermystery.core.game.role.skin;

import com.fasterxml.jackson.annotation.JsonCreator;
import ru.abstractcoder.murdermystery.core.game.skin.Skin;
import ru.abstractcoder.murdermystery.core.game.skin.data.SkinData;

import java.util.*;

public class CivilianSkinPool {

    private final Queue<SkinData> skinQueue;
    private final Collection<SkinData> initialSkins;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public CivilianSkinPool(List<SkinData> skins) {
        Collections.shuffle(skins);
        skinQueue = new ArrayDeque<>(skins);
        initialSkins = skins;
    }

    public Skin takeSkin() {
        return skinQueue.remove().getSkin();
    }

    public Collection<SkinData> asCollection() {
        return initialSkins;
    }

}
