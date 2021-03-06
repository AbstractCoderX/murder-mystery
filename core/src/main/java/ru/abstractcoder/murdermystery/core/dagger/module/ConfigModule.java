package ru.abstractcoder.murdermystery.core.dagger.module;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.typesafe.config.Config;
import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import ru.abstractcoder.benioapi.config.ConfigBuilderFactory;
import ru.abstractcoder.benioapi.config.HoconConfig;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.benioapi.database.DataSourceOwner;
import ru.abstractcoder.benioapi.jackson.BenioAdaptedObjectMapperService;
import ru.abstractcoder.murdermystery.core.config.GeneralConfig;
import ru.abstractcoder.murdermystery.core.config.Msg;
import ru.abstractcoder.murdermystery.core.game.role.skin.SimpleSkinResolver;
import ru.abstractcoder.murdermystery.core.game.role.skin.SkinResolver;

import javax.inject.Named;
import java.nio.file.Path;
import java.nio.file.Paths;

@Module
public class ConfigModule {

    @Provides
    @Reusable
    public ObjectMapper objectMapper(BenioAdaptedObjectMapperService objectMapperService) {
        return objectMapperService.createAdaptedObjectMapper()
                .registerModule(new SimpleModule("murder type bindings module")
                        .addAbstractTypeMapping(SkinResolver.class, SimpleSkinResolver.class)
                );
    }

    @Provides
    @Reusable
    @Named("globalDir")
    public Path globalConfigDir(ConfigBuilderFactory configBuilder) {
        Config globalDirCfg = configBuilder.baseConfig()
                .setFileName("global-dir.properties")
                .buildHocon().getHandle();

        return Paths.get(globalDirCfg.getString("global-dir"));
    }

    @Provides
    @Reusable
    public MsgConfig<Msg> msgConfig(ConfigBuilderFactory configBuilder, @Named("globalDir") Path globalDir) {
        return configBuilder
                .msgConfig(Msg.class)
                .setCustomPath(globalDir).build();
    }

    @Provides
    @Reusable
    @Named("ruleBook")
    public HoconConfig ruleBookConfig(ConfigBuilderFactory configBuilder, @Named("globalDir") Path globalDir) {
        return configBuilder
                .baseConfig()
                .setCustomPath(globalDir)
                .setFileName("rulebook").buildHocon();
    }

    @Provides
    @Reusable
    @Named("gui")
    public HoconConfig guiConfig(ConfigBuilderFactory configBuilder, @Named("globalDir") Path globalDir) {
        return configBuilder
                .baseConfig()
                .setCustomPath(globalDir)
                .setFileName("gui").buildHocon();
    }

    @Provides
    @Reusable
    @Named("arena")
    public HoconConfig arenaConfig(ConfigBuilderFactory configBuilder, GeneralConfig generalConfig) {
        return configBuilder
                .baseConfig()
                .setCustomPath(generalConfig.game().getWorld().getWorldFolder().toPath())
                .setFileName("arena").buildHocon();
    }

    @Provides
    public DataSourceOwner dataSourceOwner(GeneralConfig generalConfig) {
        return generalConfig.mysql().getConnectionPool();
    }

}