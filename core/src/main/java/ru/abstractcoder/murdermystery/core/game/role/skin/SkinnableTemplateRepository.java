package ru.abstractcoder.murdermystery.core.game.role.skin;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import dagger.Reusable;
import ru.abstractcoder.murdermystery.core.config.GeneralConfig;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.role.component.RoleComponentTemplate;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;

@Reusable
public class SkinnableTemplateRepository {

    private final ListMultimap<GameRole.Type, RoleComponentTemplate> skinsTemplates = ArrayListMultimap.create();

    @Inject
    public SkinnableTemplateRepository(GeneralConfig generalConfig) {
        generalConfig.game().getProfessionTemplateResolver().getAll().forEach(this::add);
        generalConfig.game().getRoleClassTemplateResolver().getAll().forEach(this::add);
    }

    private void add(RoleComponentTemplate template) {
        if (template.hasSkinResolver()) {
            skinsTemplates.put(template.getType().getRoleType(), template);
        }
    }


    public List<RoleComponentTemplate> getSkinsTemplates(GameRole.Type roleType) {
        return skinsTemplates.get(roleType);
    }

    public Collection<RoleComponentTemplate> getAllTemplates() {
        return skinsTemplates.values();
    }

}
