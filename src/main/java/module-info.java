module com.scheduler.schedulercomparison {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;

    opens com.scheduler.schedulercomparison.gui to javafx.fxml;
    exports com.scheduler.schedulercomparison.gui;
}