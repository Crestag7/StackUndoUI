//Aadarsha Shrestha
//This program creates a JavaFX UI that tracks user interactions and allows undoing actions using a stack.

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.Stack;

public class StackUndoUI extends Application {
    private Stack<String> actionStack = new Stack<>();
    private RadioButton radio1, radio2, radio3;
    private CheckBox check1, check2, check3;
    private Slider slider;
    private ComboBox<String> dropdown;
    private Button goButton, undoButton;

    @Override
    public void start(Stage primaryStage) {
        radio1 = new RadioButton("Option 1");
        radio2 = new RadioButton("Option 2");
        radio3 = new RadioButton("Option 3");
        ToggleGroup radioGroup = new ToggleGroup();
        radio1.setToggleGroup(radioGroup);
        radio2.setToggleGroup(radioGroup);
        radio3.setToggleGroup(radioGroup);

        check1 = new CheckBox("Check 1");
        check2 = new CheckBox("Check 2");
        check3 = new CheckBox("Check 3");

        slider = new Slider(0, 100, 50);
        dropdown = new ComboBox<>();
        dropdown.getItems().addAll("Item 1", "Item 2", "Item 3", "Item 4", "Item 5");
        dropdown.setValue("Item 1");

        goButton = new Button("Go");
        undoButton = new Button("Undo");

        radio1.setOnAction(e -> recordAction("Radio", "1"));
        radio2.setOnAction(e -> recordAction("Radio", "2"));
        radio3.setOnAction(e -> recordAction("Radio", "3"));
        check1.setOnAction(e -> recordAction("Check1", String.valueOf(check1.isSelected())));
        check2.setOnAction(e -> recordAction("Check2", String.valueOf(check2.isSelected())));
        check3.setOnAction(e -> recordAction("Check3", String.valueOf(check3.isSelected())));
        slider.valueProperty().addListener((obs, oldVal, newVal) -> recordAction("Slider", String.valueOf(newVal.intValue())));
        dropdown.setOnAction(e -> recordAction("Dropdown", dropdown.getValue()));

        goButton.setOnAction(e -> showState());
        undoButton.setOnAction(e -> undoAction());

        VBox layout = new VBox(10, radio1, radio2, radio3, check1, check2, check3, slider, dropdown, goButton, undoButton);
        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simple Undo Example");
        primaryStage.show();
    }

    private void recordAction(String type, String value) {
        actionStack.push(type + ":" + value);
    }

    private void undoAction() {
        if (!actionStack.isEmpty()) {
            String lastAction = actionStack.pop();
            String[] parts = lastAction.split(":");
            String type = parts[0];
            String value = parts[1];

            switch (type) {
                case "Radio":
                    radio1.setSelected(false);
                    radio2.setSelected(false);
                    radio3.setSelected(false);
                    break;
                case "Check1":
                    check1.setSelected(!Boolean.parseBoolean(value));
                    break;
                case "Check2":
                    check2.setSelected(!Boolean.parseBoolean(value));
                    break;
                case "Check3":
                    check3.setSelected(!Boolean.parseBoolean(value));
                    break;
                case "Slider":
                    slider.setValue(Double.parseDouble(value));
                    break;
                case "Dropdown":
                    dropdown.setValue("Item 1");
                    break;
            }
        }
    }

    private void showState() {
        String state = "Radio: " + (radio1.isSelected() ? "1" : radio2.isSelected() ? "2" : radio3.isSelected() ? "3" : "None") + "\n" +
                "Check1: " + check1.isSelected() + "\n" +
                "Check2: " + check2.isSelected() + "\n" +
                "Check3: " + check3.isSelected() + "\n" +
                "Slider: " + (int) slider.getValue() + "\n" +
                "Dropdown: " + dropdown.getValue();
        Alert alert = new Alert(Alert.AlertType.INFORMATION, state);
        alert.setTitle("Current State");
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
