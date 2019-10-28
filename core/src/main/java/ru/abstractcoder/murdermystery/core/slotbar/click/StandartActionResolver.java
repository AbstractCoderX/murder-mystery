package ru.abstractcoder.murdermystery.core.slotbar.click;

import org.jetbrains.annotations.NotNull;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.murdermystery.core.config.Messages;
import ru.abstractcoder.murdermystery.core.lobby.LobbyEngine;
import ru.abstractcoder.murdermystery.core.lobby.RuleBook;
import ru.abstractcoder.murdermystery.core.lobby.gui.PreferredRoleSelectingMenu;
import ru.abstractcoder.murdermystery.core.lobby.player.LobbyPlayer;

import javax.inject.Inject;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;

public class StandartActionResolver {

    private final Map<StandartClickAction, Consumer<LobbyPlayer>> actionConsumerMap;

    @Inject
    public StandartActionResolver(MsgConfig<Messages> msgConfig, RuleBook ruleBook,
            PreferredRoleSelectingMenu preferredRoleSelectingMenu, LobbyEngine lobbyEngine) {
        actionConsumerMap = new EnumMap<>(StandartClickAction.class);
        actionConsumerMap.put(StandartClickAction.LEAVE_TO_HUB, player ->
                msgConfig.get(Messages.general__you_leave_arena).kickWithReason(player)
        );
        actionConsumerMap.put(StandartClickAction.OPEN_RULE_BOOK, ruleBook::openFor);
        actionConsumerMap.put(StandartClickAction.OPEN_ROLE_SELECTING, preferredRoleSelectingMenu::open);
        actionConsumerMap.put(StandartClickAction.OPEN_COSMETIC, ) //TODO
        actionConsumerMap.put(StandartClickAction.OPEN_SHOP, ) //TODO
//        TODO
    }

    public @NotNull Consumer<LobbyPlayer> resolve(StandartClickAction action) {
        return actionConsumerMap.get(action);
    }

}