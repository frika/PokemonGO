package com.upsight.android.marketing.internal.content;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightException;
import com.upsight.android.analytics.event.UpsightDynamicEvent;
import com.upsight.android.analytics.event.UpsightDynamicEvent.Builder;
import com.upsight.android.analytics.event.datacollection.UpsightDataCollectionEvent;
import com.upsight.android.analytics.internal.action.Action;
import com.upsight.android.analytics.internal.action.ActionContext;
import com.upsight.android.analytics.internal.action.ActionFactory;
import com.upsight.android.analytics.internal.association.Association;
import com.upsight.android.analytics.internal.session.Clock;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.marketing.C1162R;
import com.upsight.android.marketing.UpsightContentMediator;
import com.upsight.android.marketing.UpsightPurchase;
import com.upsight.android.marketing.UpsightReward;
import com.upsight.android.marketing.internal.content.MarketingContent.ContentLoadedEvent;
import com.upsight.android.marketing.internal.content.MarketingContent.PendingDialog;
import com.upsight.android.marketing.internal.content.MarketingContent.SubcontentAvailabilityEvent;
import com.upsight.android.marketing.internal.content.MarketingContent.SubdialogAvailabilityEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import rx.Scheduler.Worker;
import rx.Subscription;
import rx.functions.Action0;

public final class MarketingContentActions {
    private static final Map<String, InternalFactory> FACTORY_MAP;

    private interface InternalFactory {
        Action<MarketingContent, MarketingContentActionContext> create(MarketingContentActionContext marketingContentActionContext, String str, JsonObject jsonObject);
    }

    /* renamed from: com.upsight.android.marketing.internal.content.MarketingContentActions.1 */
    static class C11851 extends HashMap<String, InternalFactory> {

        /* renamed from: com.upsight.android.marketing.internal.content.MarketingContentActions.1.1 */
        class C11761 implements InternalFactory {
            C11761() {
            }

            public Action<MarketingContent, MarketingContentActionContext> create(MarketingContentActionContext actionContext, String actionType, JsonObject actionParams) {
                return new Trigger(actionType, actionParams, null);
            }
        }

        /* renamed from: com.upsight.android.marketing.internal.content.MarketingContentActions.1.2 */
        class C11772 implements InternalFactory {
            C11772() {
            }

            public Action<MarketingContent, MarketingContentActionContext> create(MarketingContentActionContext actionContext, String actionType, JsonObject actionParams) {
                return new TriggerIfContentBuilt(actionType, actionParams, null);
            }
        }

        /* renamed from: com.upsight.android.marketing.internal.content.MarketingContentActions.1.3 */
        class C11783 implements InternalFactory {
            C11783() {
            }

            public Action<MarketingContent, MarketingContentActionContext> create(MarketingContentActionContext actionContext, String actionType, JsonObject actionParams) {
                return new TriggerIfContentAvailable(actionType, actionParams, null);
            }
        }

        /* renamed from: com.upsight.android.marketing.internal.content.MarketingContentActions.1.4 */
        class C11794 implements InternalFactory {
            C11794() {
            }

            public Action<MarketingContent, MarketingContentActionContext> create(MarketingContentActionContext actionContext, String actionType, JsonObject actionParams) {
                return new PresentScopedContent(actionType, actionParams, null);
            }
        }

        /* renamed from: com.upsight.android.marketing.internal.content.MarketingContentActions.1.5 */
        class C11805 implements InternalFactory {
            C11805() {
            }

            public Action<MarketingContent, MarketingContentActionContext> create(MarketingContentActionContext actionContext, String actionType, JsonObject actionParams) {
                return new PresentScopedDialog(actionType, actionParams, null);
            }
        }

        /* renamed from: com.upsight.android.marketing.internal.content.MarketingContentActions.1.6 */
        class C11816 implements InternalFactory {
            C11816() {
            }

            public Action<MarketingContent, MarketingContentActionContext> create(MarketingContentActionContext actionContext, String actionType, JsonObject actionParams) {
                return new PresentContent(actionType, actionParams, null);
            }
        }

