package ru.abstractcoder.murdermystery.core.game.player;

import dagger.Reusable;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import ru.abstractcoder.benioapi.util.optional.BeniOptional;
import ru.abstractcoder.murdermystery.core.game.corpse.Corpse;
import ru.abstractcoder.murdermystery.core.game.corpse.CorpseService;
import ru.abstractcoder.murdermystery.core.game.role.holder.RoleHolderResolver;
import ru.abstractcoder.murdermystery.core.game.spectate.SpectatingPlayer;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Reusable
public class PlayerController {

    private final CorpseService corpseService;
    private final RoleHolderResolver roleHolderResolver;

    private final Map<UUID, SpectatingPlayer> spectatingPlayerMap = new HashMap<>();

    @Inject
    public PlayerController(CorpseService corpseService, RoleHolderResolver roleHolderResolver) {
        this.corpseService = corpseService;
        this.roleHolderResolver = roleHolderResolver;
    }

    public SpectatingPlayer makeSpectating(GamePlayer gamePlayer, boolean needCorpse) {
        Player player = gamePlayer.getHandle();
        player.getInventory().clear();
        player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        player.setGameMode(GameMode.SPECTATOR);

        Corpse corpse = null;
        if (needCorpse) {
            corpse = corpseService.spawnCorpse(gamePlayer, player.getLocation());
        }

        SpectatingPlayer spectatingPlayer = new SpectatingPlayer(gamePlayer, player.getLocation(), corpse);
        spectatingPlayerMap.put(player.getUniqueId(), spectatingPlayer);
        roleHolderResolver.put(spectatingPlayer);
        return spectatingPlayer;
    }

    public BeniOptional<SpectatingPlayer> getSpectatingSafe(UUID playerId) {
        return BeniOptional.ofNullable(spectatingPlayerMap.get(playerId));
    }

    @Nullable
    public SpectatingPlayer getSpectating(Player player) {
        return spectatingPlayerMap.get(player.getUniqueId());
    }

    @Nullable
    public SpectatingPlayer removeSpectating(UUID uuid) {
        roleHolderResolver.remove(uuid);
        return spectatingPlayerMap.remove(uuid);
    }

    @Nullable
    public SpectatingPlayer removeSpectating(Player player) {
        return removeSpectating(player.getUniqueId());
    }

    public boolean isSpectator(Player player) {
        return spectatingPlayerMap.containsKey(player.getUniqueId());
    }

    public Collection<SpectatingPlayer> getAllSpectators() {
        return spectatingPlayerMap.values();
    }

    public BeniOptional<SpectatingPlayer> getSpectatingSafe(Player player) {
        return getSpectatingSafe(player.getUniqueId());
    }

}