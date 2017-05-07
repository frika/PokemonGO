package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.exoplayer.chunk.FormatEvaluator.AdaptiveEvaluator;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.common.internal.zzx;
import java.util.Map;

@zzgr
public class zzdz {
    private final Context mContext;
    private final VersionInfoParcel zzpb;
    private final Object zzpd;
    private final String zzyo;
    private zzb<zzbb> zzyp;
    private zzb<zzbb> zzyq;
    private zze zzyr;
    private int zzys;

    public interface zzb<T> {
        void zzc(T t);
    }

    /* renamed from: com.google.android.gms.internal.zzdz.1 */
    class C06751 implements Runnable {
        final /* synthetic */ zze zzyt;
        final /* synthetic */ zzdz zzyu;

        /* renamed from: com.google.android.gms.internal.zzdz.1.1 */
        class C06701 implements com.google.android.gms.internal.zzbb.zza {
            final /* synthetic */ zzbb zzyv;
            final /* synthetic */ C06751 zzyw;

            /* renamed from: com.google.android.gms.internal.zzdz.1.1.1 */
            class C06691 implements Runnable {
                final /* synthetic */ C06701 zzyx;

                /* renamed from: com.google.android.gms.internal.zzdz.1.1.1.1 */
                class C06681 implements Runnable {
                    final /* synthetic */ C06691 zzyy;

                    C06681(C06691 c06691) {
                        this.zzyy = c06691;
                    }

                    public void run() {
                        this.zzyy.zzyx.zzyv.destroy();
                    }
                }

                C06691(C06701 c06701) {
                    this.zzyx = c06701;
                }

                public void run() {
                    synchronized (this.zzyx.zzyw.zzyu.zzpd) {
                        if (this.zzyx.zzyw.zzyt.getStatus() == -1 || this.zzyx.zzyw.zzyt.getStatus() == 1) {
                            return;
                        }
                        this.zzyx.zzyw.zzyt.reject();
                        zzid.runOnUiThread(new C06681(this));
                        com.google.android.gms.ads.internal.util.client.zzb.m484v("Could not receive loaded message in a timely manner. Rejecting.");
                    }
                }
            }

            C06701(C06751 c06751, zzbb com_google_android_gms_internal_zzbb) {
                this.zzyw = c06751;
                this.zzyv = com_google_android_gms_internal_zzbb;
            }

            public void zzcj() {
                zzid.zzIE.postDelayed(new C06691(this), (long) zza.zzyD);
            }
        }

        /* renamed from: com.google.android.gms.internal.zzdz.1.2 */
        class C06712 implements zzdk {
            final /* synthetic */ zzbb zzyv;
            final /* synthetic */ C06751 zzyw;

            C06712(C06751 c06751, zzbb com_google_android_gms_internal_zzbb) {
                this.zzyw = c06751;
                this.zzyv = com_google_android_gms_internal_zzbb;
            }

            public void zza(zziz com_google_android_gms_internal_zziz, Map<String, String> map) {
                synchronized (this.zzyw.zzyu.zzpd) {
                    if (this.zzyw.zzyt.getStatus() == -1 || this.zzyw.zzyt.getStatus() == 1) {
                        return;
                    }
                    this.zzyw.zzyu.zzys = 0;
                    this.zzyw.zzyu.zzyp.zzc(this.zzyv);
                    this.zzyw.zzyt.zzg(this.zzyv);
                    this.zzyw.zzyu.zzyr = this.zzyw.zzyt;
                    com.google.android.gms.ads.internal.util.client.zzb.m484v("Successfully loaded JS Engine.");
                }
            }
        }

        /* renamed from: com.google.android.gms.internal.zzdz.1.3 */
        class C06723 implements zzdk {
            final /* synthetic */ zzbb zzyv;
            final /* synthetic */ C06751 zzyw;
            final /* synthetic */ zzil zzyz;

