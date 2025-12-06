package com.microservice.shoppingCart.application.dto.request;

import java.util.List;

public class CreateCartRequest {
    private String externalUserProfilesId;
    private String sessionId;
    private String statusName; // simple representation
    private String currency;
    private String notes;
    private List<AddItemRequest> items;

    public CreateCartRequest() {}

    // getters / setters
    public String getExternalUserProfilesId() { return externalUserProfilesId; }
    public void setExternalUserProfilesId(String externalUserProfilesId) { this.externalUserProfilesId = externalUserProfilesId; }
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    public String getStatusName() { return statusName; }
    public void setStatusName(String statusName) { this.statusName = statusName; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public List<AddItemRequest> getItems() { return items; }
    public void setItems(List<AddItemRequest> items) { this.items = items; }
}
