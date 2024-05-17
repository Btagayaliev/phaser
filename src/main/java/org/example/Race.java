package org.example;

import java.util.concurrent.Phaser;

public class Race {

    private static final Phaser START = new Phaser(1); // 1 for the main thread

    private static final int trackLength = 1000;

    public static void main(String[] args) throws InterruptedException {
        for (int i = 1; i <= 10; i++) {
            new Thread(new Car(i, (int) (Math.random() * 100 + 50))).start();
            Thread.sleep(1000);
        }


        while (START.getRegisteredParties() < 11)
            Thread.sleep(100);

        Thread.sleep(1000);
        System.out.println("На старт!");
        START.arriveAndAwaitAdvance();

        Thread.sleep(1000);
        System.out.println("Внимание!");
        START.arriveAndAwaitAdvance();

        Thread.sleep(1000);
        System.out.println("Марш!");
        START.arriveAndDeregister();

    }

    public static class Car implements Runnable {
        private int carNumber;
        private int carSpeed;

        public Car(int carNumber, int carSpeed) {
            this.carNumber = carNumber;
            this.carSpeed = carSpeed;
            START.register();
        }

        @Override
        public void run() {
            try {
                System.out.printf("Автомобиль №%d подъехал к стартовой прямой.\n", carNumber);
                START.arriveAndAwaitAdvance();

                START.arriveAndAwaitAdvance();
                START.arriveAndAwaitAdvance();

                Thread.sleep(trackLength / carSpeed);
                System.out.printf("Автомобиль №%d финишировал!\n", carNumber);

                START.arriveAndDeregister();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}