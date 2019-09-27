package ru.abstractcoder.murdermystery.core.game.role;

import ru.abstractcoder.murdermystery.core.game.role.civilian.CivilianRole;
import ru.abstractcoder.murdermystery.core.game.role.classed.ClassedRole;
import ru.abstractcoder.murdermystery.core.game.role.classed.RoleClass;
import ru.abstractcoder.murdermystery.core.game.role.classed.RoleClassFactory;
import ru.abstractcoder.murdermystery.core.game.role.profession.Profession;
import ru.abstractcoder.murdermystery.core.game.role.profession.template.ProfessionResolver;

public class RoleFactory {

    private final RoleClassFactory roleClassFactory;
    private final ProfessionResolver professionResolver;

    public RoleFactory(RoleClassFactory roleClassFactory, ProfessionResolver professionResolver) {
        this.roleClassFactory = roleClassFactory;
        this.professionResolver = professionResolver;
    }

//    public GameRole create(LobbyPlayer lobbyPlayer, RoleTemplate template) {
//        switch (template.getType()) {
//            case MURDER:
//            case DETECTIVE:
//                RoleClass.Type roleClassType; //TODO get player selected RoleClass type
//                return new ClassedRole(template, roleClassFactory.create(roleClassType), template.getType());
//            case CIVILIAN:
//                //TODO
//        }
//    }

    public GameRole createClassedRole(RoleClass.Type classType, RoleTemplate roleTemplate) {
        return new ClassedRole(roleTemplate, roleClassFactory.create(classType), roleTemplate.getType());
    }

    public GameRole createCivilianRole(Profession.Type professionType, RoleTemplate roleTemplate) {
        return new CivilianRole(roleTemplate, professionResolver.resolve(professionType));
    }

}