//Aadarsha Shrestha
//This program creates a JavaFX UI that tracks user interactions and allows undoing actions using a stack.


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

class CustomStack<T> {
    private static class Node<T> {
        T data;
        Node<T> next;
        Node(T data, Node<T> next) {
            this.data = data;
            this.next = next;
        }
    }
    
    private Node<T> top;
    private int size = 0;
    
    public void push(T data) {
        top = new Node<>(data, top);
        size++;
    }
    
    public T pop() {
        if (top == null) return null;
        T data = top.data;
        top = top.next;
        size--;
        return data;
    }
    
    public boolean isEmpty() {
        return top == null;
    }
}

public class StackUndoUI extends Application {
    private CustomStack<State> undoStack = new CustomStack<>();
    private CustomStack<State> redoStack = new CustomStack<>();
    
    private RadioButton radio1, radio2, radio3;
    private CheckBox check1, check2, check3;
    private Slider slider;
    private ComboBox<String> dropdown;
    private Button goButton, undoButton, redoButton;
    
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
        redoButton = new Button("Redo");

        radio1.setOnAction(e -> recordState());
        radio2.setOnAction(e -> recordState());
        radio3.setOnAction(e -> recordState());
        check1.setOnAction(e -> recordState());
        check2.setOnAction(e -> recordState());
        check3.setOnAction(e -> recordState());
        slider.valueProperty().addListener((obs, oldVal, newVal) -> recordState());
        dropdown.setOnAction(e -> recordState());
        
        goButton.setOnAction(e -> showState());
        undoButton.setOnAction(e -> undoAction());
        redoButton.setOnAction(e -> redoAction());
        
        VBox layout = new VBox(10, radio1, radio2, radio3, check1, check2, check3, slider, dropdown, goButton, undoButton, redoButton);
        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Enhanced Undo Example");
        primaryStage.show();
    }

    private void recordState() {
        undoStack.push(new State(radio1.isSelected(), radio2.isSelected(), radio3.isSelected(),
                                 check1.isSelected(), check2.isSelected(), check3.isSelected(),
                                 (int) slider.getValue(), dropdown.getValue()));
        redoStack = new CustomStack<>(); // Clear redo stack when a new action occurs
    }

    private void undoAction() {
        if (!undoStack.isEmpty()) {
            redoStack.push(getCurrentState());
            restoreState(undoStack.pop());
        }
    }
    
    private void redoAction() {
        if (!redoStack.isEmpty()) {
            undoStack.push(getCurrentState());
            restoreState(redoStack.pop());
        }
    }

    private State getCurrentState() {
        return new State(radio1.isSelected(), radio2.isSelected(), radio3.isSelected(),
                         check1.isSelected(), check2.isSelected(), check3.isSelected(),
                         (int) slider.getValue(), dropdown.getValue());
    }

    private void restoreState(State state) {
        radio1.setSelected(state.radio1);
        radio2.setSelected(state.radio2);
        radio3.setSelected(state.radio3);
        check1.setSelected(state.check1);
        check2.setSelected(state.check2);
        check3.setSelected(state.check3);
        slider.setValue(state.sliderValue);
        dropdown.setValue(state.dropdownValue);
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

    private static class State {
        boolean radio1, radio2, radio3;
        boolean check1, check2, check3;
        int sliderValue;
        String dropdownValue;

        State(boolean r1, boolean r2, boolean r3, boolean c1, boolean c2, boolean c3, int slider, String dropdown) {
            this.radio1 = r1;
            this.radio2 = r2;
            this.radio3 = r3;
            this.check1 = c1;
            this.check2 = c2;
            this.check3 = c3;
            this.sliderValue = slider;
            this.dropdownValue = dropdown;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}