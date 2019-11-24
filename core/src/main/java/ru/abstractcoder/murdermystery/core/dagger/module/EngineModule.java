package ru.abstractcoder.murdermystery.core.dagger.module;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import ru.abstractcoder.murdermystery.core.game.arena.Arena;
import ru.abstractcoder.murdermystery.core.game.arena.ArenaLoader;

@Module
public class EngineModule {

    @Provides
    @Reusable
    public Arena arena(ArenaLoader arenaLoader) {
        return arenaLoader.load();
    }

}