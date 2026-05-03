package com.scheduler.schedulercomparison.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * نقطة بداية البرنامج كله
 * زي الـ main method بس للـ JavaFX
 */
public class MainApp extends Application {

    public static final String APP_TITLE = "CPU Scheduler Comparison - RR vs Priority";
    public static final double WIDTH = 1200;
    public static final double HEIGHT = 800;

    @Override
    public void start(Stage primaryStage) {

        // إنشاء الـ MainView (الواجهة الرئيسية)
        MainView mainView = new MainView(primaryStage);

        // إنشاء الـ Scene
        Scene scene = new Scene(mainView.getRoot(), WIDTH, HEIGHT);

        // إضافة الـ CSS
        scene.getStylesheets().add(
                getClass().getResource("/styles/main.css").toExternalForm()
        );

        // إعداد الـ Stage
        primaryStage.setTitle(APP_TITLE);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}