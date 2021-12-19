package org.fractaly;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.fractaly.screens.Fractal;
import org.fractaly.utils.Complex;
import org.fractaly.utils.Julia;

/**
 * JavaFX App
 */
public class App extends Application {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 1000;
    private ImageView v;
    public double zoomFactor = 1.0;

    public void zoomIn() {
        Instant before = Instant.now();
        zoomFactor += 0.1;
        Function<Complex, Complex> julia = c -> c.multiply(c).add(Complex.build(0, -0.8)); // Fonction Julia
        BiFunction<Integer, Integer, Color> color = (i, maxI) -> Color.hsb((i / (float) maxI) * 360, 0.7, 0.7);
        Fractal f = new Fractal.Builder(WIDTH, HEIGHT).colorFunction(color).juliaFunction(julia).zoom(zoomFactor)
                .buildJulia();
        v.setImage(f);
        Duration d = Duration.between(before, Instant.now());
        System.out.println(d.toString());
    }

    @Override
    public void start(Stage stage) {
        Function<Complex, Complex> julia = c -> c.multiply(c).add(Complex.build(0, -0.8)); // Fonction Julia
        BiFunction<Integer, Integer, Color> color = (i, maxI) -> Color.hsb((i / (float) maxI) * 360, 0.7, 0.7);
        Fractal f = new Fractal.Builder(WIDTH, HEIGHT).colorFunction(color).juliaFunction(julia).zoom(1).buildJulia();
        v = new ImageView(f);
        v.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                zoomIn();
            }
        });

        var scene = new Scene(new StackPane(v), 1000, 1000);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
