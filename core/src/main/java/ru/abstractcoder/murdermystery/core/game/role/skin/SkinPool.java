package ru.abstractcoder.murdermystery.core.game.role.skin;

import com.fasterxml.jackson.annotation.JsonCreator;
import ru.abstractcoder.murdermystery.core.game.skin.Skin;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

public class SkinPool {

    private final Queue<Skin> skinQueue;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public SkinPool(List<Skin> skins) {
        Collections.shuffle(skins);
        skinQueue = new ArrayDeque<>(skins);
    }

    public Skin takeSkin() {
        return skinQueue.remove();
    }

}
