module com.calculator.advancedcalculator {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.calculator.advancedcalculator to javafx.fxml;
    exports com.calculator.advancedcalculator;
}