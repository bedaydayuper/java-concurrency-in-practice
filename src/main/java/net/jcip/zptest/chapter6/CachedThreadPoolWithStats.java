package net.jcip.zptest.chapter6;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CachedThreadPoolWithStats {

    private final ThreadPoolExecutor executor;

    public CachedThreadPoolWithStats() {
        // 创建一个带有自定义线程工厂的CachedThreadPool
        ThreadFactory threadFactory = new ThreadFactory() {
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, "cached-thread-" + Thread.currentThread().getId());
                t.setDaemon(true); // 缓存线程池默认是守护线程
                return t;
            }
        };

        executor = (ThreadPoolExecutor) Executors.newCachedThreadPool(threadFactory);
    }

    // 获取当前活跃线程数（正在执行任务的线程）
    public int getActiveThreadCount() {
        return executor.getActiveCount();
    }

    // 获取线程池当前线程数（包括空闲线程）
    // 注意：这通常不准确，因为CachedThreadPool会动态调整线程数量
    public int getPoolSize() {
        return executor.getPoolSize();
    }

    // 获取线程池的任务队列大小
    public int getQueueSize() {
        return executor.getQueue().size();
    }

    // 提交任务到线程池
    public void submitTasks(int taskCount) {
        for (int i = 0; i < taskCount; i++) {
            executor.execute(new Runnable() {
                public void run() {
                    // 模拟任务执行
                    try {
                        Thread.sleep((long) (Math.random() * 2000));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }
    }

    // 示例方法，打印线程池状态
    public void printStats() {
        System.out.println("Active Threads: " + getActiveThreadCount());
        System.out.println("Pool Size: " + getPoolSize());
        System.out.println("Queue Size: " + getQueueSize());
        System.out.println("=====");
    }

    // 示例：主方法
    public static void main(String[] args) throws InterruptedException {
        CachedThreadPoolWithStats pool = new CachedThreadPoolWithStats();

        // 提交一些任务
        pool.submitTasks(100);

        // 等待一段时间让线程池有机会回收线程


        // 打印线程池状态
        int i = 0;
        while (i < 1000) {
            pool.printStats();
            Thread.sleep(1000);
            i++;
        }

        // 优雅地关闭线程池
        pool.executor.shutdown();

        // 等待线程池中的任务完成
        if (!pool.executor.awaitTermination(60, TimeUnit.SECONDS)) {
            pool.executor.shutdownNow(); // 强制关闭线程池
        }
    }
}
