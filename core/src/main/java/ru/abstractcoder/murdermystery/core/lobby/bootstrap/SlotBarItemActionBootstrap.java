package ru.abstractcoder.murdermystery.core.lobby.bootstrap;

import dagger.Reusable;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.murdermystery.core.config.Msg;
import ru.abstractcoder.murdermystery.core.lobby.RuleBook;
import ru.abstractcoder.murdermystery.core.lobby.gui.CharacterSelectingMenu;
import ru.abstractcoder.murdermystery.core.lobby.gui.CosmeticSelectingMenu;
import ru.abstractcoder.murdermystery.core.lobby.gui.PreferredRoleSelectingMenu;
import ru.abstractcoder.murdermystery.core.lobby.gui.ShopMenu;
import ru.abstractcoder.murdermystery.core.lobby.slotbar.click.StandartActionResolver;
import ru.abstractcoder.murdermystery.core.lobby.slotbar.click.StandartClickAction;

import javax.inject.Inject;

@Reusable
public class SlotBarItemActionBootstrap {

    private final StandartActionResolver standartActionResolver;
    private final MsgConfig<Msg> msgConfig;
    private final RuleBook ruleBook;
    private final PreferredRoleSelectingMenu preferredRoleSelectingMenu;
    private final ShopMenu shopMenu;
    private final CharacterSelectingMenu characterSelectingMenu;
    private final CosmeticSelectingMenu cosmeticSelectingMenu;

    @Inject
    public SlotBarItemActionBootstrap(StandartActionResolver standartActionResolver,
            MsgConfig<Msg> msgConfig, RuleBook ruleBook,
            PreferredRoleSelectingMenu preferredRoleSelectingMenu, ShopMenu shopMenu,
            CharacterSelectingMenu characterSelectingMenu, CosmeticSelectingMenu cosmeticSelectingMenu) {
        this.standartActionResolver = standartActionResolver;
        this.msgConfig = msgConfig;
        this.ruleBook = ruleBook;
        this.preferredRoleSelectingMenu = preferredRoleSelectingMenu;
        this.shopMenu = shopMenu;
        this.characterSelectingMenu = characterSelectingMenu;
        this.cosmeticSelectingMenu = cosmeticSelectingMenu;
    }

    public void boot() {
        standartActionResolver.register(StandartClickAction.LEAVE_TO_HUB, player ->
                msgConfig.get(Msg.general__you_leave_arena).kickWithReason(player)
        );
        standartActionResolver.register(StandartClickAction.OPEN_RULE_BOOK, ruleBook::openFor);
        standartActionResolver.register(StandartClickAction.OPEN_ROLE_SELECTING, preferredRoleSelectingMenu::open);
        standartActionResolver.register(StandartClickAction.OPEN_CHARACTER_SELECTING, characterSelectingMenu::open);
        standartActionResolver.register(StandartClickAction.OPEN_COSMETIC, cosmeticSelectingMenu::open);
        standartActionResolver.register(StandartClickAction.OPEN_SHOP, shopMenu::open);
    }

}
