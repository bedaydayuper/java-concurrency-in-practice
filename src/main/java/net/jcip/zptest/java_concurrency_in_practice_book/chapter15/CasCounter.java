package net.jcip.zptest.java_concurrency_in_practice_book.chapter15;

import net.jcip.annotations.GuardedBy;

public class CasCounter {

    private SimulatedCAS vv;

    CasCounter(int v) {
        vv = new SimulatedCAS(v);
    }

    public int getValue() {
        return vv.get();
    }

    public int increment() {
        int v;
        do {
            v = vv.get();
        } while (v != vv.compareAndSwap(v, v + 1));
        return v + 1;
    }

    public static void main(String[] args) throws InterruptedException {

        final CasCounter casCounter = new CasCounter(0);

        Thread thread1 = new Thread(new Runnable() {
            public void run() {
                int i = 0;
                while (i < 10000) {
                    casCounter.increment();
                    ++i;
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            public void run() {
                int i = 0;
                while (i < 10000) {
                    casCounter.increment();
                    ++i;
                }
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
        System.out.println(casCounter.getValue());


    }

    private class SimulatedCAS {
        @GuardedBy("this") private int value;

        SimulatedCAS(int value) {
            this.value = value;
        }

        public synchronized int get() {
            return value;
        }

        public synchronized int compareAndSwap(int expectedValue,
                                               int newValue) {
            int oldValue = value;
            if (oldValue == expectedValue)
                value = newValue;
            return oldValue;
        }

        public synchronized boolean compareAndSet(int expectedValue,
                                                  int newValue) {
            return (expectedValue
                    == compareAndSwap(expectedValue, newValue));
        }
    }
}
