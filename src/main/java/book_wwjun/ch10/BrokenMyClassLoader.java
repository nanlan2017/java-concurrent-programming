package book_wwjun.ch10;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BrokenMyClassLoader extends MyClassLoader {
    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {

        System.out.println("》》》》》 BrokenMyClassLoader::loadClass:: loading " + name );

        synchronized (getClassLoadingLock(name)){

            Class<?> cls = findLoadedClass(name);

            if (cls == null){
                if (name.startsWith("java.") || name.startsWith("javax.")){
                    try{
                        cls = getSystemClassLoader().loadClass(name);
                    } catch (ClassNotFoundException e){
                        // ignore...
                    }
                } else {
                    try {
                        cls = findClass(name);

                    }catch (ClassNotFoundException e){
                        // ignore...
                    }

                    if (cls == null){
                        if (getParent() != null)
                            cls = getParent().loadClass(name);
                        else
                            cls = getSystemClassLoader().loadClass(name);
                    }
                }
            }

            if (cls == null)
                throw new ClassNotFoundException("ClassNotFound:: " + name);

            if (resolve)
                resolveClass(cls);

            return cls;
        }
    }

    public static void test() throws ClassNotFoundException
            , IllegalAccessException
            , InstantiationException
            , NoSuchMethodException
            , InvocationTargetException {
        BrokenMyClassLoader mycl = new BrokenMyClassLoader();
        Class<?> cls = mycl.loadClass("book_wwj.ch10.TestMyCL");
        System.out.println(cls.getClassLoader());

        Object obj = cls.newInstance();   // 此处才去执行该Class中的static block
        Method met = cls.getMethod("welcome");
        System.out.println(met.invoke(obj));
    }
}
