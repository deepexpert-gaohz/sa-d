package com.ideatech.ams.service.impl;

import com.ideatech.ams.account.dao.AccountsAllDao;
import com.ideatech.ams.account.entity.AccountsAll;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.customer.dto.CustomerPublicInfo;
import com.ideatech.ams.customer.entity.CustomerPublic;
import com.ideatech.ams.customer.entity.CustomerPublicLog;
import com.ideatech.ams.customer.service.CustomerPublicLogService;
import com.ideatech.ams.customer.service.CustomerPublicService;
import com.ideatech.ams.dao.ZhjnCustomerDao;
import com.ideatech.ams.dao.spec.SyncCompareSpec;
import com.ideatech.ams.domain.SyncCompare;
import com.ideatech.ams.domain.zhjn.*;
import com.ideatech.ams.dto.SyncCompareInfo;
import com.ideatech.ams.dto.ZhjnCustomerDto;
import com.ideatech.ams.dto.ZhjnCustomerSpec;
import com.ideatech.ams.service.ZhjnCustomerService;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.msg.TableResultResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 江南农商数据初始化服务类
 *
 * @auther ideatech
 * @create 2019-06-17 9:29 AM
 **/
@Service
public class ZhjnCustomerServiceImpl implements ZhjnCustomerService {

    private static final Logger log = LoggerFactory.getLogger(ZhjnCustomerServiceImpl.class);
    @Autowired
    private ZhjnCustomerDao zhjnCustomerDao;
    @Autowired
    private AccountsAllDao accountsAllDao;
    @Autowired
    private CustomerPublicService customerPublicService;

    public void testAdd(ZhjnCustomerInfo info, ZhjnCustomerInfo info2) {
        zhjnCustomerDao.save(info);
        zhjnCustomerDao.save(info2);
    }


    @Override
    public ResultDto clerkCustomer(ZhjnClerkReq req) {
        ResultDto resultDto = new ResultDto();
        try {
            ZhjnCustomerInfo zhjnCustomerInfo = zhjnCustomerDao.findZhjnCustomerInfoByOrderId(req.getOrderId());
            if (zhjnCustomerInfo == null) {
                resultDto.setCode("1");
                resultDto.setMessage("未能找到该任务编号");
                return resultDto;
            }
            if (zhjnCustomerInfo.getCustomerStatus() != 0L) {
                resultDto.setCode("1");
                resultDto.setMessage("该任务不处于待办状态");
                return resultDto;
            }
            zhjnCustomerInfo.setLocation(req.getLocation());
            zhjnCustomerInfo.setCustomerStatus(1L);
            zhjnCustomerInfo.setClerkTime(String.valueOf(new Date()));
            zhjnCustomerInfo.setImageNo(req.getImageNo());
            zhjnCustomerDao.save(zhjnCustomerInfo);
            resultDto.setCode("0");
            resultDto.setMessage("");
        } catch (Exception ee) {
            resultDto.setCode("2");
            resultDto.setMessage(ee.getMessage());
        }
        return resultDto;
    }

    @Override
    public ResultDto checkCustomer(ZhjnCheckReq req) {
        ResultDto resultDto = new ResultDto();
        try {
            ZhjnCustomerInfo zhjnCustomerInfo = zhjnCustomerDao.findZhjnCustomerInfoByOrderId(req.getOrderId());
            if (zhjnCustomerInfo == null) {
                resultDto.setCode("1");
                resultDto.setMessage("未能找到该任务编号");
                return resultDto;
            }
            if (zhjnCustomerInfo.getCustomerStatus() != 1L) {
                resultDto.setCode("1");
                resultDto.setMessage("该任务不处于待办状态");
                return resultDto;
            }
            zhjnCustomerInfo.setCheckMessage(req.getCheckMessage());
            zhjnCustomerInfo.setCheckTime(String.valueOf(new Date()));
            zhjnCustomerInfo.setCustomerStatus(req.getCustomerStatus());
            zhjnCustomerDao.save(zhjnCustomerInfo);
            resultDto.setCode("0");
            resultDto.setMessage("");
        } catch (Exception ee) {
            resultDto.setCode("2");
            resultDto.setMessage(ee.getMessage());
        }
        return resultDto;
    }

    /**
     * 有权人列表
     *
     * @param req
     */
    public ResultDto getCustomerByCheck(ZhjnCheckListReq req) {
        ResultDto resultDto = new ResultDto();
        try {
            List<ZhjnCustomerInfo> resultList = new ArrayList<>();
            if (StringUtils.isBlank(req.getCustomerName())) {
                resultList = zhjnCustomerDao.findCustomerByCheck(req.getCheckNo());
            } else {
                resultList = zhjnCustomerDao.findCustomerByCheckAndName(req.getCheckNo(), req.getCustomerName());
            }
            for (ZhjnCustomerInfo info : resultList) {
                switch (info.getAcctType()) {
                    case "jiben":
                        info.setAcctType("人民币基本存款账户");
                        break;
                    case "yiban":
                        info.setAcctType("人民币一般存款账户");
                        break;
                    case "yusuan":
                        info.setAcctType("人民币预算单位专用存款账户");
                        break;
                    case "feiyusuan":
                        info.setAcctType("人民币非预算单位专用存款账户");
                        break;
                    case "linshi":
                        info.setAcctType("人民币临时机构临时存款账户");
                        break;
                    case "feilinshi":
                        info.setAcctType("人民币非临时机构临时存款账户");
                        break;
                    default:
                        info.setAcctType("未知账户性质");
                }
                switch (info.getFileType()) {
                    case "03":
                        info.setFileType("登记证书");
                        break;
                    case "01":
                        info.setFileType("工商营业执照");
                        break;
                    case "02":
                        info.setFileType("批文");
                        break;
                    case "04":
                        info.setFileType("开户证明");
                        break;
                    case "09":
                        info.setFileType("主管部门批文");
                        break;
                    case "11":
                        info.setFileType("政府部门文件");
                        break;
                    case "10":
                        info.setFileType("相关部门证明");
                        break;
                    case "06":
                        info.setFileType("借款合同");
                        break;
                    case "07":
                        info.setFileType("其他结算需要的证明");
                        break;
                    case "12":
                        info.setFileType("证券从业资格证书");
                        break;
                    default:
                        info.setFileType("其他");
                }
            }
            resultDto.setData(resultList);
            resultDto.setMessage("");
            resultDto.setCode("0");
        } catch (Exception e) {
            resultDto.setCode("1");
            resultDto.setMessage(e.getMessage());
        }
        return resultDto;
    }

