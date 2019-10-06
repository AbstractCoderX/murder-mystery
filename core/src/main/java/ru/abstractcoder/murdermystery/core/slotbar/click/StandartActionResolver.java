package ru.abstractcoder.murdermystery.core.slotbar.click;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.murdermystery.core.config.Messages;
import ru.abstractcoder.murdermystery.core.lobby.RuleBook;

import javax.inject.Inject;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;

public class StandartActionResolver {

    private final Map<StandartClickAction, Consumer<Player>> actionConsumerMap;

    @Inject
    public StandartActionResolver(MsgConfig<Messages> msgConfig, RuleBook ruleBook) {
        actionConsumerMap = new EnumMap<>(StandartClickAction.class);
        actionConsumerMap.put(StandartClickAction.LEAVE_TO_HUB, player ->
                player.kickPlayer(msgConfig.get(Messages.general__you_leave_arena).asSingleLine())
        );
        actionConsumerMap.put(StandartClickAction.OPEN_RULE_BOOK, ruleBook::openFor);
//        TODO
    }
    
    public @NotNull Consumer<Player> resolve(StandartClickAction action) {
        return actionConsumerMap.get(action);
    }

}