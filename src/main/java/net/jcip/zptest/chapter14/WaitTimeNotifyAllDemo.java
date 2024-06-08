package net.jcip.zptest.chapter14;

public class WaitTimeNotifyAllDemo {
    private static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread notifierThread = new Thread(new Runnable() {
            public void run() {
                try {
                    // 模拟一些准备工作
                    Thread.sleep(6000); // 等待3秒钟
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock) {
                    // 唤醒所有在此锁对象上等待的线程
                    lock.notifyAll();
                }
                System.out.println("Notifier thread notified all waiting threads.");
            }
        });

        Thread waiterThread = new Thread(new Runnable() {
            public void run() {
                synchronized (lock) {
                    try {
                        System.out.println("Waiter thread is waiting with timeout.");
                        // 等待最多5秒钟，或者直到被notify/notifyAll唤醒
                        lock.wait(5000);
                        System.out.println("Waiter thread woke up.");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        System.out.println("Waiter thread not woke up");
                    }
                }
            }
        });

        notifierThread.start();
        waiterThread.start();
        // sleep 1 秒，然后将 waiterThread 中断。
        Thread.sleep(1000);
        waiterThread.interrupt();
    }
}
