package org.xtracat.UI.editForm;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.xtracat.UI.util.Localization;
import org.xtracat.datatypes.MusicBand;
import org.xtracat.usershit.User;

import java.io.IOException;
import java.net.URL;

public class EditFormWindow {
    private final Stage editStage;
    private final Stage mainStage;
    private Runnable onCloseAction;
    private EditFormController controller;

    /**
     * @param mainStage    основное окно, к которому вернёмся после закрытия
     * @param user         текущий пользователь
     * @param bandToEdit   объект MusicBand, который редактируем
     * @param onCloseAction Runnable, который будет выполнен при закрытии (например, перезагрузить таблицу)
     */
    public EditFormWindow(Stage mainStage, User user, MusicBand bandToEdit, Runnable onCloseAction) {
        this.mainStage = mainStage;
        this.editStage = new Stage();

        try {
            URL fxmlLocation = getClass().getResource("/editForm.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            EditFormController controller = loader.getController();
            controller.setUser(user);
            controller.setStages(mainStage, editStage);
            controller.setMusicBand(bandToEdit);
            this.controller = controller;
            this.onCloseAction = onCloseAction;

            editStage.setTitle(Localization.get("edit_window"));
            editStage.setScene(new Scene(root));
            editStage.initOwner(mainStage);
            editStage.initModality(Modality.WINDOW_MODAL);

            editStage.setOnHidden(event -> {
                if (onCloseAction != null) {
                    onCloseAction.run();
                }
            });

            editStage.setOnCloseRequest(event -> {
                if (onCloseAction != null) {
                    onCloseAction.run();
                }
            });

        } catch (IOException e) {
            System.err.println("Failed to load EditForm FXML");
            e.printStackTrace();
        }
    }


    public void show() {
        mainStage.hide();
        editStage.show();
    }

    public void showAndWait() {
        mainStage.hide();
        editStage.showAndWait();
        if (onCloseAction != null) {
            onCloseAction.run();
        }
    }

    public void setOnCloseAction(Runnable onCloseAction) {
        this.onCloseAction = onCloseAction;
    }

    public MusicBand getUpdatedBand() {
        return controller.getUpdatedBand();
    }
}
