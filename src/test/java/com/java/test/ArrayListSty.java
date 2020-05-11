package com.java.test;

import sun.jvm.hotspot.ui.treetable.TreeTableModelAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Exchanger;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ArrayListSty {

    public static void main(String[] args) {
        ArrayList list = new ArrayList();

        LinkedList linkedList = new LinkedList();
        linkedList.get(1);

        CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList();

        HashMap map = new HashMap();
        map.put("","");

        LinkedHashMap linkedHashMap = new LinkedHashMap();

        WeakHashMap weakHashMap = new WeakHashMap();

        TreeMap treeMap = new TreeMap();
        /**
         * 需要看看
         */
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        concurrentHashMap.put("", "");

        ConcurrentSkipListMap concurrentSkipListMap = new ConcurrentSkipListMap();
        concurrentSkipListMap.put("", "");

        TreeSet treeSet = new TreeSet();
        treeSet.add("");
        SynchronousQueue synchronousQueue = new SynchronousQueue();
        synchronousQueue.offer(1);

        PriorityBlockingQueue priorityBlockingQueue = new PriorityBlockingQueue();
        priorityBlockingQueue.add(1);
        priorityBlockingQueue.offer(1);

        /**
         * 需要看看
         */
        LinkedTransferQueue linkedTransferQueue = new LinkedTransferQueue();
        linkedTransferQueue.offer(1);

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            countDownLatch.await();
            countDownLatch.countDown();;


            ReentrantLock reentrantLock =  new ReentrantLock();
            Condition condition = reentrantLock.newCondition();
            condition.await();
            reentrantLock.newCondition();

            CyclicBarrier cyclicBarrier = new CyclicBarrier(1, new Runnable() {
                @Override
                public void run() {

                }
            });
//            cyclicBarrier.await();

            ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

            ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
            readLock.lock();
            readLock.unlock();

            ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
            writeLock.lock();
            writeLock.unlock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Exchanger exchanger = new Exchanger();



    }
}
