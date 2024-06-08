package net.jcip.zptest.chapter14;

public class ResourcePoolDemo {

    public static void main(String[] args) {
        final ResourcePool pool = new ResourcePool(5); // 创建一个最大容量为5的资源池

        // 模拟多个线程从资源池中获取和归还资源
        Runnable task = new Runnable() {
            public void run() {
                try {
                    Object resource = pool.acquireResource(); // 获取资源
                    System.out.println(Thread.currentThread().getName() + " acquired a resource.");

                    // 模拟使用资源的时间
                    Thread.sleep((long) (Math.random() * 1000));

                    pool.releaseResource(resource); // 归还资源
                    System.out.println(Thread.currentThread().getName() + " released a resource.");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println(Thread.currentThread().getName() + " was interrupted.");
                }
            }
        };

        // 创建并启动多个线程来模拟资源的使用
        for (int i = 0; i < 10; i++) {
            new Thread(task, "Thread-" + i).start();
        }
    }
}
