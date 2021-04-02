package com.dave.ConvertUnits.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestUnitExpression {

    ExpressionTestCase[] goodTestCases = {
            new ExpressionTestCase("(degree/(minute*hectare))", "(rad/(s*m²))", 2.9088820866572E-8),
            new ExpressionTestCase("ha*°", "m²*rad", 174.53292519943),
            new ExpressionTestCase("degree/minute", "rad/s", .00029088820866572),
            new ExpressionTestCase("degree", "rad", 0.017453292519943),
            new ExpressionTestCase("tonne/day", "kg/s", 0.011574074074074),
    };

    String[] badTestCases = {
            "",
            "this is not an expression",
            "(degree/(minute*hectare",
            "(degree",
            "(foo)",
            "))"
    };

    @Test
    void goodCases() {
        for (ExpressionTestCase testCase : goodTestCases) {
            UnitExpression unitExpression = new UnitExpression(testCase.expression);
            assertEquals(testCase.unitName, unitExpression.getUnitName());
            assertEquals(testCase.multiplicationFactor, unitExpression.getMultiplicationFactor());
        }
    }

    @Test
    void badCases() {
        for (String testCase : badTestCases) {
            assertThrows(IllegalArgumentException.class, () -> new UnitExpression(testCase));
        }
    }

    static class ExpressionTestCase {
        String expression;
        String unitName;
        Double multiplicationFactor;

        public ExpressionTestCase(String expression, String unitName, Double multiplicationFactor) {
            this.expression = expression;
            this.unitName = unitName;
            this.multiplicationFactor = multiplicationFactor;
        }
    }

}
