package dev.sultanov.agent;

import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface RequirementsRewriter {

    @UserMessage("""
        The following Python script failed to meet the specified requirements.
        Provide feedback on why it did not meet the requirements and rewrite the requirements by incorporating the necessary improvements while maintaining the original intent.

        Requirements: {{requirements}}
        Script: {{script}}

        Return the improved requirements.
        """)
    String rewriteRequirements(@V("requirements") String requirements, @V("script") String script);
}
