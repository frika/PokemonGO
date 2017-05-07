package com.google.android.exoplayer.hls;

import android.content.Context;
import android.text.TextUtils;
import com.google.android.exoplayer.chunk.VideoFormatSelectorUtil;
import com.google.android.exoplayer.hls.HlsTrackSelector.Output;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class DefaultHlsTrackSelector implements HlsTrackSelector {
    private static final int TYPE_DEFAULT = 0;
    private static final int TYPE_VTT = 1;
    private final Context context;
    private final int type;

    public static DefaultHlsTrackSelector newDefaultInstance(Context context) {
        return new DefaultHlsTrackSelector(context, TYPE_DEFAULT);
    }

    public static DefaultHlsTrackSelector newVttInstance() {
        return new DefaultHlsTrackSelector(null, TYPE_VTT);
    }

    private DefaultHlsTrackSelector(Context context, int type) {
        this.context = context;
        this.type = type;
    }

    public void selectTracks(HlsMasterPlaylist playlist, Output output) throws IOException {
        int i;
        if (this.type == TYPE_VTT) {
            List<Variant> subtitleVariants = playlist.subtitles;
            if (subtitleVariants != null && !subtitleVariants.isEmpty()) {
                for (i = TYPE_DEFAULT; i < subtitleVariants.size(); i += TYPE_VTT) {
                    output.fixedTrack(playlist, (Variant) subtitleVariants.get(i));
                }
                return;
            }
            return;
        }
        ArrayList<Variant> enabledVariantList = new ArrayList();
        int[] variantIndices = VideoFormatSelectorUtil.selectVideoFormatsForDefaultDisplay(this.context, playlist.variants, null, false);
        for (i = TYPE_DEFAULT; i < variantIndices.length; i += TYPE_VTT) {
            enabledVariantList.add(playlist.variants.get(variantIndices[i]));
        }
        ArrayList<Variant> definiteVideoVariants = new ArrayList();
        ArrayList<Variant> definiteAudioOnlyVariants = new ArrayList();
        for (i = TYPE_DEFAULT; i < enabledVariantList.size(); i += TYPE_VTT) {
            Variant variant = (Variant) enabledVariantList.get(i);
            if (variant.format.height > 0 || variantHasExplicitCodecWithPrefix(variant, "avc")) {
                definiteVideoVariants.add(variant);
            } else if (variantHasExplicitCodecWithPrefix(variant, "mp4a")) {
                definiteAudioOnlyVariants.add(variant);
            }
        }
        if (!definiteVideoVariants.isEmpty()) {
            enabledVariantList = definiteVideoVariants;
        } else if (definiteAudioOnlyVariants.size() < enabledVariantList.size()) {
            enabledVariantList.removeAll(definiteAudioOnlyVariants);
        }
        if (enabledVariantList.size() > TYPE_VTT) {
            Variant[] enabledVariants = new Variant[enabledVariantList.size()];
            enabledVariantList.toArray(enabledVariants);
            output.adaptiveTrack(playlist, enabledVariants);
        }
        for (i = TYPE_DEFAULT; i < enabledVariantList.size(); i += TYPE_VTT) {
            output.fixedTrack(playlist, (Variant) enabledVariantList.get(i));
        }
    }

    private static boolean variantHasExplicitCodecWithPrefix(Variant variant, String prefix) {
        String codecs = variant.format.codecs;
        if (TextUtils.isEmpty(codecs)) {
            return false;
        }
        String[] codecArray = codecs.split("(\\s*,\\s*)|(\\s*$)");
        for (int i = TYPE_DEFAULT; i < codecArray.length; i += TYPE_VTT) {
            if (codecArray[i].startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }
}
