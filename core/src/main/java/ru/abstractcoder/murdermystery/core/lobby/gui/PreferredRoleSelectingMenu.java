package ru.abstractcoder.murdermystery.core.lobby.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import dagger.Reusable;
import ru.abstractcoder.benioapi.config.HoconConfig;
import ru.abstractcoder.benioapi.gui.template.MenuApi;
import ru.abstractcoder.benioapi.gui.template.MenuTemplate;
import ru.abstractcoder.benioapi.gui.template.item.FixedMenuIcon;
import ru.abstractcoder.benioapi.gui.template.item.MenuItemFactory;
import ru.abstractcoder.benioapi.gui.template.loader.HoconSingleMenuLoader;
import ru.abstractcoder.benioapi.gui.template.loader.SingleMenuLoader;
import ru.abstractcoder.benioapi.gui.template.session.SingleMenuSession;
import ru.abstractcoder.murdermystery.core.config.GeneralConfig;
import ru.abstractcoder.murdermystery.core.lobby.player.LobbyPlayer;

import javax.inject.Inject;
import javax.inject.Named;

@Reusable
public class PreferredRoleSelectingMenu {

    private final MenuApi menuApi;
    private MenuTemplate<LobbyPlayer, SingleMenuSession<LobbyPlayer>> template;

    @Inject
    public PreferredRoleSelectingMenu(MenuApi menuApi, @Named("gui") HoconConfig guiConfig, ObjectMapper objectMapper,
            GeneralConfig generalConfig) {
        this.menuApi = menuApi;

        guiConfig.addOnReloadAction(() -> {
            Config config = guiConfig.getHandle().getConfig("preferredRoleSelecting");
            SingleMenuLoader<LobbyPlayer, SingleMenuSession<LobbyPlayer>> menuLoader;
            menuLoader = new HoconSingleMenuLoader<>(config, objectMapper);

            generalConfig.game().getRoleTemplateResolver().getAll().forEach(roleTemplate -> {
                menuLoader.setItem(roleTemplate.getType().getGuiChar(), (slot, itemData) -> {
                    return MenuItemFactory.create(builder -> builder
                            .cachedIcon(new FixedMenuIcon(slot, itemData.copy()
                                    .impose(roleTemplate.getIcon())
                                    .toItemStack()
                            ))
                            .onClick(click -> click.getIssuer().setPreferredRole(roleTemplate))
                    );
                });
            });

            template = menuLoader.load(menuApi.getTemplateBuilder().customSingleTemplate(LobbyPlayer.class))
                    .build();
        });

    }

    public void open(LobbyPlayer lobbyPlayer) {
        menuApi.getSessionResolver().getOrCreateSession(lobbyPlayer, template);
    }

}