package net.jcip.zptest.chapter14;

import java.util.LinkedList;
import java.util.Queue;

public class ResourcePool {
    private final Queue<Object> resources = new LinkedList<Object>();
    private final int maxSize;

    public ResourcePool(int maxSize) {
        this.maxSize = maxSize;
        // 初始化资源池
        for (int i = 0; i < maxSize; i++) {
            resources.add(new Object());
        }
    }

    public synchronized Object acquireResource() throws InterruptedException {
        while (resources.isEmpty()) {
            wait(); // 等待资源可用
        }
        return resources.poll(); // 获取并移除池顶的资源
    }

    public synchronized void releaseResource(Object resource) {
        if (resource != null) {
            resources.add(resource); // 归还资源到池中
            notifyAll(); // 通知等待的线程资源已归还
        }
    }
}
