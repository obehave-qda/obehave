package org.obehave.view.util;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import org.controlsfx.dialog.Dialogs;
import org.obehave.util.I18n;
import org.obehave.util.Properties;

import java.util.Optional;

/**
 * @author Markus Möslinger
 */
public class AlertUtil {
    private AlertUtil() {
        throw new AssertionError(I18n.getString("exception.constructor.utility"));
    }

    /**
     * Shows an blocking error alert
     * @param title The title of the alert window
     * @param content The message to display
     */
    public static void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * If property {@code ui.error.exceptions.show} is set to true, this method will show an exception alert with a
     * full stacktrace. Otherwise, this will just delegate to {@link AlertUtil#showError(String, String)}
     * @param title The title of the alert window
     * @param content The message to display
     * @param t The throwable to show in an expandable text area, if {@code ui.error.exceptions.show} is set to true.
     *          Otherwise, it will be ignored
     */
    public static void showError(String title, String content, Throwable t) {
        if (Properties.isUiErrorExceptionsShow()) {
            Dialogs.create().title(title).message(content).showException(t);
        } else {
            showError(title, content);
        }
    }

    public static Optional<String> askForString(String title, String description) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setContentText(description);

        return dialog.showAndWait();
    }
}
