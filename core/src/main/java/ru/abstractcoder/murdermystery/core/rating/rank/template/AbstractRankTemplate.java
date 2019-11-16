package ru.abstractcoder.murdermystery.core.rating.rank.template;

import ru.abstractcoder.benioapi.util.ColorUtils;

public abstract class AbstractRankTemplate implements RankTemplate {

    protected final String name;
    protected final int minRating;

    protected AbstractRankTemplate(String name, int minRating) {
        this.name = ColorUtils.color(name);
        this.minRating = minRating;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getMinRating() {
        return minRating;
    }

}
