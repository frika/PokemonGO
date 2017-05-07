package com.google.android.exoplayer.text.tx3g;

import com.google.android.exoplayer.text.Cue;
import com.google.android.exoplayer.text.Subtitle;
import com.google.android.exoplayer.text.SubtitleParser;
import com.google.android.exoplayer.util.MimeTypes;

public final class Tx3gParser implements SubtitleParser {
    public boolean canParse(String mimeType) {
        return MimeTypes.APPLICATION_TX3G.equals(mimeType);
    }

    public Subtitle parse(byte[] bytes, int offset, int length) {
        return new Tx3gSubtitle(new Cue(new String(bytes, offset, length)));
    }
}
