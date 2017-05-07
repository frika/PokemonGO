package com.google.android.exoplayer.util;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.Log;
import android.util.Pair;
import com.google.android.exoplayer.upstream.DefaultHttpDataSource;
import com.google.android.gms.safetynet.SafetyNetStatusCodes;
import com.mopub.volley.DefaultRetryPolicy;
import java.util.ArrayList;
import java.util.List;

public final class CodecSpecificDataUtil {
    private static final int AUDIO_OBJECT_TYPE_AAC_LC = 2;
    private static final int AUDIO_OBJECT_TYPE_ER_BSAC = 22;
    private static final int AUDIO_OBJECT_TYPE_PS = 29;
    private static final int AUDIO_OBJECT_TYPE_SBR = 5;
    private static final int AUDIO_SPECIFIC_CONFIG_CHANNEL_CONFIGURATION_INVALID = -1;
    private static final int[] AUDIO_SPECIFIC_CONFIG_CHANNEL_COUNT_TABLE;
    private static final int AUDIO_SPECIFIC_CONFIG_FREQUENCY_INDEX_ARBITRARY = 15;
    private static final int[] AUDIO_SPECIFIC_CONFIG_SAMPLING_RATE_TABLE;
    private static final byte[] NAL_START_CODE;
    private static final String TAG = "CodecSpecificDataUtil";

    public static final class SpsData {
        public final int height;
        public final float pixelWidthAspectRatio;
        public final int width;

        public SpsData(int width, int height, float pixelWidthAspectRatio) {
            this.width = width;
            this.height = height;
            this.pixelWidthAspectRatio = pixelWidthAspectRatio;
        }
    }

