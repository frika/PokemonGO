package com.upsight.mediation.vast.model;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import com.upsight.mediation.log.FuseLog;
import com.upsight.mediation.vast.VASTPlayer;
import com.upsight.mediation.vast.util.XmlTools;
import com.voxelbusters.nativeplugins.defines.Keys;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import spacemadness.com.lunarconsole.BuildConfig;

public class VASTModel implements Serializable {
    public static final long DOWNLOAD_TIMEOUT_LIMIT = 30000;
    private static String TAG = null;
    private static final String adSystemXPATH = "/VAST/Ad/InLine/AdSystem";
    private static final String adTitleXPATH = "/VAST/Ad/InLine/AdTitle";
    private static final String combinedTrackingXPATH = "/VAST/Ad/InLine/Creatives/Creative/Linear/TrackingEvents/Tracking|/VAST/Ad/InLine/Creatives/Creative/NonLinearAds/TrackingEvents/Tracking|/VAST/Ad/Wrapper/Creatives/Creative/Linear/TrackingEvents/Tracking|/VAST/Ad/Wrapper/Creatives/Creative/NonLinearAds/TrackingEvents/Tracking";
    private static final String durationXPATH = "/VAST/Ad/InLine/Creatives/Creative/Linear/Duration";
    private static final String errorUrlXPATH = "//Error";
    private static final String impressionXPATH = "/VAST/Ad/InLine/Impression";
    private static final String inlineLinearTrackingXPATH = "/VAST/Ad/InLine/Creatives/Creative/Linear/TrackingEvents/Tracking";
    private static final String inlineNonLinearTrackingXPATH = "/VAST/Ad/InLine/Creatives/Creative/NonLinearAds/TrackingEvents/Tracking";
    private static final String mediaFileXPATH = "/VAST/Ad/InLine/Creatives/Creative/Linear/MediaFiles/MediaFile";
    private static final long serialVersionUID = 4318368258447283733L;
    private static final String skipOffsetXPATH = "/VAST/Ad/InLine/Creatives/Creative/Linear[@skipoffset]";
    private static final String vastXPATH = "//VAST";
    private static final String videoClicksXPATH = "//VideoClicks";
    private static final String wrapperLinearTrackingXPATH = "/VAST/Ad/Wrapper/Creatives/Creative/Linear/TrackingEvents/Tracking";
    private static final String wrapperNonLinearTrackingXPATH = "/VAST/Ad/Wrapper/Creatives/Creative/NonLinearAds/TrackingEvents/Tracking";
    private String mediaFileDeliveryType;
    private String mediaFileLocation;
    private transient Document vastsDocument;

    /* renamed from: com.upsight.mediation.vast.model.VASTModel.1 */
    class C12461 implements Runnable {
        final /* synthetic */ Context val$c;
        final /* synthetic */ int val$downloadTimeout;
        final /* synthetic */ VASTPlayer val$vastPlayer;

        C12461(VASTPlayer vASTPlayer, Context context, int i) {
            this.val$vastPlayer = vASTPlayer;
            this.val$c = context;
            this.val$downloadTimeout = i;
        }

        public void run() {
            new DownloadTask(this.val$vastPlayer, this.val$c, this.val$downloadTimeout).execute(new String[]{VASTModel.this.mediaFileLocation});
        }
    }

    private class DownloadTask extends AsyncTask<String, Void, Integer> {
        private final Context context;
        private final long downloadTimeout;
        private final VASTPlayer mVastPlayer;

