package org.obehave.view.controller.components.coding;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.control.ScrollPane;

/**
 * This class provides bindings for viewportBounds of a {@link javafx.scene.control.ScrollPane}
 */
public class BoundsProperties {
    private BoundsProperties() {
        throw new AssertionError("This class shouldn't be instantiated");
    }

    public static class ScrollPaneViewPortHeightBinding extends DoubleBinding {

        private final ScrollPane root;

        public ScrollPaneViewPortHeightBinding(ScrollPane root) {
            this.root = root;
            super.bind(root.viewportBoundsProperty());
        }

        @Override
        protected double computeValue() {
            return root.getViewportBounds().getHeight();
        }
    }

    public static class ScrollPaneViewPortWidthBinding extends DoubleBinding {
        private final ScrollPane root;

        public ScrollPaneViewPortWidthBinding(ScrollPane root) {
            this.root = root;
            super.bind(root.viewportBoundsProperty());
            get();
        }

        @Override
        protected double computeValue() {
            return root.getViewportBounds().getWidth();
        }
    }
}
