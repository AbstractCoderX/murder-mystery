package ru.abstractcoder.murdermystery.economy;

import org.bukkit.entity.Player;
import ru.abstractcoder.benioapi.gui.template.issuer.GuiAndCommandUserMixin;

import java.util.concurrent.CompletableFuture;

public interface EconomyService {

    int getCachedBalance(Player player);

    default int getCachedBalance(GuiAndCommandUserMixin player) {
        return getCachedBalance(player.getHandle());
    }

    CompletableFuture<Integer> getBalanceAsync(String playerName);

    CompletableFuture<Void> setOfflineBalanceAsync(String playerName, int balance);

    CompletableFuture<Void> setBalanceAsync(String playerName, int balance);

    CompletableFuture<Void> setBalanceAsync(Player player, int balance);

    default CompletableFuture<Void> incrementBalanceAsync(GuiAndCommandUserMixin player, int amount) {
        return incrementBalanceAsync(player.getHandle(), amount);
    }

    default CompletableFuture<Void> incrementBalanceAsync(Player player, int amount) {
        int balance = getCachedBalance(player);
        return setBalanceAsync(player, balance + amount);
    }

    default CompletableFuture<Void> setBalanceAsync(GuiAndCommandUserMixin player, int balance) {
        return setBalanceAsync(player.getHandle(), balance);
    }

}