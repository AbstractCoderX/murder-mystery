package ru.abstractcoder.murdermystery.core.game.role.profession;

import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.role.component.RoleComponent;
import ru.abstractcoder.murdermystery.core.game.role.profession.template.ProfessionTemplate;

import java.util.Set;

public interface Profession extends RoleComponent {

    @Override
    Type getType();

    ProfessionTemplate template();

    enum Type implements RoleComponent.Type {
        POLICEMAN,
        DOCTOR,
        PROSTITUTE,
        DEFAULT

        ;

        public static final Set<Type> VALUES = Set.of(values());

        @Override
        public GameRole.Type getRoleType() {
            return GameRole.Type.CIVILIAN;
        }

    }

}