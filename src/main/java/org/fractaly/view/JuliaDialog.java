package org.fractaly.view;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class JuliaDialog {
    // Hide the implicit constructor
    private JuliaDialog(){}

    public static Dialog<Pair<Double, Double>> getInstance() {
        Dialog<Pair<Double, Double>> dialog = new Dialog<>();
        dialog.setTitle("Julia Fractal");
        dialog.setHeaderText("Choose your custom Julia fractal !");
        ButtonType confirm = new ButtonType("Compute", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirm, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField real = new TextField();
        TextField imaginary = new TextField();

        grid.add(new Label("Real part:"), 0, 0);
        grid.add(real, 1, 0);
        grid.add(new Label("Imaginary part:"), 0, 1);
        grid.add(imaginary, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirm) {
                try {
                    Double re = Double.parseDouble(real.getText());
                    Double im = Double.parseDouble(imaginary.getText());
                    return new Pair<>(re, im);
                } catch (Exception e) {
                    dialog.setHeaderText("Please enter correct numbers.");
                }
            }
            return null;
        });
        return dialog;
    }
}
