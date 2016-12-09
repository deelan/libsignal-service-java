package org.whispersystems.signalservice.internal.push;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BillingInfo {

    @JsonProperty
    private String id;

    @JsonProperty
    private String name;

    @JsonProperty
    private long created;

    @JsonProperty
    private String stripe_user_id;

    @JsonProperty
    private String token_type;

    @JsonProperty
    private String stripe_publishable_key;

    @JsonProperty
    private String scope;

    @JsonProperty
    private boolean livemode;

    @JsonProperty
    private String refresh_token;

    @JsonProperty
    private String access_token;

    BillingInfo() {}

    public String getTokenType() {
        return token_type;
    }

    public String getPublishableKey() {
        return stripe_publishable_key;
    }

    public String getScope() {
        return scope;
    }

    public boolean isLiveMode() {
        return livemode;
    }

    public String getRefreshToken() {
        return refresh_token;
    }

    public String getAccessToken() {
        return access_token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public String getUserId() {
        return stripe_user_id;
    }
}