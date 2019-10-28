package ru.abstractcoder.murdermystery.core.game.role.chance;

import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.lobby.player.LobbyPlayer.ClassedRoleData;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface ClassedRoleDataRepository {

    CompletableFuture<Map<GameRole.Type, ClassedRoleData>> load(String name);

    CompletableFuture<Void> save(String name, GameRole.Type roleType, ClassedRoleData classedRoleData);

}