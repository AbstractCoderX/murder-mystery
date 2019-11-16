package ru.abstractcoder.murdermystery.core.listener;

import net.citizensnpcs.api.event.NPCDamageByEntityEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import ru.abstractcoder.benioapi.util.EntityUtils;
import ru.abstractcoder.murdermystery.core.game.GameEngine;
import ru.abstractcoder.murdermystery.core.game.corpse.Corpse;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.role.logic.RoleLogic;
import ru.abstractcoder.murdermystery.core.game.role.logic.responsible.*;

import javax.inject.Inject;
import java.util.UUID;

public class RoleLogicListener extends AbstractBukkitListener {

    private final GameEngine gameEngine;

    @Inject
    public RoleLogicListener(GameEngine gameEngine) {
        super(gameEngine.getPlugin());
        this.gameEngine = gameEngine;
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

    @EventHandler(ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        gameEngine.getPlayerResolver().resolveSafe(event.getPlayer()).map(GamePlayer::getRoleLogic)
                .castIfInstance(InteractResponsible.class)
                .ifPresent(resp -> {
                    int slot = event.getPlayer().getInventory().getHeldItemSlot();
                    resp.onInteract(slot, event.getItem(), event.getAction(), event);
                });
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        gameEngine.getPlayerResolver().resolveSafe(event.getPlayer())
                .map(GamePlayer::getRoleLogic)
                .castIfInstance(BlockPlaceResponsible.class)
                .ifPresent(resp -> resp.onBlockPlace(event.getBlock(), event.getItemInHand(), event));
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        gameEngine.getPlayerResolver().resolveSafe(player).ifPresent(gamePlayer -> {
            RoleLogic roleLogic = gamePlayer.getRoleLogic();
            if (event.getTo().getY() <= gameEngine.getArena().getMinHeight()) {
                event.setCancelled(true);
                roleLogic.death();
                return;
            }

            if (roleLogic instanceof AnyOwnMoveResponsible) {
                AnyOwnMoveResponsible resp = (AnyOwnMoveResponsible) roleLogic;
                resp.onAnyMove(event.getFrom(), event.getTo(), event);
            }

            gameEngine.getRoleResolver().getResponsibleLogics(AnyOtherMoveResponsible.class,
                    () -> gameEngine.getPlayerResolver().getAllExpectThisOne(gamePlayer)
            ).forEach(resp -> resp.onOtherMove(gamePlayer, event.getFrom(), event.getTo(), event));
        });
    }

    @EventHandler
    public void onItemPickup(PlayerAttemptPickupItemEvent event) {
        ItemStack itemStack = event.getItem().getItemStack();
        if (itemStack.getType() == Material.GOLD_INGOT) {
            GamePlayer gamePlayer = gameEngine.getPlayerResolver().resolvePresent(event.getPlayer());
            gamePlayer.getRoleLogic().onGoldPickup(itemStack.getAmount());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onNpcDamage(NPCDamageByEntityEvent event) {
        event.setCancelled(true);
        event.setDamage(0);

        var responsibleLogics = gameEngine.getRoleResolver().getResponsibleLogics(NpcDamageResponsible.class);
        if (responsibleLogics.isEmpty()) {
            return;
        }
        EntityUtils.resolvePlayerDamager(event.getDamager()).ifPresent(damagerPlayer -> {
            GamePlayer damager = gameEngine.getPlayerResolver().resolvePresent(damagerPlayer);
            gameEngine.getNpcService().getNpc(event.getNPC().getUniqueId()).ifPresent(npc -> {
                responsibleLogics.forEach(resp -> resp.onNpcDamage(damager, npc, event));
            });
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onNpcClick(NPCRightClickEvent event) {
        Player player = event.getClicker();
        gameEngine.getPlayerResolver().resolveSafe(player)
                .map(GamePlayer::getRoleLogic)
                .castIfInstance(CorpseClickResponsible.class)
                .ifPresent(resp -> {
                    UUID uniqueId = event.getNPC().getUniqueId();
                    Corpse corpse = gameEngine.getCorpseService().getByCorpseId(uniqueId);
                    if (corpse != null) {
                        int slot = player.getInventory().getHeldItemSlot();
                        resp.onCorpseClick(corpse, slot);
                    }
                });

    }

}