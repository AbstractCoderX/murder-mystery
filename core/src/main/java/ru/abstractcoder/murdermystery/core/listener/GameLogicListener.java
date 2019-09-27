package ru.abstractcoder.murdermystery.core.listener;

import net.citizensnpcs.api.event.NPCDamageByEntityEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import ru.abstractcoder.benioapi.util.EntityUtils;
import ru.abstractcoder.murdermystery.core.game.GameEngine;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.role.logic.RoleLogic;
import ru.abstractcoder.murdermystery.core.game.role.logic.responsible.InteractResponsible;
import ru.abstractcoder.murdermystery.core.game.role.logic.responsible.NpcDamageResponsible;

public class GameLogicListener implements Listener {

    private final GameEngine gameEngine;

    public GameLogicListener(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    @EventHandler(ignoreCancelled = true)
    public void onNpcDamage(NPCDamageByEntityEvent event) {
        event.setCancelled(true);
        event.setDamage(0);

        var responsibleLogics = gameEngine.getRoleResolver().getResponsibleLogics(NpcDamageResponsible.class);
        if (responsibleLogics.isEmpty()) {
            return;
        }
        EntityUtils.resolvePlayerDamager(event.getDamager()).ifPresent(damager -> {
            GamePlayer gamePlayer = gameEngine.getPlayerResolver().resolvePresent(damager);
            gameEngine.getNpcService().getNpc(event.getNPC().getUniqueId()).ifPresent(npc -> {
                GamePlayer murder = gameEngine.getPlayerResolver().getMurder();
                responsibleLogics.forEach(resp -> resp.onNpcDamage(murder, gamePlayer, npc, event));
            });
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        gameEngine.getPlayerResolver().resolveSafe(event.getPlayer()).ifPresent(gamePlayer -> {
            RoleLogic roleLogic = gamePlayer.getRoleLogic();
            if (roleLogic instanceof InteractResponsible) {
                InteractResponsible resp = (InteractResponsible) roleLogic;
                int slot = event.getPlayer().getInventory().getHeldItemSlot();

                resp.onInteract(slot, event.getItem(), event.getAction(), event);
            }
        });
    }

}