package ru.abstractcoder.murdermystery.core.dagger.module;

import dagger.Binds;
import dagger.Module;
import ru.abstractcoder.murdermystery.core.config.GeneralConfig;
import ru.abstractcoder.murdermystery.core.config.HoconGeneralConfig;
import ru.abstractcoder.murdermystery.core.game.arena.ArenaLoader;
import ru.abstractcoder.murdermystery.core.game.arena.HoconArenaLoader;
import ru.abstractcoder.murdermystery.core.game.corpse.CitizensCorpseService;
import ru.abstractcoder.murdermystery.core.game.corpse.CorpseService;
import ru.abstractcoder.murdermystery.core.game.role.chance.ClassedRoleDataRepository;
import ru.abstractcoder.murdermystery.core.game.role.chance.MysqlClassedRoleDataRepository;

@Module
public interface BindsModule {

    @Binds
    GeneralConfig generalConfig(HoconGeneralConfig __);

    @Binds
    ArenaLoader arenaLoader(HoconArenaLoader __);

    @Binds
    ClassedRoleDataRepository roleDataRepository(MysqlClassedRoleDataRepository __);

    @Binds
    CorpseService corpseService(CitizensCorpseService __);

}