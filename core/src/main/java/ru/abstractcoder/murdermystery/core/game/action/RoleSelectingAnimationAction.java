package ru.abstractcoder.murdermystery.core.game.action;

import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.murdermystery.core.config.Messages;
import ru.abstractcoder.murdermystery.core.game.GameEngine;
import ru.abstractcoder.murdermystery.core.game.role.RoleTemplate;
import ru.abstractcoder.murdermystery.core.game.role.profession.template.ProfessionResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RoleSelectingAnimationAction implements GameAction {

    private static final Random random = new Random();

    private final GameEngine gameEngine;
    private final MsgConfig<Messages> msgConfig;
    private final List<String> animationRoleNames;

    public RoleSelectingAnimationAction(GameEngine gameEngine, MsgConfig<Messages> msgConfig) {
        this.gameEngine = gameEngine;
        this.msgConfig = msgConfig;

        animationRoleNames = new ArrayList<>();

        ProfessionResolver professionResolver = gameEngine.getProfessionResolver();
        gameEngine.settings().getProfessionTemplateResolver().getAll()
                .forEach(tmplt -> animationRoleNames.add(tmplt.getName()));

        gameEngine.settings().getRoleClassTemplateResolver().getRoleTypes()
                .stream()
                .map(gameEngine.settings().getRoleTemplateResolver()::getByType)
                .map(RoleTemplate::getName)
                .forEach(animationRoleNames::add);
    }

    @Override
    public void execute() {
        gameEngine.benio().definedTimerBuilder()
                .tickTimerBuilder(20)
                .setPeriodTicks(3)
                .setAction(() -> gameEngine.getPlayerResolver().getAll().forEach(gamePlayer -> {
                    String roleName = animationRoleNames.get(random.nextInt(animationRoleNames.size()));
                    msgConfig.get(Messages.game__role_selecting_animation, roleName).sendActionBar(gamePlayer);
                }))
                .setLastAction(() -> {
                    gameEngine.getPlayerResolver().getAll().forEach(gamePlayer -> {
                        gamePlayer.setRoleShowed(true);
                        msgConfig.get(Messages.game__role_animation_end, gamePlayer.getRole().getDisplayName())
                                .sendTitle(gamePlayer);
                    });
                });
    }

}
