package ru.abstractcoder.murdermystery.core.game.action;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.benioapi.item.ItemBuilder;
import ru.abstractcoder.benioapi.util.listener.QuickEvent;
import ru.abstractcoder.benioapi.util.listener.QuickListener;
import ru.abstractcoder.benioapi.util.ticking.Ticking;
import ru.abstractcoder.benioapi.util.ticking.TickingService;
import ru.abstractcoder.murdermystery.core.config.Messages;
import ru.abstractcoder.murdermystery.core.event.GamePlayerDeathEvent;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayerResolver;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Comparator;

public class SurvivorDetectorAction implements GameAction {

    private static final int COMPASS_SLOT = 4;

    private final TickingService tickingService;
    private final GamePlayerResolver playerResolver;

    private final ItemStack compassItem;
    private boolean executed = false;

    @Inject
    public SurvivorDetectorAction(Plugin plugin, MsgConfig<Messages> msgConfig, TickingService tickingService,
            GamePlayerResolver playerResolver) {
        this.tickingService = tickingService;
        this.playerResolver = playerResolver;
        compassItem = ItemBuilder.fromMaterial(Material.COMPASS)
                .withItemMeta()
                .setName(msgConfig.get(Messages.misc__survivor_detector_compass).asSingleLine())
                .and().build();

        QuickListener.create()
                .event(QuickEvent.builder(GamePlayerDeathEvent.class)
                        .filter(event -> event.getPlayer().getRole().getType() != GameRole.Type.MURDER)
                        .handler(event -> {
                            if (playerResolver.getSurvivors().size() == 1) {
                                this.execute();
                            }
                        })
                        .build()
                )
                .register(plugin);
    }

    @Override
    public void execute() {
        if (checkExecuted()) return;

        Player murderPlayer = playerResolver.getMurder().getHandle();
        murderPlayer.getInventory().setItem(COMPASS_SLOT, compassItem);

        tickingService.register(new Ticking() {
            @Override
            public boolean doTick() {
                Collection<GamePlayer> survivors = playerResolver.getSurvivors();
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
