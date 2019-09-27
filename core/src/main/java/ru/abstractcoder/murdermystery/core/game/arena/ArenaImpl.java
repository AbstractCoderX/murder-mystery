package ru.abstractcoder.murdermystery.core.game.arena;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ArenaImpl implements Arena {

    private final int minPlayers;
    private final int maxPlayers;
    private final int minHeight;
    private final List<Location> spawnPoints;

    @JsonCreator
    public ArenaImpl(
            @JacksonInject("gameWorld") World gameWorld,
            int minPlayers, int maxPlayers, int minHeight, List<Location> spawnPoints) {
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.minHeight = minHeight;

        spawnPoints.forEach(location -> location.setWorld(gameWorld));
        Collections.shuffle(spawnPoints);
        this.spawnPoints = spawnPoints;
    }

    @Override
    public int getMinPlayers() {
        return minPlayers;
    }

    @Override
    public int getMaxPlayers() {
        return maxPlayers;
    }

    @Override
    public int getMinHeight() {
        return minHeight;
    }

    @Override
    public List<Location> getSpawnPoints() {
        return spawnPoints;
    }

    @Override
    public Location getRandomSpawnPoint() {
        int index = ThreadLocalRandom.current().nextInt(spawnPoints.size());
        return spawnPoints.get(index);
    }

}