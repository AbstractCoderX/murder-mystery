package ru.abstractcoder.murdermystery.core.game.corpse.bed;

import com.comphenix.protocol.wrappers.WrappedBlockData;
import org.bukkit.Material;

public enum BedFacade {

    LOWER_45(10, 0.5, 0.05),
    LOWER_135(13, 0.05, 0.5),
    LOWER_225(8, 0.95, 0.5),
    LOWER_315(11, 0.95, 0.5),
    OTHER(10, 0.5, 0.05)

    ;

    public static BedFacade getByYaw(float yaw) {
        if (yaw < 45) {
            return LOWER_45;
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
        return OTHER;
    }

    private final WrappedBlockData bedData;
    private final double adjustX;
    private final double adjustZ;

    BedFacade(int bedData, double adjustX, double adjustZ) {
        //noinspection deprecation
        this.bedData = WrappedBlockData.createData(Material.BED_BLOCK, bedData);
        this.adjustX = adjustX;
        this.adjustZ = adjustZ;
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