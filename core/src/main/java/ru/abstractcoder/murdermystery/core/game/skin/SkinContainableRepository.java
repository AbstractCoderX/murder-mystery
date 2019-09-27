package ru.abstractcoder.murdermystery.core.game.skin;

import ru.abstractcoder.benioapi.util.optional.BeniOptional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkinContainableRepository {

    private final Map<UUID, SkinContainable> holderMap = new HashMap<>();

    public void add(SkinContainable holder) {
        holderMap.put(holder.getUniqueId(), holder);
    }

    public BeniOptional<SkinContainable> findById(UUID holderId) {
        return BeniOptional.ofNullable(holderMap.get(holderId));
    }

}