package com.crittercism.app;

import java.util.Date;

public class CrashData {
    private String f7a;
    private String f8b;
    private Date f9c;

    public CrashData(String name, String reason, Date timeOccurred) {
        this.f7a = name;
        this.f8b = reason;
        this.f9c = timeOccurred;
    }

    public CrashData copy() {
        return new CrashData(this.f7a, this.f8b, this.f9c);
    }

    public String getName() {
        return this.f7a;
    }

    public String getReason() {
        return this.f8b;
    }

    public Date getTimeOccurred() {
        return this.f9c;
    }
}
