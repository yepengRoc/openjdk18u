package com.java.test;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Test {


    public static void main(String[] args) {
        int a = Integer.numberOfLeadingZeros(16) | (1 << (16 - 1));
        System.out.println(a);


        Lock lock = new ReentrantLock();
        ReentrantLock reentrantLock = new ReentrantLock();
        reentrantLock = new ReentrantLock(true);
        reentrantLock.lock();
        reentrantLock.lockInterruptibly();
        Condition condition = reentrantLock.newCondition();
        condition.wait();
        condition.signal();
        reentrantLock.unlock();
        reentrantLock.notify();
        reentrantLock.notifyAll();


        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();

        readLock.lock();
        readLock.unlock();

        writeLock.lock();
        writeLock.unlock();

        CountDownLatch countDownLatch = new CountDownLatch(5);
        countDownLatch.await();
        countDownLatch.countDown();

        Semaphore semaphore = new Semaphore(5);
        semaphore.acquire();
        semaphore.release();

        /**
         * 内部通过 ReenTrantLock进行控制
         * Generation()
         */
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5);
        cyclicBarrier.await();

        cyclicBarrier.reset();

        Exchanger<String> exchanger = new Exchanger<>();

        exchanger.exchange("");



        ExecutorService cachePool = Executors.newCachedThreadPool();
        /**
         * 执行的时候生成一个futuretask
         * 然后 判断 core核心线程数，如果小于，则直接创建
         * 然后执行对应的任务。
         * 如果大于core。则把任务放入的 对应的阻塞队列中。
         * 如果阻塞队列也满了，如果core线程数 < max线程数
         * 则创建线程数。
         * 每次执行完任务，则将 创建的工作线程移除掉。如果移除后的线程数小于 core,则创建一个work线程
         */
        cachePool.submit(new Runnable() {
            @Override
            public void run() {

            }
        });
        cachePool.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return null;
            }
        });

        Executors.newFixedThreadPool(5);
        Executors.newSingleThreadExecutor();

        ExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
        scheduledExecutorService.submit(new Runnable() {
            @Override
            public void run() {

            }
        });
//        ExecutorService


    }
}
