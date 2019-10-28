package ru.abstractcoder.murdermystery.core.game.role.component;

import com.google.common.base.Preconditions;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.role.detective.DetectiveRoleClass;
import ru.abstractcoder.murdermystery.core.game.role.logic.RoleLogic;
import ru.abstractcoder.murdermystery.core.game.role.murder.MurderRoleClass;
import ru.abstractcoder.murdermystery.core.game.role.profession.Profession;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public interface RoleComponent {

    Type getType();

    RoleLogic createLogic(GamePlayer gamePlayer);

    interface Type {

        String name();

        GameRole.Type getRoleType();

    }

    class TypeResolver {

        private static final Map<String, Type> REGISTRY = new HashMap<>();

        static {
            registerAll(Profession.Type.values());
            registerAll(DetectiveRoleClass.Type.values());
            registerAll(MurderRoleClass.Type.values());
        }

        public static void registerAll(Type[] types) {
            for (Type type : types) {
                register(type);
            }
        }

        public static void register(Type type) {
            REGISTRY.put(type.getRoleType() + "_" + type.name(), type);
        }

        public static Type resolve(String s) {
            Type type = REGISTRY.get(s);
            Preconditions.checkArgument(type != null, "Unknown key %s", s);
            return type;
        }

        public static Type resolve(GameRole.Type roleType, String typeName) {
            return resolve(roleType + "_" + typeName);
        }

        public static Collection<Type> getAllTypes() {
            return REGISTRY.values();
        }

    }

}
