package com.google.android.exoplayer;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.media.PlaybackParams;
import android.os.Handler;
import android.os.SystemClock;
import com.google.android.exoplayer.MediaCodecUtil.DecoderQueryException;
import com.google.android.exoplayer.audio.AudioCapabilities;
import com.google.android.exoplayer.audio.AudioTrack;
import com.google.android.exoplayer.audio.AudioTrack.InitializationException;
import com.google.android.exoplayer.audio.AudioTrack.WriteException;
import com.google.android.exoplayer.drm.DrmSessionManager;
import com.google.android.exoplayer.util.MimeTypes;
import java.nio.ByteBuffer;

@TargetApi(16)
public class MediaCodecAudioTrackRenderer extends MediaCodecTrackRenderer implements MediaClock {
    public static final int MSG_SET_PLAYBACK_PARAMS = 2;
    public static final int MSG_SET_VOLUME = 1;
    private boolean allowPositionDiscontinuity;
    private int audioSessionId;
    private final AudioTrack audioTrack;
    private boolean audioTrackHasData;
    private long currentPositionUs;
    private final EventListener eventListener;
    private long lastFeedElapsedRealtimeMs;
    private boolean passthroughEnabled;
    private MediaFormat passthroughMediaFormat;

    /* renamed from: com.google.android.exoplayer.MediaCodecAudioTrackRenderer.1 */
    class C02801 implements Runnable {
        final /* synthetic */ InitializationException val$e;

        C02801(InitializationException initializationException) {
            this.val$e = initializationException;
        }

        public void run() {
            MediaCodecAudioTrackRenderer.this.eventListener.onAudioTrackInitializationError(this.val$e);
        }
    }

    /* renamed from: com.google.android.exoplayer.MediaCodecAudioTrackRenderer.2 */
    class C02812 implements Runnable {
        final /* synthetic */ WriteException val$e;

        C02812(WriteException writeException) {
            this.val$e = writeException;
        }

        public void run() {
            MediaCodecAudioTrackRenderer.this.eventListener.onAudioTrackWriteError(this.val$e);
        }
    }

    /* renamed from: com.google.android.exoplayer.MediaCodecAudioTrackRenderer.3 */
    class C02823 implements Runnable {
        final /* synthetic */ int val$bufferSize;
        final /* synthetic */ long val$bufferSizeMs;
        final /* synthetic */ long val$elapsedSinceLastFeedMs;

        C02823(int i, long j, long j2) {
            this.val$bufferSize = i;
            this.val$bufferSizeMs = j;
            this.val$elapsedSinceLastFeedMs = j2;
        }

        public void run() {
            MediaCodecAudioTrackRenderer.this.eventListener.onAudioTrackUnderrun(this.val$bufferSize, this.val$bufferSizeMs, this.val$elapsedSinceLastFeedMs);
        }
    }

    public interface EventListener extends com.google.android.exoplayer.MediaCodecTrackRenderer.EventListener {
        void onAudioTrackInitializationError(InitializationException initializationException);

        void onAudioTrackUnderrun(int i, long j, long j2);

        void onAudioTrackWriteError(WriteException writeException);
    }

    public MediaCodecAudioTrackRenderer(SampleSource source, MediaCodecSelector mediaCodecSelector) {
        this(source, mediaCodecSelector, null, true);
    }

    public MediaCodecAudioTrackRenderer(SampleSource source, MediaCodecSelector mediaCodecSelector, DrmSessionManager drmSessionManager, boolean playClearSamplesWithoutKeys) {
        this(source, mediaCodecSelector, drmSessionManager, playClearSamplesWithoutKeys, null, null);
    }

    public MediaCodecAudioTrackRenderer(SampleSource source, MediaCodecSelector mediaCodecSelector, Handler eventHandler, EventListener eventListener) {
        this(source, mediaCodecSelector, null, true, eventHandler, eventListener);
    }

    public MediaCodecAudioTrackRenderer(SampleSource source, MediaCodecSelector mediaCodecSelector, DrmSessionManager drmSessionManager, boolean playClearSamplesWithoutKeys, Handler eventHandler, EventListener eventListener) {
        this(source, mediaCodecSelector, drmSessionManager, playClearSamplesWithoutKeys, eventHandler, eventListener, null, 3);
    }

