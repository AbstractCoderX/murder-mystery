package ru.abstractcoder.murdermystery.core.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bukkit.plugin.Plugin;
import ru.abstractcoder.benioapi.config.HoconConfig;
import ru.abstractcoder.benioapi.database.MySqlConnectionPool;

import java.nio.file.Path;

public class HoconGeneralConfig extends HoconConfig implements GeneralConfig {

    private MySqlConnectionPool mysql;
    private Lobby lobby;
    private Game game;

    @JsonCreator
    public HoconGeneralConfig(Plugin plugin, Path dataFolder, ObjectMapper objectMapper) {
        super(plugin, dataFolder, "general", true);

        addOnReloadAction(() -> {
            mysql = objectMapper
                    .readerFor(MySqlConnectionPool.class)
                    .readValue(handle.getObject("mysql").render(JSON_WRITE_DEFAULT));
            lobby = objectMapper
                    .readerFor(Lobby.class)
                    .readValue(handle.getObject("lobby").render(JSON_WRITE_DEFAULT));
            game = objectMapper
                    .readerFor(Game.class)
                    .readValue(handle.getObject("game").render(JSON_WRITE_DEFAULT));
        });
    }

    @Override
    public MySqlConnectionPool mysql() {
        return mysql;
    }

    @Override
    public Lobby lobby() {
        return lobby;
    }

    @Override
    public Game game() {
        return game;
    }

}