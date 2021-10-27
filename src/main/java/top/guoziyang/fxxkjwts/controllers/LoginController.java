package top.guoziyang.fxxkjwts.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.guoziyang.fxxkjwts.MainApplication;
import top.guoziyang.fxxkjwts.common.OkHttpUtil;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginController {
    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    public static Stage stage;

    private double x, y = 0;

    @FXML
    private PasswordField password;
    @FXML
    private TextField captcha;
    @FXML
    private ImageView captchaImage;
    @FXML
    private TextField username;
    @FXML
    private Label errorMsg;
    @FXML
    private AnchorPane ap;

    @FXML
    public void login(ActionEvent event) {
        Map<String, String> loginData = new HashMap<>();
        loginData.put("usercode", username.getText());
        loginData.put("password", password.getText());
        loginData.put("code", captcha.getText());
        Response response = OkHttpUtil.postWithCookie("http://jwts.hit.edu.cn/loginLdap", loginData);
        try {
            if(response == null || response.code() != 200) {
                errorMsg.setText("网络错误，请重试");
            }
            String responseStr = response.body().string();
            String pattern = "您好！([\\u4e00-\\u9fa5]{2,})同学";
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(responseStr);
            if(m.find()) {
                logger.info("Login as " + m.group(1));
                stage.hide();
                try {
                    showMain(new Stage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                errorMsg.setText("用户名或密码错误");
                refreshCaptcha();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            response.close();
        }
    }

    private void showMain(Stage stage) throws IOException {
        MainController.stage = stage;
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
        refreshCaptcha();
    }

    @FXML
    public void refreshCaptcha() {
        Response captchaRes = OkHttpUtil.getWithCookie("http://jwts.hit.edu.cn/captchaImage");
        if(captchaRes == null || captchaRes.code() != 200) {
            logger.error("refresh captcha error");
            errorMsg.setText("刷新验证码失败");
        }
        captchaImage.setImage(new Image(captchaRes.body().byteStream()));
        captchaRes.close();
    }

    @FXML
    public void clearErrorMsg() {
        errorMsg.setText("");
    }

    @FXML
    public void closeProgram() {
        stage.close();
        if(MainController.stage != null) {
            MainController.stage.close();
        }
    }

}
