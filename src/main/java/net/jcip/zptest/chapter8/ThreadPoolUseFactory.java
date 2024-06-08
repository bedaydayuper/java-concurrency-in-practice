package net.jcip.zptest.chapter8;


import java.lang.reflect.Field;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolUseFactory {

    public static class MyThreadFactory1 implements ThreadFactory {

        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, namePrefix + threadNumber.getAndIncrement());
            t.setDaemon(false);
            t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }

        public MyThreadFactory1(String poolName) {
            namePrefix = "From-" + poolName + "-pool-thread-";
        }
    }

    public static class CustomRejectedExecutionHandler implements RejectedExecutionHandler {

        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            // 在这里实现你的自定义拒绝策略
            if (r instanceof FutureTask) {
                FutureTask<?> futureTask = (FutureTask<?>) r;
                Object taskObject = null;
                try {

                    // 第一步：通过反射获取FutureTask内部的Callable对象
                    Field callableField = FutureTask.class.getDeclaredField("callable");
                    callableField.setAccessible(true);
                    Callable<?> innerCallable = (Callable<?>) callableField.get(futureTask);

                    // 验证是否成功获取内部的Callable
                    if (innerCallable != null) {

                        // 第二步：通过反射获取Callable中的task属性
                        // 假设我们知道MyCallable类中有一个名为"task"的字段
                        Field taskField = NumberedTask.class.getDeclaredField("taskId");
                        taskField.setAccessible(true);
                        Object task = taskField.get(innerCallable);

                        if (task instanceof Integer) {
                            System.out.println("Task with ID  " + (Integer) task + " was rejected from ThreadPoolExecutor. Discarding task.");
                        } else if (task instanceof Callable) {
                            System.out.println("A callable task without ID was rejected from ThreadPoolExecutor. Discarding task.");
                        } else {
                            System.out.println("A future task wrapping an unknown type of task was rejected from ThreadPoolExecutor. Discarding task.");
                        }

                    } else {
                        System.out.println("Failed to retrieve inner Callable.");
                    }




                } catch (Exception e) {
                    System.out.println("Error accessing FutureTask's inner task");
                }

            } else {
                System.out.println("A non-future task was rejected from ThreadPoolExecutor. Discarding task.");
            }
        }
    }

    public static class NumberedTask implements Callable {
        private final int taskId;

        public NumberedTask(int taskId) {
            this.taskId = taskId;
        }

        public int getTaskId() {
            return taskId;
        }

        public void run() {
            // 执行任务的代码
            System.out.println("Executing task with ID: " + taskId);
        }

        @Override
        public String toString() {
            return "Task #" + taskId;
        }

        public Object call() throws Exception {
            // 执行任务的代码
            System.out.println("Executing task with ID: " + taskId);
            return null;
        }
    }

    public static void main(String[] args) {
        // 使用自定义的 ThreadFactory 创建 ThreadPoolExecutor
        MyThreadFactory1 factory = new MyThreadFactory1("zp");
        int corePoolSize = 5; // 核心线程数
        int maximumPoolSize = 10; // 最大线程数
        long keepAliveTime = 60L; // 空闲线程存活时间
        TimeUnit unit = TimeUnit.SECONDS; // 时间单位
        // 无解队列
//        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(); // 任务队列
        // 有界队列
        int queueCapacity = 10;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(queueCapacity);
        CustomRejectedExecutionHandler rejectedExecutionHandler = new CustomRejectedExecutionHandler();

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue,
                // 使用自定义的 ThreadFactory
                factory,
                // 拒绝策略
                rejectedExecutionHandler
        );

        // 提交任务到线程池
        for (int i = 0; i < 100; i++) {
            final int taskId = i;
            NumberedTask task = new NumberedTask(i);

            executor.submit(task);
        }

        // 关闭线程池（平滑关闭，会执行完已提交的任务）
        executor.shutdown();


        // 等待线程池中的所有任务完成
        try {
            if (!executor.awaitTermination(120, TimeUnit.SECONDS)) {
                // 等待超时后，可以选择取消所有未完成的任务
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            // 如果等待过程中被中断，也尝试关闭线程池
            executor.shutdownNow();
            Thread.currentThread().interrupt(); // 保持中断状态
        }
    }
}
