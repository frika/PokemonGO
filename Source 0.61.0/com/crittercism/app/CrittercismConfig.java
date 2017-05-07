package com.crittercism.app;

import android.os.Build.VERSION;
import com.crittercism.internal.cc;
import java.util.LinkedList;
import java.util.List;

public class CrittercismConfig {
    private String f16a;
    private boolean f17b;
    private boolean f18c;
    private boolean f19d;
    private boolean f20e;
    private boolean f21f;
    private boolean f22g;
    private List<String> f23h;
    private List<String> f24i;

    public CrittercismConfig() {
        this.f16a = null;
        this.f17b = false;
        this.f18c = false;
        this.f19d = true;
        this.f20e = true;
        this.f21f = false;
        this.f22g = m22a();
        this.f23h = new LinkedList();
        this.f24i = new LinkedList();
    }

    public CrittercismConfig(CrittercismConfig toCopy) {
        this.f16a = null;
        this.f17b = false;
        this.f18c = false;
        this.f19d = true;
        this.f20e = true;
        this.f21f = false;
        this.f22g = m22a();
        this.f23h = new LinkedList();
        this.f24i = new LinkedList();
        this.f16a = toCopy.f16a;
        this.f17b = toCopy.f17b;
        this.f18c = toCopy.f18c;
        this.f19d = toCopy.f19d;
        this.f20e = toCopy.f20e;
        this.f21f = toCopy.f21f;
        this.f22g = toCopy.f22g;
        setURLBlacklistPatterns(toCopy.f23h);
        setPreserveQueryStringPatterns(toCopy.f24i);
    }

    public List<String> getURLBlacklistPatterns() {
        return new LinkedList(this.f23h);
    }

    public void setURLBlacklistPatterns(List<String> patterns) {
        this.f23h.clear();
        if (patterns != null) {
            this.f23h.addAll(patterns);
        }
    }

    public void setPreserveQueryStringPatterns(List<String> patterns) {
        this.f24i.clear();
        if (patterns != null) {
            this.f24i.addAll(patterns);
        }
    }

    public List<String> getPreserveQueryStringPatterns() {
        return new LinkedList(this.f24i);
    }

    public boolean equals(Object o) {
        if (!(o instanceof CrittercismConfig)) {
            return false;
        }
        CrittercismConfig crittercismConfig = (CrittercismConfig) o;
        if (this.f17b != crittercismConfig.f17b || this.f21f != crittercismConfig.f21f || this.f19d != crittercismConfig.f19d || this.f20e != crittercismConfig.f20e || isServiceMonitoringEnabled() != crittercismConfig.isServiceMonitoringEnabled() || isVersionCodeToBeIncludedInVersionString() != crittercismConfig.isVersionCodeToBeIncludedInVersionString()) {
            return false;
        }
        boolean equals;
        String str = this.f16a;
        String str2 = crittercismConfig.f16a;
        if (str != null) {
            equals = str.equals(str2);
        } else if (str2 == null) {
            equals = true;
        } else {
            equals = false;
        }
        if (equals && this.f23h.equals(crittercismConfig.f23h) && this.f24i.equals(crittercismConfig.f24i)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int hashCode;
        int i = 1;
        String str = this.f16a;
        if (str != null) {
            hashCode = str.hashCode();
        } else {
            hashCode = 0;
        }
        int hashCode2 = this.f24i.hashCode() + ((((hashCode + 0) * 31) + this.f23h.hashCode()) * 31);
        int i2 = ((this.f17b ? 1 : 0) + 0) << 1;
        if (this.f21f) {
            hashCode = 1;
        } else {
            hashCode = 0;
        }
        i2 = (hashCode + i2) << 1;
        if (this.f19d) {
            hashCode = 1;
        } else {
            hashCode = 0;
        }
        i2 = (hashCode + i2) << 1;
        if (this.f20e) {
            hashCode = 1;
        } else {
            hashCode = 0;
        }
        i2 = (hashCode + i2) << 1;
        if (isServiceMonitoringEnabled()) {
            hashCode = 1;
        } else {
            hashCode = 0;
        }
        hashCode = (hashCode + i2) << 1;
        if (!isVersionCodeToBeIncludedInVersionString()) {
            i = 0;
        }
        return Integer.valueOf(hashCode + i).hashCode() + (hashCode2 * 31);
    }

    public final String getCustomVersionName() {
        return this.f16a;
    }

    public final void setCustomVersionName(String customVersionName) {
        this.f16a = customVersionName;
    }

    public final boolean delaySendingAppLoad() {
        return this.f17b;
    }

    public final void setDelaySendingAppLoad(boolean delaySendingAppLoad) {
        this.f17b = delaySendingAppLoad;
    }

    public final boolean isVersionCodeToBeIncludedInVersionString() {
        return this.f18c;
    }

    public final void setVersionCodeToBeIncludedInVersionString(boolean shouldIncludeVersionCode) {
        this.f18c = shouldIncludeVersionCode;
    }

    public final boolean allowsCellularAccess() {
        return this.f19d;
    }

    public final void setAllowsCellularAccess(boolean allowsCellularAccess) {
        this.f19d = allowsCellularAccess;
    }

    public boolean reportLocationData() {
        return this.f20e;
    }

    public final void setReportLocationData(boolean reportLocationData) {
        this.f20e = reportLocationData;
    }

    public final boolean isLogcatReportingEnabled() {
        return this.f21f;
    }

    public final void setLogcatReportingEnabled(boolean shouldCollectLogcat) {
        this.f21f = shouldCollectLogcat;
    }

    private static boolean m22a() {
        return VERSION.SDK_INT >= 10 && VERSION.SDK_INT <= 23;
    }

    public final boolean isServiceMonitoringEnabled() {
        return this.f22g;
    }

    public final void setServiceMonitoringEnabled(boolean isServiceMonitoringEnabled) {
        if (m22a() || !isServiceMonitoringEnabled) {
            this.f22g = isServiceMonitoringEnabled;
        } else {
            cc.m360c("OPTMZ is currently only allowed for api levels 10 to 23.  APM will not be installed");
        }
    }
}
