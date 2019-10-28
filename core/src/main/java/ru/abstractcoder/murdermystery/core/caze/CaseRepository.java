package ru.abstractcoder.murdermystery.core.caze;

import dagger.Reusable;
import ru.abstractcoder.benioapi.database.util.QueryFactory;
import ru.abstractcoder.murdermystery.core.config.GeneralConfig;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

@Reusable
public class CaseRepository {

    private final QueryFactory queryFactory;
    private final GeneralConfig generalConfig;

    @Inject
    public CaseRepository(QueryFactory queryFactory, GeneralConfig generalConfig) {
        this.queryFactory = queryFactory;
        this.generalConfig = generalConfig;
    }

    public CompletableFuture<Void> giveCase(String playerName, String caseKey, int amount) {
        //language=MySQL
        String sql = String.format("insert into %s.case_data(username, case_key, amount) values (?, ?, ?) " +
                "on duplicate key update amount = amount + ?", generalConfig.mysql().getCaseDatabase());
        return queryFactory.completableQuery().execute(sql, playerName.toLowerCase(), caseKey, amount, amount);
    }

}
