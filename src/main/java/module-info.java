module top.guoziyang.fxxkjwts {
    requires javafx.controls;
    requires javafx.fxml;

    requires javafx.graphics;

    requires okhttp3;
    requires kotlin.stdlib.common;
    requires org.slf4j;

    opens top.guoziyang.fxxkjwts to javafx.fxml;
    exports top.guoziyang.fxxkjwts;
    exports top.guoziyang.fxxkjwts.controllers;
    opens top.guoziyang.fxxkjwts.controllers to javafx.fxml;

}