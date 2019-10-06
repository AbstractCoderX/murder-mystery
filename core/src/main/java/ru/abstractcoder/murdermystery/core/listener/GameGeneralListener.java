package ru.abstractcoder.murdermystery.core.listener;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class GameGeneralListener extends AbstractBukkitListener {

    public GameGeneralListener(Plugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onBow(EntityShootBowEvent event) {
        if (event.getEntityType() != EntityType.PLAYER || event.getProjectile().getType() != EntityType.ARROW) {
            return;
        }
        Player player = (Player) event.getEntity();
        Arrow arrow = (Arrow) event.getProjectile();
        arrow.spigot().setDamage(1000);
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity().getType() != EntityType.PLAYER) {
            return;
        }
        if (event.getDamager().getType() != EntityType.PLAYER) {
            return;
        }
        Player damager = (Player) event.getDamager();
        ItemStack itemInMainHand = damager.getInventory().getItemInMainHand();
        if (itemInMainHand == null || !itemInMainHand.getType().name().endsWith("_SWORD")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

}