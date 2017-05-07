package com.upsight.android.managedvariables.internal.type;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.squareup.otto.Bus;
import com.upsight.android.Upsight;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightException;
import com.upsight.android.UpsightManagedVariablesExtension;
import com.upsight.android.analytics.event.uxm.UpsightUxmEnumerateEvent;
import com.upsight.android.analytics.internal.action.Action;
import com.upsight.android.analytics.internal.action.ActionContext;
import com.upsight.android.analytics.internal.action.ActionFactory;
import com.upsight.android.analytics.internal.session.Clock;
import com.upsight.android.internal.util.PreferencesHelper;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.managedvariables.UpsightManagedVariablesComponent;
import com.upsight.android.persistence.UpsightDataStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import rx.Observable;
import rx.Scheduler.Worker;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

public final class UxmContentActions {
    private static final Map<String, InternalFactory> FACTORY_MAP;

    private interface InternalFactory {
        Action<UxmContent, UxmContentActionContext> create(UxmContentActionContext uxmContentActionContext, String str, JsonObject jsonObject);
    }

    /* renamed from: com.upsight.android.managedvariables.internal.type.UxmContentActions.1 */
    static class C11471 extends HashMap<String, InternalFactory> {

        /* renamed from: com.upsight.android.managedvariables.internal.type.UxmContentActions.1.1 */
        class C11421 implements InternalFactory {
            C11421() {
            }

            public Action<UxmContent, UxmContentActionContext> create(UxmContentActionContext actionContext, String actionType, JsonObject actionParams) {
                return new UxmEnumerate(actionType, actionParams, null);
            }
        }

        /* renamed from: com.upsight.android.managedvariables.internal.type.UxmContentActions.1.2 */
        class C11432 implements InternalFactory {
            C11432() {
            }

            public Action<UxmContent, UxmContentActionContext> create(UxmContentActionContext actionContext, String actionType, JsonObject actionParams) {
                return new SetBundleId(actionType, actionParams, null);
            }
        }

        /* renamed from: com.upsight.android.managedvariables.internal.type.UxmContentActions.1.3 */
        class C11443 implements InternalFactory {
            C11443() {
            }

            public Action<UxmContent, UxmContentActionContext> create(UxmContentActionContext actionContext, String actionType, JsonObject actionParams) {
                return new ModifyValue(actionType, actionParams, null);
            }
        }

        /* renamed from: com.upsight.android.managedvariables.internal.type.UxmContentActions.1.4 */
        class C11454 implements InternalFactory {
            C11454() {
            }

            public Action<UxmContent, UxmContentActionContext> create(UxmContentActionContext actionContext, String actionType, JsonObject actionParams) {
                return new NotifyUxmValuesSynchronized(actionType, actionParams, null);
            }
        }

        /* renamed from: com.upsight.android.managedvariables.internal.type.UxmContentActions.1.5 */
        class C11465 implements InternalFactory {
            C11465() {
            }

            public Action<UxmContent, UxmContentActionContext> create(UxmContentActionContext actionContext, String actionType, JsonObject actionParams) {
                return new Destroy(actionType, actionParams, null);
            }
        }

        C11471() {
            put("action_uxm_enumerate", new C11421());
            put("action_set_bundle_id", new C11432());
            put("action_modify_value", new C11443());
            put("action_notify_uxm_values_synchronized", new C11454());
            put("action_destroy", new C11465());
        }
    }

    static class Destroy extends Action<UxmContent, UxmContentActionContext> {
        private Destroy(UxmContentActionContext actionContext, String type, JsonObject params) {
            super(actionContext, type, params);
        }

        public void execute(UxmContent content) {
            Bus bus = ((UxmContentActionContext) getActionContext()).mBus;
            content.signalActionCompleted(bus);
            content.signalActionMapCompleted(bus);
        }
    }

    static class ModifyValue extends Action<UxmContent, UxmContentActionContext> {
        private static final String MATCH = "match";
        private static final String OPERATOR = "operator";
        private static final String OPERATOR_SET = "set";
        private static final String PROPERTY_NAME = "property_name";
        private static final String PROPERTY_VALUE = "property_value";
        private static final String TYPE = "type";
        private static final String VALUES = "values";

        /* renamed from: com.upsight.android.managedvariables.internal.type.UxmContentActions.ModifyValue.1 */
        class C11481 implements Func1<T, JsonElement> {
            final /* synthetic */ Gson val$gson;

