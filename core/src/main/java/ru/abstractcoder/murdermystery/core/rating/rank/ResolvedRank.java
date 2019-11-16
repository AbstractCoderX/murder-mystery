package ru.abstractcoder.murdermystery.core.rating.rank;

import ru.abstractcoder.murdermystery.core.rating.rank.template.RankTemplate;

public class ResolvedRank implements Rank {

    private final RankTemplate template;
    private final String displayName;

    public ResolvedRank(RankTemplate template, String displayName) {
        this.template = template;
        this.displayName = displayName;
    }

    @Override
    public String getName() {
        return template.getName();
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public int getMinRating() {
        return template.getMinRating();
    }

}
