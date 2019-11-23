package ru.abstractcoder.murdermystery.core.lobby.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import dagger.Reusable;
import org.bukkit.Material;
import ru.abstractcoder.benioapi.config.HoconConfig;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.benioapi.gui.template.MenuApi;
import ru.abstractcoder.benioapi.gui.template.MenuTemplate;
import ru.abstractcoder.benioapi.gui.template.click.Click;
import ru.abstractcoder.benioapi.gui.template.item.MenuItemFactory;
import ru.abstractcoder.benioapi.gui.template.loader.HoconSingleMenuLoader;
import ru.abstractcoder.benioapi.gui.template.loader.SingleMenuLoader;
import ru.abstractcoder.benioapi.gui.template.session.SingleMenuSession;
import ru.abstractcoder.murdermystery.core.caze.CaseRepository;
import ru.abstractcoder.murdermystery.core.config.GeneralConfig;
import ru.abstractcoder.murdermystery.core.config.Msg;
import ru.abstractcoder.murdermystery.core.lobby.player.LobbyPlayer;
import ru.abstractcoder.murdermystery.economy.EconomyService;

import javax.inject.Inject;
import javax.inject.Named;

@Reusable
public class ShopMenu {

    private final MenuApi menuApi;
    private final MsgConfig<Msg> msgConfig;
    private final EconomyService economyService;
    private MenuTemplate<LobbyPlayer, SingleMenuSession<LobbyPlayer>> template;

    @Inject
    public ShopMenu(MenuApi menuApi, @Named("gui") HoconConfig guiConfig, ObjectMapper objectMapper,
            GeneralConfig generalConfig, MsgConfig<Msg> msgConfig, EconomyService economyService,
            CaseRepository caseRepository) {
        this.menuApi = menuApi;
        this.msgConfig = msgConfig;
        this.economyService = economyService;
        guiConfig.addOnReloadAction(() -> {
            Config config = guiConfig.getHandle().getConfig("shop");
            SingleMenuLoader<LobbyPlayer, SingleMenuSession<LobbyPlayer>> loader;
            loader = new HoconSingleMenuLoader<>(config, objectMapper);

            loader
                    .setItem('e', (slot, itemData) -> {
                        int price = generalConfig.lobby().misc().getCosmeticCasePrice();
                        return MenuItemFactory.create(builder -> builder
                                .cachedIcon(itemData
                                        .loreReplacements(s -> s
                                                .replace("{price}", String.valueOf(price))
                                        )
                                        .toMenuIcon(slot)
                                )
                                .onClick(click -> {
                                    if (!checkAndWidthrowBalance(click, price)) {
                                        return;
                                    }

                                    String name = click.getIssuer().getName();
                                    caseRepository.giveMurderCase(name, 1);

                                    click.tempItemSession(msgConfig.get(Msg.gui__cosmetic_case_succ_purchased))
                                            .material(Material.GREEN_STAINED_GLASS_PANE)
                                            .start();
                                })
                        );
                    });

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
                                    if (player.data().isRoleClassPurchased(tmplt)) {
                                        click.tempErrorSession(msgConfig.get(Msg.gui__class_already_purchased))
                                                .start();

                                        return;
                                    }

                                    if (checkAndWidthrowBalance(click, tmplt.getPrice())) {
                                        click.tempItemSession(msgConfig.get(Msg.gui__class_succ_purchased))
                                                .material(Material.GREEN_STAINED_GLASS_PANE)
                                                .start();
                                    }
                                })
                        );
                    }));

            template = loader.load(menuApi.getTemplateBuilder().customSingleTemplate(LobbyPlayer.class))
                    .build();
        });
    }

    private boolean checkAndWidthrowBalance(Click<LobbyPlayer, ?> click, int amount) {
        LobbyPlayer player = click.getIssuer();
        int balance = economyService.getCachedBalance(player);
        if (balance < amount) {
            click.tempErrorSession(msgConfig.get(Msg.general__not_enough_coins))
                    .start();
            return false;
        }

        int newBalance = balance - amount;
        economyService.setBalanceAsync(player, newBalance);

        return true;
    }

    public void open(LobbyPlayer player) {
        menuApi.getSessionResolver().getOrCreateSession(player, template);
    }

}
