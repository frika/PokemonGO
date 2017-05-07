package com.google.android.exoplayer.audio;

import android.annotation.TargetApi;
import android.media.AudioTimestamp;
import android.media.MediaFormat;
import android.media.PlaybackParams;
import android.os.ConditionVariable;
import android.os.SystemClock;
import android.util.Log;
import com.google.android.exoplayer.C0277C;
import com.google.android.exoplayer.util.Ac3Util;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.DtsUtil;
import com.google.android.exoplayer.util.MimeTypes;
import com.google.android.exoplayer.util.Util;
import com.mopub.volley.DefaultRetryPolicy;
import com.mopub.volley.Request;
import com.nianticlabs.pokemongoplus.ble.BluetoothGattSupport;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.mediation.mraid.properties.MRAIDResizeProperties;
import com.upsight.mediation.vast.model.VASTModel;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import spacemadness.com.lunarconsole.C1628R;

@TargetApi(16)
public final class AudioTrack {
    private static final int BUFFER_MULTIPLICATION_FACTOR = 4;
    public static final long CURRENT_POSITION_NOT_SET = Long.MIN_VALUE;
    private static final long MAX_AUDIO_TIMESTAMP_OFFSET_US = 5000000;
    private static final long MAX_BUFFER_DURATION_US = 750000;
    private static final long MAX_LATENCY_US = 5000000;
    private static final int MAX_PLAYHEAD_OFFSET_COUNT = 10;
    private static final long MIN_BUFFER_DURATION_US = 250000;
    private static final int MIN_PLAYHEAD_OFFSET_SAMPLE_INTERVAL_US = 30000;
    private static final int MIN_TIMESTAMP_SAMPLE_INTERVAL_US = 500000;
    private static final long PASSTHROUGH_BUFFER_DURATION_US = 250000;
    public static final int RESULT_BUFFER_CONSUMED = 2;
    public static final int RESULT_POSITION_DISCONTINUITY = 1;
    public static final int SESSION_ID_NOT_SET = 0;
    private static final int START_IN_SYNC = 1;
    private static final int START_NEED_SYNC = 2;
    private static final int START_NOT_SET = 0;
    private static final String TAG = "AudioTrack";
    public static boolean enablePreV21AudioSessionWorkaround;
    public static boolean failOnSpuriousAudioTimestamp;
    private final AudioCapabilities audioCapabilities;
    private boolean audioTimestampSet;
    private android.media.AudioTrack audioTrack;
    private final AudioTrackUtil audioTrackUtil;
    private int bufferBytesRemaining;
    private int bufferSize;
    private long bufferSizeUs;
    private int channelConfig;
    private int encoding;
    private int framesPerEncodedSample;
    private Method getLatencyMethod;
    private android.media.AudioTrack keepSessionIdAudioTrack;
    private long lastPlayheadSampleTimeUs;
    private long lastTimestampSampleTimeUs;
    private long latencyUs;
    private int nextPlayheadOffsetIndex;
    private boolean passthrough;
    private int pcmFrameSize;
    private int playheadOffsetCount;
    private final long[] playheadOffsets;
    private final ConditionVariable releasingConditionVariable;
    private long resumeSystemTimeUs;
    private int sampleRate;
    private long smoothedPlayheadOffsetUs;
    private int startMediaTimeState;
    private long startMediaTimeUs;
    private final int streamType;
    private long submittedEncodedFrames;
    private long submittedPcmBytes;
    private byte[] temporaryBuffer;
    private int temporaryBufferOffset;
    private float volume;

    /* renamed from: com.google.android.exoplayer.audio.AudioTrack.1 */
    class C02931 extends Thread {
        final /* synthetic */ android.media.AudioTrack val$toRelease;

        C02931(android.media.AudioTrack audioTrack) {
            this.val$toRelease = audioTrack;
        }

        public void run() {
            try {
                this.val$toRelease.flush();
                this.val$toRelease.release();
            } finally {
                AudioTrack.this.releasingConditionVariable.open();
            }
        }
    }

    /* renamed from: com.google.android.exoplayer.audio.AudioTrack.2 */
    class C02942 extends Thread {
        final /* synthetic */ android.media.AudioTrack val$toRelease;

        C02942(android.media.AudioTrack audioTrack) {
            this.val$toRelease = audioTrack;
        }

        public void run() {
            this.val$toRelease.release();
        }
    }

    private static class AudioTrackUtil {
        protected android.media.AudioTrack audioTrack;
        private long endPlaybackHeadPosition;
        private long lastRawPlaybackHeadPosition;
        private boolean needsPassthroughWorkaround;
        private long passthroughWorkaroundPauseOffset;
        private long rawPlaybackHeadWrapCount;
        private int sampleRate;
        private long stopPlaybackHeadPosition;
        private long stopTimestampUs;

        private AudioTrackUtil() {
        }