            C11481(Gson gson) {
                this.val$gson = gson;
            }

            public JsonElement call(T model) {
                return this.val$gson.toJsonTree(model);
            }
        }

        /* renamed from: com.upsight.android.managedvariables.internal.type.UxmContentActions.ModifyValue.2 */
        class C11492 implements Func1<JsonObject, Boolean> {
            final /* synthetic */ String val$propertyName;
            final /* synthetic */ JsonElement val$propertyValue;

            C11492(String str, JsonElement jsonElement) {
                this.val$propertyName = str;
                this.val$propertyValue = jsonElement;
            }

            public Boolean call(JsonObject model) {
                return Boolean.valueOf(model.getAsJsonObject().get(this.val$propertyName).equals(this.val$propertyValue));
            }
        }

        /* renamed from: com.upsight.android.managedvariables.internal.type.UxmContentActions.ModifyValue.3 */
        class C11503 implements Func1<JsonObject, JsonObject> {
            final /* synthetic */ String val$propertyName;
            final /* synthetic */ JsonElement val$propertyValue;

            C11503(String str, JsonElement jsonElement) {
                this.val$propertyName = str;
                this.val$propertyValue = jsonElement;
            }

            public JsonObject call(JsonObject model) {
                model.add(this.val$propertyName, this.val$propertyValue);
                return model;
            }
        }

        /* renamed from: com.upsight.android.managedvariables.internal.type.UxmContentActions.ModifyValue.4 */
        class C11544 implements Action1<JsonObject> {
            final /* synthetic */ UxmContentActionContext val$actionContext;
            final /* synthetic */ Class val$clazz;
            final /* synthetic */ UxmContent val$content;
            final /* synthetic */ UpsightDataStore val$dataStore;
            final /* synthetic */ Gson val$gson;
            final /* synthetic */ UpsightLogger val$logger;

            /* renamed from: com.upsight.android.managedvariables.internal.type.UxmContentActions.ModifyValue.4.1 */
            class C11511 implements Action1<T> {
                final /* synthetic */ JsonObject val$modelNode;

                C11511(JsonObject jsonObject) {
                    this.val$modelNode = jsonObject;
                }

                public void call(T t) {
                    C11544.this.val$logger.m574d(Upsight.LOG_TAG, "Modified managed variable of class " + C11544.this.val$clazz + " with value " + this.val$modelNode, new Object[0]);
                }
            }

            /* renamed from: com.upsight.android.managedvariables.internal.type.UxmContentActions.ModifyValue.4.2 */
            class C11522 implements Action1<Throwable> {
                C11522() {
                }

                public void call(Throwable t) {
                    C11544.this.val$logger.m577e(Upsight.LOG_TAG, t, "Failed to modify managed variable of class " + C11544.this.val$clazz, new Object[0]);
                }
            }

            /* renamed from: com.upsight.android.managedvariables.internal.type.UxmContentActions.ModifyValue.4.3 */
            class C11533 implements Action0 {
                C11533() {
                }

                public void call() {
                    C11544.this.val$content.signalActionCompleted(C11544.this.val$actionContext.mBus);
                }
            }

            C11544(UpsightDataStore upsightDataStore, Gson gson, Class cls, UxmContentActionContext uxmContentActionContext, UpsightLogger upsightLogger, UxmContent uxmContent) {
                this.val$dataStore = upsightDataStore;
                this.val$gson = gson;
                this.val$clazz = cls;
                this.val$actionContext = uxmContentActionContext;
                this.val$logger = upsightLogger;
                this.val$content = uxmContent;
            }

            public void call(JsonObject modelNode) {
                try {
                    this.val$dataStore.storeObservable(this.val$gson.fromJson((JsonElement) modelNode, this.val$clazz)).subscribeOn(this.val$actionContext.mUpsight.getCoreComponent().subscribeOnScheduler()).observeOn(this.val$actionContext.mUpsight.getCoreComponent().observeOnScheduler()).subscribe(new C11511(modelNode), new C11522(), new C11533());
                } catch (JsonSyntaxException e) {
                    this.val$logger.m577e(Upsight.LOG_TAG, e, "Failed to parse managed variable of class " + this.val$clazz, new Object[0]);
                    this.val$content.signalActionCompleted(this.val$actionContext.mBus);
                }
            }
        }

