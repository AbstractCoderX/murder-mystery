package ru.abstractcoder.murdermystery.core.game.role.classed.template.repository;

import dagger.Reusable;
import ru.abstractcoder.benioapi.database.util.QueryFactory;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.role.classed.RoleClass;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Reusable
public class PurchasedRoleClassesRepository {

    private final QueryFactory queryFactory;

    @Inject
    public PurchasedRoleClassesRepository(QueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public CompletableFuture<Set<RoleClass.Type>> load(String username) {
        //language=MySQL
        String sql = "select role, class from purchased_role_classes where username = ?";
        return queryFactory.completableQuery().query(sql, rs -> {
            Set<RoleClass.Type> result = new HashSet<>();

            while (rs.next()) {
                GameRole.Type roleType = GameRole.Type.valueOf(rs.getString("role"));
                String roleClass = rs.getString("class");
                result.add(RoleClass.TypeResolver.resolve(roleType, roleClass));
            }

            return result;
        }, username.toLowerCase());
    }

}
