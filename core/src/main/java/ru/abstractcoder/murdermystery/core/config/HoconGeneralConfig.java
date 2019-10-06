package ru.abstractcoder.murdermystery.core.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import dagger.Reusable;
import org.bukkit.plugin.Plugin;
import ru.abstractcoder.benioapi.config.HoconConfig;
import ru.abstractcoder.benioapi.database.MySqlConnectionPool;
import ru.abstractcoder.murdermystery.core.slotbar.click.StandartActionResolver;

import javax.inject.Inject;
import javax.inject.Named;
import java.nio.file.Path;

@Reusable
public class HoconGeneralConfig extends HoconConfig implements GeneralConfig {

    private Dto dto;

    @Inject
    public HoconGeneralConfig(Plugin plugin, @Named("globalDir") Path dataFolder, ObjectMapper objectMapper,
            StandartActionResolver standartActionResolver) {
        super(plugin, dataFolder, "general", true);
        ObjectReader objectReader = objectMapper
                .reader(new InjectableValues.Std()
                        .addValue(StandartActionResolver.class, standartActionResolver)
                )
                .forType(Dto.class);

        addOnReloadAction(() -> {
            if (dto.mysql != null) {
                dto.mysql.close();
            }
            dto = objectReader.readValue(handle.root().render(JSON_WRITE_DEFAULT));
        });
    }

    @Override
    public MySqlConnectionPool mysql() {
        return dto.mysql;
    }

    @Override
    public Lobby lobby() {
        return dto.lobby;
    }

    @Override
    public Game game() {
        return dto.game;
    }

    private static final class Dto {

        private final MySqlConnectionPool mysql;
        private final Lobby lobby;
        private final Game game;

        @JsonCreator
        public Dto(MySqlConnectionPool mysql, Lobby lobby, Game game) {
            this.mysql = mysql;
            this.lobby = lobby;
            this.game = game;
        }

    }

}