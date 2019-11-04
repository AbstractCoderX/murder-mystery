package ru.abstractcoder.murdermystery.core.cosmetic.responsible;

import ru.abstractcoder.murdermystery.core.cosmetic.Cosmetic;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;

public interface DeathResponsibleCosmetic extends Cosmetic {

    void onDeath(GamePlayer gamePlayer);

}
