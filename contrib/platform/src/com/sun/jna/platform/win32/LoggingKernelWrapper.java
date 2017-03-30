package com.sun.jna.platform.win32;

import java.lang.reflect.*;
import java.util.logging.Logger;

public class LoggingKernelWrapper implements InvocationHandler {

    private Kernel32 target;

    private LoggingKernelWrapper(Kernel32 target) {
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

    public Kernel32 getTarget() { return target; }

    public static Kernel32 createProxy(Kernel32 target) {
        return (Kernel32) Proxy.newProxyInstance(
                Kernel32.class.getClassLoader(),
                new Class[] { Kernel32.class },
                new LoggingKernelWrapper(target));
    };

    public static void createLibraryProxyFor() throws Exception {
        Field field=Kernel32.class.getField("INSTANCE");
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        Kernel32 wrapper = LoggingKernelWrapper.createProxy(Kernel32.INSTANCE);

        field.set(null, wrapper);
    }

}