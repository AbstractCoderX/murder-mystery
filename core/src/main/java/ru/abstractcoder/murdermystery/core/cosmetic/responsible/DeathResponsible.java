package ru.abstractcoder.murdermystery.core.cosmetic.responsible;

import ru.abstractcoder.murdermystery.core.cosmetic.Cosmetic;
import ru.abstractcoder.murdermystery.core.game.misc.DeathState;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;

public interface DeathResponsible extends Cosmetic.Logic {

    void onDeath(GamePlayer gamePlayer, DeathState deathState);

}
