package com.google.android.exoplayer.extractor.ts;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.extractor.TrackOutput;
import com.google.android.exoplayer.util.CodecSpecificDataUtil;
import com.google.android.exoplayer.util.CodecSpecificDataUtil.SpsData;
import com.google.android.exoplayer.util.MimeTypes;
import com.google.android.exoplayer.util.NalUnitUtil;
import com.google.android.exoplayer.util.ParsableBitArray;
import com.google.android.exoplayer.util.ParsableByteArray;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class H264Reader extends ElementaryStreamReader {
    private static final int FRAME_TYPE_ALL_I = 7;
    private static final int FRAME_TYPE_I = 2;
    private static final int NAL_UNIT_TYPE_AUD = 9;
    private static final int NAL_UNIT_TYPE_IDR = 5;
    private static final int NAL_UNIT_TYPE_IFR = 1;
    private static final int NAL_UNIT_TYPE_PPS = 8;
    private static final int NAL_UNIT_TYPE_SEI = 6;
    private static final int NAL_UNIT_TYPE_SPS = 7;
    private boolean foundFirstSample;
    private boolean hasOutputFormat;
    private final IfrParserBuffer ifrParserBuffer;
    private boolean isKeyframe;
    private long pesTimeUs;
    private final NalUnitTargetBuffer pps;
    private final boolean[] prefixFlags;
    private long samplePosition;
    private long sampleTimeUs;
    private final NalUnitTargetBuffer sei;
    private final SeiReader seiReader;
    private final ParsableByteArray seiWrapper;
    private final NalUnitTargetBuffer sps;
    private long totalBytesWritten;

    private static final class IfrParserBuffer {
        private static final int DEFAULT_BUFFER_SIZE = 128;
        private static final int NOT_SET = -1;
        private byte[] ifrData;
        private int ifrLength;
        private boolean isFilling;
        private final ParsableBitArray scratchSliceType;
        private int sliceType;

        public IfrParserBuffer() {
            this.ifrData = new byte[DEFAULT_BUFFER_SIZE];
            this.scratchSliceType = new ParsableBitArray(this.ifrData);
            reset();
        }

        public void reset() {
            this.isFilling = false;
            this.ifrLength = 0;
            this.sliceType = NOT_SET;
        }

        public boolean isCompleted() {
            return this.sliceType != NOT_SET;
        }

        public void startNalUnit(int nalUnitType) {
            if (nalUnitType == H264Reader.NAL_UNIT_TYPE_IFR) {
                reset();
                this.isFilling = true;
            }
        }

        public void appendToNalUnit(byte[] data, int offset, int limit) {
            if (this.isFilling) {
                int readLength = limit - offset;
                if (this.ifrData.length < this.ifrLength + readLength) {
                    this.ifrData = Arrays.copyOf(this.ifrData, (this.ifrLength + readLength) * H264Reader.FRAME_TYPE_I);
                }
                System.arraycopy(data, offset, this.ifrData, this.ifrLength, readLength);
                this.ifrLength += readLength;
                this.scratchSliceType.reset(this.ifrData, this.ifrLength);
                this.scratchSliceType.skipBits(H264Reader.NAL_UNIT_TYPE_PPS);
                int len = this.scratchSliceType.peekExpGolombCodedNumLength();
                if (len != NOT_SET && len <= this.scratchSliceType.bitsLeft()) {
                    this.scratchSliceType.skipBits(len);
                    len = this.scratchSliceType.peekExpGolombCodedNumLength();
                    if (len != NOT_SET && len <= this.scratchSliceType.bitsLeft()) {
                        this.sliceType = this.scratchSliceType.readUnsignedExpGolombCodedInt();
                        this.isFilling = false;
                    }
                }
            }
        }

        public int getSliceType() {
            return this.sliceType;
        }
    }

    public H264Reader(TrackOutput output, SeiReader seiReader, boolean allowNonIdrKeyframes) {
        super(output);
        this.seiReader = seiReader;
        this.prefixFlags = new boolean[3];
        this.ifrParserBuffer = allowNonIdrKeyframes ? new IfrParserBuffer() : null;
        this.sps = new NalUnitTargetBuffer(NAL_UNIT_TYPE_SPS, AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
        this.pps = new NalUnitTargetBuffer(NAL_UNIT_TYPE_PPS, AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
        this.sei = new NalUnitTargetBuffer(NAL_UNIT_TYPE_SEI, AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
        this.seiWrapper = new ParsableByteArray();
    }

    public void seek() {
        NalUnitUtil.clearPrefixFlags(this.prefixFlags);
        this.sps.reset();
        this.pps.reset();
        this.sei.reset();
        if (this.ifrParserBuffer != null) {
            this.ifrParserBuffer.reset();
        }
        this.foundFirstSample = false;
        this.totalBytesWritten = 0;
    }

    public void packetStarted(long pesTimeUs, boolean dataAlignmentIndicator) {
        this.pesTimeUs = pesTimeUs;
    }

    public void consume(ParsableByteArray data) {
        if (data.bytesLeft() > 0) {
            int offset = data.getPosition();
            int limit = data.limit();
            byte[] dataArray = data.data;
            this.totalBytesWritten += (long) data.bytesLeft();
            this.output.sampleData(data, data.bytesLeft());
            while (true) {
                int nalUnitOffset = NalUnitUtil.findNalUnit(dataArray, offset, limit, this.prefixFlags);
                if (nalUnitOffset == limit) {
                    feedNalUnitTargetBuffersData(dataArray, offset, limit);
                    return;
                }
                int nalUnitType = NalUnitUtil.getNalUnitType(dataArray, nalUnitOffset);
                int lengthToNalUnit = nalUnitOffset - offset;
                if (lengthToNalUnit > 0) {
                    feedNalUnitTargetBuffersData(dataArray, offset, nalUnitOffset);
                }
                switch (nalUnitType) {
                    case NAL_UNIT_TYPE_IDR /*5*/:
                        this.isKeyframe = true;
                        break;
                    case NAL_UNIT_TYPE_AUD /*9*/:
                        int bytesWrittenPastNalUnit = limit - nalUnitOffset;
                        if (this.foundFirstSample) {
                            if (this.ifrParserBuffer != null && this.ifrParserBuffer.isCompleted()) {
                                int sliceType = this.ifrParserBuffer.getSliceType();
                                boolean z = this.isKeyframe;
                                int i = (sliceType == FRAME_TYPE_I || sliceType == NAL_UNIT_TYPE_SPS) ? NAL_UNIT_TYPE_IFR : 0;
                                this.isKeyframe = i | z;
                                this.ifrParserBuffer.reset();
                            }
                            if (this.isKeyframe && !this.hasOutputFormat && this.sps.isCompleted() && this.pps.isCompleted()) {
                                this.output.format(parseMediaFormat(this.sps, this.pps));
                                this.hasOutputFormat = true;
                            }
                            this.output.sampleMetadata(this.sampleTimeUs, this.isKeyframe ? NAL_UNIT_TYPE_IFR : 0, ((int) (this.totalBytesWritten - this.samplePosition)) - bytesWrittenPastNalUnit, bytesWrittenPastNalUnit, null);
                        }
                        this.foundFirstSample = true;
                        this.samplePosition = this.totalBytesWritten - ((long) bytesWrittenPastNalUnit);
                        this.sampleTimeUs = this.pesTimeUs;
                        this.isKeyframe = false;
                        break;
                }
                feedNalUnitTargetEnd(this.pesTimeUs, lengthToNalUnit < 0 ? -lengthToNalUnit : 0);
                feedNalUnitTargetBuffersStart(nalUnitType);
                offset = nalUnitOffset + 3;
            }
        }
    }

    public void packetFinished() {
    }

    private void feedNalUnitTargetBuffersStart(int nalUnitType) {
        if (this.ifrParserBuffer != null) {
            this.ifrParserBuffer.startNalUnit(nalUnitType);
        }
        if (!this.hasOutputFormat) {
            this.sps.startNalUnit(nalUnitType);
            this.pps.startNalUnit(nalUnitType);
        }
        this.sei.startNalUnit(nalUnitType);
    }

    private void feedNalUnitTargetBuffersData(byte[] dataArray, int offset, int limit) {
        if (this.ifrParserBuffer != null) {
            this.ifrParserBuffer.appendToNalUnit(dataArray, offset, limit);
        }
        if (!this.hasOutputFormat) {
            this.sps.appendToNalUnit(dataArray, offset, limit);
            this.pps.appendToNalUnit(dataArray, offset, limit);
        }
        this.sei.appendToNalUnit(dataArray, offset, limit);
    }

    private void feedNalUnitTargetEnd(long pesTimeUs, int discardPadding) {
        this.sps.endNalUnit(discardPadding);
        this.pps.endNalUnit(discardPadding);
        if (this.sei.endNalUnit(discardPadding)) {
            this.seiWrapper.reset(this.sei.nalData, NalUnitUtil.unescapeStream(this.sei.nalData, this.sei.nalLength));
            this.seiWrapper.setPosition(4);
            this.seiReader.consume(pesTimeUs, this.seiWrapper);
        }
    }

    private static MediaFormat parseMediaFormat(NalUnitTargetBuffer sps, NalUnitTargetBuffer pps) {
        List<byte[]> initializationData = new ArrayList();
        initializationData.add(Arrays.copyOf(sps.nalData, sps.nalLength));
        initializationData.add(Arrays.copyOf(pps.nalData, pps.nalLength));
        NalUnitUtil.unescapeStream(sps.nalData, sps.nalLength);
        ParsableBitArray bitArray = new ParsableBitArray(sps.nalData);
        bitArray.skipBits(32);
        SpsData parsedSpsData = CodecSpecificDataUtil.parseSpsNalUnit(bitArray);
        return MediaFormat.createVideoFormat(null, MimeTypes.VIDEO_H264, -1, -1, -1, parsedSpsData.width, parsedSpsData.height, initializationData, -1, parsedSpsData.pixelWidthAspectRatio);
    }
}
