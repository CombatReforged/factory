package com.combatreforged.factory.api.scheduler;

import com.combatreforged.factory.api.exception.TaskException;
import com.combatreforged.factory.api.world.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskScheduler {
    private boolean ticking;
    private final Map<TaskPointer<? extends Task>, Task> tasks;
    private final List<TaskPointer<? extends Task>> cleanup;

    private TaskScheduler() {
        tasks = new HashMap<>();
        cleanup = new ArrayList<>();
    }

    public static Pair<TaskScheduler, TickFunction> create() {
        final TaskScheduler scheduler = new TaskScheduler();
        return new Pair<>(scheduler, scheduler::tick);
    }

    public TaskPointer<ScheduledTask> schedule(Runnable task, int delay) {
        ScheduledTask scheduledTask = new ScheduledTask(this, task, delay);
        return this.registerTask(scheduledTask);
    }

    public TaskPointer<ScheduledRepeatingTask> scheduleRepeating(Runnable task, int delay, int repeatDelay) {
        ScheduledRepeatingTask repeatingTask = new ScheduledRepeatingTask(this, task, delay, repeatDelay);
        return this.registerTask(repeatingTask);
    }

    public <T extends Task> TaskPointer<T> registerTask(T task) {
        TaskPointer<T> pointer = new TaskPointer<>(task);
        this.tasks.put(pointer, task);
        task.setPointer(pointer);
        return pointer;
    }

    public <T extends Task> void cancelTask(TaskPointer<T> task) {
        if (this.ticking) {
            this.cleanup.add(task);
        } else {
            this.tasks.remove(task);
        }
        task.deleteReference();
    }

    @Nullable
    public <T extends Task> Task getTask(TaskPointer<T> pointer) {
        return pointer.get();
    }

    private void tick() {
        this.ticking = true;
        for (Task task : tasks.values()) {
            try {
                if (task.isActive()) {
                    task.tick();
                }
            } catch (Exception e) {
                throw new TaskException();
            }
        }
        this.ticking = false;

        for (TaskPointer<? extends Task> taskPointer : this.cleanup) {
            this.tasks.remove(taskPointer);
        }
    }
}
