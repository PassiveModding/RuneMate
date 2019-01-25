package com.passive.api.bot.task_bot;

import com.passive.api.bot.Passive_BOT;
import com.runemate.game.api.hybrid.RuneScape;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public abstract class PassiveTaskBot<T extends Task> extends Passive_BOT {
    private ObservableList<T> tasks = new ObservableListWrapper<>(new CopyOnWriteArrayList<>());
    private ReentrantLock lock = new ReentrantLock();
    private long allTasksCompleteTime = -1;

    public void addTask(T task) {
        lock.lock();
        try {
            tasks.add(task);
        } finally {
            lock.unlock();
        }
    }

    public void removeTask(T task) {
        lock.lock();
        try {
            tasks.remove(task);
        } finally {
            lock.unlock();
        }
    }

    public void addTasks(T... tasks){
        lock.lock();
        try {
            this.tasks.addAll(tasks);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void onLoop() {
        lock.lock();
        T task = null;
        try {
            if (!tasks.isEmpty()) {
                List<T> incompleteTasks = tasks.stream().filter(T::isIncomplete).collect(Collectors.toList());
                if (incompleteTasks.isEmpty()) {
                    if (allTasksCompleteTime == -1) {
                        allTasksCompleteTime = getRuntime();
                    }
                    if (getRuntime() - allTasksCompleteTime > 30000) {
                        setStatus("No incomplete tasks found for 30 seconds, stopping bot");
                        //TODO: Ensure proper logout rather than just one try
                        RuneScape.logout();
                        stop("No incomplete tasks found for 30 seconds, stopping bot");
                    }
                } else {
                    allTasksCompleteTime = -1;
                }
                task = incompleteTasks.stream().filter(Task::validate).findFirst().orElse(null);
            }
        } finally {
            lock.unlock();
        }
        if (task != null) {
            task.execute();
        }
    }


    public void lockTasks() {
        lock.lock();
    }

    public void unlockTasks() {
        lock.unlock();
    }

    public ObservableList<T> getTasks() {
        return tasks;
    }

    public void setTasks(ObservableList<T> newTasks) {
        lock.lock();
        try {
            tasks = newTasks;
        } finally {
            lock.unlock();
        }
    }
}