        /* renamed from: com.upsight.android.marketing.internal.content.MarketingContentActions.1.7 */
        class C11827 implements InternalFactory {
            C11827() {
            }

            public Action<MarketingContent, MarketingContentActionContext> create(MarketingContentActionContext actionContext, String actionType, JsonObject actionParams) {
                return new PresentDialog(actionType, actionParams, null);
            }
        }

        /* renamed from: com.upsight.android.marketing.internal.content.MarketingContentActions.1.8 */
        class C11838 implements InternalFactory {
            C11838() {
            }

            public Action<MarketingContent, MarketingContentActionContext> create(MarketingContentActionContext actionContext, String actionType, JsonObject actionParams) {
                return new PresentScopelessContent(actionType, actionParams, null);
            }
        }

        /* renamed from: com.upsight.android.marketing.internal.content.MarketingContentActions.1.9 */
        class C11849 implements InternalFactory {
            C11849() {
            }

            public Action<MarketingContent, MarketingContentActionContext> create(MarketingContentActionContext actionContext, String actionType, JsonObject actionParams) {
                return new PresentCloseButton(actionType, actionParams, null);
            }
        }

        C11851() {
            put("action_trigger", new C11761());
            put("action_trigger_if_content_built", new C11772());
            put("action_trigger_if_content_available", new C11783());
            put("action_present_scoped_content", new C11794());
            put("action_present_scoped_dialog", new C11805());
            put("action_present_content", new C11816());
            put("action_present_dialog", new C11827());
            put("action_present_scopeless_content", new C11838());
            put("action_present_close_button", new C11849());
            put("action_destroy", new InternalFactory() {
                public Action<MarketingContent, MarketingContentActionContext> create(MarketingContentActionContext actionContext, String actionType, JsonObject actionParams) {
                    return new Destroy(actionType, actionParams, null);
                }
            });
            put("action_open_url", new InternalFactory() {
                public Action<MarketingContent, MarketingContentActionContext> create(MarketingContentActionContext actionContext, String actionType, JsonObject actionParams) {
                    return new OpenUrl(actionType, actionParams, null);
                }
            });
            put("action_send_event", new InternalFactory() {
                public Action<MarketingContent, MarketingContentActionContext> create(MarketingContentActionContext actionContext, String actionType, JsonObject actionParams) {
                    return new SendEvent(actionType, actionParams, null);
                }
            });
            put("action_send_form_data", new InternalFactory() {
                public Action<MarketingContent, MarketingContentActionContext> create(MarketingContentActionContext actionContext, String actionType, JsonObject actionParams) {
                    return new SendFormData(actionType, actionParams, null);
                }
            });
            put("action_notify_rewards", new InternalFactory() {
                public Action<MarketingContent, MarketingContentActionContext> create(MarketingContentActionContext actionContext, String actionType, JsonObject actionParams) {
                    return new NotifyRewards(actionType, actionParams, null);
                }
            });
            put("action_notify_purchases", new InternalFactory() {
                public Action<MarketingContent, MarketingContentActionContext> create(MarketingContentActionContext actionContext, String actionType, JsonObject actionParams) {
                    return new NotifyPurchases(actionType, actionParams, null);
                }
            });
            put("action_associate_once", new InternalFactory() {
                public Action<MarketingContent, MarketingContentActionContext> create(MarketingContentActionContext actionContext, String actionType, JsonObject actionParams) {
                    return new AssociateOnce(actionType, actionParams, null);
                }
            });
        }
    }

    static class AssociateOnce extends Action<MarketingContent, MarketingContentActionContext> {
        public static final String UPSIGHT_DATA = "upsight_data";
        public static final String UPSIGHT_DATA_FILTER = "upsight_data_filter";
        public static final String WITH = "with";

        private AssociateOnce(MarketingContentActionContext actionContext, String type, JsonObject params) {
            super(actionContext, type, params);
        }

