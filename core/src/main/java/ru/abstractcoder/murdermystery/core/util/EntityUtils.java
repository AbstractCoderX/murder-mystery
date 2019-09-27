package ru.abstractcoder.murdermystery.core.util;

import org.jetbrains.annotations.Contract;
import ru.abstractcoder.benioapi.nms.PackageType;
import ru.abstractcoder.benioapi.util.reflect.TrustedLookup;

import java.lang.invoke.VarHandle;

public final class EntityUtils {

    private static final VarHandle entityCountVarHandle;

    static {
        Class<?> entityClass = PackageType.NMS.getClass("Entity");
        try {
            entityCountVarHandle = TrustedLookup.get()
                    .findStaticVarHandle(entityClass, "entityCount", int.class);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new AssertionError(e);
        }
    }

    public static int getAndIncrementEntityCount() {
        return (int) entityCountVarHandle.getAndAdd(1);
    }

    // Suppress default constructor to ensure non-instantiability.
    private EntityUtils() {
        throw new AssertionError("You should not be attempting to instantiate this class.");
    }

}