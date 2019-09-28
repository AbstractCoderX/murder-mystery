package ru.abstractcoder.murdermystery.core.game.role;

import com.fasterxml.jackson.annotation.JsonCreator;
import ru.abstractcoder.benioapi.util.BenioCollectors;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class RoleTemplateResolver {

    private final Map<GameRole.Type, RoleTemplate> byCategoryMap;

    @JsonCreator
    public RoleTemplateResolver(List<RoleTemplate> gameRoles) {
        byCategoryMap = gameRoles.stream().collect(BenioCollectors.toEnumMap(
                GameRole.Type.class,
                RoleTemplate::getType,
                Function.identity()
        ));
    }

    public RoleTemplate getByType(GameRole.Type type) {
        return byCategoryMap.get(type);
    }

    public Collection<RoleTemplate> getAllTemplates() {
        return byCategoryMap.values();
    }

}