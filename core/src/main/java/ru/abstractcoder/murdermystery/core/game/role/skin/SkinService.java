package ru.abstractcoder.murdermystery.core.game.role.skin;

import dagger.Reusable;
import ru.abstractcoder.murdermystery.core.game.role.component.RoleComponent;
import ru.abstractcoder.murdermystery.core.game.skin.Skin;
import ru.abstractcoder.murdermystery.core.lobby.player.LobbyPlayer;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@Reusable
public class SkinService {

    private final PremiumSkinRepository premiumSkinRepository;
    private final Map<RoleComponent.Type, Skin> defaultSkinMap = new HashMap<>();

    @Inject
    public SkinService(PremiumSkinRepository premiumSkinRepository, SkinnableTemplateRepository skinnableTemplateRepository) {
        this.premiumSkinRepository = premiumSkinRepository;
        skinnableTemplateRepository.getAllTemplates().forEach(template -> {
            defaultSkinMap.computeIfAbsent(template.getType(), (__) -> template.getSkinResolver().getDefaultSkin());
        });
    }

    public Skin getSkin(RoleComponent.Type componentType, String key) {
        if (key.equals("default")) {
            return defaultSkinMap.get(componentType);
        }
        return premiumSkinRepository.findById(key);
    }

    public Skin getSelectedSkin(LobbyPlayer player, RoleComponent.Type componentType) {
        return player.getSelectedSkin(componentType, defaultSkinMap.get(componentType));
    }

}
