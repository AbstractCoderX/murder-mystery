package ru.abstractcoder.murdermystery.core.data;

import ru.abstractcoder.murdermystery.core.game.role.classed.RoleClass;

public class ClassedRoleData {

    private int chancePoints;
    private RoleClass.Type selectedClassType;

    public ClassedRoleData(int chancePoints, RoleClass.Type selectedClassType) {
        this.chancePoints = chancePoints;
        this.selectedClassType = selectedClassType;
    }

    public ClassedRoleData() {
    }

    public int getChancePoints() {
        return chancePoints;
    }

    public void incrementChancePoints() {
        chancePoints++;
    }

    public void resetChancePoints() {
        chancePoints = 0;
    }

    public boolean isClassTypeSelected() {
        return selectedClassType != null;
    }

    public RoleClass.Type getSelectedClassType() {
        return selectedClassType;
    }

    public void setSelectedClassType(RoleClass.Type selectedClassType) {
        this.selectedClassType = selectedClassType;
    }

}
