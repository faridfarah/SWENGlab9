package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class PrimaryController {

    @FXML
    private ComboBox<String> baseSelector;

    @FXML
    private TextField result;

    @FXML
    private Button A, B, C, D, E, F, two, three, four, five, six, seven, eight, nine;
    @FXML
    private Button and, or, xor, not;

    private String currentInput = "";
    private int base = 10; // Default base is decimal

    @FXML
    public void initialize() {
        baseSelector.getItems().addAll("Binary", "Octal", "Decimal", "Hexadecimal");
        baseSelector.setValue("Decimal");
        baseSelector.setOnAction(e -> updateBase());
        updateButtonStates(); // Set the initial state of the buttons
    }

    private void updateBase() {
        String selectedBase = baseSelector.getValue();
        int fromBase = base;
        switch (selectedBase) {
            case "Binary":
                base = 2;
                break;
            case "Octal":
                base = 8;
                break;
            case "Decimal":
                base = 10;
                break;
            case "Hexadecimal":
                base = 16;
                break;
        }
        updateButtonStates();
        //converting input to the current base (unless its an expression)
        if(currentInput!=null && currentInput.matches("-?[0-9A-F]+")) {
            currentInput = convertToDecimal(currentInput, fromBase);
            currentInput =  convertFromDecimal(currentInput, base);
            result.setText(currentInput);
        }

    }

    private void updateButtonStates() {
        // Enable/disable buttons based on the current base
        A.setDisable(base < 16);
        B.setDisable(base < 16);
        C.setDisable(base < 16);
        D.setDisable(base < 16);
        E.setDisable(base < 16);
        F.setDisable(base < 16);

        nine.setDisable(base < 10);
        eight.setDisable(base < 10);
        seven.setDisable(base < 8);
        six.setDisable(base < 8);
        five.setDisable(base < 8);
        four.setDisable(base < 8);
        three.setDisable(base < 8);
        two.setDisable(base < 8);

        // Logical and XOR operations are only available in binary mode
        and.setDisable(base != 2);
        or.setDisable(base != 2);
        xor.setDisable(base != 2); // XOR operation
        not.setDisable(base != 2);
    }

    private String convertToDecimal(String value, int fromBase) {
        try {
            int decimalValue = Integer.parseInt(value, fromBase);
            if(fromBase == 2 && value.charAt(0)== '1'){
                decimalValue -= 256;  // Adjust for 8-bit 2's complement
            }
            return String.valueOf(decimalValue);
        } catch (NumberFormatException e) {
            return "Error";
        }
    }

    private String convertFromDecimal(String value, int toBase) {
        try {
            int decimalValue = Integer.parseInt(value);
            String newValue = Integer.toString(decimalValue, toBase).toUpperCase();
            if(toBase == 2){
               newValue =  String.format("%8s", Integer.toBinaryString(decimalValue & 0xFF)).replace(' ', '0');

            }
            return newValue;
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid conversion.");
        }
    }


    @FXML
    void clear(ActionEvent event) {
        currentInput = "";
        result.clear();
    }

    @FXML
    void handleNumber(ActionEvent event) {
        Button button = (Button) event.getSource();
        currentInput += button.getText();  // Append the number to the current expression
        result.setText(currentInput);  // Show the current input in the result field
    }


    @FXML
    void handleOperator(ActionEvent event) {
            Button button = (Button) event.getSource();
            currentInput += button.getText() ;  // Append the operator with spaces for separation
            result.setText(currentInput);  // Show the current expression in the result field
    }


    @FXML
    void calculate(ActionEvent event) {
        if (!currentInput.isEmpty()) {
            try {
                // Validate the expression
                validateExpression(currentInput, base);

                // Convert the expression into a result
                int resultValue = evaluateExpression(currentInput, base);

                // Convert result to the selected base for display
                currentInput = Integer.toString(resultValue, base).toUpperCase();
                if(base == 2){
                    currentInput = String.format("%8s", Integer.toBinaryString(resultValue & 0xFF)).replace(' ', '0');
                }
                result.setText(currentInput);  // Show the result

            } catch (IllegalArgumentException e) {
                // Show the error message on the screen
                result.setText("Error: " + e.getMessage());
            }
        } else {
            result.setText("Error: Empty expression");
        }
    }


    private static void validateExpression(String expression, int base) {
        String validChars = getValidChars(base);

        // Check for invalid characters
        for (char c : expression.toCharArray()) {
            if (!Character.isWhitespace(c) && validChars.indexOf(c) == -1 && "+-*/|&^~()".indexOf(c) == -1) {
                throw new IllegalArgumentException("invalid expression");
            }
        }

        // Allow ~ after |, &, ^
        if (expression.matches(".*[+\\-*/&|^]{2,}.*") && !expression.matches(".*[&|^]~.*")) {
            throw new IllegalArgumentException("invalid expression");
        }

        // Check for division by zero (allow spaces between / and 0)
        if (expression.matches(".*/ *0.*")) {
            throw new IllegalArgumentException("trying to divide by 0 (evaluated: \"0\")");
        }

        // Base-specific validation for binary (base 2)
        if (base == 2) {
            String[] parts = expression.split("[^01]+"); // Split by non-binary characters
            for (String part : parts) {
                if (!part.isEmpty() && part.length() != 8) {
                    throw new IllegalArgumentException("invalid expression");
                }
            }

            // Check for mixed logical and arithmetic operators
            boolean hasLogical = expression.matches(".*[&|^~].*");
            boolean hasArithmetic = expression.matches(".*[+\\-*/].*");

            if (hasLogical && hasArithmetic) {
                throw new IllegalArgumentException("invalid expression");
            }
        }
    }

    private static String getValidChars(int base) {
        if (base == 2) return "01";
        if (base == 8) return "01234567";
        if (base == 10) return "0123456789";
        if (base == 16) return "0123456789ABCDEF"; // Only uppercase is valid
        return "";
    }

    private static int evaluateExpression(String expression, int base) {
        String normalizedExpression = normalizeExpression(expression, base);
        return (int) evaluateNormalizedExpression(normalizedExpression, base);
    }

    private static String normalizeExpression(String expression, int base) {
        StringBuilder result = new StringBuilder();
        StringBuilder numberBuffer = new StringBuilder();

        for (char c : expression.toCharArray()) {
            if (getValidChars(base).indexOf(c) != -1) {
                numberBuffer.append(c); // Collect numbers
            } else {
                if (numberBuffer.length() > 0) {
                    int value = Integer.parseInt(numberBuffer.toString(), base);
                    result.append(value); // Convert number to base 10
                    numberBuffer.setLength(0);
                }
                result.append(c); // Append operators or other characters
            }
        }

        if (numberBuffer.length() > 0) {
            int value = Integer.parseInt(numberBuffer.toString(), base);
            result.append(value); // Handle remaining numbers
        }

        return result.toString();
    }

    private static double evaluateNormalizedExpression(String expression, int base) {
        int pos = 0;
        int length = expression.length();
        java.util.Stack<Long> values = new java.util.Stack<>();
        java.util.Stack<Character> operators = new java.util.Stack<>();

        while (pos <= length) {
            char ch = (pos < length) ? expression.charAt(pos) : '\0';

            // Handle whitespace
            if (Character.isWhitespace(ch)) {
                pos++;
                continue;
            }

            // Handle negative numbers
            if (ch == '-' && (pos == 0 || "+-*/&|^~(".indexOf(expression.charAt(pos - 1)) != -1)) {
                StringBuilder number = new StringBuilder();
                number.append(ch);
                pos++;
                while (pos < length && Character.isDigit(expression.charAt(pos))) {
                    number.append(expression.charAt(pos++));
                }
                values.push(Long.parseLong(number.toString()));
                continue;
            }

            // Handle regular numbers
            if (Character.isDigit(ch)) {
                StringBuilder number = new StringBuilder();
                while (pos < length && Character.isDigit(expression.charAt(pos))) {
                    number.append(expression.charAt(pos++));
                }
                values.push(Long.parseLong(number.toString()));
                continue;
            }

            // Handle opening parentheses
            if (ch == '(') {
                operators.push(ch);
            }
            // Handle closing parentheses
            else if (ch == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    evaluateTop(values, operators);
                }
                if (!operators.isEmpty() && operators.peek() == '(') {
                    operators.pop();
                } else {
                    throw new IllegalArgumentException("invalid expression");
                }
            }
            // Handle operators
            else if (isOperator(ch)) {
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(ch)) {
                    evaluateTop(values, operators);
                }
                operators.push(ch);
            }
            // End of expression
            else if (ch == '\0') {
                break;
            } else {
                throw new IllegalArgumentException("invalid expression");
            }

            pos++;
        }

        while (!operators.isEmpty()) {
            evaluateTop(values, operators);
        }

        if (values.size() != 1) {
            throw new IllegalArgumentException("invalid expression");
        }

        long result = values.pop();

        // Handle 8-bit two's complement for base 2
        if (base == 2) {
            result = result & 0xFF; // Keep only 8 bits
            if ((result & 0x80) != 0) { // Check MSB for signed adjustment
                result = result - 256;
            }
        }

        return result;
    }

    private static boolean isOperator(char ch) {
        return "+-*/&|^~".indexOf(ch) != -1;
    }

    private static int precedence(char operator) {
        if (operator == '~') return 4;
        if (operator == '*' || operator == '/') return 3;
        if (operator == '+' || operator == '-') return 2;
        if (operator == '&') return 1;
        if (operator == '^') return 1;
        if (operator == '|') return 1;
        return 0;
    }

    private static void evaluateTop(java.util.Stack<Long> values, java.util.Stack<Character> operators) {
        if (values.isEmpty() || operators.isEmpty()) {
            throw new IllegalArgumentException("invalid expression");
        }

        char operator = operators.pop();

        if (operator == '~') {
            if (values.isEmpty()) {
                throw new IllegalArgumentException("invalid expression");
            }
            long value = values.pop();
            values.push(~value);
        } else {
            if (values.size() < 2) {
                throw new IllegalArgumentException("invalid expression");
            }
            long b = values.pop();
            long a = values.pop();

            switch (operator) {
                case '+': values.push(a + b); break;
                case '-': values.push(a - b); break;
                case '*': values.push(a * b); break;
                case '/':
                    if (b == 0) throw new ArithmeticException("trying to divide by 0 (evaluated: \"0\")");
                    values.push(a / b);
                    break;
                case '&': values.push(a & b); break;
                case '|': values.push(a | b); break;
                case '^': values.push(a ^ b); break;
                default: throw new IllegalArgumentException("invalid expression");
            }
        }
    }




}
