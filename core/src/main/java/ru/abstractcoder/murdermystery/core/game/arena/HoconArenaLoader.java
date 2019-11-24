package ru.abstractcoder.murdermystery.core.game.arena;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import ru.abstractcoder.benioapi.config.HoconConfig;
import ru.abstractcoder.murdermystery.core.config.GeneralConfig;

import javax.inject.Inject;
import javax.inject.Named;

public class HoconArenaLoader implements ArenaLoader {

    private final HoconConfig arenaCfg;
    private final GeneralConfig generalConfig;
    private final ObjectMapper objectMapper;

    @Inject
    public HoconArenaLoader(@Named("arena") HoconConfig arenaCfg, GeneralConfig generalConfig, ObjectMapper objectMapper) {
        this.arenaCfg = arenaCfg;
        this.generalConfig = generalConfig;
        this.objectMapper = objectMapper;
    }

    @Override
    public Arena load() {
        ObjectReader reader = objectMapper
                .reader(new InjectableValues.Std().addValue("gameWorld", generalConfig.game().getWorld()))
                .forType(ArenaImpl.class);

        try {
            return reader.readValue(arenaCfg.getHandle().root().render(HoconConfig.JSON_WRITE_DEFAULT));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Unhandled exception due to reading arena config", e);
        }
    }

}