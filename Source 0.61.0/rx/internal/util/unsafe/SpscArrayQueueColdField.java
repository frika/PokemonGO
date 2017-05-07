package rx.internal.util.unsafe;

import com.google.android.exoplayer.util.MpegAudioHeader;

/* compiled from: SpscArrayQueue */
abstract class SpscArrayQueueColdField<E> extends ConcurrentCircularArrayQueue<E> {
    private static final Integer MAX_LOOK_AHEAD_STEP;
    protected final int lookAheadStep;

    static {
        MAX_LOOK_AHEAD_STEP = Integer.getInteger("jctools.spsc.max.lookahead.step", MpegAudioHeader.MAX_FRAME_SIZE_BYTES);
    }

    public SpscArrayQueueColdField(int capacity) {
        super(capacity);
        this.lookAheadStep = Math.min(capacity / 4, MAX_LOOK_AHEAD_STEP.intValue());
    }
}
