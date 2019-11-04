package ru.abstractcoder.murdermystery.core.game.role.logic.responsible;

import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public interface InteractResponsible extends ResponsibleRoleLogic {

    void onInteract(int slot, ItemStack item, Action action, PlayerInteractEvent event);

}