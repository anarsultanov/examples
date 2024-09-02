package dev.sultanov.agent;

import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface RequirementsEvaluator {

    @UserMessage("""
            Evaluate the given requirements to determine if they are clear, concise, and feasible to implement in a single Python file.
            Return true if the requirements are clear and achievable; otherwise, return false.

            Requirements: {{requirements}}
            """)
    boolean areRequirementsFeasible(@V("requirements") String requirements);
}