    /**
     * 经办人列表
     *
     * @param req
     */
    public ResultDto getCustomerByClerk(ZhjnClerkListReq req) {
        ResultDto resultDto = new ResultDto();
        try {
            List<ZhjnCustomerInfo> resultList = new ArrayList<>();
            if (StringUtils.isBlank(req.getCustomerName())) {
                resultList = zhjnCustomerDao.findZhjnCustomerInfosByClerkNoOrderByClerkTimeDesc(req.getClerkNo());
            } else {
                resultList = zhjnCustomerDao.findCustomerByClerkAndName(req.getClerkNo(), req.getCustomerName());
            }

            for (ZhjnCustomerInfo info : resultList) {
                AccountsAll accountsAll = accountsAllDao.findByAcctNo(info.getBankCode());
                info.setCustomerNo(accountsAll.getCustomerNo());
                info.setCustomerName(accountsAll.getAcctName());
                info.setBankName(accountsAll.getBankName());
                info.setAcctType(accountsAll.getAcctType().toString());
                CustomerPublicInfo customerPublic = customerPublicService.getByCustomerNo(info.getCustomerNo());
                info.setFileNo(customerPublic.getFileNo());
                info.setFileType(customerPublic.getFileType());
                info.setLegalName(customerPublic.getLegalName());
                if (StringUtils.isBlank(customerPublic.getTelephone())) {
                    info.setTelephone(customerPublic.getLegalTelephone());
                } else {
                    info.setTelephone(customerPublic.getTelephone());
                }
                info.setRegArea(customerPublic.getRegAddress());
                zhjnCustomerDao.save(info);
            }
            for (ZhjnCustomerInfo info : resultList) {
                switch (info.getFileType()) {
                    case "03":
                        info.setFileType("登记证书");
                        break;
                    case "01":
                        info.setFileType("工商营业执照");
                        break;
                    case "02":
                        info.setFileType("批文");
                        break;
                    case "04":
                        info.setFileType("开户证明");
                        break;
                    case "09":
                        info.setFileType("主管部门批文");
                        break;
                    case "11":
                        info.setFileType("政府部门文件");
                        break;
                    case "10":
                        info.setFileType("相关部门证明");
                        break;
                    case "06":
                        info.setFileType("借款合同");
                        break;
                    case "07":
                        info.setFileType("其他结算需要的证明");
                        break;
                    case "12":
                        info.setFileType("证券从业资格证书");
                        break;
                    default:
                        info.setFileType("其他");
                }
                switch (info.getAcctType()) {
                    case "jiben":
                        info.setAcctType("人民币基本存款账户");
                        break;
                    case "yiban":
                        info.setAcctType("人民币一般存款账户");
                        break;
                    case "yusuan":
                        info.setAcctType("人民币预算单位专用存款账户");
                        break;
                    case "feiyusuan":
                        info.setAcctType("人民币非预算单位专用存款账户");
                        break;
                    case "linshi":
                        info.setAcctType("人民币临时机构临时存款账户");
                        break;
                    case "feilinshi":
                        info.setAcctType("人民币非临时机构临时存款账户");
                        break;
                    default:
                        info.setAcctType("未知账户性质");
                }
            }
            resultDto.setData(resultList);
            resultDto.setCode("0");
            resultDto.setMessage("");
        } catch (Exception e) {
            resultDto.setMessage(e.getMessage());
            resultDto.setCode("2");
        }
        return resultDto;
    }

    @Override
    public TableResultResponse<ZhjnCustomerDto> query(ZhjnCustomerDto dto, Pageable pageable) {

        long count=zhjnCustomerDao.count(new ZhjnCustomerSpec(dto));
        Page<ZhjnCustomerInfo> page =zhjnCustomerDao.findAll(new ZhjnCustomerSpec(dto),pageable);
        List<ZhjnCustomerInfo> list = page.getContent();
        log.info("页面获取到的数据为："+list.toString());
        List<ZhjnCustomerDto> listDto = ConverterService.convertToList(list, ZhjnCustomerDto.class);
        return new TableResultResponse<ZhjnCustomerDto>((int) count, listDto);



    }

    @Override
    public TableResultResponse<ZhjnCustomerDto> query1(ZhjnCustomerDto dto, Pageable pageable) {
        long count=zhjnCustomerDao.count(new ZhjnCustomerSpec(dto));
        Page<ZhjnCustomerInfo> page =zhjnCustomerDao.findAll(new ZhjnCustomerSpec(dto),pageable);
        List<ZhjnCustomerInfo> list = page.getContent();
        log.info("页面获取到的数据为："+list.toString());
        List<ZhjnCustomerDto> listDto = ConverterService.convertToList(list, ZhjnCustomerDto.class);
        return new TableResultResponse<ZhjnCustomerDto>((int) count, listDto);

    }

}
