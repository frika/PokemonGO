package com.crittercism.internal;

import android.location.Location;

public final class an {
    private static Location f102a;

    public static synchronized void m111a(Location location) {
        synchronized (an.class) {
            if (location != null) {
                location = new Location(location);
            }
            f102a = location;
        }
    }

    public static synchronized Location m110a() {
        Location location;
        synchronized (an.class) {
            location = f102a;
        }
        return location;
    }

    public static synchronized boolean m112b() {
        boolean z;
        synchronized (an.class) {
            z = f102a != null;
        }
        return z;
    }
}
