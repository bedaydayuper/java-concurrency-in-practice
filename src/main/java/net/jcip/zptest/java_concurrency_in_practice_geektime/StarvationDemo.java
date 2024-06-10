package net.jcip.zptest.java_concurrency_in_practice_geektime;

public class StarvationDemo {

    private static final Object lock = new Object();

    public static void main(String[] args) {
        Thread highPriorityThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    synchronized (lock) {
                        System.out.println("high priority thread is runing");
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        highPriorityThread.setPriority(Thread.MAX_PRIORITY);

        Thread lowPriorityThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    synchronized (lock) {
                        System.out.println("high priority thread is runing");
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        lowPriorityThread.setPriority(Thread.MIN_PRIORITY);

        highPriorityThread.start();
        lowPriorityThread.start();
    }
}
