package com.java.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Exchanger;
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



    }
}