        public void reconfigure(android.media.AudioTrack audioTrack, boolean needsPassthroughWorkaround) {
            this.audioTrack = audioTrack;
            this.needsPassthroughWorkaround = needsPassthroughWorkaround;
            this.stopTimestampUs = -1;
            this.lastRawPlaybackHeadPosition = 0;
            this.rawPlaybackHeadWrapCount = 0;
            this.passthroughWorkaroundPauseOffset = 0;
            if (audioTrack != null) {
                this.sampleRate = audioTrack.getSampleRate();
            }
        }

        public void handleEndOfStream(long submittedFrames) {
            this.stopPlaybackHeadPosition = getPlaybackHeadPosition();
            this.stopTimestampUs = SystemClock.elapsedRealtime() * 1000;
            this.endPlaybackHeadPosition = submittedFrames;
            this.audioTrack.stop();
        }

        public void pause() {
            if (this.stopTimestampUs == -1) {
                this.audioTrack.pause();
            }
        }

        public long getPlaybackHeadPosition() {
            if (this.stopTimestampUs != -1) {
                return Math.min(this.endPlaybackHeadPosition, this.stopPlaybackHeadPosition + ((((long) this.sampleRate) * ((SystemClock.elapsedRealtime() * 1000) - this.stopTimestampUs)) / C0277C.MICROS_PER_SECOND));
            }
            int state = this.audioTrack.getPlayState();
            if (state == AudioTrack.START_IN_SYNC) {
                return 0;
            }
            long rawPlaybackHeadPosition = 4294967295L & ((long) this.audioTrack.getPlaybackHeadPosition());
            if (this.needsPassthroughWorkaround) {
                if (state == AudioTrack.START_NEED_SYNC && rawPlaybackHeadPosition == 0) {
                    this.passthroughWorkaroundPauseOffset = this.lastRawPlaybackHeadPosition;
                }
                rawPlaybackHeadPosition += this.passthroughWorkaroundPauseOffset;
            }
            if (this.lastRawPlaybackHeadPosition > rawPlaybackHeadPosition) {
                this.rawPlaybackHeadWrapCount++;
            }
            this.lastRawPlaybackHeadPosition = rawPlaybackHeadPosition;
            return (this.rawPlaybackHeadWrapCount << 32) + rawPlaybackHeadPosition;
        }

        public long getPlaybackHeadPositionUs() {
            return (getPlaybackHeadPosition() * C0277C.MICROS_PER_SECOND) / ((long) this.sampleRate);
        }

        public boolean updateTimestamp() {
            return false;
        }

        public long getTimestampNanoTime() {
            throw new UnsupportedOperationException();
        }

        public long getTimestampFramePosition() {
            throw new UnsupportedOperationException();
        }

        public void setPlaybackParameters(PlaybackParams playbackParams) {
            throw new UnsupportedOperationException();
        }

        public float getPlaybackSpeed() {
            return DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
        }
    }

    @TargetApi(19)
    private static class AudioTrackUtilV19 extends AudioTrackUtil {
        private final AudioTimestamp audioTimestamp;
        private long lastRawTimestampFramePosition;
        private long lastTimestampFramePosition;
        private long rawTimestampFramePositionWrapCount;

        public AudioTrackUtilV19() {
            super();
            this.audioTimestamp = new AudioTimestamp();
        }

        public void reconfigure(android.media.AudioTrack audioTrack, boolean needsPassthroughWorkaround) {
            super.reconfigure(audioTrack, needsPassthroughWorkaround);
            this.rawTimestampFramePositionWrapCount = 0;
            this.lastRawTimestampFramePosition = 0;
            this.lastTimestampFramePosition = 0;
        }

        public boolean updateTimestamp() {
            boolean updated = this.audioTrack.getTimestamp(this.audioTimestamp);
            if (updated) {
                long rawFramePosition = this.audioTimestamp.framePosition;
                if (this.lastRawTimestampFramePosition > rawFramePosition) {
                    this.rawTimestampFramePositionWrapCount++;
                }
                this.lastRawTimestampFramePosition = rawFramePosition;
                this.lastTimestampFramePosition = (this.rawTimestampFramePositionWrapCount << 32) + rawFramePosition;
            }
            return updated;
        }

        public long getTimestampNanoTime() {
            return this.audioTimestamp.nanoTime;
        }

        public long getTimestampFramePosition() {
            return this.lastTimestampFramePosition;
        }
    }

    @TargetApi(23)
    private static class AudioTrackUtilV23 extends AudioTrackUtilV19 {
        private PlaybackParams playbackParams;
        private float playbackSpeed;

        public AudioTrackUtilV23() {
            this.playbackSpeed = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
        }

        public void reconfigure(android.media.AudioTrack audioTrack, boolean needsPassthroughWorkaround) {
            super.reconfigure(audioTrack, needsPassthroughWorkaround);
            maybeApplyPlaybackParams();
        }

        public void setPlaybackParameters(PlaybackParams playbackParams) {
            if (playbackParams == null) {
                playbackParams = new PlaybackParams();
            }
            playbackParams = playbackParams.allowDefaults();
            this.playbackParams = playbackParams;
            this.playbackSpeed = playbackParams.getSpeed();
            maybeApplyPlaybackParams();
        }

