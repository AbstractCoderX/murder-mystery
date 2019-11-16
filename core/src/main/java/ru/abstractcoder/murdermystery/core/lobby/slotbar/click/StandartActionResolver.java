package ru.abstractcoder.murdermystery.core.lobby.slotbar.click;

import org.jetbrains.annotations.NotNull;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.murdermystery.core.config.Msg;
import ru.abstractcoder.murdermystery.core.lobby.RuleBook;
import ru.abstractcoder.murdermystery.core.lobby.gui.CharacterSelectingMenu;
import ru.abstractcoder.murdermystery.core.lobby.gui.CosmeticSelectingMenu;
import ru.abstractcoder.murdermystery.core.lobby.gui.PreferredRoleSelectingMenu;
import ru.abstractcoder.murdermystery.core.lobby.gui.ShopMenu;
import ru.abstractcoder.murdermystery.core.lobby.player.LobbyPlayer;

import javax.inject.Inject;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;

public class StandartActionResolver {

    private final Map<StandartClickAction, Consumer<LobbyPlayer>> actionConsumerMap;

    @Inject
    public StandartActionResolver(MsgConfig<Msg> msgConfig, RuleBook ruleBook,
            PreferredRoleSelectingMenu preferredRoleSelectingMenu, ShopMenu shopMenu,
            CharacterSelectingMenu characterSelectingMenu, CosmeticSelectingMenu cosmeticSelectingMenu) {
        actionConsumerMap = new EnumMap<>(StandartClickAction.class);
        actionConsumerMap.put(StandartClickAction.LEAVE_TO_HUB, player ->
                msgConfig.get(Msg.general__you_leave_arena).kickWithReason(player)
        );
        actionConsumerMap.put(StandartClickAction.OPEN_RULE_BOOK, ruleBook::openFor);
        actionConsumerMap.put(StandartClickAction.OPEN_ROLE_SELECTING, preferredRoleSelectingMenu::open);
        actionConsumerMap.put(StandartClickAction.OPEN_CHARACTER_SELECTING, characterSelectingMenu::open);
        actionConsumerMap.put(StandartClickAction.OPEN_COSMETIC, cosmeticSelectingMenu::open);
        actionConsumerMap.put(StandartClickAction.OPEN_SHOP, shopMenu::open);
    }

    public @NotNull Consumer<LobbyPlayer> resolve(StandartClickAction action) {
        return actionConsumerMap.get(action);
    }

}