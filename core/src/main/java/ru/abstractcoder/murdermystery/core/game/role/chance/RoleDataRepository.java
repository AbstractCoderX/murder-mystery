package ru.abstractcoder.murdermystery.core.game.role.chance;

import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.lobby.player.LobbyPlayer.RoleData;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface RoleDataRepository {

    CompletableFuture<Map<GameRole.Type, RoleData>> load(String name);

    CompletableFuture<Void> save(String name, GameRole.Type roleType, RoleData roleData);

}