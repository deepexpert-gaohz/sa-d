package com.ideatech.ams.dto.esb.response;

import lombok.Data;

@Data
public class RBody {
    private String Request;
    private String response;

    public String getRequest() {
        return Request;
    }

    public void setRequest(String request) {
        Request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
