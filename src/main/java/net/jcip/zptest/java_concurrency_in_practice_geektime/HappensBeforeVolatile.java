package net.jcip.zptest.java_concurrency_in_practice_geektime;

public class HappensBeforeVolatile {
    private volatile boolean flag = false; // 声明一个 volatile 布尔变量

    public void writer() {
        flag = true; // 写操作
        System.out.println("Flag is set to true.");
    }

    public void reader() {
        if (flag) { // 读操作
            System.out.println("Flag is true.");
        }
    }

    public static void main(String[] args) {
        HappensBeforeVolatile example = new HappensBeforeVolatile();

        Thread writerThread = new Thread(() -> example.writer());
        Thread readerThread = new Thread(() -> example.reader());

        writerThread.start();
        try {
            // 等待写线程完成
            writerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        readerThread.start();
    }
}
