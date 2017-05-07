package com.crittercism.internal;

import com.crittercism.integrations.PluginException;
import java.io.PrintStream;
import java.io.PrintWriter;
import org.apache.commons.io.IOUtils;

public final class bl extends PluginException {
    private static final long serialVersionUID = -5983887553268578751L;
    private String[] f312a;
    private boolean f313b;

    public bl(String str, String str2, String str3, boolean z) {
        super(str, str2, str3);
        this.f312a = null;
        this.f312a = str3.split("\\r\\n");
        this.f313b = z;
    }

    public final void printStackTrace(PrintStream stream) {
        for (CharSequence append : this.f312a) {
            stream.append(append);
            stream.append(IOUtils.LINE_SEPARATOR_UNIX);
        }
    }

    public final void printStackTrace(PrintWriter pw) {
        for (CharSequence append : this.f312a) {
            pw.append(append);
            pw.append(IOUtils.LINE_SEPARATOR_UNIX);
        }
    }
}
