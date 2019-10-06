package ru.abstractcoder.murdermystery.economy;

import dagger.Reusable;
import ru.abstractcoder.benioapi.database.util.QueryFactory;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

@Reusable
public class MysqlEconomyRepository implements EconomyRepository {

    private final QueryFactory queryFactory;

    @Inject
    public MysqlEconomyRepository(QueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public CompletableFuture<Integer> loadBalance(String playerName) {
        //language=MySQL
        String sql = "select balance from economy where username=?";

        return queryFactory.completableQuery().query(sql, rs -> {
            if (rs.next()) {
                return rs.getInt("balance");
            }
            return null;
        }, playerName.toLowerCase());
    }

    @Override
    public CompletableFuture<Void> saveBalance(String playerName, int balance) {
        //language=MySQL
        String sql = "insert into economy(username, balance) values (?, ?) on duplicate key update balance=?";

        return queryFactory.completableQuery().execute(sql, playerName.toLowerCase(), balance, balance);
    }

}