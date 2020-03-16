package src.test.java.java8sty;


import java.util.Comparator;
import java.util.function.BinaryOperator;

public class Sty009 {

    public static void main(String[] args){
        Sty009 sty = new Sty009();

        System.out.println(sty.compute(4,3,(a, b) -> a+b));

        System.out.println(sty.getStr("hello", "wld", (a, b) -> a.length() - b.length()));
    }


    public int compute(int a, int b, BinaryOperator<Integer> binaryOperator){
        return binaryOperator.apply(a, b);
    }

    public String getStr(String a, String b, Comparator<String> c){
        return BinaryOperator.maxBy(c).apply(a,b);
    }
}
