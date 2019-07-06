package com.andrei.multithead.section3;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sample4__Solving_Race_Condition_With_Join_Threads {

    public static void main(String[] args) throws InterruptedException {
        List<Long> inputNumbers = Arrays.asList(100000000L, 9876L, 9876L, 7810L, 9126L, 12L, 8932L, 8396L);
        List<FactorialThread> threads = new ArrayList<FactorialThread>();

        for (long inputNumber : inputNumbers) {
            threads.add(new FactorialThread(inputNumber));
        }

        for (Thread thread : threads) {
            thread.setDaemon(true);
            //The race condition happen in this line because it is where the thread starts
            thread.start();
        }
        //forcing the main Thread to wait until all the calculation is finished.
        for (Thread thread: threads){
            // if after 2 seconds the thread doesn't terminate the join method will return,
            // and the background threads (that are not finished) will continue to execute
            //if you want to interupt the thread you can use the daemon, or the thread.interrupt()
            // we are using the daemon
            thread.join(2000);
        }

        for (int i = 0; i < inputNumbers.size(); i++) {
            FactorialThread factorialThread = threads.get(i);
            //And in this line where the main Thread executes
            if (factorialThread.isFinished()) {
                System.out.println("Factorial of " + inputNumbers.get(i) + " is " + factorialThread.getResult());
            } else {
                System.out.println("The calculation for " + inputNumbers.get(i) + " is still in progress");
            }
        }
    }

    public static class FactorialThread extends Thread {
        private long inputNumber;
        private BigInteger result = BigInteger.ZERO;
        private boolean isFinished = false;

        public FactorialThread(long inputNumber) {
            this.inputNumber = inputNumber;
        }

        @Override
        public void run() {
            this.result = factorial(inputNumber);
            this.isFinished = true;
        }

        public BigInteger factorial(long number) {
            BigInteger tempResult = BigInteger.ONE;

            for (long i = number; i > 0; i--) {
                tempResult = tempResult.multiply(new BigInteger(Long.toString(i)));
            }
            return tempResult;
        }

        public BigInteger getResult() {
            return result;
        }

        public boolean isFinished() {
            return isFinished;
        }
    }
}
