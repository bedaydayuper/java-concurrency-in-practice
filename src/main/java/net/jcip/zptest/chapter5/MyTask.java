package net.jcip.zptest.chapter5;

import java.util.concurrent.Callable;

public class MyTask implements Callable<String> {
    private final int taskId;

    public MyTask(int taskId) {
        this.taskId = taskId;
    }

    public String call() throws Exception {
        // 模拟长时间运行的任务
        Thread.sleep((long) (Math.random() * 5000));

        return "Result of task " + taskId + ", current thread: " + Thread.currentThread().getName();
    }
}





