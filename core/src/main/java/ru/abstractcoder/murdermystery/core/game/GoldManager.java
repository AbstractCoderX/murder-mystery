package ru.abstractcoder.murdermystery.core.game;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import ru.abstractcoder.benioapi.item.ItemUtils;
import ru.abstractcoder.murdermystery.core.config.GeneralConfig;
import ru.abstractcoder.murdermystery.core.game.misc.SharedConstants;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.economy.EconomyService;

public class GoldManager {

    private final EconomyService economyService;
    private final GeneralConfig generalConfig;

    public GoldManager(EconomyService economyService, GeneralConfig generalConfig) {
        this.economyService = economyService;
        this.generalConfig = generalConfig;
    }

    public void giveGold(GamePlayer gamePlayer, int goldAmount) {
        economyService.incrementBalanceAsync(gamePlayer.getHandle(), goldAmount * 2);

        PlayerInventory inventory = gamePlayer.getHandle().getInventory();

        ItemStack item = inventory.getItem(SharedConstants.GOLD_SLOT);
        if (ItemUtils.nullOrAir(item)) {
            ItemStack goldItem = generalConfig.game().general().getGoldItem(goldAmount);
            inventory.setItem(SharedConstants.GOLD_SLOT, goldItem);
        } else {
            item.setAmount(item.getAmount() + goldAmount);
        }
    }

}
