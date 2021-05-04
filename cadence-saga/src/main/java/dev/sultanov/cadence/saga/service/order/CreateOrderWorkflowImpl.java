package dev.sultanov.cadence.saga.service.order;

import com.uber.cadence.activity.ActivityOptions;
import com.uber.cadence.workflow.Saga;
import com.uber.cadence.workflow.Workflow;
import dev.sultanov.cadence.saga.service.common.CreateOrderWorkflow;
import dev.sultanov.cadence.saga.service.common.CustomerActivities;
import dev.sultanov.cadence.saga.service.common.OrderActivities;

import java.math.BigDecimal;
import java.time.Duration;

import static dev.sultanov.cadence.saga.service.common.Constants.CUSTOMER_TASK_LIST;
import static dev.sultanov.cadence.saga.service.common.Constants.ORDER_TASK_LIST;

public class CreateOrderWorkflowImpl implements CreateOrderWorkflow {

    private final ActivityOptions customerActivityOptions = new ActivityOptions.Builder()
            .setTaskList(CUSTOMER_TASK_LIST)
            .setScheduleToCloseTimeout(Duration.ofSeconds(2))
            .build();
    private final CustomerActivities customerActivities =
            Workflow.newActivityStub(CustomerActivities.class, customerActivityOptions);

    private final ActivityOptions orderActivityOptions = new ActivityOptions.Builder()
            .setTaskList(ORDER_TASK_LIST)
            .setScheduleToCloseTimeout(Duration.ofSeconds(2))
            .build();
    private final OrderActivities orderActivities =
            Workflow.newActivityStub(OrderActivities.class, orderActivityOptions);

    @Override
    public String createOrder(String customerId, BigDecimal amount) {
        Saga.Options sagaOptions = new Saga.Options.Builder().build();
        Saga saga = new Saga(sagaOptions);
        try {
            String orderId = orderActivities.createOrder(customerId, amount);
            saga.addCompensation(orderActivities::rejectOrder, orderId);

            if (customerActivities.reserveCredit(customerId, amount)) {
                orderActivities.approveOrder(orderId);
                return orderId;
            } else {
                throw new IllegalStateException("Failed to reserve credit");
            }
        } catch (Exception e) {
            saga.compensate();
            throw e;
        }
    }
}
