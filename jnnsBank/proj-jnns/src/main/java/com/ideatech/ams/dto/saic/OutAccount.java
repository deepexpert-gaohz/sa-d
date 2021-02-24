package com.ideatech.ams.dto.saic;

import java.util.List;

public class OutAccount {
    private int total;
    List<OutAccountBase> items;


    public int getTotal() {
        return this.total;
    }

    public List<OutAccountBase> getItems() {
        return this.items;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setItems(List<OutAccountBase> items) {
        this.items = items;
    }
}
