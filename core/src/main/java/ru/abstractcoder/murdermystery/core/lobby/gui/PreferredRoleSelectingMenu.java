package ru.abstractcoder.murdermystery.core.lobby.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import ru.abstractcoder.benioapi.gui.template.MenuApi;
import ru.abstractcoder.benioapi.gui.template.MenuTemplate;
import ru.abstractcoder.benioapi.gui.template.item.FixedMenuIcon;
import ru.abstractcoder.benioapi.gui.template.item.MenuItemFactory;
import ru.abstractcoder.benioapi.gui.template.loader.HoconSingleMenuLoader;
import ru.abstractcoder.benioapi.gui.template.loader.SingleMenuLoader;
import ru.abstractcoder.benioapi.gui.template.session.SingleMenuSession;
import ru.abstractcoder.murdermystery.core.game.role.RoleTemplateResolver;
import ru.abstractcoder.murdermystery.core.lobby.player.LobbyPlayer;

public class PreferredRoleSelectingMenu {

    private final MenuApi menuApi;
    private final MenuTemplate<LobbyPlayer, SingleMenuSession<LobbyPlayer>> template;

    public PreferredRoleSelectingMenu(MenuApi menuApi, Config config, ObjectMapper objectMapper,
                                      RoleTemplateResolver roleTemplateResolver) {
        this.menuApi = menuApi;

        SingleMenuLoader<LobbyPlayer, SingleMenuSession<LobbyPlayer>> menuLoader;
        menuLoader = new HoconSingleMenuLoader<>(config, objectMapper);

        roleTemplateResolver.getAllTemplates().forEach(roleTemplate -> {
            char guiChar = Character.toLowerCase(roleTemplate.getType().name().charAt(0));
            menuLoader.setItem(guiChar, (slot, itemData) -> {
                FixedMenuIcon unselectedIcon = new FixedMenuIcon(slot, itemData.toItemStack());
                FixedMenuIcon selectedIcon = new FixedMenuIcon(slot, itemData.toItemBuilder()
                        .withItemMeta()
                        .blankEnchantment()
                        .and().build()
                );

                return MenuItemFactory.create(builder -> builder
                        .perSessionIcon(session -> {
                            if (session.getOwner().getPreferredRole().getType() == roleTemplate.getType()) {
                                return selectedIcon;
                            }
                            return unselectedIcon;
                        })
                        .onClick(click -> click.getIssuer().setPrederredRole(roleTemplate))
                );
            });
        });

        template = menuLoader.load(menuApi.getTemplateBuilder().customSingleTemplate(LobbyPlayer.class))
                .build();
    }

    public void open(LobbyPlayer lobbyPlayer) {
        menuApi.getSessionResolver().getOrCreateSession(lobbyPlayer, template);
    }

}