        public float getPlaybackSpeed() {
            return this.playbackSpeed;
        }

        private void maybeApplyPlaybackParams() {
            if (this.audioTrack != null && this.playbackParams != null) {
                this.audioTrack.setPlaybackParams(this.playbackParams);
            }
        }
    }

    public static final class InitializationException extends Exception {
        public final int audioTrackState;

        public InitializationException(int audioTrackState, int sampleRate, int channelConfig, int bufferSize) {
            super("AudioTrack init failed: " + audioTrackState + ", Config(" + sampleRate + ", " + channelConfig + ", " + bufferSize + ")");
            this.audioTrackState = audioTrackState;
        }
    }

    public static final class InvalidAudioTrackTimestampException extends RuntimeException {
        public InvalidAudioTrackTimestampException(String message) {
            super(message);
        }
    }

    public static final class WriteException extends Exception {
        public final int errorCode;

        public WriteException(int errorCode) {
            super("AudioTrack write failed: " + errorCode);
            this.errorCode = errorCode;
        }
    }

    static {
        enablePreV21AudioSessionWorkaround = false;
        failOnSpuriousAudioTimestamp = false;
    }

    public AudioTrack() {
        this(null, 3);
    }

    public AudioTrack(AudioCapabilities audioCapabilities, int streamType) {
        this.audioCapabilities = audioCapabilities;
        this.streamType = streamType;
        this.releasingConditionVariable = new ConditionVariable(true);
        if (Util.SDK_INT >= 18) {
            try {
                this.getLatencyMethod = android.media.AudioTrack.class.getMethod("getLatency", (Class[]) null);
            } catch (NoSuchMethodException e) {
            }
        }
        if (Util.SDK_INT >= 23) {
            this.audioTrackUtil = new AudioTrackUtilV23();
        } else if (Util.SDK_INT >= 19) {
            this.audioTrackUtil = new AudioTrackUtilV19();
        } else {
            this.audioTrackUtil = new AudioTrackUtil();
        }
        this.playheadOffsets = new long[MAX_PLAYHEAD_OFFSET_COUNT];
        this.volume = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
        this.startMediaTimeState = START_NOT_SET;
    }

    public boolean isPassthroughSupported(String mimeType) {
        return this.audioCapabilities != null && this.audioCapabilities.supportsEncoding(getEncodingForMimeType(mimeType));
    }

    public boolean isInitialized() {
        return this.audioTrack != null;
    }

    public long getCurrentPositionUs(boolean sourceEnded) {
        if (!hasCurrentPositionUs()) {
            return CURRENT_POSITION_NOT_SET;
        }
        if (this.audioTrack.getPlayState() == 3) {
            maybeSampleSyncParams();
        }
        long systemClockUs = System.nanoTime() / 1000;
        if (this.audioTimestampSet) {
            long framesDiff = durationUsToFrames((long) (((float) (systemClockUs - (this.audioTrackUtil.getTimestampNanoTime() / 1000))) * this.audioTrackUtil.getPlaybackSpeed()));
            return framesToDurationUs(this.audioTrackUtil.getTimestampFramePosition() + framesDiff) + this.startMediaTimeUs;
        }
        long currentPositionUs;
        if (this.playheadOffsetCount == 0) {
            currentPositionUs = this.audioTrackUtil.getPlaybackHeadPositionUs() + this.startMediaTimeUs;
        } else {
            currentPositionUs = (this.smoothedPlayheadOffsetUs + systemClockUs) + this.startMediaTimeUs;
        }
        if (sourceEnded) {
            return currentPositionUs;
        }
        return currentPositionUs - this.latencyUs;
    }

    public void configure(MediaFormat format, boolean passthrough) {
        configure(format, passthrough, START_NOT_SET);
    }

