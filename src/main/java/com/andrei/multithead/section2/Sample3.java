package com.andrei.multithead.section2;

import java.math.BigInteger;

public class Sample3 {

    public static void main(String[] args) {

        Thread thread = new Thread(new LongComputationClass(new BigInteger("20000000"), new BigInteger("100000")));

        // When the Deamon is enable, when we kill our main thread everything else is killed as well.
        // example: when you kill your application you don't care if the email job was executed or not
        // so you don't want to wait for this job to finish.
        thread.setDaemon(true);
        thread.start();
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
                result = result.multiply(base);
            }

            return result;
        }
    }

}
