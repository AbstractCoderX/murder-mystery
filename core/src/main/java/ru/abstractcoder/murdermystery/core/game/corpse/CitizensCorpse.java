package ru.abstractcoder.murdermystery.core.game.corpse;

import com.comphenix.packetwrapper.AbstractPacket;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.citizensnpcs.api.npc.NPC;
import net.minecraft.server.DataWatcher;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketPlayOutEntityMetadata;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import ru.abstractcoder.benioapi.util.reflect.TrustedLookup;
import ru.abstractcoder.murdermystery.core.game.skin.Skin;
import ru.abstractcoder.murdermystery.core.util.SkinUtils;

import java.lang.invoke.MethodHandle;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CitizensCorpse implements Corpse {

    private static final MethodHandle DATAWATCHER_MAP_GETTER;
    private static final MethodHandle DATAWATCHER_MAP_SETTER;

    static {
        DATAWATCHER_MAP_GETTER = TrustedLookup.apply(lookup ->
                lookup.findGetter(DataWatcher.class, "entries", Int2ObjectOpenHashMap.class)
        );
        DATAWATCHER_MAP_SETTER = TrustedLookup.apply(lookup ->
                lookup.findSetter(DataWatcher.class, "entries", Int2ObjectOpenHashMap.class)
        );
    }

    private final Plugin plugin;
    private final NPC npc;
    private final UUID playerId;
    private Skin skin;

    private boolean removed = false;

    private final AbstractPacket blockChangePacket;
    private final Packet<?> defaultMetadataPacket;
    private Packet<?> metadataWithGlowingPacket;

    private final Set<Player> glowingEnabledPlayers = new HashSet<>();

    private final Proof proof;

    public CitizensCorpse(Plugin plugin, NPC npc, UUID playerId, Skin skin,
            AbstractPacket blockChangePacket, Packet<?> defaultMetadataPacket) {
        this.plugin = plugin;
        this.npc = npc;
        this.playerId = playerId;
        this.skin = skin;
        this.blockChangePacket = blockChangePacket;
        this.defaultMetadataPacket = defaultMetadataPacket;

        proof = new Proof(skin.data());
    }

    @Override
    public void sendTo(Player player) {
        blockChangePacket.sendPacket(player);
        //plugin.getServer().getScheduler()
        //        .runTaskLater(plugin, () -> bedPacket.sendPacket(player), 1L);

        EntityPlayer receiver = ((CraftPlayer) player).getHandle();
        if (metadataWithGlowingPacket != null && glowingEnabledPlayers.contains(player)) {
            receiver.playerConnection.sendPacket(metadataWithGlowingPacket);
        } else {
            receiver.playerConnection.sendPacket(defaultMetadataPacket);
        }
    }

    @Override
    public void remove() {
        npc.destroy();
        removed = true;
    }

    public boolean isRemoved() {
        return removed;
    }

    @Override
    public UUID getPlayerId() {
        return playerId;
    }

    @Override
    public UUID getCorpseId() {
        return npc.getUniqueId();
    }

    @Override
    public Location getLocation() {
        return npc.getStoredLocation();
    }

    public Skin getSkin() {
        return skin;
    }

    @Override
    public void setSkin(Skin skin) {
        this.skin = skin;
        SkinUtils.setSkinAndNotify(npc, skin);
    }

    @Override
    public void enableGlowingFor(Player player) {
        if (metadataWithGlowingPacket == null) {
            EntityPlayer entityPlayer = ((CraftPlayer) npc.getEntity()).getHandle();

            DataWatcher toCloneDataWatcher = entityPlayer.getDataWatcher();
            DataWatcher newDataWatcher = new DataWatcher(entityPlayer);

            Int2ObjectMap<DataWatcher.Item<?>> currentMap;
            try {
                //noinspection unchecked
                currentMap = (Int2ObjectMap<DataWatcher.Item<?>>) DATAWATCHER_MAP_GETTER.invoke(toCloneDataWatcher);
            } catch (Throwable t) {
                throw new AssertionError(t);
            }
            Int2ObjectMap<DataWatcher.Item<?>> newMap = new Int2ObjectOpenHashMap<>();

            for (int i : currentMap.keySet()) {
                newMap.put(i, currentMap.get(i).d());
            }

            //noinspection unchecked
            DataWatcher.Item<Byte> item = (DataWatcher.Item<Byte>) newMap.get(0);
            byte initialBitMask = item.b();
            byte bitMaskIndex = 6;
            item.a((byte) (initialBitMask | 1 << bitMaskIndex));

            try {
                DATAWATCHER_MAP_SETTER.invoke(newDataWatcher, newMap);
            } catch (Throwable t) {
                throw new AssertionError(t);
            }

            metadataWithGlowingPacket = new PacketPlayOutEntityMetadata(
                    npc.getEntity().getEntityId(),
                    newDataWatcher,
                    true
            );
        }

        glowingEnabledPlayers.add(player);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(metadataWithGlowingPacket);
    }

    @Override
    public Proof proof() {
        return proof;
    }

    @Override
    public int hashCode() {
        return getCorpseId().hashCode() + 485;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Corpse)) return false;
        Corpse that = (Corpse) o;
        return this.getCorpseId().equals(that.getCorpseId());
    }

}