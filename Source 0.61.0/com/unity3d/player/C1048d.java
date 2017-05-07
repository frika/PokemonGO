package com.unity3d.player;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.android.exoplayer.util.MpegAudioHeader;
import java.util.LinkedList;
import java.util.List;

/* renamed from: com.unity3d.player.d */
public final class C1048d implements C1045b {

    /* renamed from: com.unity3d.player.d.1 */
    class C10471 extends Fragment {
        final /* synthetic */ List f676a;
        final /* synthetic */ FragmentManager f677b;
        final /* synthetic */ Runnable f678c;
        final /* synthetic */ C1048d f679d;

        C10471(C1048d c1048d, List list, FragmentManager fragmentManager, Runnable runnable) {
            this.f679d = c1048d;
            this.f676a = list;
            this.f677b = fragmentManager;
            this.f678c = runnable;
        }

        public final void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
            if (i == 15881) {
                int i2 = 0;
                while (i2 < strArr.length && i2 < iArr.length) {
                    C1046c.Log(4, strArr[i2] + (iArr[i2] == 0 ? " granted" : " denied"));
                    i2++;
                }
                FragmentTransaction beginTransaction = this.f677b.beginTransaction();
                beginTransaction.remove(this);
                beginTransaction.commit();
                this.f678c.run();
            }
        }

        public final void onStart() {
            super.onStart();
            requestPermissions((String[]) this.f676a.toArray(new String[0]), 15881);
        }
    }

    private static boolean m548a(PackageItemInfo packageItemInfo) {
        try {
            return packageItemInfo.metaData.getBoolean("unityplayer.SkipPermissionsDialog");
        } catch (Exception e) {
            return false;
        }
    }

    public final void m549a(Activity activity, Runnable runnable) {
        if (activity != null) {
            PackageManager packageManager = activity.getPackageManager();
            try {
                PackageItemInfo activityInfo = packageManager.getActivityInfo(activity.getComponentName(), AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
                PackageItemInfo applicationInfo = packageManager.getApplicationInfo(activity.getPackageName(), AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
                if (C1048d.m548a(activityInfo) || C1048d.m548a(applicationInfo)) {
                    runnable.run();
                    return;
                }
            } catch (Exception e) {
            }
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(activity.getPackageName(), MpegAudioHeader.MAX_FRAME_SIZE_BYTES);
                if (packageInfo.requestedPermissions == null) {
                    packageInfo.requestedPermissions = new String[0];
                }
                List linkedList = new LinkedList();
                for (String str : packageInfo.requestedPermissions) {
                    try {
                        if (packageManager.getPermissionInfo(str, AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS).protectionLevel == 1 && activity.checkCallingOrSelfPermission(str) != 0) {
                            linkedList.add(str);
                        }
                    } catch (NameNotFoundException e2) {
                        C1046c.Log(5, "Failed to get permission info for " + str + ", manifest likely missing custom permission declaration");
                        C1046c.Log(5, "Permission " + str + " ignored");
                    }
                }
                if (linkedList.isEmpty()) {
                    runnable.run();
                    return;
                }
                FragmentManager fragmentManager = activity.getFragmentManager();
                Fragment c10471 = new C10471(this, linkedList, fragmentManager, runnable);
                FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
                beginTransaction.add(0, c10471);
                beginTransaction.commit();
            } catch (Exception e3) {
                C1046c.Log(6, "Unable to query for permission: " + e3.getMessage());
            }
        }
    }
}
