package com.google.android.gms.internal;

import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.mediation.MediationBannerAdapter;
import com.google.ads.mediation.MediationBannerListener;
import com.google.ads.mediation.MediationInterstitialAdapter;
import com.google.ads.mediation.MediationInterstitialListener;
import com.google.ads.mediation.MediationServerParameters;
import com.google.ads.mediation.NetworkExtras;
import com.google.android.gms.ads.internal.client.zzl;
import com.google.android.gms.ads.internal.util.client.zza;
import com.google.android.gms.ads.internal.util.client.zzb;

@zzgr
public final class zzez<NETWORK_EXTRAS extends NetworkExtras, SERVER_PARAMETERS extends MediationServerParameters> implements MediationBannerListener, MediationInterstitialListener {
    private final zzeo zzzL;

    /* renamed from: com.google.android.gms.internal.zzez.10 */
    class AnonymousClass10 implements Runnable {
        final /* synthetic */ zzez zzzR;
        final /* synthetic */ ErrorCode zzzS;

        AnonymousClass10(zzez com_google_android_gms_internal_zzez, ErrorCode errorCode) {
            this.zzzR = com_google_android_gms_internal_zzez;
            this.zzzS = errorCode;
        }

        public void run() {
            try {
                this.zzzR.zzzL.onAdFailedToLoad(zzfa.zza(this.zzzS));
            } catch (Throwable e) {
                zzb.zzd("Could not call onAdFailedToLoad.", e);
            }
        }
    }

    /* renamed from: com.google.android.gms.internal.zzez.1 */
    class C06881 implements Runnable {
        final /* synthetic */ zzez zzzR;

        C06881(zzez com_google_android_gms_internal_zzez) {
            this.zzzR = com_google_android_gms_internal_zzez;
        }

        public void run() {
            try {
                this.zzzR.zzzL.onAdClicked();
            } catch (Throwable e) {
                zzb.zzd("Could not call onAdClicked.", e);
            }
        }
    }

    /* renamed from: com.google.android.gms.internal.zzez.2 */
    class C06892 implements Runnable {
        final /* synthetic */ zzez zzzR;

        C06892(zzez com_google_android_gms_internal_zzez) {
            this.zzzR = com_google_android_gms_internal_zzez;
        }

        public void run() {
            try {
                this.zzzR.zzzL.onAdOpened();
            } catch (Throwable e) {
                zzb.zzd("Could not call onAdOpened.", e);
            }
        }
    }

    /* renamed from: com.google.android.gms.internal.zzez.3 */
    class C06903 implements Runnable {
        final /* synthetic */ zzez zzzR;

        C06903(zzez com_google_android_gms_internal_zzez) {
            this.zzzR = com_google_android_gms_internal_zzez;
        }

        public void run() {
            try {
                this.zzzR.zzzL.onAdLoaded();
            } catch (Throwable e) {
                zzb.zzd("Could not call onAdLoaded.", e);
            }
        }
    }

    /* renamed from: com.google.android.gms.internal.zzez.4 */
    class C06914 implements Runnable {
        final /* synthetic */ zzez zzzR;

        C06914(zzez com_google_android_gms_internal_zzez) {
            this.zzzR = com_google_android_gms_internal_zzez;
        }

        public void run() {
            try {
                this.zzzR.zzzL.onAdClosed();
            } catch (Throwable e) {
                zzb.zzd("Could not call onAdClosed.", e);
            }
        }
    }

    /* renamed from: com.google.android.gms.internal.zzez.5 */
    class C06925 implements Runnable {
        final /* synthetic */ zzez zzzR;
        final /* synthetic */ ErrorCode zzzS;

        C06925(zzez com_google_android_gms_internal_zzez, ErrorCode errorCode) {
            this.zzzR = com_google_android_gms_internal_zzez;
            this.zzzS = errorCode;
        }

        public void run() {
            try {
                this.zzzR.zzzL.onAdFailedToLoad(zzfa.zza(this.zzzS));
            } catch (Throwable e) {
                zzb.zzd("Could not call onAdFailedToLoad.", e);
            }
        }
    }

    /* renamed from: com.google.android.gms.internal.zzez.6 */
    class C06936 implements Runnable {
        final /* synthetic */ zzez zzzR;

