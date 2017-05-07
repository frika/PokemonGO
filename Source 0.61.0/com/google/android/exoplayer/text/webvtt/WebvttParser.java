package com.google.android.exoplayer.text.webvtt;

import android.text.TextUtils;
import com.google.android.exoplayer.ParserException;
import com.google.android.exoplayer.text.SubtitleParser;
import com.google.android.exoplayer.text.webvtt.WebvttCue.Builder;
import com.google.android.exoplayer.util.MimeTypes;
import com.google.android.exoplayer.util.ParsableByteArray;
import java.util.ArrayList;

public final class WebvttParser implements SubtitleParser {
    private final WebvttCueParser cueParser;
    private final ParsableByteArray parsableWebvttData;
    private final Builder webvttCueBuilder;

    public WebvttParser() {
        this.cueParser = new WebvttCueParser();
        this.parsableWebvttData = new ParsableByteArray();
        this.webvttCueBuilder = new Builder();
    }

    public final boolean canParse(String mimeType) {
        return MimeTypes.TEXT_VTT.equals(mimeType);
    }

    public final WebvttSubtitle parse(byte[] bytes, int offset, int length) throws ParserException {
        this.parsableWebvttData.reset(bytes, offset + length);
        this.parsableWebvttData.setPosition(offset);
        this.webvttCueBuilder.reset();
        WebvttParserUtil.validateWebvttHeaderLine(this.parsableWebvttData);
        do {
        } while (!TextUtils.isEmpty(this.parsableWebvttData.readLine()));
        ArrayList<WebvttCue> subtitles = new ArrayList();
        while (this.cueParser.parseNextValidCue(this.parsableWebvttData, this.webvttCueBuilder)) {
            subtitles.add(this.webvttCueBuilder.build());
            this.webvttCueBuilder.reset();
        }
        return new WebvttSubtitle(subtitles);
    }
}
