package dev.sultanov.cadence.saga.service.order;

import com.uber.cadence.client.WorkflowClient;
import com.uber.cadence.client.WorkflowClientOptions;
import com.uber.cadence.serviceclient.ClientOptions;
import com.uber.cadence.serviceclient.IWorkflowService;
import com.uber.cadence.serviceclient.WorkflowServiceTChannel;
import com.uber.cadence.worker.Worker;
import com.uber.cadence.worker.WorkerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static dev.sultanov.cadence.saga.service.common.Constants.DOMAIN;
import static dev.sultanov.cadence.saga.service.common.Constants.ORDER_TASK_LIST;

@SpringBootApplication
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

    @Bean
    WorkflowClient workflowClient() {
        IWorkflowService service = new WorkflowServiceTChannel(ClientOptions.defaultInstance());

        WorkflowClientOptions workflowClientOptions = WorkflowClientOptions.newBuilder()
                .setDomain(DOMAIN)
                .build();
        return WorkflowClient.newInstance(service, workflowClientOptions);
    }

    @Bean
    CommandLineRunner commandLineRunner(WorkflowClient workflowClient) {
        return args -> {
            WorkerFactory factory = WorkerFactory.newInstance(workflowClient);
            Worker worker = factory.newWorker(ORDER_TASK_LIST);
            worker.registerActivitiesImplementations(new OrderActivitiesImpl());
            worker.registerWorkflowImplementationTypes(CreateOrderWorkflowImpl.class);
            factory.start();
        };
    }
}
