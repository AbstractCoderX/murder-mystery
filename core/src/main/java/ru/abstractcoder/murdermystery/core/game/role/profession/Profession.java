package ru.abstractcoder.murdermystery.core.game.role.profession;

import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.role.component.RoleComponent;
import ru.abstractcoder.murdermystery.core.game.role.profession.template.ProfessionTemplate;

public interface Profession extends RoleComponent {

    @Override
    Type getType();

    ProfessionTemplate template();

    enum Type implements RoleComponent.Type {
        POLICEMAN,
        DOCTOR,
        PROSTITUTE,
        DEFAULT;

        public static final Type[] VALUES = values();

        @Override
        public GameRole.Type getRoleType() {
            return GameRole.Type.CIVILIAN;
        }

    }

}