package ru.abstractcoder.murdermystery.core.slotbar.click;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.bukkit.event.player.PlayerInteractEvent;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CommandClickHandler.class, name = "command"),
        @JsonSubTypes.Type(value = StandartActionClickHandler.class, name = "standart_action")
})
public interface ClickHandler {

    void handleInteract(PlayerInteractEvent event);

}