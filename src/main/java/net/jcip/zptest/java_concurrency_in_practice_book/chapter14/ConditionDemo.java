package net.jcip.zptest.java_concurrency_in_practice_book.chapter14;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionDemo {
    private static final int BUFFER_SIZE = 10;
    ReentrantLock lock = new ReentrantLock(false);

    private final Condition notNull = lock.newCondition();

    private final Condition notEmpty = lock.newCondition();

    private final String[] item = new String[BUFFER_SIZE];

    private int tail, head, count;

    public void put(String x) throws InterruptedException {
        lock.lock();
        try {
            // 满了，一直等
            while (count == item.length) {
                System.out.println("put wait....");
                notNull.await();
            }
            item[tail] = x;
            // 循环使用。
            if (++tail == item.length) {
                tail = 0;
            }
            ++count;
            // 唤醒空的 condition。
            notEmpty.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public String take() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0) {
                System.out.println("take wait....");
                notEmpty.await();
            }
            String x = item[head];
            item[head] = null;
            if (++head == item.length) {
                head = 0;
            }
            --count;
            notNull.signalAll();
            return x;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return "";
    }

    public static void main(String[] args) {
        final ConditionDemo conditionDemo = new ConditionDemo();
        Thread putThread = new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i < 100; i++) {
                    try {
                        String tmp = "" + i;
                        conditionDemo.put(tmp);
                        System.out.println("put result: " + tmp);
                        Thread.sleep((long) (Math.random() * 1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Thread takeThread = new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i < 100; i++) {
                    try {
                        String take = conditionDemo.take();
                        System.out.println("take result: " + take);

                        Thread.sleep((long) (Math.random() * 2000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        putThread.start();
        takeThread.start();
    }
}
