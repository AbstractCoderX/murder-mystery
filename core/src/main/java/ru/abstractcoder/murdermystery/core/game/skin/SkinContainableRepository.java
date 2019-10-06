package ru.abstractcoder.murdermystery.core.game.skin;

import dagger.Reusable;
import ru.abstractcoder.benioapi.util.optional.BeniOptional;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Reusable
public class SkinContainableRepository {

    private final Map<UUID, SkinContainable> holderMap = new HashMap<>();

    @Inject
    public SkinContainableRepository() {
    }

    public void add(SkinContainable holder) {
        holderMap.put(holder.getUniqueId(), holder);
    }

    public BeniOptional<SkinContainable> findById(UUID holderId) {
        return BeniOptional.ofNullable(holderMap.get(holderId));
    }

}