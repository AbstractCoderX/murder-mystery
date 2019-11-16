package ru.abstractcoder.murdermystery.core.game.role.classed;

import com.google.common.base.Preconditions;
import dagger.Reusable;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.murdermystery.core.config.Msg;
import ru.abstractcoder.murdermystery.core.game.GameEngine;
import ru.abstractcoder.murdermystery.core.game.role.classed.template.RoleClassTemplate;
import ru.abstractcoder.murdermystery.core.game.role.detective.DetectiveRoleClass;
import ru.abstractcoder.murdermystery.core.game.role.detective.classes.*;
import ru.abstractcoder.murdermystery.core.game.role.murder.MurderRoleClass;
import ru.abstractcoder.murdermystery.core.game.role.murder.classes.*;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@Reusable
public class RoleClassFactory {

    private GameEngine gameEngine;
    private final MsgConfig<Msg> msgConfig;
    private final Map<RoleClass.Type, RoleClassCreator> creatorsMap;

    @Inject
    public RoleClassFactory(MsgConfig<Msg> msgConfig) {
        this.msgConfig = msgConfig;
        creatorsMap = new HashMap<>(MurderRoleClass.Type.values().length + DetectiveRoleClass.Type.values().length);
    }

    public void init(GameEngine gameEngine) {
        this.gameEngine = gameEngine;

        creatorsMap.put(MurderRoleClass.Type.JACK, JackRoleClass::new);
        creatorsMap.put(MurderRoleClass.Type.AUGUST_MUSIR, AugustMusirRoleClass::new);
        creatorsMap.put(MurderRoleClass.Type.GRAVE_NICHOLAS, GraveNicholasRoleClass::new);
        creatorsMap.put(MurderRoleClass.Type.VOODOO_DOLL, VoodooDollRoleClass::new);
        creatorsMap.put(MurderRoleClass.Type.WOLF, WolfRoleClass::new);

        creatorsMap.put(DetectiveRoleClass.Type.SHERLOCK_GNOMES, SherlockGnomesRoleClass::new);
        creatorsMap.put(DetectiveRoleClass.Type.BENEDICT_CYBERSLOTZ, BenedictCyberslotzRoleClass::new);
        creatorsMap.put(DetectiveRoleClass.Type.EL_TERMU, ElTermuRoleClass::new);
        creatorsMap.put(DetectiveRoleClass.Type.MISS_PURPLE, MissPurpleRoleClass::new);
        creatorsMap.put(DetectiveRoleClass.Type.PAVEL_PINKERTON, PavelPinkertonRoleClass::new);
    }

    public RoleClass create(RoleClass.Type type) {
        Preconditions.checkState(gameEngine != null, "Not initialized yet!");

        RoleClassCreator creator = creatorsMap.get(type);
        Preconditions.checkState(creator != null, "Unknown RoleClass type %s", type);

        RoleClassTemplate template = gameEngine.settings().getRoleClassTemplateResolver().getByType(type);
        return creator.create(template, gameEngine, msgConfig);
    }

    @FunctionalInterface
    private interface RoleClassCreator {

        RoleClass create(RoleClassTemplate template, GameEngine gameEngine, MsgConfig<Msg> msgConfig);

    }

}