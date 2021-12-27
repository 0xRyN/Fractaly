package org.fractaly;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Calendar;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.BiFunction;
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
    private String getNFunction;

    private static void saveToFile(Image image, String name) throws IOException {
        File outputFile = new File(name);
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", outputFile);
        System.out.println("An image was created: " + outputFile.getName() + ".png");
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
        System.out.println("[ZOOM A LITTLE]: ENTER");
        System.out.println("[ZOOM A TON AT SPECIFIC SPOT ON CLICK]: MOUSE LEFT CLICK");
        System.out.println("[UNZOOM A LITTLE]: BACKSPACE");
        System.out.println("[UNZOOM A TON AT SPECIFIC SPOT ON CLICK]: MOUSE RIGHT CLICK");
        System.out.println("[MOVE]: ARROW KEYS");
        System.out.println("[NOTE] Not every functionnality has a controller, but the Fractal API is complete.");
        System.out.println("[NOTE] Controllers can be added easily thanks to the API. Check the App.java controller !");
        System.out.println("You got five seconds to read this instructions...");

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * It's just to get the Complex for The file description
     * 
     * @param re
     * @param im
     */
    private void getComplexe(double re, double im) {
        getRe = re;
        getIm = im;
    }

    /**
     * Change the image to Julia
     * getNFunction: It's to assign the function to the file descriptor
     * 
     * @param re
     * @param im
     */
    private void changeJulia(double re, double im) {
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
        getNFunction = "Julia";
    }

    /**
     * Change the image to a Julia set with Color
     * getNFunction: It's to assign the function to the file descriptor
     * 
     * @param color
     */
    private void changeJulia(BiFunction<Integer, Integer, Color> color) {
        Fractal fract = (Fractal) v.getImage();
        Fractal.Builder newBuilder = new Fractal.Builder(fract).colorFunction(color);
        Fractal f = null;
        f = newBuilder.buildJulia();
        v.setImage(f);
        getNFunction = "Julia";
    }

    /**
     * Change the image to a Mandelbrot set
     */
    private void changeMandelbrot() {
        Fractal fract = (Fractal) v.getImage();
        Instant before = Instant.now();
        Fractal.Builder newBuilder = new Fractal.Builder(fract);
        Fractal f = null;
        f = newBuilder.buildMandelbrot();
        v.setImage(f);
        Duration d = Duration.between(before, Instant.now());
        System.out.println("It took " + d.toString() + " to render.");
        getNFunction = "MandelBrot";
    }

    /**
     * Change the actual image to a Mandelbrot set
     * 
     * @param color Lets choose the color with the function created in FractalColors
     */
    private void changeMandelbrot(BiFunction<Integer, Integer, Color> color) {
        Fractal fract = (Fractal) v.getImage();
        Instant before = Instant.now();
        Fractal.Builder newBuilder = new Fractal.Builder(fract).colorFunction(color);
        Fractal f = null;
        f = newBuilder.buildMandelbrot();
        v.setImage(f);
        Duration d = Duration.between(before, Instant.now());
        System.out.println("It took " + d.toString() + " to render.");
        getNFunction = "MandelBrot";
    }

    /**
     * Check if the actual images is a MandelBrot Set
     * 
     * @return
     */
    private boolean isMandelBrot() {
        Fractal fract = (Fractal) v.getImage();
        return fract.isMandelbrot();
    }

    private void move(double offsetX, double offsetY) {
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

    /**
     * Zoom function
     * 
     * @param bool true is for zoom in and false to zoom out
     */
    private void zoom(boolean bool) {
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

    /**
     * Zoom function
     * 
     * @param bool true is for zoom in and false to zoom out
     */
    private void zoomMore(boolean bool) {
        Fractal fract = (Fractal) v.getImage();
        double zoomFactor = fract.getZoom();
        Instant before = Instant.now();
        if (bool) {
            zoomFactor += 1;
        } else {
            zoomFactor -= 1;
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

    /**
     * Zoom function
     * 
     * @param bool true is for zoom in and false to zoom out
     */
    private void zoomMove(boolean bool, double offX, double offY) {
        System.out.println("Moving to [" + offX + "; " + offY + "] and Zooming 10x");
        Instant before = Instant.now();

        move(offX, offY);
        zoomMore(bool);
        Duration d = Duration.between(before, Instant.now());
        System.out.println("It took " + d.toString() + " to render.");
    }

    /**
     * This function allows us to move around using our mouse
     * 
     * @param scene
     * @param stage
     */
    private void addEventListeners(Scene scene, Stage stage) {

        // For Zoom / Zoom out / Movement with mouse
        v.setOnMouseClicked(ev -> {
            MouseButton gEvent = ev.getButton();
            switch (gEvent) {
                default:
                case PRIMARY:
                    double pwidth = v.getBoundsInLocal().getWidth();
                    double pcentery = (WIDTH) / 2.0;
                    double pcenterx = (HEIGHT) / 2.0;

                    double pscale = v.getBoundsInLocal().getHeight() / pwidth;
                    double pxOffset = pscale * (pcenterx - ev.getX());
                    double pyOffset = pscale * (pcentery - ev.getY());

                    zoomMove(true, -pxOffset, -pyOffset);
                    break;
                case SECONDARY:
                    double swidth = v.getBoundsInLocal().getWidth();
                    double scentery = (WIDTH) / 2.0;
                    double scenterx = (HEIGHT) / 2.0;

                    double sscale = v.getBoundsInLocal().getHeight() / swidth;
                    double sxOffset = sscale * (scenterx - ev.getX());
                    double syOffset = sscale * (scentery - ev.getY());

                    zoomMove(false, -sxOffset, -syOffset);
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
                move(0, -50);
                ke.consume(); // <-- stops passing the event to next node
            }

            else if (ke.getCode() == KeyCode.DOWN) {
                move(0, 50);
                ke.consume(); // <-- stops passing the event to next node
            }

            else if (ke.getCode() == KeyCode.RIGHT) {
                move(50, 0);
                ke.consume(); // <-- stops passing the event to next node
            }

            else if (ke.getCode() == KeyCode.LEFT) {
                move(-50, 0);
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
        stage.setOnCloseRequest(ev -> {
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

        // Transform -j and -m at the begining
        if (getFunction.equals("j")) {
            getNFunction = "Julia";
        } else {
            getNFunction = "MandelBrot";
        }

        VBox root = new VBox();
        var stackPane = new StackPane(v);
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        final Fractal fra = f;

        // For GUI controlled user input

        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Outils");

        MenuItem menuItem1 = new MenuItem("Save");
        menuItem1.setOnAction(e -> {
            try {
                String name = getNFunction + "_" + day + "_" + hour + "_" + minute;
                saveToFile(v.snapshot(null, fra), name);
                File description = createTextFile(name);
                addDescription(description, getRe, getIm, getNFunction);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        menu.getItems().add(menuItem1);

        Dialog<Pair<Double, Double>> dialog = JuliaDialog.getInstance();
        MenuItem menuItem2 = new MenuItem("Julia Fractal");

        menuItem2.setOnAction(e -> {
            Optional<Pair<Double, Double>> result = dialog.showAndWait();
            result.ifPresent(c -> changeJulia(c.getKey(), c.getValue()));
        });
        menu.getItems().add(menuItem2);
        menuBar.getMenus().add(menu);

        MenuItem menuItem3 = new MenuItem("MandelBrot");
        menuItem3.setOnAction(e -> {
            changeMandelbrot();
        });
        menu.getItems().add(menuItem3);

        /* Colors section */
        Menu subMenu = new Menu("Colors");
        MenuItem menuItem11 = new MenuItem("Blue");
        MenuItem menuItem12 = new MenuItem("Green");
        MenuItem menuItem13 = new MenuItem("Gray");
        MenuItem menuItem14 = new MenuItem("Red");
        MenuItem menuItem15 = new MenuItem("Rainbow");

        menuItem11.setOnAction(e -> {
            if (isMandelBrot()) {
                changeMandelbrot(FractalColors.BLUE_SCALE);
            } else {
                changeJulia(FractalColors.BLUE_SCALE);
            }
        });
        menuItem12.setOnAction(e -> {
            if (isMandelBrot()) {
                changeMandelbrot(FractalColors.GREEN_SCALE);
            } else {
                changeJulia(FractalColors.GREEN_SCALE);
            }
        });
        menuItem13.setOnAction(e -> {
            if (isMandelBrot()) {
                changeMandelbrot(FractalColors.GRAY_SCALE);
            } else {
                changeJulia(FractalColors.GRAY_SCALE);
            }
        });
        menuItem14.setOnAction(e -> {
            if (isMandelBrot()) {
                changeMandelbrot(FractalColors.RED_SCALE);
            } else {
                changeJulia(FractalColors.RED_SCALE);
            }
        });
        menuItem15.setOnAction(e -> {
            if (isMandelBrot()) {
                changeMandelbrot(FractalColors.RAINBOW);
            } else {
                changeJulia(FractalColors.RAINBOW);
            }
        });

        // Add Colors items to Colors sections
        subMenu.getItems().add(menuItem11);
        subMenu.getItems().add(menuItem12);
        subMenu.getItems().add(menuItem13);
        subMenu.getItems().add(menuItem14);
        subMenu.getItems().add(menuItem15);
        menu.getItems().add(subMenu);

        // Add the stackPanel for the image and the navbar at the top
        root.getChildren().add(menuBar);
        root.getChildren().add(stackPane);

        addEventListeners(scene, stage);

        stage.setTitle("Image created with: " + getNFunction);
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

        // Check if we are on the terminal
        if (cmd.hasOption("t")) {
            System.out.println("Welcome on the terminal!");

            Calendar now = Calendar.getInstance();
            final int day = now.get(Calendar.DAY_OF_MONTH);
            final int hour = now.get(Calendar.HOUR_OF_DAY);
            final int minute = now.get(Calendar.MINUTE);

            // If we are on the Mandelbrot then build directly the MandelBrot set
            if (cmd.hasOption("m")) {
                fract = new Fractal.Builder(WIDTH, HEIGHT).buildMandelbrot();
                name = "MandelBrot_" + day + "_" + hour + "_" + minute;

                // Create the image to PNG
                File outputFile = new File(name);
                ImageIO.write(SwingFXUtils.fromFXImage(fract, null), "png", outputFile);

                System.out.println("An image was created: " + outputFile.getName());

                // Add the file descriptor
                File description = createTextFile(outputFile.getName());
                addDescription(description, .0, .0, "Mandelbrot");
            } else {
                // Check if we got the -j (Julia option)
                if (cmd.hasOption("j")) {
                    try (Scanner sc = new Scanner(System.in)) {
                        // Let's get X and
                        System.out.println("Please enter Re:");
                        String xa = sc.nextLine();
                        final Double x = Double.parseDouble(xa);
                        System.out.println("Please enter Im:");
                        String ya = sc.nextLine();
                        final double y = Double.parseDouble(ya);

                        UnaryOperator<Complex> julia = c -> c.multiply(c).add(Complex.build(x, y));
                        fract = new Fractal.Builder(WIDTH, HEIGHT).juliaFunction(julia).buildJulia();

                        name = "Julia_" + day + "_" + hour + "_" + minute;
                        File outputFile = new File(name);
                        // Let's create the image to PNG
                        ImageIO.write(SwingFXUtils.fromFXImage(fract, null), "png", outputFile);
                        System.out.println("An image was created: " + outputFile.getName());
                        // Add the file descriptor
                        File description = createTextFile(outputFile.getName());
                        addDescription(description, x, y, "Julia");
                    }
                } else {
                    // Not an option
                    formatter.printHelp(AP_STRING, options, true);
                    throw new IllegalArgumentException();
                }

            }

        }
    }

}
