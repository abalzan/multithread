package com.andrei.multithead.section3;

import java.math.BigInteger;

public class ComplexCalculation {
    public BigInteger calculateResult(BigInteger base1, BigInteger power1, BigInteger base2, BigInteger power2) {
        BigInteger result;
        Thread thread0 = new Thread(new PowerCalculatingThread(base1, power1));
        Thread thread1 = new Thread(new PowerCalculatingThread(base2, power2));
        thread0.start();
        thread1.start();

//        return result;
        return null;
    }

    private static class PowerCalculatingThread extends Thread {
        private BigInteger result = BigInteger.ONE;
        private BigInteger base;
        private BigInteger power;
    
        public PowerCalculatingThread(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }
    
        @Override
        public void run() {
           /*
           Implement the calculation of result = base ^ power
           */
        }

        private BigInteger pow (BigInteger base, BigInteger power) {
            BigInteger result = BigInteger.ONE;

            for (BigInteger i = BigInteger.ZERO; i.compareTo(power)!=0; i = i.add(BigInteger.ONE)) {
                //checking if the thread was interupted, and we handle it gracefully
                if(Thread.currentThread().isInterrupted()){
                    System.out.println("Thread interupted ");
                    return BigInteger.ZERO;
                }
                result = result.multiply(base);
            }

            return result;
        }

        public BigInteger getResult() { return result; }
    }
}