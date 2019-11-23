package ru.abstractcoder.murdermystery.core.game.role.chance;

import dagger.Reusable;
import ru.abstractcoder.benioapi.database.util.QueryFactory;
import ru.abstractcoder.murdermystery.core.data.ClassedRoleData;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.role.classed.RoleClass;

import javax.inject.Inject;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Reusable
public class MysqlClassedRoleDataRepository implements ClassedRoleDataRepository {

    private final QueryFactory queryFactory;

    @Inject
    public MysqlClassedRoleDataRepository(QueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public CompletableFuture<Map<GameRole.Type, ClassedRoleData>> load(String name) {
        //language=MySQL
        String sql = "select chance_points from classed_role_player_data where username = ?";
        return queryFactory.completableQuery().query(sql, rs -> {
            Map<GameRole.Type, ClassedRoleData> result = new EnumMap<>(GameRole.Type.class);
            while (rs.next()) {
                GameRole.Type roleType = GameRole.Type.valueOf(rs.getString("role"));
                int chancePoints = rs.getInt("chance_points");
                String selectedClassKey = rs.getString("selected_class");
                RoleClass.Type selectedClass = selectedClassKey != null
                                               ? RoleClass.TypeResolver.resolve(roleType, selectedClassKey)
                                               : null;
                result.put(roleType, new ClassedRoleData(chancePoints, selectedClass));
            }
            return result;
        }, name.toLowerCase());
    }

    @Override
    public CompletableFuture<Void> save(String name, Map<GameRole.Type, ClassedRoleData> classedRoleDataMap) {
        //language=MySQL
        String sql = "insert into classed_role_player_data values (?, ?, ?, ?)";

        String nameLower = name.toLowerCase();
        Object[][] params = classedRoleDataMap.entrySet().stream()
                .map(entry -> {
                    GameRole.Type type = entry.getKey();
                    ClassedRoleData data = entry.getValue();

                    return new Object[]{
                            nameLower, type,
                            data.getChancePoints(), data.getSelectedClassType()
                    };
                })
                .toArray(Object[][]::new);

        return queryFactory.completableQuery().executeBatch(sql, params);
    }

    //    @Override
    //    public CompletableFuture<Void> incrementPoints(String name, String role) {
    //        //language=MySQL
    //        String sql = "insert into role_data(username, role, chance_points) values (?, ?)" +
    //                "on duplicate key update chance_points = chance_points + 1";
    //
    //        return queryFactory.completableQuery().update(sql, name.toLowerCase(), role)
    //                .thenApply((__) ->  null);
    //    }

}