package ru.abstractcoder.murdermystery.core.cosmetic.responsible;

import ru.abstractcoder.murdermystery.core.cosmetic.Cosmetic;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;

//TODO invoke this
public interface VictoryResponsibleCosmetic extends Cosmetic {

    void onVictory(GamePlayer gamePlayer);

}
