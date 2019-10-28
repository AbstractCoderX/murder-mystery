package ru.abstractcoder.murdermystery.core.lobby.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import ru.abstractcoder.benioapi.config.HoconConfig;
import ru.abstractcoder.benioapi.gui.template.MenuApi;
import ru.abstractcoder.benioapi.gui.template.MenuTemplate;
import ru.abstractcoder.benioapi.gui.template.session.SingleMenuSession;
import ru.abstractcoder.murdermystery.core.lobby.player.LobbyPlayer;

import javax.inject.Named;

public class ShopMenu {

    private final MenuApi menuApi;
    private MenuTemplate<LobbyPlayer, SingleMenuSession<LobbyPlayer>> template;

    public ShopMenu(MenuApi menuApi, @Named("gui") HoconConfig guiConfig, ObjectMapper objectMapper) {
        this.menuApi = menuApi;
        guiConfig.addOnReloadAction(() -> {
            Config config = guiConfig.getHandle().getConfig("shop");
            //TODO
        });
    }

    public void open(LobbyPlayer player) {
        menuApi.getSessionResolver().getOrCreateSession(player, template);
    }

}
