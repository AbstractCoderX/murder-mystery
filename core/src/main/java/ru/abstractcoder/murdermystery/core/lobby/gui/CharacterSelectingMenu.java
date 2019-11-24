package ru.abstractcoder.murdermystery.core.lobby.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.typesafe.config.Config;
import dagger.Reusable;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ru.abstractcoder.benioapi.config.HoconConfig;
import ru.abstractcoder.benioapi.function.UncheckedPredicate;
import ru.abstractcoder.benioapi.gui.template.MenuApi;
import ru.abstractcoder.benioapi.gui.template.MenuTemplate;
import ru.abstractcoder.benioapi.gui.template.PaginatedMenuTemplate;
import ru.abstractcoder.benioapi.gui.template.item.DynamicMenuIcon;
import ru.abstractcoder.benioapi.gui.template.item.FixedMenuIcon;
import ru.abstractcoder.benioapi.gui.template.item.MenuItem;
import ru.abstractcoder.benioapi.gui.template.item.MenuItemBuilder.ConditionalItemCreator;
import ru.abstractcoder.benioapi.gui.template.item.MenuItemFactory;
import ru.abstractcoder.benioapi.gui.template.loader.HoconPaginatedMenuLoader;
import ru.abstractcoder.benioapi.gui.template.loader.PaginatedMenuLoader;
import ru.abstractcoder.benioapi.gui.template.session.DefaultPaginatedMenuSession;
import ru.abstractcoder.benioapi.item.ItemBuilder;
import ru.abstractcoder.benioapi.item.ItemData;
import ru.abstractcoder.murdermystery.core.config.GeneralConfig;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.role.RoleTemplate;
import ru.abstractcoder.murdermystery.core.game.role.classed.RoleClass;
import ru.abstractcoder.murdermystery.core.game.role.component.RoleComponent;
import ru.abstractcoder.murdermystery.core.game.role.component.RoleComponentTemplate;
import ru.abstractcoder.murdermystery.core.game.role.skin.SkinResolver;
import ru.abstractcoder.murdermystery.core.game.role.skin.SkinnableTemplateRepository;
import ru.abstractcoder.murdermystery.core.lobby.player.LobbyPlayer;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Reusable
public class CharacterSelectingMenu {

    private final MenuApi menuApi;
    private MenuTemplate<LobbyPlayer, Session> template;

    public void open(LobbyPlayer player) {
        menuApi.getSessionResolver().getOrCreateSession(player, template);
    }

