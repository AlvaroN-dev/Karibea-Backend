package com.microservice.shoppingCart.application.dto.request;


public class UpdateStatusRequest {
    private String statusName;

    public UpdateStatusRequest() {}

    public String getStatusName() { return statusName; }
    public void setStatusName(String statusName) { this.statusName = statusName; }
}
