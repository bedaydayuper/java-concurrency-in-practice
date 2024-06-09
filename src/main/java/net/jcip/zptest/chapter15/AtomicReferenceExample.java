package net.jcip.zptest.chapter15;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceExample {
    private static AtomicReference<DataHolder> atomicReference = new AtomicReference<DataHolder>(new DataHolder(0, 0L));

    public static void main(String[] args) throws InterruptedException {
        // 模拟两个线程并发更新DataHolder对象
        Runnable task = new Runnable() {
            public void run() {
                for (int i = 0; i < 5; i++) {
                    updateDataHolder(1, 1L);
                }
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Final DataHolder: " + atomicReference.get());
    }

    private static void updateDataHolder(int newValue1, long newValue2) {
        DataHolder oldDataHolder;
        DataHolder newDataHolder;
        do {
            oldDataHolder = atomicReference.get();
            newDataHolder = new DataHolder(oldDataHolder.value1 + newValue1, oldDataHolder.value2 + newValue2);
            // 使用CAS操作原子地更新DataHolder对象的引用
        } while (!atomicReference.compareAndSet(oldDataHolder, newDataHolder));
    }

    static class DataHolder {
        int value1;
        long value2;

        public DataHolder(int value1, long value2) {
            this.value1 = value1;
            this.value2 = value2;
        }

        @Override
        public String toString() {
            return "DataHolder{" +
                    "value1=" + value1 +
                    ", value2=" + value2 +
                    '}';
        }
    }

}
