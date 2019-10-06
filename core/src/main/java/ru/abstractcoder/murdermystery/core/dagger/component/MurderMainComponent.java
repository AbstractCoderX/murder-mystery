package ru.abstractcoder.murdermystery.core.dagger.component;

import dagger.Component;
import ru.abstractcoder.benioapi.dagger.DaggerBenioModule;
import ru.abstractcoder.benioapi.util.ticking.Ticking;
import ru.abstractcoder.murdermystery.core.dagger.module.BindsModule;
import ru.abstractcoder.murdermystery.core.dagger.module.ConfigModule;
import ru.abstractcoder.murdermystery.core.dagger.module.DependencyModule;
import ru.abstractcoder.murdermystery.core.dagger.module.EngineModule;

import javax.inject.Named;

@Component(modules = {
        ConfigModule.class, BindsModule.class, DependencyModule.class,
        EngineModule.class, DaggerBenioModule.class
})
public interface MurderMainComponent {

    @Named("lobby")
    Ticking lobbyTicking();

}