package com.ideatech.ams.risk.initializer;

import com.ideatech.ams.risk.modelKind.dao.RiskTypeDao;
import com.ideatech.ams.risk.modelKind.entity.RiskType;
import com.ideatech.common.initializer.AbstractDataInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RiskTypelInitializer extends AbstractDataInitializer {

    @Autowired
    RiskTypeDao riskTypeDao;

    @Override
    protected void doInit() throws Exception {
//        createRiskType(1001,"交易风险监测","交易风险监测","-1");
        createRiskType(1002,"开户风险检测","开户风险检测","-1");
        createRiskType(1003,"对账风险监测","对账风险监测","-1");
    }

    public void createRiskType(long id,String typeName,String remakes, String parentId){
        RiskType riskType = new RiskType ();
        riskType.setId ( id );
        riskType.setTypeName(typeName);
        riskType.setRemakes(remakes);
        riskType.setParentId(parentId);
        riskTypeDao.save(riskType);
    }

    @Override
    protected boolean isNeedInit() {
        return riskTypeDao.count() < 1;
    }

    @Override
    public Integer getIndex() {
        return -200;
    }
}
