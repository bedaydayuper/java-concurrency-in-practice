package net.jcip.zptest.java_concurrency_in_practice_book.chapter6;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledTaskThreadPoolDemo {


    public static void main(String[] args) {
        // 创建一个单线程的定时任务调度器
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // 创建一个Runnable任务
        Runnable task = new Runnable() {
            public void run() {
                System.out.println("定时任务执行: " + System.currentTimeMillis());
            }
        };
        System.out.println("定时任务提交: " + System.currentTimeMillis());
        // 延迟5秒后执行一次任务
        scheduler.schedule(task, 5, TimeUnit.SECONDS);

        // 注意：这里通常需要关闭调度器，以避免资源泄露
        // 但是在简单的示例中，如果程序即将结束，通常可以省略
         scheduler.shutdown();
    }
}
