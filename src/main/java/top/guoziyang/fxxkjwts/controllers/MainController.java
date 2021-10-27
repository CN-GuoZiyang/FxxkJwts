package top.guoziyang.fxxkjwts.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainController {
    private static Logger logger = LoggerFactory.getLogger(MainController.class);

    public static Stage stage;

    @FXML
    public void closeProgram() {
        if(LoginController.stage != null) {
            LoginController.stage.close();
        }
        stage.close();
    }

    @FXML
    public void initialize() {

    }

}
