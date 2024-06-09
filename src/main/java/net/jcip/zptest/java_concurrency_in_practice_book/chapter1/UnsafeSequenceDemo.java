package net.jcip.zptest.java_concurrency_in_practice_book.chapter1;

//@NotThreadSafe
public class UnsafeSequenceDemo {
    private int value;
//    private AtomicInteger value = new AtomicInteger(0);

    public int getValue() {
        return value++;
//        return value.getAndIncrement();
    }

    public static void main(String[] args) throws InterruptedException {
        final UnsafeSequenceDemo unsafeSequenceDemo = new UnsafeSequenceDemo();
//        final CountDownLatch latch = new CountDownLatch(2);
        Thread thread = new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i < 100000; i++) {
                    System.out.println("thread: " + unsafeSequenceDemo.getValue());
                }
//                latch.countDown(); // 计数减1
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i < 100000; i++) {
                    System.out.println("thread2: " + unsafeSequenceDemo.getValue());
                }
//                latch.countDown(); // 计数减1
            }
        });

        Thread thread3 = new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i < 100000; i++) {
                    System.out.println("thread3: " + unsafeSequenceDemo.getValue());
                }
//                latch.countDown(); // 计数减1
            }
        });

        thread.start();
        thread2.start();
        thread3.start();
//        latch.await();
        Thread.sleep(10000);
        System.out.println("end value is " + unsafeSequenceDemo.getValue());
    }
}
