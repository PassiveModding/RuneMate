package com.passive.api.bot.task_bot.event_handler.events;

import com.passive.api.bot.task_bot.Task;

/**
 * Created by SlashnHax on 4/01/2016.
 */
public abstract class Event extends Task {
    public abstract boolean singleRun();
}
