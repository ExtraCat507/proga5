package org.xtracat.UI.registerForm;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.xtracat.UI.addForm.AddFormController;
import org.xtracat.UI.util.Localization;
import org.xtracat.usershit.User;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class RegFormWindow {
    private final Stage stage;

    public RegFormWindow(Stage stage) {
        this.stage = Objects.requireNonNull(stage, "Stage cannot be null");
        try {
            stage.setResizable(false);
            stage.setTitle(Localization.get("auth_title"));


            URL fxmlLocation = getClass().getResource("/regForm.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            AddFormController controller = loader.getController();
            Parent root = loader.load();
            Scene scene = new Scene(root);
            this.stage.setScene(scene);

        } catch (IOException e) {
            System.err.println("FATAL: Failed to load FXML for registration form.");
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
