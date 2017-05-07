package android.support.v4.net;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.upsight.mediation.mraid.properties.MRAIDResizeProperties;
import spacemadness.com.lunarconsole.C1628R;

class ConnectivityManagerCompatGingerbread {
    ConnectivityManagerCompatGingerbread() {
    }

    public static boolean isActiveNetworkMetered(ConnectivityManager cm) {
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            return true;
        }
        switch (info.getType()) {
            case C1628R.styleable.RecyclerView_android_orientation /*0*/:
            case C1628R.styleable.RecyclerView_spanCount /*2*/:
            case C1628R.styleable.RecyclerView_reverseLayout /*3*/:
            case C1628R.styleable.RecyclerView_stackFromEnd /*4*/:
            case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_CENTER /*5*/:
            case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_RIGHT /*6*/:
                return true;
            case C1628R.styleable.RecyclerView_layoutManager /*1*/:
                return false;
            default:
                return true;
        }
    }
}
