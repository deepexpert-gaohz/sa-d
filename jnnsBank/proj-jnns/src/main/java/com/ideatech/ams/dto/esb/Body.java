package com.ideatech.ams.dto.esb;

import com.ideatech.ams.dto.esb.response.Response;
import lombok.Data;

@Data
public class Body {
    @Override
    public String toString() {
        return "Body{" +
                "Request=" + Request +
                ", response=" + response +
                '}';
    }

    private Request Request;
    private Response response;

    public Request getRequest() {
        return Request;
    }

    public void setRequest(Request Request) {
        this.Request = Request;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
