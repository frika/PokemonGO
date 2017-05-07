package com.crittercism.internal;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.ConnectivityManager.NetworkCallback;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.NetworkRequest.Builder;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.v4.os.EnvironmentCompat;
import com.crittercism.internal.at.C0201b;
import com.crittercism.internal.at.C0202c;
import com.google.android.gms.location.places.Place;
import com.mopub.volley.Request.Method;
import com.nianticlabs.pokemongoplus.ble.BluetoothGattSupport;
import com.upsight.mediation.mraid.properties.MRAIDResizeProperties;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import org.json.JSONObject;
import spacemadness.com.lunarconsole.C1628R;

public final class bp {
    av<at> f349a;
    ap f350b;
    private C0223b f351c;
    private ConnectivityManager f352d;
    private ExecutorService f353e;
    private C0222a f354f;

    /* renamed from: com.crittercism.internal.bp.1 */
    class C02211 implements Runnable {
        final /* synthetic */ at f338a;
        final /* synthetic */ bp f339b;

        C02211(bp bpVar, at atVar) {
            this.f339b = bpVar;
            this.f338a = atVar;
        }

        public final void run() {
            if (((Boolean) this.f339b.f350b.m128a(ap.f112E)).booleanValue()) {
                this.f339b.f349a.m196a(this.f338a);
            }
        }
    }

    @TargetApi(21)
    /* renamed from: com.crittercism.internal.bp.a */
    class C0222a extends NetworkCallback {
        final /* synthetic */ bp f340a;

        private C0222a(bp bpVar) {
            this.f340a = bpVar;
        }

        public final void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
        }

        public final void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        }

        public final void onLost(Network network) {
            bp.m304a(this.f340a);
        }

        public final void onLosing(Network network, int maxMsToLive) {
        }

        public final void onAvailable(Network network) {
            bp.m304a(this.f340a);
        }
    }

    /* renamed from: com.crittercism.internal.bp.b */
    enum C0223b {
        DISCONNECTED("disconnected"),
        TWO_G("2G"),
        THREE_G("3G"),
        LTE("LTE"),
        WIFI("wifi"),
        UNKNOWN(EnvironmentCompat.MEDIA_UNKNOWN);
        
        private String f348g;

        private C0223b(String str) {
            this.f348g = str;
        }

        public static C0223b m303a(NetworkInfo networkInfo) {
            if (networkInfo == null || !networkInfo.isConnected()) {
                return DISCONNECTED;
            }
            int type = networkInfo.getType();
            if (type == 0) {
                switch (networkInfo.getSubtype()) {
                    case C1628R.styleable.RecyclerView_layoutManager /*1*/:
                    case C1628R.styleable.RecyclerView_spanCount /*2*/:
                    case C1628R.styleable.RecyclerView_stackFromEnd /*4*/:
                    case Method.PATCH /*7*/:
                    case Place.TYPE_BICYCLE_STORE /*11*/:
                        return TWO_G;
                    case C1628R.styleable.RecyclerView_reverseLayout /*3*/:
                    case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_CENTER /*5*/:
                    case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_RIGHT /*6*/:
                    case BluetoothGattSupport.GATT_INSUF_AUTHENTICATION /*8*/:
                    case Place.TYPE_BAR /*9*/:
                    case Place.TYPE_BEAUTY_SALON /*10*/:
                    case Place.TYPE_BOOK_STORE /*12*/:
                    case Place.TYPE_BUS_STATION /*14*/:
                    case Place.TYPE_CAFE /*15*/:
                        return THREE_G;
                    case Place.TYPE_BOWLING_ALLEY /*13*/:
                        return LTE;
                }
            } else if (type == 1) {
                return WIFI;
            }
            return UNKNOWN;
        }

        public final String toString() {
            return this.f348g;
        }
    }

    @TargetApi(21)
    public bp(@NonNull Context context, @NonNull ExecutorService executorService, @NonNull av<at> avVar, @NonNull ap apVar) {
        this.f351c = C0223b.UNKNOWN;
        this.f353e = executorService;
        this.f349a = avVar;
        this.f350b = apVar;
        if (ao.m113a(context, "android.permission.ACCESS_NETWORK_STATE") && VERSION.SDK_INT >= 21) {
            this.f352d = (ConnectivityManager) context.getSystemService("connectivity");
            if (this.f352d != null) {
                NetworkRequest build = new Builder().addCapability(12).build();
                this.f354f = new C0222a();
                this.f352d.registerNetworkCallback(build, this.f354f);
            }
        }
    }

    static /* synthetic */ void m304a(bp bpVar) {
        C0223b a = C0223b.m303a(bpVar.f352d.getActiveNetworkInfo());
        if (bpVar.f351c != a) {
            at atVar = null;
            if (!(bpVar.f351c == C0223b.UNKNOWN || a == C0223b.UNKNOWN)) {
                if (bpVar.f351c == C0223b.DISCONNECTED) {
                    atVar = at.m158a(C0202c.f192c, a.toString());
                } else if (a == C0223b.DISCONNECTED) {
                    atVar = at.m158a(C0202c.f193d, bpVar.f351c.toString());
                } else {
                    String c0223b = bpVar.f351c.toString();
                    String c0223b2 = a.toString();
                    Map hashMap = new HashMap();
                    hashMap.put("change", Integer.valueOf(C0202c.f194e - 1));
                    hashMap.put("oldType", c0223b);
                    hashMap.put("newType", c0223b2);
                    atVar = new at(C0201b.f185e, new JSONObject(hashMap));
                }
            }
            bpVar.f351c = a;
            if (atVar != null) {
                bpVar.f353e.submit(new C02211(bpVar, atVar));
            }
        }
    }
}
