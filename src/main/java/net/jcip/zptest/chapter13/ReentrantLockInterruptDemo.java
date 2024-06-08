package net.jcip.zptest.chapter13;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockInterruptDemo {

    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(new Task("Thread-1"), "Thread-1");
        Thread thread2 = new Thread(new Task("Thread-2"), "Thread-2");

        // 启动第一个线程，它将尝试获取锁并执行任务
        thread1.start();

        // 稍微等待以确保thread1已经开始运行并获取了锁
        Thread.sleep(1000);

        // 启动第二个线程，它也将尝试获取锁
        thread2.start();

        // 让第二个线程运行一会儿，确保它在等待锁
        Thread.sleep(1000);

        // 中断第二个线程，此时它应该正在等待锁
        thread2.interrupt();

        // 等待两个线程都结束。
        thread1.join();
        thread2.join();
    }

    static class Task implements Runnable {
        private final String name;

        Task(String name) {
            this.name = name;
        }

        public void run() {
            try {
                System.out.println(name + " is trying to acquire the lock.");
                // 尝试以可中断的方式获取锁
                lock.lockInterruptibly();
                try {
                    System.out.println(name + " acquired the lock.");
                    // 模拟任务执行时间
                    Thread.sleep(5000);
                    System.out.println(name + " finished its task.");
                } catch (InterruptedException e) {
                    System.out.println(name + " was interrupted while waiting for or holding the lock.");
                } finally {
                    lock.unlock();
                    System.out.println(name + " released the lock.");
                }
            } catch (InterruptedException e) {
                // 如果线程在等待锁时被中断，会进入这个catch块
                System.out.println(name + " was interrupted before acquiring the lock.");
            }
        }
    }
}
