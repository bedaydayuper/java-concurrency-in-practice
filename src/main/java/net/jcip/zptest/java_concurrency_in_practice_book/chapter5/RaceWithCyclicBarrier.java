package net.jcip.zptest.java_concurrency_in_practice_book.chapter5;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class RaceWithCyclicBarrier {
    private static final int NUM_RUNNERS = 5; // 假设有5名选手

    public static void main(String[] args) {
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(NUM_RUNNERS, new Runnable() {
            public void run() {
                System.out.println("所有选手已就绪，比赛开始！");
            }
        });

        for (int i = 1; i <= NUM_RUNNERS; i++) {
            final int runnerNumber = i;
            new Thread(new Runnable() {
                public void run() {
                    System.out.println("选手 " + runnerNumber + " 已到达起跑线并准备就绪");
                    try {
                        // 等待其他选手就绪
                        cyclicBarrier.await();
                        // 如果到达这里，说明所有选手都已就绪，比赛可以开始
                        System.out.println("选手 " + runnerNumber + " 开始比赛！");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
