package ru.abstractcoder.murdermystery.core.game.role.chance;

import dagger.Reusable;
import ru.abstractcoder.benioapi.database.util.QueryFactory;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.role.classed.RoleClass;
import ru.abstractcoder.murdermystery.core.lobby.player.LobbyPlayer.RoleData;

import javax.inject.Inject;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Reusable
public class MysqlRoleDataRepository implements RoleDataRepository {

    private final QueryFactory queryFactory;

    @Inject
    public MysqlRoleDataRepository(QueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public CompletableFuture<Map<GameRole.Type, RoleData>> load(String name) {
        //language=MySQL
        String sql = "select chance_points from role_data where username = ?";
        return queryFactory.completableQuery().query(sql, rs -> {
            Map<GameRole.Type, RoleData> result = new EnumMap<>(GameRole.Type.class);
            while (rs.next()) {
                GameRole.Type roleType = GameRole.Type.valueOf(rs.getString("role"));
                int chancePoints = rs.getInt("chance_points");
                String selectedClassKey = rs.getString("selected_class");
                RoleClass.Type selectedClass = selectedClassKey != null
                        ? RoleClass.TypeResolver.resolve(roleType, selectedClassKey)
                        : null;
                result.put(roleType, new RoleData(chancePoints, selectedClass));
            }
            return result;
        }, name.toLowerCase());
    }

    @Override
    public CompletableFuture<Void> save(String name, GameRole.Type roleType, RoleData roleData) {
        //language=MySQL
        String sql = "insert into role_data values (?, ?, ?, ?)";
        return queryFactory.completableQuery().update(sql,
                name.toLowerCase(), roleType,
                roleData.getChancePoints(), roleData.getSelectedClassType()
        ).thenRun(() -> {});
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