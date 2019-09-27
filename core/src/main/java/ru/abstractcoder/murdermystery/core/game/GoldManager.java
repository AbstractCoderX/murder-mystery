package ru.abstractcoder.murdermystery.core.game;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import ru.abstractcoder.benioapi.item.ItemUtils;
import ru.abstractcoder.murdermystery.core.game.misc.SharedConstants;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;

public class GoldManager {

    private final GameEngine gameEngine;

    public GoldManager(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    public void giveGold(GamePlayer gamePlayer, int goldAmount) {
        gameEngine.getEconomyService().incrementBalanceAsync(gamePlayer.getHandle(), goldAmount * 2);

        PlayerInventory inventory = gamePlayer.getHandle().getInventory();

        ItemStack item = inventory.getItem(SharedConstants.GOLD_SLOT);
        if (ItemUtils.nullOrAir(item)) {
            ItemStack goldItem = gameEngine.settings().general().getGoldItem(goldAmount);
            inventory.setItem(SharedConstants.GOLD_SLOT, goldItem);
        } else {
            item.setAmount(item.getAmount() + goldAmount);
        }
    }

}