    public void configure(MediaFormat format, boolean passthrough, int specifiedBufferSize) {
        int channelConfig;
        int channelCount = format.getInteger("channel-count");
        switch (channelCount) {
            case START_IN_SYNC /*1*/:
                channelConfig = BUFFER_MULTIPLICATION_FACTOR;
                break;
            case START_NEED_SYNC /*2*/:
                channelConfig = 12;
                break;
            case C1628R.styleable.RecyclerView_reverseLayout /*3*/:
                channelConfig = 28;
                break;
            case BUFFER_MULTIPLICATION_FACTOR /*4*/:
                channelConfig = 204;
                break;
            case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_CENTER /*5*/:
                channelConfig = 220;
                break;
            case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_RIGHT /*6*/:
                channelConfig = 252;
                break;
            case Request.Method.PATCH /*7*/:
                channelConfig = 1276;
                break;
            case BluetoothGattSupport.GATT_INSUF_AUTHENTICATION /*8*/:
                channelConfig = C0277C.CHANNEL_OUT_7POINT1_SURROUND;
                break;
            default:
                throw new IllegalArgumentException("Unsupported channel count: " + channelCount);
        }
        int sampleRate = format.getInteger("sample-rate");
        int encoding = passthrough ? getEncodingForMimeType(format.getString("mime")) : START_NEED_SYNC;
        if (!isInitialized() || this.sampleRate != sampleRate || this.channelConfig != channelConfig || this.encoding != encoding) {
            long j;
            reset();
            this.encoding = encoding;
            this.passthrough = passthrough;
            this.sampleRate = sampleRate;
            this.channelConfig = channelConfig;
            this.pcmFrameSize = channelCount * START_NEED_SYNC;
            if (specifiedBufferSize != 0) {
                this.bufferSize = specifiedBufferSize;
            } else if (!passthrough) {
                int minBufferSize = android.media.AudioTrack.getMinBufferSize(sampleRate, channelConfig, encoding);
                Assertions.checkState(minBufferSize != -2);
                int multipliedBufferSize = minBufferSize * BUFFER_MULTIPLICATION_FACTOR;
                int minAppBufferSize = ((int) durationUsToFrames(PASSTHROUGH_BUFFER_DURATION_US)) * this.pcmFrameSize;
                int maxAppBufferSize = (int) Math.max((long) minBufferSize, durationUsToFrames(MAX_BUFFER_DURATION_US) * ((long) this.pcmFrameSize));
                if (multipliedBufferSize >= minAppBufferSize) {
                    minAppBufferSize = multipliedBufferSize > maxAppBufferSize ? maxAppBufferSize : multipliedBufferSize;
                }
                this.bufferSize = minAppBufferSize;
            } else if (encoding == 5 || encoding == 6) {
                this.bufferSize = 20480;
            } else {
                this.bufferSize = 49152;
            }
            if (passthrough) {
                j = -1;
            } else {
                j = framesToDurationUs(pcmBytesToFrames((long) this.bufferSize));
            }
            this.bufferSizeUs = j;
        }
    }

    public int initialize() throws InitializationException {
        return initialize(START_NOT_SET);
    }

    public int initialize(int sessionId) throws InitializationException {
        this.releasingConditionVariable.block();
        if (sessionId == 0) {
            this.audioTrack = new android.media.AudioTrack(this.streamType, this.sampleRate, this.channelConfig, this.encoding, this.bufferSize, START_IN_SYNC);
        } else {
            this.audioTrack = new android.media.AudioTrack(this.streamType, this.sampleRate, this.channelConfig, this.encoding, this.bufferSize, START_IN_SYNC, sessionId);
        }
        checkAudioTrackInitialized();
        sessionId = this.audioTrack.getAudioSessionId();
        if (enablePreV21AudioSessionWorkaround && Util.SDK_INT < 21) {
            if (!(this.keepSessionIdAudioTrack == null || sessionId == this.keepSessionIdAudioTrack.getAudioSessionId())) {
                releaseKeepSessionIdAudioTrack();
            }
            if (this.keepSessionIdAudioTrack == null) {
                this.keepSessionIdAudioTrack = new android.media.AudioTrack(this.streamType, UpsightLogger.MAX_LENGTH, BUFFER_MULTIPLICATION_FACTOR, START_NEED_SYNC, START_NEED_SYNC, START_NOT_SET, sessionId);
            }
        }
        this.audioTrackUtil.reconfigure(this.audioTrack, needsPassthroughWorkarounds());
        setAudioTrackVolume();
        return sessionId;
    }

    public int getBufferSize() {
        return this.bufferSize;
    }

    public long getBufferSizeUs() {
        return this.bufferSizeUs;
    }

    public void play() {
        if (isInitialized()) {
            this.resumeSystemTimeUs = System.nanoTime() / 1000;
            this.audioTrack.play();
        }
    }

