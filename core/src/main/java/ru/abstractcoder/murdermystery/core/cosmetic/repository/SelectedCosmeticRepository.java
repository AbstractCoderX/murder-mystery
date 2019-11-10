package ru.abstractcoder.murdermystery.core.cosmetic.repository;

import dagger.Reusable;
import ru.abstractcoder.benioapi.database.util.QueryFactory;
import ru.abstractcoder.murdermystery.core.cosmetic.CosmeticCategory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Reusable
public class SelectedCosmeticRepository {

    private final QueryFactory queryFactory;

    @Inject
    public SelectedCosmeticRepository(QueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public CompletableFuture<Map<CosmeticCategory.Type, String>> load(String name) {
        //language=MySQL
        String sql = "select category, cosmetic from selected_cosmetics where username = ?";

        return queryFactory.completableQuery().query(sql, rs -> {
            Map<CosmeticCategory.Type, String> result = new HashMap<>();

            while (rs.next()) {
                CosmeticCategory.Type category = CosmeticCategory.Type.valueOf(rs.getString("category"));
                String cosmeticId = rs.getString("cosmetic");

                result.put(category, cosmeticId);
            }

            return result;
        }, name.toLowerCase());
    }

}
