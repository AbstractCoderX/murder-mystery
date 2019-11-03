package ru.abstractcoder.murdermystery.core.game.role.skin;

import com.google.common.base.Preconditions;
import dagger.Reusable;
import org.jetbrains.annotations.NotNull;
import ru.abstractcoder.murdermystery.core.game.skin.Skin;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Reusable
public class PremiumSkinRepository {

    private final Map<String, Skin> byIdMap = new HashMap<>();

    @Inject
    public PremiumSkinRepository(SkinnableTemplateRepository skinnableTemplateRepository) {
        skinnableTemplateRepository.getAllTemplates()
                .stream()
                .map(SkinnableTemplate::getSkinResolver)
                .map(SkinResolver::getPremiumSkins)
                .flatMap(Collection::stream)
                .forEach(data -> byIdMap.put(data.getId(), data.getSkin()));
    }

    @NotNull
    public Skin findById(String id) {
        Skin skin = byIdMap.get(id);
        Preconditions.checkArgument(skin != null,
                "Unknown skin id: %s. Available skin ids: %s",
                id, byIdMap.keySet()
        );
        return skin;
    }

}