        C06936(zzez com_google_android_gms_internal_zzez) {
            this.zzzR = com_google_android_gms_internal_zzez;
        }

        public void run() {
            try {
                this.zzzR.zzzL.onAdLeftApplication();
            } catch (Throwable e) {
                zzb.zzd("Could not call onAdLeftApplication.", e);
            }
        }
    }

    /* renamed from: com.google.android.gms.internal.zzez.7 */
    class C06947 implements Runnable {
        final /* synthetic */ zzez zzzR;

        C06947(zzez com_google_android_gms_internal_zzez) {
            this.zzzR = com_google_android_gms_internal_zzez;
        }

        public void run() {
            try {
                this.zzzR.zzzL.onAdOpened();
            } catch (Throwable e) {
                zzb.zzd("Could not call onAdOpened.", e);
            }
        }
    }

    /* renamed from: com.google.android.gms.internal.zzez.8 */
    class C06958 implements Runnable {
        final /* synthetic */ zzez zzzR;

        C06958(zzez com_google_android_gms_internal_zzez) {
            this.zzzR = com_google_android_gms_internal_zzez;
        }

        public void run() {
            try {
                this.zzzR.zzzL.onAdLoaded();
            } catch (Throwable e) {
                zzb.zzd("Could not call onAdLoaded.", e);
            }
        }
    }

    /* renamed from: com.google.android.gms.internal.zzez.9 */
    class C06969 implements Runnable {
        final /* synthetic */ zzez zzzR;

        C06969(zzez com_google_android_gms_internal_zzez) {
            this.zzzR = com_google_android_gms_internal_zzez;
        }

        public void run() {
            try {
                this.zzzR.zzzL.onAdClosed();
            } catch (Throwable e) {
                zzb.zzd("Could not call onAdClosed.", e);
            }
        }
    }

    public zzez(zzeo com_google_android_gms_internal_zzeo) {
        this.zzzL = com_google_android_gms_internal_zzeo;
    }

    public void onClick(MediationBannerAdapter<?, ?> mediationBannerAdapter) {
        zzb.zzaF("Adapter called onClick.");
        if (zzl.zzcF().zzgT()) {
            try {
                this.zzzL.onAdClicked();
                return;
            } catch (Throwable e) {
                zzb.zzd("Could not call onAdClicked.", e);
                return;
            }
        }
        zzb.zzaH("onClick must be called on the main UI thread.");
        zza.zzJt.post(new C06881(this));
    }

    public void onDismissScreen(MediationBannerAdapter<?, ?> mediationBannerAdapter) {
        zzb.zzaF("Adapter called onDismissScreen.");
        if (zzl.zzcF().zzgT()) {
            try {
                this.zzzL.onAdClosed();
                return;
            } catch (Throwable e) {
                zzb.zzd("Could not call onAdClosed.", e);
                return;
            }
        }
        zzb.zzaH("onDismissScreen must be called on the main UI thread.");
        zza.zzJt.post(new C06914(this));
    }

    public void onDismissScreen(MediationInterstitialAdapter<?, ?> mediationInterstitialAdapter) {
        zzb.zzaF("Adapter called onDismissScreen.");
        if (zzl.zzcF().zzgT()) {
            try {
                this.zzzL.onAdClosed();
                return;
            } catch (Throwable e) {
                zzb.zzd("Could not call onAdClosed.", e);
                return;
            }
        }
        zzb.zzaH("onDismissScreen must be called on the main UI thread.");
        zza.zzJt.post(new C06969(this));
    }

    public void onFailedToReceiveAd(MediationBannerAdapter<?, ?> mediationBannerAdapter, ErrorCode errorCode) {
        zzb.zzaF("Adapter called onFailedToReceiveAd with error. " + errorCode);
        if (zzl.zzcF().zzgT()) {
            try {
                this.zzzL.onAdFailedToLoad(zzfa.zza(errorCode));
                return;
            } catch (Throwable e) {
                zzb.zzd("Could not call onAdFailedToLoad.", e);
                return;
            }
        }
        zzb.zzaH("onFailedToReceiveAd must be called on the main UI thread.");
        zza.zzJt.post(new C06925(this, errorCode));
    }

