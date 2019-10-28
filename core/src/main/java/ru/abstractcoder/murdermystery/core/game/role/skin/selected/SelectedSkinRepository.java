package ru.abstractcoder.murdermystery.core.game.role.skin.selected;

import dagger.Reusable;
import ru.abstractcoder.benioapi.database.util.QueryFactory;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.role.component.RoleComponent;
import ru.abstractcoder.murdermystery.core.game.role.skin.SkinRepository;
import ru.abstractcoder.murdermystery.core.game.skin.Skin;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Reusable
public class SelectedSkinRepository {

    private final QueryFactory queryFactory;
    private final SkinRepository skinRepository;

    @Inject
    public SelectedSkinRepository(QueryFactory queryFactory, SkinRepository skinRepository) {
        this.queryFactory = queryFactory;
        this.skinRepository = skinRepository;
    }

    public CompletableFuture<Map<RoleComponent.Type, Skin>> load(String name) {
        //language=MySQL
        String sql = "select role, component, selected_skin from selected_skins where username = ?";

        return queryFactory.completableQuery().query(sql, rs -> {
            Map<RoleComponent.Type, Skin> result = new HashMap<>();

            while (rs.next()) {
                GameRole.Type roleType = GameRole.Type.valueOf(rs.getString("role"));
                String componentTypeName = rs.getString("component");
                RoleComponent.Type componentType = RoleComponent.TypeResolver.resolve(roleType, componentTypeName);

                String selectedSkinId = rs.getString("selected_skin");
                Skin selectedSkin = skinRepository.findById(selectedSkinId);

                result.put(componentType, selectedSkin);
            }

            return result;
        }, name.toLowerCase());
    }

}
