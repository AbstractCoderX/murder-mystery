package ru.abstractcoder.murdermystery.core.game.role.logic.responsible;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;

public interface BlockPlaceResponsible extends ResponsibleRoleLogic {

    void onBlockPlace(Block block, ItemStack itemInHand, Cancellable event);

}