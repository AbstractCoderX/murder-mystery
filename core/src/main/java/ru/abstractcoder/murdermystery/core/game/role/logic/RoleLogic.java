package ru.abstractcoder.murdermystery.core.game.role.logic;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.spectate.SpectatingPlayer;

public interface RoleLogic {

    void load();

    void kill(GamePlayer victim, DeathState deathState);

    void kill(GamePlayer victim);

    void death(@Nullable GamePlayer killer, DeathState deathState);

    void death();

    void onGoldPickup(int amount);

    class DeathState {

        private final Location deathLocation;
        private boolean needCorpse = true;
        private SpectatingPlayer spectatingPlayer;

        public DeathState(Location deathLocation) {
            this.deathLocation = deathLocation;
        }

        public Location getDeathLocation() {
            return deathLocation;
        }

        public boolean isNeedCorpse() {
            return needCorpse;
        }

        public void setNeedCorpse(boolean needCorpse) {
            this.needCorpse = needCorpse;
        }

        public SpectatingPlayer getSpectatingPlayer() {
            Preconditions.checkState(spectatingPlayer != null, "SpectatingPlayer not initialized yet!");
            return spectatingPlayer;
        }

        public void setSpectatingPlayer(SpectatingPlayer spectatingPlayer) {
            this.spectatingPlayer = spectatingPlayer;
        }

    }

}