package com.ideatech.ams.ws.api.service;


import com.ideatech.ams.customer.dto.CustomerTuneSearchHistoryDto;
import com.ideatech.ams.customer.enums.CustomerTuneSearchEntranceType;
import com.ideatech.ams.customer.enums.CustomerTuneSearchType;
import com.ideatech.ams.customer.service.CustomerTuneSearchHistoryService;
import com.ideatech.ams.kyc.dto.CarrierOperatorDto;
import com.ideatech.ams.kyc.service.CarrierOperatorService;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.user.dao.UserDao;
import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.ams.system.user.service.UserService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author liangding
 * @create 2018-09-14 下午9:24
 **/

@Slf4j
@Service
public class TeleCarrierVerifyServiceImpl implements TeleCarrierVerifyService {
    @Autowired
    private CarrierOperatorService carrierOperatorService;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerTuneSearchHistoryService customerTuneSearchHistoryService;

    @Autowired
    private OrganizationService organizationService;

    @Value("${ams.customerManagerSearch.verifyCarrierOperator.use:false}")
    private Boolean verifyCarrierOperatorFlag;

    @Override
    public ResultDto verify(String name, String idNo, String mobile) {
        return verifyCommon(name, idNo, mobile, "");
    }

    @Override
    public ResultDto verify(String name, String idNo, String mobile, String username) {
        if(StringUtils.isBlank(username)) {
            ResultDtoFactory.toNack("用户名不能为空");
        }
        UserDto userDto = userService.findByUsername(username);
        if(userDto == null) {
            return ResultDtoFactory.toNack("用户名不存在");
        }

        return verifyCommon(name, idNo, mobile, username);
    }

    private ResultDto verifyCommon(String name, String idNo, String mobile, String username) {
        CarrierOperatorDto carrierOperatorDto = new CarrierOperatorDto();
        carrierOperatorDto.setName(name);
        carrierOperatorDto.setCardno(idNo);
        carrierOperatorDto.setMobile(mobile);
        CarrierOperatorDto carrierOperatorResult = carrierOperatorService.getCarrierOperatorResult(carrierOperatorDto, username);

        if (verifyCarrierOperatorFlag){
            UserDto userDto = userService.findByUsername(name);
            if (userDto==null){
                //纯接口调用使用虚拟用户
                userDto = userService.findById(2L);
            }

            // 接口模式下，记录查询。
            try {
                CustomerTuneSearchHistoryDto customerTuneSearchHistoryDto = new CustomerTuneSearchHistoryDto();
                customerTuneSearchHistoryDto.setName(name);
                customerTuneSearchHistoryDto.setMobile(mobile);
                customerTuneSearchHistoryDto.setCardNo(idNo);
                customerTuneSearchHistoryDto.setResult(carrierOperatorResult.getResult());

                customerTuneSearchHistoryDto.setType(CustomerTuneSearchType.VERIFY_CARRIER_OPERATOR);
                customerTuneSearchHistoryDto.setEntranceType(CustomerTuneSearchEntranceType.INTERFACE_VERIFY_CARRIER_OPERATOR);
                customerTuneSearchHistoryDto.setOrganFullId(organizationService.findById(userDto.getOrgId()).getFullId());
                customerTuneSearchHistoryDto.setOrganId(userDto.getOrgId());
                customerTuneSearchHistoryDto.setCreatedBy(userDto.getId().toString());
                customerTuneSearchHistoryDto.setLastUpdateBy(userDto.getId().toString());

                customerTuneSearchHistoryService.save(customerTuneSearchHistoryDto);
            } catch (Exception e) {
                log.warn("记录运营商校验日志失败", e.getMessage());
            }
        }

        if (StringUtils.equals("success", carrierOperatorResult.getStatus())) {
            return ResultDtoFactory.toAck(carrierOperatorResult.getResult());
        } else {
            String reason = carrierOperatorResult.getReason();
            if (StringUtils.isBlank(reason)) {
                reason = carrierOperatorResult.getResult();
            }
            return ResultDtoFactory.toNack(reason);
        }
    }

}
