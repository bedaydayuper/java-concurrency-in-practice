package net.jcip.zptest.java_concurrency_in_practice_geektime;

public class SynchronizedClassAndThisMisused {
    public void synchronizedMethod() {
        // 这儿用了 this 对象。
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

    public void synchronizedMethodWithClass() {
        // 这儿用了类对象。
        synchronized (SynchronizedClassAndThisMisused.class) {
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
                SynchronizedClassAndThisMisused misused1 = new SynchronizedClassAndThisMisused();
                misused1.synchronizedMethod();
            }
        }, "thread1");

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                SynchronizedClassAndThisMisused misused1 = new SynchronizedClassAndThisMisused();
                misused1.synchronizedMethodWithClass();
            }
        }, "thread2");

        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                SynchronizedClassAndThisMisused misused1 = new SynchronizedClassAndThisMisused();
                misused1.synchronizedMethodWithClass();
            }
        }, "thread3");

        thread.start();
        thread2.start();
        thread3.start();

    }
}
