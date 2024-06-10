package net.jcip.zptest.java_concurrency_in_practice_geektime;

public class HappensBeforeTransitivity {
    private int x = 0;
    private volatile boolean flag = false; // 声明一个 volatile 布尔变量

    public void writer() {
        x = 42; // x 这儿考虑的就是传递性。因为x happens-before flag. 而 reader 操作是读取 flag 之后，再去读取 x 的值。
        flag = true; // 写操作
        System.out.println("Flag is set to true.");
    }

    public void reader() {
        if (flag) { // 读操作
            System.out.println("x : " + x);
            System.out.println("Flag is true.");
        }
    }

    public static void main(String[] args) {
        HappensBeforeTransitivity example = new HappensBeforeTransitivity();

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
