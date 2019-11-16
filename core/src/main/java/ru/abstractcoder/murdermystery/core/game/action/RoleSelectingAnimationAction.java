package ru.abstractcoder.murdermystery.core.game.action;

import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.benioapi.util.timer.DefinedTimerBuilderFactory;
import ru.abstractcoder.murdermystery.core.config.GeneralConfig;
import ru.abstractcoder.murdermystery.core.config.Msg;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayerResolver;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.role.RoleTemplateResolver;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RoleSelectingAnimationAction implements GameAction {

    private static final Random random = new Random();

    private final MsgConfig<Msg> msgConfig;
    private final DefinedTimerBuilderFactory definedTimerBuilderFactory;
    private final GamePlayerResolver playerResolver;

    private final List<String> animationRoleNames = new ArrayList<>();

    @Inject
    public RoleSelectingAnimationAction(
            GeneralConfig generalConfig,
            MsgConfig<Msg> msgConfig, DefinedTimerBuilderFactory definedTimerBuilderFactory, GamePlayerResolver playerResolver) {
        this.msgConfig = msgConfig;
        this.definedTimerBuilderFactory = definedTimerBuilderFactory;
        this.playerResolver = playerResolver;

        generalConfig.game().getProfessionTemplateResolver().getAll()
                .forEach(template -> animationRoleNames.add(template.getName()));

        RoleTemplateResolver roleTemplateResolver = generalConfig.game().getRoleTemplateResolver();
        animationRoleNames.add(roleTemplateResolver.getByType(GameRole.Type.MURDER).getName());
        animationRoleNames.add(roleTemplateResolver.getByType(GameRole.Type.DETECTIVE).getName());
    }

    @Override
    public void execute() {
        definedTimerBuilderFactory
                .tickTimerBuilder(20)
                .setPeriodTicks(3)
                .setAction(() -> playerResolver.getAll().forEach(gamePlayer -> {
                    String roleName = animationRoleNames.get(random.nextInt(animationRoleNames.size()));
                    msgConfig.get(Msg.game__role_selecting_animation, roleName).sendActionBar(gamePlayer);
                }))
                .setLastAction(() -> {
                    playerResolver.getAll().forEach(gamePlayer -> {
                        gamePlayer.setRoleShowed(true);
                        msgConfig.get(Msg.game__role_animation_end, gamePlayer.getRole().getDisplayName())
                                .sendTitle(gamePlayer);
                    });
                });
    }

}
