package com.combatreforged.factory.api.scheduler;

public class ScheduledTask extends Task {
    int tick;
    private final int delay;

    protected ScheduledTask(TaskScheduler scheduler, Runnable task, int delay) {
        super(scheduler, task);
        this.delay = delay;
        this.tick = delay;
    }

    public int getDelay() {
        return delay;
    }

    @Override
    public void tick() {
        if (this.isActive()) {
            if (tick > 0) {
                tick--;
            } else {
                this.run();
                this.afterRun();
            }
        }
    }

    public void afterRun() {
        this.cancel();
    }
}
