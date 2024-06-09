package net.jcip.zptest.java_concurrency_in_practice_book.chapter6;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class ScheduledTaskThreadPoolFixedRateDemo {

    public static void main(String[] args) {
        // 创建一个单线程的定时任务调度器
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // 创建一个Runnable任务
        Runnable task = new Runnable() {
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("固定频率执行任务: " + System.currentTimeMillis());
            }
        };

        // 初始延迟为0秒，之后每隔2秒执行一次任务
        /**
         * 当你使用scheduleAtFixedRate时，
         * 如果任务的执行时间超过了设定的周期，那么下一个任务会在前一个任务执行完毕后立即开始，而不会等待完整的周期时间。
         * 这可能导致任务的执行频率比预期的要高。
         *
         * 如果你希望任务按照固定的周期时间间隔执行，无论任务的执行时间如何，那么应该使用scheduleWithFixedDelay方法。
         *
         * 参数：
         * 要执行的任务（实现了Runnable接口的对象）
         * 初始延迟（在首次执行任务之前等待的时间）
         * 周期（在连续执行任务之间的时间间隔）
         * 时间单位（用于初始延迟和周期的时间单位，例如TimeUnit.SECONDS）
         */
        // 如果任务的执行时间超过了设定的周期，那么下一个任务会在前一个任务执行完毕后立即开始，而不会等待完整的周期时间。
//        scheduler.scheduleAtFixedRate(task, 0, 2, TimeUnit.SECONDS);
        // 如果你希望任务按照固定的周期时间间隔执行，无论任务的执行时间如何，那么应该使用scheduleWithFixedDelay方法。
        scheduler.scheduleWithFixedDelay(task, 0, 2, TimeUnit.SECONDS);

        // 在实际应用中，你需要在程序结束时关闭调度器
        // 可以通过调用 scheduler.shutdown() 来实现
//        scheduler.shutdown();
    }
}
