package ru.abstractcoder.murdermystery.core.lobby.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import dagger.Reusable;
import org.bukkit.Material;
import ru.abstractcoder.benioapi.config.HoconConfig;
import ru.abstractcoder.benioapi.gui.template.MenuApi;
import ru.abstractcoder.benioapi.gui.template.MenuTemplate;
import ru.abstractcoder.benioapi.gui.template.PaginatedMenuTemplate;
import ru.abstractcoder.benioapi.gui.template.item.DynamicMenuIcon;
import ru.abstractcoder.benioapi.gui.template.item.MenuItemBuilder;
import ru.abstractcoder.benioapi.gui.template.item.MenuItemFactory;
import ru.abstractcoder.benioapi.gui.template.loader.HoconPaginatedMenuLoader;
import ru.abstractcoder.benioapi.gui.template.loader.PaginatedMenuLoader;
import ru.abstractcoder.benioapi.gui.template.session.DefaultPaginatedMenuSession;
import ru.abstractcoder.benioapi.item.ItemBuilder;
import ru.abstractcoder.murdermystery.core.config.GeneralConfig;
import ru.abstractcoder.murdermystery.core.cosmetic.CosmeticCategory;
import ru.abstractcoder.murdermystery.core.lobby.player.LobbyPlayer;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.stream.Collectors;

@Reusable
public class CosmeticSelectingMenu {

    private final MenuApi menuApi;
    private MenuTemplate<LobbyPlayer, Session> template;

    @Inject
    public CosmeticSelectingMenu(MenuApi menuApi, @Named("gui") HoconConfig guiConfig, ObjectMapper objectMapper,
            GeneralConfig generalConfig) {
        this.menuApi = menuApi;

        guiConfig.addOnReloadAction(() -> {
            Config config = guiConfig.getHandle().getConfig("cosmeticSelecting");

            PaginatedMenuLoader<LobbyPlayer, Session> loader = new HoconPaginatedMenuLoader<>(config, objectMapper);
            var templateBuilder = menuApi
                    .getTemplateBuilder()
                    .customPaginatedTemplate(Session::new);

            {
                int[] glassSlots = loader.getSlots('|');
                int i = 0;
                for (var category : generalConfig.game().getCosmeticCategoryResolver().getCategories()) {
                    char guiChar = category.getType().getGuiChar();
                    loader.setItem(guiChar, (slot, itemData) -> MenuItemFactory.create(builder -> builder
                            .cachedIcon(itemData.impose(category.getIcon()).toMenuIcon(slot))
                            .onClick(click -> {
                                click.getSession().setSelectedCategory(category);
                                click.getSession().updateCache();
                            })
                    ));

                    int glassSlot = glassSlots[i++];
                    templateBuilder.staticItem(MenuItemFactory.create(builder -> builder
                            .conditionalIcon(creator -> creator
                                    .withBaseItemConsumer(itemBuilder -> itemBuilder
                                            .withItemMeta()
                                            .setName(" ")
                                    )
                                    .when(session -> session.getSelectedCategory().equals(category))
                                    .then(itemBuilder -> itemBuilder
                                            .material(Material.LIME_STAINED_GLASS_PANE)
                                            .buildMenuIcon(glassSlot)
                                    )
                                    .otherwise(itemBuilder -> itemBuilder
                                            .material(Material.GRAY_STAINED_GLASS_PANE)
                                            .buildMenuIcon(glassSlot)
                                    )
                            )
                    ));
                }
            }

            template = loader.load(templateBuilder)
                    .perSessionDynamicContent(session -> session.getSelectedCategory().getPremiusCosmetics()
                            .stream()
                            .map(cosmetic -> MenuItemBuilder.<LobbyPlayer, Session, DynamicMenuIcon>create()
                                    .conditionalIcon(creator -> creator
                                            .withBaseItem(() -> loader.getDynamicItemData()
                                                    .copy()
                                                    .impose(cosmetic.getIconData())
                                                    .toItemBuilder()
                                            )
                                            .when(s -> cosmetic.isAvailableFor(s.getOwner())
                                                    && !s.getOwner()
                                                    .isCosmeticSelected(s.getSelectedCategory(), cosmetic)
                                            )
                                            .then(ItemBuilder::buildMenuIcon)
                                            .when(s -> cosmetic.isAvailableFor(s.getOwner())
                                                    && s.getOwner()
                                                    .isCosmeticSelected(s.getSelectedCategory(), cosmetic)
                                            )
                                            .then(itemBuilder -> itemBuilder
                                                    .withItemMeta()
                                                    .blankEnchantment()
                                                    .buildMenuIcon()
                                            )
                                            .otherwise(ItemBuilder.fromMaterial(Material.GRAY_DYE)
                                                    .withItemMeta()
                                                    .setName("§cЭта косметика недоступна для Вас!")
                                                    .buildMenuIcon()
                                            )
                                    )
                                    .onClick(click -> {
                                        LobbyPlayer player = click.getIssuer();
                                        if (!cosmetic.isAvailableFor(player)) {
                                            return;
                                        }

                                        CosmeticCategory selectedCategory = session.getSelectedCategory();
                                        if (player.isCosmeticSelected(selectedCategory, cosmetic)) {
                                            player.unselectCosmetic(selectedCategory);
                                        } else {
                                            player.selectedCosmetic(selectedCategory, cosmetic);
                                        }

                                        session.updateCache();
                                    })
                                    .build()
                            )
                            .collect(Collectors.toList())
                    )
                    .build();
        });
    }

    public void open(LobbyPlayer lobbyPlayer) {
        menuApi.getSessionResolver().getOrCreateSession(lobbyPlayer, template);
    }

    private static class Session extends DefaultPaginatedMenuSession<Session, LobbyPlayer> {

        private CosmeticCategory selectedCategory;

        public Session(PaginatedMenuTemplate<LobbyPlayer, Session> template, LobbyPlayer owner) {
            super(template, owner);
        }

        public CosmeticCategory getSelectedCategory() {
            return selectedCategory;
        }

        public void setSelectedCategory(CosmeticCategory selectedCategory) {
            this.selectedCategory = selectedCategory;
        }

    }

}
