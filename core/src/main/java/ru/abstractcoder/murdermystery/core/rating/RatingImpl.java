package ru.abstractcoder.murdermystery.core.rating;

import ru.abstractcoder.murdermystery.core.rating.rank.Rank;
import ru.abstractcoder.murdermystery.core.rating.rank.template.RankTemplate;
import ru.abstractcoder.murdermystery.core.statistic.PlayerStatistic;

public class RatingImpl implements Rating {

    private final RatingRankResolver ratingRankResolver;
    private final PlayerStatistic statistic;

    public RatingImpl(RatingRankResolver ratingRankResolver, PlayerStatistic statistic) {
        this.ratingRankResolver = ratingRankResolver;
        this.statistic = statistic;
    }

    @Override
    public int value() {
        return statistic.getRating();
    }

    @Override
    public void incrementBy(int amount) {
        statistic.incrementRating(amount);
    }

    @Override
    public void decrementBy(int amount) {
        statistic.decrementRating(amount);
    }

    @Override
    public Rank getRank() {
        int rating = this.value();
        RankTemplate template = ratingRankResolver.getTemplateByRating(rating);
        return template.getRank(rating);
    }

}
