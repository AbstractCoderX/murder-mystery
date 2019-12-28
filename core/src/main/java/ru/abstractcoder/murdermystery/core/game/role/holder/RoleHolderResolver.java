package ru.abstractcoder.murdermystery.core.game.role.holder;

import com.google.common.base.Preconditions;
import dagger.Reusable;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Reusable
public class RoleHolderResolver {

    private final Map<UUID, RoleHolder> byPlayerMap = new HashMap<>();

    @Inject
    public RoleHolderResolver() {
    }

    public RoleHolder resolve(UUID uniqueId) {
        RoleHolder roleHolder = byPlayerMap.get(uniqueId);
        Preconditions.checkState(roleHolder != null,
                "No role holder found for  player with UUID: %s",
                uniqueId);
        return roleHolder;
    }

    public void put(RoleHolder roleHolder) {
        byPlayerMap.put(roleHolder.getHandle().getUniqueId(), roleHolder);
    }

    public boolean remove(UUID uniqueId) {
        return byPlayerMap.remove(uniqueId) != null;
    }

    public Collection<RoleHolder> getAll() {
        return byPlayerMap.values();
    }

}
