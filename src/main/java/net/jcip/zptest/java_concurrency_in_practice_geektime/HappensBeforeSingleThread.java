package net.jcip.zptest.java_concurrency_in_practice_geektime;

public class HappensBeforeSingleThread {

    private int value = 0;

    public void updateValue() {
        value = 42; // 第一个操作：设置value的值
        System.out.println("Value updated to: " + value); // 第二个操作：打印更新后的value值
    }

    public static void main(String[] args) {
        HappensBeforeSingleThread example = new HappensBeforeSingleThread();
        example.updateValue(); // 调用updateValue方法
    }
}
