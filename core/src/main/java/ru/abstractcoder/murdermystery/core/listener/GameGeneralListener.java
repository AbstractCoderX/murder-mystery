package ru.abstractcoder.murdermystery.core.listener;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayerResolver;
import ru.abstractcoder.murdermystery.core.game.player.PlayerController;

import javax.inject.Inject;
import java.util.Set;

public class GameGeneralListener extends AbstractBukkitListener {

    private final GamePlayerResolver playerResolver;
    private final PlayerController playerController;

    @Inject
    public GameGeneralListener(Plugin plugin, GamePlayerResolver playerResolver, PlayerController playerController) {
        super(plugin);
        this.playerResolver = playerResolver;
        this.playerController = playerController;
    }

    @EventHandler(ignoreCancelled = true)
    public void onBow(EntityShootBowEvent event) {
        if (event.getEntityType() != EntityType.PLAYER || event.getProjectile().getType() != EntityType.ARROW) {
            return;
        }
        Player player = (Player) event.getEntity();
        Arrow arrow = (Arrow) event.getProjectile();
        arrow.setDamage(1000);
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity().getType() != EntityType.PLAYER || event.getDamager().getType() != EntityType.PLAYER) {
            return;
        }
        Player damager = (Player) event.getDamager();
        ItemStack itemInMainHand = damager.getInventory().getItemInMainHand();
        if (!itemInMainHand.getType().name().endsWith("_SWORD")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (playerController.isSpectator(player)) {
            Set<Player> recipients = event.getRecipients();
            recipients.clear();
            playerController.getAllSpectators().forEach(sp -> recipients.add(sp.getHandle()));
            //TODO format
        } else {
            //TODO format
        }

        //TODO extract format to config
        event.setFormat(event.getFormat()
                .replace("%1$s", player.getDisplayName())
        );
    }

}