package ru.abstractcoder.murdermystery.core.game.misc;

import com.google.common.base.Preconditions;
import dagger.Reusable;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import ru.abstractcoder.benioapi.board.SidebarService;
import ru.abstractcoder.benioapi.board.sidebar.Sidebar;
import ru.abstractcoder.benioapi.board.text.SimpleText;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.benioapi.item.ItemBuilder;
import ru.abstractcoder.benioapi.util.listener.QuickEvent;
import ru.abstractcoder.benioapi.util.listener.QuickListener;
import ru.abstractcoder.benioapi.util.ticking.Ticking;
import ru.abstractcoder.benioapi.util.ticking.TickingService;
import ru.abstractcoder.murdermystery.core.config.Msg;
import ru.abstractcoder.murdermystery.core.game.GameEngine;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayerResolver;
import ru.abstractcoder.murdermystery.core.game.role.murder.classes.WolfRoleClass;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

@Reusable
public class VendingMachine {

    private static final Random random = new Random();

    private final List<Consumer<GamePlayer>> actions = new ArrayList<>();

    @Inject
    public VendingMachine(GameEngine gameEngine, SidebarService sidebarService, MsgConfig<Msg> msgConfig) {
        actions.add(new EffectAction(PotionEffectType.SPEED));
        actions.add(new EffectAction(PotionEffectType.NIGHT_VISION));
        actions.add(new EffectAction(WolfRoleClass.UNLUCK));

        actions.add(gamePlayer -> gamePlayer.setGoldMultiplier(gamePlayer.getGoldMultiplier() * 2));

        actions.add(GamePlayer::incrementMurderProtectionPoints);

        actions.add(gamePlayer -> gamePlayer.getHandle().teleport(gameEngine.getArena().getRandomSpawnPoint()));

        //broken sidebar action
        actions.add(gamePlayer -> {
            Sidebar previousSidebar = sidebarService.removeSidebar(gamePlayer.getPlayer());
            Preconditions.checkState(previousSidebar != null, "GamePlayer sidebar is null");
            gamePlayer.setCachedSidebar(previousSidebar);

            Sidebar sidebar = sidebarService.createBukkitSidebar(gamePlayer.getPlayer());
            sidebar.setTitle(previousSidebar.getTitle());
            for (int i = 0; i < previousSidebar.getLinesCount(); i++) {
                int symbols = random.nextInt(12) + 1;
                sidebar.addLine(new SimpleText(RandomStringUtils.random(symbols)));
            }
        });

        actions.add(gamePlayer -> gamePlayer.setMuted(true));

        TickingService tickingService = gameEngine.getTickingService();
        GamePlayerResolver playerResolver = gameEngine.getPlayerResolver();
        actions.add(new ConsumerWithInitialAction(
                gamePlayer -> gamePlayer.setUnmovedDamageEnabled(true),
                () -> tickingService.register(new UnmovedDamageTicking(playerResolver))
        ));

        actions.add(gamePlayer -> gamePlayer.getSkinContainer().setDisplayRealSkin(true));

        PotionEffect slowing = new PotionEffect(PotionEffectType.SLOW, 6 * 20, 0);
        actions.add(new ConsumerWithInitialAction(
                gamePlayer -> {
                    ItemStack snowball = ItemBuilder.fromMaterial(Material.SNOWBALL)
                            .amount(5)
                            .withItemMeta()
                            .impose(msgConfig.get(Msg.misc__slowing_snowballs))
                            .blankEnchantment()
                            .build();
                    gamePlayer.getHandle().getInventory().setItem(3, snowball);
                },
                () -> QuickListener.create()
                        .event(QuickEvent.builder(ProjectileHitEvent.class)
                                .ignoreCancelled(true)
                                .filter(event -> event.getHitEntity() != null)
                                .filter(event -> event.getHitEntity().getType() == EntityType.PLAYER)
                                .filter(event -> event.getEntity().getType() == EntityType.SNOWBALL)
                                .handler(event -> ((Player) event.getHitEntity()).addPotionEffect(slowing))
                                .build()
                        )
                        .register(gameEngine.getPlugin())
        ));

        actions.add(new ConsumerWithInitialAction(
                gamePlayer -> gamePlayer.setChatCursed(true),
                () -> tickingService.register(new ChatSpamTicking(playerResolver))
        ));
    }

    public void applyRandomAction(GamePlayer gamePlayer) {
        actions.get(random.nextInt(actions.size())).accept(gamePlayer);
    }

    private static class EffectAction implements Consumer<GamePlayer> {

        private final PotionEffect potionEffect;

        public EffectAction(PotionEffectType type) {
            potionEffect = new PotionEffect(type, Integer.MAX_VALUE, 0);
        }

        public EffectAction(PotionEffectType type, int durationSeconds) {
            potionEffect = new PotionEffect(type, durationSeconds * 20, 0);
        }

        public EffectAction(PotionEffect potionEffect) {
            this.potionEffect = potionEffect;
        }

        @Override
        public void accept(GamePlayer gamePlayer) {
            gamePlayer.getHandle().addPotionEffect(potionEffect);
        }

    }

    private static class ConsumerWithInitialAction implements Consumer<GamePlayer> {

        private final Consumer<GamePlayer> consumer;
        private final Consumer<GamePlayer> initialAction;
        private boolean initialized = false;

        private ConsumerWithInitialAction(Consumer<GamePlayer> consumer, Consumer<GamePlayer> initialAction) {
            this.initialAction = initialAction;
            this.consumer = consumer;
        }

        public ConsumerWithInitialAction(Consumer<GamePlayer> consumer, Runnable initialAction) {
            this(consumer, (__) -> initialAction.run());
        }

        @Override
        public void accept(GamePlayer player) {
            if (!initialized) {
                initialAction.accept(player);
                initialized = true;
            }
            consumer.accept(player);
        }

    }

    private static class UnmovedDamageTicking implements Ticking {

        private final GamePlayerResolver playerResolver;

        private UnmovedDamageTicking(GamePlayerResolver playerResolver) {
            this.playerResolver = playerResolver;
        }

        @Override
        public boolean doTick() {
            playerResolver.getAll().forEach(gamePlayer -> {
                if (gamePlayer.isUnmovedDamageEnabled() && gamePlayer.incrementUnmovedSeconds() > 5) {
                    gamePlayer.getHandle().damage(2);
                }
            });

            return false;
        }

        @Override
        public int getPeriod() {
            return 20;
        }

    }

    private static class ChatSpamTicking implements Ticking {

        private final GamePlayerResolver playerResolver;

        private ChatSpamTicking(GamePlayerResolver playerResolver) {
            this.playerResolver = playerResolver;
        }

        @Override
        public boolean doTick() {
            playerResolver.getAll().forEach(gamePlayer -> {
                if (gamePlayer.isChatCursed()) {
                    gamePlayer.sendMessage("Твой чат проклят");
                }
            });
            return false;
        }

        @Override
        public int getPeriod() {
            return 2;
        }

    }

}
