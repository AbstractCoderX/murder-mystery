package ru.abstractcoder.murdermystery.core.lobby.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import org.bukkit.Material;
import ru.abstractcoder.benioapi.config.HoconConfig;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.benioapi.gui.template.MenuApi;
import ru.abstractcoder.benioapi.gui.template.MenuTemplate;
import ru.abstractcoder.benioapi.gui.template.item.MenuItemFactory;
import ru.abstractcoder.benioapi.gui.template.loader.HoconSingleMenuLoader;
import ru.abstractcoder.benioapi.gui.template.loader.SingleMenuLoader;
import ru.abstractcoder.benioapi.gui.template.session.SingleMenuSession;
import ru.abstractcoder.murdermystery.core.config.GeneralConfig;
import ru.abstractcoder.murdermystery.core.config.Messages;
import ru.abstractcoder.murdermystery.core.lobby.player.LobbyPlayer;
import ru.abstractcoder.murdermystery.economy.EconomyService;

import javax.inject.Named;

public class ShopMenu {

    private final MenuApi menuApi;
    private MenuTemplate<LobbyPlayer, SingleMenuSession<LobbyPlayer>> template;

    public ShopMenu(MenuApi menuApi, @Named("gui") HoconConfig guiConfig, ObjectMapper objectMapper,
            GeneralConfig generalConfig, MsgConfig<Messages> msgConfig, EconomyService economyService) {
        this.menuApi = menuApi;
        guiConfig.addOnReloadAction(() -> {
            Config config = guiConfig.getHandle().getConfig("shop");
            SingleMenuLoader<LobbyPlayer, SingleMenuSession<LobbyPlayer>> loader;
            loader = new HoconSingleMenuLoader<>(config, objectMapper);

            loader
                    .setItem('e', (slot, itemData) -> MenuItemFactory.create(builder -> builder
                            .cachedIcon(itemData
                                    .loreReplacements(s -> s.replace("{price}", /*TODO*/))
                                    .toMenuIcon(slot)
                            )
                            .onClick(click -> {
                                //TODO
                            })
                    ));

            generalConfig.game().getRoleClassTemplateResolver().getAllPurchasableTemplates()
                    .forEach(tmplt -> loader.setItem(tmplt.getShopMenuChar(), (slot, itemData) -> {
                        String price = String.valueOf(tmplt.getPrice());
                        return MenuItemFactory.create(builder -> builder
                                .cachedIcon(itemData
                                        .impose(tmplt.getIcon())
                                        .loreReplacements(s -> s.replace("{price}", price))
                                        .toMenuIcon(slot)
                                )
                                .onClick(click -> {
                                    LobbyPlayer player = click.getIssuer();
                                    if (player.isRoleClassPurchased(tmplt)) {
                                        click.tempErrorSession(msgConfig.get(Messages.gui__class_already_purchased))
                                                .start();
                                        return;
                                    }

                                    int balance = economyService.getCachedBalance(player);
                                    if (balance < tmplt.getPrice()) {
                                        click.tempErrorSession(msgConfig.get(Messages.general__not_enough_coins))
                                                .start();
                                        return;
                                    }

                                    int newBalance = balance - tmplt.getPrice();
                                    economyService.setBalanceAsync(player, newBalance);
                                    click.tempItemSession(msgConfig.get(Messages.gui__class_succ_purchased))
                                            .material(Material.GREEN_STAINED_GLASS_PANE)
                                            .start();
                                })
                        );
                    }));

            template = loader.load(menuApi.getTemplateBuilder().customSingleTemplate(LobbyPlayer.class))
                    .build();
        });
    }

    public void open(LobbyPlayer player) {
        menuApi.getSessionResolver().getOrCreateSession(player, template);
    }

}
