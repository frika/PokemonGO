package com.upsight.android.analytics.internal.configuration;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightException;
import com.upsight.android.analytics.C1061R;
import com.upsight.android.analytics.configuration.UpsightConfiguration;
import com.upsight.android.analytics.dispatcher.EndpointResponse;
import com.upsight.android.analytics.event.config.UpsightConfigExpiredEvent;
import com.upsight.android.analytics.internal.DispatcherService.DestroyEvent;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.persistence.UpsightDataStore;
import com.upsight.android.persistence.UpsightDataStoreListener;
import com.upsight.android.persistence.UpsightSubscription;
import com.upsight.android.persistence.annotation.Created;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.IOUtils;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscription;
import rx.functions.Action0;

public final class ConfigurationManager {
    public static final String CONFIGURATION_RESPONSE_SUBTYPE = "upsight.configuration";
    public static final String CONFIGURATION_SUBTYPE = "upsight.configuration.configurationManager";
    private static final String LOG_TAG = "Configuration";
    private static final int RETRY_BASE = -1;
    private final Bus mBus;
    private int mConfigExpiryRetryCount;
    private final ManagerConfigParser mConfigParser;
    private Config mCurrentConfig;
    private final UpsightDataStore mDataStore;
    private UpsightSubscription mDataStoreSubscription;
    private boolean mIsLaunched;
    private final UpsightLogger mLogger;
    private final ConfigurationResponseParser mResponseParser;
    private Action0 mSyncAction;
    private final UpsightContext mUpsight;
    private final Worker mWorker;
    private Subscription mWorkerSubscription;

    /* renamed from: com.upsight.android.analytics.internal.configuration.ConfigurationManager.1 */
    class C10721 implements Action0 {
        C10721() {
        }

        public void call() {
            int i = 0;
            ConfigurationManager.this.mLogger.m574d(ConfigurationManager.LOG_TAG, "Record config.expired", new Object[0]);
            UpsightConfigExpiredEvent.createBuilder().recordSessionless(ConfigurationManager.this.mUpsight);
            Config config = ConfigurationManager.this.mCurrentConfig;
            if (config != null) {
                ConfigurationManager.this.mConfigExpiryRetryCount = ConfigurationManager.this.mConfigExpiryRetryCount + 1;
                ConfigurationManager configurationManager = ConfigurationManager.this;
                if (ConfigurationManager.this.mConfigExpiryRetryCount <= config.retryPowerExponentMax) {
                    i = ConfigurationManager.this.mConfigExpiryRetryCount;
                }
                configurationManager.mConfigExpiryRetryCount = i;
                ConfigurationManager.this.scheduleConfigExpiry((long) (((double) config.retryMultiplierMs) * Math.pow(config.retryPowerBase, (double) ConfigurationManager.this.mConfigExpiryRetryCount)));
            }
        }
    }

    /* renamed from: com.upsight.android.analytics.internal.configuration.ConfigurationManager.2 */
    class C10732 implements UpsightDataStoreListener<Set<UpsightConfiguration>> {
        C10732() {
        }

        public void onSuccess(Set<UpsightConfiguration> result) {
            if (ConfigurationManager.this.mCurrentConfig == null) {
                boolean hasApplied = false;
                for (UpsightConfiguration config : result) {
                    if (ConfigurationManager.CONFIGURATION_SUBTYPE.equals(config.getScope())) {
                        ConfigurationManager.this.mLogger.m574d(ConfigurationManager.LOG_TAG, "Apply local configurations", new Object[0]);
                        hasApplied = ConfigurationManager.this.applyConfiguration(config.getConfiguration());
                    }
                }
                if (!hasApplied) {
                    ConfigurationManager.this.applyDefaultConfiguration();
                }
            }
        }

        public void onFailure(UpsightException exception) {
            ConfigurationManager.this.mLogger.m576e(ConfigurationManager.LOG_TAG, "Could not fetch existing configurations from datastore", exception);
            if (ConfigurationManager.this.mCurrentConfig == null) {
                ConfigurationManager.this.applyDefaultConfiguration();
            }
        }
    }

    /* renamed from: com.upsight.android.analytics.internal.configuration.ConfigurationManager.3 */
    class C10743 implements UpsightDataStoreListener<Set<UpsightConfiguration>> {
        final /* synthetic */ Collection val$configs;

        C10743(Collection collection) {
            this.val$configs = collection;
        }

        public void onSuccess(Set<UpsightConfiguration> result) {
            for (UpsightConfiguration configuration : result) {
                ConfigurationManager.this.mDataStore.remove(configuration);
            }
            for (UpsightConfiguration config : this.val$configs) {
                if (ConfigurationManager.CONFIGURATION_SUBTYPE.equals(config.getScope())) {
                    ConfigurationManager.this.mLogger.m574d(ConfigurationManager.LOG_TAG, "Try to apply newly arrived configurations", new Object[0]);
                    if (ConfigurationManager.this.applyConfiguration(config.getConfiguration())) {
                        ConfigurationManager.this.mDataStore.store(config);
                    }
                } else {
                    ConfigurationManager.this.mDataStore.store(config);
                }
            }
        }

        public void onFailure(UpsightException exception) {
        }
    }

