package dev.sultanov.springboot.unmanagedobjects;

import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class TaxCalculator {

    private static TaxCalculator instance;

    public static TaxCalculator getInstance() {
        return instance;
    }

    @PostConstruct
    private void registerInstance() {
        instance = this;
    }

    public double calculate(double price) {
        return price * 0.25;
    }
}