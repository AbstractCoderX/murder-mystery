package ru.abstractcoder.murdermystery.core.lobby.slotbar.click;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.bukkit.event.block.Action;
import ru.abstractcoder.benioapi.util.Lazy;
import ru.abstractcoder.murdermystery.core.lobby.player.LobbyPlayer;

import java.util.List;
import java.util.function.Consumer;

public class StandartActionClickHandler extends AbstractClickHandler {

    private final Lazy<Consumer<LobbyPlayer>> actionConsumer;

    @JsonCreator
    public StandartActionClickHandler(
            List<Action> allowedActions,
            StandartClickAction action,
            @JacksonInject StandartActionResolver standartActionResolver) {
        super(allowedActions);

        actionConsumer = Lazy.create(() -> standartActionResolver.resolve(action));
    }

    @Override
    protected void onClick(LobbyPlayer player) {
        actionConsumer.get().accept(player);
    }

}