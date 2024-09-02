package dev.sultanov;

import dev.sultanov.workflow.WorkflowService;
import java.util.Scanner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MultiAgentApplication {

    public static void main(String[] args) {
        SpringApplication.
                run(MultiAgentApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(WorkflowService workflowService) {
        return args -> {
            try (Scanner scanner = new Scanner(System.in)) {
                System.out.println("""
                        Welcome to the Python CLI Application Generator!
                                            
                        Please describe the requirements for the application you need.
                        Clearly specify the desired functionality in a concise manner,
                        ensuring it can be implemented in a single Python file.
                                            
                        Example: "Create a Python CLI that converts temperatures between Celsius and Fahrenheit."
                                            
                        Enter your requirements below:
                        """);

                System.out.print("> ");
                String userInput = scanner.nextLine().trim();

                if (userInput.isEmpty()) {
                    System.out.println("No input provided. Exiting...");
                    return;
                }

                try {
                    String response = workflowService.generateScript(userInput);
                    System.out.println("\n--- Result ---\n");
                    System.out.println(response);
                    System.out.println("\n----------------\n");
                } catch (Exception e) {
                    System.err.println("An error occurred while processing your request: " + e.getMessage());
                }

            } catch (Exception e) {
                System.err.println("An unexpected error occurred: " + e.getMessage());
            }
        };
    }

}
