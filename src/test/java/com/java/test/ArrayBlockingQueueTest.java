package src.test.java.com.java.test;

import java.util.concurrent.ArrayBlockingQueue;

public class ArrayBlockingQueueTest {

    public static void main(String[] args) throws Exception{
        ArrayBlockingQueue<Integer> que = new ArrayBlockingQueue(2);//需要指定有界队列大小
//        que.add(1);
//
//        que.offer(1);
//        que.put(1);
//        que.take();
//        que.peek();

        que.offer(1);
        que.offer(2);
        que.offer(3);

    }
}
