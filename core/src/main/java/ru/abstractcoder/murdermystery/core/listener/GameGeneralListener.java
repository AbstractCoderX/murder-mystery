package ru.abstractcoder.murdermystery.core.listener;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Switch;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import ru.abstractcoder.murdermystery.core.game.misc.VendingMachine;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayerResolver;
import ru.abstractcoder.murdermystery.core.game.player.PlayerController;

import javax.inject.Inject;
import java.util.Objects;
import java.util.Set;

public class GameGeneralListener extends AbstractBukkitListener {

    private final GamePlayerResolver playerResolver;
    private final PlayerController playerController;
    private final VendingMachine vendingMachine;

    @Inject
    public GameGeneralListener(Plugin plugin, GamePlayerResolver playerResolver, PlayerController playerController, VendingMachine vendingMachine) {
        super(plugin);
        this.playerResolver = playerResolver;
        this.playerController = playerController;
        this.vendingMachine = vendingMachine;
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

    @EventHandler(ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        GamePlayer gamePlayer = playerResolver.resolve(event.getPlayer());
        if (gamePlayer == null) {
            return;
        }

        @NotNull
        Block block = Objects.requireNonNull(event.getClickedBlock());
        if (!Tag.BUTTONS.isTagged(block.getType())) {
            return;
        }

        Switch button = (Switch) block.getBlockData();
        BlockFace facing = button.getFacing();
        if (block.getRelative(facing.getOppositeFace()).getType() == Material.GOLD_BLOCK)  { //TODO change material
            vendingMachine.applyRandomAction(gamePlayer);
        }
    }

}