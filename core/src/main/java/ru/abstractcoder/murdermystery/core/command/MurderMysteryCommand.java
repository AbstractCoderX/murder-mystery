package ru.abstractcoder.murdermystery.core.command;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.abstractcoder.benioapi.command.impl.ParentCommand;
import ru.abstractcoder.murdermystery.core.game.corpse.CorpseService;

public class MurderMysteryCommand extends ParentCommand {

    public MurderMysteryCommand(CorpseService corpseService) {
        super("murdermystery");

        executingStrategy()
                .aliases("murder", "mm")
                .withSubcommand(new PlayerSubcommand("throw") {
                    @Override
                    protected void execute(Player player, String[] args, String label) {
                        Location location = player.getLocation().add(0, 1, 0);
                        ArmorStand armorStand = location.getWorld().spawn(location, ArmorStand.class);
                        armorStand.setCanMove(true);
                        armorStand.setCanTick(true);
                        armorStand.setSmall(true);
                        armorStand.setVisible(false);
                        armorStand.setSmall(true);
                        armorStand.setInvulnerable(true);
                        armorStand.setBasePlate(false);
                        armorStand.setArms(false);
                        armorStand.setHelmet(new ItemStack(Material.IRON_SWORD));
                        armorStand.setHeadPose(armorStand.getHeadPose().setX(Math.toRadians(location.getPitch())));
                        armorStand.setVelocity(location.getDirection().normalize().multiply(2.5));


                    }
                })
//                .withSubcommand(new PlayerSubcommand("spawncorpse") {
//                    @Override
//                    protected void execute(Player player, String[] args, String label) {
//                        corpseService.spawnCorpse(player.getUniqueId(), player.getLocation());
//                    }
//                })

        ;
    }

}