        /* renamed from: com.upsight.android.managedvariables.internal.type.UxmContentActions.ModifyValue.5 */
        class C11555 implements Action1<Throwable> {
            final /* synthetic */ UxmContentActionContext val$actionContext;
            final /* synthetic */ Class val$clazz;
            final /* synthetic */ UxmContent val$content;
            final /* synthetic */ UpsightLogger val$logger;

            C11555(UpsightLogger upsightLogger, Class cls, UxmContent uxmContent, UxmContentActionContext uxmContentActionContext) {
                this.val$logger = upsightLogger;
                this.val$clazz = cls;
                this.val$content = uxmContent;
                this.val$actionContext = uxmContentActionContext;
            }

            public void call(Throwable throwable) {
                this.val$logger.m577e(Upsight.LOG_TAG, throwable, "Failed to fetch managed variable of class " + this.val$clazz, new Object[0]);
                this.val$content.signalActionCompleted(this.val$actionContext.mBus);
            }
        }

        private ModifyValue(UxmContentActionContext actionContext, String type, JsonObject params) {
            super(actionContext, type, params);
        }

        public void execute(UxmContent content) {
            boolean isSync = true;
            ActionContext actionContext = getActionContext();
            if (content.shouldApplyBundle()) {
                String type = optParamString(TYPE);
                JsonArray matchers = optParamJsonArray(MATCH);
                JsonArray values = optParamJsonArray(VALUES);
                if (!(TextUtils.isEmpty(type) || matchers == null || values == null)) {
                    Class<?> clazz = null;
                    if ("com.upsight.uxm.string".equals(type)) {
                        clazz = Model.class;
                    } else if ("com.upsight.uxm.boolean".equals(type)) {
                        clazz = Model.class;
                    } else if ("com.upsight.uxm.integer".equals(type)) {
                        clazz = Model.class;
                    } else if ("com.upsight.uxm.float".equals(type)) {
                        clazz = Model.class;
                    }
                    if (clazz != null) {
                        modifyValue(content, clazz, matchers, values);
                        isSync = false;
                    } else {
                        actionContext.mLogger.m576e(Upsight.LOG_TAG, "Failed to execute action_modify_value due to unknown managed variable type " + type, new Object[0]);
                    }
                }
            }
            if (isSync) {
                content.signalActionCompleted(actionContext.mBus);
            }
        }

        private <T> void modifyValue(UxmContent content, Class<T> clazz, JsonArray matchers, JsonArray values) {
            UxmContentActionContext actionContext = (UxmContentActionContext) getActionContext();
            Gson gson = actionContext.mGson;
            UpsightLogger logger = actionContext.mUpsight.getLogger();
            UpsightDataStore dataStore = actionContext.mUpsight.getDataStore();
            Observable<JsonObject> fetchObservable = dataStore.fetchObservable(clazz).map(new C11481(gson)).cast(JsonObject.class);
            JsonObject seedNode = new JsonObject();
            Iterator it = matchers.iterator();
            while (it.hasNext()) {
                JsonElement matcher = (JsonElement) it.next();
                String propertyName = matcher.getAsJsonObject().get(PROPERTY_NAME).getAsString();
                JsonElement propertyValue = matcher.getAsJsonObject().get(PROPERTY_VALUE);
                fetchObservable = fetchObservable.filter(new C11492(propertyName, propertyValue));
                seedNode.add(propertyName, propertyValue);
            }
            fetchObservable = fetchObservable.defaultIfEmpty(seedNode);
            it = values.iterator();
            while (it.hasNext()) {
                JsonElement value = (JsonElement) it.next();
                String operator = value.getAsJsonObject().get(OPERATOR).getAsString();
                propertyName = value.getAsJsonObject().get(PROPERTY_NAME).getAsString();
                propertyValue = value.getAsJsonObject().get(PROPERTY_VALUE);
                if (OPERATOR_SET.equals(operator)) {
                    fetchObservable = fetchObservable.map(new C11503(propertyName, propertyValue));
                }
            }
            Observable observeOn = fetchObservable.subscribeOn(actionContext.mUpsight.getCoreComponent().subscribeOnScheduler()).observeOn(actionContext.mUpsight.getCoreComponent().observeOnScheduler());
            r23.subscribe(new C11544(dataStore, gson, clazz, actionContext, logger, content), new C11555(logger, clazz, content, actionContext));
        }
    }

