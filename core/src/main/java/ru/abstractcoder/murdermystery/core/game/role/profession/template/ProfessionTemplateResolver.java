package ru.abstractcoder.murdermystery.core.game.role.profession.template;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import ru.abstractcoder.murdermystery.core.game.role.profession.Profession;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

public class ProfessionTemplateResolver {

    private final Map<Profession.Type, ProfessionTemplate> professionTemplateMap;

    @JsonCreator
    public ProfessionTemplateResolver(
            @JsonDeserialize(as = EnumMap.class) Map<Profession.Type, ProfessionTemplate> professionTemplateMap) {
        this.professionTemplateMap = professionTemplateMap;
    }

    public ProfessionTemplate resolve(Profession.Type type) {
        ProfessionTemplate professionTemplate = professionTemplateMap.get(type);
        Preconditions.checkState(professionTemplate != null,
                "template not loaded for %s", type);
        return professionTemplate;
    }

    public Collection<ProfessionTemplate> getAll() {
        return professionTemplateMap.values();
    }

}
