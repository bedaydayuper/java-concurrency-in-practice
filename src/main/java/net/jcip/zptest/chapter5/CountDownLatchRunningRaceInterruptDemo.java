package net.jcip.zptest.chapter5;

import java.util.concurrent.*;

/**
 * 模拟跑步，多个线程同时起跑，如果超过了规定时间，则认为不合格。
 *
 */
public class CountDownLatchRunningRaceInterruptDemo {

    public long timeTasks (int nThread, final Runnable task) throws InterruptedException {
        final CountDownLatch startLatch = new CountDownLatch(1);
        final CountDownLatch endLatch = new CountDownLatch(nThread);

        final ExecutorService executorService = Executors.newFixedThreadPool(nThread);

        for (int i = 0; i < nThread; i++) {
            executorService.submit(new Runnable() {
                public void run() {
                    try {
                        // 等发起的指令。
                        startLatch.await();
                        task.run();
                    } catch (Exception e) {
                        System.out.println("线程: " + Thread.currentThread().getName() + " 被强制退出了");

                        // 如果线程被中断，则优雅地退出
                        Thread.currentThread().interrupt(); // 保留中断状态
                        return;
                    } finally {
                        // 跑完一个。
                        endLatch.countDown();
                    }
                }
            });

        }

        long startTime = System.currentTimeMillis();
        // 发起 开始 指令。
        startLatch.countDown();

        // 使用ScheduledExecutorService来在1秒后检查, 用来模拟超过 xxx 秒的人都不合格。
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(new Runnable() {

            public void run() {
                if (endLatch.getCount() > 0) {
                    // 如果有线程还没完成，则中断它们
                    executorService.shutdownNow();
                }
            }
        }, 1, TimeUnit.SECONDS);

        // 等待所有线程完成，或者直到被中断
        try {
            endLatch.await();
        } catch (InterruptedException e) {
//            // 如果有必要，在这里可以再次调用executorService.shutdownNow()
//            executorService.shutdownNow();
//            Thread.currentThread().interrupt(); // 保留中断状态
        }

        // 关闭ExecutorService和ScheduledExecutorService
        executorService.shutdown(); // 注意：这不会立即停止正在执行的任务
        scheduler.shutdown();


        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    public static void main(String[] args) throws InterruptedException {
        CountDownLatchRunningRaceInterruptDemo demo = new CountDownLatchRunningRaceInterruptDemo();
        int nThread = 10;
        long l = demo.timeTasks(nThread, new Runnable() {
            public void run() {
                try {
                    long l1 = (long) (Math.random() * 2000);
                    System.out.println("sleep 毫秒数：" + l1);
                    Thread.sleep(l1);
                } catch (InterruptedException e) {
                    System.out.println("over time interrupt!!!!" + e.getMessage());
                }
            }
        });

        System.out.println("耗时(毫秒): " + l);
    }


}
