package ru.abstractcoder.murdermystery.core.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.murdermystery.core.config.Msg;
import ru.abstractcoder.murdermystery.core.game.GameEngine;
import ru.abstractcoder.murdermystery.core.game.GameState;

import javax.inject.Inject;

public class CommonListener implements BukkitListener {

    private final GameEngine gameEngine;
    private final MsgConfig<Msg> msgConfig;

    @Inject
    public CommonListener(GameEngine gameEngine, MsgConfig<Msg> msgConfig) {
        this.gameEngine = gameEngine;
        this.msgConfig = msgConfig;
    }

    @EventHandler(ignoreCancelled = true)
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        if (gameEngine.getState() != GameState.WAITING) {
            msgConfig.get(Msg.game__already_started).disallowLoginEvent(event);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
    }


    @EventHandler(priority = EventPriority.HIGH)
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }

}