package ru.abstractcoder.murdermystery.core;

import org.bukkit.plugin.java.annotation.dependency.Dependency;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import ru.abstractcoder.benioapi.BenioPlugin;
import ru.abstractcoder.benioapi.database.BenioConnectionPool;
import ru.abstractcoder.murdermystery.core.command.MurderMysteryCommand;
import ru.abstractcoder.murdermystery.core.game.corpse.Corpse;
import ru.abstractcoder.murdermystery.core.game.corpse.CorpseService;
import ru.abstractcoder.murdermystery.core.listener.CommonListener;
import ru.abstractcoder.murdermystery.core.listener.LobbyListener;

@Plugin(name = "MurderMystery", version = "1.0")
@Author("AbstractCoder")
@Dependency("BenioApi")
@Dependency("Citizens")
public class MurderMysteryCorePlugin extends BenioPlugin {

    private CorpseService corpseService;
    private BenioConnectionPool connectionPool;

    @Override
    protected void onPluginEnable() throws Throwable {
//        ObjectMapper objectMapper = benioApiInstance.objectMapperService().createAdaptedObjectMapper();
//
//        Config globalDirsCfg = benioApiInstance.configBuilder()
//                .baseConfig()
//                .setFileName("global-dir.properties")
//                .buildHocon().getHandle();
//
//        Path globalDir = Paths.get(globalDirsCfg.getString("global-dir"));
//
//        MsgConfig<Messages> msgConfig = benioApiInstance.configBuilder()
//                .msgConfig(Messages.class)
//                .setCustomPath(globalDir).build();
//
//        HoconConfig rulesBookConfig = benioApiInstance.configBuilder()
//                .baseConfig()
//                .setCustomPath(globalDir)
//                .setFileName("rulebook").buildHocon();
//        RuleBook ruleBook = new HoconRuleBook(rulesBookConfig);
//
//        objectMapper.setInjectableValues(new InjectableValues.Std()
//                .addValue(StandartActionResolver.class, new StandartActionResolver(msgConfig, ruleBook))
//        );
//
//        GeneralConfig generalConfig = new HoconGeneralConfig(this, globalDir, objectMapper);
//
//        HoconConfig arenaConfig = benioApiInstance.configBuilder()
//                .baseConfig()
//                .setCustomPath(generalConfig.game().getWorld().getWorldFolder().toPath())
//                .setFileName("arena").buildHocon();
//        ArenaLoader arenaLoader = new HoconArenaLoader(arenaConfig, generalConfig, objectMapper);
//        Arena arena = arenaLoader.load();

//        connectionPool = generalConfig.mysql();
//        QueryFactory queryFactory = benioApiInstance.createQueryFactory(connectionPool);
//
//        EconomyRepository economyRepository = new MysqlEconomyRepository(queryFactory);
//        EconomyService economyService = new CachingEconomyService(economyRepository, this);
//        RoleDataRepository roleDataRepository = new MysqlRoleDataRepository(queryFactory);

//        SidebarService sidebarService = benioApiInstance.getSidebarService();
//        LobbySidebarManager lobbySidebarManager = new LobbySidebarManager(sidebarService, economyService,
//                generalConfig.lobby(), msgConfig);
//        LobbyEngine lobbyEngine = new LobbyEngine(generalConfig.lobby(), arena, lobbySidebarManager, msgConfig,
//                roleDataRepository, statisticRepository);

//        GameEngine gameEngine = new GameEngine(arena, generalConfig.game(), economyService,
//                playerConverter, , corpseService, npcService, );
//        ComputingRoleBalancer roleBalancer = new ComputingRoleBalancer(lobbyEngine, gameEngine);


//        benioApiInstance.getTickingService().register(new LobbyTicking(lobbyEngine, gameEngine, generalConfig.lobby(),
//                msgConfig, roleBalancer));

//        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
//        NPCRegistry npcRegistry = CitizensAPI.getNPCRegistry();
//        corpseService = new CitizensCorpseService(npcRegistry, this);

//        new PacketListener(this, protocolManager, generalConfig, npcRegistry, corpseService);

        new LobbyListener(this, generalConfig, msgConfig).register();
        new CommonListener(this).register();
        new MurderMysteryCommand(corpseService).register(this);
    }

    @Override
    @SuppressWarnings("RedundantThrows")
    protected void onPluginDisable() throws Throwable {
        connectionPool.close();
        corpseService.getAllCorpses().forEach(Corpse::remove);
    }

}