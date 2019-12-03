package ru.abstractcoder.murdermystery.core.bootstrap;

import org.bukkit.GameRule;
import org.bukkit.World;
import ru.abstractcoder.murdermystery.core.config.GeneralConfig;

import javax.inject.Inject;

public class WorldPreparingBootstrap {

    private final GeneralConfig generalConfig;

    @Inject
    public WorldPreparingBootstrap(GeneralConfig generalConfig) {
        this.generalConfig = generalConfig;
    }

    public void boot() {
        World gameWorld = generalConfig.game().getWorld();
        World lobbyWorld = generalConfig.lobby().getWorld();

        for (World world : new World[]{gameWorld, lobbyWorld}) {
            world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);

            world.setTime(6000);
        }
    }

}
