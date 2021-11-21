package org.fractaly;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.function.Function;

import org.fractaly.screens.Fractal;
import org.fractaly.utils.Complex;

/**
 * JavaFX App
 */
public class App extends Application {
    @Override
    public void start(Stage stage) {
        Function<Complex, Complex> func = c -> c.multiply(c).add(Complex.build(0.285, 0.01));
        Fractal f = new Fractal.Builder(1000, 1000).maxIter(100).function(func).build();
        ImageView v = new ImageView(f);

        var scene = new Scene(new StackPane(v), 1000, 1000);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
