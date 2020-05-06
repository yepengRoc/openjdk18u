package com.java.test;

public class Test {


    public static void main(String[] args) {
        int a = Integer.numberOfLeadingZeros(16) | (1 << (16 - 1));
        System.out.println(a);
    }
}
