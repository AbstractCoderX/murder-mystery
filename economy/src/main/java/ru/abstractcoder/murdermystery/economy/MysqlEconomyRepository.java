package ru.abstractcoder.murdermystery.economy;

import ru.abstractcoder.benioapi.database.util.QueryFactory;

import java.util.concurrent.CompletableFuture;

public class MysqlEconomyRepository implements EconomyRepository {

    private final QueryFactory queryFactory;

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

        return queryFactory.completableQuery().update(sql, playerName.toLowerCase(), balance, balance)
                .thenApply((__) -> null);
    }

}