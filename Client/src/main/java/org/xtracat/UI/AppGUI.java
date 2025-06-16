package org.xtracat.UI;

import javafx.application.Application;
import javafx.stage.Stage;
import org.xtracat.UI.registerForm.RegFormWindow;
import org.xtracat.UI.util.Localization;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class AppGUI extends Application {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    //Stage primaryStage = new Stage();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Localization.setLocale(new Locale("ru"));
        RegFormWindow authWindow = new RegFormWindow(primaryStage);
        authWindow.show();
    }
}