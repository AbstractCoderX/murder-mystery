package ru.abstractcoder.murdermystery.core.cosmetic.responsible;

import ru.abstractcoder.murdermystery.core.cosmetic.Cosmetic;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;

public interface KillResponsible extends Cosmetic.Logic {

    void onKill(GamePlayer killer, GamePlayer victim);

}
