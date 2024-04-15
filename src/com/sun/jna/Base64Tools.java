package com.sun.jna;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Created with IntelliJ IDEA.
 * User: lwahonen
 * Date: 2018-01-31
 * Time: 11:05 AM
 */
public final class Base64Tools {

    private Base64Tools() {
    }

    private static Constructor decoderConstructor = null;
    private static Method decoderMethod = null;
    private static Object decoder = null;

    private static Constructor encoderConstructor = null;
    private static Method encoderMethod = null;
    private static Object encoder = null;

    public static Constructor getDecoderConstructor() {
        return decoderConstructor;
    }

    public static Method getDecoderMethod() {
        return decoderMethod;
    }

    public static Object getDecoder() {
        return decoder;
    }

    public static Constructor getEncoderConstructor() {
        return encoderConstructor;
    }

    public static Method getEncoderMethod() {
        return encoderMethod;
    }

    public static Object getEncoder() {
        return encoder;
    }

    public static byte[] decode(final String toDecode) {
        if (isJava9OrGreater()) {
            try {
                if (toDecode == null) {
                    return null;
                }
                if (decoderMethod != null && decoder != null) {
                    return (byte[]) decoderMethod.invoke(decoder, toDecode);
                }
                Class<Base64> c = (Class<Base64>) Class.forName("java.util.Base64");
                decoderMethod = c.getMethod("getDecoder");
                decoder = decoderMethod.invoke(null);
                decoderMethod = decoder.getClass().getMethod("decode", String.class);
                return (byte[]) decoderMethod.invoke(decoder, toDecode);
            } catch (Throwable t) {
                return null;
            }
        } else {
            try {
                if (toDecode == null) {
                    return null;
                }
                if (decoderMethod != null && decoderConstructor != null) {
                    return (byte[]) decoderMethod.invoke(decoderConstructor.newInstance(), toDecode);
                }
                Class<?> c = Class.forName("sun.misc.BASE64Decoder");
                decoderConstructor = c.getConstructor();
                decoderMethod = c.getMethod("decodeBuffer", String.class);
                return (byte[]) decoderMethod.invoke(decoderConstructor.newInstance(), toDecode);
            } catch (Throwable t) {
                return null;
            }
        }
    }

    public static boolean isJava9OrGreater() {
        String version = System.getProperty("java.specification.version");
        if (version == null) {
            return false;
        }
        float ver = Float.parseFloat(version);
        final int javaNine = 9;
        return ver >= javaNine;
    }

    public static String encode(final String toEncode) {
        if (toEncode == null) {
            return null;
        }
        return encode(toEncode.getBytes(StandardCharsets.UTF_8));
    }

    public static String encode(final byte[] toEncode) {
        if (isJava9OrGreater()) {
            try {
                if (toEncode == null) {
                    return null;
                }
                if (encoderMethod != null && encoder != null) {
                    String invoke = (String) encoderMethod.invoke(encoder, (Object) toEncode);
                    return invoke.trim();
                }
                Class<?> c = Class.forName("java.util.Base64");
                encoderMethod = c.getMethod("getEncoder");
                encoder = encoderMethod.invoke(null);
                encoderMethod = encoder.getClass().getMethod("encodeToString", byte[].class);
                String invoke = (String) encoderMethod.invoke(encoder, toEncode);
                return invoke.trim();
            } catch (Throwable t) {
                return null;
            }
        } else {
            try {
                if (toEncode == null) {
                    return null;
                }
                if (encoderMethod != null && encoderConstructor != null) {
                    String invoke = (String) encoderMethod.invoke(encoderConstructor.newInstance(), toEncode);
                    return invoke.trim();
                }
                Class<?> c = Class.forName("sun.misc.BASE64Encoder");
                encoderConstructor = c.getConstructor();
                encoderMethod = c.getMethod("encodeBuffer", byte[].class);
                String invoke = (String) encoderMethod.invoke(encoderConstructor.newInstance(), toEncode);
                return invoke.trim();
            } catch (Throwable t) {
                return null;
            }
        }
    }
}
