package org.fractaly;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
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

    private static void saveToFile(Image image, String name) throws IOException{
        File outputFile = new File(name);
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", outputFile);
    }

    public void zoomIn(Fractal fract) {
        Instant before = Instant.now();
        zoomFactor += 0.1;
        Fractal.Builder newBuilder = new Fractal.Builder(fract).zoom(zoomFactor);
        Function<Complex, Complex> julia = c -> c.multiply(c).add(Complex.build(0, -0.8)); // Fonction Julia
        BiFunction<Integer, Integer, Color> color = (i, maxI) -> Color.hsb((i / (float) maxI) * 360, 0.7, 0.7);
        Builder build =  new Fractal.Builder(WIDTH, HEIGHT).colorFunction(color).juliaFunction(julia).zoom(zoomFactor);
        Fractal f = null;
        if(fract.isMandelbrot()){
            f = build.buildMandelbrot();
        }else{

            f = newBuilder.buildJulia();
        }
        v.setImage(f);
        Duration d = Duration.between(before, Instant.now());
        System.out.println(d.toString());
    }

    public void zoomOut(Fractal fract) {
        Instant before = Instant.now();
        zoomFactor -= 0.1;
        Fractal.Builder newBuilder = new Fractal.Builder(fract).zoom(zoomFactor);
        Function<Complex, Complex> julia = c -> c.multiply(c).add(Complex.build(0, -0.8)); // Fonction Julia
        BiFunction<Integer, Integer, Color> color = (i, maxI) -> Color.hsb((i / (float) maxI) * 360, 0.7, 0.7);
        Builder build = new Fractal.Builder(WIDTH, HEIGHT).colorFunction(color).juliaFunction(julia).zoom(zoomFactor);
        Fractal f = null;
        if (fract.isMandelbrot()) {
            f = build.buildMandelbrot();
        } else {
            f = newBuilder.buildJulia();
        }
        v.setImage(f);
        Duration d = Duration.between(before, Instant.now());
        System.out.println(d.toString());
    }

    private void zoom100(double x, double y, ImageView imageView) {
        double oldHeight = imageView.getBoundsInLocal().getHeight();
        double oldWidth = imageView.getBoundsInLocal().getWidth();

        boolean heightLarger = oldHeight > oldWidth;
        imageView.setFitHeight(-1);
        imageView.setFitWidth(-1);
        // calculate scale factor
        double scale = 1;
        if (heightLarger) {
            scale = imageView.getBoundsInLocal().getHeight() / oldHeight;
        } else {
            scale = imageView.getBoundsInLocal().getWidth() / oldWidth;
        }

        double centery = root.getLayoutBounds().getHeight() / 2;
        double centerx = root.getLayoutBounds().getWidth() / 2;

        double xOffset = scale * (centerx - x);
        double yOffset = scale * (centery - y);
        imageView.setTranslateX(xOffset);
        imageView.setTranslateY(yOffset);

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
            case "j":
                julia = c -> c.multiply(c).add(Complex.build(getX, getY));
                f = build.juliaFunction(julia).buildJulia();
                v = new ImageView(f);
                final Fractal fract = f;
                v.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        MouseButton gEvent = mouseEvent.getButton();
                        switch(gEvent){
                            default: case PRIMARY:
                                zoomIn(fract);
                                break;
                            case SECONDARY:
                                zoomOut(fract);
                                break;
                            case MIDDLE:
                                double width = v.getBoundsInLocal().getWidth();
                                double centery = 1000 / 2;
                                double centerx =  1000 / 2;

                                double scale = v.getBoundsInLocal().getHeight() / width;
                                double xOffset = scale * (centerx - mouseEvent.getX());
                                double yOffset = scale * (centery - mouseEvent.getY());

                                v.setTranslateX(xOffset);
                                v.setTranslateY(yOffset);
                                break
                            ;
                        }
                    }
                });
                break;
            default: case "m":
                f = build.buildMandelbrot();
                v = new ImageView(f);
                final Fractal fractal = f;
                v.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        MouseButton gEvent = mouseEvent.getButton();
                        switch (gEvent) {
                            default:
                            case PRIMARY:
                                zoomIn(fractal);
                                break;
                            case SECONDARY:
                                zoomOut(fractal);
                                break;
                            case MIDDLE:
                                double width = v.getBoundsInLocal().getWidth();
                                double centery = 1000 / 2;
                                double centerx = 1000 / 2;

                                double scale = v.getBoundsInLocal().getHeight() / width;
                                double xOffset = scale * (centerx - mouseEvent.getX());
                                double yOffset = scale * (centery - mouseEvent.getY());

                                v.setTranslateX(xOffset);
                                v.setTranslateY(yOffset);
                                break;
                        }
                    }
                });
                break;
        }

        if(getFunction.equals("j")){
            getFunction = "Julia";
        }else{
            getFunction = "MandelBrot";
        }
        
        VBox root = new VBox();
        var stackPane = new StackPane(v);

        Scene scene = new Scene(root, 1000, 1000);
        Button button = new Button("Save Image");

        final Fractal fra = f;
        Calendar now = Calendar.getInstance();
        int day = now.get(Calendar.DAY_OF_MONTH);
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        final String name = getFunction+"_"+day+"_"+hour+"_"+minute;

        button.setOnAction(e -> {
            try {
                saveToFile(fra,name);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        button.setVisible(true);
        
        stackPane.getChildren().add(button);
        StackPane.setAlignment(button, Pos.BOTTOM_CENTER);
        root.getChildren().add(stackPane);

        stage.setTitle("Image created with: "+getFunction);
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
            getFunction = fun.getSelected();

            try (Scanner sc = new Scanner(System.in)) {
                System.out.println("Please enter X:");
                String xa = sc.nextLine();
                final Double x = Double.parseDouble(xa);
                System.out.println("Please enter Y:");
                String ya = sc.nextLine();
                final double y = Double.parseDouble(ya);
                
                System.out.println("USAGE:");
                System.out.println("[ZOOM]:  Click on Primary Mouse");
                System.out.println("[UNZOOM]: Click on Second Mouse");
                System.out.println("[MOVE]: Click on Middle Mouse");

                Thread.sleep(3000);
                Application.launch(App.class,
                    "--function=" + getFunction,
                    "--x="+x,
                    "--y="+y
                );
            }
        }
        if(cmd.hasOption("t")){    
            System.out.println("Welcome on the terminal!");

            if(cmd.hasOption("m")){
                fract = new Fractal.Builder(WIDTH, HEIGHT).buildMandelbrot();
                File outputFile = new File("mandelbrot");
                ImageIO.write(SwingFXUtils.fromFXImage(fract, null), "png", outputFile);
                System.out.println("An image was created: " + outputFile.getName());
            }else{ 
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

                        File outputFile = new File("julia");
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
