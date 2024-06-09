package net.jcip.zptest.java_concurrency_in_practice_book.chapter15;

import java.util.concurrent.atomic.AtomicReference;

public class CAS_ABA_Demo {
    private static AtomicReference<String> atomicReference = new AtomicReference<String>("A");

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                try {
                    for (int i = 0; i < 100; i++) {
                        // 模拟其他线程将值从A改为B，再改回A的过程
                        System.out.println("t1 change " + i);
                        atomicReference.set("B");
                        Thread.sleep((long) (Math.random() * 100)); // 暂停一段时间，让主线程有机会进行CAS操作
                        atomicReference.set("A");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Thread mainThread = new Thread(new Runnable() {
            public void run() {
                boolean casSuccess = false;
                String oldValue = atomicReference.get(); // 获取当前值，期望是A
                String newValue = oldValue + "1"; // 新值设为A1

                // 模拟CAS操作，尝试将值从A更改为A1
                while (!casSuccess) {
                    System.out.println("cas fail, try again~~~~~~~~");
                    // 如果当前值还是oldValue（即A），则尝试将其设置为newValue（即A1）
                    casSuccess = atomicReference.compareAndSet(oldValue, newValue);
                    if (!casSuccess) {
                        // 如果CAS失败，重新获取当前值，并准备进行下一次尝试
                        System.out.println("cas fail, try again~~~~~~~~");
                        oldValue = atomicReference.get();
                        newValue = oldValue + "1"; // 根据新的oldValue设置newValue
                    } else {
                        System.out.println("cas success~~~~~");
                    }
                }

                System.out.println("CAS operation successful. New value: " + atomicReference.get());
            }
        });

        t1.start(); // 启动模拟线程
        Thread.sleep(50); // 暂停一段时间，确保模拟线程有机会先运行
        mainThread.start(); // 启动主线程进行CAS操作

        t1.join();
        mainThread.join();

        System.out.println("Final value: " + atomicReference.get()); // 输出最终值，可能是A或A1，取决于线程的执行顺序和时机
    }
}
