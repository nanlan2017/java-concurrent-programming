package book_wwjun.ch10;

import java.util.Arrays;
import java.util.List;

/**
 * BootStrap TestClassLoader : 加载JDK核心包
 * Ext TestClassLoader ： 加载JDK ext 包
 * Application TestClassLoader ： 加载classpath中的第三方包
 */
public class TestClassLoader {
    public static String sstr = "abc";

    public static List<String> split(String rawStr, String sep){
        return Arrays.asList(rawStr.split(sep));
    }

    public static void work(){
        System.out.println("Bootstrap:" + String.class.getClassLoader());

        String bootPath = System.getProperty("sun.boot.class.path");
        split(bootPath,";").forEach(System.out::println);
        System.out.println("-------------------------------------");

        String extPath = System.getProperty("java.ext.dirs");
        split(extPath,";").forEach(System.out::println);
        System.out.println("-------------------------------------");

        String classPath = System.getProperty("java.class.path");
        split(classPath,";").forEach(System.out::println);
        System.out.println("-------------------------------------");

    }
}
