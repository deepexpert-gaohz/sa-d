package com.ideatech.ams.account.event;

import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class PushCoreDataEvent extends ApplicationEvent {

    private AllBillsPublicDTO allBillsPublicDTO;

    public PushCoreDataEvent(Object source) {
        super(source);
    }
}
