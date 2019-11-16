package ru.abstractcoder.murdermystery.core.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bukkit.Location;
import org.bukkit.World;
import ru.abstractcoder.benioapi.board.sidebar.SidebarTemplate;
import ru.abstractcoder.benioapi.config.BenioConfig;
import ru.abstractcoder.benioapi.database.MySqlConnectionPool;
import ru.abstractcoder.murdermystery.core.cosmetic.resolver.CosmeticCategoryResolver;
import ru.abstractcoder.murdermystery.core.game.role.RoleTemplateResolver;
import ru.abstractcoder.murdermystery.core.game.role.classed.template.RoleClassTemplateResolver;
import ru.abstractcoder.murdermystery.core.game.role.profession.template.ProfessionTemplateResolver;
import ru.abstractcoder.murdermystery.core.game.role.skin.CivilianSkinPool;
import ru.abstractcoder.murdermystery.core.game.setting.GameGeneralSettings;
import ru.abstractcoder.murdermystery.core.game.setting.RewardSettings;
import ru.abstractcoder.murdermystery.core.lobby.settings.MiscSettings;
import ru.abstractcoder.murdermystery.core.lobby.settings.StartingSettings;
import ru.abstractcoder.murdermystery.core.lobby.slotbar.SlotBarItemResolver;
import ru.abstractcoder.murdermystery.core.rating.RatingRankResolver;

public interface GeneralConfig extends BenioConfig {

    Mysql mysql();

    Lobby lobby();

    Game game();

    class Lobby {

        private final Location spawnLocation;

        @JsonProperty("starting")
        private final StartingSettings startingSettings;

        @JsonProperty("misc")
        private final MiscSettings miscSettings;

        @JsonProperty("sidebar")
        private final SidebarTemplate sidebarTemplate;

        @JsonProperty("slotBarItems")
        private final SlotBarItemResolver slotBarItemResolver;

        @JsonCreator
        public Lobby(
                Location spawnLocation,
                StartingSettings startingSettings,
                MiscSettings miscSettings,
                SidebarTemplate sidebarTemplate,
                SlotBarItemResolver slotBarItemResolver) {
            this.spawnLocation = spawnLocation;
            this.startingSettings = startingSettings;
            this.miscSettings = miscSettings;
            this.sidebarTemplate = sidebarTemplate;
            this.slotBarItemResolver = slotBarItemResolver;
        }

        public World getWorld() {
            return spawnLocation.getWorld();
        }

        public Location getSpawnLocation() {
            return spawnLocation;
        }

        public SidebarTemplate getSidebarTemplate() {
            return sidebarTemplate;
        }

        public StartingSettings starting() {
            return startingSettings;
        }

        public SlotBarItemResolver getSlotBarItemResolver() {
            return slotBarItemResolver;
        }

        public MiscSettings misc() {
            return miscSettings;
        }

    }

    class Mysql {

        private final MySqlConnectionPool connectionPool;
        private final String caseDatabase;

        @JsonCreator
        public Mysql(MySqlConnectionPool connectionPool, String caseDatabase) {
            this.connectionPool = connectionPool;
            this.caseDatabase = caseDatabase;
        }

        public MySqlConnectionPool getConnectionPool() {
            return connectionPool;
        }

        public String getCaseDatabase() {
            return caseDatabase;
        }

    }

    class Game {

        private final World world;
        private final GameGeneralSettings generalSettings;
        private final SidebarTemplate sidebarTemplate;
        private final RewardSettings rewardSettings;
        private final RoleTemplateResolver roleTemplateResolver;
        private final RoleClassTemplateResolver roleClassTemplateResolver;
        private final ProfessionTemplateResolver professionTemplateResolver;
        private final CivilianSkinPool civilianSkinPool;
        private final CosmeticCategoryResolver cosmeticCategoryResolver;
        private final RatingRankResolver ratingRankResolver;

        @JsonCreator
        public Game(
                World world,
                @JsonProperty("general") GameGeneralSettings generalSettings,
                @JsonProperty("sidebar") SidebarTemplate sidebarTemplate,
                @JsonProperty("rewards") RewardSettings rewardSettings,
                @JsonProperty("roleTemplates") RoleTemplateResolver roleTemplateResolver,
                @JsonProperty("roleClassTemplates") RoleClassTemplateResolver roleClassTemplateResolver,
                @JsonProperty("professionTemplates") ProfessionTemplateResolver professionTemplateResolver,
                @JsonProperty("civilianSkins") CivilianSkinPool civilianSkinPool,
                @JsonProperty("cosmeticCategories") CosmeticCategoryResolver cosmeticCategoryResolver,
                @JsonProperty("ranks") RatingRankResolver ratingRankResolver) {
            this.world = world;
            this.generalSettings = generalSettings;
            this.sidebarTemplate = sidebarTemplate;
            this.roleTemplateResolver = roleTemplateResolver;
            this.rewardSettings = rewardSettings;
            this.roleClassTemplateResolver = roleClassTemplateResolver;
            this.professionTemplateResolver = professionTemplateResolver;
            this.civilianSkinPool = civilianSkinPool;
            this.cosmeticCategoryResolver = cosmeticCategoryResolver;
            this.ratingRankResolver = ratingRankResolver;
        }

        public World getWorld() {
            return world;
        }

        public GameGeneralSettings general() {
            return generalSettings;
        }

        public SidebarTemplate getSidebarTemplate() {
            return sidebarTemplate;
        }

        public RewardSettings rewards() {
            return rewardSettings;
        }

        public RoleTemplateResolver getRoleTemplateResolver() {
            return roleTemplateResolver;
        }

        public RoleClassTemplateResolver getRoleClassTemplateResolver() {
            return roleClassTemplateResolver;
        }

        public ProfessionTemplateResolver getProfessionTemplateResolver() {
            return professionTemplateResolver;
        }

        public CivilianSkinPool getCivilianSkinPool() {
            return civilianSkinPool;
        }

        public CosmeticCategoryResolver getCosmeticCategoryResolver() {
            return cosmeticCategoryResolver;
        }

        public RatingRankResolver getRatingRankResolver() {
            return ratingRankResolver;
        }

    }

}