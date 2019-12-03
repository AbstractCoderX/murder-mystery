package ru.abstractcoder.murdermystery.core.dagger.component;

import dagger.Component;
import ru.abstractcoder.benioapi.dagger.DaggerBenioModule;
import ru.abstractcoder.murdermystery.core.bootstrap.WorldPreparingBootstrap;
import ru.abstractcoder.murdermystery.core.config.GeneralConfig;
import ru.abstractcoder.murdermystery.core.dagger.module.BindsModule;
import ru.abstractcoder.murdermystery.core.dagger.module.ConfigModule;
import ru.abstractcoder.murdermystery.core.dagger.module.DependencyModule;
import ru.abstractcoder.murdermystery.core.dagger.module.EngineModule;
import ru.abstractcoder.murdermystery.core.game.bootstrap.GameActionBootstrap;
import ru.abstractcoder.murdermystery.core.game.corpse.CorpseService;
import ru.abstractcoder.murdermystery.core.game.npc.NpcService;
import ru.abstractcoder.murdermystery.core.listener.bootstrap.ListenerRegistrationProcessBootstrap;
import ru.abstractcoder.murdermystery.core.lobby.bootstrap.SlotBarItemActionBootstrap;
import ru.abstractcoder.murdermystery.economy.dagger.EconomyModule;

@Component(modules = {
        ConfigModule.class, BindsModule.class, DependencyModule.class,
        EngineModule.class, DaggerBenioModule.class, EconomyModule.class
})
public interface MurderMainComponent {

    GeneralConfig generalConfig();

    CorpseService corpseService();

    NpcService npcService();

    GameActionBootstrap gameActionBootstrap();

    SlotBarItemActionBootstrap slotBarItemActionBootstrap();

    ListenerRegistrationProcessBootstrap listenerRegistrationProcessBootstrap();

    WorldPreparingBootstrap worldPreparingBootstrap();

}