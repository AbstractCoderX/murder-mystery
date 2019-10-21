package ru.abstractcoder.murdermystery.core.game.role;

import com.fasterxml.jackson.annotation.JsonCreator;
import ru.abstractcoder.murdermystery.core.game.role.icon.IconableTemplate;
import ru.abstractcoder.murdermystery.core.game.role.icon.TemplateIcon;

public class RoleTemplate implements IconableTemplate {

    private final GameRole.Type type;
    private final TemplateIcon icon;
    private final boolean extraPointable;

    @JsonCreator
    public RoleTemplate(GameRole.Type type, TemplateIcon icon, boolean extraPointable) {
        this.type = type;
        this.icon = icon;
        this.extraPointable = extraPointable;
    }

    public GameRole.Type getType() {
        return type;
    }

    @Override
    public TemplateIcon getIcon() {
        return icon;
    }

    public boolean isExtraPointable() {
        return extraPointable;
    }

}