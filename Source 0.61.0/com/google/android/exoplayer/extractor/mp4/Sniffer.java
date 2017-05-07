package com.google.android.exoplayer.extractor.mp4;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.android.exoplayer.extractor.ExtractorInput;
import com.google.android.exoplayer.util.MpegAudioHeader;
import com.google.android.exoplayer.util.Util;
import java.io.IOException;

final class Sniffer {
    private static final int[] COMPATIBLE_BRANDS;

    static {
        COMPATIBLE_BRANDS = new int[]{Util.getIntegerCodeForString("isom"), Util.getIntegerCodeForString("iso2"), Util.getIntegerCodeForString("avc1"), Util.getIntegerCodeForString("hvc1"), Util.getIntegerCodeForString("hev1"), Util.getIntegerCodeForString("mp41"), Util.getIntegerCodeForString("mp42"), Util.getIntegerCodeForString("3g2a"), Util.getIntegerCodeForString("3g2b"), Util.getIntegerCodeForString("3gr6"), Util.getIntegerCodeForString("3gs6"), Util.getIntegerCodeForString("3ge6"), Util.getIntegerCodeForString("3gg6"), Util.getIntegerCodeForString("M4V "), Util.getIntegerCodeForString("M4A "), Util.getIntegerCodeForString("f4v "), Util.getIntegerCodeForString("kddi"), Util.getIntegerCodeForString("M4VP"), Util.getIntegerCodeForString("qt  "), Util.getIntegerCodeForString("MSNV")};
    }

    public static boolean sniffFragmented(ExtractorInput input) throws IOException, InterruptedException {
        return sniffInternal(input, MpegAudioHeader.MAX_FRAME_SIZE_BYTES, true);
    }

    public static boolean sniffUnfragmented(ExtractorInput input) throws IOException, InterruptedException {
        return sniffInternal(input, AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS, false);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean sniffInternal(com.google.android.exoplayer.extractor.ExtractorInput r22, int r23, boolean r24) throws java.io.IOException, java.lang.InterruptedException {
        /*
        r16 = r22.getLength();
        r18 = -1;
        r18 = (r16 > r18 ? 1 : (r16 == r18 ? 0 : -1));
        if (r18 == 0) goto L_0x0013;
    L_0x000a:
        r0 = r23;
        r0 = (long) r0;
        r18 = r0;
        r18 = (r16 > r18 ? 1 : (r16 == r18 ? 0 : -1));
        if (r18 <= 0) goto L_0x0018;
    L_0x0013:
        r0 = r23;
        r0 = (long) r0;
        r16 = r0;
    L_0x0018:
        r0 = r16;
        r10 = (int) r0;
        r8 = new com.google.android.exoplayer.util.ParsableByteArray;
        r18 = 64;
        r0 = r18;
        r8.<init>(r0);
        r9 = 0;
        r13 = 0;
        r12 = 0;
    L_0x0027:
        if (r9 >= r10) goto L_0x00c2;
    L_0x0029:
        r14 = 8;
        r0 = r8.data;
        r18 = r0;
        r19 = 0;
        r0 = r22;
        r1 = r18;
        r2 = r19;
        r0.peekFully(r1, r2, r14);
        r18 = 0;
        r0 = r18;
        r8.setPosition(r0);
        r6 = r8.readUnsignedInt();
        r5 = r8.readInt();
        r18 = 1;
        r18 = (r6 > r18 ? 1 : (r6 == r18 ? 0 : -1));
        if (r18 != 0) goto L_0x0064;
    L_0x004f:
        r0 = r8.data;
        r18 = r0;
        r19 = 8;
        r0 = r22;
        r1 = r18;
        r2 = r19;
        r0.peekFully(r1, r14, r2);
        r14 = 16;
        r6 = r8.readLong();
    L_0x0064:
        r0 = (long) r14;
        r18 = r0;
        r18 = (r6 > r18 ? 1 : (r6 == r18 ? 0 : -1));
        if (r18 >= 0) goto L_0x006e;
    L_0x006b:
        r18 = 0;
    L_0x006d:
        return r18;
    L_0x006e:
        r0 = (int) r6;
        r18 = r0;
        r4 = r18 - r14;
        r18 = com.google.android.exoplayer.extractor.mp4.Atom.TYPE_ftyp;
        r0 = r18;
        if (r5 != r0) goto L_0x00bb;
    L_0x0079:
        r18 = 8;
        r0 = r18;
        if (r4 >= r0) goto L_0x0082;
    L_0x007f:
        r18 = 0;
        goto L_0x006d;
    L_0x0082:
        r18 = r4 + -8;
        r11 = r18 / 4;
        r0 = r8.data;
        r18 = r0;
        r19 = 0;
        r20 = r11 + 2;
        r20 = r20 * 4;
        r0 = r22;
        r1 = r18;
        r2 = r19;
        r3 = r20;
        r0.peekFully(r1, r2, r3);
        r15 = 0;
    L_0x009c:
        r18 = r11 + 2;
        r0 = r18;
        if (r15 >= r0) goto L_0x00b6;
    L_0x00a2:
        r18 = 1;
        r0 = r18;
        if (r15 != r0) goto L_0x00ab;
    L_0x00a8:
        r15 = r15 + 1;
        goto L_0x009c;
    L_0x00ab:
        r18 = r8.readInt();
        r18 = isCompatibleBrand(r18);
        if (r18 == 0) goto L_0x00a8;
    L_0x00b5:
        r13 = 1;
    L_0x00b6:
        if (r13 != 0) goto L_0x00de;
    L_0x00b8:
        r18 = 0;
        goto L_0x006d;
    L_0x00bb:
        r18 = com.google.android.exoplayer.extractor.mp4.Atom.TYPE_moof;
        r0 = r18;
        if (r5 != r0) goto L_0x00cb;
    L_0x00c1:
        r12 = 1;
    L_0x00c2:
        if (r13 == 0) goto L_0x00e8;
    L_0x00c4:
        r0 = r24;
        if (r0 != r12) goto L_0x00e8;
    L_0x00c8:
        r18 = 1;
        goto L_0x006d;
    L_0x00cb:
        if (r4 == 0) goto L_0x00de;
    L_0x00cd:
        r0 = (long) r9;
        r18 = r0;
        r18 = r18 + r6;
        r0 = (long) r10;
        r20 = r0;
        r18 = (r18 > r20 ? 1 : (r18 == r20 ? 0 : -1));
        if (r18 >= 0) goto L_0x00c2;
    L_0x00d9:
        r0 = r22;
        r0.advancePeekPosition(r4);
    L_0x00de:
        r0 = (long) r9;
        r18 = r0;
        r18 = r18 + r6;
        r0 = r18;
        r9 = (int) r0;
        goto L_0x0027;
    L_0x00e8:
        r18 = 0;
        goto L_0x006d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer.extractor.mp4.Sniffer.sniffInternal(com.google.android.exoplayer.extractor.ExtractorInput, int, boolean):boolean");
    }

    private static boolean isCompatibleBrand(int brand) {
        if ((brand >>> 8) == Util.getIntegerCodeForString("3gp")) {
            return true;
        }
        for (int compatibleBrand : COMPATIBLE_BRANDS) {
            if (compatibleBrand == brand) {
                return true;
            }
        }
        return false;
    }

    private Sniffer() {
    }
}
