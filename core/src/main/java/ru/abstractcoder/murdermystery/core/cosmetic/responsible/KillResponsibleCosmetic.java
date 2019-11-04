package ru.abstractcoder.murdermystery.core.cosmetic.responsible;

import ru.abstractcoder.murdermystery.core.cosmetic.Cosmetic;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;

public interface KillResponsibleCosmetic extends Cosmetic {

    void onKill(GamePlayer killer, GamePlayer victim);

}