    static class NotifyUxmValuesSynchronized extends Action<UxmContent, UxmContentActionContext> {
        private static final String TAGS = "tags";

        private NotifyUxmValuesSynchronized(UxmContentActionContext actionContext, String type, JsonObject params) {
            super(actionContext, type, params);
        }

        public void execute(UxmContent content) {
            List<String> synchronizedTags = new ArrayList();
            JsonArray tagNodes = optParamJsonArray(TAGS);
            if (content.shouldApplyBundle() && tagNodes != null) {
                Iterator it = tagNodes.iterator();
                while (it.hasNext()) {
                    JsonElement tagNode = (JsonElement) it.next();
                    if (tagNode.isJsonPrimitive() && tagNode.getAsJsonPrimitive().isString()) {
                        synchronizedTags.add(tagNode.getAsString());
                    }
                }
            }
            Bus bus = ((UxmContentActionContext) getActionContext()).mBus;
            bus.post(new ScheduleSyncNotificationEvent(synchronizedTags, null));
            content.signalActionCompleted(bus);
        }
    }

    public static class ScheduleSyncNotificationEvent {
        public final String mId;
        public final List<String> mTags;

        private ScheduleSyncNotificationEvent(String id, List<String> tags) {
            this.mId = id;
            this.mTags = tags;
        }
    }

    static class SetBundleId extends Action<UxmContent, UxmContentActionContext> {
        private static final String BUNDLE_ID = "bundle.id";

        private SetBundleId(UxmContentActionContext actionContext, String type, JsonObject params) {
            super(actionContext, type, params);
        }

        public void execute(UxmContent content) {
            if (content.shouldApplyBundle()) {
                PreferencesHelper.putString(((UxmContentActionContext) getActionContext()).mUpsight, UxmContent.PREFERENCES_KEY_UXM_BUNDLE_ID, optParamString(BUNDLE_ID));
            }
            content.signalActionCompleted(((UxmContentActionContext) getActionContext()).mBus);
        }
    }

    public static class UxmContentActionContext extends ActionContext {
        public UxmContentActionContext(UpsightContext upsight, Bus bus, Gson gson, Clock clock, Worker mainWorker, UpsightLogger logger) {
            super(upsight, bus, gson, clock, mainWorker, logger);
        }
    }

    public static class UxmContentActionFactory implements ActionFactory<UxmContent, UxmContentActionContext> {
        public static final String TYPE = "datastore_factory";

        public Action<UxmContent, UxmContentActionContext> create(UxmContentActionContext actionContext, JsonObject actionJSON) throws UpsightException {
            if (actionJSON == null) {
                throw new UpsightException("Failed to create Action. JSON is null.", new Object[0]);
            }
            String actionType = actionJSON.get(ActionFactory.KEY_ACTION_TYPE).getAsString();
            JsonObject actionParams = actionJSON.getAsJsonObject(ActionFactory.KEY_ACTION_PARAMS);
            InternalFactory factory = (InternalFactory) UxmContentActions.FACTORY_MAP.get(actionType);
            if (factory != null) {
                return factory.create(actionContext, actionType, actionParams);
            }
            throw new UpsightException("Failed to create Action. Unknown action type.", new Object[0]);
        }
    }

    static class UxmEnumerate extends Action<UxmContent, UxmContentActionContext> {
        private UxmEnumerate(UxmContentActionContext actionContext, String type, JsonObject params) {
            super(actionContext, type, params);
        }

        public void execute(UxmContent content) {
            ActionContext actionContext = getActionContext();
            UpsightManagedVariablesExtension extension = (UpsightManagedVariablesExtension) actionContext.mUpsight.getUpsightExtension(UpsightManagedVariablesExtension.EXTENSION_NAME);
            if (extension != null) {
                try {
                    UpsightUxmEnumerateEvent.createBuilder(new JSONArray(((UpsightManagedVariablesComponent) extension.getComponent()).uxmSchema().mSchemaJsonString)).record(actionContext.mUpsight);
                } catch (JSONException e) {
                    actionContext.mUpsight.getLogger().m577e(Upsight.LOG_TAG, e, "Failed to send UXM enumerate event", new Object[0]);
                }
            }
            content.signalActionCompleted(actionContext.mBus);
        }
    }

    private UxmContentActions() {
    }

    static {
        FACTORY_MAP = new C11471();
    }
}
