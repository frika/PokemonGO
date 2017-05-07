package com.crittercism.internal;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public final class as {
    private static final Map<String, String> f176e;
    public URL f177a;
    public URL f178b;
    public URL f179c;
    public URL f180d;

    static {
        Map hashMap = new HashMap();
        f176e = hashMap;
        hashMap.put("00555300", "crittercism.com");
        f176e.put("00555304", "crit-ci.com");
        f176e.put("00555305", "crit-staging.com");
        f176e.put("00444503", "eu.crittercism.com");
    }

    public as(String str) {
        if (str == null) {
            throw new IllegalArgumentException("null App ID");
        } else if (!str.matches("[0-9a-fA-F]+")) {
            throw new IllegalArgumentException("App ID must be hexadecimal characters");
        } else if (str.length() == 24 || str.length() == 40) {
            Object obj = null;
            if (str.length() == 24) {
                obj = "00555300";
            } else if (str.length() == 40) {
                obj = str.substring(str.length() - 8);
            }
            String str2 = (String) f176e.get(obj);
            if (str2 == null) {
                throw new IllegalArgumentException("Invalid character sequence");
            }
            try {
                this.f177a = new URL(System.getProperty("com.crittercism.apmUrl", "https://apm." + str2));
                this.f178b = new URL(System.getProperty("com.crittercism.apiUrl", "https://api." + str2));
                this.f179c = new URL(System.getProperty("com.crittercism.txnUrl", "https://txn.ingest." + str2));
                this.f180d = new URL(System.getProperty("com.crittercism.appLoadUrl", "https://appload.ingest." + str2));
            } catch (Throwable e) {
                throw new IllegalArgumentException("Crittercism failed to initialize", e);
            }
        } else {
            throw new IllegalArgumentException("App ID must be either 24 or 40 characters");
        }
    }
}
