package school.sorokin.javacore;

import java.util.List;
import java.util.concurrent.Callable;

public class CalculateSumTask implements Callable<Integer> {
    private final List<Integer> numbers;
    private final String taskName;

    public CalculateSumTask(List<Integer> numbers, String taskName) {
        this.numbers = numbers;
        this.taskName = taskName;
    }

    @Override
    public Integer call() throws Exception {
        System.out.println("Название задачи: " + taskName + ", имя потока: " + Thread.currentThread().getName());

        Thread.sleep(200);

        Integer sum = 0;

        for (Integer number : numbers) {
            sum += number;
        }

        return sum;
    }
}
