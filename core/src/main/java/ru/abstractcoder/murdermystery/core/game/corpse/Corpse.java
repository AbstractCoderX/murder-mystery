package ru.abstractcoder.murdermystery.core.game.corpse;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import ru.abstractcoder.benioapi.util.Lazy;
import ru.abstractcoder.murdermystery.core.game.role.detective.DetectiveLogic;
import ru.abstractcoder.murdermystery.core.game.skin.Skin;
import ru.abstractcoder.murdermystery.core.game.skin.SkinData;

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

    String getProof();

    void enableGlowingFor(Player player);

    class Proof {

        private final Lazy<String> proof;
        private boolean collected;

        public Proof(SkinData skinData) {
            proof = Lazy.create(skinData::getRandomProof);
        }

        public boolean isCollected() {
            return proof.isInitialized();
        }

        public boolean canCollect(DetectiveLogic logic) {

        }

        public String collect() {
            return proof.get();
        }

    }

}