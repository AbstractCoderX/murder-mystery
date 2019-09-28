package ru.abstractcoder.murdermystery.core.game.npc;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import ru.abstractcoder.murdermystery.core.game.skin.SkinContainable;
import ru.abstractcoder.murdermystery.core.game.skin.SkinContainer;

import java.util.UUID;

public class Npc implements SkinContainable {

    private final NPC handle;
    private SkinContainer skinContainer;

    public Npc(NPC handle, SkinContainer skinContainer, Location location) {
        this.handle = handle;
        this.skinContainer = skinContainer;

        handle.spawn(location);
    }

    public boolean despawn() {
        return handle.despawn();
    }

    public void destroy() {
        handle.destroy();
    }

    public Location getLocation() {
        return handle.getStoredLocation();
    }

    @Override
    public UUID getUniqueId() {
        return handle.getUniqueId();
    }

    public void teleport(Location location) {
        handle.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    public void setItemInHand(ItemStack item) {
        handle.getTrait(Equipment.class).set(0, item);
    }

    @Override
    public SkinContainer getSkinContainer() {
        return skinContainer;
    }

    @Override
    public void setSkinContainer(SkinContainer skinContainer) {
        this.skinContainer = skinContainer;
    }

}