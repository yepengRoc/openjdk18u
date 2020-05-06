package src.test.java.java8sty;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import src.test.java.java8sty.Person;
import java.util.stream.Collectors;

public class Sty005 {

    @Test
    public void test1(){
        List<src.test.java.java8sty.Person> listP = new ArrayList<>();

        Person p1 = new Person("zs", 20);
        Person p2 = new Person("ls", 30);
        Person p3 = new Person("ww", 40);

        listP.add(p1);
        listP.add(p2);
        listP.add(p3);

        List<Person> list1 = getPsByUsrName(listP, "zs");
        list1.forEach((a)-> System.out.println(a.getUsrName() + "=" + a.getAge()));
        System.out.println("==========");
        List<Person> list2 = getPsByAge(listP, 20);
        list2.forEach((a)-> System.out.println(a.getUsrName() + "=" + a.getAge()));


        System.out.println("==========");
        List<Person> list3 = getPsByAge2(20,listP,(age, listPs) -> {
            return listPs.stream().filter(p -> p.getAge() > age).collect(Collectors.toList());});
        list3.forEach((a)-> System.out.println(a.getUsrName() + "=" + a.getAge()));



    }

    /**
     * 根据名字获取对应person信息
     * @param listPs
     * @param usrName
     * @return
     */
    //及早求值  终止求值
    public List<Person> getPsByUsrName(List<Person> listPs, String usrName){
        return listPs.stream().filter(p -> p.getUsrName().equals("zs")).collect(Collectors.toList());

    }

    /**
     * 根据年龄过滤相应信息
     * @param listPs
     * @param age
     * @return
     */
    public List<Person> getPsByAge(List<Person> listPs, int age){
        BiFunction<Integer, List<Person>, List<Person>> biFunction = (pAge, listPson) -> {
            return listPs.stream().filter((ps) -> ps.getAge() > pAge).collect(Collectors.toList());
        };
        return biFunction.apply(age, listPs);
    }

    public List<Person> getPsByAge2(int age, List<Person> listPs, BiFunction<Integer,List<Person>,List<Person>> biFunction){
        return biFunction.apply(age, listPs);
    }

}
