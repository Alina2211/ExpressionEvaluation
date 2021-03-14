package Classes;
import Classes.Evaluation;

public class Main {

    public static void main(String[] args) {
        String expressionText = "-155-3*(6+4)";
        Evaluation myExpression = new Evaluation(expressionText);
        int value = myExpression.evaluate();
        System.out.println(value);
    }
}