            C06723(C06751 c06751, zzbb com_google_android_gms_internal_zzbb, zzil com_google_android_gms_internal_zzil) {
                this.zzyw = c06751;
                this.zzyv = com_google_android_gms_internal_zzbb;
                this.zzyz = com_google_android_gms_internal_zzil;
            }

            public void zza(zziz com_google_android_gms_internal_zziz, Map<String, String> map) {
                synchronized (this.zzyw.zzyu.zzpd) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzaG("JS Engine is requesting an update");
                    if (this.zzyw.zzyu.zzys == 0) {
                        com.google.android.gms.ads.internal.util.client.zzb.zzaG("Starting reload.");
                        this.zzyw.zzyu.zzys = 2;
                        this.zzyw.zzyu.zzdN();
                    }
                    this.zzyv.zzb("/requestReload", (zzdk) this.zzyz.get());
                }
            }
        }

        /* renamed from: com.google.android.gms.internal.zzdz.1.4 */
        class C06744 implements Runnable {
            final /* synthetic */ zzbb zzyv;
            final /* synthetic */ C06751 zzyw;

            /* renamed from: com.google.android.gms.internal.zzdz.1.4.1 */
            class C06731 implements Runnable {
                final /* synthetic */ C06744 zzyA;

                C06731(C06744 c06744) {
                    this.zzyA = c06744;
                }

                public void run() {
                    this.zzyA.zzyv.destroy();
                }
            }

            C06744(C06751 c06751, zzbb com_google_android_gms_internal_zzbb) {
                this.zzyw = c06751;
                this.zzyv = com_google_android_gms_internal_zzbb;
            }

            public void run() {
                synchronized (this.zzyw.zzyu.zzpd) {
                    if (this.zzyw.zzyt.getStatus() == -1 || this.zzyw.zzyt.getStatus() == 1) {
                        return;
                    }
                    this.zzyw.zzyt.reject();
                    zzid.runOnUiThread(new C06731(this));
                    com.google.android.gms.ads.internal.util.client.zzb.m484v("Could not receive loaded message in a timely manner. Rejecting.");
                }
            }
        }

        C06751(zzdz com_google_android_gms_internal_zzdz, zze com_google_android_gms_internal_zzdz_zze) {
            this.zzyu = com_google_android_gms_internal_zzdz;
            this.zzyt = com_google_android_gms_internal_zzdz_zze;
        }

