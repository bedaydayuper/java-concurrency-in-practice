package net.jcip.zptest.java_concurrency_in_practice_book.chapter5;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapDemo {

    public static void main(String[] args) {
        final ConcurrentHashMap<Integer, Integer> concurrentHashMap = new ConcurrentHashMap();
        concurrentHashMap.put(1, 1);
        Thread thread1 = new Thread(new Runnable() {
            public void run() {
                for (int j = 0; j < 10000; j++) {
                    concurrentHashMap.put(j, j);
                }

            }
        });

        Thread thread2 = new Thread(new Runnable() {
            public void run() {
                for (int j = 10000; j < 20000; j++) {
                    concurrentHashMap.put(j, j);
                }

            }
        });
        thread1.start();

        thread2.start();

//        while (true) {
//            System.out.println(concurrentHashMap.size());
//        }



    }
}
