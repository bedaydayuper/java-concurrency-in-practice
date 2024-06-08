package net.jcip.zptest.chapter13;

/**
 * Thread join 的用法。当前线程，要等到另外一个线程运行完毕才可以继续执行。
 *
 */
public class ThreadJoinDemo {

    public static void main(String[] args) {
        final Thread t1 = new Thread(new Runnable() {
            public void run() {
                System.out.println("Thread 1 is running.");
                try {
                    Thread.sleep(2000); // 模拟耗时操作
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Thread 1 finished.");
            }
        });

        Thread t2 = new Thread(new Runnable() {
            public void run() {
                System.out.println("Thread 2 is waiting for Thread 1 to finish.");
                try {
                    t1.join(); // t2 等待 t1 完成
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Thread 2 continues after Thread 1 has finished.");
            }
        });

        t2.start();
        t1.start();
    }
}
