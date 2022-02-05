package dev.sultanov.springboot.unmanagedobjects;

import org.springframework.stereotype.Component;

@Component
public class TaxCalculatorProvider {

    private static ITaxCalculator calculator;

    public TaxCalculatorProvider(ITaxCalculator calculator) {
        TaxCalculatorProvider.calculator = calculator;
    }

    public static ITaxCalculator getCalculator() {
        return calculator;
    }
}