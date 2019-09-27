package ru.abstractcoder.murdermystery.core.slotbar.click;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractClickHandler implements ClickHandler {

    private static final List<Action> DEFAULT_ALLOWED_ACTIONS = Arrays.asList(
            Action.RIGHT_CLICK_AIR,
            Action.RIGHT_CLICK_BLOCK
    );

    private final List<Action> allowedActions;

    protected AbstractClickHandler(List<Action> allowedActions) {
        this.allowedActions = allowedActions != null
                ? allowedActions
                : DEFAULT_ALLOWED_ACTIONS;
    }

    @Override
    public void handleInteract(PlayerInteractEvent event) {
        if (allowedActions.contains(event.getAction())) {
            onClick(event.getPlayer());
            event.setUseItemInHand(Event.Result.DENY);
        }
    }

    protected abstract void onClick(Player player);

}