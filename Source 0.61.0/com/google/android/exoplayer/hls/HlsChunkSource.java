package com.google.android.exoplayer.hls;

import android.net.Uri;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.exoplayer.BehindLiveWindowException;
import com.google.android.exoplayer.chunk.BaseChunkSampleSourceEventListener;
import com.google.android.exoplayer.chunk.Chunk;
import com.google.android.exoplayer.chunk.ChunkOperationHolder;
import com.google.android.exoplayer.chunk.DataChunk;
import com.google.android.exoplayer.chunk.Format;
import com.google.android.exoplayer.chunk.Format.DecreasingBandwidthComparator;
import com.google.android.exoplayer.extractor.mp3.Mp3Extractor;
import com.google.android.exoplayer.extractor.ts.AdtsExtractor;
import com.google.android.exoplayer.extractor.ts.PtsTimestampAdjuster;
import com.google.android.exoplayer.extractor.ts.TsExtractor;
import com.google.android.exoplayer.hls.HlsMediaPlaylist.Segment;
import com.google.android.exoplayer.hls.HlsTrackSelector.Output;
import com.google.android.exoplayer.upstream.BandwidthMeter;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DataSpec;
import com.google.android.exoplayer.upstream.HttpDataSource.InvalidResponseCodeException;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.MimeTypes;
import com.google.android.exoplayer.util.UriUtil;
import com.google.android.exoplayer.util.Util;
import com.google.android.gms.location.LocationStatusCodes;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class HlsChunkSource implements Output {
    private static final String AAC_FILE_EXTENSION = ".aac";
    public static final int ADAPTIVE_MODE_ABRUPT = 3;
    public static final int ADAPTIVE_MODE_NONE = 0;
    public static final int ADAPTIVE_MODE_SPLICE = 1;
    private static final float BANDWIDTH_FRACTION = 0.8f;
    public static final long DEFAULT_MAX_BUFFER_TO_SWITCH_DOWN_MS = 20000;
    public static final long DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS = 5000;
    public static final long DEFAULT_PLAYLIST_BLACKLIST_MS = 60000;
    private static final String MP3_FILE_EXTENSION = ".mp3";
    private static final String TAG = "HlsChunkSource";
    private static final String VTT_FILE_EXTENSION = ".vtt";
    private static final String WEBVTT_FILE_EXTENSION = ".webvtt";
    private final int adaptiveMode;
    private final BandwidthMeter bandwidthMeter;
    private final String baseUri;
    private final DataSource dataSource;
    private long durationUs;
    private byte[] encryptionIv;
    private String encryptionIvString;
    private byte[] encryptionKey;
    private Uri encryptionKeyUri;
    private IOException fatalError;
    private final boolean isMaster;
    private boolean live;
    private final HlsMasterPlaylist masterPlaylist;
    private final long maxBufferDurationToSwitchDownUs;
    private final long minBufferDurationToSwitchUpUs;
    private final HlsPlaylistParser playlistParser;
    private boolean prepareCalled;
    private byte[] scratchSpace;
    private int selectedTrackIndex;
    private int selectedVariantIndex;
    private final PtsTimestampAdjusterProvider timestampAdjusterProvider;
    private final HlsTrackSelector trackSelector;
    private final ArrayList<ExposedTrack> tracks;
    private long[] variantBlacklistTimes;
    private long[] variantLastPlaylistLoadTimesMs;
    private HlsMediaPlaylist[] variantPlaylists;
    private Variant[] variants;

    /* renamed from: com.google.android.exoplayer.hls.HlsChunkSource.1 */
    class C03111 implements Comparator<Variant> {
        private final Comparator<Format> formatComparator;

        C03111() {
            this.formatComparator = new DecreasingBandwidthComparator();
        }

        public int compare(Variant first, Variant second) {
            return this.formatComparator.compare(first.format, second.format);
        }
    }

    private static final class EncryptionKeyChunk extends DataChunk {
        public final String iv;
        private byte[] result;
        public final int variantIndex;

        public EncryptionKeyChunk(DataSource dataSource, DataSpec dataSpec, byte[] scratchSpace, String iv, int variantIndex) {
            super(dataSource, dataSpec, HlsChunkSource.ADAPTIVE_MODE_ABRUPT, HlsChunkSource.ADAPTIVE_MODE_NONE, null, -1, scratchSpace);
            this.iv = iv;
            this.variantIndex = variantIndex;
        }

        protected void consume(byte[] data, int limit) throws IOException {
            this.result = Arrays.copyOf(data, limit);
        }

        public byte[] getResult() {
            return this.result;
        }
    }

    public interface EventListener extends BaseChunkSampleSourceEventListener {
    }

    private static final class ExposedTrack {
        private final int adaptiveMaxHeight;
        private final int adaptiveMaxWidth;
        private final int defaultVariantIndex;
        private final Variant[] variants;

        public ExposedTrack(Variant fixedVariant) {
            Variant[] variantArr = new Variant[HlsChunkSource.ADAPTIVE_MODE_SPLICE];
            variantArr[HlsChunkSource.ADAPTIVE_MODE_NONE] = fixedVariant;
            this.variants = variantArr;
            this.defaultVariantIndex = HlsChunkSource.ADAPTIVE_MODE_NONE;
            this.adaptiveMaxWidth = -1;
            this.adaptiveMaxHeight = -1;
        }

        public ExposedTrack(Variant[] adaptiveVariants, int defaultVariantIndex, int maxWidth, int maxHeight) {
            this.variants = adaptiveVariants;
            this.defaultVariantIndex = defaultVariantIndex;
            this.adaptiveMaxWidth = maxWidth;
            this.adaptiveMaxHeight = maxHeight;
        }
    }

    private static final class MediaPlaylistChunk extends DataChunk {
        private final HlsPlaylistParser playlistParser;
        private final String playlistUrl;
        private HlsMediaPlaylist result;
        public final int variantIndex;

        public MediaPlaylistChunk(DataSource dataSource, DataSpec dataSpec, byte[] scratchSpace, HlsPlaylistParser playlistParser, int variantIndex, String playlistUrl) {
            super(dataSource, dataSpec, 4, HlsChunkSource.ADAPTIVE_MODE_NONE, null, -1, scratchSpace);
            this.variantIndex = variantIndex;
            this.playlistParser = playlistParser;
            this.playlistUrl = playlistUrl;
        }

        protected void consume(byte[] data, int limit) throws IOException {
            this.result = (HlsMediaPlaylist) this.playlistParser.parse(this.playlistUrl, new ByteArrayInputStream(data, HlsChunkSource.ADAPTIVE_MODE_NONE, limit));
        }

        public HlsMediaPlaylist getResult() {
            return this.result;
        }
    }

    public HlsChunkSource(boolean isMaster, DataSource dataSource, String playlistUrl, HlsPlaylist playlist, HlsTrackSelector trackSelector, BandwidthMeter bandwidthMeter, PtsTimestampAdjusterProvider timestampAdjusterProvider, int adaptiveMode) {
        this(isMaster, dataSource, playlistUrl, playlist, trackSelector, bandwidthMeter, timestampAdjusterProvider, adaptiveMode, DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS, DEFAULT_MAX_BUFFER_TO_SWITCH_DOWN_MS);
    }

    public HlsChunkSource(boolean isMaster, DataSource dataSource, String playlistUrl, HlsPlaylist playlist, HlsTrackSelector trackSelector, BandwidthMeter bandwidthMeter, PtsTimestampAdjusterProvider timestampAdjusterProvider, int adaptiveMode, long minBufferDurationToSwitchUpMs, long maxBufferDurationToSwitchDownMs) {
        this.isMaster = isMaster;
        this.dataSource = dataSource;
        this.trackSelector = trackSelector;
        this.bandwidthMeter = bandwidthMeter;
        this.timestampAdjusterProvider = timestampAdjusterProvider;
        this.adaptiveMode = adaptiveMode;
        this.minBufferDurationToSwitchUpUs = 1000 * minBufferDurationToSwitchUpMs;
        this.maxBufferDurationToSwitchDownUs = 1000 * maxBufferDurationToSwitchDownMs;
        this.baseUri = playlist.baseUri;
        this.playlistParser = new HlsPlaylistParser();
        this.tracks = new ArrayList();
        if (playlist.type == 0) {
            this.masterPlaylist = (HlsMasterPlaylist) playlist;
            return;
        }
        Format format = new Format("0", MimeTypes.APPLICATION_M3U8, -1, -1, -1.0f, -1, -1, -1, null, null);
        List<Variant> variants = new ArrayList();
        variants.add(new Variant(playlistUrl, format));
        this.masterPlaylist = new HlsMasterPlaylist(playlistUrl, variants, Collections.emptyList());
    }

    public void maybeThrowError() throws IOException {
        if (this.fatalError != null) {
            throw this.fatalError;
        }
    }

    public boolean prepare() {
        if (!this.prepareCalled) {
            this.prepareCalled = true;
            try {
                this.trackSelector.selectTracks(this.masterPlaylist, this);
                selectTrack(ADAPTIVE_MODE_NONE);
            } catch (IOException e) {
                this.fatalError = e;
            }
        }
        if (this.fatalError == null) {
            return true;
        }
        return false;
    }

    public boolean isLive() {
        return this.live;
    }

    public long getDurationUs() {
        return this.durationUs;
    }

    public int getTrackCount() {
        return this.tracks.size();
    }

    public Variant getFixedTrackVariant(int index) {
        Variant[] variants = ((ExposedTrack) this.tracks.get(index)).variants;
        return variants.length == ADAPTIVE_MODE_SPLICE ? variants[ADAPTIVE_MODE_NONE] : null;
    }

    public int getSelectedTrackIndex() {
        return this.selectedTrackIndex;
    }

    public void selectTrack(int index) {
        this.selectedTrackIndex = index;
        ExposedTrack selectedTrack = (ExposedTrack) this.tracks.get(this.selectedTrackIndex);
        this.selectedVariantIndex = selectedTrack.defaultVariantIndex;
        this.variants = selectedTrack.variants;
        this.variantPlaylists = new HlsMediaPlaylist[this.variants.length];
        this.variantLastPlaylistLoadTimesMs = new long[this.variants.length];
        this.variantBlacklistTimes = new long[this.variants.length];
    }

    public void seek() {
        if (this.isMaster) {
            this.timestampAdjusterProvider.reset();
        }
    }

    public void reset() {
        this.fatalError = null;
    }

    public void getChunkOperation(TsChunk previousTsChunk, long playbackPositionUs, ChunkOperationHolder out) {
        int nextVariantIndex;
        boolean switchingVariantSpliced;
        if (this.adaptiveMode == 0) {
            nextVariantIndex = this.selectedVariantIndex;
            switchingVariantSpliced = false;
        } else {
            nextVariantIndex = getNextVariantIndex(previousTsChunk, playbackPositionUs);
            switchingVariantSpliced = (previousTsChunk == null || this.variants[nextVariantIndex].format.equals(previousTsChunk.format) || this.adaptiveMode != ADAPTIVE_MODE_SPLICE) ? false : true;
        }
        HlsMediaPlaylist mediaPlaylist = this.variantPlaylists[nextVariantIndex];
        if (mediaPlaylist == null) {
            out.chunk = newMediaPlaylistChunk(nextVariantIndex);
            return;
        }
        int chunkMediaSequence;
        this.selectedVariantIndex = nextVariantIndex;
        if (this.live) {
            if (previousTsChunk == null) {
                chunkMediaSequence = getLiveStartChunkMediaSequence(nextVariantIndex);
            } else {
                chunkMediaSequence = switchingVariantSpliced ? previousTsChunk.chunkIndex : previousTsChunk.chunkIndex + ADAPTIVE_MODE_SPLICE;
                if (chunkMediaSequence < mediaPlaylist.mediaSequence) {
                    this.fatalError = new BehindLiveWindowException();
                    return;
                }
            }
        } else if (previousTsChunk == null) {
            chunkMediaSequence = Util.binarySearchFloor(mediaPlaylist.segments, Long.valueOf(playbackPositionUs), true, true) + mediaPlaylist.mediaSequence;
        } else {
            chunkMediaSequence = switchingVariantSpliced ? previousTsChunk.chunkIndex : previousTsChunk.chunkIndex + ADAPTIVE_MODE_SPLICE;
        }
        int chunkIndex = chunkMediaSequence - mediaPlaylist.mediaSequence;
        if (chunkIndex < mediaPlaylist.segments.size()) {
            long startTimeUs;
            HlsExtractorWrapper extractorWrapper;
            Segment segment = (Segment) mediaPlaylist.segments.get(chunkIndex);
            Uri chunkUri = UriUtil.resolveToUri(mediaPlaylist.baseUri, segment.url);
            if (segment.isEncrypted) {
                Uri keyUri = UriUtil.resolveToUri(mediaPlaylist.baseUri, segment.encryptionKeyUri);
                if (!keyUri.equals(this.encryptionKeyUri)) {
                    out.chunk = newEncryptionKeyChunk(keyUri, segment.encryptionIV, this.selectedVariantIndex);
                    return;
                } else if (!Util.areEqual(segment.encryptionIV, this.encryptionIvString)) {
                    setEncryptionData(keyUri, segment.encryptionIV, this.encryptionKey);
                }
            } else {
                clearEncryptionData();
            }
            DataSpec dataSpec = new DataSpec(chunkUri, (long) segment.byterangeOffset, (long) segment.byterangeLength, null);
            if (!this.live) {
                startTimeUs = segment.startTimeUs;
            } else if (previousTsChunk == null) {
                startTimeUs = 0;
            } else if (switchingVariantSpliced) {
                startTimeUs = previousTsChunk.startTimeUs;
            } else {
                startTimeUs = previousTsChunk.endTimeUs;
            }
            long endTimeUs = startTimeUs + ((long) (segment.durationSecs * 1000000.0d));
            Format format = this.variants[this.selectedVariantIndex].format;
            String lastPathSegment = chunkUri.getLastPathSegment();
            if (lastPathSegment.endsWith(AAC_FILE_EXTENSION)) {
                extractorWrapper = new HlsExtractorWrapper(ADAPTIVE_MODE_NONE, format, startTimeUs, new AdtsExtractor(startTimeUs), switchingVariantSpliced, -1, -1);
            } else {
                if (lastPathSegment.endsWith(MP3_FILE_EXTENSION)) {
                    extractorWrapper = new HlsExtractorWrapper(ADAPTIVE_MODE_NONE, format, startTimeUs, new Mp3Extractor(startTimeUs), switchingVariantSpliced, -1, -1);
                } else {
                    PtsTimestampAdjuster timestampAdjuster;
                    if (!lastPathSegment.endsWith(WEBVTT_FILE_EXTENSION)) {
                        if (!lastPathSegment.endsWith(VTT_FILE_EXTENSION)) {
                            if (previousTsChunk != null && previousTsChunk.discontinuitySequenceNumber == segment.discontinuitySequenceNumber && format.equals(previousTsChunk.format)) {
                                extractorWrapper = previousTsChunk.extractorWrapper;
                            } else {
                                timestampAdjuster = this.timestampAdjusterProvider.getAdjuster(this.isMaster, segment.discontinuitySequenceNumber, startTimeUs);
                                if (timestampAdjuster != null) {
                                    int workaroundFlags = ADAPTIVE_MODE_NONE;
                                    String codecs = format.codecs;
                                    if (!TextUtils.isEmpty(codecs)) {
                                        if (MimeTypes.getAudioMediaMimeType(codecs) != MimeTypes.AUDIO_AAC) {
                                            workaroundFlags = ADAPTIVE_MODE_NONE | 2;
                                        }
                                        if (MimeTypes.getVideoMediaMimeType(codecs) != MimeTypes.VIDEO_H264) {
                                            workaroundFlags |= 4;
                                        }
                                    }
                                    ExposedTrack selectedTrack = (ExposedTrack) this.tracks.get(this.selectedTrackIndex);
                                    extractorWrapper = new HlsExtractorWrapper(ADAPTIVE_MODE_NONE, format, startTimeUs, new TsExtractor(timestampAdjuster, workaroundFlags), switchingVariantSpliced, selectedTrack.adaptiveMaxWidth, selectedTrack.adaptiveMaxHeight);
                                } else {
                                    return;
                                }
                            }
                        }
                    }
                    timestampAdjuster = this.timestampAdjusterProvider.getAdjuster(this.isMaster, segment.discontinuitySequenceNumber, startTimeUs);
                    if (timestampAdjuster != null) {
                        extractorWrapper = new HlsExtractorWrapper(ADAPTIVE_MODE_NONE, format, startTimeUs, new WebvttExtractor(timestampAdjuster), switchingVariantSpliced, -1, -1);
                    } else {
                        return;
                    }
                }
            }
            out.chunk = new TsChunk(this.dataSource, dataSpec, ADAPTIVE_MODE_NONE, format, startTimeUs, endTimeUs, chunkMediaSequence, segment.discontinuitySequenceNumber, extractorWrapper, this.encryptionKey, this.encryptionIv);
        } else if (!mediaPlaylist.live) {
            out.endOfStream = true;
        } else if (shouldRerequestLiveMediaPlaylist(nextVariantIndex)) {
            out.chunk = newMediaPlaylistChunk(nextVariantIndex);
        }
    }

    public void onChunkLoadCompleted(Chunk chunk) {
        if (chunk instanceof MediaPlaylistChunk) {
            MediaPlaylistChunk mediaPlaylistChunk = (MediaPlaylistChunk) chunk;
            this.scratchSpace = mediaPlaylistChunk.getDataHolder();
            setMediaPlaylist(mediaPlaylistChunk.variantIndex, mediaPlaylistChunk.getResult());
        } else if (chunk instanceof EncryptionKeyChunk) {
            EncryptionKeyChunk encryptionKeyChunk = (EncryptionKeyChunk) chunk;
            this.scratchSpace = encryptionKeyChunk.getDataHolder();
            setEncryptionData(encryptionKeyChunk.dataSpec.uri, encryptionKeyChunk.iv, encryptionKeyChunk.getResult());
        }
    }

    public boolean onChunkLoadError(Chunk chunk, IOException e) {
        if (chunk.bytesLoaded() == 0 && (((chunk instanceof TsChunk) || (chunk instanceof MediaPlaylistChunk) || (chunk instanceof EncryptionKeyChunk)) && (e instanceof InvalidResponseCodeException))) {
            int responseCode = ((InvalidResponseCodeException) e).responseCode;
            if (responseCode == 404 || responseCode == 410) {
                int variantIndex;
                if (chunk instanceof TsChunk) {
                    variantIndex = getVariantIndex(((TsChunk) chunk).format);
                } else if (chunk instanceof MediaPlaylistChunk) {
                    variantIndex = ((MediaPlaylistChunk) chunk).variantIndex;
                } else {
                    variantIndex = ((EncryptionKeyChunk) chunk).variantIndex;
                }
                boolean alreadyBlacklisted = this.variantBlacklistTimes[variantIndex] != 0;
                this.variantBlacklistTimes[variantIndex] = SystemClock.elapsedRealtime();
                if (alreadyBlacklisted) {
                    Log.w(TAG, "Already blacklisted variant (" + responseCode + "): " + chunk.dataSpec.uri);
                    return false;
                } else if (allVariantsBlacklisted()) {
                    Log.w(TAG, "Final variant not blacklisted (" + responseCode + "): " + chunk.dataSpec.uri);
                    this.variantBlacklistTimes[variantIndex] = 0;
                    return false;
                } else {
                    Log.w(TAG, "Blacklisted variant (" + responseCode + "): " + chunk.dataSpec.uri);
                    return true;
                }
            }
        }
        return false;
    }

    public void adaptiveTrack(HlsMasterPlaylist playlist, Variant[] variants) {
        Arrays.sort(variants, new C03111());
        int defaultVariantIndex = ADAPTIVE_MODE_NONE;
        int maxWidth = -1;
        int maxHeight = -1;
        int minOriginalVariantIndex = Integer.MAX_VALUE;
        for (int i = ADAPTIVE_MODE_NONE; i < variants.length; i += ADAPTIVE_MODE_SPLICE) {
            int originalVariantIndex = playlist.variants.indexOf(variants[i]);
            if (originalVariantIndex < minOriginalVariantIndex) {
                minOriginalVariantIndex = originalVariantIndex;
                defaultVariantIndex = i;
            }
            Format variantFormat = variants[i].format;
            maxWidth = Math.max(variantFormat.width, maxWidth);
            maxHeight = Math.max(variantFormat.height, maxHeight);
        }
        if (maxWidth <= 0) {
            maxWidth = 1920;
        }
        if (maxHeight <= 0) {
            maxHeight = 1080;
        }
        this.tracks.add(new ExposedTrack(variants, defaultVariantIndex, maxWidth, maxHeight));
    }

    public void fixedTrack(HlsMasterPlaylist playlist, Variant variant) {
        this.tracks.add(new ExposedTrack(variant));
    }

    private int getNextVariantIndex(TsChunk previousTsChunk, long playbackPositionUs) {
        clearStaleBlacklistedVariants();
        long bitrateEstimate = this.bandwidthMeter.getBitrateEstimate();
        if (this.variantBlacklistTimes[this.selectedVariantIndex] != 0) {
            return getVariantIndexForBandwidth(bitrateEstimate);
        }
        if (previousTsChunk == null) {
            return this.selectedVariantIndex;
        }
        if (bitrateEstimate == -1) {
            return this.selectedVariantIndex;
        }
        int idealIndex = getVariantIndexForBandwidth(bitrateEstimate);
        if (idealIndex == this.selectedVariantIndex) {
            return this.selectedVariantIndex;
        }
        long bufferedUs = (this.adaptiveMode == ADAPTIVE_MODE_SPLICE ? previousTsChunk.startTimeUs : previousTsChunk.endTimeUs) - playbackPositionUs;
        if (this.variantBlacklistTimes[this.selectedVariantIndex] != 0) {
            return idealIndex;
        }
        if (idealIndex <= this.selectedVariantIndex || bufferedUs >= this.maxBufferDurationToSwitchDownUs) {
            return (idealIndex >= this.selectedVariantIndex || bufferedUs <= this.minBufferDurationToSwitchUpUs) ? this.selectedVariantIndex : idealIndex;
        } else {
            return idealIndex;
        }
    }

    private int getVariantIndexForBandwidth(long bitrateEstimate) {
        if (bitrateEstimate == -1) {
            bitrateEstimate = 0;
        }
        int effectiveBitrate = (int) (((float) bitrateEstimate) * BANDWIDTH_FRACTION);
        int lowestQualityEnabledVariantIndex = -1;
        for (int i = ADAPTIVE_MODE_NONE; i < this.variants.length; i += ADAPTIVE_MODE_SPLICE) {
            if (this.variantBlacklistTimes[i] == 0) {
                if (this.variants[i].format.bitrate <= effectiveBitrate) {
                    return i;
                }
                lowestQualityEnabledVariantIndex = i;
            }
        }
        Assertions.checkState(lowestQualityEnabledVariantIndex != -1);
        return lowestQualityEnabledVariantIndex;
    }

    private boolean shouldRerequestLiveMediaPlaylist(int nextVariantIndex) {
        return SystemClock.elapsedRealtime() - this.variantLastPlaylistLoadTimesMs[nextVariantIndex] >= ((long) ((this.variantPlaylists[nextVariantIndex].targetDurationSecs * LocationStatusCodes.GEOFENCE_NOT_AVAILABLE) / 2));
    }

    private int getLiveStartChunkMediaSequence(int variantIndex) {
        HlsMediaPlaylist mediaPlaylist = this.variantPlaylists[variantIndex];
        return mediaPlaylist.mediaSequence + (mediaPlaylist.segments.size() > ADAPTIVE_MODE_ABRUPT ? mediaPlaylist.segments.size() - 3 : ADAPTIVE_MODE_NONE);
    }

    private MediaPlaylistChunk newMediaPlaylistChunk(int variantIndex) {
        Uri mediaPlaylistUri = UriUtil.resolveToUri(this.baseUri, this.variants[variantIndex].url);
        return new MediaPlaylistChunk(this.dataSource, new DataSpec(mediaPlaylistUri, 0, -1, null, ADAPTIVE_MODE_SPLICE), this.scratchSpace, this.playlistParser, variantIndex, mediaPlaylistUri.toString());
    }

    private EncryptionKeyChunk newEncryptionKeyChunk(Uri keyUri, String iv, int variantIndex) {
        return new EncryptionKeyChunk(this.dataSource, new DataSpec(keyUri, 0, -1, null, ADAPTIVE_MODE_SPLICE), this.scratchSpace, iv, variantIndex);
    }

    private void setEncryptionData(Uri keyUri, String iv, byte[] secretKey) {
        String trimmedIv;
        if (iv.toLowerCase(Locale.getDefault()).startsWith("0x")) {
            trimmedIv = iv.substring(2);
        } else {
            trimmedIv = iv;
        }
        byte[] ivData = new BigInteger(trimmedIv, 16).toByteArray();
        byte[] ivDataWithPadding = new byte[16];
        int offset = ivData.length > 16 ? ivData.length - 16 : ADAPTIVE_MODE_NONE;
        System.arraycopy(ivData, offset, ivDataWithPadding, (ivDataWithPadding.length - ivData.length) + offset, ivData.length - offset);
        this.encryptionKeyUri = keyUri;
        this.encryptionKey = secretKey;
        this.encryptionIvString = iv;
        this.encryptionIv = ivDataWithPadding;
    }

    private void clearEncryptionData() {
        this.encryptionKeyUri = null;
        this.encryptionKey = null;
        this.encryptionIvString = null;
        this.encryptionIv = null;
    }

    private void setMediaPlaylist(int variantIndex, HlsMediaPlaylist mediaPlaylist) {
        this.variantLastPlaylistLoadTimesMs[variantIndex] = SystemClock.elapsedRealtime();
        this.variantPlaylists[variantIndex] = mediaPlaylist;
        this.live |= mediaPlaylist.live;
        this.durationUs = this.live ? -1 : mediaPlaylist.durationUs;
    }

    private boolean allVariantsBlacklisted() {
        for (int i = ADAPTIVE_MODE_NONE; i < this.variantBlacklistTimes.length; i += ADAPTIVE_MODE_SPLICE) {
            if (this.variantBlacklistTimes[i] == 0) {
                return false;
            }
        }
        return true;
    }

    private void clearStaleBlacklistedVariants() {
        long currentTime = SystemClock.elapsedRealtime();
        int i = ADAPTIVE_MODE_NONE;
        while (i < this.variantBlacklistTimes.length) {
            if (this.variantBlacklistTimes[i] != 0 && currentTime - this.variantBlacklistTimes[i] > DEFAULT_PLAYLIST_BLACKLIST_MS) {
                this.variantBlacklistTimes[i] = 0;
            }
            i += ADAPTIVE_MODE_SPLICE;
        }
    }

    private int getVariantIndex(Format format) {
        for (int i = ADAPTIVE_MODE_NONE; i < this.variants.length; i += ADAPTIVE_MODE_SPLICE) {
            if (this.variants[i].format.equals(format)) {
                return i;
            }
        }
        throw new IllegalStateException("Invalid format: " + format);
    }
}
