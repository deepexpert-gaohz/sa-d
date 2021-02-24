package com.ideatech.ams.pbc.spi.sync;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.dto.AmsYibanSyncCondition;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.enums.SyncSystem;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.service.ams.AmsYibanOpenService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 人行账管系统一般户开户接口,用来报备人行账管系统
 *
 * @author zoulang
 */
@Component
public class AmsYibanOpenSynchronizer extends AbstractSynchronizer {

    @Autowired
    AmsYibanOpenService amsYibanOpenService;

    @Override
    protected void doSynchron(SyncSystem syncSystem, LoginAuth auth, AllAcct allAcct) throws SyncException, Exception {
        AmsYibanSyncCondition amsYibanOpenCondition = new AmsYibanSyncCondition();
        BeanUtils.copyProperties(allAcct, amsYibanOpenCondition);
        amsYibanOpenService.openAccountFirstStep(auth, amsYibanOpenCondition);
        allAcct.setRegAreaCode(amsYibanOpenCondition.getRegAreaCode());// 在第一步校验时，地区代码可能发生变化
        amsYibanOpenService.openAccountSecondStep(auth, amsYibanOpenCondition);
        if (!isProEnvironment(allAcct.getAcctNo(), allAcct.getOperateType(), allAcct.getAcctType())) {
            return;
        }
        amsYibanOpenService.openAccountLastStep(auth);
        // 校验人行是否存在
        //睡眠5秒   循环2次   提示友善
        boolean result = false;
        for (int i = 1; i < 3; i++) {
            logger.info("一般户开户成功，5秒后通过账号进行人行校验......");
            Thread.sleep(5000);//停顿5s
            if (checkSyncStatus(auth, allAcct.getAcctNo(), allAcct.getOperateType())) {
                result = true;
                break;
            }
        }
        if (!result) {
            throw new SyncException("账户" + allAcct.getAcctNo() + "报备人行成功,因网络原因未查询到该账号，请线下人工进行查询......");
        }
    }
}
