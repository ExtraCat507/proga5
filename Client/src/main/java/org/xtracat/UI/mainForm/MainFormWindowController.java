package org.xtracat.UI.mainForm;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.xtracat.UI.editForm.EditFormWindow;
import org.xtracat.UI.helpForm.HelpWindow;
import org.xtracat.UI.addForm.AddFormWindow;
import org.xtracat.UI.util.Alerts;
import org.xtracat.UI.util.Localization;
import org.xtracat.UI.util.ScriptExecutor;
import org.xtracat.UI.visualization.VisualizationFormWindow;
import org.xtracat.client.util.Dispatcher;
import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;
import org.xtracat.datatypes.Coordinates;
import org.xtracat.datatypes.MusicBand;
import org.xtracat.datatypes.MusicLabel;
import org.xtracat.usershit.User;

import java.io.FileNotFoundException;
import java.time.ZonedDateTime;
import java.util.*;

public class MainFormWindowController {


    private User currentUser;
    private final Dispatcher dispatcher = new Dispatcher();
    private final ObservableList<MusicBand> musicBandList = FXCollections.observableArrayList();

    //<editor-fold desc="FXML Fields">
    @FXML private TableView<MusicBand> table;
    @FXML private TableColumn<MusicBand, Long> idColumn;
    @FXML private TableColumn<MusicBand, String> nameColumn;
    @FXML private TableColumn<MusicBand, Long> xColumn;
    @FXML private TableColumn<MusicBand, Long> yColumn;
    @FXML private TableColumn<MusicBand, ZonedDateTime> creationDateColumn;
    @FXML private TableColumn<MusicBand, Integer> singlesColumn;
    @FXML private TableColumn<MusicBand, Long> participantsColumn;
    @FXML private TableColumn<MusicBand, String> genreColumn;
    @FXML private TableColumn<MusicBand, Long> labelBandsColumn;
    @FXML private TableColumn<MusicBand, Double> labelSalesColumn;
    @FXML private TableColumn<MusicBand, String> ownerColumn;

    @FXML public ComboBox<String> langBox;
    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Button helpButton;
    @FXML private Button clearButton;
    @FXML private Button executeScriptButton;
    @FXML private Button visualizationButton;
    @FXML private Button printFieldAscendingNumberOfParticipants; // ID from FXML
    @FXML private Button countGreaterThanNumberOfParticipantsButton;
    @FXML private Button exitButton;

    @FXML private Label languageLabel;
    @FXML private Label userLabel;
    @FXML private Label dateLabel;
    @FXML private Label userField;
    @FXML private Label dateField;

    //</editor-fold>

