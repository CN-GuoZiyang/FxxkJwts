module top.guoziyang.fxxkjwts {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires javafx.graphics;

    opens top.guoziyang.fxxkjwts to javafx.fxml;
    exports top.guoziyang.fxxkjwts;
    exports top.guoziyang.fxxkjwts.controllers;
    opens top.guoziyang.fxxkjwts.controllers to javafx.fxml;
}