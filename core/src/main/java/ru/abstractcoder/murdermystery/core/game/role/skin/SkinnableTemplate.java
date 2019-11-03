package ru.abstractcoder.murdermystery.core.game.role.skin;

public interface SkinnableTemplate {

    default boolean hasSkinResolver() {
        return getSkinResolver() != null;
    }

    SkinResolver getSkinResolver();

}
