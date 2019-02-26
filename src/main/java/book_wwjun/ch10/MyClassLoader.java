package book_wwjun.ch10;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MyClassLoader extends ClassLoader {

    private static final Path DEFAULT_CLASS_DIR = Paths.get("C:","workspace","WS_IDEA_Ja","test");

    private final Path classDir;

    public MyClassLoader(){
        super();
        classDir = DEFAULT_CLASS_DIR;
    }

    public MyClassLoader(String classdir){
        super();
        classDir = Paths.get(classdir);
    }

    public MyClassLoader(ClassLoader parent){
        super(parent);
        classDir = DEFAULT_CLASS_DIR;
    }

    public MyClassLoader(String classdir,ClassLoader parent){
        super(parent);
        classDir = Paths.get(classdir);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        System.out.println("———————————— Loading class : " + name +" ——————————————");
        byte[] classBytes = readClassBytes(name);

        // check
        if (classBytes ==null || classBytes.length ==0)
            throw new ClassNotFoundException("Can't load the class:" + name);

        return defineClass(name,classBytes,0,classBytes.length);
    }

    private byte[] readClassBytes(String name) throws ClassNotFoundException {
        String classpath = name.replace(".","/");
        Path fullClasspath = classDir.resolve(Paths.get(classpath+".class"));

        if (!fullClasspath.toFile().exists())
            throw new ClassNotFoundException("The Class "+ name +" not found.");

        try (ByteArrayOutputStream  baos = new ByteArrayOutputStream()){
            Files.copy(fullClasspath,baos);
            return baos.toByteArray();
        } catch (IOException e){
            throw new ClassNotFoundException("Error occurs when loading the class : "+name);
        }
    }

    @Override
    public String toString(){
        return "My ClassLoader";
    }

    /**
     * package book_wwj.ch10;
     *
     * public class TestMyCL {
     *
     *     static {
     *         System.out.println("TestMyCL is initialized.");
     *     }
     *
     *     public TestMyCL() {
     *     }
     *
     *     public String welcome() {
     *         return "Hello World";
     *     }
     *
     * }
     */
    public static void test() throws ClassNotFoundException
            , IllegalAccessException
            , InstantiationException
            , NoSuchMethodException
            , InvocationTargetException {
        MyClassLoader mycl = new MyClassLoader();
        Class<?> cls = mycl.loadClass("book_wwj.ch10.TestMyCL");
        System.out.println(cls.getClassLoader());

        Object obj = cls.newInstance();   // 此处才去执行该Class中的static block
        Method met = cls.getMethod("welcome");
        System.out.println(met.invoke(obj));
    }
}
