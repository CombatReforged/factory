package com.combatreforged.factory.api.scheduler;

public class ScheduledRepeatingTask extends ScheduledTask {
    private final int repeatingDelay;

    protected ScheduledRepeatingTask(TaskScheduler scheduler, Runnable task, int delay, int repeatingDelay) {
        super(scheduler, task, delay);
        this.repeatingDelay = repeatingDelay;
    }

    public int getRepeatingDelay() {
        return repeatingDelay;
    }

    @Override
    public void afterRun() {
        this.tick = repeatingDelay;
    }
}
