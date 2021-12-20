package org.fractaly;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Calendar;
import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.function.Function;

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
import org.fractaly.view.Fractal.Builder;

/**
 * JavaFX App
 */
public class App extends Application {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 1000;
    private ImageView v;

    Calendar now = Calendar.getInstance();
    private int day = now.get(Calendar.DAY_OF_MONTH);
    private int hour = now.get(Calendar.HOUR_OF_DAY);
    private int minute = now.get(Calendar.MINUTE);

    private static void saveToFile(Image image, String name) throws IOException {
        File outputFile = new File(name);
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", outputFile);
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
        }
        ;
    }

    public void move(double offsetX, double offsetY) {
        Fractal fract = (Fractal) v.getImage();
        Instant before = Instant.now();
        // Adding old offset makes it not very intuitive
        double resOffsetX = -(Math.abs(fract.getOffsetX()) + offsetX);
        double resOffsetY = -(Math.abs(fract.getOffsetY()) + offsetY);
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
        v.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                MouseButton gEvent = mouseEvent.getButton();
                switch (gEvent) {
                    default:
                    case PRIMARY:
                        zoom(true);
                        break;
                    case SECONDARY:
                        zoom(false);
                        break;
                    case MIDDLE:
                        double width = v.getBoundsInLocal().getWidth();
                        double centery = (WIDTH) / 2;
                        double centerx = (HEIGHT) / 2;

                        double scale = v.getBoundsInLocal().getHeight() / width;
                        double xOffset = scale * (centerx - mouseEvent.getX());
                        double yOffset = scale * (centery - mouseEvent.getY());

                        move(xOffset, yOffset);
                        break;
                }
            }
        });

        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                if (ke.getCode() == KeyCode.UP) {
                    move(0, 20);
                    ke.consume(); // <-- stops passing the event to next node
                }

                else if (ke.getCode() == KeyCode.DOWN) {
                    move(0, -20);
                    ke.consume(); // <-- stops passing the event to next node
                }

                else if (ke.getCode() == KeyCode.RIGHT) {
                    move(-20, 0);
                    ke.consume(); // <-- stops passing the event to next node
                }

                else if (ke.getCode() == KeyCode.LEFT) {
                    move(20, 0);
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
            }
        });

        // When window closes, close all threads and exit the program
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
                System.exit(0);
            }
        });
    }

    @Override
    public void start(Stage stage) {
        Parameters params = getParameters();
        String getFunction = params.getNamed().get("function");
        double getX = Double.parseDouble(params.getNamed().get("x"));
        double getY = Double.parseDouble(params.getNamed().get("y"));

        // Builds and displays a fractal based on arguments.
        Builder build = new Fractal.Builder(WIDTH, HEIGHT).colorFunction(FractalColors.GRAY_SCALE);
        Fractal f = null;
        switch (getFunction) {
            case "j":
                Function<Complex, Complex> julia = c -> c.multiply(c).add(Complex.build(getX, getY));
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
        final String name = getFunction + "_" + day + "_" + hour + "_" + minute;

        button.setOnAction(e -> {
            try {
                saveToFile(fra, name);
                File description = createTextFile(name);
                addDescription(description, .0, .0, name);
                System.out.println("A description was created " + description.getName());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        button.setVisible(true);

        stackPane.getChildren().add(button);
        StackPane.setAlignment(button, Pos.BOTTOM_CENTER);
        root.getChildren().add(stackPane);

        addEventListeners(scene, stage);

        stage.setTitle("Image created with: " + getFunction);
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) throws ParseException, IOException, InterruptedException {
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
        formatter.printHelp("AppTester", options, true);

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
                Scanner sc = new Scanner(System.in);
                // Parse args
                System.out.println("Please enter X:");
                String xa = sc.nextLine();
                final Double x = Double.parseDouble(xa);
                System.out.println("Please enter Y:");
                String ya = sc.nextLine();
                final double y = Double.parseDouble(ya);

                // Print instructions
                printInstructions();

                // Launch app
                Application.launch(App.class,
                        "--function=" + getFunction,
                        "--x=" + x,
                        "--y=" + y);

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
                formatter.printHelp("AppTester", options, true);
                throw new IllegalArgumentException();
            }
        }

        String name = "";

        if (cmd.hasOption("t")) {
            System.out.println("Welcome on the terminal!");

            Calendar now_ = Calendar.getInstance();
            final int day_ = now_.get(Calendar.DAY_OF_MONTH);
            final int hour_ = now_.get(Calendar.HOUR_OF_DAY);
            final int minute_ = now_.get(Calendar.MINUTE);

            if (cmd.hasOption("m")) {
                fract = new Fractal.Builder(WIDTH, HEIGHT).buildMandelbrot();

                name = "MandelBrot_" + day_ + "_" + hour_ + "_" + minute_;
                File outputFile = new File(name);
                ImageIO.write(SwingFXUtils.fromFXImage(fract, null), "png", outputFile);
                System.out.println("An image was created: " + outputFile.getName());
                File description = createTextFile(outputFile.getName());
                addDescription(description, .0, .0, "Mandelbrot");
                System.out.println("A description was created " + description.getName());
            } else {
                if (cmd.hasOption("j")) {
                    try (Scanner sc = new Scanner(System.in)) {
                        System.out.println("Please enter X:");
                        String xa = sc.nextLine();
                        final Double x = Double.parseDouble(xa);
                        System.out.println("Please enter Y:");
                        String ya = sc.nextLine();
                        final double y = Double.parseDouble(ya);

                        Function<Complex, Complex> julia = c -> c.multiply(c).add(Complex.build(x, y));
                        fract = new Fractal.Builder(WIDTH, HEIGHT).juliaFunction(julia).buildJulia();

                        name = "Julia_" + day_ + "_" + hour_ + "_" + minute_;
                        File outputFile = new File(name);

                        ImageIO.write(SwingFXUtils.fromFXImage(fract, null), "png", outputFile);
                        System.out.println("An image was created: " + outputFile.getName());
                        File description = createTextFile(outputFile.getName());
                        addDescription(description, x, y, "Julia");
                        System.out.println("A description was created " + description.getName());

                    }
                } else {
                    formatter.printHelp("AppTester", options, true);
                    throw new IllegalArgumentException();
                }

            }

        }
    }

}
