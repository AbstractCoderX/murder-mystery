package ru.abstractcoder.murdermystery.core;

import org.bukkit.plugin.java.annotation.dependency.Dependency;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import ru.abstractcoder.benioapi.BenioPlugin;
import ru.abstractcoder.benioapi.dagger.DaggerBenioModule;
import ru.abstractcoder.murdermystery.core.config.GeneralConfig;
import ru.abstractcoder.murdermystery.core.dagger.component.DaggerMurderMainComponent;
import ru.abstractcoder.murdermystery.core.dagger.component.MurderMainComponent;
import ru.abstractcoder.murdermystery.core.game.corpse.Corpse;
import ru.abstractcoder.murdermystery.core.game.corpse.CorpseService;
import ru.abstractcoder.murdermystery.core.game.npc.Npc;
import ru.abstractcoder.murdermystery.core.game.npc.NpcService;

@Plugin(name = "MurderMystery", version = "1.0")
@Author("AbstractCoder")
@Dependency("BenioApi")
@Dependency("Citizens")
@ApiVersion(ApiVersion.Target.v1_13)
public class MurderMysteryCorePlugin extends BenioPlugin {

    private GeneralConfig generalConfig;
    private CorpseService corpseService;
    private NpcService npcService;

    @Override
    protected void onPluginEnable() throws Throwable {
        MurderMainComponent component = DaggerMurderMainComponent.builder()
                .daggerBenioModule(new DaggerBenioModule(this))
                .build();

        generalConfig = component.generalConfig();
        corpseService = component.corpseService();
        npcService = component.npcService();

        component.gameActionBootstrap().boot();
        component.listenerRegistrationProcessBootstrap().boot();
        component.slotBarItemActionBootstrap().boot();
        component.worldPreparingBootstrap().boot();
        component.npcBoardTeamBootstrap().boot();
        component.murderMysteryCommand().register(this);
    }

    @Override
    @SuppressWarnings("RedundantThrows")
    protected void onPluginDisable() throws Throwable {
        generalConfig.mysql().getConnectionPool().close();
        corpseService.getAllCorpses().forEach(Corpse::remove);
        npcService.getAll().forEach(Npc::destroy);
    }

}