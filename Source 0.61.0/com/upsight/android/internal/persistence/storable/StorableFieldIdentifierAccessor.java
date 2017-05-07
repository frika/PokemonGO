package com.upsight.android.internal.persistence.storable;

import com.upsight.android.UpsightException;
import java.lang.reflect.Field;

class StorableFieldIdentifierAccessor implements StorableIdentifierAccessor {
    private final Field mField;

    StorableFieldIdentifierAccessor(Field field) {
        if (field == null) {
            throw new IllegalArgumentException("Specified Field can not be null.");
        }
        this.mField = field;
    }

    public synchronized void setId(Object target, String id) throws UpsightException {
        try {
            this.mField.setAccessible(true);
            this.mField.set(target, id);
            this.mField.setAccessible(false);
        } catch (Exception e) {
            throw new UpsightException("Error accessing field: " + this.mField, e);
        } catch (Throwable th) {
            this.mField.setAccessible(false);
        }
    }

    public synchronized String getId(Object target) throws UpsightException {
        String str;
        try {
            this.mField.setAccessible(true);
            str = (String) this.mField.get(target);
            this.mField.setAccessible(false);
        } catch (Exception e) {
            throw new UpsightException("Error accessing field: " + this.mField, e);
        } catch (Throwable th) {
            this.mField.setAccessible(false);
        }
        return str;
    }
}
