package ru.abstractcoder.murdermystery.core.game.role;

import com.fasterxml.jackson.annotation.JsonCreator;
import ru.abstractcoder.benioapi.util.ColorUtils;

public class RoleTemplate {

    private final GameRole.Type type;
    private final String name;
    private final boolean extraPointable;

    @JsonCreator
    public RoleTemplate(GameRole.Type type, String name, boolean extraPointable) {
        this.type = type;
        this.name = ColorUtils.color(name);
        this.extraPointable = extraPointable;
    }

    public GameRole.Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public boolean isExtraPointable() {
        return extraPointable;
    }

}