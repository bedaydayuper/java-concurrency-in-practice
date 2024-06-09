package net.jcip.zptest.java_concurrency_in_practice_book.chapter15;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class CASWithSpinLimitDemo {
    private static final AtomicInteger atomicInt = new AtomicInteger(0);
    private static final int MAX_SPIN_ATTEMPTS = 1; // 设置最大自旋尝试次数
    private static final int NUM_RUNNERS = 100;

    public static void main(String[] args) {

        final CyclicBarrier cyclicBarrier = new CyclicBarrier(NUM_RUNNERS, new Runnable() {
            public void run() {
                System.out.println("所有选手已就绪，比赛开始！");
            }
        });

        Runnable task = new Runnable() {
            public void run() {
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                int oldValue = atomicInt.get(); // 获取当前值
                int newValue = oldValue + 1; // 设置新值
                int spinCount = 0; // 自旋计数器
                boolean success = false;

                while (!success && spinCount < MAX_SPIN_ATTEMPTS) {
                    // 尝试CAS操作
                    System.out.println(Thread.currentThread().getId() + " spinCount: " + spinCount);

                    success = atomicInt.compareAndSet(oldValue, newValue);
                    if (!success) {
                        // 如果CAS失败，重新读取当前值，并增加自旋计数器
                        System.out.println(Thread.currentThread().getId() + " cas fail");
                        oldValue = atomicInt.get();
                        newValue = oldValue + 1;
                        spinCount++;
                        // 可以选择性地添加延迟，以减少CPU占用
//                         Thread.yield(); // 或者使用LockSupport.parkNanos(1L); 进行短暂停顿
                    }
                }

                if (success) {
                    System.out.println("Thread " + Thread.currentThread().getId() + " successfully incremented the value to " + newValue);
                } else {
                    System.out.println("Thread " + Thread.currentThread().getId() + " failed to increment the value after " + MAX_SPIN_ATTEMPTS + " attempts.");
                }
            }
        };

        // 创建并启动多个线程来模拟并发更新
        for (int i = 0; i < NUM_RUNNERS; i++) {
            new Thread(task).start();
        }

    }
}
