package com.crittercism.internal;

import java.lang.reflect.Field;

/* renamed from: com.crittercism.internal.e */
public final class C0255e {
    public static <C, F> F m383a(Field field, C c) {
        F f = null;
        if (!(field == null || field == null)) {
            field.setAccessible(true);
            try {
                f = field.get(c);
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable th) {
                bh bhVar = new bh("Unable to get value of field", th);
            }
        }
        return f;
    }

    public static Field m384a(Class<?> cls, Class<?> cls2, boolean z) {
        Field[] declaredFields = cls.getDeclaredFields();
        Field field = null;
        for (int i = 0; i < declaredFields.length; i++) {
            if (cls2.isAssignableFrom(declaredFields[i].getType())) {
                if (field != null) {
                    throw new bh("Field is ambiguous: " + field.getName() + ", " + declaredFields[i].getName());
                }
                field = declaredFields[i];
            }
        }
        if (field != null) {
            field.setAccessible(true);
        } else if (z) {
            throw new bh("Could not find field matching type: " + cls2.getName());
        }
        return field;
    }
}
