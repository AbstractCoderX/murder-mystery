package ru.abstractcoder.murdermystery.core.game.role;

import com.fasterxml.jackson.annotation.JsonCreator;
import ru.abstractcoder.benioapi.util.BenioCollectors;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RoleTemplateResolver {

    private final Map<GameRole.Type, RoleTemplate> byCategoryMap;
    private final List<RoleTemplate> extraPointableTemplates;

    @JsonCreator
    public RoleTemplateResolver(List<RoleTemplate> gameRoles) {
        byCategoryMap = gameRoles.stream().collect(BenioCollectors.toEnumMap(
                GameRole.Type.class,
                RoleTemplate::getType,
                Function.identity()
        ));

        extraPointableTemplates = gameRoles.stream()
                .filter(RoleTemplate::isExtraPointable)
                .collect(Collectors.toList());
    }

    public RoleTemplate getByType(GameRole.Type type) {
        return byCategoryMap.get(type);
    }

    public Collection<RoleTemplate> getAll() {
        return byCategoryMap.values();
    }

    public List<RoleTemplate> getExtraPointableTemplates() {
        return extraPointableTemplates;
    }

}