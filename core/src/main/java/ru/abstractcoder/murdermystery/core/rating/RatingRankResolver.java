package ru.abstractcoder.murdermystery.core.rating;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import ru.abstractcoder.murdermystery.core.rating.rank.template.RankTemplate;

import java.util.List;
import java.util.function.Function;

import static ru.abstractcoder.benioapi.util.BenioCollectors.toCustomMapThrowing;

public class RatingRankResolver {

    private final Int2ObjectMap<RankTemplate> byMinRating;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public RatingRankResolver(List<RankTemplate> rankTemplates) {
        byMinRating = rankTemplates.stream().collect(toCustomMapThrowing(
                RankTemplate::getMinRating,
                Function.identity(),
                () -> new Int2ObjectOpenHashMap<>(rankTemplates.size())
        ));
    }

    public RankTemplate getTemplateByRating(int rating) {
        RankTemplate last = null;
        for (var entry : byMinRating.int2ObjectEntrySet()) {
            int rankRating = entry.getIntKey();
            if (rating < rankRating) {
                last = entry.getValue();
                continue;
            }

            return last;
        }

        throw new IllegalStateException("No rank found for rating: " + rating);
    }

}
