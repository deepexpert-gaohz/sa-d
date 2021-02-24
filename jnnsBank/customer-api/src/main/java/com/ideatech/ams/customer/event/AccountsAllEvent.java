package com.ideatech.ams.customer.event;

import com.ideatech.ams.customer.dto.CustomerAllResponse;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class AccountsAllEvent extends ApplicationEvent {
    private Long customerLogId;

    private CustomerAllResponse customerAllResponse;

    public AccountsAllEvent(Object source) {
        super(source);
    }
}
