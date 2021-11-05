package com.combatreforged.factory.api.scheduler;

import org.jetbrains.annotations.Nullable;

public class TaskPointer<T extends Task> {
    private T task;

    protected TaskPointer(T task) {
        this.task = task;
    }

    protected synchronized void deleteReference() {
        this.task = null;
    }

    public synchronized void cancel() {
        if (task != null && this.task.isActive()) {
            task.cancel();
        }
    }

    @Nullable
    public synchronized T get() {
        if (task != null && task.isActive()) {
            return task;
        } else {
            return null;
        }
    }

    public synchronized boolean isPresent() {
        return this.task != null && this.task.isActive();
    }
}
