package com.java.test;

public class ThreadLocalTest {


    public static void main(String[] args){
        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        threadLocal.set("");
        threadLocal.get();
    }
}
