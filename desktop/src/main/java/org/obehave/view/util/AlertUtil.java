package org.obehave.view.util;

import javafx.scene.control.Alert;
import org.obehave.util.I18n;

/**
 * @author Markus Möslinger
 */
public class AlertUtil {
    private AlertUtil() {
        throw new AssertionError(I18n.getString("exception.constructor.utility"));
    }

    public static void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
