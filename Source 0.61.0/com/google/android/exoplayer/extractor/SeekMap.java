package com.google.android.exoplayer.extractor;

public interface SeekMap {
    public static final SeekMap UNSEEKABLE;

    /* renamed from: com.google.android.exoplayer.extractor.SeekMap.1 */
    static class C03071 implements SeekMap {
        C03071() {
        }

        public boolean isSeekable() {
            return false;
        }

        public long getPosition(long timeUs) {
            return 0;
        }
    }

    long getPosition(long j);

    boolean isSeekable();

    static {
        UNSEEKABLE = new C03071();
    }
}
