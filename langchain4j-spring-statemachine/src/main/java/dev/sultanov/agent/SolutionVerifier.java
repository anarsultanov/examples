package dev.sultanov.agent;

import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface SolutionVerifier {

    @UserMessage("""
        Review the provided Python script to ensure it accurately solves the problem as described in the requirements.
        The requirements should be specific, actionable, and focus on a single, clear task.
        
        If the script addresses multiple unrelated tasks, includes ambiguous steps, or lacks sufficient detail for implementation, return false and specify why the requirements are inadequate.

        Requirements: {{requirements}}
        Script: {{script}}
        """)
    boolean isScriptValid(@V("script") String script, @V("requirements") String requirements);
}

