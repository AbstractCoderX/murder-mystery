package ru.abstractcoder.murdermystery.core.rating;

import ru.abstractcoder.murdermystery.core.rating.rank.Rank;

public interface Rating {

    int value();

    void incrementBy(int amount);

    void decrementBy(int amount);

    Rank getRank();

}
