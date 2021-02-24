package com.ideatech.ams.account.service;

import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.account.dao.SyncHistoryPoDao;
import com.ideatech.ams.account.dao.spec.SyncHistoryPoSpec;
import com.ideatech.ams.account.dto.SyncHistoryDto;
import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.entity.SyncHistoryPo;
import com.ideatech.ams.account.enums.bill.CompanySyncStatus;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.pbc.enums.EAccountType;
import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.ams.system.user.service.UserService;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseServiceImpl;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
@Transactional
@Slf4j
public class SyncHistoryServiceImpl extends BaseServiceImpl<SyncHistoryPoDao, SyncHistoryPo, SyncHistoryDto> implements SyncHistoryService {
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private UserService userService;
    @Override
    public TableResultResponse query(SyncHistoryDto syncHistoryDto, Pageable pageable) {
        SecurityUtils.UserInfo current = SecurityUtils.getCurrentUser();
        OrganizationDto organizationDto = organizationService.findById(current.getOrgId());
        syncHistoryDto.setOrganFullId(organizationDto.getFullId());
        Page<SyncHistoryPo> data = getBaseDao().findAll(new SyncHistoryPoSpec(syncHistoryDto),pageable);
        return new TableResultResponse((int)data.getTotalElements(), data.getContent());
    }

    @Override
    public void write(AllBillsPublicDTO billsPublic, EAccountType syncType, CompanySyncStatus syncStatus, String errorMessage) {
        try {
            SecurityUtils.UserInfo current = SecurityUtils.getCurrentUser();
            Long userId;
            if(current!=null){
                userId = current.getId();
            }else{
                userId = 2l;
            }
            UserDto userDto = userService.findById(userId);
            OrganizationDto organizationDto ;
            if(StringUtils.isNotBlank(billsPublic.getOrganCode())){
                organizationDto = organizationService.findByCode(billsPublic.getOrganCode());
            }else{
                if(current==null){
                    organizationDto = organizationService.findById(userDto.getOrgId());
                }else{
                    organizationDto = organizationService.findById(current.getOrgId());
                }
            }

            SyncHistoryPo syncHistoryPo = new SyncHistoryPo();
            //设置上报系统
            syncHistoryPo.setSyncType(syncType);
            //设置账号
            syncHistoryPo.setAcctNo(billsPublic.getAcctNo());
            //设置机构名称
            syncHistoryPo.setOrganName(organizationDto.getName());
            //设置机构代码
            syncHistoryPo.setOrganCode(organizationDto.getCode());
            //设置人行机构代码
            syncHistoryPo.setBankCode(organizationDto.getPbcCode());
            //设置完整fullid
            syncHistoryPo.setOrganFullId(organizationDto.getFullId());
            //设置账户类别
            syncHistoryPo.setAcctType(billsPublic.getAcctType());
            //设置单据类型
            syncHistoryPo.setBillType(billsPublic.getBillType());
            //设置报送人
            syncHistoryPo.setSyncName(userDto.getCname());
            //设置报送时间
            syncHistoryPo.setSyncDateTime(DateFormatUtils.format(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss"));
            //设置关联单据id
            syncHistoryPo.setRefBillId(billsPublic.getRefBillId());
            //设置上报结果
            syncHistoryPo.setSyncStatus(syncStatus);
            //设置上报失败原因
            syncHistoryPo.setFailMsg(errorMessage);
            getBaseDao().save(syncHistoryPo);
        }catch (Exception e){
            log.error("记录上报日志异常：{}",e);
        }
    }

    @Override
    public JSONObject getSyncErrorMsg(Long refBillId) {
        JSONObject json = new JSONObject();

        SyncHistoryPo syncHistoryPo1 = getBaseDao().findFirstByRefBillIdAndSyncStatusAndSyncTypeOrderBySyncDateTimeDesc(refBillId, CompanySyncStatus.tongBuShiBai, EAccountType.AMS);
        SyncHistoryPo syncHistoryPo2 = getBaseDao().findFirstByRefBillIdAndSyncStatusAndSyncTypeOrderBySyncDateTimeDesc(refBillId, CompanySyncStatus.tongBuShiBai, EAccountType.ECCS);
        SyncHistoryPo syncHistoryPo3 = getBaseDao().findFirstByRefBillIdAndSyncStatusAndSyncTypeOrderBySyncDateTimeDesc(refBillId, CompanySyncStatus.tongBuShiBai, EAccountType.IMS);

        json.put("pbcFailMsg", syncHistoryPo1 == null ? "" : syncHistoryPo1.getFailMsg());
        json.put("eccsFailMsg", syncHistoryPo2 == null ? "" : syncHistoryPo2.getFailMsg());
        json.put("IMSFailMsg", syncHistoryPo3 == null ? "" : syncHistoryPo3.getFailMsg());
        return json;
    }
}