    /**
     * Called by the FXML loader after the components are loaded.
     */
    @FXML
    public void initialize() {
        initializeTableColumns();
        initializeLanguageMenu();
        table.setItems(musicBandList);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> loadCollection());
            }
        }, 5000);
    }


    public void initUser(User user) {
        this.currentUser = user;
        this.userField.setText(user.login());
        loadCollection();
        loadCreationDate();
    }

    private void loadCreationDate() {
        Response response =  dispatcher.send(new Request("info", null,currentUser));
        dateField.setText(Localization.getDate((ZonedDateTime) response.getData()));
    }

    private void initializeTableColumns() {
        // Assuming MusicBand has corresponding getters for these properties
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        // Assuming Coordinates object is accessible via getCoordinates()
        xColumn.setCellValueFactory(cellData -> {
            Coordinates coordinates = cellData.getValue().getCoordinates();
            if (coordinates != null) {
                return new SimpleLongProperty(coordinates.getX()).asObject();
            } else {
                return new SimpleLongProperty(0L).asObject(); // или null, если хочешь пустую ячейку
            }
        });
        yColumn.setCellValueFactory(cellData -> {
            Coordinates coordinates = cellData.getValue().getCoordinates();
            if (coordinates != null) {
                return new SimpleLongProperty(coordinates.getY()).asObject();
            } else {
                return new SimpleLongProperty(0L).asObject(); // или null, если хочешь пустую ячейку
            }
        });
        creationDateColumn.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        singlesColumn.setCellValueFactory(new PropertyValueFactory<>("singlesCount"));
        participantsColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfParticipants"));
        // Assuming Genre is an enum or has a toString() method
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        // Assuming Label object is accessible
        labelBandsColumn.setCellValueFactory(cellData -> {
            MusicLabel label = cellData.getValue().getLabel();
            if (label != null) {
                return new SimpleLongProperty(label.getBands()).asObject();
            } else {
                return null;
            }
        });
        labelSalesColumn.setCellValueFactory(cellData -> {
            MusicLabel label = cellData.getValue().getLabel();
            if (label != null) {
                return new SimpleDoubleProperty(label.getSales()).asObject();
            } else {
                return null;
            }
        });
        ownerColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
    }

    private void initializeLanguageMenu() {
        langBox.getItems().addAll("Русский", "Portugal", "ελληνικά", "Español (el Salvador)");
        langBox.setValue("Русский"); // Set default value in the box
    }

    private void addMenuItemToMenu(String text, Locale locale, MenuButton menu) {
        MenuItem item = new MenuItem(text);
        item.setOnAction(e -> {
            Localization.setLocale(locale);
            updateUI();
        });
        menu.getItems().add(item);
    }

    protected void updateUI() {
        Stage stage = (Stage) table.getScene().getWindow();
        if (stage != null) stage.setTitle(Localization.get("main_title"));

        addButton.setText(Localization.get("add"));
        editButton.setText(Localization.get("edit"));
        deleteButton.setText(Localization.get("delete"));
        helpButton.setText(Localization.get("help"));
        clearButton.setText(Localization.get("clear"));
        executeScriptButton.setText(Localization.get("execute_script"));
        visualizationButton.setText(Localization.get("visualization"));
        printFieldAscendingNumberOfParticipants.setText(Localization.get("print_ascending_number_of_participants"));
        countGreaterThanNumberOfParticipantsButton.setText(Localization.get("count_greater_than_number_of_participants"));
        exitButton.setText(Localization.get("exit"));
        languageLabel.setText(Localization.get("language"));
        userLabel.setText(Localization.get("user"));
        dateLabel.setText(Localization.get("date"));
        //dateField.setText(ZonedDateTime.now().toLocalDate().toString());

        loadCreationDate();

    }

    public void loadCollection() {
        Request request = new Request("show", null, currentUser);
        Response response = dispatcher.send(request);
        if (response != null && response.getData() instanceof List) {
            List<MusicBand> bands = (List<MusicBand>) response.getData();
            musicBandList.setAll(bands);
        } else if (response != null) {
            Alerts.error("Failed to load collection: " + response.getMessage());
        }
        else{
            Alerts.error("watafak on load");
        }
    }

    //<editor-fold desc="Action Handlers">
    @FXML
    private void addAction() {
        AddFormWindow addFormWindow = new AddFormWindow((Stage) table.getScene().getWindow(),currentUser, this::loadCollection);
        addFormWindow.show();

    }

    private MusicBand getSelectedMusicBand() {
        return table.getSelectionModel().getSelectedItem();
    }

    @FXML
    private void editAction() {
        MusicBand selected = getSelectedMusicBand();
        if (selected != null) {
            if(!currentUser.login().equals(selected.getAuthor())){return;}
            EditFormWindow editFormWindow = new EditFormWindow((Stage) table.getScene().getWindow(),currentUser, selected, this::loadCollection);
            editFormWindow.showAndWait();
        } else {
            // показать предупреждение
            Alerts.info("Выберите элемент для редактирования.");
        }
    }

    @FXML
    private void deleteAction() {
        MusicBand selected = getSelectedMusicBand();
        if (selected != null) {

            if(!currentUser.login().equals(selected.getAuthor())){return;}

            Request request = new Request("remove_by_id", selected.getId(),currentUser);
            Response response = dispatcher.send(request);
            Alerts.info(response.getMessage());
            loadCollection();
        } else {
            Alerts.info("Сначала выделите строку для удаления.");
        }
    }

    @FXML
    private void clearAction() {
        ObservableList<MusicBand> items = table.getItems();
        List<MusicBand> toDelete = new ArrayList<>(items);
        for (MusicBand band : toDelete) {
            Request request = new Request("remove_by_id", band.getId(), currentUser);
            Response response = dispatcher.send(request);
        }
        loadCollection();
    }
    @FXML private void executeScriptAction() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("ExecuteScript");
        dialog.setHeaderText("Введите имя файла");
        dialog.setContentText("");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(input -> {
            try {
                ScriptExecutor.executeScript(input,currentUser);
            } catch (Exception e) {
                if(e.getMessage().equals("Выполнено")){
                    Alerts.info(e.getMessage());
                    return;
                }
                Alerts.error(e.getMessage());
            }
        });
    }

    @FXML
    private void visualizationAction() {
        Request request = new Request("show", null, currentUser);
        Response response = dispatcher.send(request);
        if (response != null && response.getData() instanceof List) {
            List<MusicBand> bands = (List<MusicBand>) response.getData();
            musicBandList.setAll(bands);
        } else if (response != null) {
            Alerts.error("Failed to update collection: " + response.getMessage());
        }
        VisualizationFormWindow visualizationFormWindow = new VisualizationFormWindow((Stage) table.getScene().getWindow(),currentUser,musicBandList,this::loadCollection);
        visualizationFormWindow.show();
    }

    @FXML
    private void printAscendingListAction() {
        Request request = new Request("printFieldAscendingNumberOfParticipants",null,currentUser);
        Response response = dispatcher.send(request);
        if (response == null) {
            Alerts.error("Нет ответа от сервера.");
            return;
        }

        Object data = response.getData( );
        StringBuilder result = new StringBuilder();
        if (data instanceof long[]) {
            long[] numbers = (long[]) data;
            if (numbers.length == 0) {
                Alerts.info("Коллекция пуста.");
            } else {
                result.append(response.getMessage()).append("\n");
                for (Long num : numbers) {
                    result.append(num).append(" ");
                }
                result.append("\n");
            }
        } else {
            Alerts.error(response.getMessage());
        }

        Alerts.info(result.toString());

    }

    @FXML
    private void countGreaterAction() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Подсчитать по участникам");
        dialog.setHeaderText("Введите число участников");
        dialog.setContentText("Число участников:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(input -> {
            int value;
            try {
                value = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                Alerts.error("Введите целое число.");
                return;
            }
            Request req = new Request("countGreaterThanNumberOfParticipants", value, currentUser);
            Response response = dispatcher.send(req);
            if (response == null) {
                Alerts.error("Нет ответа от сервера.");
                return;
            }
            Alerts.info(response.getMessage());
        });
    }


    @FXML private void filterAction() { /* TODO: Implement filtering logic */ Alerts.info("Filter action triggered."); }

    @FXML
    private void helpAction() {
        HelpWindow helpWindow = new HelpWindow((Stage) table.getScene().getWindow());
        helpWindow.show();
    }

    @FXML private void localeChanged() {
        String selected = langBox.getValue();
        if (selected == null) return;

        switch (selected) {
            case "Русский":
                Localization.setLocale(new Locale("ru"));
                break;
            case "Portugal":
                Localization.setLocale(new Locale("pt"));
                break;
            case "ελληνικά":
                Localization.setLocale(new Locale("el"));
                break;
            case "Español (el Salvador)":
                Localization.setLocale(new Locale("es"));
                break;
        }
        updateUI();
    }

    @FXML
    private void exitAction() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}