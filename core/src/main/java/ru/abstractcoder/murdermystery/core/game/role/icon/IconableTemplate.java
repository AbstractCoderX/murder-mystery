package ru.abstractcoder.murdermystery.core.game.role.icon;

public interface IconableTemplate {

    TemplateIcon getIcon();

    default String getName() {
        return getIcon().getName();
    }

}
