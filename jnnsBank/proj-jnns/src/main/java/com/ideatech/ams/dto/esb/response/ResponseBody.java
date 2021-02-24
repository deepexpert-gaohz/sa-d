package com.ideatech.ams.dto.esb.response;

public class ResponseBody {
    @Override
    public String toString() {
        return "ResponseBody{" +
                "transaction='" + transaction + '\'' +
                '}';
    }

    private String transaction;

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }
}
