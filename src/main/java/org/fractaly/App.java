package org.fractaly;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import org.fractaly.screens.Fractal;
import org.fractaly.utils.Complex;

/**
 * JavaFX App
 */
public class App extends Application {
    @Override
    public void start(Stage stage) {
        Fractal f = Fractal.buildJulia(1000, 1000);
        ImageView v = new ImageView(f);

        var scene = new Scene(new StackPane(v), 1000, 1000);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}