package ru.abstractcoder.murdermystery.economy.dagger;

import dagger.Binds;
import dagger.Module;
import ru.abstractcoder.murdermystery.economy.CachingEconomyService;
import ru.abstractcoder.murdermystery.economy.EconomyRepository;
import ru.abstractcoder.murdermystery.economy.EconomyService;
import ru.abstractcoder.murdermystery.economy.MysqlEconomyRepository;

@Module
public interface EconomyModule {

    @Binds
    EconomyRepository economyRepository(MysqlEconomyRepository mer);

    @Binds
    EconomyService economyService(CachingEconomyService ces);

}