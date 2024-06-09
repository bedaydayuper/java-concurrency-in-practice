package net.jcip.zptest.java_concurrency_in_practice_book.chapter13;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTimeoutDemo {

    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        Thread thread1 = new Thread(new Task("Thread-1"));
        Thread thread2 = new Thread(new Task("Thread-2"));

        thread1.start();
        // 稍微延迟启动第二个线程，以便第一个线程有机会先获取锁
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread2.start();
    }

    static class Task implements Runnable {
        private final String name;

        Task(String name) {
            this.name = name;
        }

        public void run() {
            try {
                // 尝试在5秒内获取锁
                if (lock.tryLock(5, TimeUnit.SECONDS)) {
                    try {
                        System.out.println(name + " acquired the lock.");
                        // 模拟一些工作
                        TimeUnit.SECONDS.sleep(10);
                        System.out.println(name + " finished its work.");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                        System.out.println(name + " released the lock.");
                    }
                } else {
                    System.out.println(name + " failed to acquire the lock within 5 seconds.");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(name + " was interrupted while waiting for the lock.");
            }
        }
    }
}
