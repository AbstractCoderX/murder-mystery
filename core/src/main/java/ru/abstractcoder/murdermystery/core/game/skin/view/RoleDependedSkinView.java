package ru.abstractcoder.murdermystery.core.game.skin.view;

import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.skin.Skin;

public abstract class RoleDependedSkinView implements SkinView {

    @Override
    public Skin getSkinFor(GamePlayer player) {
        if (player.getRole().getType() == GameRole.Type.MURDER) {
            return getSkinForMurder();
        }
        return getSkinForSurvivors();
    }

    protected abstract Skin getSkinForMurder();

    protected abstract Skin getSkinForSurvivors();

}
