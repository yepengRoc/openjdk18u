package src.test.java.java8sty;


import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * 如果myinterface只是定义一个toString方法是不能叫做函数式接口。详见 FunctionInterface 注释
 *FunctionalInterface 注解加不加都可以。编译器汇自行推断。详见 FunctionInterface 注释说明
 */
@FunctionalInterface
interface  MyInterface{
    void test();

    String toString();
}

public class Sty002 {

    public void test2(MyInterface myInterface){
        myInterface.test();
    }

    public static void main(String[] args){
        Sty002 test002 = new Sty002();
        /**
         * 从这里可以看出，传递给接口的是一个行为。即具体要跑的逻辑
         */
        test002.test2(() -> System.out.println("123"));
        /**
         * 这种方式也可以
         */
        MyInterface my = () -> System.out.println("123");

        test002.test2(my);

        System.out.println(my.getClass());
        System.out.println(my.getClass().getSuperclass());
        System.out.println(my.getClass().getInterfaces().length);
        System.out.println(my.getClass().getInterfaces()[0]);
    }

    /**
     * 123
     * 123
     * class com.ylsh.ssy.java8.sty001.Test002$$Lambda$2/1029991479
     * class java.lang.Object
     * 1
     * interface com.ylsh.ssy.java8.sty001.MyInterface
     *
     * 说明my是object的一个实例，实现了MyInterface接口。
     * lambda的格式 如果有参数则写一个参数名称 -> 逻辑功能。多行用{}包裹
     *
     */
    @Test
    public void testList(){
        List<Integer> list = Arrays.asList(1, 2,3,4);

        for(int i=0 ;i< list.size();i++){
            System.out.println(list.get(i));
        }
        //外部迭代
        for(Integer in : list){
            System.out.println(in);
        }
        //内部迭代--
        list.forEach(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                System.out.println(integer);
            }
        });

        /**
         * 鼠标放new Consumer<Integer> 提示可以被替换为lambda表达式
         */
        //内部迭代
        list.forEach((i)-> System.out.println(i));
        list.forEach(i -> System.out.println(i));
        list.forEach(System.out::println);//构造函数引用

    }


    @Test
    public void testInterface(){
        I002_1 i1 = () -> {};
        System.out.println(i1.getClass().getInterfaces()[0]);

        I002_2 i2 = () -> {};
        System.out.println(i2.getClass().getInterfaces()[0]);
    }
   /* interface com.ylsh.java8.sty001.I002_1
    interface com.ylsh.java8.sty001.I002_2*/

}

@FunctionalInterface
interface  I002_1{

    void method1();
}

@FunctionalInterface
interface  I002_2{
    void method2();
}



