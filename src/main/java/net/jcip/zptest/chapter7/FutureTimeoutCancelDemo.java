package net.jcip.zptest.chapter7;

import java.util.concurrent.*;

public class FutureTimeoutCancelDemo {

    private static final long TIMEOUT_BUDGET = 5000;

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Callable<String> callable = new Callable<String>() {
            public String call() throws Exception {
                Thread.sleep((long) (Math.random() * 100000));
                return "test callable timeout";
            }
        };
        long endNanos = System.currentTimeMillis() + TIMEOUT_BUDGET;

        Future<String> stringFuture = executorService.submit(callable);

        long timeLeft = endNanos - System.currentTimeMillis();
        try {
            String s = stringFuture.get(timeLeft, TimeUnit.MILLISECONDS);
            System.out.println(s);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            System.out.println("timeout !!!");

        } finally {
            // 不再需要这个 future， 可以直接 cancel 掉。
            stringFuture.cancel(true);
        }
    }
}
