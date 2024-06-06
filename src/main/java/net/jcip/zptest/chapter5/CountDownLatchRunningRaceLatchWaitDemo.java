package net.jcip.zptest.chapter5;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 使用 countDownLatch 的 await , 模拟超时之后，主线程直接走后续处理了。
 * 不使用线程池。
 */
public class CountDownLatchRunningRaceLatchWaitDemo {

    public long timeTasks (int nThread, final Runnable task) {
        final CountDownLatch startLatch = new CountDownLatch(1);
        final CountDownLatch endLatch = new CountDownLatch(nThread);
        for (int i = 0; i < nThread; i++) {
            Thread thread = new Thread() {

                @Override
                public void run() {
                    try {
                        // 等发起的指令。
                        startLatch.await();
                        task.run();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        // 跑完一个。
                        endLatch.countDown();
                    }
                    super.run();
                }
            };
            thread.start();

        }

        long startTime = System.currentTimeMillis();
        // 发起 开始 指令。
        startLatch.countDown();

        try {
            // 等待所有人都结束。
//            endLatch.await();
            // 下面是主线程 只等待 1秒。
            boolean pass = endLatch.await(1000, TimeUnit.MILLISECONDS);
            // 超时的情况，需要处理子线程。
            if (!pass) {
                // 如果有线程仍在运行，您可能想要中断它们
                for (Thread thread : Thread.getAllStackTraces().keySet()) {
                    if (thread.getState() != Thread.State.TERMINATED) {
                        thread.interrupt();
                    }
                }
                // 或者您可能只想简单地记录超时事件
                System.out.println("有线程未在指定时间内完成，超时发生了。");
            }
        } catch (Exception e) {
            System.out.println("主线程抛异常了。" + e);
        }
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    public static void main(String[] args) {
        CountDownLatchRunningRaceLatchWaitDemo demo = new CountDownLatchRunningRaceLatchWaitDemo();
        int nThread = 10;
        long l = demo.timeTasks(nThread, new Runnable() {
            public void run() {
                try {
                    long l1 = (long) (Math.random() * 10000);
                    System.out.println("sleep 毫秒数：" + l1);
                    Thread.sleep(l1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        System.out.println("耗时(毫秒): " + l);
        System.out.println("直接走后续逻辑处理");
    }


}
