package ru.abstractcoder.murdermystery.core.listener;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.murdermystery.core.config.GeneralConfig;
import ru.abstractcoder.murdermystery.core.config.Messages;
import ru.abstractcoder.murdermystery.core.lobby.LobbyEngine;
import ru.abstractcoder.murdermystery.core.slotbar.SlotBarItem;

public class LobbyListener implements Listener {

    private final GeneralConfig generalConfig;
    private final LobbyEngine lobbyEngine;
    private final MsgConfig<Messages> msgConfig;

    public LobbyListener(GeneralConfig generalConfig, LobbyEngine lobbyEngine, MsgConfig<Messages> msgConfig) {
        this.generalConfig = generalConfig;
        this.lobbyEngine = lobbyEngine;
        this.msgConfig = msgConfig;

        lobbyEngine.addShutdownHook(() -> HandlerList.unregisterAll(this));
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
        player.teleport(generalConfig.lobby().getSpawnLocation());

        lobbyEngine.loadPlayer(player).thenAccept(lobbyPlayer -> {
            msgConfig.get(Messages.general__joined_broadcast,
                    player.getName(),
                    lobbyEngine.getPlayerCount(),
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
        generalConfig.lobby().getSlotBarItemResolver()
                .resolve(slot)
                .map(SlotBarItem::getClickHandler)
                .ifPresent(clickHandler -> clickHandler.handleInteract(event))
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
            player.teleport(generalConfig.lobby().getSpawnLocation());
        }
    }

//    @EventHandler
//    public void onInventoryClick(InventoryClickEvent event) {
//        event.getWhoClicked().sendMessage(String.valueOf(event.getSlot()));
//        event.getWhoClicked().sendMessage(String.valueOf(event.getRawSlot()));
//        event.getWhoClicked().getInventory().setItem(40, new ItemStack(Material.STONE));
//    }

}