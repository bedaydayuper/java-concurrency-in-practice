package net.jcip.zptest.java_concurrency_in_practice_book.chapter5;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class FutureDemo {

    public static void main(String[] args) throws ExecutionException {
        // 设定线程池大小固定为4个。
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        List<Future<String>> futureList = new ArrayList<Future<String>>();

        // 总体任务 有 10个。
        for (int i = 0; i < 10; i++) {
            Future<String> future = executorService.submit(new MyTask(i));
            futureList.add(future);
        }

        // 获取并打印每个任务的结果
        for (Future<String> future : futureList) {
            try {
                // 一直等待。
//                String resultAllTime = future.get();
//                System.out.println(resultAllTime);

                // get() 方法会阻塞，等待指定时间，超时则抛异常。
                String result = future.get(1, TimeUnit.SECONDS);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        System.out.println("超时之后，主线程继续执行。。。");

        // 关闭线程池
        executorService.shutdown();
    }
}
