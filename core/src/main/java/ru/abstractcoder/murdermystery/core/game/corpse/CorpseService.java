package ru.abstractcoder.murdermystery.core.game.corpse;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.skin.Skin;
import ru.abstractcoder.murdermystery.core.game.spectate.SpectatingPlayer;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Stream;

public interface CorpseService {

    Corpse spawnCorpse(Player player, Skin skin, Location location);

    default Corpse spawnCorpse(GamePlayer player, Location location) {
        return spawnCorpse(player.getHandle(), player.getSkinContainer().getRealSkin(), location);
    }

    default Corpse spawnCorpse(SpectatingPlayer player, Location location) {
        Skin realSkin = player.getCachedSkinContainer().getRealSkin();
        Corpse corpse = spawnCorpse(player.getHandle(), realSkin, location);
        player.setCorpse(corpse);
        return corpse;
    }

    void removeCorpse(Corpse corpse);

    @Nullable
    Corpse getByCorpseId(UUID corpseId);

    @Nullable
    Corpse getByPlayerId(UUID playerId);

    Collection<Corpse> getAllCorpses();

    Stream<Corpse> nearbyCorpsesStream(Location location, double radius);

}