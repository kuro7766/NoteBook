package com.example.notebook.Util;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 反射工具
 */
public class FieldUtil {
    /**
     * 获取该对象下所有变量信息，包括私有变量，可以用来查询使用，示例
     * 传入new Integer(1):
     MIN_VALUE:-2147483648
     MAX_VALUE:2147483647
     TYPE:int
     digits:[C@677327b6
     DigitTens:[C@14ae5a5
     DigitOnes:[C@7f31245a
     sizeTable:[I@6d6f6e28
     value:1
     SIZE:32
     BYTES:4
     serialVersionUID:1360826667806852920

     可以看到，1其实是在value这个变量里面存着

     * @param o 对象
     * @return 对象所有变量拼成的字符串
     *
     * 对于有继承关系的对象
     * @see FieldUtil#getVariableDataString(Object, Class)
     * 在必要的时候需要传入object类型，例如
     * class A{
     *     int a;
     *     int b;
     * }
     * class B extends A{
     *     //空
     * }
     * 在调用
     * @see FieldUtil#getVariableDataString(Object) 的时候
     * 传入B类，((A)B).getClass()仍然是B.class，但是B类中的代码声明
     * 将获取不到A类的 a, b这两个变量，这时要用
     * @see FieldUtil#getVariableDataString(Object, Class) 方法
     * 手动设置class类型为A.class
     */
    public static String getVariableDataString(Object o, Class tClass){
        StringBuilder sb=new StringBuilder();
        Field[] pickerFields = tClass.getDeclaredFields();
        for (Field pf : pickerFields) {
            pf.setAccessible(true);
            try {
                sb.append(pf.getName());
                sb.append(":");
                sb.append(pf.get(o));
                sb.append("\n");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static String getVariableDataString(Object o){
        return getVariableDataString(o,o.getClass());
    }

    /**
     * @param tClass 手动设置class类
     */
    public static void set(Object object, Class tClass, String name, Object value){
        try {
            Field pf = tClass.getDeclaredField(name);
            pf.setAccessible(true);
            try {
                pf.set(object,value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置私有变量
     * @param object 对象
     * @param name 变量名称
     * @param value 想要设定该变量的值
     */
    public static void set(Object object, String name, Object value){
        set(object,object.getClass(),name,value);
    }

    /**
     * 获取对象的所有方法名称
     * @param o 对象
     * @return 对象函数拼成的字符串
     */
    public static String getFunctionsString(Object o){
        return getFunctionsString(o,o.getClass());
    }

    public static String getFunctionsString(Object o, Class tClass){
        StringBuilder sb=new StringBuilder();
        Method[] pickerFields = tClass.getDeclaredMethods();
        for (Method pf : pickerFields) {
            pf.setAccessible(true);
            sb.append(pf);
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * 获取对象某个私有变量的值
     * @param o 对象
     * @param name 变量名称
     * @return 获取的变量值
     */
    public static Object get(Object o, String name){
        return get(o.getClass(),name);
    }

    public static Object get(Object o, Class tClass, String name){
        try {
            Field pf = tClass.getDeclaredField(name);
            pf.setAccessible(true);
            try {
                return pf.get(o);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 反射调用对象的私有方法
     *
     * !!!这是对于非重载的方法!!!
     * 也就是这个名字的函数只有一个的时候,因为反射依据就是这个methodName字符串
     * 详细原因见
     * @see FieldUtil#invoke(Object, String, Object[], Class[])
     * 的注释
     *
     * @param object 对象
     * @param methodName  方法名字
     * @param arguments
     *                  函数传参,第一层统一用new Object[]{arg1,arg2,arg3} 包裹，如果需要包含
     *                  Object ... objects 时，请用new Object[]{} 继续包裹
     *                  示例:
     *                  1.对于func(String s)，arguments应该是new Object[]{str}，之所以这么麻烦，
     *                    是为了解决3,4的问题
     *                  2.对于func(String s1,String s2)，arguments应该是new Object[]{str1,str2}
     *                  3.例如对于func(Object ... objects)，arguments应该是
     *                  new Object[]{new Object{arg1,arg2,arg3}}
     *                  4.如果是func(Object ... objects,Object ... objects2)，arguments应该是
     *                  new Object[]{new Object{arg1,arg2,arg3}，new Object{arg4,arg5,arg6}}
     *                  5.如果是空参数，arguments可以传null,也可以传new Object[]{}    (里面什么也没有)
     *
     *
     * @return 函数返回值，void和调用失败都返回null
     */
    public static Object invoke(Object object, String methodName, Object[] arguments){
        return invoke(object,object.getClass(),methodName,arguments);
    }

    public static Object invoke(Object object, Class objectClass, String methodName, Object[] arguments){
        Method[] methods=objectClass.getDeclaredMethods();
        for(Method method:methods){
            if(method.getName().equals(methodName)){
                try {
                    method.setAccessible(true);
                    return method.invoke(object,arguments);
                }catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 反射调用对象的私有方法
     *
     * @param object 对象
     * @param methodName 方法名称
     * @param arguments 第一层统一用new Object[]{arg1,arg2,arg3} 包裹，如果需要包含
     *                  Object ... objects 时，请用new Object[]{} 继续包裹
     * @param argumentTypes 参数类型，用new Class[] 包裹
     *                      加这个参数的原因，是函数可能会******重载******，
     *                      例如print(Object o)和print(String o)
     *                      如果传入一个"hello"对象，这两个函数都可以接收
     *                      在   for(Method method:methods)
     *                      （必须要遍历，因为object.getClass().getDeclaredMethod(methodName)只能得到public方法，失去了反射的意义）
     *                      的遍历中，是从上往下遍历的，先找到谁，如果能传入就直接调用了
     *                      所以加上一层参数判断  !Arrays.equals(method.getParameterTypes(),argumentTypes)
     *                      示例:
     *                      1.func(int a) new Class[]{int.class}
     *                      2.func(int ... a) new Class[]{int[].class}
     *                      3.func() new Class[]{}
     * @return 函数返回值，void和调用失败都返回null
     */
    public static Object invoke(Object object, String methodName, Object[] arguments, Class[] argumentTypes){
        return invoke(object,object.getClass(),methodName,arguments,argumentTypes);
    }

    public static Object invoke(Object object, Class objectClass, String methodName, Object[] arguments, Class[] argumentTypes){
        Method[] methods=objectClass.getDeclaredMethods();
        for(Method method:methods){
            if(method.getName().equals(methodName)){
                if(!Arrays.equals(method.getParameterTypes(),argumentTypes)){
                    continue;
                }
                try {
                    method.setAccessible(true);
                    return method.invoke(object,arguments);
                }catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
