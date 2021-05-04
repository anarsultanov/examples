package dev.sultanov.cadence.saga.service.common;

import com.uber.cadence.workflow.WorkflowMethod;

import java.math.BigDecimal;

public interface CreateOrderWorkflow {
    @WorkflowMethod
    String createOrder(String customerId, BigDecimal totalMoney);
}
