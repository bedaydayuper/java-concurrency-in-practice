package net.jcip.zptest.java_concurrency_in_practice_book.chapter5;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 模拟跑步，多个线程同时起跑，如果超过了规定时间，则认为不合格。
 * 子线程不使用线程池-直接使用 List 存储子线程。
 *
 */
public class CountDownLatchRunningRaceInterruptNoThreadPoolDemo {

    public long timeTasks (int nThread, final Runnable task) throws InterruptedException {
        final CountDownLatch startLatch = new CountDownLatch(1);
        final CountDownLatch endLatch = new CountDownLatch(nThread);

        final List<Thread> threadList = new LinkedList<Thread>();

        for (int i = 0; i < nThread; i++) {
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    try {
                        // 等发起的指令。
                        startLatch.await();
                        task.run();
                        System.out.println("线程：" + Thread.currentThread().getName() + " 运行完毕");
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
            }, "thread-" + i);
            threadList.add(thread);
            thread.start();
        }

        long startTime = System.currentTimeMillis();
        // 发起 开始 指令。
        startLatch.countDown();

        // 使用ScheduledExecutorService来在1秒后检查, 用来模拟超过 xxx 秒的人都不合格。
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(new Runnable() {

            public void run() {
                for (Thread thread : threadList) {
                    if (thread.isAlive()) {
                        thread.interrupt();
                        System.out.println("Interrupting task thread. id: " + thread.getName());
                    }
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

        // 关闭ScheduledExecutorService
        scheduler.shutdown();


        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    public static void main(String[] args) throws InterruptedException {
        CountDownLatchRunningRaceInterruptNoThreadPoolDemo demo = new CountDownLatchRunningRaceInterruptNoThreadPoolDemo();
        int nThread = 10;
        long l = demo.timeTasks(nThread, new Runnable() {
            public void run() {
                try {
                    long l1 = (long) (Math.random() * 2000);
                    System.out.println("sleep 毫秒数：" + l1);
                    Thread.sleep(l1);
                    System.out.println("sleep 毫秒数：" + l1 + "end");
                } catch (InterruptedException e) {
                    System.out.println("over time interrupt!!!!" + e.getMessage());
                }
            }
        });

        System.out.println("耗时(毫秒): " + l);
    }


}
