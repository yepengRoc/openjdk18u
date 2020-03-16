package src.test.java.java8sty;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * lambda表达式，是没有声明的方法，即没有访问修饰符、返回值和名字
 * 作用：
 * 传递行为，不仅是值。
 * 1提升抽象层次
 * 2api重用性更好
 * 3更加灵活
 * 基本语法：
 * （参数）-> {逻辑}
 * 例如：（arg1,arg2） -> {逻辑}
 * (类型 arg1,类型 arg2) - {逻辑}
 * 可以指定参数的类型，也可以不指定，不指定的前提是可以通过上下文推断出来。
 *
 * （int a,int b） -> {return a+b}
 * () -> 42
 * () -> return 3
 *
 * lambda结构：
 *  可以有0或多个参数
 *  可以声明参数类型，也可以不声明-根据上下文进行推断
 *  参数必须在（）内，多个参数有逗号分割
 *  空的（）标示无参
 *  只有一个参数，且类型可以推导，则圆括号可以省略
 *  主体可以有一条或多条语句
 *  如果主体只有一条则{}也可以省略。匿名函数返回的类型和该主体表达式一致（不用显示的return.）
 *  如果主体包含多于一条的语句，则需要用{}包含。匿名函数的返回类型与代码块一致，若没有则返回空
 */
public class Sty004 {

    public static void main(String[] args){
        //对名字进行排序
        List<String> list = new ArrayList<>();

        list.add("zhansan");
        list.add("lisi");
        list.add("wangwu");

        Collections.sort(list,(String o1, String o2) -> {
            return o2.compareTo(o1);//声明式 statement
        });
        //直接逆序
         Collections.sort(list, Comparator.reverseOrder());
        /**
         * 更加简介。因为list存储的类型已经是清楚的，可以推断出来。compare作为返回
         *
         */
        Collections.sort(list,(o1, o2) -> o2.compareTo(o1) );//表达式风格，没有分号和 return


    }

    /**
     * 如果一个函数接收一个函数作为参数，或者返回值是一个函数，则是一个高阶函数
     * 这里的就是一个高阶函数。
     * 传统的计算，需要先在方法内把计算逻辑定义出来，然后再去调用
     */

    class FunctionTest{
        public int compute(int i , Function<Integer,Integer> function){
            return function.apply(i);
        }

        public String convert(int i, Function<Integer,String> function){
            return function.apply(i);
        }
    }
    @Test
    public void functionTest(){
        FunctionTest functionTest = new FunctionTest();
        //
        System.out.println(functionTest.compute(1, val -> {
            return val * 2;
        }));
        System.out.println(functionTest.compute(1, val -> val * 2));
        System.out.println(functionTest.convert(1, val -> String.valueOf(val)));
    }
    /**
     * Function 解读
     * 里面有两个default方法
     * compose 方法 实现多个function函数的串联
     * andThen 方法
     * identity 返回输入参数
     *
     */


    @Test
    public void functionMethodTest(){
        System.out.println(composeMethod(2, val -> val * 4, val -> val * val));
        System.out.println(andThenMethod(2, val -> val*4, val -> val*val));

        System.out.println(biFunctionM(2,3,(a, b) -> a+b));
        System.out.println(biFunctionM(2,3,(a, b) -> a-b));
        System.out.println(biFunctionM(2,3,(a, b) -> a*b));
        System.out.println(biFunctionM(2,3,(a, b) -> b/a));

        System.out.println(biAndThenM(2,4,(a, b) -> a+b, a -> a*a));

    }

    public int composeMethod(int a , Function<Integer,Integer> f1, Function<Integer,Integer> f2){
        return f1.compose(f2).apply(a);
    }

    public int andThenMethod(int a, Function<Integer,Integer> f1, Function<Integer,Integer> f2){
        return f1.andThen(f2).apply(a);
    }

    /**
     * BiFunction  传入两个 参数 返回一个结果
     * 为什么没有 compose 。因为function只能返回一个结果，但是bifunction需要两个参数
     *
     */

    public int biFunctionM(int a, int b, BiFunction<Integer,Integer,Integer> biFunction){
        return biFunction.apply(a, b);
    }
    /**
     * bifunction
     * andThen 方法，类比上面的方法
     */
    public int biAndThenM(int a, int b, BiFunction<Integer,Integer,Integer> biFunction, Function<Integer,Integer> f){
        return biFunction.andThen(f).apply(a, b);
    }


}
