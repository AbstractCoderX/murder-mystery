package ru.abstractcoder.murdermystery.core.rating.rank.template;

import com.fasterxml.jackson.annotation.JsonCreator;
import ru.abstractcoder.murdermystery.core.rating.rank.Rank;
import ru.abstractcoder.murdermystery.core.rating.rank.ResolvedRank;

public class FinalRankTemplate extends AbstractRankTemplate {

    private final Rank resolvedRank;

    @JsonCreator
    public FinalRankTemplate(String name, int minRating) {
        super(name, minRating);
        resolvedRank = new ResolvedRank(this, this.name);
    }

    @Override
    public Rank getRank(int rating) {
        return resolvedRank;
    }

}
