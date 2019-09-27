package ru.abstractcoder.murdermystery.core.game.arena;

import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.abstractcoder.benioapi.config.HoconConfig;
import ru.abstractcoder.murdermystery.core.config.GeneralConfig;

import java.io.IOException;

public class HoconArenaLoader implements ArenaLoader {

    private final HoconConfig arenaCfg;
    private final GeneralConfig generalConfig;
    private final ObjectMapper objectMapper;

    public HoconArenaLoader(HoconConfig arenaCfg, GeneralConfig generalConfig, ObjectMapper objectMapper) {
        this.arenaCfg = arenaCfg;
        this.generalConfig = generalConfig;
        this.objectMapper = objectMapper;
    }

    @Override
    public Arena load() throws IOException {
        return objectMapper
                .reader(new InjectableValues.Std().addValue("gameWorld", generalConfig.game().getWorld()))
                .forType(ArenaImpl.class)
                .readValue(arenaCfg.getHandle().root().render(HoconConfig.JSON_WRITE_DEFAULT));
    }

}