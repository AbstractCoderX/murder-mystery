package ru.abstractcoder.murdermystery.core.game.role.logic.responsible;

import org.bukkit.event.Cancellable;
import ru.abstractcoder.murdermystery.core.game.npc.Npc;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;

public interface NpcDamageResponsible extends ResponsibleRoleLogic {

    void onNpcDamage(GamePlayer damager, Npc npc, Cancellable event);

}