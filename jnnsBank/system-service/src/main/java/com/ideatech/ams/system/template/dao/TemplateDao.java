package com.ideatech.ams.system.template.dao;

import com.ideatech.ams.system.template.entity.TemplatePo;
import com.ideatech.common.enums.BillType;
import com.ideatech.common.enums.DepositorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TemplateDao extends JpaRepository<TemplatePo, Long>, JpaSpecificationExecutor<TemplatePo> {
    @Query("SELECT templateName FROM TemplatePo WHERE billType=?1 AND depositorType=?2")
    List<String> listTemplateNameByBillTypeAndDepositorType(BillType billType, DepositorType depositorType);

    List<TemplatePo> findByBillTypeAndDepositorTypeAndTemplateName(BillType billType, DepositorType depositorType, String templateName);

    List<TemplatePo> findByTemplateName(String templateName);

    List<TemplatePo> findByBillTypeAndDepositorType(BillType billType, DepositorType depositorType);

}
