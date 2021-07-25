package com.combatreforged.factory.api.scheduler;

import com.combatreforged.factory.api.exception.TaskException;
import com.combatreforged.factory.api.world.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class TaskScheduler {
    private final Map<TaskPointer<? extends Task>, Task> tasks;

    private TaskScheduler() {
        tasks = new HashMap<>();
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
        this.tasks.remove(task);
        task.deleteReference();
    }

    @Nullable
    public <T extends Task> Task getTask(TaskPointer<T> pointer) {
        return pointer.get();
    }

    private void tick() {
        for (Task task : tasks.values()) {
            try {
                task.tick();
            } catch (Exception e) {
                throw new TaskException();
            }
        }
    }
}
