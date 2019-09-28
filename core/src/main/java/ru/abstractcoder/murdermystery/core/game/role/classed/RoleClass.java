package ru.abstractcoder.murdermystery.core.game.role.classed;

import com.google.common.base.Preconditions;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.role.classed.template.RoleClassTemplate;
import ru.abstractcoder.murdermystery.core.game.role.logic.RoleLogicCreator;

import java.util.HashMap;
import java.util.Map;

public interface RoleClass extends RoleLogicCreator {

    default Type getType() {
        return template().getType();
    }

    RoleClassTemplate template();

    interface Type {

        GameRole.Type roleType();

        String key();

    }

    class TypeResolver {

        private static final Map<String, Type> REGISTRY = new HashMap<>();

        public static void register(Type type) {
            REGISTRY.put(type.roleType() + "_" + type.key(), type);
        }

        public static Type resolve(String s) {
            Type type = REGISTRY.get(s);
            Preconditions.checkArgument(type != null, "Unknown key %s", s);
            return type;
        }

        public static Type resolve(GameRole.Type roleType, String key) {
            return resolve(roleType + "_" + key);
        }

    }

}