package testonly;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * for invisible test
 */
public class RefMethod {

    private Method method;
    private Constructor constructor;

    /**
     * init target invisible method.
     *
     * @param clazz      Class has invisible method.
     * @param methodName Name for invisible method.
     * @param argument   params for invisible method.
     */
    public RefMethod(Class clazz, String methodName, Class... argument) {
        try {
            // default constructor
            this.constructor = clazz.getDeclaredConstructor();
            this.constructor.setAccessible(true);

            // get method defined target class.
            this.method = clazz.getDeclaredMethod(methodName, argument);
            this.method.setAccessible(true);
        } catch (NoSuchMethodException e) {
            // show message and application stop.
            var message = new StringBuilder();
            message.append("fatal Initialize method.\r\n")
                    .append("class: ").append(clazz.getName()).append("\r\n")
                    .append("method: ").append(methodName).append("\r\n");
            Arrays.stream(argument).forEach(c -> message.append("argument: ").append(c));
            System.out.println(message);
            System.out.println();
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * usage<br>
     * ・example method definition "execute(String arg)"<br>
     * 1. execute("arg") -> invoke(new Object[]{"arg"})<br>
     * 2. execute("arg") -> invoke("arg")<br>
     * <p>
     * wrap {@link Method#invoke(Object, Object...)}
     *
     * @param args see {@link Method#invoke(Object, Object...)}
     * @return see {@link Method#invoke(Object, Object...)}
     * @throws Exception when invoke as method signature wrong.
     */
    public Object invoke(Object... args) throws Exception {
        // signature check
        // argument size check
        if (!(this.method.getParameterCount() == args.length)) {
            throw new Exception(
                    new StringBuilder("discrepancy argument length given\r\n")
                            .append("given: ").append(args.length).append("\r\n")
                            .append("define: ").append(this.method.getParameterCount())
                            .toString()
            );
        }
        // argument type check(without given is null)
        var defines = this.method.getParameterTypes();
        List<Class<?>> givens = new ArrayList<>();
        // if given is null use Dummy class.
        class Dummy {
        }
        Arrays.stream(args).forEach(o -> {
            givens.add(Objects.isNull(o) ? Dummy.class : o.getClass());
        });
        for (var i = 0; i < this.method.getParameterCount(); i++) {
            if (Dummy.class.equals(givens.get(i))) {
                continue;
            } else if (defines[i].equals(givens.get(i))) {
                continue;
            } else {
                //when has diff type definition show massage and throw.
                var message = new StringBuilder("\r\ndiscrepancy argument type given\r\n");
                message.append("  givens:\r\n");
                givens.forEach(s -> message.append("        ").append(s.getName()).append("\r\n"));
                message.append("\r\n");

                message.append("  defines:\r\n");
                Arrays.stream(defines).forEach(s -> message.append("        ").append(s.getName()).append("\r\n"));
                message.append("\r\n");

                throw new Exception(message.toString());
            }
        }

        return this.method.invoke(constructor.newInstance(), args);

    }

}
