package src.test.java.java8sty;


import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

public class Sty007 {

    public static void main(String[] args){
        List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
        Sty007 sty = new Sty007();

        sty.filter(list,i -> i > 5);
        sty.filter(list,i -> true);//打印所有
        sty.filter(list,i -> false);//所有不打印

        System.out.println("======================");
        sty.filter2(list,i -> i > 5,i -> i%2 == 0);

        System.out.println(sty.isEqual("test").test("test"));
        System.out.println(Predicate.isEqual("test").test("test"));

        System.out.println(Predicate.isEqual(new Date()).test(new Date()));


    }

    public void filter(List<Integer> list, Predicate<Integer> p){
        list.forEach(i -> {
            if(p.test(i)){
                System.out.println(i);
            }
        });
    }

    public void filter2(List<Integer> list, Predicate<Integer> p, Predicate<Integer> p1){
        list.forEach(i -> {
            if(p.and(p1).negate().test(i)){
                System.out.println(i);
            }
        });
    }

    public Predicate<String> isEqual(Object object){
        return Predicate.isEqual(object);
    }

}
