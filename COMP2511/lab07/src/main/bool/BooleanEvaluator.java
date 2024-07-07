package bool;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BooleanEvaluator {

    public static boolean evaluate(BooleanNode expression) {
        // Return the expression evaluated
        return expression.evaluate();
    }

    public static String prettyPrint(BooleanNode expression) {
        // Pretty print the expression
        return expression.prettyPrint();
    }

    public static void main(String[] args) {
        System.out.println(BooleanEvaluator.evaluate(new And(new BooleanLeaf(true), new BooleanLeaf(true))));
        System.out.println(BooleanEvaluator.evaluate(new And(new BooleanLeaf(true), new BooleanLeaf(false))));
        System.out.println(BooleanEvaluator.prettyPrint(new Or(new And(new BooleanLeaf(true), new BooleanLeaf(true)), new BooleanLeaf(false))));
        System.out.println(BooleanEvaluator.prettyPrint(new And(new BooleanLeaf(true), new BooleanLeaf(false))));
    }

}