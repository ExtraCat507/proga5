package org.xtracat.UI.visualization;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.xtracat.UI.editForm.EditFormWindow;
import org.xtracat.UI.util.Alerts;
import org.xtracat.UI.util.Localization;
import org.xtracat.datatypes.MusicBand;
import org.xtracat.usershit.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class VisualizationFormController {

    @FXML
    private Pane visualPane;

    @FXML
    private Button exitButton;

    private Stage mainStage;
    private Stage visStage;
    private User currentUser;
    private ObservableList<MusicBand> bandList;


    private final Map<MusicBand, Rectangle> shapeMap = new HashMap<>();

    private final Map<String, Color> userColorMap = new HashMap<>();

    @FXML
    public void initialize() {

        exitButton.setOnAction(evt -> {
            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.close();
            if (mainStage != null) {
                mainStage.show();
            }
        });


        visualPane.widthProperty().addListener((obs, oldW, newW) -> redrawAll());
        visualPane.heightProperty().addListener((obs, oldH, newH) -> redrawAll());

    }


    public void setStages(Stage mainStage, Stage visStage) {
        this.mainStage = mainStage;
        this.visStage = visStage;
    }

    public void setUser(User user) {
        this.currentUser = user;
    }


    public void setBandList(ObservableList<MusicBand> bandList) {
        this.bandList = bandList;

        bandList.addListener((ListChangeListener<MusicBand>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (MusicBand b : change.getAddedSubList()) {
                        Platform.runLater(() -> addBandShape(b));
                    }
                }
                if (change.wasRemoved()) {
                    for (MusicBand b : change.getRemoved()) {
                        Platform.runLater(() -> removeBandShape(b));
                    }
                }
                if (change.wasUpdated()) {
                    int idx = change.getFrom();
                    if (idx >= 0 && idx < bandList.size()) {
                        MusicBand updated = bandList.get(idx);
                        Platform.runLater(() -> updateBandShape(updated));
                    }
                }
            }
        });

        Platform.runLater(() -> {
            shapeMap.clear();
            visualPane.getChildren().clear();
            redrawAll();
        });
    }

    private void redrawAll() {
        visualPane.getChildren().clear();
        shapeMap.clear();
        drawAxes();
        if (bandList != null) {
            for (MusicBand b : bandList) {
                addBandShape(b);
            }
        }
    }

    private void drawAxes() {
        double w = visualPane.getWidth();
        double h = visualPane.getHeight();
        double centerX = w / 2.0;
        double centerY = h / 2.0;

        Line xAxis = new Line(0, centerY, w, centerY);
        xAxis.setStroke(Color.GRAY);
        Line yAxis = new Line(centerX, 0, centerX, h);
        yAxis.setStroke(Color.GRAY);

        visualPane.getChildren().addAll(xAxis, yAxis);
    }

    private void addBandShape(MusicBand band) {
        if (shapeMap.containsKey(band)) return;

        Rectangle rect = new Rectangle(20, 20);
        rect.setFill(colorForUser(band.getAuthor()));
        rect.setStroke(Color.BLACK);

        positionRectangle(rect, band);

        String tip = String.format("ID: %d\nName: %s\nAuthor: %s",
                band.getId(), band.getName(), band.getAuthor());
        Tooltip tooltip = new Tooltip(tip);
        Tooltip.install(rect, tooltip);

        rect.setOnMouseClicked(evt -> {
            if (!currentUser.login().equals(band.getAuthor())) {
                return;
            }
            EditFormWindow editWindow = new EditFormWindow(visStage, currentUser, band, null);
            editWindow.setOnCloseAction(() -> {
                MusicBand updated = editWindow.getUpdatedBand();
                //Alerts.info(updated.toString());
                if (updated != null) {
                    int idx = bandList.indexOf(band);
                    if (idx >= 0) {
                        Platform.runLater(() -> bandList.set(idx, updated));
                    }
                }
                shapeMap.remove(band);
                shapeMap.put(updated,rect);
                updateBandShape(updated);
            });
            editWindow.showAndWait();
        });

        rect.setOpacity(0);
        rect.setScaleX(0.5);
        rect.setScaleY(0.5);

        FadeTransition fade = new FadeTransition(Duration.millis(400), rect);
        fade.setFromValue(0);
        fade.setToValue(1);

        ScaleTransition scale = new ScaleTransition(Duration.millis(400), rect);
        scale.setFromX(0.5);
        scale.setFromY(0.5);
        scale.setToX(1.0);
        scale.setToY(1.0);

        ParallelTransition appear = new ParallelTransition(fade, scale);
        appear.play();

        visualPane.getChildren().add(rect);
        shapeMap.put(band, rect);
    }

    private void removeBandShape(MusicBand band) {
        Rectangle rect = shapeMap.remove(band);
        if (rect != null) {
            visualPane.getChildren().remove(rect);
        }
    }

    private void updateBandShape(MusicBand band) {
        Rectangle rect = shapeMap.get(band);
        if (rect != null) {
            positionRectangle(rect, band);
            rect.setFill(colorForUser(band.getAuthor()));
            String tip = String.format("ID: %d\nName: %s\nAuthor: %s",
                    band.getId(), band.getName(), band.getAuthor());
            Tooltip.install(rect, new Tooltip(tip));
        }
    }

    private void positionRectangle(Rectangle rect, MusicBand band) {
        double w = visualPane.getWidth();
        double h = visualPane.getHeight();
        double centerX = w / 2.0;
        double centerY = h / 2.0;

        double scaleX = w / 1600.0;
        double scaleY = h / 1000.0;

        Long realX = null;
        Integer realY = null;
        if (band.getCoordinates() != null) {
            realX = band.getCoordinates().getX();
            realY = band.getCoordinates().getY();
        }
        if (realX == null) realX = 0L;
        if (realY == null) realY = 0;

        double px = centerX + realX * scaleX - rect.getWidth() / 2.0;
        double py = centerY - realY * scaleY - rect.getHeight() / 2.0;

        rect.setLayoutX(px);
        rect.setLayoutY(py);
    }

    private Color colorForUser(String user) {
        if (user == null) user = "";
        return userColorMap.computeIfAbsent(user, u -> {
            int hash = Math.abs(u.hashCode());
            double hue = hash % 360;
            return Color.hsb(hue, 0.8, 1);
        });
    }
}
