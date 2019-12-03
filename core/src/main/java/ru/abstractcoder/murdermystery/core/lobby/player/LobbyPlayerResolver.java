package ru.abstractcoder.murdermystery.core.lobby.player;

import dagger.Reusable;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Reusable
public class LobbyPlayerResolver {

    private final ConcurrentMap<Player, LobbyPlayer> playerMap = new ConcurrentHashMap<>();

    @Inject
    public LobbyPlayerResolver() {
    }

    public boolean hasPlayer(Player player) {
        return playerMap.containsKey(player);
    }

    public boolean add(LobbyPlayer lobbyPlayer) {
        return playerMap.put(lobbyPlayer.getHandle(), lobbyPlayer) == null;
    }

    public boolean remove(Player player) {
        return playerMap.remove(player) != null;
    }

    public int getPlayerCount() {
        return playerMap.size();
    }

    public Collection<LobbyPlayer> getPlayers() {
        return playerMap.values();
    }

    public LobbyPlayer resolve(Player player) {
        return playerMap.get(player);
    }

}
