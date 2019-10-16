package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class Controller {

    @FXML
    private Label result;

    private StringBuilder output = new StringBuilder();
    private Float number1 = null;
    private Float number2 = null;
    private Character operator = null;

    public void processNumber(ActionEvent actionEvent) {
        String button = ((Button) actionEvent.getSource()).getText();
        if (button.equals("0") && result.getText().equals("0")) {
            return;
        }
        output.append(button);
        result.setText(output.toString());
    }

    public void processOperator(ActionEvent actionEvent) {
        String button = ((Button) actionEvent.getSource()).getText();
        switch (button) {
            case "AC":
                output.delete(0, output.length());
                result.setText("0");
                number1 = null;
                number2 = null;
                operator = null;
                break;
            case "±":
                if (output.toString().isEmpty())
                    return;
                if (output.charAt(0) == '-') {
                    String temp = output.toString();
                    output = new StringBuilder(temp.substring(1));
                    result.setText(output.toString());
                    return;
                }
                output.insert(0, "-");
                result.setText(output.toString());
                break;
            case ".":
                if (output.toString().contains(".")) {
                    return;
                }
                if (output.toString().isEmpty()) {
                    output.append("0.");
                    result.setText(output.toString());
                    return;
                }

                output.append(".");
                result.setText(output.toString());
                break;
            case "%":
                if (number1 == null)
                    number1 = Float.valueOf(output.toString());
                number1 = calculate(number1);
                operator = button.charAt(0);
                output.delete(0, output.length());
                result.setText(number1.toString());
            default:
                process(button);
                break;
        }

    }

    private void process(String button) {

        if (button.charAt(0) == '=') {
            if (number2 == null) {
                if (operator == '%')
                    return;
                number2 = Float.valueOf(output.toString());
            }
            if (operator != null && number1 != null) {
                number1 = calculate(number1, number2, operator);
                number2 = null;
                operator = null;
                output.delete(0, output.length());
                if (number1.toString().substring(number1.toString().indexOf(".") + 1).equals("0")) {
                    result.setText(number1.toString().substring(0, number1.toString().indexOf(".")));
                    return;
                }
                result.setText(number1.toString());
            }
            return;
        }

        if (number1 == null) {
            number1 = Float.valueOf(output.toString());
            output.delete(0, output.length());
            operator = button.charAt(0);
            return;
        } else if (number2 == null && !output.toString().equals("")) {
            number2 = Float.valueOf(output.toString());
            output.delete(0, output.length());
        } else {
            operator = button.charAt(0);
            return;
        }

        number1 = calculate(number1, number2, operator);
        number2 = null;
        operator = button.charAt(0);

        if (number1 == null) {
            output.delete(0, output.length());
            result.setText("Error");
        } else {
            output.delete(0, output.length());
            if (number1.toString().substring(number1.toString().indexOf(".") + 1).equals("0")) {
                result.setText(number1.toString().substring(0, number1.toString().indexOf(".")));
                return;
            }
            result.setText(number1.toString());
        }

    }

    private Float calculate(Float num1, Float num2, Character operator) {

        switch (operator) {
            case '+':
                return num1 + num2;
            case '×':
                return num1 * num2;
            case '–':
                return num1 - num2;
            case '÷':
                if (num2 == 0.0 || num2 == 0)
                    return null;
                return num1 / num2;
        }

        return null;
    }

    private Float calculate(Float num1) {

        return num1 / 100;
    }
}
