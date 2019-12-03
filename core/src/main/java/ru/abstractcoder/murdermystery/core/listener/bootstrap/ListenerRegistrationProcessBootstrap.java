package ru.abstractcoder.murdermystery.core.listener.bootstrap;

import org.bukkit.plugin.Plugin;
import ru.abstractcoder.murdermystery.core.game.action.GameActionService;
import ru.abstractcoder.murdermystery.core.listener.*;
import ru.abstractcoder.murdermystery.core.lobby.LobbyEngine;

import javax.inject.Inject;

public class ListenerRegistrationProcessBootstrap {

    private final Plugin plugin;
    private final LobbyEngine lobbyEngine;
    private final GameActionService gameActionService;

    private final MurderListener[] commonListeners;
    private final MurderListener[] lobbyListeners;
    private final MurderListener[] gameListeners;

    @Inject
    public ListenerRegistrationProcessBootstrap(Plugin plugin,
            LobbyEngine lobbyEngine, GameActionService gameActionService,
            CommonListener commonListener,
            LobbyListener lobbyListener, GameGeneralListener gameGeneralListener,
            RoleLogicListener roleLogicListener, CorpsePacketListener corpsePacketListener,
            SkinPacketListener skinPacketListener) {
        this.plugin = plugin;
        this.lobbyEngine = lobbyEngine;
        this.gameActionService = gameActionService;

        commonListeners = new MurderListener[]{commonListener};

        lobbyListeners = new MurderListener[]{lobbyListener};

        gameListeners = new MurderListener[]{gameGeneralListener, roleLogicListener, corpsePacketListener,
                skinPacketListener};
    }

    public void boot() {
        for (MurderListener listener : commonListeners) {
            listener.register(plugin);
        }

        for (MurderListener listener : lobbyListeners) {
            listener.register(plugin);
            lobbyEngine.addShutdownHook(listener::unregister);
        }

        for (MurderListener listener : gameListeners) {
            gameActionService.addStartingAction(() -> listener.register(plugin));
            gameActionService.addEndingAction(listener::unregister);
        }
    }

}
