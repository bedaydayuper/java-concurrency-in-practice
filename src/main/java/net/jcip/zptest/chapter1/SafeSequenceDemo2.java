package net.jcip.zptest.chapter1;

//@NotThreadSafe
public class SafeSequenceDemo2 {
    private int value;
//    private AtomicInteger value = new AtomicInteger(0);

    public synchronized int getValue() {
        return value++;
//        return value.getAndIncrement();
    }

    public static void main(String[] args) throws InterruptedException {
        final SafeSequenceDemo2 safeSequenceDemo = new SafeSequenceDemo2();
//        final CountDownLatch latch = new CountDownLatch(2);
        Thread thread = new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i < 100000; i++) {
                    System.out.println("thread: " + safeSequenceDemo.getValue());
                }
//                latch.countDown(); // 计数减1
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i < 100000; i++) {
                    System.out.println("thread2: " + safeSequenceDemo.getValue());
                }
//                latch.countDown(); // 计数减1
            }
        });

        Thread thread3 = new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i < 100000; i++) {
                    System.out.println("thread3: " + safeSequenceDemo.getValue());
                }
//                latch.countDown(); // 计数减1
            }
        });

        thread.start();
        thread2.start();
        thread3.start();
//        latch.await();
        Thread.sleep(10000);
        System.out.println("end value is " + safeSequenceDemo.getValue());
    }
}
