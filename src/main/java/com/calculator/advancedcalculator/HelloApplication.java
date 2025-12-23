package com.calculator.advancedcalculator;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    private TextField display;
    private ObservableList<String> historyData = FXCollections.observableArrayList();
    private double num1 = 0;
    private String operator = "";
    private boolean start = true;
    private boolean isDark = true;

    @Override
    public void start(Stage primaryStage) {
        // 1. Display Screen (Sleek Header Style)
        display = new TextField();
        display.setEditable(false);
        display.setAlignment(Pos.CENTER_RIGHT);
        display.setPrefHeight(90);
        display.setStyle("-fx-font-size: 38px; -fx-background-color: #2d3436; -fx-text-fill: #dfe6e9; -fx-background-radius: 15; -fx-padding: 15;");

        // 2. Main Calculator Grid (Refined Spacing)
        GridPane grid = new GridPane();
        grid.setHgap(12); grid.setVgap(12);
        grid.setPadding(new Insets(15, 0, 0, 0));
        grid.setAlignment(Pos.CENTER);

        String[] standardButtons = {"7", "8", "9", "/", "4", "5", "6", "*", "1", "2", "3", "-", "C", "0", "=", "+"};
        int r = 0, c = 0;
        for (String text : standardButtons) {
            grid.add(createStyledButton(text, false), c, r);
            c++; if (c > 3) { c = 0; r++; }
        }

        String[] sciButtons = {"√", "x²", "sin", "cos"};
        for (int i = 0; i < sciButtons.length; i++) {
            grid.add(createStyledButton(sciButtons[i], true), 4, i);
        }

        // 3. Left Container (Header + Display + Grid)
        Button themeBtn = new Button("🌓 Toggle Theme");
        themeBtn.setStyle("-fx-background-color: #636e72; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 8 20; -fx-cursor: hand;");

        HBox topBar = new HBox(themeBtn);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(0, 0, 15, 0));

        VBox calcPanel = new VBox(0, topBar, display, grid);
        calcPanel.setPadding(new Insets(20));
        calcPanel.setStyle("-fx-background-color: #1e272e; -fx-background-radius: 20;");

        // 4. History Panel (Styled to Match)
        ListView<String> historyList = new ListView<>(historyData);
        historyList.setPrefWidth(220);
        historyList.setStyle("-fx-background-color: transparent; -fx-control-inner-background: #2d3436; -fx-text-fill: white; -fx-background-radius: 10;");

        Label historyLabel = new Label("CALCULATION HISTORY");
        historyLabel.setStyle("-fx-text-fill: #95a5a6; -fx-font-weight: bold; -fx-font-size: 12px;");

        Button clearBtn = new Button("Clear History");
        clearBtn.setMaxWidth(Double.MAX_VALUE);
        clearBtn.setStyle("-fx-background-color: #ff5e57; -fx-text-fill: white; -fx-background-radius: 10; -fx-padding: 10;");
        clearBtn.setOnAction(e -> historyData.clear());

        VBox historyPanel = new VBox(15, historyLabel, historyList, clearBtn);
        historyPanel.setPadding(new Insets(20));
        historyPanel.setPrefWidth(260);
        historyPanel.setStyle("-fx-background-color: #2d3436; -fx-background-radius: 20;");

        // 5. Final Root Layout (Using Spacing for Organization)
        HBox root = new HBox(20, calcPanel, historyPanel);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: #0d1117;"); // Deep space background

        themeBtn.setOnAction(e -> toggleTheme(root, calcPanel, historyPanel));

        Scene scene = new Scene(root, 840, 620);
        scene.setOnKeyPressed(this::handleKeyboardInput);

        primaryStage.setTitle("Advanced Standalone Scientific Calculator");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void toggleTheme(HBox root, VBox calcPanel, VBox historyPanel) {
        isDark = !isDark;
        if (isDark) {
            root.setStyle("-fx-background-color: #0d1117;");
            calcPanel.setStyle("-fx-background-color: #1e272e; -fx-background-radius: 20;");
            historyPanel.setStyle("-fx-background-color: #2d3436; -fx-background-radius: 20;");
            display.setStyle("-fx-font-size: 38px; -fx-background-color: #2d3436; -fx-text-fill: #dfe6e9; -fx-background-radius: 15;");
        } else {
            root.setStyle("-fx-background-color: #f0f2f5;");
            calcPanel.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");
            historyPanel.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 20;");
            display.setStyle("-fx-font-size: 38px; -fx-background-color: #f1f2f6; -fx-text-fill: #2d3436; -fx-background-radius: 15; -fx-border-color: #dfe6e9; -fx-border-radius: 15;");
        }
    }

    private Button createStyledButton(String text, boolean isSci) {
        Button btn = new Button(text);
        btn.setPrefSize(75, 75);
        String style = "-fx-font-size: 20px; -fx-background-radius: 18; -fx-font-weight: bold; -fx-cursor: hand;";

        if (isSci) style += "-fx-background-color: #4b6584; -fx-text-fill: white;";
        else if (text.matches("[0-9]")) style += "-fx-background-color: #485460; -fx-text-fill: white;";
        else if (text.equals("=")) style += "-fx-background-color: #05c46b; -fx-text-fill: white;";
        else if (text.equals("C")) style += "-fx-background-color: #ff5e57; -fx-text-fill: white;";
        else style += "-fx-background-color: #ffa801; -fx-text-fill: white;";

        btn.setStyle(style);
        btn.setOnAction(e -> handleInput(text));
        return btn;
    }

    // ... (Keep handleInput, handleKeyboardInput, and calculate methods from previous version)
    private void handleInput(String value) {
        if (value.matches("[0-9]")) {
            if (start) { display.setText(""); start = false; }
            display.appendText(value);
        } else if (value.equals("C")) {
            display.setText(""); num1 = 0; operator = ""; start = true;
        } else if (value.equals("√") || value.equals("x²") || value.equals("sin") || value.equals("cos")) {
            double val = Double.parseDouble(display.getText());
            double res = switch (value) {
                case "√" -> Math.sqrt(val);
                case "x²" -> Math.pow(val, 2);
                case "sin" -> Math.sin(Math.toRadians(val));
                case "cos" -> Math.cos(Math.toRadians(val));
                default -> 0;
            };
            display.setText(String.valueOf(res));
            historyData.add(0, value + "(" + val + ") = " + res);
            start = true;
        } else if (value.equals("=")) {
            if (operator.isEmpty()) return;
            double num2 = Double.parseDouble(display.getText());
            double result = calculate(num1, num2, operator);
            historyData.add(0, num1 + " " + operator + " " + num2 + " = " + result);
            display.setText(String.valueOf(result));
            operator = ""; start = true;
        } else {
            num1 = Double.parseDouble(display.getText());
            operator = value;
            start = true;
        }
    }

    private void handleKeyboardInput(KeyEvent event) {
        String code = event.getText();
        if (code.matches("[0-9/*\\-+=]")) handleInput(code);
        else if (event.getCode() == javafx.scene.input.KeyCode.ENTER) handleInput("=");
        else if (event.getCode() == javafx.scene.input.KeyCode.BACK_SPACE) handleInput("C");
    }

    private double calculate(double n1, double n2, String op) {
        return switch (op) {
            case "+" -> n1 + n2;
            case "-" -> n1 - n2;
            case "*" -> n1 * n2;
            case "/" -> n2 == 0 ? 0 : n1 / n2;
            default -> 0;
        };
    }

    public static void main(String[] args) { launch(args); }
}