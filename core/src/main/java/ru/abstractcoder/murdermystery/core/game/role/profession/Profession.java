package ru.abstractcoder.murdermystery.core.game.role.profession;

import ru.abstractcoder.murdermystery.core.game.role.logic.RoleLogicCreator;
import ru.abstractcoder.murdermystery.core.game.role.profession.template.ProfessionTemplate;

public interface Profession extends RoleLogicCreator {

    Type getType();

    ProfessionTemplate template();

    enum Type {
        POLICEMAN,
        DOCTOR,
        PROSTITUTE,
        DEFAULT
    }

}