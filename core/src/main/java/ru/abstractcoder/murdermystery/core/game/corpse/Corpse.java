package ru.abstractcoder.murdermystery.core.game.corpse;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import ru.abstractcoder.benioapi.util.Lazy;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.skin.Skin;
import ru.abstractcoder.murdermystery.core.game.skin.data.SkinData;

import java.util.HashSet;
import java.util.Set;
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

    Proof proof();

    class Proof {

        private final Lazy<String> proof;
        private final Set<GamePlayer> collectedBy = new HashSet<>();

        public Proof(SkinData skinData) {
            proof = Lazy.create(skinData::getRandomProof);
        }

        public boolean isCollectedByAnyone() {
            return !collectedBy.isEmpty();
        }

        public String collectBy(GamePlayer gamePlayer) {
            collectedBy.add(gamePlayer);
            return proof.get();
        }

        public boolean isCollectedBy(GamePlayer gamePlayer) {
            return collectedBy.contains(gamePlayer);
        }

    }

}