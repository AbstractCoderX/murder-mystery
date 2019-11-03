package ru.abstractcoder.murdermystery.core.game.role.skin.selected;

import dagger.Reusable;
import ru.abstractcoder.benioapi.database.util.QueryFactory;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.role.component.RoleComponent.Type;
import ru.abstractcoder.murdermystery.core.game.role.component.RoleComponent.TypeResolver;
import ru.abstractcoder.murdermystery.core.game.role.skin.SkinService;
import ru.abstractcoder.murdermystery.core.game.skin.Skin;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Reusable
public class SelectedSkinRepository {

    private final QueryFactory queryFactory;
    private final SkinService skinService;

    @Inject
    public SelectedSkinRepository(QueryFactory queryFactory, SkinService skinService) {
        this.queryFactory = queryFactory;
        this.skinService = skinService;
    }

    public CompletableFuture<Map<Type, Skin>> load(String name) {
        //language=MySQL
        String sql = "select role, component, selected_skin from selected_skins where username = ?";

        return queryFactory.completableQuery().query(sql, rs -> {
            Map<Type, Skin> result = new HashMap<>();

            while (rs.next()) {
                GameRole.Type roleType = GameRole.Type.valueOf(rs.getString("role"));

                Type componentType = TypeResolver.resolve(roleType, rs.getString("component"));
                Skin selectedSkin = skinService.getSkin(componentType, rs.getString("selected_skin"));

                result.put(componentType, selectedSkin);
            }

            return result;
        }, name.toLowerCase());
    }

}
