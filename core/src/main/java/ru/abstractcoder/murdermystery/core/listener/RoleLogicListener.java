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
import ru.abstractcoder.murdermystery.core.game.arena.Arena;
import ru.abstractcoder.murdermystery.core.game.corpse.Corpse;
import ru.abstractcoder.murdermystery.core.game.corpse.CorpseService;
import ru.abstractcoder.murdermystery.core.game.npc.NpcService;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayerResolver;
import ru.abstractcoder.murdermystery.core.game.role.RoleResolver;
import ru.abstractcoder.murdermystery.core.game.role.logic.RoleLogic;
import ru.abstractcoder.murdermystery.core.game.role.logic.responsible.*;

import javax.inject.Inject;
import java.util.UUID;

public class RoleLogicListener implements BukkitListener {
    
    private final GamePlayerResolver playerResolver;
    private final RoleResolver roleResolver;
    private final Arena arena;
    private final NpcService npcService;
    private final CorpseService corpseService;

    @Inject
    public RoleLogicListener(GamePlayerResolver playerResolver, RoleResolver roleResolver,
            Arena arena, NpcService npcService, CorpseService corpseService) {
        this.playerResolver = playerResolver;
        this.roleResolver = roleResolver;
        this.arena = arena;
        this.npcService = npcService;
        this.corpseService = corpseService;
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
        GamePlayer gVictim = playerResolver.resolvePresent(victim);

        if (killer != null) {
            GamePlayer gKiller = playerResolver.resolvePresent(killer);
            gKiller.getRoleLogic().kill(gVictim);
        } else {
            gVictim.getRoleLogic().death();
        }

    }

    @EventHandler(ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        playerResolver.resolveSafe(event.getPlayer()).map(GamePlayer::getRoleLogic)
                .castIfInstance(InteractResponsible.class)
                .ifPresent(resp -> {
                    int slot = event.getPlayer().getInventory().getHeldItemSlot();
                    resp.onInteract(slot, event.getItem(), event.getAction(), event);
                });
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        playerResolver.resolveSafe(event.getPlayer())
                .map(GamePlayer::getRoleLogic)
                .castIfInstance(BlockPlaceResponsible.class)
                .ifPresent(resp -> resp.onBlockPlace(event.getBlock(), event.getItemInHand(), event));
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        playerResolver.resolveSafe(player).ifPresent(gamePlayer -> {
            RoleLogic roleLogic = gamePlayer.getRoleLogic();
            if (event.getTo().getY() <= arena.getMinHeight()) {
                event.setCancelled(true);
                roleLogic.death();
                return;
            }

            if (roleLogic instanceof AnyOwnMoveResponsible) {
                AnyOwnMoveResponsible resp = (AnyOwnMoveResponsible) roleLogic;
                resp.onAnyMove(event.getFrom(), event.getTo(), event);
            }

            roleResolver.getResponsibleLogics(AnyOtherMoveResponsible.class,
                    () -> playerResolver.getAllExpectThisOne(gamePlayer)
            ).forEach(resp -> resp.onOtherMove(gamePlayer, event.getFrom(), event.getTo(), event));
        });
    }

    @EventHandler
    public void onItemPickup(PlayerAttemptPickupItemEvent event) {
        ItemStack itemStack = event.getItem().getItemStack();
        if (itemStack.getType() == Material.GOLD_INGOT) {
            GamePlayer gamePlayer = playerResolver.resolvePresent(event.getPlayer());
            gamePlayer.getRoleLogic().pickupGolds(itemStack.getAmount());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onNpcDamage(NPCDamageByEntityEvent event) {
        event.setCancelled(true);
        event.setDamage(0);

        var responsibleLogics = roleResolver.getResponsibleLogics(NpcDamageResponsible.class);
        if (responsibleLogics.isEmpty()) {
            return;
        }
        EntityUtils.resolvePlayerDamager(event.getDamager()).ifPresent(damagerPlayer -> {
            GamePlayer damager = playerResolver.resolvePresent(damagerPlayer);
            npcService.getNpc(event.getNPC().getUniqueId()).ifPresent(npc -> {
                responsibleLogics.forEach(resp -> resp.onNpcDamage(damager, npc, event));
            });
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onNpcClick(NPCRightClickEvent event) {
        Player player = event.getClicker();
        playerResolver.resolveSafe(player)
                .map(GamePlayer::getRoleLogic)
                .castIfInstance(CorpseClickResponsible.class)
                .ifPresent(resp -> {
                    UUID uniqueId = event.getNPC().getUniqueId();
                    Corpse corpse = corpseService.getByCorpseId(uniqueId);
                    if (corpse != null) {
                        int slot = player.getInventory().getHeldItemSlot();
                        resp.onCorpseClick(corpse, slot);
                    }
                });

    }

}