package ru.abstractcoder.murdermystery.core.game.action;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.benioapi.item.ItemBuilder;
import ru.abstractcoder.benioapi.util.listener.QuickEvent;
import ru.abstractcoder.benioapi.util.listener.QuickListener;
import ru.abstractcoder.benioapi.util.ticking.Ticking;
import ru.abstractcoder.murdermystery.core.config.Messages;
import ru.abstractcoder.murdermystery.core.event.GamePlayerDeathEvent;
import ru.abstractcoder.murdermystery.core.game.GameEngine;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;

import java.util.Collection;
import java.util.Comparator;

public class SurvivorDetectorAction implements GameAction {

    private static final int COMPASS_SLOT = 4;

    private final GameEngine gameEngine;

    private final ItemStack compassItem;
    private boolean executed = false;

    public SurvivorDetectorAction(GameEngine gameEngine, MsgConfig<Messages> msgConfig) {
        this.gameEngine = gameEngine;
        compassItem = ItemBuilder.fromMaterial(Material.COMPASS)
                .withItemMeta()
                .setName(msgConfig.get(Messages.misc__survivor_detector_compass).asSingleLine())
                .and().build();

        QuickListener.create()
                .event(QuickEvent.builder(GamePlayerDeathEvent.class)
                        .filter(event -> event.getPlayer().getRole().getType() != GameRole.Type.MURDER)
                        .handler(event -> {
                            if (gameEngine.getPlayerResolver().getSurvivors().size() == 1) {
                                this.execute();
                            }
                        })
                        .build()
                )
                .register(gameEngine.getPlugin());
    }

    @Override
    public void execute() {
        if (checkExecuted()) return;

        Player murderPlayer = gameEngine.getPlayerResolver().getMurder().getHandle();
        murderPlayer.getInventory().setItem(COMPASS_SLOT, compassItem);

        gameEngine.benio().getTickingService().register(new Ticking() {
            @Override
            public boolean doTick() {
                Collection<GamePlayer> survivors = gameEngine.getPlayerResolver().getSurvivors();
                if (survivors.isEmpty()) {
                    return true;
                }

                Location murderLoc = murderPlayer.getLocation();
                survivors.stream()
                        .map(GamePlayer::getHandle)
                        .map(Player::getLocation)
                        .min(Comparator.comparing(location -> location.distanceSquared(murderLoc)))
                        .ifPresent(murderPlayer::setCompassTarget);

                return false;
            }

            @Override
            public int getPeriod() {
                return 15;
            }
        });
    }

    private boolean checkExecuted() {
        if (executed) {
            return true;
        }
        executed = true;
        return false;
    }

}
