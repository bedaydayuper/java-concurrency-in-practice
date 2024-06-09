package net.jcip.zptest.java_concurrency_in_practice_book.chapter6;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CompletionServiceExample {

    public static void main(String[] args) throws Exception {
        // 创建一个固定线程池的ExecutorService
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        // 使用ExecutorService创建一个CompletionService
        CompletionService<String> completionService = new ExecutorCompletionService<String>(executorService);

        // 准备一些任务
        List<Callable<String>> tasks = new ArrayList<Callable<String>>();
        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            tasks.add(new Callable<String>() {
                public String call() throws Exception {
                    TimeUnit.MILLISECONDS.sleep((long) (Math.random() * 1000));
                    return "Result of task " + taskId;
                }
            });
        }

        // 提交任务并处理结果
        for (Callable<String> task : tasks) {
            completionService.submit(task);
        }

        // 获取并处理完成的任务结果
        for (int i = 0; i < tasks.size(); i++) {
            Future<String> future = completionService.take(); // 阻塞直到下一个任务完成
            System.out.println(future.get()); // 打印任务结果
        }

        // 关闭ExecutorService
        executorService.shutdown();
    }
}
