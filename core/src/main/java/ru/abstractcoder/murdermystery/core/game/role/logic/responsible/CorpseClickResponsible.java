package ru.abstractcoder.murdermystery.core.game.role.logic.responsible;

import ru.abstractcoder.murdermystery.core.game.corpse.Corpse;

public interface CorpseClickResponsible extends ResponsibleRoleLogic {

    void onCorpseClick(Corpse corpse, int slot);

}