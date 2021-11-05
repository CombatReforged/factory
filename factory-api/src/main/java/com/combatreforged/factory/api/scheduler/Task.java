package com.combatreforged.factory.api.scheduler;

public abstract class Task {
    private final TaskScheduler scheduler;
    private final Runnable task;

    private boolean inactive;

    private TaskPointer<?> pointer;

    public Task(TaskScheduler scheduler, Runnable task) {
        this.scheduler = scheduler;
        this.task = task;
        this.inactive = false;
    }

    public void run() {
        this.task.run();
    }

    protected synchronized void markInactive() {
        this.inactive = true;
    }

    public synchronized boolean isActive() {
        return !this.inactive;
    }

    protected synchronized void setPointer(TaskPointer<?> pointer) {
        this.pointer = pointer;
    }

    public abstract void tick();

    public synchronized void cancel() {
        this.scheduler.cancelTask(pointer);
    }
}
