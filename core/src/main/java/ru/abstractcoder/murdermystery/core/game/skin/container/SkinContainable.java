package ru.abstractcoder.murdermystery.core.game.skin.container;

import java.util.UUID;

public interface SkinContainable {

    UUID getUniqueId();

    SkinContainer getSkinContainer();

    void setSkinContainer(SkinContainer skinContainer);

}