package org.xtracat.UI.addForm;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.xtracat.UI.addForm.AddFormController;
import org.xtracat.UI.util.Localization;
import org.xtracat.usershit.User;

import java.io.IOException;
import java.net.URL;

public class AddFormWindow {
    private final Stage addStage;
    private final Stage mainStage;

    public AddFormWindow(Stage mainStage, User user,Runnable onCloseAction) {
        this.mainStage = mainStage;
        this.addStage = new Stage();
        try {
            URL fxmlLocation = getClass().getResource("/addForm.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();
            AddFormController controller = loader.getController();
            controller.setUser(user);
            controller.setStages(mainStage, addStage);


            addStage.setTitle(Localization.get("create_window"));
            addStage.setScene(new Scene(root));
            addStage.initOwner(mainStage);
            addStage.initModality(Modality.WINDOW_MODAL);

            addStage.setOnHidden(event -> {
                if (onCloseAction != null) {
                    onCloseAction.run();
                }
            });

        } catch (IOException e) {
            System.err.println("Failed to load AddForm FXML");
            e.printStackTrace();
        }
    }

    public void show() {
        mainStage.hide();
        addStage.show();
    }
}
