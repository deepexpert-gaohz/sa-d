package com.ideatech.ams.risk.initializer;

import com.ideatech.ams.risk.modelKind.dao.RiskLevelDao;
import com.ideatech.ams.risk.modelKind.entity.RiskLevel;
import com.ideatech.common.initializer.AbstractDataInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RiskLevellInitializer extends AbstractDataInitializer {

    @Autowired
    RiskLevelDao riskLevelDao;

    @Override
    protected void doInit() throws Exception {
        createRiskLevel(1001,"高","高","-1");
        createRiskLevel(1002,"中","中","-1");
        createRiskLevel(1003,"低","低","-1");
    }

    public void createRiskLevel(long id,String levelName,String remakes, String parentId){
        RiskLevel riskLevel = new RiskLevel ();
        riskLevel.setId ( id );
        riskLevel.setLevelName(levelName);
        riskLevel.setRemakes(remakes);
        riskLevel.setParentId(parentId);
        riskLevelDao.save(riskLevel);
    }

    @Override
    protected boolean isNeedInit() {
        return riskLevelDao.count() < 1;
    }

    @Override
    public Integer getIndex() {
        return -200;
    }
}
