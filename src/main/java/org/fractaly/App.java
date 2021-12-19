package org.fractaly;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.imageio.ImageIO;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.fractaly.utils.Complex;
import org.fractaly.view.Fractal;

/**
 * JavaFX App
 */
public class App extends Application {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 1000;
    private ImageView v;
    private double zoomFactor = 1.0;

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

            ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
        }
    }

  

    public void zoomIn() {
        Instant before = Instant.now();
        zoomFactor += 0.1;
        Function<Complex, Complex> julia = c -> c.multiply(c).add(Complex.build(0, -0.8)); // Fonction Julia
        BiFunction<Integer, Integer, Color> color = (i, maxI) -> Color.hsb((i / (float) maxI) * 360, 0.7, 0.7);
        Fractal f = new Fractal.Builder(WIDTH, HEIGHT).colorFunction(color).juliaFunction(julia).zoom(zoomFactor)
                .buildMandelbrot();
        v.setImage(f);
        Duration d = Duration.between(before, Instant.now());
        System.out.println(d.toString());
    }

    @Override
    public void start(Stage stage) {
        Function<Complex, Complex> julia = c -> c.multiply(c).add(Complex.build(0, -0.8)); // Fonction Julia
        BiFunction<Integer, Integer, Color> color = (i, maxI) -> Color.hsb((i / (float) maxI) * 360, 0.7, 0.7);
        Fractal f = new Fractal.Builder(WIDTH, HEIGHT).colorFunction(color).juliaFunction(julia).zoom(1)
                .buildMandelbrot();
        try {
            saveImageFile(f, stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public static void main(String[] args) throws ParseException, IOException {
        Options options = new Options();
        options.addRequiredOption("f", "function", true, "MandelBrot | Julia ");
        options.addRequiredOption("m", "mode", true, "terminal | gui");

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("CLITester", options, true);

        // ***Parsing Stage***
        // Create a parser
        CommandLineParser parser = new DefaultParser();

        // parse the options passed as command line arguments
        CommandLine cmd = parser.parse(options, args);

        // ***Interrogation Stage***
        // hasOptions checks if option is present or not
        String getopt = "";
        Fractal fract;
        WritableImage writableImage;
        
        if (cmd.hasOption("m")) {
            getopt = cmd.getOptionValue("m");
            if(getopt.equals("gui")){
                launch();
            }
            if(getopt.equals("terminal")){
                System.out.println("Bienvenue dans la version Terminale");

                try (Scanner sc = new Scanner(System.in)) {
                    System.out.println("Entrez X:");
                    String xa = sc.nextLine();
                    final Double x = Double.parseDouble(xa);
                    System.out.println("Entrez Y:");
                    String ya = sc.nextLine();
                    final double y = Double.parseDouble(ya);
                    Function<Complex, Complex> julia = c -> c.multiply(c).add(Complex.build(x, y)); // Fonction Julia
                    BiFunction<Integer, Integer, Color> color = (i, maxI) -> Color.hsb((i / (float) maxI) * 360, 0.7,
                            0.7);
                    getopt = cmd.getOptionValue("f");
                    if(getopt.equals("MandelBrot")){
                       fract = new Fractal.Builder(WIDTH, HEIGHT).colorFunction(color).juliaFunction(julia).zoom(1).buildMandelbrot();
                    }else{
                        fract = new Fractal.Builder(WIDTH, HEIGHT).colorFunction(color).juliaFunction(julia).zoom(1).buildJulia(); 
                    }
                    File outputFile = new File("image");
                    ImageIO.write(SwingFXUtils.fromFXImage(fract, null), "png", outputFile);
                }
                
            }
        }
        

    }

}
