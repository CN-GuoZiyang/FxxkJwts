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
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.guoziyang.fxxkjwts.common.CourseUtil;
import top.guoziyang.fxxkjwts.common.OkHttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainController {
    private static Logger logger = LoggerFactory.getLogger(MainController.class);

    public static Map<String, String> xnxq = new HashMap<>();
    public static Map<String, String> kkxq = new HashMap<>();
    public static Map<String, String> kkyx = new HashMap<>();

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
    private ChoiceBox<String> xnxqChoice;
    @FXML
    private ChoiceBox<String> kkxqChoice;
    @FXML
    private ChoiceBox<String> kkyxChoice;

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
        categoriesChoice.setOnAction(event -> getCourseBaseInfo());
    }

    private void getCourseBaseInfo() {
        String value = categoriesChoice.getValue();
        String courseCode = CourseUtil.courseCategoryMap.get(value);
        Response response = OkHttpUtil.getWithCookie("http://jwts.hit.edu.cn/xsxk/queryXsxk?pageXklb=" + courseCode);
        if(response == null || response.code() != 200) {
            logger.error("network error");
            return;
        }
        String content = null;
        try {
            content = response.body().string().replaceAll("\n", "");
        } catch (IOException e) {
            logger.error("network error");
            e.printStackTrace();
            return;
        }

        List<String> xnxqList = new ArrayList<>();
        List<String> kkxqList = new ArrayList<>();
        List<String> kkyxList = new ArrayList<>();
        // 获取学年学期
        xnxq.clear();
        String pattern = "<option value=\"(.*?)\"[\\s]?>(.*?)</option>";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(content);
        while(m.find()) {
            if(m.group(2).contains("全部")) {
                kkxq.put(m.group(2), m.group(1));
                kkxqList.add(m.group(2));
                break;
            }
            xnxq.put(m.group(2), m.group(1));
            xnxqList.add(m.group(2));
        }
        xnxqChoice.setItems(FXCollections.observableArrayList(xnxqList));
        xnxqChoice.setValue(xnxqList.get(0));

        // 获取校区
        kkxq.clear();
        while (m.find()) {
            if(m.group(2).contains("全部")) {
                kkyx.put(m.group(2), m.group(1));
                kkyxList.add(m.group(2));
                break;
            }
            kkxq.put(m.group(2), m.group(1));
            kkxqList.add(m.group(2));
        }
        kkxqChoice.setItems(FXCollections.observableArrayList(kkxqList));
        kkxqChoice.setValue(kkxqList.get(0));

        // 获取院系
        kkyx.clear();
        while (m.find()) {
            kkyx.put(m.group(2), m.group(1));
            kkyxList.add(m.group(2));
        }
        kkyxChoice.setItems(FXCollections.observableArrayList(kkyxList));
        kkyxChoice.setValue(kkyxList.get(0));
    }

}
