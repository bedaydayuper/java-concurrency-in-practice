package net.jcip.zptest.chapter6;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ExecutorPoolDemo {
    private static final int NTHREADS = 16;

    /**
     * 线程池
     *
     * Runnable 跟 callable的区别：
     *
     * Runnable和Callable在Java中都是用于多线程编程的接口，但它们之间存在一些关键的区别。以下是它们之间的主要区别：
     *
     * 方法定义：
     * Runnable接口中定义了一个run()方法，该方法没有返回值（返回类型为void），也不允许抛出受检异常（checked exception）。
     * Callable接口中定义了一个call()方法，该方法可以返回一个值，并且允许抛出受检异常。这个返回值可以通过泛型来指定。
     * 返回值：
     * Runnable的run()方法没有返回值。
     * Callable的call()方法必须有一个返回值，并且这个返回值可以通过Future对象来获取。
     * 异常处理：
     * 在Runnable中，如果run()方法抛出了异常，那么这个异常需要在实现Runnable接口的类中自行处理，或者声明为运行时异常（unchecked exception）。由于run()方法不允许抛出受检异常，因此无法直接传递这些异常给调用者。
     * 在Callable中，如果call()方法抛出了异常，那么这个异常可以被捕获并由调用者通过Future对象的get()方法来处理。get()方法会抛出ExecutionException，这个异常封装了call()方法抛出的原始异常。
     * 使用方式：
     * Runnable通常与Thread类一起使用，通过继承Thread类或者实现Runnable接口来创建线程。Runnable实例可以传递给Thread类的构造函数，然后由Thread类的start()方法来启动线程并执行run()方法。
     * Callable通常与ExecutorService一起使用，通过ExecutorService的submit()方法来提交Callable任务。submit()方法会返回一个Future对象，该对象表示异步计算的结果。调用者可以通过Future对象的get()方法来获取Callable任务的返回值或者处理异常。
     * 适用场景：
     * Runnable适用于那些不需要返回值，且不会抛出受检异常的情况，比如简单的打印输出或者修改一些共享的变量。
     * Callable适用于那些需要返回值或者需要抛出受检异常的情况，比如对某个任务的计算结果进行处理，或者需要进行网络或IO操作等。在Java中，常常使用Callable来实现异步任务的处理，以提高系统的吞吐量和响应速度。
     * 总结来说，Runnable和Callable的主要区别在于方法定义、返回值、异常处理、使用方式和适用场景。在选择使用哪个接口时，需要根据具体的需求和场景来决定。
     *
     */
    static final ExecutorService executorService = Executors.newFixedThreadPool(1);
    private static AtomicInteger count = new AtomicInteger(0);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Runnable task = new Runnable() {
            public void run() {
                System.out.println("runnable" + count.getAndIncrement());
            }
        };

        Callable<String> callableTask =new Callable<String>() {

            public String call() throws Exception {
                // 模拟一个耗时的计算
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException(e);
                }
                return "Hello from Callable!";
            }
        };


        executorService.execute(task);
        // runnable： 有返回值。
        executorService.submit(task);

        // callable： 有返回值。
        Future<String> future = executorService.submit(callableTask);
        String s = future.get();
        System.out.println(s);

        Thread thread = new Thread(new Runnable() {
            public void run() {
                System.out.println("thread run");
            }
        });




    }
}