    public MediaCodecAudioTrackRenderer(SampleSource source, MediaCodecSelector mediaCodecSelector, DrmSessionManager drmSessionManager, boolean playClearSamplesWithoutKeys, Handler eventHandler, EventListener eventListener, AudioCapabilities audioCapabilities, int streamType) {
        super(source, mediaCodecSelector, drmSessionManager, playClearSamplesWithoutKeys, eventHandler, eventListener);
        this.eventListener = eventListener;
        this.audioSessionId = 0;
        this.audioTrack = new AudioTrack(audioCapabilities, streamType);
    }

    protected boolean handlesTrack(MediaCodecSelector mediaCodecSelector, MediaFormat mediaFormat) throws DecoderQueryException {
        String mimeType = mediaFormat.mimeType;
        if (!MimeTypes.isAudio(mimeType)) {
            return false;
        }
        if (MimeTypes.AUDIO_UNKNOWN.equals(mimeType) || ((allowPassthrough(mimeType) && mediaCodecSelector.getPassthroughDecoderName() != null) || mediaCodecSelector.getDecoderInfo(mimeType, false) != null)) {
            return true;
        }
        return false;
    }

    protected DecoderInfo getDecoderInfo(MediaCodecSelector mediaCodecSelector, String mimeType, boolean requiresSecureDecoder) throws DecoderQueryException {
        if (allowPassthrough(mimeType)) {
            String passthroughDecoderName = mediaCodecSelector.getPassthroughDecoderName();
            if (passthroughDecoderName != null) {
                this.passthroughEnabled = true;
                return new DecoderInfo(passthroughDecoderName, false);
            }
        }
        this.passthroughEnabled = false;
        return super.getDecoderInfo(mediaCodecSelector, mimeType, requiresSecureDecoder);
    }

    protected boolean allowPassthrough(String mimeType) {
        return this.audioTrack.isPassthroughSupported(mimeType);
    }

    protected void configureCodec(MediaCodec codec, boolean codecIsAdaptive, MediaFormat format, MediaCrypto crypto) {
        String mimeType = format.getString("mime");
        if (this.passthroughEnabled) {
            format.setString("mime", MimeTypes.AUDIO_RAW);
            codec.configure(format, null, crypto, 0);
            format.setString("mime", mimeType);
            this.passthroughMediaFormat = format;
            return;
        }
        codec.configure(format, null, crypto, 0);
        this.passthroughMediaFormat = null;
    }

    protected MediaClock getMediaClock() {
        return this;
    }

    protected void onOutputFormatChanged(MediaFormat outputFormat) {
        boolean passthrough = this.passthroughMediaFormat != null;
        AudioTrack audioTrack = this.audioTrack;
        if (passthrough) {
            outputFormat = this.passthroughMediaFormat;
        }
        audioTrack.configure(outputFormat, passthrough);
    }

    protected void onAudioSessionId(int audioSessionId) {
    }

    protected void onStarted() {
        super.onStarted();
        this.audioTrack.play();
    }

    protected void onStopped() {
        this.audioTrack.pause();
        super.onStopped();
    }

    protected boolean isEnded() {
        return super.isEnded() && !this.audioTrack.hasPendingData();
    }

    protected boolean isReady() {
        return this.audioTrack.hasPendingData() || super.isReady();
    }

    public long getPositionUs() {
        long newCurrentPositionUs = this.audioTrack.getCurrentPositionUs(isEnded());
        if (newCurrentPositionUs != Long.MIN_VALUE) {
            if (!this.allowPositionDiscontinuity) {
                newCurrentPositionUs = Math.max(this.currentPositionUs, newCurrentPositionUs);
            }
            this.currentPositionUs = newCurrentPositionUs;
            this.allowPositionDiscontinuity = false;
        }
        return this.currentPositionUs;
    }

    protected void onDisabled() throws ExoPlaybackException {
        this.audioSessionId = 0;
        try {
            this.audioTrack.release();
        } finally {
            super.onDisabled();
        }
    }

    protected void onDiscontinuity(long positionUs) throws ExoPlaybackException {
        super.onDiscontinuity(positionUs);
        this.audioTrack.reset();
        this.currentPositionUs = positionUs;
        this.allowPositionDiscontinuity = true;
    }

