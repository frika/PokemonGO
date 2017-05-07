package com.google.android.exoplayer.text.eia608;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.MediaFormatHolder;
import com.google.android.exoplayer.SampleHolder;
import com.google.android.exoplayer.SampleSource;
import com.google.android.exoplayer.SampleSourceTrackRenderer;
import com.google.android.exoplayer.text.Cue;
import com.google.android.exoplayer.text.TextRenderer;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.Util;
import com.google.android.gms.location.places.Place;
import java.util.Collections;
import java.util.TreeSet;
import org.apache.commons.io.IOUtils;

public final class Eia608TrackRenderer extends SampleSourceTrackRenderer implements Callback {
    private static final int CC_MODE_PAINT_ON = 3;
    private static final int CC_MODE_POP_ON = 2;
    private static final int CC_MODE_ROLL_UP = 1;
    private static final int CC_MODE_UNKNOWN = 0;
    private static final int DEFAULT_CAPTIONS_ROW_COUNT = 4;
    private static final int MAX_SAMPLE_READAHEAD_US = 5000000;
    private static final int MSG_INVOKE_RENDERER = 0;
    private String caption;
    private int captionMode;
    private int captionRowCount;
    private final StringBuilder captionStringBuilder;
    private final Eia608Parser eia608Parser;
    private final MediaFormatHolder formatHolder;
    private boolean inputStreamEnded;
    private String lastRenderedCaption;
    private final TreeSet<ClosedCaptionList> pendingCaptionLists;
    private ClosedCaptionCtrl repeatableControl;
    private final SampleHolder sampleHolder;
    private final TextRenderer textRenderer;
    private final Handler textRendererHandler;

    public Eia608TrackRenderer(SampleSource source, TextRenderer textRenderer, Looper textRendererLooper) {
        SampleSource[] sampleSourceArr = new SampleSource[CC_MODE_ROLL_UP];
        sampleSourceArr[CC_MODE_UNKNOWN] = source;
        super(sampleSourceArr);
        this.textRenderer = (TextRenderer) Assertions.checkNotNull(textRenderer);
        this.textRendererHandler = textRendererLooper == null ? null : new Handler(textRendererLooper, this);
        this.eia608Parser = new Eia608Parser();
        this.formatHolder = new MediaFormatHolder();
        this.sampleHolder = new SampleHolder(CC_MODE_ROLL_UP);
        this.captionStringBuilder = new StringBuilder();
        this.pendingCaptionLists = new TreeSet();
    }

    protected boolean handlesTrack(MediaFormat mediaFormat) {
        return this.eia608Parser.canParse(mediaFormat.mimeType);
    }

    protected void onEnabled(int track, long positionUs, boolean joining) throws ExoPlaybackException {
        super.onEnabled(track, positionUs, joining);
    }

    protected void onDiscontinuity(long positionUs) {
        this.inputStreamEnded = false;
        this.repeatableControl = null;
        this.pendingCaptionLists.clear();
        clearPendingSample();
        this.captionRowCount = DEFAULT_CAPTIONS_ROW_COUNT;
        setCaptionMode(CC_MODE_UNKNOWN);
        invokeRenderer(null);
    }

    protected void doSomeWork(long positionUs, long elapsedRealtimeUs, boolean sourceIsReady) throws ExoPlaybackException {
        int result;
        if (isSamplePending()) {
            maybeParsePendingSample(positionUs);
        }
        if (this.inputStreamEnded) {
            result = -1;
        } else {
            result = -3;
        }
        while (!isSamplePending() && result == -3) {
            result = readSource(positionUs, this.formatHolder, this.sampleHolder);
            if (result == -3) {
                maybeParsePendingSample(positionUs);
            } else if (result == -1) {
                this.inputStreamEnded = true;
            }
        }
        while (!this.pendingCaptionLists.isEmpty() && ((ClosedCaptionList) this.pendingCaptionLists.first()).timeUs <= positionUs) {
            ClosedCaptionList nextCaptionList = (ClosedCaptionList) this.pendingCaptionLists.pollFirst();
            consumeCaptionList(nextCaptionList);
            if (!nextCaptionList.decodeOnly) {
                invokeRenderer(this.caption);
            }
        }
    }

