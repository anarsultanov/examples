package dev.sultanov.cadence.saga.service.common;

import java.math.BigDecimal;

public interface CustomerActivities {

    boolean reserveCredit(String customerId, BigDecimal amount);
}
