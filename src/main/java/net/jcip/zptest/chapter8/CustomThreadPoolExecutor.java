package net.jcip.zptest.chapter8;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

public class CustomThreadPoolExecutor implements ExecutorService {

    private final ExecutorService delegate;

    public CustomThreadPoolExecutor(ExecutorService delegate) {
        this.delegate = delegate;
    }

    public void execute(final Runnable command) {
        Runnable wrappedCommand = new Runnable() {
            public void run() {
                beforeExecution();
                try {
                    command.run();
                } finally {
                    afterExecution();
                }
            }
        };
        delegate.execute(wrappedCommand);
    }

    private void beforeExecution() {
        // 在这里添加 beforeExecution 的逻辑
        System.out.println("Before execution");
    }

    private void afterExecution() {
        // 在这里添加 afterExecution 的逻辑
        System.out.println("After execution");
    }


    public void shutdown() {

    }

    public List<Runnable> shutdownNow() {
        return null;
    }

    public boolean isShutdown() {
        return false;
    }

    public boolean isTerminated() {
        return false;
    }

    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return false;
    }

    public <T> Future<T> submit(Callable<T> task) {
        return null;
    }

    public <T> Future<T> submit(Runnable task, T result) {
        return null;
    }

    public Future<?> submit(Runnable task) {
        return null;
    }

    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return null;
    }

    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return null;
    }

    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return null;
    }

    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        CustomThreadPoolExecutor customExecutor = new CustomThreadPoolExecutor(executorService);
        customExecutor.execute(new Runnable() {
            public void run() {
                System.out.println("Running task");
            }
        });
        customExecutor.shutdown(); // 不要忘记关闭执行器
    }


}
