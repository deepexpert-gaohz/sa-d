package com.ideatech.ams.risk.customerTransaction.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name="business_changes")
public class BusinessChanges extends BaseMaintainablePo {
    private String name;
    private String changesType;
    private String changesBeforeContent;
    private String changesAfterContent;
    private String changesDate;
}
