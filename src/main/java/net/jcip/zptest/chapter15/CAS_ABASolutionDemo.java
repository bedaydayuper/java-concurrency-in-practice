package net.jcip.zptest.chapter15;

import java.util.concurrent.atomic.AtomicStampedReference;

public class CAS_ABASolutionDemo {

    private static AtomicStampedReference<String> atomicStampedRef =
            new AtomicStampedReference<String>("A", 0);

    public static void main(String[] args) throws InterruptedException {
        // 工作线程，负责更改共享资源的值
        Thread workerThread = new Thread(new Runnable() {
            public void run() {
                int stamp = atomicStampedRef.getStamp();
                for (int i = 0; i < 10; i++) {
                    // 模拟ABA情况：A -> B -> A
                    if (i % 2 == 0) {
                        atomicStampedRef.set("B", stamp + 1);
                        stamp++;
                        Thread.yield(); // 让出CPU控制权，增加并发冲突的可能性
                        atomicStampedRef.set("A", stamp + 1);
                        stamp++;
                    } else {
                        atomicStampedRef.set("C", stamp + 1);
                        stamp++;
                    }
                    try {
                        Thread.sleep(100); // 暂停一段时间，以便观察
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // CAS操作线程，尝试更新值
        Thread casThread = new Thread(new Runnable() {
            public void run() {
                String expectedValue = "A";
                int expectedStamp = atomicStampedRef.getStamp();
                String newValue = "A1";
                boolean casSuccess = false;

                while (!casSuccess) {
                    casSuccess = atomicStampedRef.compareAndSet(expectedValue, newValue, expectedStamp, expectedStamp + 1);
                    if (!casSuccess) {
                        // 如果CAS失败，则重新读取当前值和邮戳
                        expectedValue = atomicStampedRef.getReference();
                        expectedStamp = atomicStampedRef.getStamp();
                        newValue = expectedValue + "1"; // 尝试设置新值
                    }
                }

                System.out.println("CAS operation successful. Thread " + Thread.currentThread().getId() + " updated value to: " + newValue);

            }
        });

        // 启动线程
        workerThread.start();
        casThread.start();

        // 等待线程结束
        workerThread.join();
        casThread.join();

        // 输出最终值
        System.out.println("Final value: " + atomicStampedRef.getReference() + " with stamp: " + atomicStampedRef.getStamp());
    }
}
