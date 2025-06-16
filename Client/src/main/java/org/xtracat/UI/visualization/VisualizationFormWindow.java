package org.xtracat.UI.visualization;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.xtracat.datatypes.MusicBand;
import org.xtracat.usershit.User;

import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import javafx.collections.ObservableList;

import javax.swing.plaf.ViewportUI;

public class VisualizationFormWindow {
    private final Stage visStage;
    private final Stage mainStage;
    private final VisualizationFormController controller;

    public VisualizationFormWindow(Stage mainStage, User user,
                               ObservableList<MusicBand> bandList,
                               Runnable onCloseAction) {
        this.mainStage = mainStage;
        this.visStage = new Stage();
        VisualizationFormController tmpController = null;
        try {
            URL fxmlLocation = getClass().getResource("/visualizationForm.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();
            tmpController = loader.getController();

            tmpController.setUser(user);
            tmpController.setBandList(bandList);
            tmpController.setStages(mainStage, visStage);

            visStage.setTitle("Визуализация");
            visStage.setScene(new Scene(root));
            visStage.initOwner(mainStage);
            visStage.initModality(Modality.WINDOW_MODAL);

            visStage.setOnHidden(evt -> {
                if (onCloseAction != null) {
                    onCloseAction.run();
                }
            });
        } catch (IOException e) {
            System.err.println("Failed to load visualization FXML");
            e.printStackTrace();
        }
        this.controller = tmpController;
    }

    public void show() {
        mainStage.hide();
        visStage.show();
    }

    public void showAndWait() {
        mainStage.hide();
        visStage.showAndWait();
    }
}
