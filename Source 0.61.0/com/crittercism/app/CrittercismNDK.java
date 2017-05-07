package com.crittercism.app;

import android.content.Context;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import com.crittercism.internal.cc;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CrittercismNDK {
    private static final String ASSET_SO_FILE_NAME = "lib64libcrittercism-v3.crt";
    private static final String DST_SO_FILE_NAME = "lib64libcrittercism-v3.so";
    private static final String[] LEGACY_SO_FILE_NAMES;
    private static final String LIBRARY_NAME = "64libcrittercism-v3";
    private static boolean isNdkInstalled;

    public static native boolean installNdk(String str);

    static {
        isNdkInstalled = false;
        LEGACY_SO_FILE_NAMES = new String[]{"libcrittercism-ndk.so", "libcrittercism-v3.so"};
    }

    public static File getInstalledLibraryFile(Context c) {
        return new File((c.getFilesDir().getAbsolutePath() + "/com.crittercism/lib/") + DST_SO_FILE_NAME);
    }

    public static File crashDumpDirectory(Context context) {
        return new File(context.getFilesDir().getAbsolutePath(), "/com.crittercism/dumps");
    }

    public static void installNdkLib(Context context) {
        boolean isLoaded;
        if (doNdkSharedLibrariesExist(context)) {
            isLoaded = loadLibraryFromAssets(context);
        } else {
            try {
                System.loadLibrary(LIBRARY_NAME);
                isLoaded = true;
            } catch (Throwable th) {
                isLoaded = false;
            }
        }
        if (isLoaded) {
            cc.m362d("loaded NDK library.");
            try {
                File crashDumpDirectory = crashDumpDirectory(context);
                if (installNdk(crashDumpDirectory.getAbsolutePath())) {
                    crashDumpDirectory.mkdirs();
                    isNdkInstalled = true;
                    cc.m360c("initialized NDK crash reporting.");
                    return;
                }
                cc.m357b("Unable to initialize NDK crash reporting.");
                return;
            } catch (Throwable th2) {
                cc.m355a(th2);
                return;
            }
        }
        cc.m362d("did not load NDK library.");
    }

    public static boolean loadLibraryFromAssets(Context context) {
        File file = new File(context.getFilesDir(), "/com.crittercism/lib/");
        File file2 = new File(file, DST_SO_FILE_NAME);
        if (!file2.exists()) {
            if (copyLibFromAssets(context, file2)) {
                for (String file3 : LEGACY_SO_FILE_NAMES) {
                    File file4 = new File(file, file3);
                    cc.m362d("legacy lib: " + file4.getAbsolutePath() + ": " + (file4.exists() ? "deleting" : "not found"));
                    file4.delete();
                }
            } else {
                file2.delete();
                return false;
            }
        }
        try {
            System.load(file2.getAbsolutePath());
            return true;
        } catch (Throwable th) {
            cc.m353a("Unable to install NDK library: " + th.getMessage());
            cc.m355a(th);
            file2.delete();
            return false;
        }
    }

    public static boolean doNdkSharedLibrariesExist(Context context) {
        try {
            getJarredLibFileStream(context);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static InputStream getJarredLibFileStream(Context context) {
        String str = "armeabi";
        String property = System.getProperty("os.arch");
        cc.m362d("getJarredLibFileStream: os.arch: " + property);
        if (property.contains("v7")) {
            str = str + "-v7a";
        } else if (property.equals("aarch64")) {
            str = "arm64-v8a";
        }
        str = str + "/lib64libcrittercism-v3.crt";
        cc.m362d("getJarredLibFileStream: openning input stream from: " + str);
        return context.getAssets().open(str);
    }

    public static boolean copyLibFromAssets(Context context, File installedLibFile) {
        try {
            File parentFile = installedLibFile.getParentFile();
            cc.m362d("copyLibFromAssets: creating dir: " + parentFile.getAbsolutePath());
            parentFile.mkdirs();
            cc.m362d("copyLibFromAssets: installing library into: " + installedLibFile.getAbsolutePath());
            FileOutputStream fileOutputStream = new FileOutputStream(installedLibFile);
            InputStream jarredLibFileStream = getJarredLibFileStream(context);
            byte[] bArr = new byte[AccessibilityNodeInfoCompat.ACTION_SCROLL_BACKWARD];
            while (true) {
                int read = jarredLibFileStream.read(bArr);
                if (read >= 0) {
                    fileOutputStream.write(bArr, 0, read);
                } else {
                    jarredLibFileStream.close();
                    fileOutputStream.close();
                    cc.m362d("copyLibFromAssets: successful");
                    return true;
                }
            }
        } catch (Exception e) {
            cc.m353a("Could not install breakpad library: " + e.toString());
            return false;
        }
    }

    public static boolean isNdkCrashReportingInstalled() {
        return isNdkInstalled;
    }
}
