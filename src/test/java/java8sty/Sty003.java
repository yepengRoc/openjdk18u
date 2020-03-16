package src.test.java.java8sty;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Sty003 {

    public static void main(String[] args){
        new Thread(() -> {
            System.out.println("hello workd");
        }).start();


        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        //将每个元素首字母大写
        list.forEach(str -> System.out.println(str.toUpperCase()));
        //将每个元素首字母大写，然后存入到新的集合中
        //中间流，结果流。stream需要有一个来源数据
        list.stream().map(str -> str.toUpperCase()).forEach(item -> System.out.println(item));
        list.stream().map(String::toUpperCase).forEach(item -> System.out.println(item));
        /**
         * function 函数接收一个输入，返回一个输出
         * 这里输入是一个String,输出是一个String
         * String ：： toUpperCase  String作为输入，来源于方法调用者。 toUpperCasa作为输出
         */
        Function<String, String> function = String::toUpperCase;
        System.out.println(function.getClass().getInterfaces()[0]);
        System.out.println(function.apply("aefd"));
        list.stream().map(function).forEach(item -> System.out.println(item));


    }
    @Test
    public void test1(){
        Function<String, String> function = String::toUpperCase;
        System.out.println(function.getClass().getInterfaces()[0]);
        System.out.println(function.apply("aefd"));
    }
}
