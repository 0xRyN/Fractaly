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
import org.fractaly.view.Fractal.Builder;

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

    public void zoomIn(boolean isMandelbrot) {
        Instant before = Instant.now();
        zoomFactor += 0.1;
        Function<Complex, Complex> julia = c -> c.multiply(c).add(Complex.build(0, -0.8)); // Fonction Julia
        BiFunction<Integer, Integer, Color> color = (i, maxI) -> Color.hsb((i / (float) maxI) * 360, 0.7, 0.7);
        Builder build =  new Fractal.Builder(WIDTH, HEIGHT).colorFunction(color).juliaFunction(julia).zoom(zoomFactor);
        Fractal f = null;
        if(isMandelbrot){
            f = build.buildMandelbrot();
        }else{
            f = build.buildJulia();
        }
        v.setImage(f);
        Duration d = Duration.between(before, Instant.now());
        System.out.println(d.toString());
    }

    @Override
    public void start(Stage stage) {
        Parameters params = getParameters();
        String getFunction = params.getNamed().get("function");
        double getX = Double.parseDouble(params.getNamed().get("x"));
        double getY = Double.parseDouble(params.getNamed().get("y"));

        Function<Complex, Complex> julia;
        Builder build = new Fractal.Builder(WIDTH, HEIGHT);
        Fractal f = null;

        switch(getFunction){
            case "Julia":
                julia = c -> c.multiply(c).add(Complex.build(getX, getY));
                f = build.juliaFunction(julia).buildJulia();
                v = new ImageView(f);
                v.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        zoomIn(false);
                    }
                });
                break;
            default: case "MandelBrot":
                f = build.buildMandelbrot();
                v = new ImageView(f);
                v.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        zoomIn(true);
                    }
                });
                break;
        }
        var scene = new Scene(new StackPane(v), 1000, 1000);
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) throws ParseException, IOException {
        Options options = new Options();
        options.addRequiredOption("f", "function", true, "MandelBrot | Julia ");
        options.addRequiredOption("m", "mode", true, "terminal | gui");

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("AppTester", options, true);

        // ***Parsing Stage***
        CommandLineParser parser = new DefaultParser();

        // parse the options passed as command line arguments
        CommandLine cmd = parser.parse(options, args);

        // ***Interrogation Stage***
        String getFunction = "";
        String getMode = "";
        Fractal fract;
        
        if (cmd.hasOption("m")) {
            getMode = cmd.getOptionValue("m");
            if(getMode.equals("gui")){
                getFunction = cmd.getOptionValue("f");

                try (Scanner sc = new Scanner(System.in)) {
                    System.out.println("Please enter X:");
                    String xa = sc.nextLine();
                    final Double x = Double.parseDouble(xa);
                    System.out.println("Please enter Y:");
                    String ya = sc.nextLine();
                    final double y = Double.parseDouble(ya);

                    Application.launch(App.class,
                            "--function=" + getFunction,
                            "--x="+x,
                            "--y="+y
                    );
                }
            }
            if(getMode.equals("terminal")){
                
                System.out.println("Welcome on the terminal!");

                if(getFunction.equals("MandelBrot")){
                    fract = new Fractal.Builder(WIDTH, HEIGHT).buildMandelbrot();
                }else{ 
                    if(getFunction.equals("Julia")){
                        try (Scanner sc = new Scanner(System.in)) {
                            System.out.println("Please enter X:");
                            String xa = sc.nextLine();
                            final Double x = Double.parseDouble(xa);
                            System.out.println("Please enter Y:");
                            String ya = sc.nextLine();
                            final double y = Double.parseDouble(ya);

                            getFunction = cmd.getOptionValue("f");
                            Function<Complex, Complex> julia = c -> c.multiply(c).add(Complex.build(x, y)); 
                            fract = new Fractal.Builder(WIDTH, HEIGHT).juliaFunction(julia).buildJulia(); 

                            File outputFile = new File(getFunction);
                            ImageIO.write(SwingFXUtils.fromFXImage(fract, null), "png", outputFile);
                            System.out.println("An image was created: "+outputFile.getName());
                        }
                    }else{
                        System.out.println(getFunction+": Function doesn't exist");
                        formatter.printHelp("AppTester", options, true);
                    }

                }
                
            }
        }

        

    }

}
