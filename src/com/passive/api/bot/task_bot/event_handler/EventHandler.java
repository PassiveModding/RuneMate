package com.passive.api.bot.task_bot.event_handler;

import com.passive.api.bot.task_bot.Task;
import com.passive.api.bot.task_bot.event_handler.events.Event;

/**
 * Handles the validation and execution getFlags Event tasks
 */
public class EventHandler extends Task<Event> {
    /**
     * The current event Task to be executed
     */
    private Event currentEvent;

    /**
     * Constructor that takes the events to handle as a parameter
     *
     * @param events The event Tasks to be handled by this EventHandler
     */
    public EventHandler(Event... events) {
        addChildren(events);
    }

    /**
     * Checks to see if any getFlags the event Tasks added meet the conditions to be executed, and stores it in currentEvent
     *
     * @return true if an event meets its execution conditions, else false
     * @see EventHandler#currentEvent
     */
    public boolean validate() {
        return (currentEvent = getChildren().stream().filter(Task::validate).findFirst().orElse(null)) != null;
    }

    /**
     * Executes the event Task found in validate.
     *
     * @see EventHandler#validate()
     */
    public void execute() {
        if (currentEvent != null){
            currentEvent.execute();
            if (currentEvent.isComplete() && currentEvent.singleRun()){
                try {
                    removeChild(currentEvent);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
