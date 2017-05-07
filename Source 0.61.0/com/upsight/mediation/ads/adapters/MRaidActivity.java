package com.upsight.mediation.ads.adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import com.upsight.mediation.log.FuseLog;
import com.upsight.mediation.mraid.MRAIDInterstitial;
import spacemadness.com.lunarconsole.BuildConfig;
import spacemadness.com.lunarconsole.C1628R;

public class MRaidActivity extends Activity {
    private static final String TAG = "MRaidActivity";
    private boolean firstResume;
    private MRAIDInterstitial interstitial;
    public boolean isVisible;
    private MRaidAdAdapter provider;
    private int registryId;
    private int rotateMode;

    public MRaidActivity() {
        this.firstResume = true;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.registryId = getIntent().getIntExtra("registryId", -1);
        this.provider = MRaidAdAdapter.mRaidRegistry.getProvider(this.registryId);
        this.provider.setMRaidActivity(this);
        this.rotateMode = getIntent().getIntExtra("rotate", 1);
        setRequestedOrientation(getOrientationValue(this.rotateMode));
        this.interstitial = this.provider.getInterstitial();
        this.interstitial.updateContext(this);
        FuseLog.m605e(TAG, getRequestedOrientation() + BuildConfig.FLAVOR);
    }

    private int getOrientationValue(int rotateMode) {
        switch (rotateMode) {
            case C1628R.styleable.RecyclerView_spanCount /*2*/:
                return 14;
            case C1628R.styleable.RecyclerView_reverseLayout /*3*/:
                return 6;
            case C1628R.styleable.RecyclerView_stackFromEnd /*4*/:
                return 7;
            default:
                return -1;
        }
    }

    protected void onResume() {
        super.onResume();
        this.isVisible = true;
        if (this.firstResume) {
            this.firstResume = false;
            if (this.interstitial == null) {
                finish();
            } else if (!this.interstitial.show()) {
                this.provider.mraidInterstitialFailedToShow();
                finish();
            }
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onPause() {
        super.onPause();
        this.isVisible = false;
    }

    public void onBackPressed() {
        super.onBackPressed();
        this.provider.mraidInterstitialHide(this.interstitial);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 1) {
            if (resultCode == -1) {
                this.provider.onAdCompleted();
            } else if (resultCode == 0) {
                this.provider.onAdSkipped();
            }
            this.provider.mraidInterstitialHide(this.interstitial);
        }
    }
}