        public void execute(MarketingContent content) {
            Exception e;
            ActionContext actionContext = getActionContext();
            String with = optParamString(WITH);
            JsonObject upsightDataFilter = optParamJsonObject(UPSIGHT_DATA_FILTER);
            JsonObject upsightData = optParamJsonObject(UPSIGHT_DATA);
            try {
                actionContext.mUpsight.getDataStore().store(Association.from(with, upsightDataFilter, upsightData, actionContext.mGson, actionContext.mClock));
            } catch (IllegalArgumentException e2) {
                e = e2;
                actionContext.mLogger.m577e(getClass().getSimpleName(), e, "Failed to parse Association with=" + with + " upsightDataFilter=" + upsightDataFilter + " upsightData" + upsightData, new Object[0]);
                content.signalActionCompleted(actionContext.mBus);
            } catch (IOException e3) {
                e = e3;
                actionContext.mLogger.m577e(getClass().getSimpleName(), e, "Failed to parse Association with=" + with + " upsightDataFilter=" + upsightDataFilter + " upsightData" + upsightData, new Object[0]);
                content.signalActionCompleted(actionContext.mBus);
            }
            content.signalActionCompleted(actionContext.mBus);
        }
    }

    static class Destroy extends Action<MarketingContent, MarketingContentActionContext> {
        private Destroy(MarketingContentActionContext actionContext, String type, JsonObject params) {
            super(actionContext, type, params);
        }

        public void execute(MarketingContent content) {
            String id = content.getId();
            MarketingContentActionContext actionContext = (MarketingContentActionContext) getActionContext();
            if (!TextUtils.isEmpty(id)) {
                actionContext.mContentStore.remove(id);
                actionContext.mBus.post(new DestroyEvent(null));
            }
            Bus bus = actionContext.mBus;
            content.signalActionCompleted(bus);
            content.signalActionMapCompleted(bus);
        }
    }

    public static class DestroyEvent {
        public final String mId;

        private DestroyEvent(String id) {
            this.mId = id;
        }
    }

    public static class MarketingContentActionContext extends ActionContext {
        public final MarketingContentMediatorManager mContentMediatorManager;
        public final MarketingContentStore mContentStore;
        public final ContentTemplateWebViewClientFactory mContentTemplateWebViewClientFactory;

        public MarketingContentActionContext(UpsightContext upsight, Bus bus, Gson gson, Clock clock, Worker mainWorker, UpsightLogger logger, MarketingContentMediatorManager contentMediatorManager, MarketingContentStore contentStore, ContentTemplateWebViewClientFactory contentTemplateWebViewClientFactory) {
            super(upsight, bus, gson, clock, mainWorker, logger);
            this.mContentMediatorManager = contentMediatorManager;
            this.mContentStore = contentStore;
            this.mContentTemplateWebViewClientFactory = contentTemplateWebViewClientFactory;
        }
    }

    public static class MarketingContentActionFactory implements ActionFactory<MarketingContent, MarketingContentActionContext> {
        public static final String TYPE = "marketing_content_factory";

        public Action<MarketingContent, MarketingContentActionContext> create(MarketingContentActionContext actionContext, JsonObject actionJSON) throws UpsightException {
            if (actionJSON == null) {
                throw new UpsightException("Failed to create Action. JSON is null.", new Object[0]);
            }
            String actionType = actionJSON.get(ActionFactory.KEY_ACTION_TYPE).getAsString();
            JsonObject actionParams = actionJSON.getAsJsonObject(ActionFactory.KEY_ACTION_PARAMS);
            InternalFactory factory = (InternalFactory) MarketingContentActions.FACTORY_MAP.get(actionType);
            if (factory != null) {
                return factory.create(actionContext, actionType, actionParams);
            }
            throw new UpsightException("Failed to create Action. Unknown action type.", new Object[0]);
        }
    }

    static class NotifyPurchases extends Action<MarketingContent, MarketingContentActionContext> {
        public static final String PURCHASES = "purchases";

        private NotifyPurchases(MarketingContentActionContext actionContext, String type, JsonObject params) {
            super(actionContext, type, params);
        }

