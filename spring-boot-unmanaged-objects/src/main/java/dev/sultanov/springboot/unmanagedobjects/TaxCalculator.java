package dev.sultanov.springboot.unmanagedobjects;

import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class TaxCalculator implements ITaxCalculator {

    private static TaxCalculator instance;

    public static TaxCalculator getInstance() {
        return instance;
    }

    @PostConstruct
    private void registerInstance() {
        instance = this;
    }

    @Override
    public double calculate(double price) {
        return price * 0.25;
    }
}