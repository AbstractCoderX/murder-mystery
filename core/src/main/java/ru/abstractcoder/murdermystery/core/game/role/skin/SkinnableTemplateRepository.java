package ru.abstractcoder.murdermystery.core.game.role.skin;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import dagger.Reusable;
import ru.abstractcoder.murdermystery.core.config.GeneralConfig;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.role.component.RoleComponentTemplate;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Reusable
public class SkinnableTemplateRepository {

    private final Collection<RoleComponentTemplate> allTemplates = new ArrayList<>();
    private final ListMultimap<GameRole.Type, RoleComponentTemplate> purchasableSkinsTemplates = ArrayListMultimap.create();
    private final ListMultimap<GameRole.Type, RoleComponentTemplate> pooledSkinsTemplates = ArrayListMultimap.create();

    @Inject
    public SkinnableTemplateRepository(GeneralConfig generalConfig) {
        generalConfig.game().getProfessionTemplateResolver().getAll().forEach(this::add);
        generalConfig.game().getRoleClassTemplateResolver().getAll().forEach(this::add);
    }

    private void add(RoleComponentTemplate template) {
        if (template.getSkinResolver().isPurchasable()) {
            put(purchasableSkinsTemplates, template);
        } else {
            put(pooledSkinsTemplates, template);
        }
        allTemplates.add(template);
    }

    private void put(Multimap<GameRole.Type, RoleComponentTemplate> multimap, RoleComponentTemplate template) {
        multimap.put(template.getType().getRoleType(), template);
    }

    public List<RoleComponentTemplate> getPurchasableSkinsTemplates(GameRole.Type roleType) {
        return purchasableSkinsTemplates.get(roleType);
    }

    public List<RoleComponentTemplate> getPooledSkinsTemplate(GameRole.Type roleType) {
        return purchasableSkinsTemplates.get(roleType);
    }

    public Collection<RoleComponentTemplate> getAllTemplates() {
        return allTemplates;
    }

}
