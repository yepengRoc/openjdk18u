package com.java.test;

import sun.misc.Launcher;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiFunction;

public class Test1 {

    public static void main(String[] args){
        List<Integer> list = new ArrayList<>();

        Map<String, Object> map = new HashMap<>();

        Queue<Integer> queue = new PriorityQueue<>();

        Set<Integer> set = new HashSet();

        Deque<Integer> deque = new ArrayDeque<>();

        Lock lock = new ReentrantLock();
        Object object = new Object();
        synchronized (lock){

        }
//        Deque
//        BiFunction

//        Reference<> reference = new SoftReference();
    }
}
