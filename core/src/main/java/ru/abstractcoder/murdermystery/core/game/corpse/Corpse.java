package ru.abstractcoder.murdermystery.core.game.corpse;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import ru.abstractcoder.murdermystery.core.game.skin.Skin;

import java.util.UUID;

public interface Corpse {

    void sendTo(Player player);

    void remove();

    boolean isRemoved();

    UUID getPlayerId();

    UUID getCorpseId();

    Location getLocation();

    Skin getSkin();

    void setSkin(Skin skin);

    void enableGlowingFor(Player player);

}