    @Inject
    public CharacterSelectingMenu(MenuApi menuApi, @Named("gui") HoconConfig guiConfig, ObjectMapper objectMapper,
            GeneralConfig generalConfig, SkinnableTemplateRepository skinnableTemplateRepository) {
        this.menuApi = menuApi;
        guiConfig.addOnReloadAction(() -> {
            Config config = guiConfig.getHandle().getConfig("characterSelecting");
            PaginatedMenuLoader<LobbyPlayer, Session> loader = new HoconPaginatedMenuLoader<>(config, objectMapper);
            var templateBuilder = menuApi
                    .getTemplateBuilder()
                    .customPaginatedTemplate(Session::new);

            int[] roleGlassSlots = loader.getSlots('|');
            int[] templateSlots = loader.getSlots('x');
            int[] templateGlassSlots = loader.getSlots('/');

            int i = 0;
            for (GameRole.Type roleType : GameRole.Type.VALUES) {
                RoleTemplate roleTemplate = generalConfig.game().getRoleTemplateResolver().getByType(roleType);
                loader.setItem(roleType.getGuiChar(), (slot, itemData) -> MenuItemFactory.create(builder -> builder
                        .cachedIcon(new FixedMenuIcon(slot, itemData.copy()
                                .impose(roleTemplate.getIcon())
                                .toItemStack()
                        ))
                        .onClick(click -> {
                            if (click.getSession().getSelectedRole() == roleType) {
                                return;
                            }
                            click.getSession().setSelectedRole(roleType);
                            click.getSession().updateCache();
                        })
                ));

                int roleGlassSlot = roleGlassSlots[i++];
                templateBuilder.staticItem(MenuItemFactory.create(builder -> builder
                        .conditionalIcon(creator -> prepareGlassItemCreator(creator, roleGlassSlot,
                                session -> session.getSelectedRole() == roleType
                        ))
                ));

                var templateIterator = skinnableTemplateRepository.getSkinsTemplates(roleType).iterator();
                ItemData templateItemData = loader.getItemData('x');
                for (int j = 0; templateIterator.hasNext(); j++) {
                    RoleComponentTemplate template = templateIterator.next();

                    int templateSlot = templateSlots[j];
                    int templateGlassSlot = templateGlassSlots[j];

                    ItemStack templateItemStack = templateItemData.copy().impose(template.getIcon()).toItemStack();
                    templateBuilder
                            .staticItem(MenuItemFactory.create(builder -> builder
                                    .cachedIcon(new FixedMenuIcon(templateSlot, templateItemStack))
                                    .addAvailability(session -> session.getSelectedTemplate() == template)
                                    .onClick(click -> {
                                        if (click.getSession().getSelectedTemplate() == template) {
                                            return;
                                        }
                                        if (roleType.isClassed()) {
                                            click.getIssuer().data().getClassedRoleData(roleType)
                                                    .setSelectedClassType((RoleClass.Type) template.getType());
                                        }
                                        click.getSession().setSelectedTemplate(template);
                                        click.getSession().updateCache();
                                    })
                            ))
                            .staticItem(MenuItemFactory.create(builder -> builder
                                    .addAvailability(session -> session.getSelectedTemplate() == template)
                                    .conditionalIcon(creator -> prepareGlassItemCreator(creator, templateGlassSlot,
                                            session -> session.getSelectedTemplate() == template
                                    ))
                            ));
                }
            }

            template = loader.load(templateBuilder)
                    .perSessionDynamicContent(session -> {
                        if (!session.isAnyTemplateSelected()) {
                            return Collections.emptyList();
                        }
                        SkinResolver skinResolver = session.getSelectedTemplate().getSkinResolver();
                        List<MenuItem<LobbyPlayer, Session, DynamicMenuIcon>> result = new ArrayList<>();

                        skinResolver.getAllSkins().forEach(data -> result.add(MenuItemFactory.create(b -> b
                                .conditionalIcon(creator -> creator
                                        .when(s -> data.isAvailableFor(s.getOwner()))
                                        .then(loader.getDynamicItemData().toMenuIcon())
                                        .otherwise(ItemBuilder.fromMaterial(Material.GRAY_DYE)
                                                .withItemMeta()
                                                .setName("§cЭтот скин недоступен для Вас!")
                                                .buildMenuIcon()
                                        )
                                )
                                .onClick(click -> {
                                    if (!data.isAvailableFor(click.getIssuer())) {
                                        return;
                                    }

                                    RoleComponent.Type componentType = click.getSession().getSelectedTemplate().getType();
                                    click.getIssuer().data().setSelectedSkin(componentType, data.getSkin());
                                })
                        )));

                        return result;
                    })
                    .build();

        });
    }

    private void prepareGlassItemCreator(
            ConditionalItemCreator<LobbyPlayer, Session, FixedMenuIcon> conditionalItemCreator,
            int slot,
            UncheckedPredicate<Session> isSelectedPredicate) {

        conditionalItemCreator
                .withBaseItemConsumer(itemBuilder -> itemBuilder
                        .withItemMeta()
                        .setName(" ")
                )
                .when(isSelectedPredicate)
                .then(itemBuilder -> itemBuilder
                        .material(Material.LIME_STAINED_GLASS_PANE)
                        .buildMenuIcon(slot)
                )
                .otherwise(itemBuilder -> itemBuilder
                        .material(Material.GRAY_STAINED_GLASS_PANE)
                        .buildMenuIcon(slot)
                );
    }

    private static class Session extends DefaultPaginatedMenuSession<Session, LobbyPlayer> {

        private GameRole.Type selectedRole;
        private final Map<GameRole.Type, RoleComponentTemplate> templateMap = Maps.newEnumMap(GameRole.Type.class);

        public Session(PaginatedMenuTemplate<LobbyPlayer, Session> template, LobbyPlayer owner) {
            super(template, owner);
        }

        public boolean isAnyRoleSelected() {
            return selectedRole != null;
        }

        public GameRole.Type getSelectedRole() {
            return selectedRole;
        }

        public void setSelectedRole(GameRole.Type selectedRole) {
            this.selectedRole = selectedRole;
        }

        public RoleComponentTemplate getSelectedTemplate() {
            return !isAnyRoleSelected() ? null : templateMap.get(selectedRole);
        }

        public boolean isAnyTemplateSelected() {
            return isAnyRoleSelected() && templateMap.containsKey(selectedRole);
        }

        public void setSelectedTemplate(RoleComponentTemplate template) {
            Preconditions.checkState(isAnyRoleSelected(), "Role not selected");
            templateMap.put(selectedRole, template);
        }

    }

}