    public void onFailedToReceiveAd(MediationInterstitialAdapter<?, ?> mediationInterstitialAdapter, ErrorCode errorCode) {
        zzb.zzaF("Adapter called onFailedToReceiveAd with error " + errorCode + ".");
        if (zzl.zzcF().zzgT()) {
            try {
                this.zzzL.onAdFailedToLoad(zzfa.zza(errorCode));
                return;
            } catch (Throwable e) {
                zzb.zzd("Could not call onAdFailedToLoad.", e);
                return;
            }
        }
        zzb.zzaH("onFailedToReceiveAd must be called on the main UI thread.");
        zza.zzJt.post(new AnonymousClass10(this, errorCode));
    }

    public void onLeaveApplication(MediationBannerAdapter<?, ?> mediationBannerAdapter) {
        zzb.zzaF("Adapter called onLeaveApplication.");
        if (zzl.zzcF().zzgT()) {
            try {
                this.zzzL.onAdLeftApplication();
                return;
            } catch (Throwable e) {
                zzb.zzd("Could not call onAdLeftApplication.", e);
                return;
            }
        }
        zzb.zzaH("onLeaveApplication must be called on the main UI thread.");
        zza.zzJt.post(new C06936(this));
    }

    public void onLeaveApplication(MediationInterstitialAdapter<?, ?> mediationInterstitialAdapter) {
        zzb.zzaF("Adapter called onLeaveApplication.");
        if (zzl.zzcF().zzgT()) {
            try {
                this.zzzL.onAdLeftApplication();
                return;
            } catch (Throwable e) {
                zzb.zzd("Could not call onAdLeftApplication.", e);
                return;
            }
        }
        zzb.zzaH("onLeaveApplication must be called on the main UI thread.");
        zza.zzJt.post(new Runnable() {
            final /* synthetic */ zzez zzzR;

            {
                this.zzzR = r1;
            }

            public void run() {
                try {
                    this.zzzR.zzzL.onAdLeftApplication();
                } catch (Throwable e) {
                    zzb.zzd("Could not call onAdLeftApplication.", e);
                }
            }
        });
    }

    public void onPresentScreen(MediationBannerAdapter<?, ?> mediationBannerAdapter) {
        zzb.zzaF("Adapter called onPresentScreen.");
        if (zzl.zzcF().zzgT()) {
            try {
                this.zzzL.onAdOpened();
                return;
            } catch (Throwable e) {
                zzb.zzd("Could not call onAdOpened.", e);
                return;
            }
        }
        zzb.zzaH("onPresentScreen must be called on the main UI thread.");
        zza.zzJt.post(new C06947(this));
    }

    public void onPresentScreen(MediationInterstitialAdapter<?, ?> mediationInterstitialAdapter) {
        zzb.zzaF("Adapter called onPresentScreen.");
        if (zzl.zzcF().zzgT()) {
            try {
                this.zzzL.onAdOpened();
                return;
            } catch (Throwable e) {
                zzb.zzd("Could not call onAdOpened.", e);
                return;
            }
        }
        zzb.zzaH("onPresentScreen must be called on the main UI thread.");
        zza.zzJt.post(new C06892(this));
    }

    public void onReceivedAd(MediationBannerAdapter<?, ?> mediationBannerAdapter) {
        zzb.zzaF("Adapter called onReceivedAd.");
        if (zzl.zzcF().zzgT()) {
            try {
                this.zzzL.onAdLoaded();
                return;
            } catch (Throwable e) {
                zzb.zzd("Could not call onAdLoaded.", e);
                return;
            }
        }
        zzb.zzaH("onReceivedAd must be called on the main UI thread.");
        zza.zzJt.post(new C06958(this));
    }

    public void onReceivedAd(MediationInterstitialAdapter<?, ?> mediationInterstitialAdapter) {
        zzb.zzaF("Adapter called onReceivedAd.");
        if (zzl.zzcF().zzgT()) {
            try {
                this.zzzL.onAdLoaded();
                return;
            } catch (Throwable e) {
                zzb.zzd("Could not call onAdLoaded.", e);
                return;
            }
        }
        zzb.zzaH("onReceivedAd must be called on the main UI thread.");
        zza.zzJt.post(new C06903(this));
    }
}
