package com.jamesharris.langtonantcellularautomatonparallelimplementationcomparisonjavajavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Launcher extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("main-view.fxml"));
        BorderPane root = loader.load();

        stage.setTitle("Langton's Ant - Parallel Simulation");

        Scene scene = new Scene(root, 720, 640);

        stage.setScene(scene);

        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}