    public static final class Config {
        public final long requestIntervalMs;
        public final long retryMultiplierMs;
        public final double retryPowerBase;
        public final int retryPowerExponentMax;

        Config(long requestIntervalMs, long retryMultiplierMs, double retryPowerBase, int retryPowerExponentMax) {
            this.requestIntervalMs = requestIntervalMs;
            this.retryMultiplierMs = retryMultiplierMs;
            this.retryPowerBase = retryPowerBase;
            this.retryPowerExponentMax = retryPowerExponentMax;
        }

        public boolean isValid() {
            return this.requestIntervalMs > 0 && this.retryMultiplierMs > 0 && this.retryPowerBase > 0.0d && this.retryPowerExponentMax > 0;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Config that = (Config) o;
            if (that.requestIntervalMs == this.requestIntervalMs && that.retryMultiplierMs == this.retryMultiplierMs && that.retryPowerBase == this.retryPowerBase && that.retryPowerExponentMax == this.retryPowerExponentMax) {
                return true;
            }
            return false;
        }
    }

    ConfigurationManager(UpsightContext upsight, UpsightDataStore dataStore, ConfigurationResponseParser responseParser, ManagerConfigParser managerConfigParser, Scheduler scheduler, Bus bus, UpsightLogger logger) {
        this.mIsLaunched = false;
        this.mConfigExpiryRetryCount = RETRY_BASE;
        this.mSyncAction = new C10721();
        this.mUpsight = upsight;
        this.mDataStore = dataStore;
        this.mResponseParser = responseParser;
        this.mConfigParser = managerConfigParser;
        this.mBus = bus;
        this.mLogger = logger;
        this.mWorker = scheduler.createWorker();
    }

    private void fetchCurrentConfig() {
        this.mDataStore.fetch(UpsightConfiguration.class, new C10732());
    }

    private void applyDefaultConfiguration() {
        try {
            String config = IOUtils.toString(this.mUpsight.getResources().openRawResource(C1061R.raw.configurator_config));
            this.mLogger.m574d(LOG_TAG, "Apply default configurations", new Object[0]);
            applyConfiguration(config);
        } catch (IOException e) {
            this.mLogger.m576e(LOG_TAG, "Could not read default config", e);
        }
    }

    private synchronized boolean applyConfiguration(String jsonConfiguration) {
        boolean z = true;
        synchronized (this) {
            try {
                Config config = this.mConfigParser.parse(jsonConfiguration);
                if (config == null || !config.isValid()) {
                    this.mLogger.m582w(LOG_TAG, "Skipped invalid configurations", new Object[0]);
                    z = false;
                } else {
                    this.mCurrentConfig = config;
                    this.mLogger.m574d(LOG_TAG, "Configurations applied", new Object[0]);
                    if (this.mWorkerSubscription == null) {
                        scheduleConfigExpiry(0);
                    } else {
                        this.mConfigExpiryRetryCount = RETRY_BASE;
                        scheduleConfigExpiry(config.requestIntervalMs);
                    }
                }
            } catch (IOException e) {
                this.mLogger.m576e(LOG_TAG, "Could not parse configurations", e);
                z = false;
            }
        }
        return z;
    }

    private synchronized void scheduleConfigExpiry(long delayTime) {
        if (!(this.mWorkerSubscription == null || this.mWorkerSubscription.isUnsubscribed())) {
            this.mLogger.m574d(LOG_TAG, "Stop config.expired recording scheduler", new Object[0]);
            this.mWorkerSubscription.unsubscribe();
        }
        this.mLogger.m574d(LOG_TAG, "Schedule recording of config.expired in " + delayTime + " ms", new Object[0]);
        this.mWorkerSubscription = this.mWorker.schedule(this.mSyncAction, delayTime, TimeUnit.MILLISECONDS);
    }

    @Created
    public void onEndpointResponse(EndpointResponse response) {
        if (CONFIGURATION_RESPONSE_SUBTYPE.equals(response.getType())) {
            try {
                this.mDataStore.fetch(UpsightConfiguration.class, new C10743(this.mResponseParser.parse(response.getContent())));
            } catch (IOException e) {
                this.mLogger.m576e(LOG_TAG, "Could not parse newly arrived configurations", e);
            }
        }
    }

    @Subscribe
    public void handle(DestroyEvent event) {
        terminate();
    }

    public synchronized void launch() {
        if (!this.mIsLaunched) {
            this.mIsLaunched = true;
            this.mCurrentConfig = null;
            this.mDataStoreSubscription = this.mDataStore.subscribe(this);
            this.mWorkerSubscription = null;
            this.mConfigExpiryRetryCount = RETRY_BASE;
            this.mBus.register(this);
            fetchCurrentConfig();
        }
    }

    public synchronized void terminate() {
        this.mBus.unregister(this);
        if (this.mDataStoreSubscription != null) {
            this.mDataStoreSubscription.unsubscribe();
            this.mDataStoreSubscription = null;
        }
        if (this.mWorkerSubscription != null) {
            this.mLogger.m574d(LOG_TAG, "Stop config.expired recording scheduler", new Object[0]);
            this.mWorkerSubscription.unsubscribe();
            this.mWorkerSubscription = null;
        }
        this.mConfigExpiryRetryCount = RETRY_BASE;
        this.mCurrentConfig = null;
        this.mIsLaunched = false;
    }
}
