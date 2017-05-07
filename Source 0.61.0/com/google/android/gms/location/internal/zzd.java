package com.google.android.gms.location.internal;

import android.app.PendingIntent;
import android.location.Location;
import android.os.Looper;
import android.os.RemoteException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class zzd implements FusedLocationProviderApi {

    private static abstract class zza extends com.google.android.gms.location.LocationServices.zza<Status> {
        public zza(GoogleApiClient googleApiClient) {
            super(googleApiClient);
        }

        public /* synthetic */ Result zzb(Status status) {
            return zzd(status);
        }

        public Status zzd(Status status) {
            return status;
        }
    }

    /* renamed from: com.google.android.gms.location.internal.zzd.1 */
    class C08071 extends zza {
        final /* synthetic */ LocationRequest zzaFd;
        final /* synthetic */ LocationListener zzaFe;
        final /* synthetic */ zzd zzaFf;

        /* renamed from: com.google.android.gms.location.internal.zzd.1.1 */
        class C08061 extends com.google.android.gms.location.internal.zzg.zza {
            final /* synthetic */ C08071 zzaFg;

            C08061(C08071 c08071) {
                this.zzaFg = c08071;
            }

            public void zza(FusedLocationProviderResult fusedLocationProviderResult) {
                this.zzaFg.zzb(fusedLocationProviderResult.getStatus());
            }
        }

        C08071(zzd com_google_android_gms_location_internal_zzd, GoogleApiClient googleApiClient, LocationRequest locationRequest, LocationListener locationListener) {
            this.zzaFf = com_google_android_gms_location_internal_zzd;
            this.zzaFd = locationRequest;
            this.zzaFe = locationListener;
            super(googleApiClient);
        }

        protected void zza(zzl com_google_android_gms_location_internal_zzl) throws RemoteException {
            com_google_android_gms_location_internal_zzl.zza(this.zzaFd, this.zzaFe, null, new C08061(this));
        }
    }

    /* renamed from: com.google.android.gms.location.internal.zzd.2 */
    class C08092 extends zza {
        final /* synthetic */ zzd zzaFf;
        final /* synthetic */ LocationCallback zzaFh;

        /* renamed from: com.google.android.gms.location.internal.zzd.2.1 */
        class C08081 extends com.google.android.gms.location.internal.zzg.zza {
            final /* synthetic */ C08092 zzaFi;

            C08081(C08092 c08092) {
                this.zzaFi = c08092;
            }

            public void zza(FusedLocationProviderResult fusedLocationProviderResult) {
                this.zzaFi.zzb(fusedLocationProviderResult.getStatus());
            }
        }

        C08092(zzd com_google_android_gms_location_internal_zzd, GoogleApiClient googleApiClient, LocationCallback locationCallback) {
            this.zzaFf = com_google_android_gms_location_internal_zzd;
            this.zzaFh = locationCallback;
            super(googleApiClient);
        }

        protected void zza(zzl com_google_android_gms_location_internal_zzl) throws RemoteException {
            com_google_android_gms_location_internal_zzl.zza(this.zzaFh, new C08081(this));
        }
    }

    /* renamed from: com.google.android.gms.location.internal.zzd.3 */
    class C08103 extends zza {
        final /* synthetic */ zzd zzaFf;
        final /* synthetic */ boolean zzaFj;

        C08103(zzd com_google_android_gms_location_internal_zzd, GoogleApiClient googleApiClient, boolean z) {
            this.zzaFf = com_google_android_gms_location_internal_zzd;
            this.zzaFj = z;
            super(googleApiClient);
        }

        protected void zza(zzl com_google_android_gms_location_internal_zzl) throws RemoteException {
            com_google_android_gms_location_internal_zzl.zzah(this.zzaFj);
            zzb(Status.zzabb);
        }
    }

    /* renamed from: com.google.android.gms.location.internal.zzd.4 */
    class C08114 extends zza {
        final /* synthetic */ zzd zzaFf;
        final /* synthetic */ Location zzaFk;

        C08114(zzd com_google_android_gms_location_internal_zzd, GoogleApiClient googleApiClient, Location location) {
            this.zzaFf = com_google_android_gms_location_internal_zzd;
            this.zzaFk = location;
            super(googleApiClient);
        }

        protected void zza(zzl com_google_android_gms_location_internal_zzl) throws RemoteException {
            com_google_android_gms_location_internal_zzl.zzc(this.zzaFk);
            zzb(Status.zzabb);
        }
    }

    /* renamed from: com.google.android.gms.location.internal.zzd.5 */
    class C08135 extends zza {
        final /* synthetic */ LocationRequest zzaFd;
        final /* synthetic */ LocationListener zzaFe;
        final /* synthetic */ zzd zzaFf;
        final /* synthetic */ Looper zzaFl;

        /* renamed from: com.google.android.gms.location.internal.zzd.5.1 */
        class C08121 extends com.google.android.gms.location.internal.zzg.zza {
            final /* synthetic */ C08135 zzaFm;

            C08121(C08135 c08135) {
                this.zzaFm = c08135;
            }

            public void zza(FusedLocationProviderResult fusedLocationProviderResult) {
                this.zzaFm.zzb(fusedLocationProviderResult.getStatus());
            }
        }

        C08135(zzd com_google_android_gms_location_internal_zzd, GoogleApiClient googleApiClient, LocationRequest locationRequest, LocationListener locationListener, Looper looper) {
            this.zzaFf = com_google_android_gms_location_internal_zzd;
            this.zzaFd = locationRequest;
            this.zzaFe = locationListener;
            this.zzaFl = looper;
            super(googleApiClient);
        }

        protected void zza(zzl com_google_android_gms_location_internal_zzl) throws RemoteException {
            com_google_android_gms_location_internal_zzl.zza(this.zzaFd, this.zzaFe, this.zzaFl, new C08121(this));
        }
    }

    /* renamed from: com.google.android.gms.location.internal.zzd.6 */
    class C08156 extends zza {
        final /* synthetic */ LocationRequest zzaFd;
        final /* synthetic */ zzd zzaFf;
        final /* synthetic */ LocationCallback zzaFh;
        final /* synthetic */ Looper zzaFl;

        /* renamed from: com.google.android.gms.location.internal.zzd.6.1 */
        class C08141 extends com.google.android.gms.location.internal.zzg.zza {
            final /* synthetic */ C08156 zzaFn;

            C08141(C08156 c08156) {
                this.zzaFn = c08156;
            }

            public void zza(FusedLocationProviderResult fusedLocationProviderResult) {
                this.zzaFn.zzb(fusedLocationProviderResult.getStatus());
            }
        }

        C08156(zzd com_google_android_gms_location_internal_zzd, GoogleApiClient googleApiClient, LocationRequest locationRequest, LocationCallback locationCallback, Looper looper) {
            this.zzaFf = com_google_android_gms_location_internal_zzd;
            this.zzaFd = locationRequest;
            this.zzaFh = locationCallback;
            this.zzaFl = looper;
            super(googleApiClient);
        }

        protected void zza(zzl com_google_android_gms_location_internal_zzl) throws RemoteException {
            com_google_android_gms_location_internal_zzl.zza(LocationRequestInternal.zzb(this.zzaFd), this.zzaFh, this.zzaFl, new C08141(this));
        }
    }

    /* renamed from: com.google.android.gms.location.internal.zzd.7 */
    class C08177 extends zza {
        final /* synthetic */ PendingIntent zzaEY;
        final /* synthetic */ LocationRequest zzaFd;
        final /* synthetic */ zzd zzaFf;

        /* renamed from: com.google.android.gms.location.internal.zzd.7.1 */
        class C08161 extends com.google.android.gms.location.internal.zzg.zza {
            final /* synthetic */ C08177 zzaFo;

            C08161(C08177 c08177) {
                this.zzaFo = c08177;
            }

            public void zza(FusedLocationProviderResult fusedLocationProviderResult) {
                this.zzaFo.zzb(fusedLocationProviderResult.getStatus());
            }
        }

        C08177(zzd com_google_android_gms_location_internal_zzd, GoogleApiClient googleApiClient, LocationRequest locationRequest, PendingIntent pendingIntent) {
            this.zzaFf = com_google_android_gms_location_internal_zzd;
            this.zzaFd = locationRequest;
            this.zzaEY = pendingIntent;
            super(googleApiClient);
        }

        protected void zza(zzl com_google_android_gms_location_internal_zzl) throws RemoteException {
            com_google_android_gms_location_internal_zzl.zza(this.zzaFd, this.zzaEY, new C08161(this));
        }
    }

    /* renamed from: com.google.android.gms.location.internal.zzd.8 */
    class C08198 extends zza {
        final /* synthetic */ LocationListener zzaFe;
        final /* synthetic */ zzd zzaFf;

        /* renamed from: com.google.android.gms.location.internal.zzd.8.1 */
        class C08181 extends com.google.android.gms.location.internal.zzg.zza {
            final /* synthetic */ C08198 zzaFp;

            C08181(C08198 c08198) {
                this.zzaFp = c08198;
            }

            public void zza(FusedLocationProviderResult fusedLocationProviderResult) {
                this.zzaFp.zzb(fusedLocationProviderResult.getStatus());
            }
        }

        C08198(zzd com_google_android_gms_location_internal_zzd, GoogleApiClient googleApiClient, LocationListener locationListener) {
            this.zzaFf = com_google_android_gms_location_internal_zzd;
            this.zzaFe = locationListener;
            super(googleApiClient);
        }

        protected void zza(zzl com_google_android_gms_location_internal_zzl) throws RemoteException {
            com_google_android_gms_location_internal_zzl.zza(this.zzaFe, new C08181(this));
        }
    }

    /* renamed from: com.google.android.gms.location.internal.zzd.9 */
    class C08219 extends zza {
        final /* synthetic */ PendingIntent zzaEY;
        final /* synthetic */ zzd zzaFf;

        /* renamed from: com.google.android.gms.location.internal.zzd.9.1 */
        class C08201 extends com.google.android.gms.location.internal.zzg.zza {
            final /* synthetic */ C08219 zzaFq;

            C08201(C08219 c08219) {
                this.zzaFq = c08219;
            }

            public void zza(FusedLocationProviderResult fusedLocationProviderResult) {
                this.zzaFq.zzb(fusedLocationProviderResult.getStatus());
            }
        }

        C08219(zzd com_google_android_gms_location_internal_zzd, GoogleApiClient googleApiClient, PendingIntent pendingIntent) {
            this.zzaFf = com_google_android_gms_location_internal_zzd;
            this.zzaEY = pendingIntent;
            super(googleApiClient);
        }

        protected void zza(zzl com_google_android_gms_location_internal_zzl) throws RemoteException {
            com_google_android_gms_location_internal_zzl.zza(this.zzaEY, new C08201(this));
        }
    }

    public Location getLastLocation(GoogleApiClient client) {
        try {
            return LocationServices.zzd(client).getLastLocation();
        } catch (Exception e) {
            return null;
        }
    }

    public LocationAvailability getLocationAvailability(GoogleApiClient client) {
        try {
            return LocationServices.zzd(client).zzwD();
        } catch (Exception e) {
            return null;
        }
    }

    public PendingResult<Status> removeLocationUpdates(GoogleApiClient client, PendingIntent callbackIntent) {
        return client.zzb(new C08219(this, client, callbackIntent));
    }

    public PendingResult<Status> removeLocationUpdates(GoogleApiClient client, LocationCallback callback) {
        return client.zzb(new C08092(this, client, callback));
    }

    public PendingResult<Status> removeLocationUpdates(GoogleApiClient client, LocationListener listener) {
        return client.zzb(new C08198(this, client, listener));
    }

    public PendingResult<Status> requestLocationUpdates(GoogleApiClient client, LocationRequest request, PendingIntent callbackIntent) {
        return client.zzb(new C08177(this, client, request, callbackIntent));
    }

    public PendingResult<Status> requestLocationUpdates(GoogleApiClient client, LocationRequest request, LocationCallback callback, Looper looper) {
        return client.zzb(new C08156(this, client, request, callback, looper));
    }

    public PendingResult<Status> requestLocationUpdates(GoogleApiClient client, LocationRequest request, LocationListener listener) {
        return client.zzb(new C08071(this, client, request, listener));
    }

    public PendingResult<Status> requestLocationUpdates(GoogleApiClient client, LocationRequest request, LocationListener listener, Looper looper) {
        return client.zzb(new C08135(this, client, request, listener, looper));
    }

    public PendingResult<Status> setMockLocation(GoogleApiClient client, Location mockLocation) {
        return client.zzb(new C08114(this, client, mockLocation));
    }

    public PendingResult<Status> setMockMode(GoogleApiClient client, boolean isMockMode) {
        return client.zzb(new C08103(this, client, isMockMode));
    }
}
