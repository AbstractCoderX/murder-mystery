package ru.abstractcoder.murdermystery.core.game.role.profession.template;

import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.murdermystery.core.config.Messages;
import ru.abstractcoder.murdermystery.core.game.GameEngine;
import ru.abstractcoder.murdermystery.core.game.role.civilian.professions.DefaultProfession;
import ru.abstractcoder.murdermystery.core.game.role.civilian.professions.DoctorProfession;
import ru.abstractcoder.murdermystery.core.game.role.civilian.professions.PolicemanProfession;
import ru.abstractcoder.murdermystery.core.game.role.civilian.professions.ProstituteProfession;
import ru.abstractcoder.murdermystery.core.game.role.profession.Profession;
import ru.abstractcoder.murdermystery.core.game.role.profession.Profession.Type;

import java.util.EnumMap;
import java.util.Map;

public class ProfessionResolver {

    private final Map<Type, Profession> professionMap;

    public ProfessionResolver(GameEngine gameEngine, MsgConfig<Messages> msgConfig) {
        professionMap = new EnumMap<>(Type.class);
        ProfessionTemplateResolver templateResolver = gameEngine.settings().getProfessionTemplateResolver();

        put(new DoctorProfession(templateResolver.resolve(Type.DOCTOR), gameEngine, msgConfig));
        put(new ProstituteProfession(templateResolver.resolve(Type.PROSTITUTE), gameEngine, msgConfig));
        put(new PolicemanProfession(templateResolver.resolve(Type.POLICEMAN), gameEngine, msgConfig));
        put(new DefaultProfession(templateResolver.resolve(Type.DEFAULT), gameEngine, msgConfig));
    }

    private void put(Profession profession) {
        professionMap.put(profession.getType(), profession);
    }

    public Profession resolve(Type type) {
        return professionMap.get(type);
    }

}