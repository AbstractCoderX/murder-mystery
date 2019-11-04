package ru.abstractcoder.murdermystery.core.game.role.civilian;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.Nullable;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.benioapi.item.ItemUtils;
import ru.abstractcoder.murdermystery.core.config.Messages;
import ru.abstractcoder.murdermystery.core.game.GameEngine;
import ru.abstractcoder.murdermystery.core.game.misc.DeathState;
import ru.abstractcoder.murdermystery.core.game.misc.SharedConstants;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.role.logic.SurvivorLogic;

public class CivilianLogic extends SurvivorLogic {

    private static final ItemStack BOW = new ItemStack(Material.BOW, 1, Material.BOW.getMaxDurability());

    private int goldPickupedAmount = 0;

    protected CivilianLogic(GamePlayer gamePlayer, GameEngine gameEngine, MsgConfig<Messages> msgConfig) {
        super(gamePlayer, gameEngine, msgConfig);
    }

    @Override
    public void load() {
        gameEngine.getPlayerResolver().loadCivilian(gamePlayer);
    }

    @Override
    public void death(@Nullable GamePlayer killer, DeathState deathState) {
        super.death(killer, deathState);
        gameEngine.getPlayerResolver().removeCivilian(gamePlayer);
    }

    @Override
    public void onGoldPickup(int amount) {
        super.onGoldPickup(amount);

        int newAmount = goldPickupedAmount + amount;
        if (newAmount >= this.getBowGoldPeriod()) {
            PlayerInventory inventory = gamePlayer.getHandle().getInventory();
            if (ItemUtils.nullOrAir(inventory.getItem(SharedConstants.WEAPON_SLOT))) {
                inventory.setItem(SharedConstants.WEAPON_SLOT, BOW);
            }
            inventory.setItem(SharedConstants.ARROW_SLOT, SharedConstants.ARROW_ITEM);
            goldPickupedAmount = newAmount % 10;
        } else {
            goldPickupedAmount = newAmount;
        }

    }

    protected int getBowGoldPeriod() {
        return 10;
    }

}