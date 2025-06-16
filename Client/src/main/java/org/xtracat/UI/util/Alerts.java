package org.xtracat.UI.util;

import javafx.scene.control.Alert;

public class Alerts {

    public static void info(String info) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(Localization.get("message"));
        alert.setHeaderText(null);
        alert.setContentText(info);
        alert.showAndWait();
    }


    public static void error(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(error);
        alert.showAndWait();

    }

}