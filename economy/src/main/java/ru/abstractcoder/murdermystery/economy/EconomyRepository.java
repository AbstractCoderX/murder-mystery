package ru.abstractcoder.murdermystery.economy;

import java.util.concurrent.CompletableFuture;

public interface EconomyRepository {

    CompletableFuture<Integer> loadBalance(String playerName);

    CompletableFuture<Void> saveBalance(String playerName, int balance);

}