package ru.abstractcoder.murdermystery.core.util;

import com.fasterxml.jackson.annotation.JsonCreator;

public interface Mixins {

    //not used
    abstract class WrappedSkinMixin {

        @JsonCreator
        public WrappedSkinMixin(String name, String value, String signature) {
        }

    }

}