    protected boolean processOutputBuffer(long positionUs, long elapsedRealtimeUs, MediaCodec codec, ByteBuffer buffer, BufferInfo bufferInfo, int bufferIndex, boolean shouldSkip) throws ExoPlaybackException {
        if (this.passthroughEnabled && (bufferInfo.flags & MSG_SET_PLAYBACK_PARAMS) != 0) {
            codec.releaseOutputBuffer(bufferIndex, false);
            return true;
        } else if (shouldSkip) {
            codec.releaseOutputBuffer(bufferIndex, false);
            r2 = this.codecCounters;
            r2.skippedOutputBufferCount += MSG_SET_VOLUME;
            this.audioTrack.handleDiscontinuity();
            return true;
        } else {
            if (this.audioTrack.isInitialized()) {
                boolean audioTrackHadData = this.audioTrackHasData;
                this.audioTrackHasData = this.audioTrack.hasPendingData();
                if (audioTrackHadData && !this.audioTrackHasData && getState() == 3) {
                    long elapsedSinceLastFeedMs = SystemClock.elapsedRealtime() - this.lastFeedElapsedRealtimeMs;
                    long bufferSizeUs = this.audioTrack.getBufferSizeUs();
                    notifyAudioTrackUnderrun(this.audioTrack.getBufferSize(), bufferSizeUs == -1 ? -1 : bufferSizeUs / 1000, elapsedSinceLastFeedMs);
                }
            } else {
                try {
                    if (this.audioSessionId != 0) {
                        this.audioTrack.initialize(this.audioSessionId);
                    } else {
                        this.audioSessionId = this.audioTrack.initialize();
                        onAudioSessionId(this.audioSessionId);
                    }
                    this.audioTrackHasData = false;
                    if (getState() == 3) {
                        this.audioTrack.play();
                    }
                } catch (Throwable e) {
                    notifyAudioTrackInitializationError(e);
                    throw new ExoPlaybackException(e);
                }
            }
            try {
                int handleBufferResult = this.audioTrack.handleBuffer(buffer, bufferInfo.offset, bufferInfo.size, bufferInfo.presentationTimeUs);
                this.lastFeedElapsedRealtimeMs = SystemClock.elapsedRealtime();
                if ((handleBufferResult & MSG_SET_VOLUME) != 0) {
                    handleAudioTrackDiscontinuity();
                    this.allowPositionDiscontinuity = true;
                }
                if ((handleBufferResult & MSG_SET_PLAYBACK_PARAMS) == 0) {
                    return false;
                }
                codec.releaseOutputBuffer(bufferIndex, false);
                r2 = this.codecCounters;
                r2.renderedOutputBufferCount += MSG_SET_VOLUME;
                return true;
            } catch (Throwable e2) {
                notifyAudioTrackWriteError(e2);
                throw new ExoPlaybackException(e2);
            }
        }
    }

    protected void onOutputStreamEnded() {
        this.audioTrack.handleEndOfStream();
    }

    protected void handleAudioTrackDiscontinuity() {
    }

    public void handleMessage(int messageType, Object message) throws ExoPlaybackException {
        switch (messageType) {
            case MSG_SET_VOLUME /*1*/:
                this.audioTrack.setVolume(((Float) message).floatValue());
            case MSG_SET_PLAYBACK_PARAMS /*2*/:
                this.audioTrack.setPlaybackParams((PlaybackParams) message);
            default:
                super.handleMessage(messageType, message);
        }
    }

    private void notifyAudioTrackInitializationError(InitializationException e) {
        if (this.eventHandler != null && this.eventListener != null) {
            this.eventHandler.post(new C02801(e));
        }
    }

    private void notifyAudioTrackWriteError(WriteException e) {
        if (this.eventHandler != null && this.eventListener != null) {
            this.eventHandler.post(new C02812(e));
        }
    }

    private void notifyAudioTrackUnderrun(int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {
        if (this.eventHandler != null && this.eventListener != null) {
            this.eventHandler.post(new C02823(bufferSize, bufferSizeMs, elapsedSinceLastFeedMs));
        }
    }
}
