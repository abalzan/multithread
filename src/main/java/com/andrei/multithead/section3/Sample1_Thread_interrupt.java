package com.andrei.multithead.section3;

public class Sample1_Thread_interrupt {

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
