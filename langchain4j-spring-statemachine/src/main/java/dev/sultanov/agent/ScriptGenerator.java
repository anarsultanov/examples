package dev.sultanov.agent;

import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface ScriptGenerator {
    @UserMessage("""
        You are an expert Python developer. Create only the Python CLI application script based on the given requirements.
        Do not include any explanations, comments, or additional text—only the code itself.

        Requirements: {{requirements}}
        """)
    String generateScript(@V("requirements") String requirements);
}
