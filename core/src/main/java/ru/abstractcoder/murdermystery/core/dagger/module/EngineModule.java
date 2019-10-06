package ru.abstractcoder.murdermystery.core.dagger.module;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import ru.abstractcoder.murdermystery.core.game.arena.Arena;
import ru.abstractcoder.murdermystery.core.game.arena.ArenaLoader;

import java.io.IOException;

@Module
public class EngineModule {

    @Provides
    @Reusable
    public Arena arena(ArenaLoader arenaLoader) throws IOException {
        return arenaLoader.load();
    }

}