package ru.abstractcoder.murdermystery.core.cosmetic;

import org.jetbrains.annotations.Nullable;

public interface Cosmetic {

    String getId();

    CosmeticCategory getCategory();

    @Nullable
    Logic getLogic();

    //Marker
    interface Logic {
    }

}