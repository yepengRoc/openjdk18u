package com.java.test;

import java.util.PriorityQueue;

public class PriorityQueueTest {

    public static void main(String[] args){
        PriorityQueue<Integer> priorityQueue = new PriorityQueue();
        priorityQueue.add(1);
        priorityQueue.offer(1);

        priorityQueue.poll();//
        priorityQueue.peek();//

        priorityQueue.remove();//移除头元素，没有就抛异常
        priorityQueue.element();//获取但是不移除
    }
}
