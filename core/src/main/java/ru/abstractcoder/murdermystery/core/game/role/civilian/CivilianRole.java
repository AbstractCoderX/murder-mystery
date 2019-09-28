package ru.abstractcoder.murdermystery.core.game.role.civilian;

import ru.abstractcoder.murdermystery.core.game.role.AbstractRole;
import ru.abstractcoder.murdermystery.core.game.role.RoleTemplate;
import ru.abstractcoder.murdermystery.core.game.role.equipper.CivilianEquipper;
import ru.abstractcoder.murdermystery.core.game.role.logic.RoleLogicCreator;
import ru.abstractcoder.murdermystery.core.game.role.profession.Profession;

public class CivilianRole extends AbstractRole {

    private final Profession profession;

    public CivilianRole(RoleTemplate template, Profession profession) {
        super(template, new CivilianEquipper());
        this.profession = profession;
    }

    public Profession getProfession() {
        return profession;
    }

    @Override
    public String getDisplayName() {
        return profession.template().getName();
    }

    @Override
    public Type getType() {
        return Type.CIVILIAN;
    }

    @Override
    protected RoleLogicCreator getRoleLogicCreator() {
        return profession;
    }

}