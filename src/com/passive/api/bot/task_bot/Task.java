package com.passive.api.bot.task_bot;

import com.passive.api.bot.Passive_BOT;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A Task has a validate and setExecute method.
 * Validation ensures the correct conditions are met before execution occurs.
 */
public abstract class Task<T extends Task> {
    protected Passive_BOT bot = Passive_BOT.getBot();
    protected Task parent = null;
    private ReentrantLock taskLock = new ReentrantLock();
    private List<T> children = new ArrayList<>();

    public Task getParent() {
        return parent;
    }

    public void setParent(Task parent) {
        this.parent = parent;
    }

    /**
     * Ensures the correct conditions are met for execution.
     *
     * @return true if the conditions are met, else false.
     */
    public abstract boolean validate();

    /**
     *
     */
    public abstract void execute();

    /**
     * Adds a Task to the children collection
     *
     * @param task the Task to offer
     */
    public void addChild(T task) {
        taskLock.lock();
        task.setParent(this);
        children.add(task);
        taskLock.unlock();
    }

    public void removeChild(T task) {
        taskLock.lock();
        children.remove(task);
        taskLock.unlock();
    }

    public void addChildren(T... children) {
        taskLock.lock();
        for (T child : children) {
            this.children.add(child);
            child.setParent(this);
        }
        taskLock.unlock();
    }

    @SafeVarargs
    public final void removeChildren(T... children) {
        taskLock.lock();
        for (T t : children) {
            this.children.remove(t);
        }
        taskLock.unlock();
    }

    public boolean hasChildren() {
        return children.size() > 0;
    }

    public List<T> getChildren() {
        return children;
    }

    public void setChildren(List<T> children) {
        this.children = children;
        children.forEach(e -> setParent(this));
    }

    public T getFirstValidChild() {
        taskLock.lock();
        try {
            return getChildren().stream().filter(t -> !t.isComplete() && t.validate()).findFirst().orElse(null);
        } finally {
            taskLock.unlock();
        }
    }

    public String getName() {
        return getClass().getSimpleName();
    }

    public void setStatus(String status) {
        bot.setStatus(status);
    }

    public boolean isComplete() {
        return false;
    }

    public final boolean isIncomplete() {
        return !isComplete();
    }
}
