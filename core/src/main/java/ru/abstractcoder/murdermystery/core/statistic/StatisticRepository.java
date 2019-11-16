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
                "select wins, defeats, rating from statistic where username = ?",
                rs -> {
                    if (!rs.next()) {
                        return new PlayerStatistic();
                    }

                    int wins = rs.getInt("wins");
                    int defeats = rs.getInt("defeats");
                    int kills = rs.getInt("kills");
                    int deaths = rs.getInt("deaths");
                    int goldsPickedUp = rs.getInt("goldsPickedUp");
                    int rating = rs.getInt("rating");

                    return new PlayerStatistic(wins, defeats, kills, deaths, goldsPickedUp, rating);
                },
                name.toLowerCase()
        );
    }

    public CompletableFuture<Void> save(String name, PlayerStatistic statistic) {
        return queryFactory.completableQuery().execute(
                //language=MySQL
                "insert into statistic values (?, ?, ?, ?, ?, ?, ?)",
                name, statistic.getWins(), statistic.getDefeats(),
                statistic.getKills(), statistic.getDeaths(),
                statistic.getGoldsPickedUp(), statistic.getRating()
        );
    }

}
