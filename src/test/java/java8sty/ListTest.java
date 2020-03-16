package src.test.java.java8sty;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ListTest {

    public static void main(String[] args){

        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        /**
         * cosumer接收的是一个动作，这里 动作，即可以改变原来的值，比如说
         * 输出2倍。这样是有风险的。
         * 如果没有动作，则按顺序迭代
         */

        list.forEach(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                System.out.println(integer);
            }
        });
        //也可以迭代。lambda可以通过 方法引用，构造函数。这里的是方法引用。点击->指向了consumer
        list.forEach(i -> System.out.println(i));
        //方法引用 创建函数式接口的实例。
        list.forEach(System.out::println);


    }
}
