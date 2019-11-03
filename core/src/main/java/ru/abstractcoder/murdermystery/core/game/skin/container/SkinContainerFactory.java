package ru.abstractcoder.murdermystery.core.game.skin.container;

import dagger.Reusable;
import ru.abstractcoder.murdermystery.core.config.GeneralConfig;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.role.component.RoleComponent;
import ru.abstractcoder.murdermystery.core.game.role.profession.Profession;
import ru.abstractcoder.murdermystery.core.game.role.skin.SkinService;
import ru.abstractcoder.murdermystery.core.game.skin.Skin;
import ru.abstractcoder.murdermystery.core.game.skin.view.ExcellingCivilianSkinView;
import ru.abstractcoder.murdermystery.core.game.skin.view.MurderSkinView;
import ru.abstractcoder.murdermystery.core.game.skin.view.SingleSkinView;
import ru.abstractcoder.murdermystery.core.game.skin.view.SkinView;
import ru.abstractcoder.murdermystery.core.lobby.player.LobbyPlayer;

import javax.inject.Inject;

@Reusable
public class SkinContainerFactory {

    private final SkinService skinService;
    private final GeneralConfig generalConfig;

    @Inject
    public SkinContainerFactory(SkinService skinService, GeneralConfig generalConfig) {
        this.skinService = skinService;
        this.generalConfig = generalConfig;
    }

    public SkinContainer createFor(LobbyPlayer player) {
        GameRole role = player.getBalancedRole();
        Skin simpleSkin = generalConfig.game().getCivilianSkinPool().takeSkin();
        RoleComponent component = role.getComponent();
        RoleComponent.Type componentType = component.getType();

        if (componentType == Profession.Type.DEFAULT) {
            return new SkinContainer(simpleSkin, new SingleSkinView(simpleSkin));
        }

        Skin realSkin = skinService.getSelectedSkin(player, componentType);
        SkinView skinView;
        if (role.getType() == GameRole.Type.MURDER) {
            skinView = new MurderSkinView(realSkin, simpleSkin);
        } else {
            skinView = new ExcellingCivilianSkinView(realSkin, simpleSkin);
        }

        return new SkinContainer(realSkin, skinView);
    }

    public SkinContainer createSimpleCivilian() {

    }

}
