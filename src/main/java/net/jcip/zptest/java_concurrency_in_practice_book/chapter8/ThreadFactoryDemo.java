package net.jcip.zptest.java_concurrency_in_practice_book.chapter8;


import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadFactoryDemo {

    public static class MyThreadFactory1 implements ThreadFactory {

        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, namePrefix + threadNumber.getAndIncrement());
            t.setDaemon(false);
            t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }

        public MyThreadFactory1(String poolName) {
            namePrefix = "From-" + poolName + "-pool-thread-";
        }
    }

    public static void main(String[] args) {
        MyThreadFactory1 myThreadFactory1 = new MyThreadFactory1("zp");
        Thread thread = myThreadFactory1.newThread(new Runnable() {
            public void run() {
                System.out.println("use factory create thread");
            }
        });

        System.out.println("thread name : " + thread.getName());
        thread.start();
    }
}
