package com.google.android.exoplayer.extractor.mp3;

import com.google.android.exoplayer.C0277C;
import com.google.android.exoplayer.util.MpegAudioHeader;
import com.google.android.exoplayer.util.ParsableByteArray;
import com.google.android.exoplayer.util.Util;
import spacemadness.com.lunarconsole.C1628R;

final class VbriSeeker implements Seeker {
    private final long durationUs;
    private final long[] positions;
    private final long[] timesUs;

    public static VbriSeeker create(MpegAudioHeader mpegAudioHeader, ParsableByteArray frame, long position, long inputLength) {
        frame.skipBytes(10);
        int numFrames = frame.readInt();
        if (numFrames <= 0) {
            return null;
        }
        int sampleRate = mpegAudioHeader.sampleRate;
        long durationUs = Util.scaleLargeTimestamp((long) numFrames, ((long) (sampleRate >= 32000 ? 1152 : 576)) * C0277C.MICROS_PER_SECOND, (long) sampleRate);
        int entryCount = frame.readUnsignedShort();
        int scale = frame.readUnsignedShort();
        int entrySize = frame.readUnsignedShort();
        frame.skipBytes(2);
        position += (long) mpegAudioHeader.frameSize;
        long[] timesUs = new long[(entryCount + 1)];
        long[] positions = new long[(entryCount + 1)];
        timesUs[0] = 0;
        positions[0] = position;
        for (int index = 1; index < timesUs.length; index++) {
            int segmentSize;
            long j;
            switch (entrySize) {
                case C1628R.styleable.RecyclerView_layoutManager /*1*/:
                    segmentSize = frame.readUnsignedByte();
                    break;
                case C1628R.styleable.RecyclerView_spanCount /*2*/:
                    segmentSize = frame.readUnsignedShort();
                    break;
                case C1628R.styleable.RecyclerView_reverseLayout /*3*/:
                    segmentSize = frame.readUnsignedInt24();
                    break;
                case C1628R.styleable.RecyclerView_stackFromEnd /*4*/:
                    segmentSize = frame.readUnsignedIntToInt();
                    break;
                default:
                    return null;
            }
            position += (long) (segmentSize * scale);
            timesUs[index] = (((long) index) * durationUs) / ((long) entryCount);
            if (inputLength == -1) {
                j = position;
            } else {
                j = Math.min(inputLength, position);
            }
            positions[index] = j;
        }
        return new VbriSeeker(timesUs, positions, durationUs);
    }

    private VbriSeeker(long[] timesUs, long[] positions, long durationUs) {
        this.timesUs = timesUs;
        this.positions = positions;
        this.durationUs = durationUs;
    }

    public boolean isSeekable() {
        return true;
    }

    public long getPosition(long timeUs) {
        return this.positions[Util.binarySearchFloor(this.timesUs, timeUs, true, true)];
    }

    public long getTimeUs(long position) {
        return this.timesUs[Util.binarySearchFloor(this.positions, position, true, true)];
    }

    public long getDurationUs() {
        return this.durationUs;
    }
}