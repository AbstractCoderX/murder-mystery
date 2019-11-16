package ru.abstractcoder.murdermystery.core.rating.rank.template;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.base.Preconditions;
import ru.abstractcoder.murdermystery.core.rating.rank.Rank;
import ru.abstractcoder.murdermystery.core.rating.rank.ResolvedRank;

public class LeveledRankTemplate extends AbstractRankTemplate {

    @JsonCreator
    public LeveledRankTemplate(String name, int minRating) {
        super(name, minRating);
    }

    @Override
    public Rank getRank(int rating) {
        Preconditions.checkArgument(rating >= minRating,
                "Rating %s smaller than min rating %s",
                rating, minRating
        );

        int additionalRating = rating - minRating;
        Level level = Level.getByAdditionalRating(additionalRating);
        String displayName = name + " " + level.getRoman();

        return new ResolvedRank(this, displayName);
    }

    public enum Level {

        FIRST("I"),
        SECOND("II"),
        THIRD("III"),
        FOURTH("IV"),
        FIFTH("V");

        private final String roman;

        public static final Level[] VALUES = values();

        public static final int RATING_DISTANCE = 100;

        public static Level getByAdditionalRating(int additionalRating) {
            int index = additionalRating / RATING_DISTANCE;
            Preconditions.checkArgument(index >= 0 && index < VALUES.length,
                    "additionalRating must be in range of 0 to %s but got %s",
                    VALUES.length * RATING_DISTANCE,
                    additionalRating
            );

            return VALUES[index];
        }

        Level(String roman) {
            this.roman = roman;
        }

        public String getRoman() {
            return roman;
        }

    }

}