    public void handleDiscontinuity() {
        if (this.startMediaTimeState == START_IN_SYNC) {
            this.startMediaTimeState = START_NEED_SYNC;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int handleBuffer(java.nio.ByteBuffer r23, int r24, int r25, long r26) throws com.google.android.exoplayer.audio.AudioTrack.WriteException {
        /*
        r22 = this;
        if (r25 != 0) goto L_0x0004;
    L_0x0002:
        r11 = 2;
    L_0x0003:
        return r11;
    L_0x0004:
        r16 = r22.needsPassthroughWorkarounds();
        if (r16 == 0) goto L_0x0042;
    L_0x000a:
        r0 = r22;
        r0 = r0.audioTrack;
        r16 = r0;
        r16 = r16.getPlayState();
        r17 = 2;
        r0 = r16;
        r1 = r17;
        if (r0 != r1) goto L_0x001e;
    L_0x001c:
        r11 = 0;
        goto L_0x0003;
    L_0x001e:
        r0 = r22;
        r0 = r0.audioTrack;
        r16 = r0;
        r16 = r16.getPlayState();
        r17 = 1;
        r0 = r16;
        r1 = r17;
        if (r0 != r1) goto L_0x0042;
    L_0x0030:
        r0 = r22;
        r0 = r0.audioTrackUtil;
        r16 = r0;
        r16 = r16.getPlaybackHeadPosition();
        r18 = 0;
        r16 = (r16 > r18 ? 1 : (r16 == r18 ? 0 : -1));
        if (r16 == 0) goto L_0x0042;
    L_0x0040:
        r11 = 0;
        goto L_0x0003;
    L_0x0042:
        r11 = 0;
        r0 = r22;
        r0 = r0.bufferBytesRemaining;
        r16 = r0;
        if (r16 != 0) goto L_0x00f9;
    L_0x004b:
        r0 = r25;
        r1 = r22;
        r1.bufferBytesRemaining = r0;
        r23.position(r24);
        r0 = r22;
        r0 = r0.passthrough;
        r16 = r0;
        if (r16 == 0) goto L_0x0078;
    L_0x005c:
        r0 = r22;
        r0 = r0.framesPerEncodedSample;
        r16 = r0;
        if (r16 != 0) goto L_0x0078;
    L_0x0064:
        r0 = r22;
        r0 = r0.encoding;
        r16 = r0;
        r0 = r16;
        r1 = r23;
        r16 = getFramesPerEncodedSample(r0, r1);
        r0 = r16;
        r1 = r22;
        r1.framesPerEncodedSample = r0;
    L_0x0078:
        r0 = r22;
        r0 = r0.passthrough;
        r16 = r0;
        if (r16 == 0) goto L_0x0172;
    L_0x0080:
        r0 = r22;
        r0 = r0.framesPerEncodedSample;
        r16 = r0;
        r0 = r16;
        r14 = (long) r0;
    L_0x0089:
        r0 = r22;
        r4 = r0.framesToDurationUs(r14);
        r6 = r26 - r4;
        r0 = r22;
        r0 = r0.startMediaTimeState;
        r16 = r0;
        if (r16 != 0) goto L_0x0181;
    L_0x0099:
        r16 = 0;
        r0 = r16;
        r16 = java.lang.Math.max(r0, r6);
        r0 = r16;
        r2 = r22;
        r2.startMediaTimeUs = r0;
        r16 = 1;
        r0 = r16;
        r1 = r22;
        r1.startMediaTimeState = r0;
    L_0x00af:
        r16 = com.google.android.exoplayer.util.Util.SDK_INT;
        r17 = 21;
        r0 = r16;
        r1 = r17;
        if (r0 >= r1) goto L_0x00f9;
    L_0x00b9:
        r0 = r22;
        r0 = r0.temporaryBuffer;
        r16 = r0;
        if (r16 == 0) goto L_0x00d2;
    L_0x00c1:
        r0 = r22;
        r0 = r0.temporaryBuffer;
        r16 = r0;
        r0 = r16;
        r0 = r0.length;
        r16 = r0;
        r0 = r16;
        r1 = r25;
        if (r0 >= r1) goto L_0x00de;
    L_0x00d2:
        r0 = r25;
        r0 = new byte[r0];
        r16 = r0;
        r0 = r16;
        r1 = r22;
        r1.temporaryBuffer = r0;
    L_0x00de:
        r0 = r22;
        r0 = r0.temporaryBuffer;
        r16 = r0;
        r17 = 0;
        r0 = r23;
        r1 = r16;
        r2 = r17;
        r3 = r25;
        r0.get(r1, r2, r3);
        r16 = 0;
        r0 = r16;
        r1 = r22;
        r1.temporaryBufferOffset = r0;
    L_0x00f9:
        r10 = 0;
        r16 = com.google.android.exoplayer.util.Util.SDK_INT;
        r17 = 21;
        r0 = r16;
        r1 = r17;
        if (r0 >= r1) goto L_0x020e;
    L_0x0104:
        r0 = r22;
        r0 = r0.submittedPcmBytes;
        r16 = r0;
        r0 = r22;
        r0 = r0.audioTrackUtil;
        r18 = r0;
        r18 = r18.getPlaybackHeadPosition();
        r0 = r22;
        r0 = r0.pcmFrameSize;
        r20 = r0;
        r0 = r20;
        r0 = (long) r0;
        r20 = r0;
        r18 = r18 * r20;
        r16 = r16 - r18;
        r0 = r16;
        r8 = (int) r0;
        r0 = r22;
        r0 = r0.bufferSize;
        r16 = r0;
        r9 = r16 - r8;
        if (r9 <= 0) goto L_0x0168;
    L_0x0130:
        r0 = r22;
        r0 = r0.bufferBytesRemaining;
        r16 = r0;
        r0 = r16;
        r9 = java.lang.Math.min(r0, r9);
        r0 = r22;
        r0 = r0.audioTrack;
        r16 = r0;
        r0 = r22;
        r0 = r0.temporaryBuffer;
        r17 = r0;
        r0 = r22;
        r0 = r0.temporaryBufferOffset;
        r18 = r0;
        r0 = r16;
        r1 = r17;
        r2 = r18;
        r10 = r0.write(r1, r2, r9);
        if (r10 < 0) goto L_0x0168;
    L_0x015a:
        r0 = r22;
        r0 = r0.temporaryBufferOffset;
        r16 = r0;
        r16 = r16 + r10;
        r0 = r16;
        r1 = r22;
        r1.temporaryBufferOffset = r0;
    L_0x0168:
        if (r10 >= 0) goto L_0x0226;
    L_0x016a:
        r16 = new com.google.android.exoplayer.audio.AudioTrack$WriteException;
        r0 = r16;
        r0.<init>(r10);
        throw r16;
    L_0x0172:
        r0 = r25;
        r0 = (long) r0;
        r16 = r0;
        r0 = r22;
        r1 = r16;
        r14 = r0.pcmBytesToFrames(r1);
        goto L_0x0089;
    L_0x0181:
        r0 = r22;
        r0 = r0.startMediaTimeUs;
        r16 = r0;
        r18 = r22.getSubmittedFrames();
        r0 = r22;
        r1 = r18;
        r18 = r0.framesToDurationUs(r1);
        r12 = r16 + r18;
        r0 = r22;
        r0 = r0.startMediaTimeState;
        r16 = r0;
        r17 = 1;
        r0 = r16;
        r1 = r17;
        if (r0 != r1) goto L_0x01e4;
    L_0x01a3:
        r16 = r12 - r6;
        r16 = java.lang.Math.abs(r16);
        r18 = 200000; // 0x30d40 float:2.8026E-40 double:9.8813E-319;
        r16 = (r16 > r18 ? 1 : (r16 == r18 ? 0 : -1));
        if (r16 <= 0) goto L_0x01e4;
    L_0x01b0:
        r16 = "AudioTrack";
        r17 = new java.lang.StringBuilder;
        r17.<init>();
        r18 = "Discontinuity detected [expected ";
        r17 = r17.append(r18);
        r0 = r17;
        r17 = r0.append(r12);
        r18 = ", got ";
        r17 = r17.append(r18);
        r0 = r17;
        r17 = r0.append(r6);
        r18 = "]";
        r17 = r17.append(r18);
        r17 = r17.toString();
        android.util.Log.e(r16, r17);
        r16 = 2;
        r0 = r16;
        r1 = r22;
        r1.startMediaTimeState = r0;
    L_0x01e4:
        r0 = r22;
        r0 = r0.startMediaTimeState;
        r16 = r0;
        r17 = 2;
        r0 = r16;
        r1 = r17;
        if (r0 != r1) goto L_0x00af;
    L_0x01f2:
        r0 = r22;
        r0 = r0.startMediaTimeUs;
        r16 = r0;
        r18 = r6 - r12;
        r16 = r16 + r18;
        r0 = r16;
        r2 = r22;
        r2.startMediaTimeUs = r0;
        r16 = 1;
        r0 = r16;
        r1 = r22;
        r1.startMediaTimeState = r0;
        r11 = r11 | 1;
        goto L_0x00af;
    L_0x020e:
        r0 = r22;
        r0 = r0.audioTrack;
        r16 = r0;
        r0 = r22;
        r0 = r0.bufferBytesRemaining;
        r17 = r0;
        r0 = r16;
        r1 = r23;
        r2 = r17;
        r10 = writeNonBlockingV21(r0, r1, r2);
        goto L_0x0168;
    L_0x0226:
        r0 = r22;
        r0 = r0.bufferBytesRemaining;
        r16 = r0;
        r16 = r16 - r10;
        r0 = r16;
        r1 = r22;
        r1.bufferBytesRemaining = r0;
        r0 = r22;
        r0 = r0.passthrough;
        r16 = r0;
        if (r16 != 0) goto L_0x024d;
    L_0x023c:
        r0 = r22;
        r0 = r0.submittedPcmBytes;
        r16 = r0;
        r0 = (long) r10;
        r18 = r0;
        r16 = r16 + r18;
        r0 = r16;
        r2 = r22;
        r2.submittedPcmBytes = r0;
    L_0x024d:
        r0 = r22;
        r0 = r0.bufferBytesRemaining;
        r16 = r0;
        if (r16 != 0) goto L_0x0003;
    L_0x0255:
        r0 = r22;
        r0 = r0.passthrough;
        r16 = r0;
        if (r16 == 0) goto L_0x0276;
    L_0x025d:
        r0 = r22;
        r0 = r0.submittedEncodedFrames;
        r16 = r0;
        r0 = r22;
        r0 = r0.framesPerEncodedSample;
        r18 = r0;
        r0 = r18;
        r0 = (long) r0;
        r18 = r0;
        r16 = r16 + r18;
        r0 = r16;
        r2 = r22;
        r2.submittedEncodedFrames = r0;
    L_0x0276:
        r11 = r11 | 2;
        goto L_0x0003;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer.audio.AudioTrack.handleBuffer(java.nio.ByteBuffer, int, int, long):int");
    }

    public void handleEndOfStream() {
        if (isInitialized()) {
            this.audioTrackUtil.handleEndOfStream(getSubmittedFrames());
        }
    }

    @TargetApi(21)
    private static int writeNonBlockingV21(android.media.AudioTrack audioTrack, ByteBuffer buffer, int size) {
        return audioTrack.write(buffer, size, START_IN_SYNC);
    }

    public boolean hasPendingData() {
        return isInitialized() && (getSubmittedFrames() > this.audioTrackUtil.getPlaybackHeadPosition() || overrideHasPendingData());
    }

    public void setPlaybackParams(PlaybackParams playbackParams) {
        this.audioTrackUtil.setPlaybackParameters(playbackParams);
    }

    public void setVolume(float volume) {
        if (this.volume != volume) {
            this.volume = volume;
            setAudioTrackVolume();
        }
    }

    private void setAudioTrackVolume() {
        if (!isInitialized()) {
            return;
        }
        if (Util.SDK_INT >= 21) {
            setAudioTrackVolumeV21(this.audioTrack, this.volume);
        } else {
            setAudioTrackVolumeV3(this.audioTrack, this.volume);
        }
    }

    @TargetApi(21)
    private static void setAudioTrackVolumeV21(android.media.AudioTrack audioTrack, float volume) {
        audioTrack.setVolume(volume);
    }

    private static void setAudioTrackVolumeV3(android.media.AudioTrack audioTrack, float volume) {
        audioTrack.setStereoVolume(volume, volume);
    }

    public void pause() {
        if (isInitialized()) {
            resetSyncParams();
            this.audioTrackUtil.pause();
        }
    }

    public void reset() {
        if (isInitialized()) {
            this.submittedPcmBytes = 0;
            this.submittedEncodedFrames = 0;
            this.framesPerEncodedSample = START_NOT_SET;
            this.bufferBytesRemaining = START_NOT_SET;
            this.startMediaTimeState = START_NOT_SET;
            this.latencyUs = 0;
            resetSyncParams();
            if (this.audioTrack.getPlayState() == 3) {
                this.audioTrack.pause();
            }
            android.media.AudioTrack toRelease = this.audioTrack;
            this.audioTrack = null;
            this.audioTrackUtil.reconfigure(null, false);
            this.releasingConditionVariable.close();
            new C02931(toRelease).start();
        }
    }

    public void release() {
        reset();
        releaseKeepSessionIdAudioTrack();
    }

    private void releaseKeepSessionIdAudioTrack() {
        if (this.keepSessionIdAudioTrack != null) {
            android.media.AudioTrack toRelease = this.keepSessionIdAudioTrack;
            this.keepSessionIdAudioTrack = null;
            new C02942(toRelease).start();
        }
    }

    private boolean hasCurrentPositionUs() {
        return isInitialized() && this.startMediaTimeState != 0;
    }

    private void maybeSampleSyncParams() {
        long playbackPositionUs = this.audioTrackUtil.getPlaybackHeadPositionUs();
        if (playbackPositionUs != 0) {
            long systemClockUs = System.nanoTime() / 1000;
            if (systemClockUs - this.lastPlayheadSampleTimeUs >= VASTModel.DOWNLOAD_TIMEOUT_LIMIT) {
                this.playheadOffsets[this.nextPlayheadOffsetIndex] = playbackPositionUs - systemClockUs;
                this.nextPlayheadOffsetIndex = (this.nextPlayheadOffsetIndex + START_IN_SYNC) % MAX_PLAYHEAD_OFFSET_COUNT;
                if (this.playheadOffsetCount < MAX_PLAYHEAD_OFFSET_COUNT) {
                    this.playheadOffsetCount += START_IN_SYNC;
                }
                this.lastPlayheadSampleTimeUs = systemClockUs;
                this.smoothedPlayheadOffsetUs = 0;
                for (int i = START_NOT_SET; i < this.playheadOffsetCount; i += START_IN_SYNC) {
                    this.smoothedPlayheadOffsetUs += this.playheadOffsets[i] / ((long) this.playheadOffsetCount);
                }
            }
            if (!needsPassthroughWorkarounds() && systemClockUs - this.lastTimestampSampleTimeUs >= 500000) {
                this.audioTimestampSet = this.audioTrackUtil.updateTimestamp();
                if (this.audioTimestampSet) {
                    long audioTimestampUs = this.audioTrackUtil.getTimestampNanoTime() / 1000;
                    long audioTimestampFramePosition = this.audioTrackUtil.getTimestampFramePosition();
                    if (audioTimestampUs < this.resumeSystemTimeUs) {
                        this.audioTimestampSet = false;
                    } else if (Math.abs(audioTimestampUs - systemClockUs) > MAX_LATENCY_US) {
                        message = "Spurious audio timestamp (system clock mismatch): " + audioTimestampFramePosition + ", " + audioTimestampUs + ", " + systemClockUs + ", " + playbackPositionUs;
                        if (failOnSpuriousAudioTimestamp) {
                            throw new InvalidAudioTrackTimestampException(message);
                        }
                        Log.w(TAG, message);
                        this.audioTimestampSet = false;
                    } else if (Math.abs(framesToDurationUs(audioTimestampFramePosition) - playbackPositionUs) > MAX_LATENCY_US) {
                        message = "Spurious audio timestamp (frame position mismatch): " + audioTimestampFramePosition + ", " + audioTimestampUs + ", " + systemClockUs + ", " + playbackPositionUs;
                        if (failOnSpuriousAudioTimestamp) {
                            throw new InvalidAudioTrackTimestampException(message);
                        }
                        Log.w(TAG, message);
                        this.audioTimestampSet = false;
                    }
                }
                if (!(this.getLatencyMethod == null || this.passthrough)) {
                    try {
                        this.latencyUs = (((long) ((Integer) this.getLatencyMethod.invoke(this.audioTrack, (Object[]) null)).intValue()) * 1000) - this.bufferSizeUs;
                        this.latencyUs = Math.max(this.latencyUs, 0);
                        if (this.latencyUs > MAX_LATENCY_US) {
                            Log.w(TAG, "Ignoring impossibly large audio latency: " + this.latencyUs);
                            this.latencyUs = 0;
                        }
                    } catch (Exception e) {
                        this.getLatencyMethod = null;
                    }
                }
                this.lastTimestampSampleTimeUs = systemClockUs;
            }
        }
    }

    private void checkAudioTrackInitialized() throws InitializationException {
        int state = this.audioTrack.getState();
        if (state != START_IN_SYNC) {
            try {
                this.audioTrack.release();
            } catch (Exception e) {
            } finally {
                this.audioTrack = null;
            }
            throw new InitializationException(state, this.sampleRate, this.channelConfig, this.bufferSize);
        }
    }

    private long pcmBytesToFrames(long byteCount) {
        return byteCount / ((long) this.pcmFrameSize);
    }

    private long framesToDurationUs(long frameCount) {
        return (C0277C.MICROS_PER_SECOND * frameCount) / ((long) this.sampleRate);
    }

    private long durationUsToFrames(long durationUs) {
        return (((long) this.sampleRate) * durationUs) / C0277C.MICROS_PER_SECOND;
    }

    private long getSubmittedFrames() {
        return this.passthrough ? this.submittedEncodedFrames : pcmBytesToFrames(this.submittedPcmBytes);
    }

    private void resetSyncParams() {
        this.smoothedPlayheadOffsetUs = 0;
        this.playheadOffsetCount = START_NOT_SET;
        this.nextPlayheadOffsetIndex = START_NOT_SET;
        this.lastPlayheadSampleTimeUs = 0;
        this.audioTimestampSet = false;
        this.lastTimestampSampleTimeUs = 0;
    }

    private boolean needsPassthroughWorkarounds() {
        return Util.SDK_INT < 23 && (this.encoding == 5 || this.encoding == 6);
    }

    private boolean overrideHasPendingData() {
        return needsPassthroughWorkarounds() && this.audioTrack.getPlayState() == START_NEED_SYNC && this.audioTrack.getPlaybackHeadPosition() == 0;
    }

    private static int getEncodingForMimeType(String mimeType) {
        int i = -1;
        switch (mimeType.hashCode()) {
            case -1095064472:
                if (mimeType.equals(MimeTypes.AUDIO_DTS)) {
                    i = START_NEED_SYNC;
                    break;
                }
                break;
            case 187078296:
                if (mimeType.equals(MimeTypes.AUDIO_AC3)) {
                    i = START_NOT_SET;
                    break;
                }
                break;
            case 1504578661:
                if (mimeType.equals(MimeTypes.AUDIO_E_AC3)) {
                    i = START_IN_SYNC;
                    break;
                }
                break;
            case 1505942594:
                if (mimeType.equals(MimeTypes.AUDIO_DTS_HD)) {
                    i = 3;
                    break;
                }
                break;
        }
        switch (i) {
            case START_NOT_SET /*0*/:
                return 5;
            case START_IN_SYNC /*1*/:
                return 6;
            case START_NEED_SYNC /*2*/:
                return 7;
            case C1628R.styleable.RecyclerView_reverseLayout /*3*/:
                return 8;
            default:
                return START_NOT_SET;
        }
    }

    private static int getFramesPerEncodedSample(int encoding, ByteBuffer buffer) {
        if (encoding == 7 || encoding == 8) {
            return DtsUtil.parseDtsAudioSampleCount(buffer);
        }
        if (encoding == 5) {
            return Ac3Util.getAc3SyncframeAudioSampleCount();
        }
        if (encoding == 6) {
            return Ac3Util.parseEAc3SyncframeAudioSampleCount(buffer);
        }
        throw new IllegalStateException("Unexpected audio encoding: " + encoding);
    }
}
