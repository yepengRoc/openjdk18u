匿名函数和闭包 的体现

java中无法把一个函数作为参数进行传递，返回结果也无法是一个函数

参数和返回只能是基本变量和实例化的对象

函数式接口：
1如果一个接口只有一个抽象方法，则是
2如果某个接口上声明了FunctionalInterface注解，则是
3如果接口只有一个抽象方法，但是没有声明FunctionalInterface,依旧是


/**

此注解来声明一个函数式接口。
 
 函数式接口必须要有一个抽象方法，
 如果接种中定义的抽象方法和Object类中的方法名相同，则不能认为是一个
 函数式接口，因为对象或多或少都会继承Object。接口的抽象方法不会加1
 
 lambda表达式必须是方法引用或构造函数引用
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FunctionalInterface {}

例如：
//函数时接口
interface 接口名{
    void test();
}
//非函数式接口
interface 接口名{
    void test();
    void toString();
}
//函数式接口
interface 接口名{
    void test();
    String toString();//因为这里重写来Object类中的方法
}  



接口中也可以定义方法，方法用default修饰。默认方法，保证和老版本代码兼容。实现类默认拥有
接口中的默认方法

java中lambda是一个对象，其它语言中是函数,称为函数式接口.

函数式接口必须有一个上下文。
MyInterface my = () -> System.out.println("123"); 必须这样（）->{} 才能存在，MyInterface就是上下文，用来提供类型推断。




