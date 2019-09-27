package ru.abstractcoder.murdermystery.core.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;

public class GamePlayerDeathEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final GamePlayer gamePlayer;

    public GamePlayerDeathEvent(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public GamePlayer getPlayer() {
        return gamePlayer;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
