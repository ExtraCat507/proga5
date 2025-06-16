package org.xtracat.UI.mainForm;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.xtracat.UI.util.Localization;
import org.xtracat.usershit.User;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class MainFormWindow {
    private final Stage stage;

    public MainFormWindow(User user) {
        Objects.requireNonNull(user, "User cannot be null for the main window.");
        this.stage = new Stage();
        try {
            URL fxmlLocation = getClass().getResource("/mainForm.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlLocation);

            Parent root = loader.load();
            MainFormWindowController controller = loader.getController();
            controller.initUser(user);

            stage.setTitle(Localization.get("main_title"));
            stage.setScene(new Scene(root));
            stage.setMinWidth(1200);
            stage.setMinHeight(700);
            controller.updateUI();

        } catch (IOException e) {
            System.err.println("FATAL: Failed to load FXML for the main form.");
            e.printStackTrace();

        }
    }

    /**
     * Shows the configured stage.
     */
    public void show() {
        stage.show();
    }
}