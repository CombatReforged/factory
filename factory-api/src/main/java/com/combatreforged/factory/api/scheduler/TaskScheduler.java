package com.combatreforged.factory.api.scheduler;

import com.combatreforged.factory.api.world.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class TaskScheduler {
    public static final Logger LOGGER = LogManager.getLogger("TaskScheduler");

    private final AtomicBoolean ticking = new AtomicBoolean(false);
    private final Map<TaskPointer<? extends Task>, Task> tasks;
    private final List<Runnable> updates;

    private TaskScheduler() {
        tasks = new HashMap<>();
        updates = new ArrayList<>();
    }

    public static Pair<TaskScheduler, TickFunction> create() {
        final TaskScheduler scheduler = new TaskScheduler();
        return new Pair<>(scheduler, scheduler::tick);
    }

    public synchronized TaskPointer<ScheduledTask> schedule(Runnable task, int delay) {
        ScheduledTask scheduledTask = new ScheduledTask(this, task, delay);
        return this.registerTask(scheduledTask);
    }

    public synchronized TaskPointer<ScheduledRepeatingTask> scheduleRepeating(Runnable task, int delay, int repeatDelay) {
        ScheduledRepeatingTask repeatingTask = new ScheduledRepeatingTask(this, task, delay, repeatDelay);
        return this.registerTask(repeatingTask);
    }

    public synchronized <T extends Task> TaskPointer<T> registerTask(T task) {
        TaskPointer<T> pointer = new TaskPointer<>(task);
        if (this.ticking.get()) {
            this.updates.add(() -> tasks.put(pointer, task));
        } else {
            this.tasks.put(pointer, task);
        }
        task.setPointer(pointer);
        return pointer;
    }

    public synchronized <T extends Task> void cancelTask(TaskPointer<T> task) {
        if (this.ticking.get()) {
            this.updates.add(() -> tasks.remove(task));
        } else {
            this.tasks.remove(task);
        }
        task.deleteReference();
    }

    @Nullable
    public synchronized <T extends Task> Task getTask(TaskPointer<T> pointer) {
        return pointer.get();
    }

    private void tick() {
        this.ticking.set(true);
        for (Task task : tasks.values()) {
            try {
                if (task.isActive()) {
                    task.tick();
                }
            } catch (Exception e) {
                LOGGER.error("An error occurred while executing task " + task.toString() + ": ", e);
                task.cancel();
            }
        }
        this.ticking.set(false);

        synchronized (this) {
            for (Runnable run : this.updates) {
                run.run();
            }
            updates.clear();
        }
    }
}
