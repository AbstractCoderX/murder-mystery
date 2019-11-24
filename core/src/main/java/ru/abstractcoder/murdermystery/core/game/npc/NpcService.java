package ru.abstractcoder.murdermystery.core.game.npc;

import org.bukkit.Location;
import ru.abstractcoder.benioapi.util.optional.BeniOptional;
import ru.abstractcoder.murdermystery.core.game.skin.container.SkinContainer;

import java.util.Collection;
import java.util.UUID;

public interface NpcService {

    Npc spawnNpc(Location location, SkinContainer skinContainer);

    BeniOptional<Npc> getNpc(UUID uniqueId);

    void removeNpc(Npc npc);

    Collection<Npc> getAll();

}
