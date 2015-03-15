package org.obehave.view.util;

import javafx.scene.control.TreeItem;

/**
 * @author Markus Möslinger
 */
public class TreeUtil {
    private TreeUtil() {

    }

    public static int getHierarchyLevel(TreeItem<?> treeItem) {
        int level = 0;

        while (treeItem.getParent() != null) {
            treeItem = treeItem.getParent();
            level++;
        }

        return level;
    }
}
