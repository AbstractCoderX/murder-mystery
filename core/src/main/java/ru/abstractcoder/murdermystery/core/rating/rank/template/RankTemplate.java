package ru.abstractcoder.murdermystery.core.rating.rank.template;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ru.abstractcoder.murdermystery.core.rating.rank.Rank;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type",
        defaultImpl = LeveledRankTemplate.class
)
@JsonSubTypes({
        @Type(value = FinalRankTemplate.class, name = "final")
})
public interface RankTemplate {

    String getName();

    int getMinRating();

    Rank getRank(int rating);

}
