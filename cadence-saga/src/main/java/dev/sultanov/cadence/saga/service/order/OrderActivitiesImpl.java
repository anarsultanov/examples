package dev.sultanov.cadence.saga.service.order;

import dev.sultanov.cadence.saga.service.common.OrderActivities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.UUID;

public class OrderActivitiesImpl implements OrderActivities {

    private static final Logger logger = LoggerFactory.getLogger(OrderActivitiesImpl.class);

    @Override
    public String createOrder(String customerId, BigDecimal amount) {
        var orderId = UUID.randomUUID().toString();
        logger.info("Order {} created in pending state", orderId);
        return orderId;
    }

    @Override
    public void approveOrder(String orderId) {
        logger.info("Order {} approved", orderId);
    }

    @Override
    public void rejectOrder(String orderId) {
        logger.info("Order {} rejected", orderId);
    }
}
