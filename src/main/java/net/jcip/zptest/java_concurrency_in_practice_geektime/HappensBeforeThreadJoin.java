package net.jcip.zptest.java_concurrency_in_practice_geektime;

public class HappensBeforeThreadJoin {
    private static int sharedValue = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread workerThread = new Thread(() -> {
            // 工作线程修改共享变量
            sharedValue = 42;
            System.out.println("Worker thread set sharedValue to: " + sharedValue);
        });

        // 启动工作线程
        workerThread.start();

        // 等待工作线程结束，确保看到工作线程对共享变量的修改
        workerThread.join();

        // 由于join()的happens-before关系，这里将看到sharedValue=42
        System.out.println("Main thread sees sharedValue: " + sharedValue);
    }
}
