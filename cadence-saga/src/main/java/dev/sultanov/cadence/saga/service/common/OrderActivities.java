package dev.sultanov.cadence.saga.service.common;

import java.math.BigDecimal;

public interface OrderActivities {

    String createOrder(String customerId, BigDecimal totalMoney);
    void approveOrder(String orderId);
    void rejectOrder(String orderId);
}
