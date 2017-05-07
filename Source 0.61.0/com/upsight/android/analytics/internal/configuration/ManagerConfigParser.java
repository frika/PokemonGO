package com.upsight.android.analytics.internal.configuration;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.analytics.internal.configuration.ConfigurationManager.Config;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class ManagerConfigParser {
    private Gson mGson;

    public static class ConfigJson {
        @SerializedName("requestInterval")
        @Expose
        public long requestInterval;
        @SerializedName("retryMultiplier")
        @Expose
        public long retryMultiplier;
        @SerializedName("retryPowerBase")
        @Expose
        public double retryPowerBase;
        @SerializedName("retryPowerExponentMax")
        @Expose
        public int retryPowerExponentMax;
    }

    @Inject
    ManagerConfigParser(@Named("config-gson") Gson gson) {
        this.mGson = gson;
    }

    public Config parse(String configJson) throws IOException {
        try {
            ConfigJson config = (ConfigJson) this.mGson.fromJson(configJson, ConfigJson.class);
            return new Config(TimeUnit.SECONDS.toMillis(config.requestInterval), TimeUnit.SECONDS.toMillis(config.retryMultiplier), config.retryPowerBase, config.retryPowerExponentMax);
        } catch (JsonSyntaxException e) {
            throw new IOException(e);
        }
    }
}
