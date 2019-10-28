package ru.abstractcoder.murdermystery.core.slotbar.click;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.bukkit.event.block.Action;
import ru.abstractcoder.murdermystery.core.lobby.player.LobbyPlayer;

import java.util.List;

public class CommandClickHandler extends AbstractClickHandler {

    private final String cmd;

    @JsonCreator
    public CommandClickHandler(List<Action> allowedActions, String cmd) {
        super(allowedActions);
        this.cmd = cmd;
    }

    @Override
    protected void onClick(LobbyPlayer player) {
        player.getHandle().chat(cmd);
    }

}