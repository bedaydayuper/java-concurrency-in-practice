package net.jcip.zptest.chapter16;

import java.util.concurrent.Exchanger;

public class ExchangerExample {

    public static void main(String[] args) {
        final Exchanger<String> exchanger = new Exchanger<String>();

        Thread producer = new Thread(new Runnable() {
            public void run() {
                try {
                    String producedData = "Data from producer";
                    System.out.println("Producer thread is offering: " + producedData);
                    String consumerData = exchanger.exchange(producedData);
                    System.out.println("Producer received: " + consumerData);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                String consumedData = "Data from consumer";
                System.out.println("Consumer thread is offering: " + consumedData);
                String producerData = exchanger.exchange(consumedData);
                System.out.println("Consumer received: " + producerData);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();
    }
}
