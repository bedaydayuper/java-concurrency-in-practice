package net.jcip.zptest.chapter3;

import java.util.HashMap;

public class NoVisibility {
    private static boolean ready;
    private static int number;


    private static class ReaderThread extends Thread {
        @Override
        public void run() {
            while (!ready) {
                System.out.println("not ready");
                Thread.yield();
            }
            System.out.println(number);

        }
    }

    public static void main(String[] args) throws InterruptedException {
        new ReaderThread().start();
        Thread.sleep(1000);
        number = 42;
        ready = true;
        System.out.println("number done");
        System.out.println("end....");
        Thread.sleep(10000);
    }
}
