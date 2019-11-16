package ru.abstractcoder.murdermystery.core.rating;

import com.google.common.base.Preconditions;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.rating.rank.Rank;
import ru.abstractcoder.murdermystery.core.rating.rank.template.RankTemplate;

public class StatisticRating implements Rating {

    private final RatingRankResolver ratingRankResolver;
    private GamePlayer gamePlayer;

    public StatisticRating(RatingRankResolver ratingRankResolver) {
        this.ratingRankResolver = ratingRankResolver;
    }

    public void init(GamePlayer gamePlayer) {
        Preconditions.checkState(this.gamePlayer == null, "Already initialized!");
        this.gamePlayer = gamePlayer;
    }

    private GamePlayer gamePlayer() {
        Preconditions.checkState(gamePlayer != null, "Not initialized yet!");
        return gamePlayer;
    }

    @Override
    public int value() {
        return gamePlayer().getStatistic().getRating();
    }

    @Override
    public void incrementBy(int amount) {
        gamePlayer().getStatistic().incrementRating(amount);
    }

    @Override
    public void decrementBy(int amount) {
        gamePlayer().getStatistic().decrementRating(amount);
    }

    @Override
    public Rank getRank() {
        int rating = this.value();
        RankTemplate template = ratingRankResolver.getTemplateByRating(rating);
        return template.getRank(rating);
    }

}
