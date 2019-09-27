package ru.abstractcoder.murdermystery.core.game.player;

import org.bukkit.entity.Player;
import ru.abstractcoder.murdermystery.core.lobby.player.LobbyPlayer;

//TODO gamemode, teleport
public class PlayerFactory {

    private final GamePlayerResolver playerResolver;

    public PlayerFactory(GamePlayerResolver playerResolver) {
        this.playerResolver = playerResolver;
    }

    public GamePlayer fromLobbyPlayer(LobbyPlayer lobbyPlayer) {
        return new GamePlayer(lobbyPlayer.getPlayer(), lobbyPlayer.getBalancedRole(), ); //TODO think
    }

    public GamePlayer createCivilian(Player player) {
        GamePlayer gamePlayer = new GamePlayer(player, ); //TODO think
        playerResolver.loadCivilian(gamePlayer);
        return gamePlayer;
    }

}