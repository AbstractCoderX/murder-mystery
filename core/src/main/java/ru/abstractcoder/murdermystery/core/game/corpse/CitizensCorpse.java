package ru.abstractcoder.murdermystery.core.game.corpse;

import com.comphenix.packetwrapper.AbstractPacket;
import net.citizensnpcs.api.npc.NPC;
import net.minecraft.server.v1_12_R1.DataWatcher;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityMetadata;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import ru.abstractcoder.benioapi.util.reflect.TrustedLookup;
import ru.abstractcoder.murdermystery.core.game.skin.Skin;
import ru.abstractcoder.murdermystery.core.util.SkinUtils;

import java.lang.invoke.MethodHandle;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CitizensCorpse implements Corpse {

    private static final MethodHandle DATAWATCHER_MAP_GETTER;
    private static final MethodHandle DATAWATCHER_MAP_SETTER;

    static {
        DATAWATCHER_MAP_GETTER = TrustedLookup.apply(lookup -> lookup.findGetter(DataWatcher.class, "d", Map.class));
        DATAWATCHER_MAP_SETTER = TrustedLookup.apply(lookup -> lookup.findSetter(DataWatcher.class, "d", Map.class));
    }

    private final Plugin plugin;
    private final NPC npc;
    private final UUID playerId;
    private Skin skin;

    private boolean removed = false;

    private final AbstractPacket blockChangePacket;
    private final AbstractPacket bedPacket;
    private Packet<?> glowingPacket;

    public CitizensCorpse(Plugin plugin, NPC npc, UUID playerId, Skin skin, AbstractPacket blockChangePacket, AbstractPacket bedPacket) {
        this.plugin = plugin;
        this.npc = npc;
        this.playerId = playerId;
        this.skin = skin;
        this.blockChangePacket = blockChangePacket;
        this.bedPacket = bedPacket;
    }

    @Override
    public void sendTo(Player player) {
        blockChangePacket.sendPacket(player);
        plugin.getServer().getScheduler()
                .runTaskLater(plugin, () -> bedPacket.sendPacket(player), 1L);

        if (glowingPacket != null) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(glowingPacket);
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
        if (glowingPacket == null) {
            EntityPlayer entityPlayer = ((CraftPlayer) npc.getEntity()).getHandle();

            DataWatcher toCloneDataWatcher = entityPlayer.getDataWatcher();
            DataWatcher newDataWatcher = new DataWatcher(entityPlayer);

            Map<Integer, DataWatcher.Item<?>> currentMap;
            try {
                //noinspection unchecked
                currentMap = (Map<Integer, DataWatcher.Item<?>>) DATAWATCHER_MAP_GETTER.invoke(toCloneDataWatcher);
            } catch (Throwable t) {
                throw new AssertionError(t);
            }
            Map<Integer, DataWatcher.Item<?>> newMap = new HashMap<>();

            for (Integer integer : currentMap.keySet()) {
                newMap.put(integer, currentMap.get(integer).d());
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

            glowingPacket = new PacketPlayOutEntityMetadata(npc.getEntity().getEntityId(), newDataWatcher, true);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(glowingPacket);
        }
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