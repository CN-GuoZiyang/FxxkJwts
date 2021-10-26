package top.guoziyang.fxxkjwts.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import top.guoziyang.fxxkjwts.MainApplication;
import top.guoziyang.fxxkjwts.common.OkHttpUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    @FXML
    public void initialize() {
        // 登录页面初始化
        Request r = new Request.Builder()
                .url("http://jwts.hit.edu.cn/")
                .get()
                .build();
        Call call = OkHttpUtil.getInstance().newCall(r);
        String rawCookies = null;
        try {
            Response response = call.execute();
            Map<String, List<String>> m = response.headers().toMultimap();
            rawCookies = m.get("Set-Cookie").toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] cookies = rawCookies.substring(1, rawCookies.length()-1).split(";|,");
        List<String> cookiesList = new ArrayList<>();
        for(String cookie : cookies) {
            if(cookie.contains("name") || cookie.contains("JSESSIONID") || cookie.contains("clwz_blc_pst")) {
                cookiesList.add(cookie.trim());
            }
        }
        String finalCookie = String.join("; ", cookiesList);
        r = new Request.Builder()
                .url("http://jwts.hit.edu.cn/captchaImage")
                .header("Cookie", finalCookie)
                .get()
                .build();
        call = OkHttpUtil.getInstance().newCall(r);
        try {
            Response response = call.execute();
            captchaImage.setImage(new Image(response.body().byteStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
