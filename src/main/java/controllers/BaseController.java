package main.java.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.ResourceBundle;

public class BaseController implements Initializable{
    @FXML
    private AnchorPane paneRight;
    @FXML
    private AnchorPane paneLeft;
    @FXML
    private JFXButton btnDashboard;
    @FXML
    private Label lblAccessLevel;
    @FXML
    private Label lblUsername;
    @FXML
    private JFXButton btnCustomers;
    @FXML
    private JFXButton btnInventoryItem;
    @FXML
    private JFXButton btnAccounts;
    @FXML
    private JFXButton btnDueUpdate;
    @FXML
    private JFXButton btnAdmin;
    @FXML
    private JFXButton btn;
    @FXML
    private Label lblClock;
    @FXML
    private JFXButton btnRentals;
    @FXML
    private JFXButton btnSells;
    private static String username = "";
    private static String accessLevel = "";
    private AnchorPane newRightPane = null;
    private JFXButton temp = null;
    private JFXButton recover = null;
    private static boolean anchorFlag = false;
    private HashMap<String, String> FXML_URL = new HashMap<>();
    private void autoResizePane() {
        newRightPane.setPrefWidth(paneRight.getWidth());
        newRightPane.setPrefHeight(paneRight.getHeight());
    }
    @FXML
    private void ctrlRightPane(String URL) throws IOException {
        try {
            paneRight.getChildren().clear();
            newRightPane = FXMLLoader.load(getClass().getResource(URL));

            newRightPane.setPrefHeight(paneRight.getHeight());
            newRightPane.setPrefWidth(paneRight.getWidth());

            paneRight.getChildren().add(newRightPane);
            paneRight.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> {
                autoResizePane();
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void btnNavigators(ActionEvent event) {
        borderSelector(event);

        JFXButton btn = (JFXButton)event.getSource();
        String btnText = btn.getText();
        try {
            ctrlRightPane(FXML_URL.get(btnText));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadFXMLMap() {
        FXML_URL.put("Dashboard", "/main/resources/view/login.fxml");
        FXML_URL.put("Items", "/main/resources/view/inventory.fxml");
        FXML_URL.put("Customers", "/main/resources/view/customer.fxml");
        FXML_URL.put("Dashboard", "/main/resources/view/dashboard.fxml");
        FXML_URL.put("Sells", "/main/resources/view/sells.fxml");
        FXML_URL.put("Rentals", "/main/resources/view/rentals.fxml");
        FXML_URL.put("Accounts", "/main/resources/view/accounts.fxml");
        FXML_URL.put("Administrative", "/main/resources/view/administrator.fxml");
        FXML_URL.put("Update Due", "/main/resources/view/dueupdate.fxml");
    }
    private void borderSelector(ActionEvent event) {
        JFXButton btn = (JFXButton)event.getSource();

        if(temp == null) {
            temp = btn;
        } else {
            temp.setStyle("");
            temp = btn;
        }

        btn.setStyle("-fx-background-color: #455A64");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadFXMLMap();
        username = LogInController.loggerUsername;
        accessLevel = LogInController.loggerAccessLevel;
        lblUsername.setText(username.toUpperCase());
        lblAccessLevel.setText(accessLevel);

        //Controling access by checking access level of user
        if(accessLevel.equals("Employee")) {
            btnAdmin.setDisable(true);

        }
        //Setting Clock within a new Thread
        Runnable clock = new Runnable() {
            @Override
            public void run() {
                runClock();
            }
        };

        Thread newClock = new Thread(clock);
        newClock.setDaemon(true);
        newClock.start();
        try {
            ctrlRightPane("/main/resources/view/dashboard.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void runClock() {
        while (true) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    String time = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss a"));
                    lblClock.setText(time);
                }
            });

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void logOut() {
        Stage current = (Stage)lblUsername.getScene().getWindow();
        current.close();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/main/resources/view/login.fxml"));
            root.getStylesheets().add("/main/resources/css/login.css");
            Scene scene = new Scene(root);
            Stage logInPrompt = new Stage();
            logInPrompt.setScene(scene);
            logInPrompt.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
