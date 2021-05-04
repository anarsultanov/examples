package dev.sultanov.cadence.saga.service.order;

import com.uber.cadence.client.WorkflowClient;
import com.uber.cadence.client.WorkflowOptions;
import dev.sultanov.cadence.saga.service.common.CreateOrderWorkflow;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.Duration;

import static dev.sultanov.cadence.saga.service.common.Constants.ORDER_TASK_LIST;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final WorkflowClient workflowClient;

    @PostMapping("/sync")
    public ResponseEntity<CreateOrderResponse> createOrderAsync(@RequestBody CreateOrderRequest request) {
        var workflow = workflowClient.newWorkflowStub(
                CreateOrderWorkflow.class,
                new WorkflowOptions.Builder()
                        .setExecutionStartToCloseTimeout(Duration.ofSeconds(5))
                        .setTaskList(ORDER_TASK_LIST)
                        .build()
        );
        var orderId = workflow.createOrder(request.customerId, request.amount);
        return ResponseEntity.ok(new CreateOrderResponse(orderId));
    }

    @PostMapping("/async")
    public ResponseEntity<Void> createOrder(@RequestBody CreateOrderRequest request) {
        var workflow = workflowClient.newWorkflowStub(
                CreateOrderWorkflow.class,
                new WorkflowOptions.Builder()
                        .setExecutionStartToCloseTimeout(Duration.ofSeconds(5))
                        .setTaskList(ORDER_TASK_LIST)
                        .build()
        );
        WorkflowClient.execute(workflow::createOrder, request.customerId, request.amount);
        return ResponseEntity.accepted().build();
    }

    @Value
    @NoArgsConstructor(force = true)
    private static class CreateOrderRequest {
        String customerId;
        BigDecimal amount;
    }

    @Value
    private static class CreateOrderResponse {
        String orderId;
    }
}
