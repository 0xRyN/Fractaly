package org.fractaly;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.fractaly.screens.Fractal;
import org.fractaly.utils.Complex;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * JavaFX App
 */
public class App extends Application {
    @Override
    public void start(Stage stage) {
        Fractal f = Fractal.buildJulia(1000, 1000, 1000, Complex.build(-0.4, 0.6));
        ImageView v = new ImageView(f);

        var scene = new Scene(new StackPane(v), 1000, 1000);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Options options = new Options();
        for (String s : args) {
            System.out.println(s);
        }
        launch();
    }

}