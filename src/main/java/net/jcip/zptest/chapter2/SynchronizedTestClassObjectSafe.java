package net.jcip.zptest.chapter2;

import java.util.concurrent.atomic.AtomicInteger;

public class SynchronizedTestClassObjectSafe {
    // 这是一个内部类，用于封装数据和同步方法
    public static class DataHolder {
        private int data;
        // 定义一个共享的锁对象
        private final Object lock = new Object();

        // 使用共享的锁对象进行同步
        public void setData(int data) {
            synchronized (DataHolder.class) {
                this.data = data;
                System.out.println(Thread.currentThread().getName() + " setData: " + data);
                try {
                    Thread.sleep(1000); // 模拟耗时操作
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " setData: " + data  + " end");

            }
        }

        // 使用共享的锁对象进行同步
        public int getData() {
            synchronized (DataHolder.class) {
                System.out.println(Thread.currentThread().getName() + " getData: " + data);
                return data;
            }
        }
    }

    public static void main(String[] args) {
        // 创建一个DataHolder对象，现在所有线程将共用这个对象的锁
        final DataHolder holder = new DataHolder();
        final DataHolder holder2 = new DataHolder();

        // 启动两个线程，它们都将使用holder的锁
        Thread thread1 = new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i < 5; i++) {
                    holder.setData(i);
                    System.out.println(holder.getData());
                }
            }
        }, "Thread-1");

        Thread thread2 = new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i < 5; i++) {
                    holder2.setData(i + 10);
                    System.out.println(holder2.getData());
                }
            }
        }, "Thread-2");

        // 启动线程
        thread1.start();
        thread2.start();
    }
}
