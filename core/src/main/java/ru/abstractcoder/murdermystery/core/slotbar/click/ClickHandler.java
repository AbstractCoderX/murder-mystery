package ru.abstractcoder.murdermystery.core.slotbar.click;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.bukkit.event.player.PlayerInteractEvent;
import ru.abstractcoder.murdermystery.core.lobby.player.LobbyPlayer;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type",
        defaultImpl = StandartActionClickHandler.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CommandClickHandler.class, name = "command"),
        @JsonSubTypes.Type(value = StandartActionClickHandler.class, name = "standart_action")
})
public interface ClickHandler {

    void handleInteract(PlayerInteractEvent event, LobbyPlayer player);

}