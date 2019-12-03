package ru.abstractcoder.murdermystery.core.game.corpse;

import com.comphenix.packetwrapper.WrapperPlayServerBlockChange;
import com.comphenix.protocol.wrappers.BlockPosition;
import dagger.Reusable;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.trait.Gravity;
import net.minecraft.server.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;
import ru.abstractcoder.benioapi.util.optional.BeniOptional;
import ru.abstractcoder.benioapi.util.reflect.TrustedLookup;
import ru.abstractcoder.murdermystery.core.game.corpse.bed.BedFacade;
import ru.abstractcoder.murdermystery.core.game.skin.Skin;
import ru.abstractcoder.murdermystery.core.util.SkinUtils;

import javax.inject.Inject;
import java.lang.invoke.MethodHandle;
import java.util.*;
import java.util.stream.Stream;

@Reusable
public class CitizensCorpseService implements CorpseService {

    private final NPCRegistry npcRegistry;
    private final Plugin plugin;
    private final Map<UUID, Corpse> byCorpseIdMap = new HashMap<>();
    private final Map<UUID, Corpse> byPlayerIdMap = new HashMap<>();

    private static final DataWatcherObject<EntityPose> ENTITY_POSE;
    private static final DataWatcherObject<Optional<net.minecraft.server.BlockPosition>> BED_POSITION;

    static {
        MethodHandle entityPoseHandle = TrustedLookup.apply(lookup ->
                lookup.findStaticGetter(Entity.class, "POSE", DataWatcherObject.class));
        MethodHandle bedPositionHandle = TrustedLookup.apply(lookup ->
                lookup.findStaticGetter(EntityLiving.class, "bs", DataWatcherObject.class));
        try {
            //noinspection unchecked
            ENTITY_POSE = (DataWatcherObject<EntityPose>) entityPoseHandle.invoke();
            //noinspection unchecked
            BED_POSITION = (DataWatcherObject<Optional<net.minecraft.server.BlockPosition>>) bedPositionHandle.invoke();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @Inject
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
    public Corpse spawnCorpse(Player player, Skin skin, Location location) {
        NPC npc = npcRegistry.createNPC(EntityType.PLAYER, player.getName());
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
        var nmsBedposition = (net.minecraft.server.BlockPosition) BlockPosition.getConverter().getGeneric(bedPosition);

        npc.spawn(adjustedSpawnLoc);

        WrapperPlayServerBlockChange blockChangePacked = new WrapperPlayServerBlockChange();
        blockChangePacked.setLocation(bedPosition);
        blockChangePacked.setBlockData(bedFacade.getBedData());

        EntityPlayer entityPlayer = ((CraftPlayer) npc.getEntity()).getHandle();
        DataWatcher dataWatcher = entityPlayer.getDataWatcher();
        dataWatcher.set(BED_POSITION, Optional.of(nmsBedposition));
        dataWatcher.set(ENTITY_POSE, EntityPose.SLEEPING);

        PacketPlayOutEntityMetadata defaultMetadataPacket = new PacketPlayOutEntityMetadata(
                entityPlayer.getId(),
                dataWatcher,
                true
        );

        //        WrapperPlayServerBed bedPacked = new WrapperPlayServerBed();
        //        bedPacked.setLocation(bedPosition);
        //        bedPacked.setEntityID(npc.getEntity().getEntityId());

        Corpse corpse = new CitizensCorpse(plugin, npc, player.getUniqueId(),
                skin, blockChangePacked, defaultMetadataPacket);
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