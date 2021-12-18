package org.fractaly;

import javafx.fxml.*;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.fractaly.screens.Fractal;
import org.fractaly.utils.Complex;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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

    private static void saveImageFile(WritableImage writableImage, Stage stage) throws IOException {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "image files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            String fileName = file.getName();

            if (!fileName.toUpperCase().endsWith(".PNG")) {
                file = new File(file.getAbsolutePath() + ".png");
            }

            ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null),"png", file);
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        Fractal f = Fractal.buildJulia(1000, 1000, 1000, Complex.build(-0.4, 0.6));
        saveImageFile(f, stage);
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