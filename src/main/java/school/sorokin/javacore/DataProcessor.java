package school.sorokin.javacore;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DataProcessor {
    private final ExecutorService executor;
    private final AtomicInteger taskCounter = new AtomicInteger(1);
    private final AtomicInteger activeTaskCounter = new AtomicInteger(0);
    private final Map<String, Integer> results = new HashMap<>();
    private final Map<String, Future<Integer>> futures = new HashMap<>();

    public DataProcessor(int poolSize) {
        this.executor = Executors.newFixedThreadPool(poolSize);
    }

    public synchronized void submitTask(List<Integer> numbers) {
        String taskName = "task" + taskCounter.getAndIncrement();

        CalculateSumTask task = new CalculateSumTask(numbers, taskName);
        activeTaskCounter.incrementAndGet();

        Future<Integer> future = executor.submit(() -> {
            try {
                Integer result = task.call();
                synchronized (results) {
                    results.put(taskName, result);
                }
                return result;
            } finally {
                activeTaskCounter.decrementAndGet();
            }
        });

        futures.put(taskName, future);
    }

    public int getActiveTaskCount() {
        return activeTaskCounter.get();
    }

    public Optional<Integer> getResult(String taskName) {
        synchronized (results) {
            return Optional.ofNullable(results.get(taskName));
        }
    }

    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}