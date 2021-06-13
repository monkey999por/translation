package testonly;

import setting.common.Setting;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

/**
 * for invisible test
 */
public class RefMethod {

    private Method method;
    private Constructor constructor;

    /**
     * init target invisible method.
     * @param clazz Class has invisible method.
     * @param methodName Name for invisible method.
     * @param argument params for invisible method.
     * @throws NoSuchMethodException
     */
    public RefMethod(Class clazz, String methodName, Class... argument) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        // default constructor
        this.constructor = clazz.getDeclaredConstructor();
        this.constructor.setAccessible(true);

        // get method defined target class.
        this.method = clazz.getDeclaredMethod(methodName, argument);
        this.method.setAccessible(true);
    }

    /**
     * usege call method<br>
     * ・example method definition "execute(String arg)"<br>
     * :: execute("arg") -> invoke(new Object[]{"arg"})<br>
     *
     * wrap {@link Method#invoke(Object, Object...)}
     * @param args see {@link Method#invoke(Object, Object...)}
     * @return see {@link Method#invoke(Object, Object...)}
     * @throws InvocationTargetException see {@link Method#invoke(Object, Object...)}
     * @throws InstantiationException see {@link Method#invoke(Object, Object...)}
     * @throws IllegalAccessException see {@link Method#invoke(Object, Object...)}
     */
    public Object invoke(Object... args) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        if (args.length == 0){
            return this.method.invoke(constructor.newInstance());
        } else {
            return this.method.invoke(constructor.newInstance(), args);
        }
    }

}
