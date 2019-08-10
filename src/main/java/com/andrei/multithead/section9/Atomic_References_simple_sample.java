package com.andrei.multithead.section9;

import java.util.concurrent.atomic.AtomicReference;

public class Atomic_References_simple_sample {
    public static void main(String[] args) {
        String oldName = "oldName";
        String newName = "newName";

        AtomicReference<String> atomicReference = new AtomicReference<>(oldName);

        if(atomicReference.compareAndSet(oldName, newName)) {
            System.out.println("New name is " + atomicReference.get());
        }else {
            System.out.println("Nothing change");
        }
    }
}
