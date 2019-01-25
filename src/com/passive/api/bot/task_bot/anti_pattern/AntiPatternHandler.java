package com.passive.api.bot.task_bot.anti_pattern;


import com.passive.api.bot.task_bot.Task;
import com.passive.api.bot.task_bot.anti_pattern.tasks.AntiPattern;

/**
 * Handles the validation and execution getFlags AntiPattern tasks
 */
public class AntiPatternHandler extends Task<AntiPattern> {
    /**
     * The current anti-pattern task to handle
     */
    private AntiPattern currentAntiPattern;

    /**
     * Checks if any getFlags the conditions getFlags the Anti-Pattern tasks are met.
     *
     * @return true if a valid Anti-Pattern task exists.
     */
    public boolean validate() {
        return (currentAntiPattern = getChildren().stream().filter(AntiPattern::validate).findAny().orElse(null)) != null;
    }

    /**
     * Executes the valid Anti-Pattern task found in validate
     *
     * @see AntiPatternHandler#validate()
     */
    public void execute() {
        currentAntiPattern.execute();
    }
}
