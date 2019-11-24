package ru.abstractcoder.murdermystery.core.game.corpse.bed;

import com.comphenix.protocol.wrappers.WrappedBlockData;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;

public enum BedFacade {

    BEETWEEN_315_AND_45(BlockFace.SOUTH, 0.5, 0.05),
    LOWER_135(BlockFace.WEST, 0.05, 0.5),
    LOWER_225(BlockFace.NORTH, 0.95, 0.5),
    LOWER_315(BlockFace.EAST, 0.95, 0.5),

    ;

    public static BedFacade getByYaw(float yaw) {
        if (yaw >= 315 || yaw < 45) {
            return BEETWEEN_315_AND_45;
        }
        if (yaw < 135) {
            return LOWER_135;
        }
        if (yaw < 225) {
            return LOWER_225;
        }
        if (yaw < 315) {
            return LOWER_315;
        }

        throw new IllegalArgumentException("Incorrect yaw: " + yaw);
    }

    private final WrappedBlockData bedData;
    private final double adjustX;
    private final double adjustZ;

    BedFacade(BlockFace facing, double adjustX, double adjustZ) {
        this.adjustX = adjustX;
        this.adjustZ = adjustZ;

        BlockData blockData = Material.RED_BED.createBlockData(data -> ((Directional) data).setFacing(facing));
        this.bedData = WrappedBlockData.createData(blockData);
    }

    public WrappedBlockData getBedData() {
        return bedData;
    }

    public double getAdjustX() {
        return adjustX;
    }

    public double getAdjustZ() {
        return adjustZ;
    }

}