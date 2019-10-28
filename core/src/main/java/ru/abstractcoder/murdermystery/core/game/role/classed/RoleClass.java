package ru.abstractcoder.murdermystery.core.game.role.classed;

import com.google.common.base.Preconditions;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.role.classed.template.RoleClassTemplate;
import ru.abstractcoder.murdermystery.core.game.role.component.RoleComponent;

public interface RoleClass extends RoleComponent {

    default Type getType() {
        return template().getType();
    }

    RoleClassTemplate template();

    //Marker interface
    interface Type extends RoleComponent.Type {}

    class TypeResolver {

        public static Type resolve(GameRole.Type roleType, String typeName) {
            Preconditions.checkArgument(roleType.isClassed(), "roleType must be classed type");
            RoleComponent.Type componentType = RoleComponent.TypeResolver.resolve(roleType + "_" + typeName);
            return (Type) componentType;
        }

    }

}