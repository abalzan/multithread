package com.andrei.multithead.section2;

public class Sample1 {

    public static void main(String[] args) {

        Thread thread = new Thread(new BlockingThread());
        thread.start();

        thread.interrupt();
    }

    private static class BlockingThread implements Runnable{

        @Override
        public void run() {
            try {
                Thread.sleep(50000);
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted");
            }
        }
    }

}
