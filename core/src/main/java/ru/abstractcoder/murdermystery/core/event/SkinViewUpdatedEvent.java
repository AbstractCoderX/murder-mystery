package ru.abstractcoder.murdermystery.core.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ru.abstractcoder.murdermystery.core.game.skin.container.SkinContainable;

public class SkinViewUpdatedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final SkinContainable skinHolder;

    public SkinViewUpdatedEvent(SkinContainable skinHolder) {
        this.skinHolder = skinHolder;
    }

    public SkinContainable getSkinHolder() {
        return skinHolder;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
