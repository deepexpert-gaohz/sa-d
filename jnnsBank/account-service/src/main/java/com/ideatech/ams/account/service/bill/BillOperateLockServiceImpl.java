package com.ideatech.ams.account.service.bill;

import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.account.dao.bill.BillOperateLockDao;
import com.ideatech.ams.account.dto.bill.BillOperateLockDTO;
import com.ideatech.ams.account.entity.bill.BillOperateLock;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.ams.system.user.service.UserService;
import com.ideatech.common.msg.ObjectRestResponse;
import com.ideatech.common.util.BeanCopierUtils;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

@Service
@Transactional
@Slf4j
public class BillOperateLockServiceImpl implements BillOperateLockService {

    @Autowired
    private BillOperateLockDao billOperateLockDao;

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationService organizationService;

    /**
     * 上锁
     *
     * @param billId
     * @return
     */
    @Override
    public void billLock(Long billId) throws Exception {
        BillOperateLock bol = billOperateLockDao.findByBillId(billId);
        //若有其他人员已经在操作该流水 则上锁失败
        JSONObject jsonObject = this.getBillIsBusy(bol);
        if (jsonObject != null) {
            throw new Exception(jsonObject.get("organName") + "机构" + jsonObject.get("userName") + "用户正在进行该笔业务，业务流水上锁失败！");
        }
        //上锁
        if (bol != null) {
            bol.setLastUpdateBy(String.valueOf(SecurityUtils.getCurrentUser().getId()));
            bol.setLastUpdateDate(new Date());
            billOperateLockDao.save(bol);//更新最后修改人员和修改时间
        } else {
            bol = new BillOperateLock();
            bol.setBillId(billId);
            billOperateLockDao.save(bol);//新增流水上锁数据
        }
    }

    /**
     * 解锁
     *
     * @param billId
     * @return
     */
    @Override
    public void billUnLock(Long billId) {
        billOperateLockDao.deleteByBillId(billId);
    }

    /**
     * 判断指定流水有没有人员正在操作
     *
     * @param billId 流水id
     */
    @Override
    public JSONObject getBillIsBusy(Long billId) {
        BillOperateLock bol = billOperateLockDao.findByBillId(billId);
        return getBillIsBusy(bol);
    }

    private JSONObject getBillIsBusy(BillOperateLock bol) {
        if (bol != null && (new Date().getTime() - bol.getLastUpdateDate().getTime()) < (1000 * 60 * 10)) { //判断该上锁是否超时(10分钟)
            //获取当前登录人员id
            Long userId = Objects.requireNonNull(SecurityUtils.getCurrentUser()).getId();
            if (!bol.getLastUpdateBy().equals(userId.toString())) {//当前登录人员与该流水最后操作人员不同
                UserDto user = userService.findById(Long.valueOf(bol.getLastUpdateBy()));
                OrganizationDto organ = organizationService.findById(user.getOrgId());
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("organName", organ.getName());
                jsonObject.put("userName", user.getCname());
                return jsonObject;
            }
        }
        return null;
    }

    @Override
    public BillOperateLockDTO getByBillId(Long billId) {
        BillOperateLockDTO bold = new BillOperateLockDTO();
        BillOperateLock bol = billOperateLockDao.findByBillId(billId);
        if (bol == null) {
            return null;
        } else {
            BeanCopierUtils.copyProperties(bol, bold);
            return bold;
        }
    }

}
