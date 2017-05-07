package com.google.android.exoplayer;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.MimeTypes;
import com.google.android.exoplayer.util.Util;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class MediaFormat {
    public static final int NO_VALUE = -1;
    public static final long OFFSET_SAMPLE_RELATIVE = Long.MAX_VALUE;
    public final boolean adaptive;
    public final int bitrate;
    public final int channelCount;
    public final long durationUs;
    public final int encoderDelay;
    public final int encoderPadding;
    private android.media.MediaFormat frameworkMediaFormat;
    private int hashCode;
    public final int height;
    public final List<byte[]> initializationData;
    public final String language;
    public final int maxHeight;
    public final int maxInputSize;
    public final int maxWidth;
    public final String mimeType;
    public final float pixelWidthHeightRatio;
    public final int rotationDegrees;
    public final int sampleRate;
    public final long subsampleOffsetUs;
    public final String trackId;
    public final int width;

    public static MediaFormat createVideoFormat(String trackId, String mimeType, int bitrate, int maxInputSize, long durationUs, int width, int height, List<byte[]> initializationData) {
        return createVideoFormat(trackId, mimeType, bitrate, maxInputSize, durationUs, width, height, initializationData, NO_VALUE, -1.0f);
    }

    public static MediaFormat createVideoFormat(String trackId, String mimeType, int bitrate, int maxInputSize, long durationUs, int width, int height, List<byte[]> initializationData, int rotationDegrees, float pixelWidthHeightRatio) {
        return new MediaFormat(trackId, mimeType, bitrate, maxInputSize, durationUs, width, height, rotationDegrees, pixelWidthHeightRatio, NO_VALUE, NO_VALUE, null, OFFSET_SAMPLE_RELATIVE, initializationData, false, NO_VALUE, NO_VALUE, NO_VALUE, NO_VALUE);
    }

    public static MediaFormat createAudioFormat(String trackId, String mimeType, int bitrate, int maxInputSize, long durationUs, int channelCount, int sampleRate, List<byte[]> initializationData, String language) {
        return new MediaFormat(trackId, mimeType, bitrate, maxInputSize, durationUs, NO_VALUE, NO_VALUE, NO_VALUE, -1.0f, channelCount, sampleRate, language, OFFSET_SAMPLE_RELATIVE, initializationData, false, NO_VALUE, NO_VALUE, NO_VALUE, NO_VALUE);
    }

    public static MediaFormat createTextFormat(String trackId, String mimeType, int bitrate, long durationUs, String language) {
        return createTextFormat(trackId, mimeType, bitrate, durationUs, language, OFFSET_SAMPLE_RELATIVE);
    }

    public static MediaFormat createTextFormat(String trackId, String mimeType, int bitrate, long durationUs, String language, long subsampleOffsetUs) {
        return new MediaFormat(trackId, mimeType, bitrate, NO_VALUE, durationUs, NO_VALUE, NO_VALUE, NO_VALUE, -1.0f, NO_VALUE, NO_VALUE, language, subsampleOffsetUs, null, false, NO_VALUE, NO_VALUE, NO_VALUE, NO_VALUE);
    }

    public static MediaFormat createFormatForMimeType(String trackId, String mimeType, int bitrate, long durationUs) {
        return new MediaFormat(trackId, mimeType, bitrate, NO_VALUE, durationUs, NO_VALUE, NO_VALUE, NO_VALUE, -1.0f, NO_VALUE, NO_VALUE, null, OFFSET_SAMPLE_RELATIVE, null, false, NO_VALUE, NO_VALUE, NO_VALUE, NO_VALUE);
    }

    public static MediaFormat createId3Format() {
        return createFormatForMimeType(null, MimeTypes.APPLICATION_ID3, NO_VALUE, -1);
    }

    MediaFormat(String trackId, String mimeType, int bitrate, int maxInputSize, long durationUs, int width, int height, int rotationDegrees, float pixelWidthHeightRatio, int channelCount, int sampleRate, String language, long subsampleOffsetUs, List<byte[]> initializationData, boolean adaptive, int maxWidth, int maxHeight, int encoderDelay, int encoderPadding) {
        this.trackId = trackId;
        this.mimeType = Assertions.checkNotEmpty(mimeType);
        this.bitrate = bitrate;
        this.maxInputSize = maxInputSize;
        this.durationUs = durationUs;
        this.width = width;
        this.height = height;
        this.rotationDegrees = rotationDegrees;
        this.pixelWidthHeightRatio = pixelWidthHeightRatio;
        this.channelCount = channelCount;
        this.sampleRate = sampleRate;
        this.language = language;
        this.subsampleOffsetUs = subsampleOffsetUs;
        if (initializationData == null) {
            initializationData = Collections.emptyList();
        }
        this.initializationData = initializationData;
        this.adaptive = adaptive;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.encoderDelay = encoderDelay;
        this.encoderPadding = encoderPadding;
    }

    public MediaFormat copyWithMaxInputSize(int maxInputSize) {
        return new MediaFormat(this.trackId, this.mimeType, this.bitrate, maxInputSize, this.durationUs, this.width, this.height, this.rotationDegrees, this.pixelWidthHeightRatio, this.channelCount, this.sampleRate, this.language, this.subsampleOffsetUs, this.initializationData, this.adaptive, this.maxWidth, this.maxHeight, this.encoderDelay, this.encoderPadding);
    }

    public MediaFormat copyWithMaxVideoDimensions(int maxWidth, int maxHeight) {
        return new MediaFormat(this.trackId, this.mimeType, this.bitrate, this.maxInputSize, this.durationUs, this.width, this.height, this.rotationDegrees, this.pixelWidthHeightRatio, this.channelCount, this.sampleRate, this.language, this.subsampleOffsetUs, this.initializationData, this.adaptive, maxWidth, maxHeight, this.encoderDelay, this.encoderPadding);
    }

    public MediaFormat copyWithSubsampleOffsetUs(long subsampleOffsetUs) {
        return new MediaFormat(this.trackId, this.mimeType, this.bitrate, this.maxInputSize, this.durationUs, this.width, this.height, this.rotationDegrees, this.pixelWidthHeightRatio, this.channelCount, this.sampleRate, this.language, subsampleOffsetUs, this.initializationData, this.adaptive, this.maxWidth, this.maxHeight, this.encoderDelay, this.encoderPadding);
    }

    public MediaFormat copyWithDurationUs(long durationUs) {
        return new MediaFormat(this.trackId, this.mimeType, this.bitrate, this.maxInputSize, durationUs, this.width, this.height, this.rotationDegrees, this.pixelWidthHeightRatio, this.channelCount, this.sampleRate, this.language, this.subsampleOffsetUs, this.initializationData, this.adaptive, this.maxWidth, this.maxHeight, this.encoderDelay, this.encoderPadding);
    }

    public MediaFormat copyWithFixedTrackInfo(String trackId, int bitrate, int width, int height, String language) {
        return new MediaFormat(trackId, this.mimeType, bitrate, this.maxInputSize, this.durationUs, width, height, this.rotationDegrees, this.pixelWidthHeightRatio, this.channelCount, this.sampleRate, language, this.subsampleOffsetUs, this.initializationData, this.adaptive, NO_VALUE, NO_VALUE, this.encoderDelay, this.encoderPadding);
    }

    public MediaFormat copyAsAdaptive(String trackId) {
        return new MediaFormat(trackId, this.mimeType, NO_VALUE, NO_VALUE, this.durationUs, NO_VALUE, NO_VALUE, NO_VALUE, -1.0f, NO_VALUE, NO_VALUE, null, OFFSET_SAMPLE_RELATIVE, null, true, this.maxWidth, this.maxHeight, NO_VALUE, NO_VALUE);
    }

    public MediaFormat copyWithGaplessInfo(int encoderDelay, int encoderPadding) {
        return new MediaFormat(this.trackId, this.mimeType, this.bitrate, this.maxInputSize, this.durationUs, this.width, this.height, this.rotationDegrees, this.pixelWidthHeightRatio, this.channelCount, this.sampleRate, this.language, this.subsampleOffsetUs, this.initializationData, this.adaptive, this.maxWidth, this.maxHeight, encoderDelay, encoderPadding);
    }

    @SuppressLint({"InlinedApi"})
    @TargetApi(16)
    public final android.media.MediaFormat getFrameworkMediaFormatV16() {
        if (this.frameworkMediaFormat == null) {
            android.media.MediaFormat format = new android.media.MediaFormat();
            format.setString("mime", this.mimeType);
            maybeSetStringV16(format, "language", this.language);
            maybeSetIntegerV16(format, "max-input-size", this.maxInputSize);
            maybeSetIntegerV16(format, "width", this.width);
            maybeSetIntegerV16(format, "height", this.height);
            maybeSetIntegerV16(format, "rotation-degrees", this.rotationDegrees);
            maybeSetIntegerV16(format, "max-width", this.maxWidth);
            maybeSetIntegerV16(format, "max-height", this.maxHeight);
            maybeSetIntegerV16(format, "channel-count", this.channelCount);
            maybeSetIntegerV16(format, "sample-rate", this.sampleRate);
            maybeSetIntegerV16(format, "encoder-delay", this.encoderDelay);
            maybeSetIntegerV16(format, "encoder-padding", this.encoderPadding);
            for (int i = 0; i < this.initializationData.size(); i++) {
                format.setByteBuffer("csd-" + i, ByteBuffer.wrap((byte[]) this.initializationData.get(i)));
            }
            if (this.durationUs != -1) {
                format.setLong("durationUs", this.durationUs);
            }
            this.frameworkMediaFormat = format;
        }
        return this.frameworkMediaFormat;
    }

    @TargetApi(16)
    @Deprecated
    final void setFrameworkFormatV16(android.media.MediaFormat format) {
        this.frameworkMediaFormat = format;
    }

    public String toString() {
        return "MediaFormat(" + this.trackId + ", " + this.mimeType + ", " + this.bitrate + ", " + this.maxInputSize + ", " + this.width + ", " + this.height + ", " + this.rotationDegrees + ", " + this.pixelWidthHeightRatio + ", " + this.channelCount + ", " + this.sampleRate + ", " + this.language + ", " + this.durationUs + ", " + this.adaptive + ", " + this.maxWidth + ", " + this.maxHeight + ", " + this.encoderDelay + ", " + this.encoderPadding + ")";
    }

    public int hashCode() {
        int i = 0;
        if (this.hashCode == 0) {
            int hashCode = ((((((((((((((((((((((((((((((((this.trackId == null ? 0 : this.trackId.hashCode()) + 527) * 31) + (this.mimeType == null ? 0 : this.mimeType.hashCode())) * 31) + this.bitrate) * 31) + this.maxInputSize) * 31) + this.width) * 31) + this.height) * 31) + this.rotationDegrees) * 31) + Float.floatToRawIntBits(this.pixelWidthHeightRatio)) * 31) + ((int) this.durationUs)) * 31) + (this.adaptive ? 1231 : 1237)) * 31) + this.maxWidth) * 31) + this.maxHeight) * 31) + this.encoderDelay) * 31) + this.encoderPadding) * 31) + this.channelCount) * 31) + this.sampleRate) * 31;
            if (this.language != null) {
                i = this.language.hashCode();
            }
            int result = hashCode + i;
            for (int i2 = 0; i2 < this.initializationData.size(); i2++) {
                result = (result * 31) + Arrays.hashCode((byte[]) this.initializationData.get(i2));
            }
            this.hashCode = result;
        }
        return this.hashCode;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        MediaFormat other = (MediaFormat) obj;
        if (this.adaptive != other.adaptive || this.bitrate != other.bitrate || this.maxInputSize != other.maxInputSize || this.width != other.width || this.height != other.height || this.rotationDegrees != other.rotationDegrees || this.pixelWidthHeightRatio != other.pixelWidthHeightRatio || this.maxWidth != other.maxWidth || this.maxHeight != other.maxHeight || this.encoderDelay != other.encoderDelay || this.encoderPadding != other.encoderPadding || this.channelCount != other.channelCount || this.sampleRate != other.sampleRate || !Util.areEqual(this.trackId, other.trackId) || !Util.areEqual(this.language, other.language) || !Util.areEqual(this.mimeType, other.mimeType) || this.initializationData.size() != other.initializationData.size()) {
            return false;
        }
        for (int i = 0; i < this.initializationData.size(); i++) {
            if (!Arrays.equals((byte[]) this.initializationData.get(i), (byte[]) other.initializationData.get(i))) {
                return false;
            }
        }
        return true;
    }

    @TargetApi(16)
    private static final void maybeSetStringV16(android.media.MediaFormat format, String key, String value) {
        if (value != null) {
            format.setString(key, value);
        }
    }

    @TargetApi(16)
    private static final void maybeSetIntegerV16(android.media.MediaFormat format, String key, int value) {
        if (value != NO_VALUE) {
            format.setInteger(key, value);
        }
    }
}
