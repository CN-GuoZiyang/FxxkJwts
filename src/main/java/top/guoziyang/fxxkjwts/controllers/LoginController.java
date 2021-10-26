package top.guoziyang.fxxkjwts.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import top.guoziyang.fxxkjwts.MainApplication;

import java.io.IOException;
import java.util.Objects;

public class LoginController {

    private double x, y = 0;

    @FXML
    private TextField password;
    @FXML
    private TextField captcha;
    @FXML
    private ImageView captchaImage;
    @FXML
    private TextField username;

    @FXML
    void login(ActionEvent event) {
        username.getScene().getWindow().hide();
        try {
            showMain(new Stage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showMain(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource("main.fxml")));
        Scene scene = new Scene(root);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        // 鼠标拖拽
        root.setOnMousePressed(mouseEvent -> {
            x = mouseEvent.getSceneX();
            y = mouseEvent.getSceneY();
        });
        root.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - x);
            stage.setY(mouseEvent.getScreenY() - y);
        });
        stage.setScene(scene);
        stage.show();
    }

}