        public DownloadTask(VASTPlayer vastPlayer, Context context, int downloadTimeout) {
            this.mVastPlayer = vastPlayer;
            this.context = context;
            this.downloadTimeout = downloadTimeout > 0 ? (long) downloadTimeout : VASTModel.DOWNLOAD_TIMEOUT_LIMIT;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        protected java.lang.Integer doInBackground(java.lang.String... r27) {
            /*
            r26 = this;
            r15 = 0;
            r16 = 0;
            r3 = 0;
            r9 = 0;
            r18 = java.lang.System.nanoTime();	 Catch:{ SocketTimeoutException -> 0x02ad, Exception -> 0x026d }
            r20 = new java.net.URL;	 Catch:{ SocketTimeoutException -> 0x02ad, Exception -> 0x026d }
            r21 = 0;
            r21 = r27[r21];	 Catch:{ SocketTimeoutException -> 0x02ad, Exception -> 0x026d }
            r20.<init>(r21);	 Catch:{ SocketTimeoutException -> 0x02ad, Exception -> 0x026d }
            r21 = 0;
            r21 = r27[r21];	 Catch:{ SocketTimeoutException -> 0x02ad, Exception -> 0x026d }
            r22 = 0;
            r22 = r27[r22];	 Catch:{ SocketTimeoutException -> 0x02ad, Exception -> 0x026d }
            r23 = 47;
            r22 = r22.lastIndexOf(r23);	 Catch:{ SocketTimeoutException -> 0x02ad, Exception -> 0x026d }
            r22 = r22 + 1;
            r23 = 0;
            r23 = r27[r23];	 Catch:{ SocketTimeoutException -> 0x02ad, Exception -> 0x026d }
            r23 = r23.length();	 Catch:{ SocketTimeoutException -> 0x02ad, Exception -> 0x026d }
            r14 = r21.substring(r22, r23);	 Catch:{ SocketTimeoutException -> 0x02ad, Exception -> 0x026d }
            r21 = r20.openConnection();	 Catch:{ SocketTimeoutException -> 0x02ad, Exception -> 0x026d }
            r0 = r21;
            r0 = (java.net.HttpURLConnection) r0;	 Catch:{ SocketTimeoutException -> 0x02ad, Exception -> 0x026d }
            r3 = r0;
            r21 = "HEAD";
            r0 = r21;
            r3.setRequestMethod(r0);	 Catch:{ SocketTimeoutException -> 0x02ad, Exception -> 0x026d }
            r21 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;
            r0 = r21;
            r3.setConnectTimeout(r0);	 Catch:{ SocketTimeoutException -> 0x02ad, Exception -> 0x026d }
            r3.connect();	 Catch:{ SocketTimeoutException -> 0x02ad, Exception -> 0x026d }
            r13 = r3.getContentLength();	 Catch:{ SocketTimeoutException -> 0x02ad, Exception -> 0x026d }
            r21 = r3.getResponseCode();	 Catch:{ SocketTimeoutException -> 0x02ad, Exception -> 0x026d }
            r22 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
            r0 = r21;
            r1 = r22;
            if (r0 == r1) goto L_0x009a;
        L_0x0058:
            r21 = com.upsight.mediation.vast.model.VASTModel.TAG;	 Catch:{ SocketTimeoutException -> 0x02ad, Exception -> 0x026d }
            r22 = new java.lang.StringBuilder;	 Catch:{ SocketTimeoutException -> 0x02ad, Exception -> 0x026d }
            r22.<init>();	 Catch:{ SocketTimeoutException -> 0x02ad, Exception -> 0x026d }
            r23 = "Server returned HTTP ";
            r22 = r22.append(r23);	 Catch:{ SocketTimeoutException -> 0x02ad, Exception -> 0x026d }
            r23 = r3.getResponseCode();	 Catch:{ SocketTimeoutException -> 0x02ad, Exception -> 0x026d }
            r22 = r22.append(r23);	 Catch:{ SocketTimeoutException -> 0x02ad, Exception -> 0x026d }
            r23 = " ";
            r22 = r22.append(r23);	 Catch:{ SocketTimeoutException -> 0x02ad, Exception -> 0x026d }
            r23 = r3.getResponseMessage();	 Catch:{ SocketTimeoutException -> 0x02ad, Exception -> 0x026d }
            r22 = r22.append(r23);	 Catch:{ SocketTimeoutException -> 0x02ad, Exception -> 0x026d }
            r22 = r22.toString();	 Catch:{ SocketTimeoutException -> 0x02ad, Exception -> 0x026d }
            com.upsight.mediation.log.FuseLog.m604d(r21, r22);	 Catch:{ SocketTimeoutException -> 0x02ad, Exception -> 0x026d }
            r21 = 401; // 0x191 float:5.62E-43 double:1.98E-321;
            r21 = java.lang.Integer.valueOf(r21);	 Catch:{ SocketTimeoutException -> 0x02ad, Exception -> 0x026d }
            if (r16 == 0) goto L_0x008f;
        L_0x008c:
            r16.close();	 Catch:{ IOException -> 0x02c7 }
        L_0x008f:
            if (r15 == 0) goto L_0x0094;
        L_0x0091:
            r15.close();	 Catch:{ IOException -> 0x02c7 }
        L_0x0094:
            if (r3 == 0) goto L_0x0099;
        L_0x0096:
            r3.disconnect();
        L_0x0099:
            return r21;
        L_0x009a:
            r3.disconnect();	 Catch:{ SocketTimeoutException -> 0x02ad, Exception -> 0x026d }
            r2 = new java.io.File;	 Catch:{ SocketTimeoutException -> 0x02ad, Exception -> 0x026d }
            r0 = r26;
            r0 = r0.context;	 Catch:{ SocketTimeoutException -> 0x02ad, Exception -> 0x026d }
            r21 = r0;
            r21 = r21.getCacheDir();	 Catch:{ SocketTimeoutException -> 0x02ad, Exception -> 0x026d }
            r22 = "fuse_vast_cache";
            r0 = r21;
            r1 = r22;
            r2.<init>(r0, r1);	 Catch:{ SocketTimeoutException -> 0x02ad, Exception -> 0x026d }
            r21 = r2.exists();	 Catch:{ SocketTimeoutException -> 0x02ad, Exception -> 0x026d }
            if (r21 != 0) goto L_0x00d4;
        L_0x00b8:
            r21 = r2.mkdir();	 Catch:{ SocketTimeoutException -> 0x02ad, Exception -> 0x026d }
            if (r21 != 0) goto L_0x00d4;
        L_0x00be:
            r21 = 401; // 0x191 float:5.62E-43 double:1.98E-321;
            r21 = java.lang.Integer.valueOf(r21);	 Catch:{ SocketTimeoutException -> 0x02ad, Exception -> 0x026d }
            if (r16 == 0) goto L_0x00c9;
        L_0x00c6:
            r16.close();	 Catch:{ IOException -> 0x02c4 }
        L_0x00c9:
            if (r15 == 0) goto L_0x00ce;
        L_0x00cb:
            r15.close();	 Catch:{ IOException -> 0x02c4 }
        L_0x00ce:
            if (r3 == 0) goto L_0x0099;
        L_0x00d0:
            r3.disconnect();
            goto L_0x0099;
        L_0x00d4:
            r12 = new java.io.File;	 Catch:{ SocketTimeoutException -> 0x02ad, Exception -> 0x026d }
            r12.<init>(r2, r14);	 Catch:{ SocketTimeoutException -> 0x02ad, Exception -> 0x026d }
            r21 = r12.exists();	 Catch:{ SocketTimeoutException -> 0x02b0, Exception -> 0x02a2, all -> 0x0298 }
            if (r21 == 0) goto L_0x010e;
        L_0x00df:
            r22 = r12.length();	 Catch:{ SocketTimeoutException -> 0x02b0, Exception -> 0x02a2, all -> 0x0298 }
            r0 = (long) r13;	 Catch:{ SocketTimeoutException -> 0x02b0, Exception -> 0x02a2, all -> 0x0298 }
            r24 = r0;
            r21 = (r22 > r24 ? 1 : (r22 == r24 ? 0 : -1));
            if (r21 != 0) goto L_0x010e;
        L_0x00ea:
            r0 = r26;
            r0 = com.upsight.mediation.vast.model.VASTModel.this;	 Catch:{ SocketTimeoutException -> 0x02b0, Exception -> 0x02a2, all -> 0x0298 }
            r21 = r0;
            r22 = r12.getAbsolutePath();	 Catch:{ SocketTimeoutException -> 0x02b0, Exception -> 0x02a2, all -> 0x0298 }
            r21.mediaFileLocation = r22;	 Catch:{ SocketTimeoutException -> 0x02b0, Exception -> 0x02a2, all -> 0x0298 }
            r21 = 0;
            r21 = java.lang.Integer.valueOf(r21);	 Catch:{ SocketTimeoutException -> 0x02b0, Exception -> 0x02a2, all -> 0x0298 }
            if (r16 == 0) goto L_0x0102;
        L_0x00ff:
            r16.close();	 Catch:{ IOException -> 0x02c1 }
        L_0x0102:
            if (r15 == 0) goto L_0x0107;
        L_0x0104:
            r15.close();	 Catch:{ IOException -> 0x02c1 }
        L_0x0107:
            if (r3 == 0) goto L_0x010c;
        L_0x0109:
            r3.disconnect();
        L_0x010c:
            r9 = r12;
            goto L_0x0099;
        L_0x010e:
            r0 = (long) r13;
            r22 = r0;
            r0 = r26;
            r0 = r0.mVastPlayer;	 Catch:{ SocketTimeoutException -> 0x02b0, Exception -> 0x02a2, all -> 0x0298 }
            r21 = r0;
            r24 = r21.getMaxFileSize();	 Catch:{ SocketTimeoutException -> 0x02b0, Exception -> 0x02a2, all -> 0x0298 }
            r21 = (r22 > r24 ? 1 : (r22 == r24 ? 0 : -1));
            if (r21 <= 0) goto L_0x0137;
        L_0x011f:
            r21 = 400; // 0x190 float:5.6E-43 double:1.976E-321;
            r21 = java.lang.Integer.valueOf(r21);	 Catch:{ SocketTimeoutException -> 0x02b0, Exception -> 0x02a2, all -> 0x0298 }
            if (r16 == 0) goto L_0x012a;
        L_0x0127:
            r16.close();	 Catch:{ IOException -> 0x02be }
        L_0x012a:
            if (r15 == 0) goto L_0x012f;
        L_0x012c:
            r15.close();	 Catch:{ IOException -> 0x02be }
        L_0x012f:
            if (r3 == 0) goto L_0x0134;
        L_0x0131:
            r3.disconnect();
        L_0x0134:
            r9 = r12;
            goto L_0x0099;
        L_0x0137:
            r21 = r20.openConnection();	 Catch:{ SocketTimeoutException -> 0x02b0, Exception -> 0x02a2, all -> 0x0298 }
            r0 = r21;
            r0 = (java.net.HttpURLConnection) r0;	 Catch:{ SocketTimeoutException -> 0x02b0, Exception -> 0x02a2, all -> 0x0298 }
            r3 = r0;
            r21 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;
            r0 = r21;
            r3.setConnectTimeout(r0);	 Catch:{ SocketTimeoutException -> 0x02b0, Exception -> 0x02a2, all -> 0x0298 }
            r3.connect();	 Catch:{ SocketTimeoutException -> 0x02b0, Exception -> 0x02a2, all -> 0x0298 }
            r21 = r3.getResponseCode();	 Catch:{ SocketTimeoutException -> 0x02b0, Exception -> 0x02a2, all -> 0x0298 }
            r22 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
            r0 = r21;
            r1 = r22;
            if (r0 == r1) goto L_0x019a;
        L_0x0156:
            r21 = com.upsight.mediation.vast.model.VASTModel.TAG;	 Catch:{ SocketTimeoutException -> 0x02b0, Exception -> 0x02a2, all -> 0x0298 }
            r22 = new java.lang.StringBuilder;	 Catch:{ SocketTimeoutException -> 0x02b0, Exception -> 0x02a2, all -> 0x0298 }
            r22.<init>();	 Catch:{ SocketTimeoutException -> 0x02b0, Exception -> 0x02a2, all -> 0x0298 }
            r23 = "Server returned HTTP ";
            r22 = r22.append(r23);	 Catch:{ SocketTimeoutException -> 0x02b0, Exception -> 0x02a2, all -> 0x0298 }
            r23 = r3.getResponseCode();	 Catch:{ SocketTimeoutException -> 0x02b0, Exception -> 0x02a2, all -> 0x0298 }
            r22 = r22.append(r23);	 Catch:{ SocketTimeoutException -> 0x02b0, Exception -> 0x02a2, all -> 0x0298 }
            r23 = " ";
            r22 = r22.append(r23);	 Catch:{ SocketTimeoutException -> 0x02b0, Exception -> 0x02a2, all -> 0x0298 }
            r23 = r3.getResponseMessage();	 Catch:{ SocketTimeoutException -> 0x02b0, Exception -> 0x02a2, all -> 0x0298 }
            r22 = r22.append(r23);	 Catch:{ SocketTimeoutException -> 0x02b0, Exception -> 0x02a2, all -> 0x0298 }
            r22 = r22.toString();	 Catch:{ SocketTimeoutException -> 0x02b0, Exception -> 0x02a2, all -> 0x0298 }
            com.upsight.mediation.log.FuseLog.m604d(r21, r22);	 Catch:{ SocketTimeoutException -> 0x02b0, Exception -> 0x02a2, all -> 0x0298 }
            r21 = 401; // 0x191 float:5.62E-43 double:1.98E-321;
            r21 = java.lang.Integer.valueOf(r21);	 Catch:{ SocketTimeoutException -> 0x02b0, Exception -> 0x02a2, all -> 0x0298 }
            if (r16 == 0) goto L_0x018d;
        L_0x018a:
            r16.close();	 Catch:{ IOException -> 0x02bb }
        L_0x018d:
            if (r15 == 0) goto L_0x0192;
        L_0x018f:
            r15.close();	 Catch:{ IOException -> 0x02bb }
        L_0x0192:
            if (r3 == 0) goto L_0x0197;
        L_0x0194:
            r3.disconnect();
        L_0x0197:
            r9 = r12;
            goto L_0x0099;
        L_0x019a:
            r15 = r3.getInputStream();	 Catch:{ SocketTimeoutException -> 0x02b0, Exception -> 0x02a2, all -> 0x0298 }
            r17 = new java.io.FileOutputStream;	 Catch:{ SocketTimeoutException -> 0x02b0, Exception -> 0x02a2, all -> 0x0298 }
            r0 = r17;
            r0.<init>(r12);	 Catch:{ SocketTimeoutException -> 0x02b0, Exception -> 0x02a2, all -> 0x0298 }
            r21 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
            r0 = r21;
            r5 = new byte[r0];	 Catch:{ SocketTimeoutException -> 0x0200, Exception -> 0x02a5, all -> 0x029b }
        L_0x01ab:
            r4 = r15.read(r5);	 Catch:{ SocketTimeoutException -> 0x0200, Exception -> 0x02a5, all -> 0x029b }
            r21 = -1;
            r0 = r21;
            if (r4 == r0) goto L_0x021b;
        L_0x01b5:
            r10 = java.lang.System.nanoTime();	 Catch:{ SocketTimeoutException -> 0x0200, Exception -> 0x02a5, all -> 0x029b }
            r22 = r10 - r18;
            r24 = 1000000; // 0xf4240 float:1.401298E-39 double:4.940656E-318;
            r6 = r22 / r24;
            r0 = r26;
            r0 = r0.downloadTimeout;	 Catch:{ SocketTimeoutException -> 0x0200, Exception -> 0x02a5, all -> 0x029b }
            r22 = r0;
            r21 = (r6 > r22 ? 1 : (r6 == r22 ? 0 : -1));
            if (r21 < 0) goto L_0x01d3;
        L_0x01ca:
            r21 = 1;
            r0 = r26;
            r1 = r21;
            r0.cancel(r1);	 Catch:{ SocketTimeoutException -> 0x0200, Exception -> 0x02a5, all -> 0x029b }
        L_0x01d3:
            r21 = r26.isCancelled();	 Catch:{ SocketTimeoutException -> 0x0200, Exception -> 0x02a5, all -> 0x029b }
            if (r21 == 0) goto L_0x01f6;
        L_0x01d9:
            r15.close();	 Catch:{ SocketTimeoutException -> 0x0200, Exception -> 0x02a5, all -> 0x029b }
            r21 = 402; // 0x192 float:5.63E-43 double:1.986E-321;
            r21 = java.lang.Integer.valueOf(r21);	 Catch:{ SocketTimeoutException -> 0x0200, Exception -> 0x02a5, all -> 0x029b }
            if (r17 == 0) goto L_0x01e7;
        L_0x01e4:
            r17.close();	 Catch:{ IOException -> 0x02b8 }
        L_0x01e7:
            if (r15 == 0) goto L_0x01ec;
        L_0x01e9:
            r15.close();	 Catch:{ IOException -> 0x02b8 }
        L_0x01ec:
            if (r3 == 0) goto L_0x01f1;
        L_0x01ee:
            r3.disconnect();
        L_0x01f1:
            r9 = r12;
            r16 = r17;
            goto L_0x0099;
        L_0x01f6:
            r21 = 0;
            r0 = r17;
            r1 = r21;
            r0.write(r5, r1, r4);	 Catch:{ SocketTimeoutException -> 0x0200, Exception -> 0x02a5, all -> 0x029b }
            goto L_0x01ab;
        L_0x0200:
            r8 = move-exception;
            r9 = r12;
            r16 = r17;
        L_0x0204:
            r21 = 402; // 0x192 float:5.63E-43 double:1.986E-321;
            r21 = java.lang.Integer.valueOf(r21);	 Catch:{ all -> 0x0285 }
            if (r16 == 0) goto L_0x020f;
        L_0x020c:
            r16.close();	 Catch:{ IOException -> 0x02aa }
        L_0x020f:
            if (r15 == 0) goto L_0x0214;
        L_0x0211:
            r15.close();	 Catch:{ IOException -> 0x02aa }
        L_0x0214:
            if (r3 == 0) goto L_0x0099;
        L_0x0216:
            r3.disconnect();
            goto L_0x0099;
        L_0x021b:
            r21 = r12.exists();	 Catch:{ SocketTimeoutException -> 0x0200, Exception -> 0x02a5, all -> 0x029b }
            if (r21 == 0) goto L_0x022c;
        L_0x0221:
            r22 = r12.length();	 Catch:{ SocketTimeoutException -> 0x0200, Exception -> 0x02a5, all -> 0x029b }
            r0 = (long) r13;	 Catch:{ SocketTimeoutException -> 0x0200, Exception -> 0x02a5, all -> 0x029b }
            r24 = r0;
            r21 = (r22 > r24 ? 1 : (r22 == r24 ? 0 : -1));
            if (r21 == 0) goto L_0x0246;
        L_0x022c:
            r21 = 400; // 0x190 float:5.6E-43 double:1.976E-321;
            r21 = java.lang.Integer.valueOf(r21);	 Catch:{ SocketTimeoutException -> 0x0200, Exception -> 0x02a5, all -> 0x029b }
            if (r17 == 0) goto L_0x0237;
        L_0x0234:
            r17.close();	 Catch:{ IOException -> 0x02b6 }
        L_0x0237:
            if (r15 == 0) goto L_0x023c;
        L_0x0239:
            r15.close();	 Catch:{ IOException -> 0x02b6 }
        L_0x023c:
            if (r3 == 0) goto L_0x0241;
        L_0x023e:
            r3.disconnect();
        L_0x0241:
            r9 = r12;
            r16 = r17;
            goto L_0x0099;
        L_0x0246:
            if (r17 == 0) goto L_0x024b;
        L_0x0248:
            r17.close();	 Catch:{ IOException -> 0x02b4 }
        L_0x024b:
            if (r15 == 0) goto L_0x0250;
        L_0x024d:
            r15.close();	 Catch:{ IOException -> 0x02b4 }
        L_0x0250:
            if (r3 == 0) goto L_0x0255;
        L_0x0252:
            r3.disconnect();
        L_0x0255:
            r0 = r26;
            r0 = com.upsight.mediation.vast.model.VASTModel.this;
            r21 = r0;
            r22 = r12.getAbsolutePath();
            r21.mediaFileLocation = r22;
            r21 = 0;
            r21 = java.lang.Integer.valueOf(r21);
            r9 = r12;
            r16 = r17;
            goto L_0x0099;
        L_0x026d:
            r8 = move-exception;
        L_0x026e:
            r21 = 400; // 0x190 float:5.6E-43 double:1.976E-321;
            r21 = java.lang.Integer.valueOf(r21);	 Catch:{ all -> 0x0285 }
            if (r16 == 0) goto L_0x0279;
        L_0x0276:
            r16.close();	 Catch:{ IOException -> 0x02a0 }
        L_0x0279:
            if (r15 == 0) goto L_0x027e;
        L_0x027b:
            r15.close();	 Catch:{ IOException -> 0x02a0 }
        L_0x027e:
            if (r3 == 0) goto L_0x0099;
        L_0x0280:
            r3.disconnect();
            goto L_0x0099;
        L_0x0285:
            r21 = move-exception;
        L_0x0286:
            if (r16 == 0) goto L_0x028b;
        L_0x0288:
            r16.close();	 Catch:{ IOException -> 0x0296 }
        L_0x028b:
            if (r15 == 0) goto L_0x0290;
        L_0x028d:
            r15.close();	 Catch:{ IOException -> 0x0296 }
        L_0x0290:
            if (r3 == 0) goto L_0x0295;
        L_0x0292:
            r3.disconnect();
        L_0x0295:
            throw r21;
        L_0x0296:
            r22 = move-exception;
            goto L_0x0290;
        L_0x0298:
            r21 = move-exception;
            r9 = r12;
            goto L_0x0286;
        L_0x029b:
            r21 = move-exception;
            r9 = r12;
            r16 = r17;
            goto L_0x0286;
        L_0x02a0:
            r22 = move-exception;
            goto L_0x027e;
        L_0x02a2:
            r8 = move-exception;
            r9 = r12;
            goto L_0x026e;
        L_0x02a5:
            r8 = move-exception;
            r9 = r12;
            r16 = r17;
            goto L_0x026e;
        L_0x02aa:
            r22 = move-exception;
            goto L_0x0214;
        L_0x02ad:
            r8 = move-exception;
            goto L_0x0204;
        L_0x02b0:
            r8 = move-exception;
            r9 = r12;
            goto L_0x0204;
        L_0x02b4:
            r21 = move-exception;
            goto L_0x0250;
        L_0x02b6:
            r22 = move-exception;
            goto L_0x023c;
        L_0x02b8:
            r22 = move-exception;
            goto L_0x01ec;
        L_0x02bb:
            r22 = move-exception;
            goto L_0x0192;
        L_0x02be:
            r22 = move-exception;
            goto L_0x012f;
        L_0x02c1:
            r22 = move-exception;
            goto L_0x0107;
        L_0x02c4:
            r22 = move-exception;
            goto L_0x00ce;
        L_0x02c7:
            r22 = move-exception;
            goto L_0x0094;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.upsight.mediation.vast.model.VASTModel.DownloadTask.doInBackground(java.lang.String[]):java.lang.Integer");
        }

        protected void onPostExecute(Integer error) {
            if (error.intValue() != 0) {
                this.mVastPlayer.listener.vastError(error.intValue());
                return;
            }
            FuseLog.m608v(VASTModel.TAG, "on execute complete");
            this.mVastPlayer.setLoaded(true);
        }

        protected void onCancelled() {
            this.mVastPlayer.listener.vastError(VASTPlayer.ERROR_VIDEO_TIMEOUT);
        }
    }

    static {
        TAG = "VASTModel";
    }

    public VASTModel(Document vasts) {
        this.mediaFileLocation = null;
        this.mediaFileDeliveryType = null;
        this.vastsDocument = vasts;
    }

    public boolean evaluateAdSystem() {
        try {
            Node node = (Node) XPathFactory.newInstance().newXPath().evaluate(adSystemXPATH, this.vastsDocument, XPathConstants.NODE);
            return true;
        } catch (XPathExpressionException e) {
            return false;
        }
    }

    public boolean evaluateAdTitle() {
        try {
            Node node = (Node) XPathFactory.newInstance().newXPath().evaluate(adTitleXPATH, this.vastsDocument, XPathConstants.NODE);
            return true;
        } catch (XPathExpressionException e) {
            return false;
        }
    }

    public String getVastVersion() {
        try {
            return ((Node) XPathFactory.newInstance().newXPath().evaluate(vastXPATH, this.vastsDocument, XPathConstants.NODE)).getAttributes().getNamedItem("version").getNodeValue().toString();
        } catch (Exception e) {
            return null;
        }
    }

    public HashMap<TRACKING_EVENTS_TYPE, List<VASTTracking>> getTrackingEvents() {
        HashMap<TRACKING_EVENTS_TYPE, List<VASTTracking>> trackings = new HashMap();
        XPath xpath = XPathFactory.newInstance().newXPath();
        NodeList nodes = (NodeList) xpath.evaluate(combinedTrackingXPATH, this.vastsDocument, XPathConstants.NODESET);
        String progressOffset = BuildConfig.FLAVOR;
        if (nodes == null) {
            return trackings;
        }
        int i = 0;
        while (i < nodes.getLength()) {
            Node node = nodes.item(i);
            NamedNodeMap attributes = node.getAttributes();
            String eventName = attributes.getNamedItem(SendEvent.EVENT).getNodeValue();
            if (eventName.equals(NotificationCompatApi21.CATEGORY_PROGRESS)) {
                try {
                    progressOffset = attributes.getNamedItem("offset").getNodeValue();
                } catch (NullPointerException e) {
                    progressOffset = null;
                }
            }
            try {
                TRACKING_EVENTS_TYPE key = TRACKING_EVENTS_TYPE.valueOf(eventName);
            } catch (IllegalArgumentException e2) {
            }
            try {
                String trackingURL = XmlTools.getElementValue(node);
                VASTTracking vastTracking = new VASTTracking();
                vastTracking.setEvent(key);
                vastTracking.setValue(trackingURL);
                if (key.equals(TRACKING_EVENTS_TYPE.progress) && progressOffset != null) {
                    vastTracking.setOffset(progressOffset);
                }
                if (trackings.containsKey(key)) {
                    ((List) trackings.get(key)).add(vastTracking);
                } else {
                    List<VASTTracking> tracking = new ArrayList();
                    tracking.add(vastTracking);
                    trackings.put(key, tracking);
                }
                i++;
            } catch (Exception e3) {
                return null;
            }
        }
        return trackings;
    }

    public List<VASTMediaFile> getMediaFiles() {
        ArrayList<VASTMediaFile> mediaFiles = new ArrayList();
        try {
            NodeList nodes = (NodeList) XPathFactory.newInstance().newXPath().evaluate(mediaFileXPATH, this.vastsDocument, XPathConstants.NODESET);
            if (nodes == null) {
                return mediaFiles;
            }
            for (int i = 0; i < nodes.getLength(); i++) {
                BigInteger bigInteger;
                VASTMediaFile mediaFile = new VASTMediaFile();
                Node node = nodes.item(i);
                NamedNodeMap attributes = node.getAttributes();
                Node attributeNode = attributes.getNamedItem("apiFramework");
                mediaFile.setApiFramework(attributeNode == null ? null : attributeNode.getNodeValue());
                attributeNode = attributes.getNamedItem("bitrate");
                if (attributeNode == null) {
                    bigInteger = null;
                } else {
                    bigInteger = new BigInteger(attributeNode.getNodeValue());
                }
                mediaFile.setBitrate(bigInteger);
                attributeNode = attributes.getNamedItem("delivery");
                mediaFile.setDelivery(attributeNode == null ? null : attributeNode.getNodeValue());
                attributeNode = attributes.getNamedItem("height");
                mediaFile.setHeight(attributeNode == null ? null : new BigInteger(attributeNode.getNodeValue()));
                attributeNode = attributes.getNamedItem(TriggerIfContentAvailable.ID);
                mediaFile.setId(attributeNode == null ? null : attributeNode.getNodeValue());
                attributeNode = attributes.getNamedItem("maintainAspectRatio");
                mediaFile.setMaintainAspectRatio(attributeNode == null ? null : Boolean.valueOf(attributeNode.getNodeValue()));
                attributeNode = attributes.getNamedItem("scalable");
                mediaFile.setScalable(attributeNode == null ? null : Boolean.valueOf(attributeNode.getNodeValue()));
                attributeNode = attributes.getNamedItem(Keys.TYPE);
                mediaFile.setType(attributeNode == null ? null : attributeNode.getNodeValue());
                attributeNode = attributes.getNamedItem("width");
                mediaFile.setWidth(attributeNode == null ? null : new BigInteger(attributeNode.getNodeValue()));
                mediaFile.setValue(XmlTools.getElementValue(node));
                mediaFiles.add(mediaFile);
            }
            return mediaFiles;
        } catch (Exception e) {
            return null;
        }
    }

    public int cache(Context c, VASTPlayer vastPlayer, int downloadTimeout) {
        ((Activity) c).runOnUiThread(new C12461(vastPlayer, c, downloadTimeout));
        return 0;
    }

    public String getDuration() {
        FuseLog.m604d(TAG, "getDuration");
        String duration = null;
        try {
            NodeList nodes = (NodeList) XPathFactory.newInstance().newXPath().evaluate(durationXPATH, this.vastsDocument, XPathConstants.NODESET);
            if (nodes != null) {
                for (int i = 0; i < nodes.getLength(); i++) {
                    duration = XmlTools.getElementValue(nodes.item(i));
                }
            }
            return duration;
        } catch (Exception e) {
            return null;
        }
    }

    public String getSkipOffset() {
        try {
            return ((Node) XPathFactory.newInstance().newXPath().evaluate(skipOffsetXPATH, this.vastsDocument, XPathConstants.NODE)).getAttributes().getNamedItem("skipoffset").getNodeValue().toString();
        } catch (Exception e) {
            return null;
        }
    }

    public VideoClicks getVideoClicks() {
        VideoClicks videoClicks = new VideoClicks();
        try {
            NodeList nodes = (NodeList) XPathFactory.newInstance().newXPath().evaluate(videoClicksXPATH, this.vastsDocument, XPathConstants.NODESET);
            if (nodes == null) {
                return videoClicks;
            }
            for (int i = 0; i < nodes.getLength(); i++) {
                NodeList childNodes = nodes.item(i).getChildNodes();
                for (int childIndex = 0; childIndex < childNodes.getLength(); childIndex++) {
                    Node child = childNodes.item(childIndex);
                    String nodeName = child.getNodeName();
                    if (nodeName.equalsIgnoreCase("ClickTracking")) {
                        videoClicks.getClickTracking().add(XmlTools.getElementValue(child));
                    } else if (nodeName.equalsIgnoreCase("ClickThrough")) {
                        videoClicks.setClickThrough(XmlTools.getElementValue(child));
                    } else if (nodeName.equalsIgnoreCase("CustomClick")) {
                        videoClicks.getCustomClick().add(XmlTools.getElementValue(child));
                    }
                }
            }
            return videoClicks;
        } catch (Exception e) {
            return null;
        }
    }

    public List<String> getImpressions() {
        return getListFromXPath(impressionXPATH);
    }

    public List<String> getErrorUrl() {
        return getListFromXPath(errorUrlXPATH);
    }

    private List<String> getListFromXPath(String xPath) {
        ArrayList<String> list = new ArrayList();
        try {
            NodeList nodes = (NodeList) XPathFactory.newInstance().newXPath().evaluate(xPath, this.vastsDocument, XPathConstants.NODESET);
            if (nodes == null) {
                return list;
            }
            for (int i = 0; i < nodes.getLength(); i++) {
                String value = XmlTools.getElementValue(nodes.item(i));
                if (value != null || !value.equals(BuildConfig.FLAVOR)) {
                    list.add(value);
                }
            }
            return list;
        } catch (Exception e) {
            return null;
        }
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(XmlTools.xmlDocumentToString(this.vastsDocument));
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        this.vastsDocument = XmlTools.stringToDocument((String) ois.readObject());
    }

    public void setPickedMediaFileLocation(String mediaFileLocation) {
        this.mediaFileLocation = mediaFileLocation;
    }

    public String getPickedMediaFileLocation() {
        return this.mediaFileLocation;
    }

    public void setPickedMediaFileDeliveryType(String deliveryType) {
        this.mediaFileDeliveryType = deliveryType;
    }

    public String getPickedMediaFileDeliveryType() {
        return this.mediaFileDeliveryType;
    }
}
