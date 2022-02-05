package dev.sultanov.springboot.unmanagedobjects;

public record Invoice(double price) {

    public double calculateTaxUsingComponent() {
        return TaxCalculator.getInstance().calculate(this.price);
    }

    public double calculateTaxUsingProvider() {
        return TaxCalculatorProvider.getCalculator().calculate(this.price);
    }
}