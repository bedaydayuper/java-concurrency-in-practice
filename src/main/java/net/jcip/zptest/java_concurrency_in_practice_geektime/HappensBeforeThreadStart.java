package net.jcip.zptest.java_concurrency_in_practice_geektime;

/**
 * Thread.start() 的happens-before关系确保了新线程能够看到主线程在启动它之前所做的修改。
 */
public class HappensBeforeThreadStart {
    private static boolean flag = false;
    private static int sharedValue = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            // 由于Thread.start()的happens-before关系，这里将看到flag=true和sharedValue=42
            if (flag) {
                System.out.println("The value of sharedValue in the new thread is: " + sharedValue);
            }
        });

        // 修改共享变量
        sharedValue = 42;
        flag = true;

        // 启动线程
        thread.start();

        // 等待线程结束
        thread.join();
    }
}
