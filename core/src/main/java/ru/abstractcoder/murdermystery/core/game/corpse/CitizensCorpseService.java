package ru.abstractcoder.murdermystery.core.game.corpse;

import com.comphenix.packetwrapper.WrapperPlayServerBed;
import com.comphenix.packetwrapper.WrapperPlayServerBlockChange;
import com.comphenix.protocol.wrappers.BlockPosition;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.trait.Gravity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;
import ru.abstractcoder.benioapi.util.optional.BeniOptional;
import ru.abstractcoder.murdermystery.core.game.corpse.bed.BedFacade;
import ru.abstractcoder.murdermystery.core.game.skin.Skin;
import ru.abstractcoder.murdermystery.core.util.SkinUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

public class CitizensCorpseService implements CorpseService {

    private final NPCRegistry npcRegistry;
    private final Plugin plugin;
    private final Map<UUID, Corpse> byCorpseIdMap = new HashMap<>();
    private final Map<UUID, Corpse> byPlayerIdMap = new HashMap<>();

    public CitizensCorpseService(NPCRegistry npcRegistry, Plugin plugin) {
        this.npcRegistry = npcRegistry;
        this.plugin = plugin;
    }

    @Nullable
    public Corpse getByCorpseId(UUID corpseId) {
        return byCorpseIdMap.get(corpseId);
    }

    @Nullable
    public Corpse getByPlayerId(UUID playerId) {
        return byPlayerIdMap.get(playerId);
    }

    @Override
    public Corpse spawnCorpse(UUID playerId, Skin skin, Location location) {
        NPC npc = npcRegistry.createNPC(EntityType.PLAYER, " ");
        npc.getTrait(Gravity.class).gravitate(true);

        SkinUtils.setSkin(npc, skin);

        BedFacade bedFacade = BedFacade.getByYaw(location.getYaw());
        Location spawnLoc = getNonClippableBlockUnderPlayer(location).or(location);
        Location adjustedSpawnLoc = new Location(
                spawnLoc.getWorld(),
                spawnLoc.getBlockX() + bedFacade.getAdjustX(),
                spawnLoc.getBlockY() + 0.13,
                spawnLoc.getBlockZ() + bedFacade.getAdjustZ());
        BlockPosition bedPosition = new BlockPosition(adjustedSpawnLoc.getBlockX(), 0, adjustedSpawnLoc.getBlockZ());

        npc.spawn(adjustedSpawnLoc);

        WrapperPlayServerBlockChange blockChangePacked = new WrapperPlayServerBlockChange();
        blockChangePacked.setLocation(bedPosition);
        blockChangePacked.setBlockData(bedFacade.getBedData());

        WrapperPlayServerBed bedPacked = new WrapperPlayServerBed();
        bedPacked.setLocation(bedPosition);
        bedPacked.setEntityID(npc.getEntity().getEntityId());

        Corpse corpse = new CitizensCorpse(plugin, npc, playerId, skin, blockChangePacked, bedPacked);
        byCorpseIdMap.put(corpse.getCorpseId(), corpse);
        byPlayerIdMap.put(corpse.getPlayerId(), corpse);

        Bukkit.getOnlinePlayers().forEach(corpse::sendTo);

        return corpse;
    }

    public void removeCorpse(Corpse corpse) {
        corpse.remove();
        byCorpseIdMap.remove(corpse.getCorpseId());
        byPlayerIdMap.remove(corpse.getPlayerId());
    }

    @Override
    public Collection<Corpse> getAllCorpses() {
        return byCorpseIdMap.values();
    }

    @Override
    public Stream<Corpse> nearbyCorpsesStream(Location location, double radius) {
        double radiusSquare = radius * radius;
        return getAllCorpses().stream()
                .filter(corpse -> corpse.getLocation().distanceSquared(location) <= radiusSquare);

    }

    private BeniOptional<Location> getNonClippableBlockUnderPlayer(Location loc) {
        loc = loc.clone();
        int blockY = loc.getBlockY();
        if (blockY < 0) {
            return BeniOptional.empty();
        }
        for (int y = blockY; y >= 0; y--) {
            loc.setY(y);
            if (loc.getBlock().getType().isSolid()) {
                loc.setY(y + 1);
                return BeniOptional.of(loc);
            }
        }
        return BeniOptional.empty();
    }

}