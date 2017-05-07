package com.crittercism.internal;

import android.content.Context;
import com.google.android.exoplayer.util.MpegAudioHeader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Scanner;

public final class cd {
    public static void m364a(File file) {
        if (file.getAbsolutePath().contains("com.crittercism")) {
            if (file.isDirectory()) {
                for (File a : file.listFiles()) {
                    m364a(a);
                }
            }
            file.delete();
        }
    }

    public static String m366b(File file) {
        Scanner scanner = new Scanner(file);
        try {
            String next = scanner.useDelimiter("\\A").next();
            return next;
        } finally {
            scanner.close();
        }
    }

    public static byte[] m365a(InputStream inputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[MpegAudioHeader.MAX_FRAME_SIZE_BYTES];
        while (true) {
            int read = inputStream.read(bArr);
            if (-1 == read) {
                return byteArrayOutputStream.toByteArray();
            }
            byteArrayOutputStream.write(bArr, 0, read);
        }
    }

    public static String m363a(Context context, String str) {
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(str);
            String str2 = new String(m365a(inputStream));
            return str2;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
}
