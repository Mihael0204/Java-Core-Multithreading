package school.sorokin.javacore;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int poolSize = 10;

        DataProcessor dataProcessor = new DataProcessor(poolSize);

        for (int i = 0; i < 100; i++) {
            List<Integer> numbers = Arrays.asList(i, i + 1, i + 2);
            dataProcessor.submitTask(numbers);
        }

        while (dataProcessor.getActiveTaskCount() > 0) {
            System.out.println("Активных задач: " + dataProcessor.getActiveTaskCount());
            TimeUnit.MILLISECONDS.sleep(300);
        }

        System.out.println("\n Результаты задач: ");
        for (int i = 0; i < 100; i++) {
            String taskName = "task" + i;
            System.out.println("Название задачи: " + dataProcessor.getResult(taskName).orElse(null));
        }

        dataProcessor.shutdown();
        dataProcessor.awaitTermination();
    }
}
