package com.company.retail.models;
/**
 * @author omkar
 * @Description Just an indicator to notify 
 */

public class ServiceResponseModel {

    private Boolean successful;

    public ServiceResponseModel(Boolean successful){
        this.successful = successful;
    }

    public Boolean getSuccessful() {
        return successful;
    }

    public void setSuccessful(Boolean successful) {
        this.successful = successful;
    }

}
