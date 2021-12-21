package org.fractaly.view;

import java.util.Observable;

import org.fractaly.App;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Choice extends Application {

    
    // launch the application
    public void start(Stage s) {
        // set title for the stage
        s.setTitle("Choice Box");

        // create a button
        Button b = new Button("show");

        // create a tile pane
        TilePane r = new TilePane();

        // create a label
        Label l = new Label("Choose an option");


        // create a choiceBox
        ChoiceBox<String> mChoiceBox = new ChoiceBox<>();
        ChoiceBox<String> dChoiceBox = new ChoiceBox<>();
        mChoiceBox.getItems().addAll("Custom", "Default");
        dChoiceBox.getItems().addAll("c = 0.285 + 0i", "c = 0.285 + 0.01i");
          
        mChoiceBox.setOnAction(event -> {
            String getString = mChoiceBox.getValue();
            r.getChildren().remove(mChoiceBox);
            if(getString.equals("Custom")){
                
                r.getChildren().add(dChoiceBox);
            }else{
                System.out.println("On va mettre ici de la merde");
            }
        });

        dChoiceBox.setOnAction(event -> {
            String getString = dChoiceBox.getValue();
            r.getChildren().remove(dChoiceBox);
            switch(getString){
                default: case "c = 0.285 + 0i":
                    s.close();
                    Platform.exit();
                    launch(App.class);
                    break;
                case "c = 0.285 + 0.01i":
                    break;
            }
        });

        // add ChoiceBox
        r.getChildren().add(l);
        r.getChildren().add(mChoiceBox);

        // create a scene
        Scene sc = new Scene(r, 400, 400);

        // set the scene
        s.setScene(sc);

        s.show();
    }
}