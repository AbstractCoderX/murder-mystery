package ru.abstractcoder.murdermystery.core.lobby.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import dagger.Reusable;
import org.slf4j.Logger;
import ru.abstractcoder.benioapi.config.HoconConfig;
import ru.abstractcoder.benioapi.gui.template.MenuApi;
import ru.abstractcoder.benioapi.gui.template.MenuTemplate;
import ru.abstractcoder.benioapi.gui.template.PaginatedMenuTemplate;
import ru.abstractcoder.benioapi.gui.template.session.DefaultPaginatedMenuSession;
import ru.abstractcoder.murdermystery.core.lobby.player.LobbyPlayer;

@Reusable
public class SkinSelectingMenu {

    private final MenuApi menuApi;
    private MenuTemplate<LobbyPlayer, Session> template;

    public SkinSelectingMenu(MenuApi menuApi, HoconConfig guiConfig, ObjectMapper objectMapper) {
        this.menuApi = menuApi;
        guiConfig.addOnReloadAction(() -> {
            Config config = guiConfig.getHandle().getConfig("skinSelecting");
            Logger

        });
    }

    private static class Session extends DefaultPaginatedMenuSession<Session, LobbyPlayer> {

        public Session(PaginatedMenuTemplate<LobbyPlayer, Session> template, LobbyPlayer owner) {
            super(template, owner);
        }

    }

}
