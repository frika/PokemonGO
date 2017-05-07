package com.upsight.mediation.ads.adapters;

import com.upsight.mediation.log.FuseLog;
import java.util.ArrayList;

public class MRaidRegistry {
    private static final String TAG = "MRaidRegistry";
    public ArrayList<MRaidAdAdapter> registeredProviders;

    public MRaidRegistry() {
        this.registeredProviders = new ArrayList();
    }

    public int register(MRaidAdAdapter provider) {
        if (this.registeredProviders.contains(provider)) {
            FuseLog.m609w(TAG, "Trying to register provider, already registered");
        }
        this.registeredProviders.add(provider);
        return this.registeredProviders.indexOf(provider);
    }

    public MRaidAdAdapter getProvider(int index) {
        return (MRaidAdAdapter) this.registeredProviders.get(index);
    }
}
