package ru.abstractcoder.murdermystery.core.listener;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import ru.abstractcoder.murdermystery.core.game.GameEngine;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.role.logic.responsible.AnyOwnMoveResponsible;
import ru.abstractcoder.murdermystery.core.game.role.logic.responsible.AnyOtherMoveResponsible;

public class GameGeneralListener implements Listener {

    private final GameEngine gameEngine;

    public GameGeneralListener(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
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
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setCancelled(true);
        Player player = event.getEntity();
        onDeath(player, player.getKiller());
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) {
            return;
        }
        Player player = (Player) event.getEntity();
        if (event.getCause() == EntityDamageEvent.DamageCause.LAVA) {
            event.setCancelled(true);
            onDeath(player, null);
        }
    }

    private void onDeath(Player victim, @Nullable Player killer) {
        GamePlayer gVictim = gameEngine.getPlayerResolver().resolvePresent(victim);

        if (killer != null) {
            GamePlayer gKiller = gameEngine.getPlayerResolver().resolvePresent(killer);
            gKiller.getRoleLogic().kill(gVictim);
        } else {
            gVictim.getRoleLogic().death();
        }

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

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        GamePlayer gamePlayer = gameEngine.getPlayerResolver().resolve(player);
        if (gamePlayer != null) {
            if (event.getTo().getY() <= gameEngine.getArena().getMinHeight()) {
                event.setCancelled(true);
                onDeath(player, null);
                return;
            }

            if (gamePlayer.getRoleLogic() instanceof AnyOwnMoveResponsible) {
                AnyOwnMoveResponsible resp = (AnyOwnMoveResponsible) gamePlayer.getRoleLogic();
                resp.onAnyMove(event.getFrom(), event.getTo(), event);
            }

            gameEngine.getRoleResolver().getResponsibleLogics(AnyOtherMoveResponsible.class, () -> {
                return gameEngine.getPlayerResolver().getAllExpectThisOne(gamePlayer);
            }).forEach(resp -> resp.onOtherMove(gamePlayer, event.getFrom(), event.getTo(), event));
        }
    }

    @EventHandler
    public void onItemPickup(PlayerAttemptPickupItemEvent event) {
        ItemStack itemStack = event.getItem().getItemStack();
        if (itemStack != null && itemStack.getType() == Material.GOLD_INGOT) {
            GamePlayer gamePlayer = gameEngine.getPlayerResolver().resolvePresent(event.getPlayer());
            gamePlayer.getRoleLogic().onGoldPickup(itemStack.getAmount());
        }
    }

}