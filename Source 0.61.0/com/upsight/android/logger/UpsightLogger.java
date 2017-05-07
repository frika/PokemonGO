package com.upsight.android.logger;

import java.util.EnumSet;

public interface UpsightLogger {
    public static final int MAX_LENGTH = 4000;

    public enum Level {
        VERBOSE,
        DEBUG,
        INFO,
        WARN,
        ERROR
    }

    void m574d(String str, String str2, Object... objArr);

    void m575d(String str, Throwable th, String str2, Object... objArr);

    void m576e(String str, String str2, Object... objArr);

    void m577e(String str, Throwable th, String str2, Object... objArr);

    void m578i(String str, String str2, Object... objArr);

    void m579i(String str, Throwable th, String str2, Object... objArr);

    void setLogLevel(String str, EnumSet<Level> enumSet);

    void m580v(String str, String str2, Object... objArr);

    void m581v(String str, Throwable th, String str2, Object... objArr);

    void m582w(String str, String str2, Object... objArr);

    void m583w(String str, Throwable th, String str2, Object... objArr);
}
