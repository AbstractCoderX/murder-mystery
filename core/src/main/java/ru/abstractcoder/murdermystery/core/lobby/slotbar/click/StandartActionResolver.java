package ru.abstractcoder.murdermystery.core.lobby.slotbar.click;

import org.jetbrains.annotations.NotNull;
import ru.abstractcoder.murdermystery.core.lobby.player.LobbyPlayer;

import javax.inject.Inject;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;

public class StandartActionResolver {

    private final Map<StandartClickAction, Consumer<LobbyPlayer>> actionConsumerMap;

    @Inject
    public StandartActionResolver() {
        actionConsumerMap = new EnumMap<>(StandartClickAction.class);
    }

    public void register(StandartClickAction key, Consumer<LobbyPlayer> action) {
        actionConsumerMap.put(key, action);
    }

    public @NotNull Consumer<LobbyPlayer> resolve(StandartClickAction action) {
        return actionConsumerMap.get(action);
    }

}