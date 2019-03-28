package com.andrei.multithead.section2;

import java.math.BigInteger;

public class Sample2 {

    public static void main(String[] args) {

        Thread thread = new Thread(new LongComputationClass(new BigInteger("20000000"), new BigInteger("100000")));
        thread.start();

        //comment the line bellow just to see that the thread takes long time to complete
        thread.interrupt();
    }

    private static class LongComputationClass implements Runnable{
        private BigInteger base;
        private BigInteger power;

        private LongComputationClass(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
            System.out.println(base +"*"+power + "equals: "+ pow(base, power));
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
    }

}
