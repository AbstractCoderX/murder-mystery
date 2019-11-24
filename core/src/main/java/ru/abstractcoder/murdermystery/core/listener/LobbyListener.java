package ru.abstractcoder.murdermystery.core.listener;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.murdermystery.core.config.Msg;
import ru.abstractcoder.murdermystery.core.lobby.LobbyEngine;
import ru.abstractcoder.murdermystery.core.lobby.slotbar.SlotBarItem;

import javax.inject.Inject;

public class LobbyListener implements BukkitListener {

    private final LobbyEngine lobbyEngine;
    private final MsgConfig<Msg> msgConfig;

    @Inject
    public LobbyListener(LobbyEngine lobbyEngine, MsgConfig<Msg> msgConfig) {
        this.lobbyEngine = lobbyEngine;
        this.msgConfig = msgConfig;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.getInventory().clear();

        player.setFoodLevel(20);
        player.setSaturation(20);

        player.eject();
        player.leaveVehicle();

        player.setFireTicks(0);
        player.setNoDamageTicks(0);

        player.setExp(0);
        player.setLevel(0);
        player.setTotalExperience(0);

        player.setFlying(false);
        player.setAllowFlight(false);

        player.setWalkSpeed(0.2F);

        player.setGameMode(GameMode.ADVENTURE);
        player.teleport(lobbyEngine.settings().getSpawnLocation());

        lobbyEngine.loadPlayer(player).thenAccept(lobbyPlayer -> {
            msgConfig.get(Msg.general__joined_broadcast,
                    player.getName(),
                    lobbyEngine.getPlayerResolver().getPlayerCount(),
                    lobbyEngine.getArena().getMaxPlayers()
            ).broadcastSession().broadcastChat();
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        lobbyEngine.unloadPlayer(event.getPlayer());
    }

    @EventHandler
    public void onItemClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        int slot = event.getHand() == EquipmentSlot.OFF_HAND ? 40 : player.getInventory().getHeldItemSlot();
        lobbyEngine.settings().getSlotBarItemResolver()
                .resolve(slot)
                .map(SlotBarItem::getClickHandler)
                .ifPresent(clickHandler -> clickHandler.handleInteract(event,
                        lobbyEngine.getPlayerResolver().resolve(player))
                )
                .orElse(() -> event.setCancelled(true));
    }

    @EventHandler
    public void onHangingBreak(HangingBreakEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getTo().getY() <= 0) {
            Player player = event.getPlayer();
            player.setFallDistance(0);
            player.teleport(lobbyEngine.settings().getSpawnLocation());
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        //TODO
    }

    //    @EventHandler
    //    public void onInventoryClick(InventoryClickEvent event) {
    //        event.getWhoClicked().sendMessage(String.valueOf(event.getSlot()));
    //        event.getWhoClicked().sendMessage(String.valueOf(event.getRawSlot()));
    //        event.getWhoClicked().getInventory().setItem(40, new ItemStack(Material.STONE));
    //    }

}