    static {
        NAL_START_CODE = new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 1};
        AUDIO_SPECIFIC_CONFIG_SAMPLING_RATE_TABLE = new int[]{96000, 88200, 64000, 48000, 44100, 32000, 24000, 22050, 16000, SafetyNetStatusCodes.SAFE_BROWSING_UNSUPPORTED_THREAT_TYPES, 11025, DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS, 7350};
        AUDIO_SPECIFIC_CONFIG_CHANNEL_COUNT_TABLE = new int[]{0, 1, AUDIO_OBJECT_TYPE_AAC_LC, 3, 4, AUDIO_OBJECT_TYPE_SBR, 6, 8, AUDIO_SPECIFIC_CONFIG_CHANNEL_CONFIGURATION_INVALID, AUDIO_SPECIFIC_CONFIG_CHANNEL_CONFIGURATION_INVALID, AUDIO_SPECIFIC_CONFIG_CHANNEL_CONFIGURATION_INVALID, 7, 8, AUDIO_SPECIFIC_CONFIG_CHANNEL_CONFIGURATION_INVALID, 8, AUDIO_SPECIFIC_CONFIG_CHANNEL_CONFIGURATION_INVALID};
    }

    private CodecSpecificDataUtil() {
    }

    public static Pair<Integer, Integer> parseAacAudioSpecificConfig(byte[] audioSpecificConfig) {
        int sampleRate;
        boolean z;
        boolean z2 = true;
        ParsableBitArray bitArray = new ParsableBitArray(audioSpecificConfig);
        int audioObjectType = bitArray.readBits(AUDIO_OBJECT_TYPE_SBR);
        int frequencyIndex = bitArray.readBits(4);
        if (frequencyIndex == AUDIO_SPECIFIC_CONFIG_FREQUENCY_INDEX_ARBITRARY) {
            sampleRate = bitArray.readBits(24);
        } else {
            if (frequencyIndex < 13) {
                z = true;
            } else {
                z = false;
            }
            Assertions.checkArgument(z);
            sampleRate = AUDIO_SPECIFIC_CONFIG_SAMPLING_RATE_TABLE[frequencyIndex];
        }
        int channelConfiguration = bitArray.readBits(4);
        if (audioObjectType == AUDIO_OBJECT_TYPE_SBR || audioObjectType == AUDIO_OBJECT_TYPE_PS) {
            frequencyIndex = bitArray.readBits(4);
            if (frequencyIndex == AUDIO_SPECIFIC_CONFIG_FREQUENCY_INDEX_ARBITRARY) {
                sampleRate = bitArray.readBits(24);
            } else {
                if (frequencyIndex < 13) {
                    z = true;
                } else {
                    z = false;
                }
                Assertions.checkArgument(z);
                sampleRate = AUDIO_SPECIFIC_CONFIG_SAMPLING_RATE_TABLE[frequencyIndex];
            }
            if (bitArray.readBits(AUDIO_OBJECT_TYPE_SBR) == AUDIO_OBJECT_TYPE_ER_BSAC) {
                channelConfiguration = bitArray.readBits(4);
            }
        }
        int channelCount = AUDIO_SPECIFIC_CONFIG_CHANNEL_COUNT_TABLE[channelConfiguration];
        if (channelCount == AUDIO_SPECIFIC_CONFIG_CHANNEL_CONFIGURATION_INVALID) {
            z2 = false;
        }
        Assertions.checkArgument(z2);
        return Pair.create(Integer.valueOf(sampleRate), Integer.valueOf(channelCount));
    }

    public static byte[] buildAacAudioSpecificConfig(int audioObjectType, int sampleRateIndex, int channelConfig) {
        byte[] audioSpecificConfig = new byte[AUDIO_OBJECT_TYPE_AAC_LC];
        audioSpecificConfig[0] = (byte) (((audioObjectType << 3) & 248) | ((sampleRateIndex >> 1) & 7));
        audioSpecificConfig[1] = (byte) (((sampleRateIndex << 7) & AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS) | ((channelConfig << 3) & 120));
        return audioSpecificConfig;
    }

    public static byte[] buildAacAudioSpecificConfig(int sampleRate, int numChannels) {
        int i;
        int sampleRateIndex = AUDIO_SPECIFIC_CONFIG_CHANNEL_CONFIGURATION_INVALID;
        for (i = 0; i < AUDIO_SPECIFIC_CONFIG_SAMPLING_RATE_TABLE.length; i++) {
            if (sampleRate == AUDIO_SPECIFIC_CONFIG_SAMPLING_RATE_TABLE[i]) {
                sampleRateIndex = i;
            }
        }
        int channelConfig = AUDIO_SPECIFIC_CONFIG_CHANNEL_CONFIGURATION_INVALID;
        for (i = 0; i < AUDIO_SPECIFIC_CONFIG_CHANNEL_COUNT_TABLE.length; i++) {
            if (numChannels == AUDIO_SPECIFIC_CONFIG_CHANNEL_COUNT_TABLE[i]) {
                channelConfig = i;
            }
        }
        byte[] csd = new byte[AUDIO_OBJECT_TYPE_AAC_LC];
        csd[0] = (byte) ((sampleRateIndex >> 1) | 16);
        csd[1] = (byte) (((sampleRateIndex & 1) << 7) | (channelConfig << 3));
        return csd;
    }

    public static byte[] buildNalUnit(byte[] data, int offset, int length) {
        byte[] nalUnit = new byte[(NAL_START_CODE.length + length)];
        System.arraycopy(NAL_START_CODE, 0, nalUnit, 0, NAL_START_CODE.length);
        System.arraycopy(data, offset, nalUnit, NAL_START_CODE.length, length);
        return nalUnit;
    }

    public static byte[][] splitNalUnits(byte[] data) {
        if (!isNalStartCode(data, 0)) {
            return (byte[][]) null;
        }
        List<Integer> starts = new ArrayList();
        int nalUnitIndex = 0;
        do {
            starts.add(Integer.valueOf(nalUnitIndex));
            nalUnitIndex = findNalStartCode(data, NAL_START_CODE.length + nalUnitIndex);
        } while (nalUnitIndex != AUDIO_SPECIFIC_CONFIG_CHANNEL_CONFIGURATION_INVALID);
        byte[][] split = new byte[starts.size()][];
        int i = 0;
        while (i < starts.size()) {
            int startIndex = ((Integer) starts.get(i)).intValue();
            byte[] nal = new byte[((i < starts.size() + AUDIO_SPECIFIC_CONFIG_CHANNEL_CONFIGURATION_INVALID ? ((Integer) starts.get(i + 1)).intValue() : data.length) - startIndex)];
            System.arraycopy(data, startIndex, nal, 0, nal.length);
            split[i] = nal;
            i++;
        }
        return split;
    }

    private static int findNalStartCode(byte[] data, int index) {
        int endIndex = data.length - NAL_START_CODE.length;
        for (int i = index; i <= endIndex; i++) {
            if (isNalStartCode(data, i)) {
                return i;
            }
        }
        return AUDIO_SPECIFIC_CONFIG_CHANNEL_CONFIGURATION_INVALID;
    }

    private static boolean isNalStartCode(byte[] data, int index) {
        if (data.length - index <= NAL_START_CODE.length) {
            return false;
        }
        for (int j = 0; j < NAL_START_CODE.length; j++) {
            if (data[index + j] != NAL_START_CODE[j]) {
                return false;
            }
        }
        return true;
    }

    public static SpsData parseSpsNalUnit(ParsableBitArray bitArray) {
        int i;
        int profileIdc = bitArray.readBits(8);
        bitArray.skipBits(16);
        bitArray.readUnsignedExpGolombCodedInt();
        int chromaFormatIdc = 1;
        if (profileIdc == 100 || profileIdc == 110 || profileIdc == 122 || profileIdc == 244 || profileIdc == 44 || profileIdc == 83 || profileIdc == 86 || profileIdc == 118 || profileIdc == 128 || profileIdc == 138) {
            chromaFormatIdc = bitArray.readUnsignedExpGolombCodedInt();
            if (chromaFormatIdc == 3) {
                bitArray.skipBits(1);
            }
            bitArray.readUnsignedExpGolombCodedInt();
            bitArray.readUnsignedExpGolombCodedInt();
            bitArray.skipBits(1);
            if (bitArray.readBit()) {
                int limit = chromaFormatIdc != 3 ? 8 : 12;
                i = 0;
                while (i < limit) {
                    if (bitArray.readBit()) {
                        skipScalingList(bitArray, i < 6 ? 16 : 64);
                    }
                    i++;
                }
            }
        }
        bitArray.readUnsignedExpGolombCodedInt();
        long picOrderCntType = (long) bitArray.readUnsignedExpGolombCodedInt();
        if (picOrderCntType != 0) {
            if (picOrderCntType == 1) {
                bitArray.skipBits(1);
                bitArray.readSignedExpGolombCodedInt();
                bitArray.readSignedExpGolombCodedInt();
                long numRefFramesInPicOrderCntCycle = (long) bitArray.readUnsignedExpGolombCodedInt();
                i = 0;
                while (true) {
                    if (((long) i) >= numRefFramesInPicOrderCntCycle) {
                        break;
                    }
                    bitArray.readUnsignedExpGolombCodedInt();
                    i++;
                }
            }
        } else {
            bitArray.readUnsignedExpGolombCodedInt();
        }
        bitArray.readUnsignedExpGolombCodedInt();
        bitArray.skipBits(1);
        int picWidthInMbs = bitArray.readUnsignedExpGolombCodedInt() + 1;
        int picHeightInMapUnits = bitArray.readUnsignedExpGolombCodedInt() + 1;
        boolean frameMbsOnlyFlag = bitArray.readBit();
        int frameHeightInMbs = (2 - (frameMbsOnlyFlag ? 1 : 0)) * picHeightInMapUnits;
        if (!frameMbsOnlyFlag) {
            bitArray.skipBits(1);
        }
        bitArray.skipBits(1);
        int frameWidth = picWidthInMbs * 16;
        int frameHeight = frameHeightInMbs * 16;
        if (bitArray.readBit()) {
            int cropUnitX;
            int cropUnitY;
            int frameCropLeftOffset = bitArray.readUnsignedExpGolombCodedInt();
            int frameCropRightOffset = bitArray.readUnsignedExpGolombCodedInt();
            int frameCropTopOffset = bitArray.readUnsignedExpGolombCodedInt();
            int frameCropBottomOffset = bitArray.readUnsignedExpGolombCodedInt();
            if (chromaFormatIdc == 0) {
                cropUnitX = 1;
                cropUnitY = 2 - (frameMbsOnlyFlag ? 1 : 0);
            } else {
                cropUnitX = chromaFormatIdc == 3 ? 1 : AUDIO_OBJECT_TYPE_AAC_LC;
                cropUnitY = (chromaFormatIdc == 1 ? AUDIO_OBJECT_TYPE_AAC_LC : 1) * (2 - (frameMbsOnlyFlag ? 1 : 0));
            }
            frameWidth -= (frameCropLeftOffset + frameCropRightOffset) * cropUnitX;
            frameHeight -= (frameCropTopOffset + frameCropBottomOffset) * cropUnitY;
        }
        float pixelWidthHeightRatio = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
        if (bitArray.readBit() && bitArray.readBit()) {
            int aspectRatioIdc = bitArray.readBits(8);
            if (aspectRatioIdc == 255) {
                int sarWidth = bitArray.readBits(16);
                int sarHeight = bitArray.readBits(16);
                if (!(sarWidth == 0 || sarHeight == 0)) {
                    pixelWidthHeightRatio = ((float) sarWidth) / ((float) sarHeight);
                }
            } else {
                int length = NalUnitUtil.ASPECT_RATIO_IDC_VALUES.length;
                if (aspectRatioIdc < r0) {
                    pixelWidthHeightRatio = NalUnitUtil.ASPECT_RATIO_IDC_VALUES[aspectRatioIdc];
                } else {
                    Log.w(TAG, "Unexpected aspect_ratio_idc value: " + aspectRatioIdc);
                }
            }
        }
        return new SpsData(frameWidth, frameHeight, pixelWidthHeightRatio);
    }

    private static void skipScalingList(ParsableBitArray bitArray, int size) {
        int lastScale = 8;
        int nextScale = 8;
        for (int i = 0; i < size; i++) {
            if (nextScale != 0) {
                nextScale = ((lastScale + bitArray.readSignedExpGolombCodedInt()) + AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY) % AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY;
            }
            if (nextScale != 0) {
                lastScale = nextScale;
            }
        }
    }
}
