package ru.abstractcoder.murdermystery.core.game.role.logic.responsible;

import ru.abstractcoder.murdermystery.core.game.misc.DeathState;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;

public interface AnyKillResponsible extends ResponsibleRoleLogic {

    void onAnyKill(GamePlayer killer, GamePlayer victim, DeathState deathState);

}