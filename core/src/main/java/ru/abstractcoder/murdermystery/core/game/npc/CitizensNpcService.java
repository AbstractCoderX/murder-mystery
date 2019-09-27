package ru.abstractcoder.murdermystery.core.game.npc;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import ru.abstractcoder.benioapi.util.optional.BeniOptional;
import ru.abstractcoder.murdermystery.core.game.skin.SkinContainableRepository;
import ru.abstractcoder.murdermystery.core.game.skin.SkinContainer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CitizensNpcService {

    private final NPCRegistry npcRegistry;
    private final SkinContainableRepository skinContainableRepository;

    private final Map<UUID, Npc> npcMap = new HashMap<>();

    public CitizensNpcService(NPCRegistry npcRegistry, SkinContainableRepository skinContainableRepository) {
        this.npcRegistry = npcRegistry;
        this.skinContainableRepository = skinContainableRepository;
    }

    public Npc spawnNpc(Location location, SkinContainer skinContainer) {
        NPC npc = npcRegistry.createNPC(EntityType.PLAYER, " ");
        Npc wrappedNpc = new Npc(npc, skinContainer, location);

        npcMap.put(npc.getUniqueId(), wrappedNpc);
        skinContainableRepository.add(wrappedNpc);

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
