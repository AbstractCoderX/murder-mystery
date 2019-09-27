package ru.abstractcoder.murdermystery.core.slotbar.click;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

import java.util.List;
import java.util.function.Consumer;

public class StandartActionClickHandler extends AbstractClickHandler {

    private final Consumer<Player> actionConsumer;

    @JsonCreator
    public StandartActionClickHandler(
            List<Action> allowedActions,
            StandartClickAction action,
            @JacksonInject StandartActionResolver standartActionResolver) {
        super(allowedActions);

        actionConsumer = standartActionResolver.resolve(action);
    }

    @Override
    protected void onClick(Player player) {
        actionConsumer.accept(player);
    }

}