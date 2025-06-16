// File: src/main/java/org/xtracat/UI/help/HelpWindowController.java
package org.xtracat.UI.helpForm;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.xtracat.UI.util.Localization;

public class HelpWindowController {
    @FXML
    private TextArea helpField;
    @FXML
    private Label helpLabel;
    @FXML
    private Button exitButton;

    private Stage mainStage;
    private Stage helpStage;

    public void setStages(Stage mainStage, Stage helpStage) {
        this.mainStage = mainStage;
        this.helpStage = helpStage;
    }

    @FXML
    private void initialize() {
        helpLabel.setText(Localization.get("help_title"));
        helpField.setText(Localization.get("help_content"));
        exitButton.setText(Localization.get("exit"));
    }

    @FXML
    private void exitAction() {
        helpStage.close();
        mainStage.show();
    }

    private void loadHelp(){

    }


}
