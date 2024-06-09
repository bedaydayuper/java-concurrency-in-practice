package net.jcip.zptest.java_concurrency_in_practice_book.chapter16;

public class SingletonDclDemo {
    // 使用volatile关键字确保instance变量的可见性和禁止指令重排序
    private static volatile SingletonDclDemo instance;

    private SingletonDclDemo() {
        // 私有构造函数，防止外部实例化
    }

    public static SingletonDclDemo getInstance() {
        if (instance == null) { // 第一次检查，避免不必要的同步
            synchronized (SingletonDclDemo.class) { // 同步块，确保线程安全
                if (instance == null) { // 第二次检查，确保只实例化一次
                    // volatile 禁止指令重排序 是指作用在 new SingletonDclDemo() 上。
                    /**
                     *
                     * 当创建一个对象时，Java虚拟机（JVM）和处理器可能会对指令进行重排序以提高性能。
                     * 例如，理论上，JVM可能会先分配内存空间，然后将对象的引用指向这块内存，最后再进行对象的初始化（即调用构造函数和初始化字段）。
                     * 如果没有适当的同步措施，这种重排序可能会导致其他线程看到一个尚未完全初始化的对象。
                     *
                     * volatile关键字的一个重要作用就是禁止这种可能破坏数据完整性的指令重排序。
                     * 在声明了volatile的变量上，JVM会保证所有的写操作（如对象的初始化和引用的赋值）对其他线程都是立即可见的，并且会按照代码的顺序执行，不会发生重排序。
                     *
                     * 在 SingletonDclDemo 类的示例中，volatile确保了在instance = new SingletonDclDemo();
                     * 这行代码中，对象的创建和初始化的整个过程是原子的，不会被重排序。
                     * 这意味着，当其他线程看到instance变量不再是null时，它们可以确保看到的是已经完全初始化的Singleton对象。
                     *
                     * 简而言之，volatile在这里确保了：
                     *
                     * 可见性：当一个线程修改了instance的值，其他线程能够立即看到修改后的值。
                     * 有序性：通过禁止指令重排序，确保对象的创建和初始化过程按照代码的顺序执行，不会被JVM或处理器优化所打乱。
                     */
                    instance = new SingletonDclDemo();
                }
            }
        }
        return instance;
    }
}
