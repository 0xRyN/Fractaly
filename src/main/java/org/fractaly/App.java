package org.fractaly;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.time.Duration;
import java.time.Instant;
import java.util.Calendar;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import javax.imageio.ImageIO;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.fractaly.utils.Complex;
import org.fractaly.utils.FractalColors;
import org.fractaly.view.Fractal;
import org.fractaly.view.JuliaDialog;
import org.fractaly.view.Fractal.Builder;

/**
 * JavaFX App
 */
public class App extends Application {

    private static final String AP_STRING = "AppTest";
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 1000;
    private ImageView v;

    Calendar now = Calendar.getInstance();
    private int day = now.get(Calendar.DAY_OF_MONTH);
    private int hour = now.get(Calendar.HOUR_OF_DAY);
    private int minute = now.get(Calendar.MINUTE);
    private double getIm;
    private double getRe;

    private static void saveToFile(Image image, String name) throws IOException {
        File outputFile = new File(name);
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", outputFile);
        System.out.println("An image was created: " + outputFile.getName()+"png");
    }

    private static File createTextFile(String name) {
        return new File(name + ".txt");
    }

    private static void addDescription(File f, Double x, Double y, String function) {
        try {
            FileWriter myWriter = new FileWriter(f);
            myWriter.write("X: " + x + "| Y: " + y + " | Function: " + function);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
            System.out.println("A description was created " + f.getName());
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private static void printInstructions() {
        System.out.println("USAGE:");
        System.out.println("[ZOOM]: LEFT CLICK / ENTER");
        System.out.println("[UNZOOM]: RIGHT CLICK / BACKSPACE");
        System.out.println("[MOVE]: ARROW KEYS");
        System.out.println("You got five seconds to read this instructions...");

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    public void getComplexe(double re, double im){
        getRe = re;
        getIm = im;
    }

    public void changeJulia(double re, double im) {
        Fractal fract = (Fractal) v.getImage();
        Instant before = Instant.now();
        Fractal.Builder newBuilder = new Fractal.Builder(fract)
                .juliaFunction(c -> c.multiply(c).add(Complex.build(re, im)));
        Fractal f = null;
        f = newBuilder.buildJulia();
        v.setImage(f);
        Duration d = Duration.between(before, Instant.now());
        System.out.println("It took " + d.toString() + " to render.");
        getComplexe(re, im);
    }

    public void move(double offsetX, double offsetY) {
        Fractal fract = (Fractal) v.getImage();
        Instant before = Instant.now();
        // Adding old offset makes it not very intuitive
        double resOffsetX = (fract.getOffsetX() + offsetX);
        double resOffsetY = (fract.getOffsetY() + offsetY);
        Fractal.Builder newBuilder = new Fractal.Builder(fract).offsetX(resOffsetX).offsetY(resOffsetY);
        Fractal f = null;
        if (fract.isMandelbrot()) {
            f = newBuilder.buildMandelbrot();
        } else {

            f = newBuilder.buildJulia();
        }
        v.setImage(f);
        Duration d = Duration.between(before, Instant.now());
        System.out.println("It took " + d.toString() + " to render.");
    }

    public void zoom(boolean bool) {
        Fractal fract = (Fractal) v.getImage();
        double zoomFactor = fract.getZoom();
        Instant before = Instant.now();
        if (bool) {
            zoomFactor += 0.1;
        } else {
            zoomFactor -= 0.1;
        }
        Fractal.Builder newBuilder = new Fractal.Builder(fract).zoom(zoomFactor);
        Fractal f = null;
        if (fract.isMandelbrot()) {
            f = newBuilder.buildMandelbrot();
        } else {

            f = newBuilder.buildJulia();
        }
        v.setImage(f);
        Duration d = Duration.between(before, Instant.now());
        System.out.println("It took " + d.toString() + " to render.");
    }

    public void addEventListeners(Scene scene, Stage stage) {

        // For Zoom / Zoom out / Movement with mouse
        v.setOnMouseClicked( ev -> {
            MouseButton gEvent = ev.getButton();
            switch (gEvent) {
                default:
                case PRIMARY:
                    zoom(true);
                    break;
                case SECONDARY:
                    zoom(false);
                    break;
                    /*
                     * case MIDDLE:
                     * double width = v.getBoundsInLocal().getWidth();
                     * double centery = (WIDTH) / 2.0;
                     * double centerx = (HEIGHT) / 2.0;
                     * 
                     * double scale = v.getBoundsInLocal().getHeight() / width;
                     * double xOffset = scale * (centerx - mouseEvent.getX());
                     * double yOffset = scale * (centery - mouseEvent.getY());
                     * 
                     * move(xOffset, yOffset);
                     * break;
                     */
            }
        });

        scene.addEventFilter(KeyEvent.KEY_PRESSED, ke -> {
                if (ke.getCode() == KeyCode.UP) {
                    move(0, -20);
                    ke.consume(); // <-- stops passing the event to next node
                }

                else if (ke.getCode() == KeyCode.DOWN) {
                    move(0, 20);
                    ke.consume(); // <-- stops passing the event to next node
                }

                else if (ke.getCode() == KeyCode.RIGHT) {
                    move(20, 0);
                    ke.consume(); // <-- stops passing the event to next node
                }

                else if (ke.getCode() == KeyCode.LEFT) {
                    move(-20, 0);
                    ke.consume(); // <-- stops passing the event to next node
                }

                else if (ke.getCode() == KeyCode.ENTER) {
                    zoom(true);
                    ke.consume(); // <-- stops passing the event to next node
                }

                else if (ke.getCode() == KeyCode.BACK_SPACE) {
                    zoom(false);
                    ke.consume(); // <-- stops passing the event to next node
                }
        });

        // When window closes, close all threads and exit the program
        stage.setOnCloseRequest( ev -> {
            Platform.exit();
            System.exit(0);
        });
    }

    @Override
    public void start(Stage stage) {
        Parameters params = getParameters();
        String getFunction = params.getNamed().get("function");

        // Builds and displays a fractal based on arguments.
        Builder build = new Fractal.Builder(WIDTH, HEIGHT).colorFunction(FractalColors.RED_SCALE);
        Fractal f = null;
        switch (getFunction) {
            case "j":
                UnaryOperator<Complex> julia = c -> c.multiply(c).add(Complex.build(getRe, getIm));
                f = build.juliaFunction(julia).buildJulia();
                break;
            case "m":
                f = build.buildMandelbrot();
                break;
            default:
                break;
        }
        v = new ImageView(f);

        if (getFunction.equals("j")) {
            getFunction = "Julia";
        } else {
            getFunction = "MandelBrot";
        }

        VBox root = new VBox();
        var stackPane = new StackPane(v);

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        Button button = new Button("Save Image");

        final Fractal fra = f;
        final String function = getFunction;
        final String name = getFunction + "_" + day + "_" + hour + "_" + minute;

        button.setOnAction(e -> {
            try {
                saveToFile(v.snapshot(null, fra), name);
                File description = createTextFile(name);
                addDescription(description, getRe, getIm, function);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        button.setVisible(true);

        // For GUI controlled user input

        Dialog<Pair<Double, Double>> dialog = JuliaDialog.getInstance();

        Button changeFractal = new Button("Julia Fractal");
        changeFractal.setOnAction(e -> {
            Optional<Pair<Double, Double>> result = dialog.showAndWait();
            result.ifPresent(c -> changeJulia(c.getKey(), c.getValue()));
            
        });

        changeFractal.setVisible(true);

        stackPane.getChildren().add(button);
        stackPane.getChildren().add(changeFractal);
        StackPane.setAlignment(button, Pos.BOTTOM_CENTER);
        root.getChildren().add(stackPane);

        addEventListeners(scene, stage);

        stage.setTitle("Image created with: " + getFunction);
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) throws ParseException, IOException {
        Options options = new Options();

        OptionGroup mode = new OptionGroup();
        mode.addOption(new Option("g", "gui", false, "Show GUI"));
        mode.addOption(new Option("t", "terminal", false, "Use only terminal"));
        mode.setRequired(true);
        options.addOptionGroup(mode);

        OptionGroup fun = new OptionGroup();
        fun.addOption(new Option("m", "mandelbrot", false, "Use MandelBrot function"));
        fun.addOption(new Option("j", "julia", false, "Use Julia function"));
        fun.setRequired(true);
        options.addOptionGroup(fun);

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(AP_STRING, options, true);

        // ***Parsing Stage***
        CommandLineParser parser = new DefaultParser();

        // parse the options passed as command line arguments
        CommandLine cmd = parser.parse(options, args);

        // ***Interrogation Stage***
        String getFunction = "";
        Fractal fract;

        if (cmd.hasOption("g")) {
            System.out.println("Welcome on the GUI!");
            // Laucnh choice menu etc

            getFunction = fun.getSelected();
            if (cmd.hasOption("j")) {
                // Print instructions
                printInstructions();

                // Launch app
                Application.launch(App.class,
                        "--function=" + getFunction);
            }

            else if (cmd.hasOption("m")) {
                // Print instructions
                printInstructions();

                // Launch app
                Application.launch(App.class,
                        "--function=" + getFunction,
                        "--x=" + 0,
                        "--y=" + 0);
            }

            else {
                formatter.printHelp(AP_STRING, options, true);
                throw new IllegalArgumentException();
            }
        }

        String name = "";

        if (cmd.hasOption("t")) {
            System.out.println("Welcome on the terminal!");

            Calendar now = Calendar.getInstance();
            final int day = now.get(Calendar.DAY_OF_MONTH);
            final int hour = now.get(Calendar.HOUR_OF_DAY);
            final int minute = now.get(Calendar.MINUTE);

            if (cmd.hasOption("m")) {
                fract = new Fractal.Builder(WIDTH, HEIGHT).buildMandelbrot();

                name = "MandelBrot_" + day + "_" + hour + "_" + minute;
                File outputFile = new File(name);
                ImageIO.write(SwingFXUtils.fromFXImage(fract, null), "png", outputFile);
                System.out.println("An image was created: " + outputFile.getName());
                File description = createTextFile(outputFile.getName());
                addDescription(description, .0, .0, "Mandelbrot");
            } else {
                if (cmd.hasOption("j")) {
                    try (Scanner sc = new Scanner(System.in)) {
                        System.out.println("Please enter X:");
                        String xa = sc.nextLine();
                        final Double x = Double.parseDouble(xa);
                        System.out.println("Please enter Y:");
                        String ya = sc.nextLine();
                        final double y = Double.parseDouble(ya);
                        UnaryOperator<Complex> julia = c -> c.multiply(c).add(Complex.build(x, y));
                        fract = new Fractal.Builder(WIDTH, HEIGHT).juliaFunction(julia).buildJulia();

                        name = "Julia_" + day + "_" + hour + "_" + minute;
                        File outputFile = new File(name);

                        ImageIO.write(SwingFXUtils.fromFXImage(fract, null), "png", outputFile);
                        System.out.println("An image was created: " + outputFile.getName());
                        File description = createTextFile(outputFile.getName());
                        addDescription(description, x, y, "Julia");
                    }
                } else {
                    formatter.printHelp(AP_STRING, options, true);
                    throw new IllegalArgumentException();
                }

            }

        }
    }

}
