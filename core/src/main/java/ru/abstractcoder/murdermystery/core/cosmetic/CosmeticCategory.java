package ru.abstractcoder.murdermystery.core.cosmetic;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ru.abstractcoder.benioapi.item.ItemData;
import ru.abstractcoder.murdermystery.core.cosmetic.impl.DeathSoundCosmeticCategory;
import ru.abstractcoder.murdermystery.core.cosmetic.impl.KillEffectCosmeticCategory;
import ru.abstractcoder.murdermystery.core.cosmetic.impl.KillMessageCosmeticCategory;
import ru.abstractcoder.murdermystery.core.cosmetic.impl.VictoryFireworksCosmeticCategory;

import java.util.Collection;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @Type(value = DeathSoundCosmeticCategory.class, name = "DEATH_SOUND"),
        @Type(value = KillEffectCosmeticCategory.class, name = "KILL_EFFECT"),
        @Type(value = KillMessageCosmeticCategory.class, name = "KILL_MESSAGE"),
        @Type(value = VictoryFireworksCosmeticCategory.class, name = "VICTORY_FIREWORKS")
})
public interface CosmeticCategory {

    Type getType();

    Cosmetic getDefaultCosmetic();

    Collection<PremiumCosmetic> getPremiusCosmetics();

    Cosmetic getCosmeticById(String id);

    ItemData getIcon();

    enum Type {
        DEATH_SOUND('a'),
        KILL_EFFECT('b'),
        KILL_MESSAGE('c'),
        VICTORY_FIREWORKS('d');

        private final char guiChar;

        Type(char guiChar) {
            this.guiChar = guiChar;
        }

        public char getGuiChar() {
            return guiChar;
        }
    }

}
