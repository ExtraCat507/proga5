package org.xtracat.UI.registerForm;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.xtracat.UI.mainForm.MainFormWindow;
import org.xtracat.UI.util.Alerts;
import org.xtracat.UI.util.Localization;
import org.xtracat.client.util.Dispatcher;
import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;
import org.xtracat.usershit.User;

import java.util.Locale;

public class RegFormController {
    private User initedUser;

    @FXML
    private Label loginOrSignUpLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private Label passwordLabel;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;
    @FXML
    private ComboBox<String> langBox;
    @FXML
    private Label langField;

    private final Dispatcher dispatcher = new Dispatcher();

    @FXML
    public void initialize() {
        // Set a default locale and populate the language selection box
        Localization.setLocale(new Locale("ru")); // Default to Russian as per FXML text
        langBox.getItems().addAll("Русский", "Portugal", "ελληνικά", "Español (el Salvador)");
        langBox.setValue("Русский"); // Set default value in the box

        // Initial UI text update
        updateUI();
    }

    private void updateUI() {
        // Update all text elements based on the current locale
        loginOrSignUpLabel.setText(Localization.get("login_or_sign_up"));
        usernameLabel.setText(Localization.get("username"));
        passwordLabel.setText(Localization.get("password"));
        loginButton.setText(Localization.get("login"));
        registerButton.setText(Localization.get("register"));
        langField.setText(Localization.get("lang"));

    }

    @FXML
    protected void onRegButtonClicked() {

        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Alerts.error(Localization.get("empty_credentials_error"));
            return;
        }

        User user = new User(username, password);
        Request authRequest = new Request("register", null, user);
        Response response = dispatcher.send(authRequest);

        if (response.getData() == null || response.getMessage().equals("Нет ответа от сервера.")) {
            Alerts.error(response.getMessage());
            return;
        }
        Alerts.info(response.getMessage());
        initedUser = (User)response.getData();
        closeWindowAndOpenMain();
    }

    @FXML
    protected void onLoginButtonClicked() {

        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Alerts.error(Localization.get("empty_credentials_error"));
            return;
        }

        User user = new User(username, password);
        Request authRequest = new Request("auth", null, user);
        Response response = dispatcher.send(authRequest);

        Alerts.info(response.getMessage());

        if (response.getData() == null) {
            return;
        }
        initedUser = (User)response.getData();
        closeWindowAndOpenMain();
    }

    @FXML
    protected void localeChanged() {
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

    private void closeWindowAndOpenMain() {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();
        stage.close();
        MainFormWindow root = new MainFormWindow(initedUser);
        root.show();
    }
}