package org.example.view;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Tower Defense");
        stage.setWidth(800);
        stage.setHeight(600);
        stage.setResizable(false);

        MenuView menu = new MenuView(stage);
        stage.setScene(menu.getScene());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}