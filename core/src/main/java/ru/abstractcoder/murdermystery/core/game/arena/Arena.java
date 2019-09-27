package ru.abstractcoder.murdermystery.core.game.arena;

import org.bukkit.Location;

import java.util.List;

public interface Arena {

    int getMinPlayers();

    int getMaxPlayers();

    int getMinHeight();

    List<Location> getSpawnPoints();

    Location getRandomSpawnPoint();

}