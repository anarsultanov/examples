package dev.sultanov.springboot.unmanagedobjects;

import org.springframework.stereotype.Component;

@Component
public class TaxCalculator {

    public double calculate(double price) {
        return price * 0.25;
    }
}