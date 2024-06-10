package net.jcip.zptest.java_concurrency_in_practice_geektime;

public class DeadLockDemo {
    private final Object lock1 = new Object();
    private final Object lock2 = new Object();

    public void thread1() throws InterruptedException {
        synchronized (lock1) {
            System.out.println("thread1 : holding lock 1");

            Thread.sleep(100);

            synchronized (lock2) {
                System.out.println("thread1: holding lock 1 & 2");
            }
        }
    }

    public void thread2() throws InterruptedException {
        synchronized (lock2) {
            System.out.println("thread2 : holding lock 2");

            Thread.sleep(100);

            synchronized (lock1) {
                System.out.println("thread2: holding lock 1 & 2");
            }
        }
    }

    public static void main(String[] args) {
        DeadLockDemo demo = new DeadLockDemo();
        new Thread(() -> {
            try {
                demo.thread1();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                demo.thread2();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
