package ru.abstractcoder.murdermystery.core.game.misc;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import ru.abstractcoder.murdermystery.core.game.spectate.SpectatingPlayer;

public class DeathState {

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
