package com.andrei.multithead.section1;

public class Sample1 {

    public static void main(String[] args) throws InterruptedException {

        Thread thread = new Thread(new Runnable() {
            public void run() {
                System.out.println("we are in the thread " + Thread.currentThread().getName());
                System.out.println("Current thread priority " + Thread.currentThread().getPriority());


            }
        });
        thread.setName("New Work Thread");
        thread.setPriority(Thread.MAX_PRIORITY);

        System.out.println("actual Theard" + Thread.currentThread().getName()+ " before starting new Thread");
        thread.start();
        System.out.println("actual Theard" + Thread.currentThread().getName()+ " after starting new Thread");

        Thread.sleep(1000);
    }
}
