package ru.abstractcoder.murdermystery.core.game.role.profession.template;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import ru.abstractcoder.murdermystery.core.game.role.profession.Profession;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ProfessionTemplateResolver {

    private final Map<Profession.Type, ProfessionTemplate> professionTemplateMap;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public ProfessionTemplateResolver(
            List<ProfessionTemplate> professionTemplates) {
        professionTemplateMap = Maps.newHashMapWithExpectedSize(professionTemplates.size());
        professionTemplates.forEach(temlpate -> professionTemplateMap.put(temlpate.getType(), temlpate));
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
