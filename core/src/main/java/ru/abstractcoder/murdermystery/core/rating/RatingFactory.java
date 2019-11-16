package ru.abstractcoder.murdermystery.core.rating;

import dagger.Reusable;
import ru.abstractcoder.murdermystery.core.config.GeneralConfig;
import ru.abstractcoder.murdermystery.core.statistic.PlayerStatistic;

import javax.inject.Inject;

@Reusable
public class RatingFactory {

    private final GeneralConfig generalConfig;

    @Inject
    public RatingFactory(GeneralConfig generalConfig) {
        this.generalConfig = generalConfig;
    }

    public Rating create(PlayerStatistic statistic) {
        return new RatingImpl(generalConfig.game().getRatingRankResolver(), statistic);
    }

}