        public void run() {
            zzbb zza = this.zzyu.zza(this.zzyu.mContext, this.zzyu.zzpb);
            zza.zza(new C06701(this, zza));
            zza.zza("/jsLoaded", new C06712(this, zza));
            zzil com_google_android_gms_internal_zzil = new zzil();
            zzdk c06723 = new C06723(this, zza, com_google_android_gms_internal_zzil);
            com_google_android_gms_internal_zzil.set(c06723);
            zza.zza("/requestReload", c06723);
            if (this.zzyu.zzyo.endsWith(".js")) {
                zza.zzs(this.zzyu.zzyo);
            } else if (this.zzyu.zzyo.startsWith("<html>")) {
                zza.zzu(this.zzyu.zzyo);
            } else {
                zza.zzt(this.zzyu.zzyo);
            }
            zzid.zzIE.postDelayed(new C06744(this, zza), (long) zza.zzyC);
        }
    }

    /* renamed from: com.google.android.gms.internal.zzdz.2 */
    class C06762 implements com.google.android.gms.internal.zzis.zzc<zzbb> {
        final /* synthetic */ zze zzyB;
        final /* synthetic */ zzdz zzyu;

        C06762(zzdz com_google_android_gms_internal_zzdz, zze com_google_android_gms_internal_zzdz_zze) {
            this.zzyu = com_google_android_gms_internal_zzdz;
            this.zzyB = com_google_android_gms_internal_zzdz_zze;
        }

        public void zza(zzbb com_google_android_gms_internal_zzbb) {
            synchronized (this.zzyu.zzpd) {
                this.zzyu.zzys = 0;
                if (!(this.zzyu.zzyr == null || this.zzyB == this.zzyu.zzyr)) {
                    com.google.android.gms.ads.internal.util.client.zzb.m484v("New JS engine is loaded, marking previous one as destroyable.");
                    this.zzyu.zzyr.zzdR();
                }
                this.zzyu.zzyr = this.zzyB;
            }
        }

        public /* synthetic */ void zzc(Object obj) {
            zza((zzbb) obj);
        }
    }

    /* renamed from: com.google.android.gms.internal.zzdz.3 */
    class C06773 implements com.google.android.gms.internal.zzis.zza {
        final /* synthetic */ zze zzyB;
        final /* synthetic */ zzdz zzyu;

        C06773(zzdz com_google_android_gms_internal_zzdz, zze com_google_android_gms_internal_zzdz_zze) {
            this.zzyu = com_google_android_gms_internal_zzdz;
            this.zzyB = com_google_android_gms_internal_zzdz_zze;
        }

        public void run() {
            synchronized (this.zzyu.zzpd) {
                this.zzyu.zzys = 1;
                com.google.android.gms.ads.internal.util.client.zzb.m484v("Failed loading new engine. Marking new engine destroyable.");
                this.zzyB.zzdR();
            }
        }
    }

    static class zza {
        static int zzyC;
        static int zzyD;

        static {
            zzyC = 60000;
            zzyD = AdaptiveEvaluator.DEFAULT_MIN_DURATION_FOR_QUALITY_INCREASE_MS;
        }
    }

    public static class zzc<T> implements zzb<T> {
        public void zzc(T t) {
        }
    }

    public static class zzd extends zzit<zzbe> {
        private final Object zzpd;
        private final zze zzyE;
        private boolean zzyF;

        /* renamed from: com.google.android.gms.internal.zzdz.zzd.1 */
        class C06781 implements com.google.android.gms.internal.zzis.zzc<zzbe> {
            final /* synthetic */ zzd zzyG;

            C06781(zzd com_google_android_gms_internal_zzdz_zzd) {
                this.zzyG = com_google_android_gms_internal_zzdz_zzd;
            }

            public void zzb(zzbe com_google_android_gms_internal_zzbe) {
                com.google.android.gms.ads.internal.util.client.zzb.m484v("Ending javascript session.");
                ((zzbf) com_google_android_gms_internal_zzbe).zzck();
            }

            public /* synthetic */ void zzc(Object obj) {
                zzb((zzbe) obj);
            }
        }

        /* renamed from: com.google.android.gms.internal.zzdz.zzd.2 */
        class C06792 implements com.google.android.gms.internal.zzis.zzc<zzbe> {
            final /* synthetic */ zzd zzyG;

            C06792(zzd com_google_android_gms_internal_zzdz_zzd) {
                this.zzyG = com_google_android_gms_internal_zzdz_zzd;
            }

            public void zzb(zzbe com_google_android_gms_internal_zzbe) {
                com.google.android.gms.ads.internal.util.client.zzb.m484v("Releasing engine reference.");
                this.zzyG.zzyE.zzdQ();
            }

            public /* synthetic */ void zzc(Object obj) {
                zzb((zzbe) obj);
            }
        }

        /* renamed from: com.google.android.gms.internal.zzdz.zzd.3 */
        class C06803 implements com.google.android.gms.internal.zzis.zza {
            final /* synthetic */ zzd zzyG;

            C06803(zzd com_google_android_gms_internal_zzdz_zzd) {
                this.zzyG = com_google_android_gms_internal_zzdz_zzd;
            }

            public void run() {
                this.zzyG.zzyE.zzdQ();
            }
        }

        public zzd(zze com_google_android_gms_internal_zzdz_zze) {
            this.zzpd = new Object();
            this.zzyE = com_google_android_gms_internal_zzdz_zze;
        }

        public void release() {
            synchronized (this.zzpd) {
                if (this.zzyF) {
                    return;
                }
                this.zzyF = true;
                zza(new C06781(this), new com.google.android.gms.internal.zzis.zzb());
                zza(new C06792(this), new C06803(this));
            }
        }
    }

    public static class zze extends zzit<zzbb> {
        private final Object zzpd;
        private boolean zzyH;
        private int zzyI;
        private zzb<zzbb> zzyq;

        /* renamed from: com.google.android.gms.internal.zzdz.zze.1 */
        class C06811 implements com.google.android.gms.internal.zzis.zzc<zzbb> {
            final /* synthetic */ zzd zzyJ;
            final /* synthetic */ zze zzyK;

            C06811(zze com_google_android_gms_internal_zzdz_zze, zzd com_google_android_gms_internal_zzdz_zzd) {
                this.zzyK = com_google_android_gms_internal_zzdz_zze;
                this.zzyJ = com_google_android_gms_internal_zzdz_zzd;
            }

            public void zza(zzbb com_google_android_gms_internal_zzbb) {
                com.google.android.gms.ads.internal.util.client.zzb.m484v("Getting a new session for JS Engine.");
                this.zzyJ.zzg(com_google_android_gms_internal_zzbb.zzci());
            }

            public /* synthetic */ void zzc(Object obj) {
                zza((zzbb) obj);
            }
        }

        /* renamed from: com.google.android.gms.internal.zzdz.zze.2 */
        class C06822 implements com.google.android.gms.internal.zzis.zza {
            final /* synthetic */ zzd zzyJ;
            final /* synthetic */ zze zzyK;

            C06822(zze com_google_android_gms_internal_zzdz_zze, zzd com_google_android_gms_internal_zzdz_zzd) {
                this.zzyK = com_google_android_gms_internal_zzdz_zze;
                this.zzyJ = com_google_android_gms_internal_zzdz_zzd;
            }

            public void run() {
                com.google.android.gms.ads.internal.util.client.zzb.m484v("Rejecting reference for JS Engine.");
                this.zzyJ.reject();
            }
        }

        /* renamed from: com.google.android.gms.internal.zzdz.zze.3 */
        class C06843 implements com.google.android.gms.internal.zzis.zzc<zzbb> {
            final /* synthetic */ zze zzyK;

            /* renamed from: com.google.android.gms.internal.zzdz.zze.3.1 */
            class C06831 implements Runnable {
                final /* synthetic */ zzbb zzrE;
                final /* synthetic */ C06843 zzyL;

                C06831(C06843 c06843, zzbb com_google_android_gms_internal_zzbb) {
                    this.zzyL = c06843;
                    this.zzrE = com_google_android_gms_internal_zzbb;
                }

                public void run() {
                    this.zzyL.zzyK.zzyq.zzc(this.zzrE);
                    this.zzrE.destroy();
                }
            }

            C06843(zze com_google_android_gms_internal_zzdz_zze) {
                this.zzyK = com_google_android_gms_internal_zzdz_zze;
            }

            public void zza(zzbb com_google_android_gms_internal_zzbb) {
                zzid.runOnUiThread(new C06831(this, com_google_android_gms_internal_zzbb));
            }

            public /* synthetic */ void zzc(Object obj) {
                zza((zzbb) obj);
            }
        }

        public zze(zzb<zzbb> com_google_android_gms_internal_zzdz_zzb_com_google_android_gms_internal_zzbb) {
            this.zzpd = new Object();
            this.zzyq = com_google_android_gms_internal_zzdz_zzb_com_google_android_gms_internal_zzbb;
            this.zzyH = false;
            this.zzyI = 0;
        }

        public zzd zzdP() {
            zzd com_google_android_gms_internal_zzdz_zzd = new zzd(this);
            synchronized (this.zzpd) {
                zza(new C06811(this, com_google_android_gms_internal_zzdz_zzd), new C06822(this, com_google_android_gms_internal_zzdz_zzd));
                zzx.zzZ(this.zzyI >= 0);
                this.zzyI++;
            }
            return com_google_android_gms_internal_zzdz_zzd;
        }

        protected void zzdQ() {
            boolean z = true;
            synchronized (this.zzpd) {
                if (this.zzyI < 1) {
                    z = false;
                }
                zzx.zzZ(z);
                com.google.android.gms.ads.internal.util.client.zzb.m484v("Releasing 1 reference for JS Engine");
                this.zzyI--;
                zzdS();
            }
        }

        public void zzdR() {
            boolean z = true;
            synchronized (this.zzpd) {
                if (this.zzyI < 0) {
                    z = false;
                }
                zzx.zzZ(z);
                com.google.android.gms.ads.internal.util.client.zzb.m484v("Releasing root reference. JS Engine will be destroyed once other references are released.");
                this.zzyH = true;
                zzdS();
            }
        }

        protected void zzdS() {
            synchronized (this.zzpd) {
                zzx.zzZ(this.zzyI >= 0);
                if (this.zzyH && this.zzyI == 0) {
                    com.google.android.gms.ads.internal.util.client.zzb.m484v("No reference is left (including root). Cleaning up engine.");
                    zza(new C06843(this), new com.google.android.gms.internal.zzis.zzb());
                } else {
                    com.google.android.gms.ads.internal.util.client.zzb.m484v("There are still references to the engine. Not destroying.");
                }
            }
        }
    }

    public zzdz(Context context, VersionInfoParcel versionInfoParcel, String str) {
        this.zzpd = new Object();
        this.zzys = 1;
        this.zzyo = str;
        this.mContext = context.getApplicationContext();
        this.zzpb = versionInfoParcel;
        this.zzyp = new zzc();
        this.zzyq = new zzc();
    }

    public zzdz(Context context, VersionInfoParcel versionInfoParcel, String str, zzb<zzbb> com_google_android_gms_internal_zzdz_zzb_com_google_android_gms_internal_zzbb, zzb<zzbb> com_google_android_gms_internal_zzdz_zzb_com_google_android_gms_internal_zzbb2) {
        this(context, versionInfoParcel, str);
        this.zzyp = com_google_android_gms_internal_zzdz_zzb_com_google_android_gms_internal_zzbb;
        this.zzyq = com_google_android_gms_internal_zzdz_zzb_com_google_android_gms_internal_zzbb2;
    }

    private zze zzdM() {
        zze com_google_android_gms_internal_zzdz_zze = new zze(this.zzyq);
        zzid.runOnUiThread(new C06751(this, com_google_android_gms_internal_zzdz_zze));
        return com_google_android_gms_internal_zzdz_zze;
    }

    protected zzbb zza(Context context, VersionInfoParcel versionInfoParcel) {
        return new zzbd(context, versionInfoParcel, null);
    }

    protected zze zzdN() {
        zze zzdM = zzdM();
        zzdM.zza(new C06762(this, zzdM), new C06773(this, zzdM));
        return zzdM;
    }

    public zzd zzdO() {
        zzd zzdP;
        synchronized (this.zzpd) {
            if (this.zzyr == null || this.zzyr.getStatus() == -1) {
                this.zzys = 2;
                this.zzyr = zzdN();
                zzdP = this.zzyr.zzdP();
            } else if (this.zzys == 0) {
                zzdP = this.zzyr.zzdP();
            } else if (this.zzys == 1) {
                this.zzys = 2;
                zzdN();
                zzdP = this.zzyr.zzdP();
            } else if (this.zzys == 2) {
                zzdP = this.zzyr.zzdP();
            } else {
                zzdP = this.zzyr.zzdP();
            }
        }
        return zzdP;
    }
}
