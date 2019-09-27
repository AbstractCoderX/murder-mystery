package ru.abstractcoder.murdermystery.core.game.role.logic.responsible;

import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;

public interface PlayerClickResponsible {

    void onPlayerClick(GamePlayer gamePlayer, GamePlayer target);

}
