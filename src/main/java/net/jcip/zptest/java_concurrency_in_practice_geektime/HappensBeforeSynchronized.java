package net.jcip.zptest.java_concurrency_in_practice_geektime;

public class HappensBeforeSynchronized {
    private final Object lock = new Object();
    private int counter = 0;

    public void increment() {
        synchronized (lock) { // 获取监视器锁
            counter++;
            System.out.println("Counter incremented to: " + counter);
        } // 释放监视器锁
    }

    public int getCounter() {
        synchronized (lock) { // 获取监视器锁
            return counter;
        } // 释放监视器锁在这里发生，但实际上锁在方法结束时自动释放
    }

    public static void main(String[] args) {
        HappensBeforeSynchronized example = new HappensBeforeSynchronized();

        // 模拟两个线程同时操作counter
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                example.increment();
                try {
                    Thread.sleep(100); // 模拟一些延时
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("Thread 2 sees counter: " + example.getCounter());
                try {
                    Thread.sleep(150); // 模拟一些延时
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread1.start();
        thread2.start();
    }
}
