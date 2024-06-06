package net.jcip.zptest.chapter2;

import java.util.concurrent.atomic.AtomicInteger;

public class SynchronizedTestObjectNotSafe {
    public AtomicInteger count = new AtomicInteger(0);

    public static class DataHolder {
        private int data;

        public synchronized void setData(int data) {
            this.data = data;
            System.out.println(Thread.currentThread().getName() + " setData: " + data);
            try {
                Thread.sleep(1000); // 模拟耗时操作
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " setData: " + data + " end");

        }

        public synchronized int getData() {
            System.out.println(Thread.currentThread().getName() + " getData: " + data);
            return data;
        }
    }

    public static void main(String[] args) {
        // 创建两个不同的DataHolder对象
        final DataHolder holder1 = new DataHolder();
        final DataHolder holder2 = new DataHolder();

        // 启动两个线程，每个线程操作一个不同的DataHolder对象
        Thread thread1 = new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i < 5; i++) {
                    holder1.setData(i);
                    System.out.println(holder1.getData());
                }
            }
        }, "thread1");

        Thread thread2 = new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i < 5; i++) {
                    holder2.setData(i + 10);
                    System.out.println(holder2.getData());
                }
            }
        }, "thread2");

        // 启动线程
        thread1.start();
        thread2.start();
    }
}
