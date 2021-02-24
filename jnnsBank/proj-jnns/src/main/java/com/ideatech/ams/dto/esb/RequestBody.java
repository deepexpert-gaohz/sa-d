package com.ideatech.ams.dto.esb;

import lombok.Data;

@Data
public class RequestBody {
    private  Transaction Transaction;

    public Transaction getTransaction() {
        return Transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.Transaction = transaction;
    }
}
