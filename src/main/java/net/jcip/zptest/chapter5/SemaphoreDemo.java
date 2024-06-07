package net.jcip.zptest.chapter5;

import java.util.concurrent.Semaphore;

/**
 *  acquire 失败，会进入 semaphore 的阻塞等待队列，等待release 之后被唤醒。
 *
 *  当semaphore.acquire();失败时，线程会进行以下操作：
 *
 * 1 阻塞等待：
 * 如果Semaphore中的许可数量（即permits）为0，且没有其他线程释放许可，那么调用acquire()方法的线程将会阻塞，直到有许可可用。
 * 在阻塞期间，线程会进入Semaphore内部的阻塞队列中等待。
 * 2 响应中断：
 * 如果线程在等待许可的过程中被中断（即其他线程调用了该线程的interrupt()方法），acquire()方法将抛出InterruptedException异常。
 * 需要注意的是，这是acquire()方法默认的行为，它响应中断。但Semaphore还提供了acquireUninterruptibly()方法，该方法在获取不到许可时会一直阻塞等待，即使线程被中断也不会抛出异常。
 * 3 挂起与唤醒：
 * 当其他线程调用release()方法释放许可时，Semaphore会唤醒阻塞队列中的一个线程（通常是等待时间最长的线程，但这也取决于具体的实现和公平策略）。
 * 被唤醒的线程会重新尝试获取许可。如果此时有可用的许可，线程将获取许可并继续执行；否则，线程可能会再次进入阻塞状态。
 * 4 返回值与状态更新：
 * acquire()方法没有返回值（即返回类型为void），但它会更新Semaphore的内部状态，包括可用的许可数量。
 * 如果线程成功获取了许可，那么Semaphore中的可用许可数量将减1；如果线程被中断或由于其他原因未能获取许可，那么Semaphore的状态将保持不变。
 * 5 公平性：
 * Semaphore可以配置为公平或非公平模式。在公平模式下，等待时间最长的线程将优先获取许可；而在非公平模式下，则可能出现后来请求的线程先获取许可的情况。
 * 公平性策略在创建Semaphore时通过构造函数指定，例如Semaphore(int permits, boolean fair)。
 * 总结来说，当semaphore.acquire();失败时，线程会进入阻塞状态并等待许可变得可用。在等待期间，线程可以响应中断或被其他线程唤醒。线程的行为还受到Semaphore公平性策略的影响。
 *
 */
public class SemaphoreDemo {
    private int permit = 3;
    private final Semaphore semaphore = new Semaphore(permit);

    public void carEnters() throws InterruptedException {
        // 获取一个许可（即一个停车位）
        semaphore.acquire();
        System.out.println("Car entered the parking lot. There are " + (permit - semaphore.availablePermits()) + " cars inside.");

        // 模拟汽车停车一段时间
        Thread.sleep((long)(Math.random() * 5000));

        // 汽车离开停车场，释放许可
        semaphore.release();
        System.out.println("Car left the parking lot. There are " + (permit - semaphore.availablePermits()) + " cars inside.");


    }

    public static void main(String[] args) {
        final SemaphoreDemo parkingLot = new SemaphoreDemo();

        // 模拟多辆汽车尝试进入停车场
        for (int i = 0; i < 5; i++) {
            final int carNumber = i + 1;
            new Thread(new Runnable() {
                public void run() {
                    try {
                        System.out.println("Car " + carNumber + " is trying to enter the parking lot... begin");
                        parkingLot.carEnters();
                        System.out.println("Car " + carNumber + " is trying to enter the parking lot... success");

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        System.out.println("Car " + carNumber + " is trying to enter the parking lot... but fail");

                    }
                }
            }).start();
        }
    }
}
