package net.jcip.zptest.chapter6;

import java.util.concurrent.*;

public class FutureTimeoutDemo {

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
            // 如果超时，则直接取消 future。
            stringFuture.cancel(true);
        }
    }
}
