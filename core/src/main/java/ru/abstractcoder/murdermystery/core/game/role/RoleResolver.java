package ru.abstractcoder.murdermystery.core.game.role;

import org.jetbrains.annotations.NotNull;
import ru.abstractcoder.murdermystery.core.game.GameEngine;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayerResolver;
import ru.abstractcoder.murdermystery.core.game.role.classed.RoleClass;
import ru.abstractcoder.murdermystery.core.game.role.classed.template.RoleClassTemplate;
import ru.abstractcoder.murdermystery.core.game.role.classed.template.RoleClassTemplateResolver;
import ru.abstractcoder.murdermystery.core.game.role.logic.responsible.Responsible;
import ru.abstractcoder.murdermystery.core.game.role.profession.Profession;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class RoleResolver {

    private final RoleTemplateResolver roleTemplateResolver;
    private final RoleClassTemplateResolver roleClassTemplateResolver;
    private final RoleFactory roleFactory;
    private final GamePlayerResolver playerResolver;

    private final Map<Profession.Type, GameRole> civilianRoles = new EnumMap<>(Profession.Type.class);
    private final Map<RoleClass.Type, GameRole> classedRoles = new HashMap<>();

    private final Map<Class<? extends Responsible>, Collection<Responsible>> responsibleRoleLogics = new HashMap<>();

    public RoleResolver(GameEngine gameEngine, RoleFactory roleFactory) {
        this.roleTemplateResolver = gameEngine.settings().getRoleTemplateResolver();
        this.roleFactory = roleFactory;
        this.roleClassTemplateResolver = gameEngine.settings().getRoleClassTemplateResolver();
        this.playerResolver = gameEngine.getPlayerResolver();
    }

    //    public GameRole resolvePreparedRole(LobbyPlayer lobbyPlayer) {
    //        RoleTemplate roleTemplate = lobbyPlayer.getBalancedRole();
    //        LobbyPlayer.RoleData roleData = lobbyPlayer.getRoleData(roleTemplate.getType());
    //
    //        RoleClassTemplate roleClassTemplate = roleData.isClassTypeSelected()
    //                ? roleClassTemplateResolver.getByClassType(roleData.getSelectedClassType())
    //                : roleClassTemplateResolver.getDefaultTemplate(roleTemplate.getType());
    //        return
    //    }

    @NotNull
    public GameRole resolveClassedRole(RoleClass.Type classType, GameRole.Type roleType) {
        return resolveClassedRole(classType, roleTemplateResolver.getByType(roleType));
    }

    @NotNull
    public GameRole resolveClassedRole(RoleClass.Type classType, RoleTemplate roleTemplate) {
        return classedRoles.computeIfAbsent(classType, ct -> roleFactory.createClassedRole(ct, roleTemplate));
    }

    @NotNull
    public GameRole resolveCivilianRole(Profession.Type professionType, GameRole.Type roleType) {
        return resolveCivilianRole(professionType, roleTemplateResolver.getByType(roleType));
    }

    @NotNull
    public GameRole resolveCivilianRole(Profession.Type professionType, RoleTemplate roleTemplate) {
        return civilianRoles.computeIfAbsent(professionType, pt -> roleFactory.createCivilianRole(pt, roleTemplate));
    }

    @NotNull
    public GameRole getDefaultClassedRole(GameRole.Type roleType) {
        RoleClassTemplate defaultTemplate = roleClassTemplateResolver.getDefaultTemplate(roleType);
        return resolveClassedRole(defaultTemplate.getType(), roleType);
    }

    public <T extends Responsible> Collection<T> getResponsibleLogics(Class<? extends T> clazz) {
        return getResponsibleLogics(clazz, playerResolver::getAll);
    }

    public <T extends Responsible> Collection<T> getResponsibleLogics(Class<? extends T> clazz,
            Supplier<Collection<GamePlayer>> targetPlayers) {
        //noinspection unchecked
        return (Collection<T>) responsibleRoleLogics.computeIfAbsent(clazz, (__) -> {
            return targetPlayers.get().stream()
                    .map(GamePlayer::getRoleLogic)
                    .filter(clazz::isInstance)
                    .map(clazz::cast)
                    .collect(Collectors.toList());
        });
    }

}