        public void execute(MarketingContent content) {
            List<UpsightPurchase> purchases = new ArrayList();
            JsonElement purchasesJson = optParamJsonArray(PURCHASES);
            if (purchasesJson != null && purchasesJson.isJsonArray()) {
                ActionContext actionContext = getActionContext();
                Iterator<JsonElement> itr = purchasesJson.getAsJsonArray().iterator();
                while (itr.hasNext()) {
                    JsonElement purchaseJson = null;
                    try {
                        purchaseJson = (JsonElement) itr.next();
                        purchases.add(Purchase.from(purchaseJson, actionContext.mGson));
                    } catch (IOException e) {
                        actionContext.mLogger.m577e(getClass().getSimpleName(), e, "Failed to parse Purchase purchaseJson=" + purchaseJson, new Object[0]);
                    }
                }
            }
            Bus bus = ((MarketingContentActionContext) getActionContext()).mBus;
            bus.post(new PurchasesEvent(purchases, null));
            content.signalActionCompleted(bus);
        }
    }

    static class NotifyRewards extends Action<MarketingContent, MarketingContentActionContext> {
        public static final String REWARDS = "rewards";

        private NotifyRewards(MarketingContentActionContext actionContext, String type, JsonObject params) {
            super(actionContext, type, params);
        }

        public void execute(MarketingContent content) {
            List<UpsightReward> rewards = new ArrayList();
            JsonElement rewardsJson = optParamJsonArray(REWARDS);
            if (rewardsJson != null && rewardsJson.isJsonArray()) {
                ActionContext actionContext = getActionContext();
                Iterator<JsonElement> itr = rewardsJson.getAsJsonArray().iterator();
                while (itr.hasNext()) {
                    JsonElement rewardJson = null;
                    try {
                        rewardJson = (JsonElement) itr.next();
                        rewards.add(Reward.from(rewardJson, actionContext.mGson));
                    } catch (IOException e) {
                        actionContext.mLogger.m577e(getClass().getSimpleName(), e, "Failed to parse Reward rewardJson=" + rewardJson, new Object[0]);
                    }
                }
            }
            Bus bus = ((MarketingContentActionContext) getActionContext()).mBus;
            bus.post(new RewardsEvent(rewards, null));
            content.signalActionCompleted(bus);
        }
    }

    static class OpenUrl extends Action<MarketingContent, MarketingContentActionContext> {
        public static final String URL = "url";

        private OpenUrl(MarketingContentActionContext actionContext, String type, JsonObject params) {
            super(actionContext, type, params);
        }

