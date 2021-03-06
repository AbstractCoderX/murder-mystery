package ru.abstractcoder.murdermystery.core.game.player;

import com.google.common.base.Preconditions;
import dagger.Reusable;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.abstractcoder.benioapi.util.optional.BeniOptional;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.role.holder.RoleHolderResolver;
import ru.abstractcoder.murdermystery.core.game.skin.container.SkinContainableResolver;

import javax.inject.Inject;
import java.util.*;

@Reusable
public class GamePlayerResolver {

    private final SkinContainableResolver skinContainableResolver;
    private final RoleHolderResolver roleHolderResolver;

    private final Map<UUID, GamePlayer> gamePlayerMap = new HashMap<>();

    private boolean isMurderAlive = true;
    private GamePlayer murder;
    private GamePlayer detective;
    private final Collection<GamePlayer> survivors = new ArrayList<>(16);
    private final Collection<GamePlayer> civilians = new ArrayList<>(16);

    @Inject
    public GamePlayerResolver(SkinContainableResolver skinContainableResolver, RoleHolderResolver roleHolderResolver) {
        this.skinContainableResolver = skinContainableResolver;
        this.roleHolderResolver = roleHolderResolver;
    }

    public @Nullable GamePlayer resolve(Player player) {
        return gamePlayerMap.get(player.getUniqueId());
    }

    public @NotNull GamePlayer resolvePresent(Player player) {
        GamePlayer gamePlayer = resolve(player);
        Preconditions.checkState(gamePlayer != null, "Player not %s not in game", player.getName());
        return gamePlayer;
    }

    public @Nullable GamePlayer resolve(UUID playerId) {
        return gamePlayerMap.get(playerId);
    }

    public BeniOptional<GamePlayer> resolveSafe(UUID playerId) {
        return BeniOptional.ofNullable(gamePlayerMap.get(playerId));
    }

    public BeniOptional<GamePlayer> resolveSafe(Player player) {
        return resolveSafe(player.getUniqueId());
    }

    public boolean hasPlayer(Player player) {
        return gamePlayerMap.containsKey(player.getUniqueId());
    }

    public Collection<GamePlayer> getAllExpectThisOne(GamePlayer gamePlayer) {
        switch (gamePlayer.getRole().getType()) {
            case MURDER:
                return survivors;
            case DETECTIVE:
                List<GamePlayer> result = new ArrayList<>(civilians.size() + 1);
                result.addAll(civilians);
                result.add(murder);
                return result;
            case CIVILIAN:
                result = new ArrayList<>(gamePlayerMap.size() - 1);
                result.add(murder);
                result.add(detective);
                for (GamePlayer player : civilians) {
                    if (player.equals(gamePlayer)) {
                        continue;
                    }
                    result.add(player);
                }
                return result;
            default:
                throw new AssertionError();
        }
    }

    public boolean isMurderAlive() {
        return isMurderAlive;
    }

    public void setMurderAlive(boolean murderAlive) {
        isMurderAlive = murderAlive;
    }

    public GamePlayer getMurder() {
        return murder;
    }

    public void loadMurder(GamePlayer gamePlayer) {
        checkRole(gamePlayer, GameRole.Type.MURDER);

        put(gamePlayer);
        this.murder = gamePlayer;
    }

    public @Nullable GamePlayer getDetective() {
        return detective;
    }

    public void loadDetective(GamePlayer gamePlayer) {
        checkRole(gamePlayer, GameRole.Type.DETECTIVE);

        put(gamePlayer);
        survivors.add(gamePlayer);
        this.detective = gamePlayer;
    }

    public void setDetective(GamePlayer gamePlayer) {
        if (gamePlayer == null) {
            survivors.remove(this.detective);
            detective = null;
            return;
        }

        checkRole(gamePlayer, GameRole.Type.DETECTIVE);

        this.detective = gamePlayer;
    }

    public void removeMurder() {
        gamePlayerMap.remove(murder.getUniqueId());
        setMurderAlive(false);
    }

    public Collection<GamePlayer> getAll() {
        return gamePlayerMap.values();
    }

    public Collection<GamePlayer> getSurvivors() {
        return survivors;
    }

    public Collection<GamePlayer> getCivilians() {
            return civilians;
    }

    public void loadCivilian(GamePlayer gamePlayer) {
        checkRole(gamePlayer, GameRole.Type.CIVILIAN);

        put(gamePlayer);
        civilians.add(gamePlayer);
        survivors.add(gamePlayer);
    }

    public void removeCivilian(GamePlayer gamePlayer) {
        checkRole(gamePlayer, GameRole.Type.CIVILIAN);

        gamePlayerMap.remove(gamePlayer.getHandle().getUniqueId());
        civilians.remove(gamePlayer);
        survivors.remove(gamePlayer);
    }

    private void put(GamePlayer gamePlayer) {
        Player handle = gamePlayer.getHandle();
        if (gamePlayerMap.containsKey(handle.getUniqueId())) {
            throw new IllegalArgumentException("Player %s already added");
        }

        gamePlayerMap.put(handle.getUniqueId(), gamePlayer);
        skinContainableResolver.add(gamePlayer);
        roleHolderResolver.put(gamePlayer);

        System.out.println(String.format("%s - %s", gamePlayer.getRole().getType(), gamePlayer.getRole().getClass()));
    }

    private void checkRole(GamePlayer gamePlayer, GameRole.Type expectedType) {
        Preconditions.checkArgument(gamePlayer.getRole().getType() == expectedType,
                "Expected role type %s, but got %s",
                expectedType,
                gamePlayer.getRole().getType());
    }

}