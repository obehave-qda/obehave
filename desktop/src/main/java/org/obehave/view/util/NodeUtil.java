package org.obehave.view.util;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ObservableNumberValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class NodeUtil {
    public static double snapXY(double value) {
        return ((int) value) + 0.5;
    }

    public static DoubleBinding snapXY(final ObservableNumberValue position) {
        return new DoubleBinding() {
            {
                super.bind(position);
            }

            @Override
            public void dispose() {
                super.unbind(position);
            }

            @Override
            protected double computeValue() {
                return NodeUtil.snapXY(position.doubleValue());
            }

            @Override
            public ObservableList<?> getDependencies() {
                return FXCollections.singletonObservableList(position);
            }
        };
    }
}
