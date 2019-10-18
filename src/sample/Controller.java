package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class Controller {

    @FXML
    private Label result;

    private StringBuilder output = new StringBuilder();
    private String num1 = "";
    private String num2 = "";
    private Character operator = null;

    public void processNumber(ActionEvent actionEvent) {
        String button = ((Button) actionEvent.getSource()).getText();
        if (button.equals("0") && result.getText().equals("0")) {
            return;
        }
        if (output.toString().length() > 12)
            return;
        output.append(button);
        result.setText(output.toString());
        resize(output.toString().length());
    }

    public void processOperator(ActionEvent actionEvent) {
        String button = ((Button) actionEvent.getSource()).getText();
        switch (button) {
            case "AC":
                num1 = "";
                num2 = "";
                output = new StringBuilder();
                operator = null;
                resize(0);
                result.setText("0");
                break;
            case "±":
                if (output.toString().isEmpty())
                    return;
                if (output.charAt(0) == '-') {
                    output = new StringBuilder(output.substring(1));
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
                if (num1.equals("")) {
                    num1 = calculatePercent(output.toString());
                    output = new StringBuilder();
                    result.setText(num1);
                    resize(num1.length());
                } else if (operator != null) {
                    output = new StringBuilder(calculatePercent(output.toString()));
                    result.setText(output.toString());
                    resize(output.length());
                }
                break;
            default:
                process(button.charAt(0));
                break;
        }

    }

    private void process(Character operator) {

        String answer;

        if (this.operator == null) {
            num1 = output.toString();
            this.operator = operator;
            output = new StringBuilder();
        } else if (!output.toString().isEmpty()) {
            num2 = output.toString();
            answer = calculate(num1, num2, this.operator);
            num1 = answer;
            num2 = "";
            output = new StringBuilder();
            if (operator != '=') {
                this.operator = operator;
            }

            if (answer == null) {
                result.setText("Error");
            } else if (answer.substring(answer.indexOf(".") + 1).equals("0")) {
                result.setText(answer.substring(0, answer.indexOf(".")));
                resize(answer.substring(0, answer.indexOf(".")).length());
            } else {
                result.setText(answer);
                resize(answer.length());
            }
        } else
            this.operator = operator;

    }

    private String calculate(String num1, String num2, Character operator) {

        boolean n1 = num1.contains(".");
        boolean n2 = num2.contains(".");

        switch (operator) {
            case '+':
                if (n1 && n2)
                    return String.valueOf(Float.parseFloat(num1) + Float.parseFloat(num2));
                if (!n1 && !n2)
                    return String.valueOf(Long.parseLong(num1) + Long.parseLong(num2));
                if (!n1)
                    return String.valueOf(Long.parseLong(num1) + Float.parseFloat(num2));

                return String.valueOf(Float.parseFloat(num1) + Long.parseLong(num2));
            case '×':
                if (n1 && n2)
                    return String.valueOf(Float.parseFloat(num1) * Float.parseFloat(num2));
                if (!n1 && !n2)
                    return String.valueOf(Long.parseLong(num1) * Long.parseLong(num2));
                if (!n1)
                    return String.valueOf(Long.parseLong(num1) * Float.parseFloat(num2));

                return String.valueOf(Float.parseFloat(num1) * Long.parseLong(num2));
            case '–':
                if (n1 && n2)
                    return String.valueOf(Float.parseFloat(num1) - Float.parseFloat(num2));
                if (!n1 && !n2)
                    return String.valueOf(Long.parseLong(num1) - Long.parseLong(num2));
                if (!n1)
                    return String.valueOf(Long.parseLong(num1) - Float.parseFloat(num2));

                return String.valueOf(Float.parseFloat(num1) - Long.parseLong(num2));
            case '÷':
                if (num2.equals("0") || num2.equals("0.0"))
                    return null;

                if (n1 && n2)
                    return String.valueOf(Float.parseFloat(num1) / Float.parseFloat(num2));
                if (!n1 && !n2) {
                    long $n1 = Long.parseLong(num1);
                    long $n2 = Long.parseLong(num2);
                    float temp = (float) $n1 / $n2;
                    return String.valueOf(temp);
                }
                if (!n1)
                    return String.valueOf(Long.parseLong(num1) / Float.parseFloat(num2));

                return String.valueOf(Float.parseFloat(num1) / Long.parseLong(num2));
        }

        return null;
    }

    private String calculatePercent(String num1) {

        if (num1.contains("."))
            return String.valueOf(Float.parseFloat(num1) / 100);

        int n = Integer.valueOf(num1);
        float c = (float) n / 100;

        return String.valueOf(c);
    }

    private void resize(int lengthOfOutput) {
        if (lengthOfOutput > 9) {
            if (result.getFont().getSize() == 50)
                result.setFont(Font.font(35));
        } else {
            if (result.getFont().getSize() == 35)
                result.setFont(Font.font(50));
        }
    }

}
