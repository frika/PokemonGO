package com.crittercism.internal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public final class cf {
    public static final cf f482a;
    private cg f483b;
    private ThreadLocal<SimpleDateFormat> f484c;
    private ThreadLocal<SimpleDateFormat> f485d;

    /* renamed from: com.crittercism.internal.cf.a */
    class C0251a implements cg {
        final /* synthetic */ cf f481a;

        private C0251a(cf cfVar) {
            this.f481a = cfVar;
        }

        public final Date m370a() {
            return new Date();
        }
    }

    static {
        f482a = new cf();
    }

    private cf() {
        this.f483b = new C0251a();
        this.f484c = new ThreadLocal();
        this.f485d = new ThreadLocal();
    }

    private SimpleDateFormat m371b() {
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) this.f484c.get();
        if (simpleDateFormat != null) {
            return simpleDateFormat;
        }
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        simpleDateFormat.setLenient(false);
        this.f484c.set(simpleDateFormat);
        return simpleDateFormat;
    }

    public final String m373a() {
        return m374a(this.f483b.m369a());
    }

    public final String m374a(Date date) {
        return m371b().format(date);
    }

    public final long m372a(String str) {
        Date parse;
        try {
            parse = m371b().parse(str);
        } catch (ParseException e) {
            SimpleDateFormat simpleDateFormat = (SimpleDateFormat) this.f485d.get();
            if (simpleDateFormat == null) {
                simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                simpleDateFormat.setLenient(false);
                this.f485d.set(simpleDateFormat);
            }
            parse = simpleDateFormat.parse(str);
        }
        return parse.getTime();
    }
}
