package dev.sultanov.workflow;

enum States {
    AWAITING_INPUT,
    REQUIREMENTS_EVALUATION,
    SCRIPT_GENERATION,
    SOLUTION_VERIFICATION,
    REQUIREMENTS_REVISION,
    SUCCESSFUL_COMPLETION,
    INVALID_REQUIREMENTS
}
