import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Pattern;

public class Calculator {
    private List <String> tokens;
    private List <String> sortedTokens = new ArrayList<>();
    private Stack <Double> answer = new Stack<>();
    private String newLine;
    
    public Calculator() {}
    
    public Calculator(final String problem) {
        this.tokens = new ArrayList<>(Arrays.asList(problem
                                            .replaceAll("\\s+", "")
                                            .split("(?<=[-+*\\%\\/\\(\\)=])|(?=[-+*\\%\\/\\(\\)=])")));
        sortToPostFix(tokens);
        matchToOperator(sortedTokens);
        //getResult();
    }
    
    public final double getResult() {
        return this.answer.pop();
    }

    public final String getNewLine() {
        return this.newLine;
    }

    private final void sortToPostFix(final List<String> tokens) {
        Map<String, Integer> prec = new HashMap<>();
        Stack<String> opStack = new Stack<>();

        prec.put("*", 3);
        prec.put("%", 3);
        prec.put("/", 3);
        prec.put("+", 2);
        prec.put("-", 2);
        prec.put("(", 1);

        for (String s : tokens) {
            if(s.equals("(")) {
                opStack.push(s);
            } else if(s.equals(")")) {
                while (!opStack.peek().equals("(")) {
                    String val = opStack.pop();
                    if(!val.equals("(")) {
                        this.sortedTokens.add(val);
                    }
                }
                opStack.pop();
            } else if(prec.containsKey(s)) {
                if(opStack.isEmpty()) {
                    opStack.push(s);
                } else {
                    if(prec.get(opStack.peek()) >= prec.get(s)) {
                        this.sortedTokens.add(opStack.pop());
                        opStack.push(s);
                    } else {
                        opStack.push(s);
                    }
                }
            } else {
                this.sortedTokens.add(s);
            }
        }
        while(!opStack.isEmpty()) {
            this.sortedTokens.add(opStack.pop());
        }
        //System.out.println(this.sortedTokens.toString());
    }

    private final void matchToOperator(final List<String> sortedTokens) {
        final CalculatorService calculatorService =
        new CalculatorService(new Addition(), new Subtraction(), new Multiplication(), new Division(), new Mode());

        for(String s : sortedTokens) {
            //System.out.println(s);
            // if(PatternType.NUMBER.matcher(s).matches()) {
            //     this.answer.push(Double.parseDouble(s));
            //     //System.out.println(s);
            // } else if(s.equals("+")) {
            //     createOperatorOrder(this.answer.pop(), this.answer.pop(), Operator.ADD);
            // } else if(s.equals("-")) {
            //     createOperatorOrder(this.answer.pop(), this.answer.pop(), Operator.SUBTRACT);
            // } else if(s.equals("*")) {
            //     createOperatorOrder(this.answer.pop(), this.answer.pop(), Operator.MULTIPLY);
            // } else if(s.equals("/")) {
            //     createOperatorOrder(this.answer.pop(), this.answer.pop(), Operator.DIVIDE);
            // } else if(s.equals("%")) {
            //     createOperatorOrder(this.answer.pop(), this.answer.pop(), Operator.MOD);
            // } else {
            //     throw new IllegalStateException();
            // }

            if(PatternType.NUMBER.matcher(s).matches()) {
                this.answer.push(Double.parseDouble(s));
                //System.out.println(s);
            } else if(s.equals("+")) {
                this.answer.push(calculatorService.add(this.answer.pop(), this.answer.pop()));
            } else if(s.equals("-")) {
                this.answer.push(calculatorService.sub(this.answer.pop(), this.answer.pop()));
            } else if(s.equals("*")) {
                this.answer.push(calculatorService.mul(this.answer.pop(), this.answer.pop()));
            } else if(s.equals("/")) {
                this.answer.push(calculatorService.div(this.answer.pop(), this.answer.pop()));
            } else if(s.equals("%")) {
                this.answer.push(calculatorService.mod(this.answer.pop(), this.answer.pop()));
            } else {
                throw new IllegalStateException();
            }
        }
    }

    private final void createOperatorOrder(final double num1, final double num2, final Operator operator) {
        switch (operator) {
            case ADD:
                // this.answer.push(add(num1, num2));

                break;
            case SUBTRACT:
                this.answer.push(sub(num1, num2));
                break;
            case MULTIPLY:
                this.answer.push(mul(num1, num2));
                break;
            case DIVIDE:
                this.answer.push(div(num1, num2));
                break;
            case MOD:
                this.answer.push(mod(num1, num2));
                break;
            default:
                throw new IllegalStateException();
        }
    }

    private final double add(final double num1, final double num2) {
        return num2 + num1;
    }

    private final double sub(final double num1, final double num2) {
        return num2 - num1;
    }

