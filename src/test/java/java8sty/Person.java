package src.test.java.java8sty;

public class Person {

    private String usrName;

    private int age;

    Person(){
        this.usrName = "ts";
        this.age = 20;
    }
    Person(String usrName, int age){
        this.usrName = usrName;
        this.age = age;

    }

    public String getUsrName() {
        return usrName;
    }

    public void setUsrName(String usrName) {
        this.usrName = usrName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
