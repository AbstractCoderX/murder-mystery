package ru.abstractcoder.murdermystery.core.statistic;

import dagger.Reusable;
import ru.abstractcoder.benioapi.database.util.QueryFactory;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

@Reusable
public class StatisticRepository {

    private final QueryFactory queryFactory;

    @Inject
    public StatisticRepository(QueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public CompletableFuture<PlayerStatistic> load(String name) {
        return queryFactory.completableQuery().query(
                "select wins, defeats, kills, deaths, golds_picked_up, rating rating from statistic " +
                        "where username = ?",
                rs -> {
                    if (!rs.next()) {
                        return new PlayerStatistic();
                    }

                    int wins = rs.getInt("wins");
                    int defeats = rs.getInt("defeats");
                    int kills = rs.getInt("kills");
                    int deaths = rs.getInt("deaths");
                    int goldsPickedUp = rs.getInt("golds_picked_up");
                    int rating = rs.getInt("rating");

                    return new PlayerStatistic(wins, defeats, kills, deaths, goldsPickedUp, rating);
                },
                name.toLowerCase()
        );
    }

    public CompletableFuture<Void> save(String name, PlayerStatistic statistic) {
        return queryFactory.completableQuery().execute(
                //language=MySQL
                "insert into statistic values (?, ?, ?, ?, ?, ?, ?) on duplicate key update " +
                        "wins = values(wins), defeats = values(defeats), kills = values(kills), " +
                        "deaths = values(deaths), golds_picked_up = values(golds_picked_up), " +
                        "rating = values(rating)",
                name, statistic.getWins(), statistic.getDefeats(),
                statistic.getKills(), statistic.getDeaths(),
                statistic.getGoldsPickedUp(), statistic.getRating()
        );
    }

}