    protected long getBufferedPositionUs() {
        return -3;
    }

    protected boolean isEnded() {
        return this.inputStreamEnded;
    }

    protected boolean isReady() {
        return true;
    }

    private void invokeRenderer(String text) {
        if (!Util.areEqual(this.lastRenderedCaption, text)) {
            this.lastRenderedCaption = text;
            if (this.textRendererHandler != null) {
                this.textRendererHandler.obtainMessage(CC_MODE_UNKNOWN, text).sendToTarget();
            } else {
                invokeRendererInternal(text);
            }
        }
    }

    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case CC_MODE_UNKNOWN /*0*/:
                invokeRendererInternal((String) msg.obj);
                return true;
            default:
                return false;
        }
    }

    private void invokeRendererInternal(String cueText) {
        if (cueText == null) {
            this.textRenderer.onCues(Collections.emptyList());
        } else {
            this.textRenderer.onCues(Collections.singletonList(new Cue(cueText)));
        }
    }

    private void maybeParsePendingSample(long positionUs) {
        if (this.sampleHolder.timeUs <= 5000000 + positionUs) {
            ClosedCaptionList holder = this.eia608Parser.parse(this.sampleHolder);
            clearPendingSample();
            if (holder != null) {
                this.pendingCaptionLists.add(holder);
            }
        }
    }

    private void consumeCaptionList(ClosedCaptionList captionList) {
        int captionBufferSize = captionList.captions.length;
        if (captionBufferSize != 0) {
            boolean isRepeatableControl = false;
            for (int i = CC_MODE_UNKNOWN; i < captionBufferSize; i += CC_MODE_ROLL_UP) {
                ClosedCaption caption = captionList.captions[i];
                if (caption.type == 0) {
                    ClosedCaptionCtrl captionCtrl = (ClosedCaptionCtrl) caption;
                    isRepeatableControl = captionBufferSize == CC_MODE_ROLL_UP && captionCtrl.isRepeatable();
                    if (isRepeatableControl && this.repeatableControl != null && this.repeatableControl.cc1 == captionCtrl.cc1 && this.repeatableControl.cc2 == captionCtrl.cc2) {
                        this.repeatableControl = null;
                    } else {
                        if (isRepeatableControl) {
                            this.repeatableControl = captionCtrl;
                        }
                        if (captionCtrl.isMiscCode()) {
                            handleMiscCode(captionCtrl);
                        } else if (captionCtrl.isPreambleAddressCode()) {
                            handlePreambleAddressCode();
                        }
                    }
                } else {
                    handleText((ClosedCaptionText) caption);
                }
            }
            if (!isRepeatableControl) {
                this.repeatableControl = null;
            }
            if (this.captionMode == CC_MODE_ROLL_UP || this.captionMode == CC_MODE_PAINT_ON) {
                this.caption = getDisplayCaption();
            }
        }
    }

    private void handleText(ClosedCaptionText captionText) {
        if (this.captionMode != 0) {
            this.captionStringBuilder.append(captionText.text);
        }
    }

    private void handleMiscCode(ClosedCaptionCtrl captionCtrl) {
        switch (captionCtrl.cc2) {
            case Place.TYPE_ELECTRONICS_STORE /*32*/:
                setCaptionMode(CC_MODE_POP_ON);
            case Place.TYPE_FLORIST /*37*/:
                this.captionRowCount = CC_MODE_POP_ON;
                setCaptionMode(CC_MODE_ROLL_UP);
            case Place.TYPE_FOOD /*38*/:
                this.captionRowCount = CC_MODE_PAINT_ON;
                setCaptionMode(CC_MODE_ROLL_UP);
            case Place.TYPE_FUNERAL_HOME /*39*/:
                this.captionRowCount = DEFAULT_CAPTIONS_ROW_COUNT;
                setCaptionMode(CC_MODE_ROLL_UP);
            case Place.TYPE_GAS_STATION /*41*/:
                setCaptionMode(CC_MODE_PAINT_ON);
            default:
                if (this.captionMode != 0) {
                    switch (captionCtrl.cc2) {
                        case Place.TYPE_EMBASSY /*33*/:
                            if (this.captionStringBuilder.length() > 0) {
                                this.captionStringBuilder.setLength(this.captionStringBuilder.length() - 1);
                            }
                        case Place.TYPE_GYM /*44*/:
                            this.caption = null;
                            if (this.captionMode == CC_MODE_ROLL_UP || this.captionMode == CC_MODE_PAINT_ON) {
                                this.captionStringBuilder.setLength(CC_MODE_UNKNOWN);
                            }
                        case Place.TYPE_HAIR_CARE /*45*/:
                            maybeAppendNewline();
                        case Place.TYPE_HARDWARE_STORE /*46*/:
                            this.captionStringBuilder.setLength(CC_MODE_UNKNOWN);
                        case Place.TYPE_HEALTH /*47*/:
                            this.caption = getDisplayCaption();
                            this.captionStringBuilder.setLength(CC_MODE_UNKNOWN);
                        default:
                    }
                }
        }
    }

    private void handlePreambleAddressCode() {
        maybeAppendNewline();
    }

    private void setCaptionMode(int captionMode) {
        if (this.captionMode != captionMode) {
            this.captionMode = captionMode;
            this.captionStringBuilder.setLength(CC_MODE_UNKNOWN);
            if (captionMode == CC_MODE_ROLL_UP || captionMode == 0) {
                this.caption = null;
            }
        }
    }

    private void maybeAppendNewline() {
        int buildLength = this.captionStringBuilder.length();
        if (buildLength > 0 && this.captionStringBuilder.charAt(buildLength - 1) != '\n') {
            this.captionStringBuilder.append('\n');
        }
    }

    private String getDisplayCaption() {
        int buildLength = this.captionStringBuilder.length();
        if (buildLength == 0) {
            return null;
        }
        boolean endsWithNewline;
        if (this.captionStringBuilder.charAt(buildLength - 1) == '\n') {
            endsWithNewline = true;
        } else {
            endsWithNewline = false;
        }
        if (buildLength == CC_MODE_ROLL_UP && endsWithNewline) {
            return null;
        }
        int endIndex;
        if (endsWithNewline) {
            endIndex = buildLength - 1;
        } else {
            endIndex = buildLength;
        }
        if (this.captionMode != CC_MODE_ROLL_UP) {
            return this.captionStringBuilder.substring(CC_MODE_UNKNOWN, endIndex);
        }
        int startIndex = CC_MODE_UNKNOWN;
        int searchBackwardFromIndex = endIndex;
        for (int i = CC_MODE_UNKNOWN; i < this.captionRowCount && searchBackwardFromIndex != -1; i += CC_MODE_ROLL_UP) {
            searchBackwardFromIndex = this.captionStringBuilder.lastIndexOf(IOUtils.LINE_SEPARATOR_UNIX, searchBackwardFromIndex - 1);
        }
        if (searchBackwardFromIndex != -1) {
            startIndex = searchBackwardFromIndex + CC_MODE_ROLL_UP;
        }
        this.captionStringBuilder.delete(CC_MODE_UNKNOWN, startIndex);
        return this.captionStringBuilder.substring(CC_MODE_UNKNOWN, endIndex - startIndex);
    }

    private void clearPendingSample() {
        this.sampleHolder.timeUs = -1;
        this.sampleHolder.clearData();
    }

    private boolean isSamplePending() {
        return this.sampleHolder.timeUs != -1;
    }
}
