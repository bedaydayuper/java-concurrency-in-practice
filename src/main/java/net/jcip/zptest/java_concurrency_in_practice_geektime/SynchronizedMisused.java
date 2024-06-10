package net.jcip.zptest.java_concurrency_in_practice_geektime;

public class SynchronizedMisused {
    public void synchronizedMethod() {
        synchronized (this) {
            // ... 执行一些需要同步的代码 ...
            System.out.println("线程" + Thread.currentThread().getId() + "正在执行同步代码块");
            try {
                Thread.sleep(5000); // 模拟耗时操作
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程" + Thread.currentThread().getId() + " 退出同步代码块");
        }
    }


    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                SynchronizedMisused misused1 = new SynchronizedMisused();
                misused1.synchronizedMethod();
            }
        }, "thread1");

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                SynchronizedMisused misused1 = new SynchronizedMisused();
                misused1.synchronizedMethod();
            }
        }, "thread2");

        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                SynchronizedMisused misused1 = new SynchronizedMisused();
                misused1.synchronizedMethod();
            }
        }, "thread3");

        thread.start();
        thread2.start();
        thread3.start();

    }
}
