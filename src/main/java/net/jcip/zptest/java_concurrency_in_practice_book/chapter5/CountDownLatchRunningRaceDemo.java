package net.jcip.zptest.java_concurrency_in_practice_book.chapter5;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchRunningRaceDemo {

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
            endLatch.await();
            // 下面是主线程 只等待 1秒。
//            endLatch.await(1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    public static void main(String[] args) {
        CountDownLatchRunningRaceDemo demo = new CountDownLatchRunningRaceDemo();
        int nThread = 10;
        long l = demo.timeTasks(nThread, new Runnable() {
            public void run() {
                try {
                    long l1 = (long) (Math.random() * 5000);
                    System.out.println("sleep 毫秒数：" + l1);
                    Thread.sleep(l1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        System.out.println("耗时(毫秒): " + l);
    }


}
