package ru.abstractcoder.murdermystery.core.game.npc;

import dagger.Reusable;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import ru.abstractcoder.benioapi.util.optional.BeniOptional;
import ru.abstractcoder.murdermystery.core.game.misc.SharedConstants;
import ru.abstractcoder.murdermystery.core.game.skin.container.SkinContainableResolver;
import ru.abstractcoder.murdermystery.core.game.skin.container.SkinContainer;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Reusable
public class CitizensNpcService implements NpcService {

    private final NPCRegistry npcRegistry;
    private final SkinContainableResolver skinContainableResolver;

    private final Map<UUID, Npc> npcMap = new HashMap<>();

    @Inject
    public CitizensNpcService(NPCRegistry npcRegistry, SkinContainableResolver skinContainableResolver) {
        this.npcRegistry = npcRegistry;
        this.skinContainableResolver = skinContainableResolver;
    }

    @Override
    public Npc spawnNpc(Location location, SkinContainer skinContainer) {
        NPC npc = npcRegistry.createNPC(EntityType.PLAYER, SharedConstants.NPC_NAME);
        Npc wrappedNpc = new Npc(npc, skinContainer, location);
        wrappedNpc.notifySkinUpdated();

        npcMap.put(npc.getUniqueId(), wrappedNpc);
        skinContainableResolver.add(wrappedNpc);

        return wrappedNpc;
    }

    @Override
    public BeniOptional<Npc> getNpc(UUID uniqueId) {
        return BeniOptional.ofNullable(npcMap.get(uniqueId));
    }

    @Override
    public void removeNpc(Npc npc) {
        npcMap.remove(npc.getUniqueId());
        npc.destroy();
    }

    @Override
    public Collection<Npc> getAll() {
        return npcMap.values();
    }

}
