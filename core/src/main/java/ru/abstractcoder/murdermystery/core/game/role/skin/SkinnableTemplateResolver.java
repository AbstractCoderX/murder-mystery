package ru.abstractcoder.murdermystery.core.game.role.skin;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import dagger.Reusable;
import ru.abstractcoder.murdermystery.core.config.GeneralConfig;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.role.component.RoleComponentTemplate;

import javax.inject.Inject;
import java.util.List;

@Reusable
public class SkinnableTemplateResolver {

    private final ListMultimap<GameRole.Type, RoleComponentTemplate> purchasableSkinsTemplates = ArrayListMultimap.create();
    private final ListMultimap<GameRole.Type, RoleComponentTemplate> pooledSkinsTemplates = ArrayListMultimap.create();

    @Inject
    public SkinnableTemplateResolver(GeneralConfig generalConfig) {
        generalConfig.game().getProfessionTemplateResolver().getAll().forEach(this::add);
        generalConfig.game().getRoleClassTemplateResolver().getAll().forEach(this::add);
    }

    private void add(RoleComponentTemplate roleComponentTemplate) {
        if (roleComponentTemplate.getSkinResolver().isPurchasable()) {
            purchasableSkinsTemplates.put(roleComponentTemplate.roleType(), roleComponentTemplate);
        } else {
            pooledSkinsTemplates.put(roleComponentTemplate.roleType(), roleComponentTemplate);
        }
    }

    public List<RoleComponentTemplate> getPurchasableSkinsTemplates(GameRole.Type roleType) {
        return purchasableSkinsTemplates.get(roleType);
    }

    public List<RoleComponentTemplate> getPooledSkinsTemplate(GameRole.Type roleType) {
        return purchasableSkinsTemplates.get(roleType);
    }

}