    private final double mul(final double num1, final double num2) {
        return num2 * num1;
    }

    private final double div(final double num1, final double num2) {
        return num2 / num1;
    }

    private final double mod(final double num1, final double num2) {
        return num2 % num1;
    }

    enum Operator {
        ADD, 
        SUBTRACT, 
        MULTIPLY, 
        DIVIDE, 
        MOD
    }
    
    public static class PatternType {
      public static final Pattern PRINT = Pattern.compile("^print\\((.+)\\)$");
      public static final Pattern ID = Pattern.compile("^id\\((.+)\\)$");
      public static final Pattern DECLARATION = Pattern.compile("^(([a-zA-Z_][a-zA-Z_0-9]*)=(.+))|(([a-zA-Z_][a-zA-Z_0-9]*) = (.+))$");
      public static final Pattern VARIABLE = Pattern.compile("^([a-zA-Z_][a-zA-Z_0-9]*)$");
      public static final Pattern VARIABLE_CALCULATOR = Pattern.compile("^([a-zA-Z_][a-zA-Z_0-9 \\+\\/\\%\\.\\-\\\\*\\(\\)]*)$");
      public static final Pattern CALCULATOR = Pattern.compile("^[\\d \\+\\/\\%\\.\\-\\\\*\\(\\)]*$");
      public static final Pattern NUMBER = Pattern.compile("^(\\d+)|(\\d+\\.\\d+)$");
      public static final Pattern INTEGER = Pattern.compile("^(\\d+)|(\\d+.0)$");
      public static final Pattern DOUBLE = Pattern.compile("^\\d+\\.\\d+$");
      public static final Pattern STRING = Pattern.compile("^(\"(.+)\")|(\'(.+)\')$");
    }

    public static void main(String[] args) {
        String number1 = "2 * (4+5)";
        String number2 = "1231232 * (4133+51312)";
        String number3 = "231414 * (436344+142145) + 80808";
        String number4 = "212312.5151 * (4.31+5.13333) / 2";
        System.out.println(Arrays.toString(number1.replaceAll("\\s+", "").split("(?<=[-+*\\/\\(\\)=])|(?=[-+*\\/\\(\\)=])")));
        System.out.println(Arrays.toString(number2.replaceAll("\\s+", "").split("(?<=[-+*\\/\\(\\)=])|(?=[-+*\\/\\(\\)=])")));
        System.out.println(Arrays.toString(number3.replaceAll("\\s+", "").split("(?<=[-+*\\/\\(\\)=])|(?=[-+*\\/\\(\\)=])")));
        System.out.println(Arrays.toString(number4.replaceAll("\\s+", "").split("(?<=[-+*\\/\\(\\)=])|(?=[-+*\\/\\(\\)=])")));
        // String number1 = "2*(4+5)";
        // String number2 = "2               *(4+          5        )";
        // String number3 = "((1+(2*1))/3)+3";
        System.out.println(PatternType.NUMBER.matcher("2").matches());
        System.out.println(new Calculator(number1).getResult());
        System.out.println(new Calculator(number2).getResult());
        System.out.println(new Calculator(number3).getResult());
    }
}

interface Calculation {
    double Calculate(final double num1, final double num2);
}

class Addition implements Calculation {
    @Override
    public double Calculate(final double num1, final double num2) {
        return num2 + num1;
    }
}

class Subtraction implements Calculation {
    @Override
    public double Calculate(final double num1, final double num2) {
        return num2 - num1;
    }
}

class Multiplication implements Calculation {
    @Override
    public double Calculate(final double num1, final double num2) {
        return num2 * num1;
    }
}

class Division implements Calculation {
    @Override
    public double Calculate(final double num1, final double num2) {
        return num2 / num1;
    }
}

class Mode implements Calculation {
    @Override
    public double Calculate(final double num1, final double num2) {
        return num2 % num1;
    }
}

class CalculatorService {
    private final Calculation addition;
    private final Calculation subtraction;
    private final Calculation multiplication;
    private final Calculation division;
    private final Calculation mode;

    public CalculatorService(final Calculation addition, final Calculation subtraction, final Calculation multiplication, final Calculation division, final Calculation mode) {
        this.addition = addition;
        this.subtraction = subtraction;
        this.multiplication = multiplication;
        this.division = division;
        this.mode = mode;
    }

    public final double add(final double num1, final double num2) {
        return addition.Calculate(num1, num2);
    }

    public final double sub(final double num1, final double num2) {
        return subtraction.Calculate(num1, num2);
    }

    public final double mul(final double num1, final double num2) {
        return multiplication.Calculate(num1, num2);
    }

    public final double div(final double num1, final double num2) {
        return division.Calculate(num1, num2);
    }

    public final double mod(final double num1, final double num2) {
        return mode.Calculate(num1, num2);
    }
}