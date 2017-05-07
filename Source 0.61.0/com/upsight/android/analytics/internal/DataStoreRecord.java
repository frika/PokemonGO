package com.upsight.android.analytics.internal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.persistence.annotation.UpsightStorableIdentifier;
import com.upsight.android.persistence.annotation.UpsightStorableType;

@UpsightStorableType("upsight.datastore.record")
public final class DataStoreRecord {
    @SerializedName("action")
    @Expose
    Action action;
    @SerializedName("campaign_id")
    @Expose
    Integer campaignID;
    @SerializedName("id")
    @UpsightStorableIdentifier
    @Expose
    String id;
    @SerializedName("identifiers")
    @Expose
    String identifiers;
    @SerializedName("message_id")
    @Expose
    Integer messageID;
    @SerializedName("past_session_time")
    @Expose
    Long pastSessionTime;
    @SerializedName("session_id")
    @Expose
    Long sessionID;
    @SerializedName("session_num")
    @Expose
    Integer sessionNumber;
    @SerializedName("source")
    @Expose
    String source;
    @SerializedName("source_type")
    @Expose
    String sourceType;

    public enum Action {
        Created,
        Updated,
        Removed
    }

    public static DataStoreRecord create(Action action, Long sessionID, Integer messageID, Integer campaignID, Integer sessionNumber, Long pastSessionTime, String source, String sourceType) {
        return new DataStoreRecord(action, sessionID, messageID, campaignID, sessionNumber, pastSessionTime, source, sourceType);
    }

    public static DataStoreRecord create(Action action, Long sessionID, Integer sessionNumber, Long pastSessionTime, String source, String sourceType) {
        return create(action, sessionID, null, null, sessionNumber, pastSessionTime, source, sourceType);
    }

    DataStoreRecord() {
    }

    private DataStoreRecord(Action action, Long sessionID, Integer messageID, Integer campaignID, Integer sessionNumber, Long pastSessionTime, String source, String sourceType) {
        this.action = action;
        this.sessionID = sessionID;
        this.messageID = messageID;
        this.campaignID = campaignID;
        this.source = source;
        this.sourceType = sourceType;
        this.sessionNumber = sessionNumber;
        this.pastSessionTime = pastSessionTime;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DataStoreRecord that = (DataStoreRecord) o;
        if (this.id != null) {
            if (this.id.equals(that.id)) {
                return true;
            }
        } else if (that.id == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.id != null ? this.id.hashCode() : 0;
    }

    public String getID() {
        return this.id;
    }

    public Long getSessionID() {
        return this.sessionID;
    }

    public Integer getMessageID() {
        return this.messageID;
    }

    public Integer getCampaignID() {
        return this.campaignID;
    }

    public Integer getSessionNumber() {
        return this.sessionNumber;
    }

    public Long getPastSessionTime() {
        return this.pastSessionTime;
    }

    public Action getAction() {
        return this.action;
    }

    public String getSource() {
        return this.source;
    }

    public String getSourceType() {
        return this.sourceType;
    }

    public void setIdentifiers(String identifiers) {
        this.identifiers = identifiers;
    }

    public String getIdentifiers() {
        return this.identifiers;
    }
}
