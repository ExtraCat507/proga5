// File: src/main/java/org/xtracat/UI/help/HelpWindow.java
package org.xtracat.UI.helpForm;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.xtracat.UI.util.Localization;

import java.io.IOException;
import java.net.URL;

public class HelpWindow {
    private final Stage mainStage;
    private final Stage helpStage;

    public HelpWindow(Stage mainStage) {
        this.mainStage = mainStage;
        helpStage = new Stage();
        try {
            URL fxmlLocation = getClass().getResource("/helpForm.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();
            HelpWindowController controller = loader.getController();
            controller.setStages(mainStage, helpStage);

            helpStage.setTitle(Localization.get("help_title"));
            helpStage.setScene(new Scene(root));
            helpStage.initOwner(mainStage);
            helpStage.initModality(Modality.WINDOW_MODAL);
        } catch (IOException e) {
            System.err.println("Failed to load HelpWindow FXML");
            e.printStackTrace();
        }
    }

    public void show() {
        mainStage.hide();
        helpStage.show();
    }
}
