package org.fractaly;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.fractaly.screens.Fractal;
import org.fractaly.utils.Complex;

/**
 * JavaFX App
 */
public class App extends Application {

    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;
    public static ImageView v;
    public static LinkedList<Fractal> l;

    public static double round(double value, int places) {
        if (places < 0)
            throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static void beautiful() {
        l = new LinkedList<>();
        // 0.7885
        for (int i = 0; i < 360; i++) {
            Complex c = Complex.fromPolar(0.7885, Math.toRadians(i));
            Function<Complex, Complex> f = x -> x.multiply(x).add(c);
            l.add(new Fractal.Builder(WIDTH, HEIGHT).juliaFunction(f).build());
            System.out.println(i);
        }
        v.setImage(l.getFirst());
    }

    public static void playBeautiful() {

        Timeline t = new Timeline(new KeyFrame(Duration.millis(30), new EventHandler<ActionEvent>() {
            Iterator<Fractal> it = l.iterator();

            @Override
            public void handle(ActionEvent event) {
                if (it.hasNext())
                    v.setImage(it.next());
                else {
                    it = l.iterator();
                }
            }
        }));
        t.setCycleCount(Timeline.INDEFINITE);
        t.play();
    }

    public static void replace(double x, double y) {
        double xx = round((-1 + ((2.0 / HEIGHT) * x)), 4);
        double yy = round((-1 + ((2.0 / WIDTH) * y)), 4);
        System.out.println(xx);
        System.out.println(yy);
        Fractal f = new Fractal.Builder(WIDTH, HEIGHT).juliaFunction(c -> c.multiply(c).add(Complex.build(xx, yy)))
                .build();
        v.setImage(f);
    }

    @Override
    public void start(Stage stage) {
        Function<Complex, Complex> julia = c -> c.multiply(c).add(Complex.build(0, -0.8)); // Fonction Julia
        BiFunction<Integer, Integer, Color> color = (i, maxI) -> Color.hsb((i / (float) maxI) * 360, 0.7, 0.7);
        v = new ImageView();
        // v.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
        // @Override
        // public void handle(MouseEvent mouseEvent) {
        // replace(mouseEvent.getX(), mouseEvent.getY());
        // };
        // });
        v.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                playBeautiful();
            }
        });
        beautiful();

        var scene = new Scene(new StackPane(v), 1000, 1000);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
