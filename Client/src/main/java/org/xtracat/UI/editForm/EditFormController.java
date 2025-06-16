package org.xtracat.UI.editForm;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.xtracat.UI.util.Alerts;
import org.xtracat.UI.util.Localization;
import org.xtracat.client.util.Dispatcher;
import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;
import org.xtracat.datatypes.Coordinates;
import org.xtracat.datatypes.MusicBand;
import org.xtracat.datatypes.MusicGenre;
import org.xtracat.datatypes.MusicLabel;
import org.xtracat.server.CoordinatesBuilder;
import org.xtracat.server.LabelBuilder;
import org.xtracat.server.MusicBandBuilder;
import org.xtracat.usershit.User;

public class EditFormController {

    @FXML private Label addWindowLabel;

    @FXML private Label nameLabel;
    @FXML private Label singlesLabel;
    @FXML private Label participantsLabel;
    @FXML private Label genreLabel;
    @FXML private Label xCoordLabel;
    @FXML private Label yCoordLabel;
    @FXML private Label labelLabel;
    @FXML private Label bandSalesLabel;

    @FXML private TextField nameField;
    @FXML private TextField singlesField;
    @FXML private TextField participantsField;
    @FXML private TextField xCoordField;
    @FXML private TextField yCoordField;
    @FXML private ChoiceBox<MusicGenre> genreMenu;
    @FXML private CheckBox labelCheckBox;
    @FXML private TextField labelBandsField;
    @FXML private TextField bandSalesField;

    @FXML private Button applyButton;

    private Stage mainStage;
    private Stage editStage;
    private User currentUser;
    private MusicBand editingBand;
    private MusicGenre selectedGenre;

    private final Dispatcher dispatcher = new Dispatcher();
    private MusicBand updatedBand;

    @FXML
    public void initialize() {
        genreMenu.getItems().setAll(MusicGenre.values());
        labelCheckBox.setOnAction(this::onCheckedAction);
        toggleLabelFields();
        applyButton.setOnAction(this::onClickedAction);
    }

    /**
     * Вызывается из EditFormWindow сразу после загрузки FXML.
     */
    public void setStages(Stage mainStage, Stage editStage) {
        this.mainStage = mainStage;
        this.editStage = editStage;
    }

    /**
     * Устанавливает текущего пользователя.
     */
    public void setUser(User user) {
        this.currentUser = user;
    }


    public void setMusicBand(MusicBand band) {
        this.editingBand = band;
        if (band == null) return;

        nameField.setText(band.getName());
        singlesField.setText(String.valueOf(band.getSinglesCount()));
        participantsField.setText(String.valueOf(band.getNumberOfParticipants()));

        Coordinates coords = band.getCoordinates();
        if (coords != null) {
            xCoordField.setText(String.valueOf(coords.getX()));
            yCoordField.setText(String.valueOf(coords.getY()));
        }

        selectedGenre = band.getGenre();
        genreMenu.setValue(selectedGenre);


        MusicLabel lbl = band.getLabel();
        if (lbl != null) {
            labelCheckBox.setSelected(true);
            toggleLabelFields();
            labelBandsField.setText(lbl.getBands().toString());
            if (lbl.getSales() != null) {
                bandSalesField.setText(String.valueOf(lbl.getSales()));
            }
        } else {
            labelCheckBox.setSelected(false);
            toggleLabelFields();
        }
    }

    @FXML
    private void onCheckedAction(ActionEvent event) {
        toggleLabelFields();
    }

    private void toggleLabelFields() {
        boolean visible = labelCheckBox.isSelected();
        labelBandsField.setVisible(visible);
        bandSalesField.setVisible(visible);
        labelLabel.setVisible(visible);
        bandSalesLabel.setVisible(visible);
    }

    @FXML
    private void onClickedAction(ActionEvent event) {
        if (editingBand == null) {
            Alerts.error("No item to edit.");
            return;
        }
        try {
            // Сбор данных из полей
            String name = nameField.getText();

            Long x = null;
            Integer y = null;
            if (!xCoordField.getText().isEmpty()) {
                x = Long.parseLong(xCoordField.getText());
            }
            if (!yCoordField.getText().isEmpty()) {
                y = Integer.parseInt(yCoordField.getText());
            }
            Coordinates coords = new CoordinatesBuilder()
                    .setX(x)
                    .setY(y)
                    .build();

            Long participants = null;
            if (!participantsField.getText().isEmpty()) {
                participants = Long.parseLong(participantsField.getText());
            }
            Integer singles = null;
            if (!singlesField.getText().isEmpty()) {
                singles = Integer.parseInt(singlesField.getText());
            }

            MusicLabel label = null;
            if (labelCheckBox.isSelected()) {
                Long bands = Long.parseLong(labelBandsField.getText());
                Double sales = null;
                if (!bandSalesField.getText().isEmpty()) {
                    sales = Double.parseDouble(bandSalesField.getText());
                }
                label = new LabelBuilder()
                        .setBands(bands)
                        .setSales(sales)
                        .build();
            }

            MusicGenre genre = genreMenu.getValue();

            // Используем MusicBandBuilder. Для сохранения оригинального creationDate используем перегрузку build(...)
            MusicBandBuilder builder = new MusicBandBuilder();

            MusicBand newBand = builder.build(
                    name,
                    coords,
                    editingBand.getCreationDate(),
                    participants,
                    singles,
                    genre,
                    label
            );
            // Если builder.build(...) возвращает null, значит ошибка
            if (newBand == null) {
                Alerts.error("Failed to build updated MusicBand.");
                return;
            }
            // Сохраняем ID из оригинала, если в MusicBand есть setter или через конструктор:
            try {
                // Предполагается, что есть метод setId или конструктор сохраняет id автоматически на сервере
                newBand.setId(editingBand.getId());
            } catch (Exception ignored) {}

            long id = editingBand.getId();

            Object[] payload = new Object[]{id, newBand};

            // Отправляем запрос на обновление
            Request request = new Request("update", payload,currentUser);
            Response response = dispatcher.send(request);

            this.updatedBand = newBand;

            // Закрываем окно редактирования
            exitAction();

        } catch (NumberFormatException nfe) {
            Alerts.error("Некорректный формат числа: " + nfe.getMessage());
        } catch (Exception ex) {
            Alerts.error("Ошибка при сохранении: " + ex.getMessage());
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

    public MusicBand getUpdatedBand() {
        return this.updatedBand;
    }
}
