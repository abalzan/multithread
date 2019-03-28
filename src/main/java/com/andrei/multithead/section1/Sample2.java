package com.andrei.multithead.section1;

public class Sample2 {

    public static void main(String[] args) throws InterruptedException {

        Thread thread = new Thread(new Runnable() {
            public void run() {
                throw new RuntimeException("Intentional Exception");
            }
        });

        thread.setName("Misbehaving thread");

        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("critical error in thread " + t.getName() +
                        " the error is "+ e.getMessage());
            }
        });

        thread.start();
    }
}
