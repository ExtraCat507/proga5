package org.xtracat.UI.addForm;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.xtracat.UI.util.Alerts;
import org.xtracat.client.util.Dispatcher;
import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;
import org.xtracat.datatypes.*;
import org.xtracat.server.MusicBandBuilder;
import org.xtracat.server.CoordinatesBuilder;
import org.xtracat.server.LabelBuilder;
import org.xtracat.usershit.User;

public class AddFormController {

    private final Dispatcher dispatcher = new Dispatcher();
    private User currentUser;


    @FXML private TextField nameField;
    @FXML private TextField xField;
    @FXML private TextField yField;
    @FXML private TextField participantsField;
    @FXML private TextField singlesField;
    @FXML private ChoiceBox<MusicGenre> genreChoiceBox;
    @FXML private CheckBox labelCheckBox;
    @FXML private TextField labelBandsField;
    @FXML private TextField bandSalesField;
    @FXML private Label labelLabel;
    @FXML private Label bandSalesLabel;
    @FXML private Button createButton;
    @FXML private Button exitButton;

    private Stage addStage;
    private Stage mainStage;

    @FXML
    public void initialize() {
        genreChoiceBox.getItems().setAll(MusicGenre.values());
        labelCheckBox.setOnAction(e -> onClickedAction());
        onClickedAction();

        createButton.setOnAction(e -> addAction());
    }

    private void onClickedAction() {
        boolean visible = labelCheckBox.isSelected();
        labelBandsField.setVisible(visible);
        bandSalesField.setVisible(visible);
        labelLabel.setVisible(visible);
        bandSalesLabel.setVisible(visible);
    }

    @FXML
    private void addAction() {
        try {
            Coordinates coords = new CoordinatesBuilder()
                    .setX(Long.parseLong(xField.getText()))
                    .setY(Integer.parseInt(yField.getText()))
                    .build();

            MusicLabel label = null;
            if (labelCheckBox.isSelected()) {
                label = new LabelBuilder()
                        .setBands(Long.parseLong(labelBandsField.getText()))
                        .setSales(Double.parseDouble(bandSalesField.getText()))
                        .build();
            }

            MusicBandBuilder musicBandBuilder = new MusicBandBuilder()
                    .setName(nameField.getText())
                    .setCoordinates(coords)
                    .setNumberOfParticipants(Long.parseLong(participantsField.getText()))
                    .setSinglesCount(Integer.parseInt(singlesField.getText()))
                    .setGenre(genreChoiceBox.getValue())
                    .setMusicLabel(label);

            try{
                musicBandBuilder.validate();
                MusicBand band = musicBandBuilder.build();
                if (band != null) {
                    System.out.println("MusicBand created successfully: " + band);
                    // Здесь можно отправить band на сервер или в коллекцию

                    Request request = new Request("add", band, currentUser);
                    Response response = dispatcher.send(request);
                    //Alerts.info(response.getMessage());

                    exitAction();
                }
            } catch (IllegalArgumentException e) {
                Alerts.error(e.getMessage());
            }

        } catch (Exception ex) {
            System.err.println("Error creating MusicBand: " + ex.getMessage());
        }

    }

    @FXML
    private void exitAction() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
        if (mainStage != null) {
            mainStage.show();
        }
    }



    public void setStages(Stage mainStage, Stage addStage) {
        this.mainStage = mainStage;
        this.addStage = addStage;
    }

    public void onClickedAction(ActionEvent actionEvent) {
    }

    public void setUser(User user) {
        this.currentUser = user;
    }
}
