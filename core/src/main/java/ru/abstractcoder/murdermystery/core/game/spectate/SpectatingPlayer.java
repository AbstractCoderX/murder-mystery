package ru.abstractcoder.murdermystery.core.game.spectate;

import org.jetbrains.annotations.Nullable;
import ru.abstractcoder.murdermystery.core.game.corpse.Corpse;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.skin.SkinContainer;
import ru.abstractcoder.murdermystery.core.util.AbstractWrappedPlayer;

public class SpectatingPlayer extends AbstractWrappedPlayer {

    private final GameRole cachedRole;
    private final SkinContainer cachedSkinContainer;
    private Corpse corpse;

    public SpectatingPlayer(GamePlayer gamePlayer, @Nullable Corpse corpse) {
        super(gamePlayer.getHandle());
        cachedRole = gamePlayer.getRole();
        cachedSkinContainer = gamePlayer.getSkinContainer();
        this.corpse = corpse;
    }

    public GameRole getCachedRole() {
        return cachedRole;
    }

    public SkinContainer getCachedSkinContainer() {
        return cachedSkinContainer;
    }

    public Corpse getCorpse() {
        return corpse;
    }

    public void setCorpse(Corpse corpse) {
        this.corpse = corpse;
    }

}