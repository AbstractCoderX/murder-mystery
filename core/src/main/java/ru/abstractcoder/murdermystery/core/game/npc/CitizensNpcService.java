package ru.abstractcoder.murdermystery.core.game.npc;

import dagger.Reusable;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import ru.abstractcoder.benioapi.util.optional.BeniOptional;
import ru.abstractcoder.murdermystery.core.game.skin.container.SkinContainableResolver;
import ru.abstractcoder.murdermystery.core.game.skin.container.SkinContainer;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Reusable
public class CitizensNpcService {

    private final NPCRegistry npcRegistry;
    private final SkinContainableResolver skinContainableResolver;

    private final Map<UUID, Npc> npcMap = new HashMap<>();

    @Inject
    public CitizensNpcService(NPCRegistry npcRegistry, SkinContainableResolver skinContainableResolver) {
        this.npcRegistry = npcRegistry;
        this.skinContainableResolver = skinContainableResolver;
    }

    public Npc spawnNpc(Location location, SkinContainer skinContainer) {
        NPC npc = npcRegistry.createNPC(EntityType.PLAYER, " ");
        Npc wrappedNpc = new Npc(npc, skinContainer, location);

        npcMap.put(npc.getUniqueId(), wrappedNpc);
        skinContainableResolver.add(wrappedNpc);

        return wrappedNpc;
    }

    public BeniOptional<Npc> getNpc(UUID uniqueId) {
        return BeniOptional.ofNullable(npcMap.get(uniqueId));
    }

    public void removeNpc(Npc npc) {
        npcMap.remove(npc.getUniqueId());
        npc.destroy();
    }

}
