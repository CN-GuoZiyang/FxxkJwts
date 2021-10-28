package top.guoziyang.fxxkjwts.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.guoziyang.fxxkjwts.common.CourseUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainController {
    private static Logger logger = LoggerFactory.getLogger(MainController.class);

    public static Stage stage;

    @FXML
    private Pane infoPane;
    @FXML
    private Pane fuckPane;
    @FXML
    private Pane coursePane;
    @FXML
    private Pane aboutPane;
    @FXML
    private Pane commentPane;

    @FXML
    private Button infoButton;
    @FXML
    private Button fuckButton;
    @FXML
    private Button courseButton;
    @FXML
    private Button commentButton;
    @FXML
    private Button aboutButton;

    // fuckPanel
    @FXML
    private ChoiceBox<String> categoriesChoice;

    @FXML
    public void closeProgram() {
        if(LoginController.stage != null) {
            LoginController.stage.close();
        }
        stage.close();
    }

    @FXML
    void tabChange(ActionEvent event) {
        if(event.getSource() == infoButton) {
            infoPane.toFront();
        } else if(event.getSource() == fuckButton) {
            fuckPane.toFront();
        } else if(event.getSource() == commentButton) {
            commentPane.toFront();
        } else if(event.getSource() == courseButton) {
            coursePane.toFront();
        } else {
            aboutPane.toFront();
        }
    }

    @FXML
    public void initialize() {
        List<String> categoryName = new ArrayList<>();
        for(Map.Entry<String, String> entry : CourseUtil.courseCategoryMap.entrySet()) {
            categoryName.add(entry.getKey());
        }
        categoriesChoice.setItems(FXCollections.observableArrayList(categoryName));
        categoriesChoice.setValue("--请选择--");
        categoriesChoice.setOnAction(event -> {
            //logger.info(categoriesChoice.getValue());
        });
    }

}
