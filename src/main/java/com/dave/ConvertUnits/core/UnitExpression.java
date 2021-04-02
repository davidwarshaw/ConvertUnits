package com.dave.ConvertUnits.core;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UnitExpression {

    private final Map<String, String> unitsMap = Collections.unmodifiableMap(Stream.of(
            new SimpleEntry<>("m", "s"),
            new SimpleEntry<>("h", "s"),
            new SimpleEntry<>("d", "s"),
            new SimpleEntry<>("°", "rad"),
            new SimpleEntry<>("'", "rad"),
            new SimpleEntry<>("\"", "rad"),
            new SimpleEntry<>("ha", "m²"),
            new SimpleEntry<>("L", "m³"),
            new SimpleEntry<>("t", "kg"))
            .collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue)));

    private final Map<String, Double> multiplicationFactorsMap = Collections.unmodifiableMap(Stream.of(
            new SimpleEntry<>("m", 60.0),
            new SimpleEntry<>("h", 3600.0),
            new SimpleEntry<>("d", 86400.0),
            new SimpleEntry<>("°", Math.PI / 180),
            new SimpleEntry<>("'", Math.PI / 10800),
            new SimpleEntry<>("\"", Math.PI / 648000),
            new SimpleEntry<>("ha", 10000.0),
            new SimpleEntry<>("L", 0.001),
            new SimpleEntry<>("t", 1000.0))
            .collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue)));

    private final String[][] abbreviations = {
            {"minute", "m"},
            {"hour", "h"},
            {"day", "d"},
            {"degree", "°"},
            {"arcminute", "'"},
            {"arcsecond", "\""},
            {"hectare", "ha"},
            {"litre", "L"},
            {"tonne", "t"},
    };
    private final String BAD_ARGUMENT_MESSAGE = "Error: bad unit expression";
    private final String[] operators = {"(", ")", "*", "/"};
    private final String unitName;
    // We want a specific level of precision
    private final int digitsPrecision = 14;
    private final BigDecimal multiplicationFactor;

    public UnitExpression(String expression) throws IllegalArgumentException {

        String normalized = normalize(expression);
        ArrayList<String> tokens = tokenize(normalized);

        StringBuilder unitName = new StringBuilder();
        Stack<BigDecimal> operands = new Stack<>();
        Stack<String> operators = new Stack<>();

        try {
            for (String token : tokens) {
                switch (token) {
                    case "*":
                    case "/":
                    case "(": {
                        unitName.append(token);
                        operators.push(token);
                        break;
                    }
                    case ")": {
                        unitName.append(token);
                        // Evaluate until we hit the opening parentheses
                        while (!operators.peek().equals("(")) {
                            evaluate(operands, operators);
                        }
                        // This must be a "(", so pop it off
                        operators.pop();
                        break;
                    }
                    default: {
                        unitName.append(unitsMap.get(token));
                        BigDecimal multiplicationFactor = BigDecimal.valueOf(multiplicationFactorsMap.get(token));
                        operands.push(multiplicationFactor);
                    }
                }
            }
            // No more tokens, consume the stack
            while (!operators.isEmpty()) {
                evaluate(operands, operators);
            }

            this.unitName = unitName.toString();
            this.multiplicationFactor = operands.pop();

        } catch (EmptyStackException e) {
            // If we ever get an empty stack, the expression was bad
            throw new IllegalArgumentException(BAD_ARGUMENT_MESSAGE);
        }
    }

    private void evaluate(Stack<BigDecimal> operands, Stack<String> operators) throws EmptyStackException {

        BigDecimal right = operands.pop();
        BigDecimal left = operands.pop();
        String operation = operators.pop();

        switch (operation) {
            case "*": {
                operands.push(left.multiply(right));
                break;
            }
            case "/": {
                // Reduce to set precision to guard against division resulting in repeating decimal
                operands.push(left.divide(right, new MathContext(this.digitsPrecision)));
                break;
            }
        }
    }

    private String normalize(String expression) {
        String normalized = expression.toLowerCase(Locale.ROOT).replaceAll("\\s", "");
        for (String[] abbreviation : abbreviations) {
            normalized = normalized.replaceAll(abbreviation[0], abbreviation[1]);
        }
        return normalized;
    }

    private ArrayList<String> tokenize(String expression) {
        // Create a single array of all legal tokens
        List<String> tokens = Arrays.stream(abbreviations).map(abbreviation -> abbreviation[1]).collect(Collectors.toList());
        tokens.addAll(new ArrayList<>(Arrays.asList(operators)));

        ArrayList<String> tokenized = new ArrayList<>();

        // Collect a token until we have the largest possible token (greedy approach)
        // This is necessary because some abbreviations are a strict substring of others
        char[] chs = expression.toCharArray();
        String currentToken = "";
        for (char ch : chs) {
            if (tokens.contains(currentToken) && !tokens.contains(currentToken + ch)) {
                tokenized.add(currentToken);
                currentToken = "";
            }
            currentToken += ch;
        }
        // If we have something left over that's not a token, we got bad input
        if (!tokens.contains(currentToken)) {
            throw new IllegalArgumentException(BAD_ARGUMENT_MESSAGE);
        }
        tokenized.add(currentToken);

        return tokenized;
    }

    public String getUnitName() {
        return unitName;
    }

    public Double getMultiplicationFactor() {
        // Reduce to set precision
        BigDecimal definedPrecision = this.multiplicationFactor.round(new MathContext(this.digitsPrecision));
        return definedPrecision.doubleValue();
    }
}
