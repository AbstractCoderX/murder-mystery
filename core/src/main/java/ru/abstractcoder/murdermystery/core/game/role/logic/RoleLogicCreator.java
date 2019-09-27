package ru.abstractcoder.murdermystery.core.game.role.logic;

import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;

public interface RoleLogicCreator {

    RoleLogic createLogic(GamePlayer gamePlayer);

}