        public void execute(MarketingContent content) {
            MarketingContentActionContext actionContext = (MarketingContentActionContext) getActionContext();
            String uri = optParamString(URL);
            if (TextUtils.isEmpty(uri)) {
                actionContext.mLogger.m576e(getClass().getSimpleName(), "Action execution failed actionType=" + getType() + " uri=" + uri, new Object[0]);
            } else {
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(uri));
                intent.setFlags(268435456);
                try {
                    actionContext.mUpsight.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    actionContext.mLogger.m577e(getClass().getSimpleName(), e, "Action execution failed actionType=" + getType() + " intent=" + intent, new Object[0]);
                }
            }
            content.signalActionCompleted(actionContext.mBus);
        }
    }

    static class PresentCloseButton extends Action<MarketingContent, MarketingContentActionContext> {
        public static final String DELAY_MS = "delay_ms";

        /* renamed from: com.upsight.android.marketing.internal.content.MarketingContentActions.PresentCloseButton.1 */
        class C11861 implements Action0 {
            final /* synthetic */ MarketingContent val$content;

            C11861(MarketingContent marketingContent) {
                this.val$content = marketingContent;
            }

            public void call() {
                View contentView = this.val$content.getContentView();
                if (contentView != null && contentView.getRootView() != null) {
                    ((ImageView) contentView.findViewById(C1162R.id.upsight_marketing_content_view_close_button)).setVisibility(0);
                }
            }
        }

        private PresentCloseButton(MarketingContentActionContext actionContext, String type, JsonObject params) {
            super(actionContext, type, params);
        }

        public void execute(MarketingContent content) {
            ((MarketingContentActionContext) getActionContext()).mMainWorker.schedule(new C11861(content), (long) optParamInt(DELAY_MS), TimeUnit.MILLISECONDS);
            content.signalActionCompleted(((MarketingContentActionContext) getActionContext()).mBus);
        }
    }

    static class PresentContent extends Action<MarketingContent, MarketingContentActionContext> {
        private PresentContent(MarketingContentActionContext actionContext, String type, JsonObject params) {
            super(actionContext, type, params);
        }

        public void execute(MarketingContent content) {
            Bus bus = ((MarketingContentActionContext) getActionContext()).mBus;
            bus.post(new SubcontentAvailabilityEvent(content.getId()));
            content.signalActionCompleted(bus);
        }
    }

    static class PresentDialog extends Action<MarketingContent, MarketingContentActionContext> {
        public static final String BUTTONS = "buttons";
        public static final String DISMISS_TRIGGER = "dismiss_trigger";
        public static final String MESSAGE = "message";
        public static final String TITLE = "title";

        private PresentDialog(MarketingContentActionContext actionContext, String type, JsonObject params) {
            super(actionContext, type, params);
        }

        public void execute(MarketingContent content) {
            MarketingContentActionContext actionContext = (MarketingContentActionContext) getActionContext();
            String title = optParamString(TITLE);
            String message = optParamString(MESSAGE);
            JsonArray buttons = optParamJsonArray(BUTTONS);
            String dismissTrigger = optParamString(DISMISS_TRIGGER);
            String serializedButtons = null;
            if (buttons != null) {
                serializedButtons = buttons.toString();
            }
            Bus bus = ((MarketingContentActionContext) getActionContext()).mBus;
            bus.post(new SubdialogAvailabilityEvent(content.getId(), new PendingDialog(content.getId(), title, message, serializedButtons, dismissTrigger)));
            content.signalActionCompleted(bus);
        }
    }

    static class PresentScopedContent extends Action<MarketingContent, MarketingContentActionContext> {
        public static final String ID = "id";
        public static final String SCOPE_LIST = "scope_list";

        private PresentScopedContent(MarketingContentActionContext actionContext, String type, JsonObject params) {
            super(actionContext, type, params);
        }

        public void execute(MarketingContent content) {
            String id = optParamString(ID);
            JsonElement scopeList = optParamJsonArray(SCOPE_LIST);
            if (!(TextUtils.isEmpty(id) || scopeList == null || !scopeList.isJsonArray())) {
                List<String> scopes = new ArrayList();
                Iterator it = scopeList.getAsJsonArray().iterator();
                while (it.hasNext()) {
                    JsonElement scope = (JsonElement) it.next();
                    if (scope.isJsonPrimitive() && scope.getAsJsonPrimitive().isString()) {
                        scopes.add(scope.getAsString());
                    }
                }
                ((MarketingContentActionContext) getActionContext()).mContentStore.presentScopedContent(id, (String[]) scopes.toArray(new String[scopes.size()]));
            }
            content.signalActionCompleted(((MarketingContentActionContext) getActionContext()).mBus);
        }
    }

    static class PresentScopedDialog extends Action<MarketingContent, MarketingContentActionContext> {
        public static final String BUTTONS = "buttons";
        public static final String DISMISS_TRIGGER = "dismiss_trigger";
        public static final String MESSAGE = "message";
        public static final String SCOPE_LIST = "scope_list";
        public static final String TITLE = "title";

        private PresentScopedDialog(MarketingContentActionContext actionContext, String type, JsonObject params) {
            super(actionContext, type, params);
        }

        public void execute(MarketingContent content) {
            MarketingContentActionContext actionContext = (MarketingContentActionContext) getActionContext();
            JsonArray scopeList = optParamJsonArray(SCOPE_LIST);
            String title = optParamString(TITLE);
            String message = optParamString(MESSAGE);
            JsonArray buttons = optParamJsonArray(BUTTONS);
            String dismissTrigger = optParamString(DISMISS_TRIGGER);
            List<String> scopes = new ArrayList();
            Iterator it = scopeList.iterator();
            while (it.hasNext()) {
                JsonElement scope = (JsonElement) it.next();
                if (scope.isJsonPrimitive() && scope.getAsJsonPrimitive().isString()) {
                    scopes.add(scope.getAsString());
                }
            }
            String serializedButtons = null;
            if (buttons != null) {
                serializedButtons = buttons.toString();
            }
            content.addPendingDialog(new PendingDialog(content.getId(), title, message, serializedButtons, dismissTrigger));
            ((MarketingContentActionContext) getActionContext()).mContentStore.presentScopedContent(content.getId(), (String[]) scopes.toArray(new String[scopes.size()]));
            content.signalActionCompleted(actionContext.mBus);
        }
    }

    static class PresentScopelessContent extends Action<MarketingContent, MarketingContentActionContext> {
        public static final String NEXT_ID = "next_id";
        public static final String SELF_ID = "self_id";

        private PresentScopelessContent(MarketingContentActionContext actionContext, String type, JsonObject params) {
            super(actionContext, type, params);
        }

        public void execute(MarketingContent content) {
            String selfId = optParamString(SELF_ID);
            String nextId = optParamString(NEXT_ID);
            if (!(TextUtils.isEmpty(selfId) || TextUtils.isEmpty(nextId))) {
                ((MarketingContentActionContext) getActionContext()).mContentStore.presentScopelessContent(nextId, selfId);
            }
            content.signalActionCompleted(((MarketingContentActionContext) getActionContext()).mBus);
        }
    }

    public static class PurchasesEvent {
        public final String mId;
        public final List<UpsightPurchase> mPurchases;

        private PurchasesEvent(String id, List<UpsightPurchase> purchases) {
            this.mId = id;
            this.mPurchases = purchases;
        }
    }

    public static class RewardsEvent {
        public final String mId;
        public final List<UpsightReward> mRewards;

        private RewardsEvent(String id, List<UpsightReward> rewards) {
            this.mId = id;
            this.mRewards = rewards;
        }
    }

    static class SendEvent extends Action<MarketingContent, MarketingContentActionContext> {
        public static final String EVENT = "event";
        public static final String IDENTIFIERS = "identifiers";
        public static final String PUB_DATA = "pub_data";
        public static final String TYPE = "type";
        public static final String UPSIGHT_DATA = "upsight_data";

        private SendEvent(MarketingContentActionContext actionContext, String type, JsonObject params) {
            super(actionContext, type, params);
        }

        public void execute(MarketingContent content) {
            MarketingContentActionContext actionContext = (MarketingContentActionContext) getActionContext();
            JsonObject event = optParamJsonObject(EVENT);
            if (event != null) {
                JsonElement type = event.get(TYPE);
                if (type.isJsonPrimitive() && type.getAsJsonPrimitive().isString()) {
                    Builder dynamicEventBuilder = UpsightDynamicEvent.createBuilder(type.getAsString()).putUpsightData(event.get(UPSIGHT_DATA).getAsJsonObject());
                    if (event.has(PUB_DATA)) {
                        dynamicEventBuilder.putPublisherData(event.getAsJsonObject(PUB_DATA));
                    }
                    if (event.has(IDENTIFIERS) && event.get(IDENTIFIERS).isJsonPrimitive() && event.get(IDENTIFIERS).getAsJsonPrimitive().isString()) {
                        dynamicEventBuilder.setDynamicIdentifiers(event.get(IDENTIFIERS).getAsString());
                    }
                    dynamicEventBuilder.record(actionContext.mUpsight);
                } else {
                    actionContext.mLogger.m576e(getClass().getSimpleName(), "Action failed actionType=" + getType() + " type=" + type, new Object[0]);
                }
            } else {
                actionContext.mLogger.m576e(getClass().getSimpleName(), "Action failed actionType=" + getType() + " event=" + event, new Object[0]);
            }
            content.signalActionCompleted(actionContext.mBus);
        }
    }

    static class SendFormData extends Action<MarketingContent, MarketingContentActionContext> {
        public static final String DATA_KEY = "data_key";
        public static final String STREAM_ID = "stream_id";

        private SendFormData(MarketingContentActionContext actionContext, String type, JsonObject params) {
            super(actionContext, type, params);
        }

        public void execute(MarketingContent content) {
            MarketingContentActionContext actionContext = (MarketingContentActionContext) getActionContext();
            String dataKey = optParamString(DATA_KEY);
            String streamId = optParamString(STREAM_ID);
            if (dataKey == null || streamId == null) {
                actionContext.mLogger.m576e(getClass().getSimpleName(), "Action failed actionType=" + getType() + " dataKey=" + dataKey, new Object[0]);
            } else {
                String optInData = content.getExtra(dataKey);
                if (optInData != null) {
                    UpsightDataCollectionEvent.createBuilder(optInData, streamId).record(actionContext.mUpsight);
                }
            }
            content.signalActionCompleted(actionContext.mBus);
        }
    }

    static class Trigger extends Action<MarketingContent, MarketingContentActionContext> {
        public static final String TRIGGER = "trigger";

        private Trigger(MarketingContentActionContext actionContext, String type, JsonObject params) {
            super(actionContext, type, params);
        }

        public void execute(MarketingContent content) {
            String trigger = optParamString(TRIGGER);
            if (!TextUtils.isEmpty(trigger)) {
                content.executeActions(trigger);
            }
            content.signalActionCompleted(((MarketingContentActionContext) getActionContext()).mBus);
        }
    }

    static class TriggerIfContentAvailable extends Action<MarketingContent, MarketingContentActionContext> {
        public static final String CONDITION_PARAMETERS = "condition_parameters";
        public static final String ELSE_TRIGGER = "else_trigger";
        public static final String ID = "id";
        public static final String THEN_TRIGGER = "then_trigger";
        public static final String TIMEOUT_MS = "timeout_ms";
        private boolean isTriggerExecuted;
        private String mConditionalContentID;
        private MarketingContent mContent;
        private Subscription mSubscription;

        /* renamed from: com.upsight.android.marketing.internal.content.MarketingContentActions.TriggerIfContentAvailable.1 */
        class C11871 implements Action0 {
            final /* synthetic */ MarketingContentActionContext val$actionContext;
            final /* synthetic */ MarketingContent val$content;

            C11871(MarketingContent marketingContent, MarketingContentActionContext marketingContentActionContext) {
                this.val$content = marketingContent;
                this.val$actionContext = marketingContentActionContext;
            }

            public void call() {
                String trigger = TriggerIfContentAvailable.this.optParamString(TriggerIfContentAvailable.ELSE_TRIGGER);
                if (!(TextUtils.isEmpty(trigger) || TriggerIfContentAvailable.this.isTriggerExecuted)) {
                    this.val$content.executeActions(trigger);
                    TriggerIfContentAvailable.this.isTriggerExecuted = true;
                }
                this.val$actionContext.mBus.unregister(this);
            }
        }

        private TriggerIfContentAvailable(MarketingContentActionContext actionContext, String type, JsonObject params) {
            super(actionContext, type, params);
            this.isTriggerExecuted = false;
        }

        public void execute(MarketingContent content) {
            MarketingContentActionContext actionContext = (MarketingContentActionContext) getActionContext();
            this.mContent = content;
            long timeoutMs = 0;
            try {
                JsonObject conditionalParameters = optParamJsonObject(CONDITION_PARAMETERS).getAsJsonObject();
                this.mConditionalContentID = conditionalParameters.get(ID).getAsString();
                timeoutMs = conditionalParameters.get(TIMEOUT_MS).getAsLong();
            } catch (NullPointerException e) {
                actionContext.mLogger.m577e(getClass().getSimpleName(), e, "Action execution failed actionType=" + getType() + " invalid CONDITION_PARAMETERS", new Object[0]);
            }
            String trigger;
            if (this.mConditionalContentID != null) {
                MarketingContent conditionalContent = (MarketingContent) actionContext.mContentStore.get(this.mConditionalContentID);
                if (conditionalContent == null || !conditionalContent.isLoaded()) {
                    actionContext.mBus.register(this);
                    this.mSubscription = actionContext.mMainWorker.schedule(new C11871(content, actionContext), timeoutMs, TimeUnit.MILLISECONDS);
                } else {
                    trigger = optParamString(THEN_TRIGGER);
                    if (!(TextUtils.isEmpty(trigger) || this.isTriggerExecuted)) {
                        content.executeActions(trigger);
                        this.isTriggerExecuted = true;
                    }
                }
            } else {
                trigger = optParamString(ELSE_TRIGGER);
                if (!(TextUtils.isEmpty(trigger) || this.isTriggerExecuted)) {
                    content.executeActions(trigger);
                    this.isTriggerExecuted = true;
                }
            }
            content.signalActionCompleted(((MarketingContentActionContext) getActionContext()).mBus);
        }

        @Subscribe
        public void handleAvailabilityEvent(ContentLoadedEvent event) {
            if (event.getId().equals(this.mConditionalContentID)) {
                this.mSubscription.unsubscribe();
                ((MarketingContentActionContext) getActionContext()).mBus.unregister(this);
                String trigger = optParamString(THEN_TRIGGER);
                if (!TextUtils.isEmpty(trigger) && !this.isTriggerExecuted) {
                    this.mContent.executeActions(trigger);
                    this.isTriggerExecuted = true;
                }
            }
        }
    }

    static class TriggerIfContentBuilt extends Action<MarketingContent, MarketingContentActionContext> {
        public static final String CONDITION_PARAMETERS = "condition_parameters";
        public static final String CONTENT_MODEL = "content_model";
        public static final String CONTENT_PROVIDER = "content_provider";
        public static final String CONTENT_PROVIDER_NAME = "name";
        public static final String ELSE_TRIGGER = "else_trigger";
        public static final String THEN_TRIGGER = "then_trigger";

        private TriggerIfContentBuilt(MarketingContentActionContext actionContext, String type, JsonObject params) {
            super(actionContext, type, params);
        }

        public void execute(MarketingContent content) {
            boolean isContentBuilt = false;
            MarketingContentActionContext actionContext = (MarketingContentActionContext) getActionContext();
            UpsightContentMediator mediator = null;
            JsonElement contentProvider = optParamJsonObject(CONDITION_PARAMETERS).get(CONTENT_PROVIDER);
            if (contentProvider != null) {
                JsonElement contentProviderName = contentProvider.getAsJsonObject().get(CONTENT_PROVIDER_NAME);
                if (contentProviderName.isJsonPrimitive() && contentProviderName.getAsJsonPrimitive().isString()) {
                    mediator = actionContext.mContentMediatorManager.getContentMediator(contentProviderName.getAsString());
                }
            }
            if (mediator == null) {
                mediator = actionContext.mContentMediatorManager.getDefaultContentMediator();
            }
            content.setContentMediator(mediator);
            JsonElement model = null;
            try {
                model = optParamJsonObject(CONDITION_PARAMETERS).get(CONTENT_MODEL);
            } catch (NullPointerException e) {
                actionContext.mLogger.m577e(getClass().getSimpleName(), e, "Action execution failed actionType=" + getType() + " invalid CONDITION_PARAMETERS", new Object[0]);
            }
            if (model == null || !model.isJsonObject()) {
                actionContext.mLogger.m576e(getClass().getSimpleName(), "Action execution failed actionType=" + getType() + " model=" + model, new Object[0]);
            } else {
                try {
                    actionContext.mContentStore.put(content.getId(), content);
                    Object contentModel = mediator.buildContentModel(content, actionContext, model.getAsJsonObject());
                    isContentBuilt = contentModel != null;
                    content.setContentModel(contentModel);
                    if (isContentBuilt) {
                        content.setContentView(mediator.buildContentView(content, actionContext));
                    }
                } catch (Exception e2) {
                    actionContext.mLogger.m577e(getClass().getSimpleName(), e2, "Action execution failed actionType=" + getType() + " model=" + model, new Object[0]);
                }
            }
            String trigger;
            if (isContentBuilt) {
                trigger = optParamString(THEN_TRIGGER);
                if (!TextUtils.isEmpty(trigger)) {
                    content.executeActions(trigger);
                }
            } else {
                trigger = optParamString(ELSE_TRIGGER);
                if (!TextUtils.isEmpty(trigger)) {
                    content.executeActions(trigger);
                }
            }
            content.signalActionCompleted(((MarketingContentActionContext) getActionContext()).mBus);
        }
    }

    private MarketingContentActions() {
    }

    static {
        FACTORY_MAP = new C11851();
    }
}
