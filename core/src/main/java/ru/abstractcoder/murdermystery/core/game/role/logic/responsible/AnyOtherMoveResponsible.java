package ru.abstractcoder.murdermystery.core.game.role.logic.responsible;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;

public interface AnyOtherMoveResponsible extends ResponsibleRoleLogic {

    void onOtherMove(GamePlayer other, Location from, Location to, Cancellable event);

}