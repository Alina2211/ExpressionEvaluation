package Tests;

import Classes.Evaluation;

import static org.junit.Assert.assertEquals;

public class EvaluationTest {

    String expr = "-5+7*pow(7+3*1, 3)/2";
    Evaluation testExpr = new Evaluation(expr);

    @org.junit.Test
    public void evaluate() {

        int expected = testExpr.evaluate();
        int real = 3495;
        assertEquals(expected, real);
    }

}