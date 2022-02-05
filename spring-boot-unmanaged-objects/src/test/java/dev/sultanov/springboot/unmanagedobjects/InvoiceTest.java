package dev.sultanov.springboot.unmanagedobjects;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InvoiceTest {

    @Test
    void calculateTaxUsingComponent() {
        // given
        var invoice = new Invoice(20);

        // when
        var result = invoice.calculateTaxUsingComponent();

        // then
        assertThat(result).isEqualTo(5.0);
    }

    @Test
    void calculateTaxUsingProvider() {
        // given
        var invoice = new Invoice(50);

        // when
        var result = invoice.calculateTaxUsingProvider();

        // then
        assertThat(result).isEqualTo(12.5);
    }
}