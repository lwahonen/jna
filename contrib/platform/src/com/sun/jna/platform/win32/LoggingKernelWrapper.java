package com.sun.jna.platform.win32;

import com.sun.jna.platform.win32.LoggingKernelWrapper;

import java.lang.reflect.*;
import java.util.logging.Logger;

public class LoggingKernelWrapper implements InvocationHandler {

    private Object target;

    private LoggingKernelWrapper(Object target) {
        if (target == null) throw new IllegalArgumentException("'target' can't be null.");
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {

        try {
            // Add code here to log Method.name etc
            Object result = method.invoke(target, args);
            return result;
        } catch (InvocationTargetException e) {
            Throwable cause = e.getTargetException();
            tryLogFailure(cause);
            throw cause;
        }

    }

    private void tryLogFailure(Throwable cause) {
        Logger.getLogger("JavaProxy").warning(
                "THE INJECTED CODE SAYS: " + cause);
    }

    public Object getTarget() { return target; }

    public static Object createProxy(Object target, Class interfaceClass) {
        return Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class[] {interfaceClass},
                new LoggingKernelWrapper(target));
    };

    public static void createLibraryProxyFor(Class targetClass) throws Exception {
        Field field= targetClass.getField("INSTANCE");
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        Object target = field.get(null);
        Object wrapper = LoggingKernelWrapper.createProxy(target, targetClass);

        field.set(null, wrapper);
    }


}