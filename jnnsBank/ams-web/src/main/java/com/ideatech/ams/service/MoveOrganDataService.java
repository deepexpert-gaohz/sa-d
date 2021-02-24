package com.ideatech.ams.service;


import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateTableStatement;
import com.alibaba.fastjson.JSON;
import com.ideatech.ams.account.dao.AccountChangeSummaryDao;
import com.ideatech.ams.account.dto.*;
import com.ideatech.ams.account.dto.bill.AccountBillsAllInfo;
import com.ideatech.ams.account.entity.AccountChangeSummary;
import com.ideatech.ams.account.entity.AccountsAll;
import com.ideatech.ams.account.service.*;
import com.ideatech.ams.account.service.bill.AccountBillsAllService;
import com.ideatech.ams.customer.dao.CustomersAllDao;
import com.ideatech.ams.customer.dto.CustomerPublicLogInfo;
import com.ideatech.ams.customer.dto.CustomerPublicMidInfo;
import com.ideatech.ams.customer.dto.CustomerTuneSearchHistoryDto;
import com.ideatech.ams.customer.dto.CustomersAllInfo;
import com.ideatech.ams.customer.dto.bill.CustomerBillsAllInfo;
import com.ideatech.ams.customer.entity.CustomerTuneSearchHistory;
import com.ideatech.ams.customer.entity.CustomersAll;
import com.ideatech.ams.customer.service.CustomerPublicLogService;
import com.ideatech.ams.customer.service.CustomerPublicMidService;
import com.ideatech.ams.customer.service.CustomerTuneSearchHistoryService;
import com.ideatech.ams.customer.service.CustomersAllService;
import com.ideatech.ams.customer.service.bill.CustomerBillsAllService;
import com.ideatech.ams.kyc.dao.idcard.IdCheckLogDao;
import com.ideatech.ams.kyc.dto.idcard.IdCardLocalDto;
import com.ideatech.ams.kyc.entity.idcard.IdCheckLog;
import com.ideatech.ams.kyc.service.idcard.IdCardLocalService;
import com.ideatech.ams.poi.AccountPoi;
import com.ideatech.ams.poi.CustomerPoi;
import com.ideatech.ams.poi.UserPoi;
import com.ideatech.ams.service.poi.AccountExport;
import com.ideatech.ams.service.poi.CustomerExport;
import com.ideatech.ams.service.poi.UserExport;
import com.ideatech.ams.system.org.dto.MoveOrganDataDto;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.entity.OrganizationPo;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.pbc.dto.PbcAccountDto;
import com.ideatech.ams.system.pbc.service.PbcAccountService;
import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.ams.system.user.service.UserService;
import com.ideatech.ams.vo.chaibing.AccountExcelVo;
import com.ideatech.ams.vo.chaibing.CustomerExcelVo;
import com.ideatech.ams.vo.chaibing.UserExcelVo;
import com.ideatech.common.constant.ResultCode;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.excel.util.ImportExcel;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.util.BeanValueUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class MoveOrganDataService {

    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private UserService userService;
    @Autowired
    private PbcAccountService pbcAccountService;
    @Autowired
    private AccountsAllService accountsAllService;
    @Autowired
    private AccountBillsAllService accountBillsAllService;
    @Autowired
    private AccountPublicLogService accountPublicLogService;
    @Autowired
    private CustomerPublicMidService customerPublicMidService;
    @Autowired
    private CustomerBillsAllService customerBillsAllService;
    @Autowired
    private CustomersAllService customersAllService;
    @Autowired
    private CustomerPublicLogService customerPublicLogService;
    @Autowired
    private CustomersAllDao customersAllDao;
    @Autowired
    private IdCardLocalService idCardLocalService;
    @Autowired
    private IdCheckLogDao idCheckLogDao;
    @Autowired
    private PbcSyncListService pbcSyncListService;
    @Autowired
    private BatchSuspendService batchSuspendService;
    @Autowired
    private AccountPublicService accountPublicService;
    @Autowired
    private CustomerTuneSearchHistoryService customerTuneSearchHistoryService;
    @Autowired
    private AccountChangeSummaryDao accountChangeSummaryDao;


    public void moveOrganData(MoveOrganDataDto moveOrganDataDto) {
        OrganizationDto formOrganDto2 = null;

        if(moveOrganDataDto.getFromOrgId() == 1){
            throw new BizServiceException(EErrorCode.SYSTEM_ERROR,"父类机构不能进行并迁！");
        }

        if(moveOrganDataDto.getFromOrgId().equals(moveOrganDataDto.getToOrgId())){
            throw new BizServiceException(EErrorCode.SYSTEM_ERROR,"机构并迁不能选择本机构，请重新选择！");
        }

        //机构迁移
        OrganizationDto formOrganDto = organizationService.findById(moveOrganDataDto.getFromOrgId());
        OrganizationDto toOrganDto = organizationService.findById(moveOrganDataDto.getToOrgId());

        if("2".equals(moveOrganDataDto.getMoveData())) {//机构信息迁移校验
            List<OrganizationDto> organizationDtos = organizationService.searchChild(formOrganDto.getId(), "");
            if(organizationDtos.size() > 0) {
                throw new BizServiceException(EErrorCode.SYSTEM_ERROR,"机构信息迁移被迁机构底下不能包含子机构，请重新选择！");
            }
        }

        if ("0".equals(moveOrganDataDto.getMoveData()) || "2".equals(moveOrganDataDto.getMoveData())) {
            if("2".equals(moveOrganDataDto.getMoveData())) {  //机构信息迁移
                formOrganDto2 = new OrganizationDto();
                BeanValueUtils.copyProperties(formOrganDto, formOrganDto2);
                toOrganDto = moveOrgan(formOrganDto2, toOrganDto);
            }
            //迁移用户数据
            moveUserData(formOrganDto, toOrganDto);
            moveData(formOrganDto, toOrganDto);
            log.info("全量迁移结束......");
        }
    }

    public void moveData(OrganizationDto formOrganFullId, OrganizationDto toOrganFullId) {
        //账户迁移
        moveAccounsAll(formOrganFullId, toOrganFullId);
        //客户迁移
        moveCustomersAll(formOrganFullId, toOrganFullId);
        //迁移本地身份信息
        moveIdLocalCard(formOrganFullId, toOrganFullId);
        //迁移人行同步信息表
        movePbcSyncList(formOrganFullId, toOrganFullId);
        //迁移批量久悬任务表
        moveBatchSuspend(formOrganFullId, toOrganFullId);
    }

    public void moveBatchSuspend(OrganizationDto formOrgan, OrganizationDto toOrgan) {
        //BatchSuspendPo
        log.info("开始迁移批量久悬任务表");
        try {
            log.info("开始迁移BatchSuspend数据......");
            List<BatchSuspendDto> batchSuspendDtoList = batchSuspendService.findByOrganFullIdLike(formOrgan.getFullId() + "%");
            if(CollectionUtils.isNotEmpty(batchSuspendDtoList)){
                for (BatchSuspendDto batchSuspendDto : batchSuspendDtoList) {
                    batchSuspendDto.setOrganFullId(toOrgan.getFullId());
                    batchSuspendService.save(batchSuspendDto);
                }
            }

        } catch (Exception e) {
            log.error("迁移数据失败......");
            throw new BizServiceException(EErrorCode.TECH_DATA_NOT_EXIST, String.format("BatchSuspendPo 数据迁移异常", e));
        }
        log.info("批量久悬任务表迁移完成");
    }

    public void movePbcSyncList(OrganizationDto formOrgan, OrganizationDto toOrgan) {
        //PbcSyncList
        log.info("开始迁移人行信息同步表");
        try {
            log.info("开始迁移PbcSyncList数据......");
            List<PbcSyncListDto> pbcSyncListList = pbcSyncListService.findByOrganFullIdLike(formOrgan.getFullId() + "%");
            if(CollectionUtils.isNotEmpty(pbcSyncListList)){
                for (PbcSyncListDto pbcSyncListDto : pbcSyncListList) {
                    pbcSyncListDto.setOrganFullId(toOrgan.getFullId());
                    pbcSyncListService.savePbcSyncList(pbcSyncListDto);
                }
            }

        } catch (Exception e) {
            log.error("迁移数据失败......");
            throw new BizServiceException(EErrorCode.TECH_DATA_NOT_EXIST, String.format("PbcSyncList数据迁移异常", e));
        }
        log.info("人行信息同步表迁移完成");
    }

    public void moveIdLocalCard(OrganizationDto formOrgan, OrganizationDto toOrgan) {
        //IdCardLocal   本地身份证信息
        //IdCheckLog   身份证信息核查记录
        log.info("本地身份信息开始迁移");
        try {
            log.info("开始迁移IdCardLocal数据......");
            List<IdCardLocalDto> idCardLocalDtoList = idCardLocalService.findByOrganFullIdLike(formOrgan.getFullId() + "%");
            if(CollectionUtils.isNotEmpty(idCardLocalDtoList)){
                for (IdCardLocalDto idCardLocalDto : idCardLocalDtoList) {
                    idCardLocalDto.setOrganFullId(toOrgan.getFullId());
                    idCardLocalService.save(idCardLocalDto);
                }
            }

        } catch (Exception e) {
            log.error("迁移数据失败......");
            throw new BizServiceException(EErrorCode.TECH_DATA_NOT_EXIST, String.format("IdCardLocal数据迁移异常", e));
        }

        try {
            log.info("开始迁移IdCheckLog数据......");
            List<IdCheckLog> idCheckLogList = idCheckLogDao.findByOrganFullIdLike(formOrgan.getFullId() + "%");
            if(CollectionUtils.isNotEmpty(idCheckLogList)){
                for (IdCheckLog idCheckLog : idCheckLogList) {
                    idCheckLog.setOrganFullId(toOrgan.getFullId());
                    idCheckLogDao.save(idCheckLog);
                }
            }

        } catch (Exception e) {
            log.error("迁移数据失败......");
            throw new BizServiceException(EErrorCode.TECH_DATA_NOT_EXIST, String.format("IdCheckLog数据迁移异常", e));
        }
        log.info("本地身份信息迁移完成");
    }

    public void moveCustomersAll(OrganizationDto formOrgan, OrganizationDto toOrgan) {
        //CustomerPublicMid  对公客户更新中间表
        //CustomerBillsAll   客户流水表
        //CustomersAll   客户信息基础表
        //CustomerPublicLog    对公客户更新中间表
        //根据orgFullId查询中间表数据
        log.info("开始迁移客户数据");
        try {
            log.info("开始迁移CustomerPublicMid数据......");
            List<CustomerPublicMidInfo> customerPublicMidInfoList = customerPublicMidService.findByOrganFullIdLike(formOrgan.getFullId() + "%");
            if(CollectionUtils.isNotEmpty(customerPublicMidInfoList)){
                for (CustomerPublicMidInfo customerPublicMidInfo : customerPublicMidInfoList) {
                    customerPublicMidInfo.setOrganFullId(toOrgan.getFullId());
                    customerPublicMidService.save(customerPublicMidInfo);
                }
            }

        } catch (Exception e) {
            log.error("迁移数据失败......");
            throw new BizServiceException(EErrorCode.TECH_DATA_NOT_EXIST, String.format("CustomerPublicMid数据迁移异常", e));
        }

        try {
            log.info("开始迁移CustomerBillsAll数据......");
            List<CustomerBillsAllInfo> customerBillsAllInfoList = customerBillsAllService.findByOrganFullIdLike(formOrgan.getFullId() + "%");
            if(CollectionUtils.isNotEmpty(customerBillsAllInfoList)){
                for (CustomerBillsAllInfo customerBillsAllInfo : customerBillsAllInfoList) {
                    customerBillsAllInfo.setOrganFullId(toOrgan.getFullId());
                    customerBillsAllService.save(customerBillsAllInfo);
                }
            }

        } catch (Exception e) {
            log.error("迁移数据失败......");
            throw new BizServiceException(EErrorCode.TECH_DATA_NOT_EXIST, String.format("CustomerBillsAll 数据迁移异常", e));
        }

        try {
            log.info("开始迁移CustomersAll数据......");
            OrganizationDto toOrganDto = organizationService.findByOrganFullId(toOrgan.getFullId());
            List<CustomersAllInfo> customersAllInfoList = customersAllService.findByOrganFullIdLike(formOrgan.getFullId() + "%");

            if(CollectionUtils.isNotEmpty(customersAllInfoList)){
                for (CustomersAllInfo customersAllInfo : customersAllInfoList) {
                    CustomersAll cusAll = customersAllDao.findOne(customersAllInfo.getId());
                    customersAllInfo.setOrganFullId(toOrgan.getFullId());
                    customersAllInfo.setOrganName(toOrganDto.getName());
                    BeanUtils.copyProperties(customersAllInfo, cusAll);
                    customersAllDao.save(cusAll);
                }
            }

        } catch (Exception e) {
            log.error("迁移数据失败......");
            throw new BizServiceException(EErrorCode.TECH_DATA_NOT_EXIST, String.format("CustomersAllInfo 数据迁移异常", e));
        }

        try {
            log.info("开始迁移CustomerPublicLog数据......");
            List<CustomerPublicLogInfo> customerPublicLogInfoList = customerPublicLogService.findByOrganFullIdLike(formOrgan.getFullId() + "%");
            if(CollectionUtils.isNotEmpty(customerPublicLogInfoList)){
                for (CustomerPublicLogInfo customerPublicLogInfo : customerPublicLogInfoList) {
                    customerPublicLogInfo.setOrganFullId(toOrgan.getFullId());
                    customerPublicLogService.save(customerPublicLogInfo);
                }
            }

        } catch (Exception e) {
            log.error("迁移数据失败......");
            throw new BizServiceException(EErrorCode.TECH_DATA_NOT_EXIST, String.format("CustomerPublicLog 数据迁移异常", e));
        }

        try {
            log.info("开始迁移CustomerTuneSearchHistoryDto数据......");
            List<CustomerTuneSearchHistoryDto> customerTuneSearchHistoryDtoList = customerTuneSearchHistoryService.findByOrganFullIdLike(formOrgan.getFullId() + "%");
            if(CollectionUtils.isNotEmpty(customerTuneSearchHistoryDtoList)){
                for (CustomerTuneSearchHistoryDto customerTuneSearchHistoryDto : customerTuneSearchHistoryDtoList) {
                    customerTuneSearchHistoryDto.setOrganFullId(toOrgan.getFullId());
                    customerTuneSearchHistoryDto.setOrganId(toOrgan.getId());
                    customerTuneSearchHistoryService.save(customerTuneSearchHistoryDto);
                }
            }

        } catch (Exception e) {
            log.error("迁移数据失败......");
            throw new BizServiceException(EErrorCode.TECH_DATA_NOT_EXIST, String.format("CustomerTuneSearchHistoryDto 数据迁移异常", e));
        }

        log.info("客户数据迁移完成");
    }

    public OrganizationDto moveOrgan(OrganizationDto formOrganDto, OrganizationDto toOrganDto) {
        OrganizationDto formOrganDto2 = new OrganizationDto();

        log.info("开始迁移机构数据");
        //需要迁移的机构
        try {
            log.info("开始迁移Organ数据......");
            //查询是否存在下属机构
            formOrganDto.setParentId(toOrganDto.getId());
            formOrganDto.setFullId(toOrganDto.getFullId() + "-" + formOrganDto.getId());
            BeanValueUtils.copyProperties(formOrganDto, formOrganDto2);
            organizationService.save(formOrganDto);
//            saveChildorg(formOrganDto, toOrganDto);
        } catch (Exception e) {
            log.error("迁移数据失败......");
            throw new BizServiceException(EErrorCode.TECH_DATA_NOT_EXIST, String.format("机构迁移数据异常", e));
        }
        log.info("机构迁移结束");

        return formOrganDto2;
    }

    public void saveChildorg(OrganizationDto formOrganDto, OrganizationDto toOrganDto) {
        List<OrganizationDto> orgList = organizationService.findByOrganFullIdLike(formOrganDto.getFullId() + "-");
        if (CollectionUtils.isNotEmpty(orgList)) {
            for (OrganizationDto organizationDto : orgList) {
                organizationDto.setFullId(formOrganDto.getFullId() + "-" + organizationDto.getId());
                organizationService.save(organizationDto);
                saveChildorg(organizationDto, toOrganDto);
            }
        }
    }

    public void moveAccounsAll(OrganizationDto formOrgan, OrganizationDto toOrgan) {
        log.info("开始迁移账户数据");
        //账户有关3张表：AccountsAll    AccountPublicLog   AccountBillsAll
        //关联字段：organFullId
        //根据需迁移的机构号去AccountBillsAll查找数据
        try {
            log.info("开始迁移AccountBillsAll数据......");
            List<AccountBillsAllInfo> accountBillsAllList = accountBillsAllService.findByOrganFullIdLike(formOrgan.getFullId() + "%");
            if(CollectionUtils.isNotEmpty(accountBillsAllList)){
                for (AccountBillsAllInfo accountBillsAllInfo : accountBillsAllList) {
                    accountBillsAllInfo.setOrganFullId(toOrgan.getFullId());
                    accountBillsAllService.save(accountBillsAllInfo);
                    //根据billsId查找accountChangeSummery对象修改organFullid
                    AccountChangeSummary accountChangeSummary = accountChangeSummaryDao.findByRefBillId(accountBillsAllInfo.getId());
                    if(accountChangeSummary != null){
                        accountChangeSummary.setOrganFullId(toOrgan.getFullId());
                        accountChangeSummaryDao.save(accountChangeSummary);
                    }
                }
            }
        } catch (Exception e) {
            log.error("迁移数据失败......");
            throw new BizServiceException(EErrorCode.TECH_DATA_NOT_EXIST, String.format("AccountBillsAll迁移数据异常", e));
        }

        //根据迁移机构查找AccountPublicLog数据
        try {
            log.info("开始迁移AccountPublicLog数据......");
            List<AccountPublicLogInfo> accountPublicLogInfoList = accountPublicLogService.findByOrganFullIdLike(formOrgan.getFullId() + "%");
            if(CollectionUtils.isNotEmpty(accountPublicLogInfoList)){
                for (AccountPublicLogInfo accountPublicLogInfo : accountPublicLogInfoList) {
                    accountPublicLogInfo.setOrganFullId(toOrgan.getFullId());
                    accountPublicLogService.saveOrUpdate(accountPublicLogInfo);
                }
            }
        } catch (Exception e) {
            log.error("迁移数据失败......");
            throw new BizServiceException(EErrorCode.TECH_DATA_NOT_EXIST, String.format("AccountPublicLog迁移数据异常", e));
        }

        //根据迁移机构查找AccountPublic数据
        try {
            log.info("开始迁移AccountPublic数据......");
            List<AccountPublicInfo> accountPublicInfoList = accountPublicService.findByOrganFullId(formOrgan.getFullId() + "%");
            if(CollectionUtils.isNotEmpty(accountPublicInfoList)){
                for (AccountPublicInfo accountPublicInfo : accountPublicInfoList) {
                    accountPublicInfo.setOrganFullId(toOrgan.getFullId());
                    accountPublicService.save(accountPublicInfo);
                }
            }
        } catch (Exception e) {
            log.error("迁移数据失败......");
            throw new BizServiceException(EErrorCode.TECH_DATA_NOT_EXIST, String.format("AccountPublicLog迁移数据异常", e));
        }

        //根据迁移机构查找AccountsAll数据
        try {
            log.info("开始迁移AccountsAll数据......");
            List<AccountsAllInfo> accountsAllInfoList = accountsAllService.findByOrganFullIdLike(formOrgan.getFullId() + "%");
            if(CollectionUtils.isNotEmpty(accountsAllInfoList)){
                for (AccountsAllInfo accountsAllInfo : accountsAllInfoList) {
                    accountsAllInfo.setOrganFullId(toOrgan.getFullId());
                    accountsAllInfo.setBankName(toOrgan.getName());
                    accountsAllInfo.setBankCode(toOrgan.getPbcCode());
                    accountsAllService.save(accountsAllInfo);
                }
            }
        } catch (Exception e) {
            log.error("迁移数据失败......");
            throw new BizServiceException(EErrorCode.TECH_DATA_NOT_EXIST, String.format("AccountsAll迁移数据异常", e));
        }
        log.info("账户数据迁移结束");
    }

    public void moveUserData(OrganizationDto formOrganDto, OrganizationDto toOrganDto) {
        //迁移用户数据
        log.info("开始迁移用户数据");
        try {
            log.info("开始迁移User数据......");
            List<UserDto> userList = userService.findByOrgId(formOrganDto.getId());
            if(CollectionUtils.isNotEmpty(userList)){
                for (UserDto userDto : userList) {
                    userDto.setOrgId(toOrganDto.getId());
                    userService.save(userDto);
                }
            }
        } catch (Exception e) {
            log.error("迁移数据失败......");
            throw new BizServiceException(EErrorCode.TECH_DATA_NOT_EXIST, String.format("用户数据迁移失败", e));
        }
        log.info("用户数据完成");
        //迁移人行用户信息数据
        log.info("开始迁移人行用户数据");
        try {
            log.info("全部迁移删除PbcAccount人行账号数据......");
            List<PbcAccountDto> pbcAccountList = pbcAccountService.listByOrgId(formOrganDto.getId());
            if(CollectionUtils.isNotEmpty(pbcAccountList)){
                for (PbcAccountDto pbcAccountDto : pbcAccountList) {
                    pbcAccountService.delete(pbcAccountDto.getId());
                }
            }
        } catch (Exception e) {
            log.error("迁移数据失败......");
            throw new BizServiceException(EErrorCode.TECH_DATA_NOT_EXIST, String.format("用户关联人行信息数据迁移失败", e));
        }
        log.info("人行用户数据迁移完成");
    }

    public IExcelExport exportUserExcel(Long fromOrgId) {
        List<UserDto> userDtoList = userService.findByOrgId(fromOrgId);
        IExcelExport userExcelExport = new UserExport();
        List<UserPoi> userPoiList = new ArrayList<UserPoi>();
        for (UserDto userDto : userDtoList) {
            UserPoi userPoi = new UserPoi();
            BeanUtils.copyProperties(userDto, userPoi);
            userPoiList.add(userPoi);
        }
        userExcelExport.setPoiList(userPoiList);
        return userExcelExport;
    }

    public IExcelExport exportCustomerExcel(Long fromOrgId) {
        OrganizationDto organizationDto = organizationService.findById(fromOrgId);
        List<CustomersAllInfo> customersAllInfoList = customersAllService.findByOrganFullIdLike(organizationDto.getFullId() + "%");
        List<CustomerPoi> customerPoiList = new ArrayList<CustomerPoi>();
        IExcelExport customerExcelExport = new CustomerExport();
        for (CustomersAllInfo customersAllInfo : customersAllInfoList) {
            CustomerPoi customerPoi = new CustomerPoi();
            BeanUtils.copyProperties(customersAllInfo, customerPoi);
            customerPoi.setCustomerId(customersAllInfo.getId() + "");
            customerPoi.setOrgName(organizationDto.getName());
            customerPoi.setOrganCode(organizationDto.getCode());
            customerPoiList.add(customerPoi);
        }
        customerExcelExport.setPoiList(customerPoiList);
        return customerExcelExport;
    }

    public IExcelExport exportAccountExcel(Long fromOrgId) {
        OrganizationDto organizationDto = organizationService.findById(fromOrgId);
        List<AccountsAllInfo> accountsAllInfoList = accountsAllService.findByOrganFullIdLike(organizationDto.getFullId() + "%");
        List<AccountPoi> accountPoiList = new ArrayList<AccountPoi>();
        IExcelExport customerExcelExport = new AccountExport();
        for (AccountsAllInfo accountsAllInfo : accountsAllInfoList) {
            AccountPoi accountPoi = new AccountPoi();
            BeanUtils.copyProperties(accountsAllInfo, accountPoi);
            accountPoiList.add(accountPoi);
        }
        customerExcelExport.setPoiList(accountPoiList);
        return customerExcelExport;
    }

    public ResultDto importExcel(MultipartFile file, String moveTemplate) {
        ResultDto dto = new ResultDto();
        try {
            if ("1".equals(moveTemplate)) {
                log.info("用户迁并导入开始......");
                //用户迁并导入
                ImportExcel importExcel = new ImportExcel(file, 0, 0);
                if (importExcel.getRow(0) != null && importExcel.getRow(0).getPhysicalNumberOfCells() != 3) {
                    ResultDtoFactory.toNack("导入失败，错误的模板");
                    dto.setCode(ResultCode.NACK);
                    dto.setMessage("导入失败，错误的模板");
                    log.info("导入模版错误...");
                } else {
                    log.info("开始迁移用户数据......");
                    long starttime = System.currentTimeMillis();
                    List<UserExcelVo> dataList = importExcel.getDataList(UserExcelVo.class);
                    if(CollectionUtils.isEmpty(dataList)){
                        dto.setCode(ResultCode.NACK);
                        dto.setMessage("导入文件为空，请检查后再次导入......");
                        return dto;
                    }

                    for (UserExcelVo userExcelVo : dataList) {
                        if(StringUtils.isEmpty(userExcelVo.getUsername()) || StringUtils.isEmpty(userExcelVo.getCname())){
                            dto.setCode(ResultCode.NACK);
                            dto.setMessage("除“迁移机构（内部机构号）”外字段必填。请不要篡改除“迁移机构（内部机构号）” 外的其他数据，如有篡改请重新下载。");
                            return dto;
                        }
                    }
                    long endtime = System.currentTimeMillis();
                    log.info("用时:" + (endtime - starttime) + "");
                    for (UserExcelVo userExcelVo : dataList) {

                        if(StringUtils.isBlank(userExcelVo.getToOrganCode())){
                            //没有填写迁并机构进行下一条
                            continue;
                        }

                        //找出要迁移到的机构
                        String toOrganCode = userExcelVo.getToOrganCode();
                        OrganizationDto toOrgan = organizationService.findByCode(toOrganCode);
                        if(toOrgan == null){
                            dto.setCode(ResultCode.NACK);
                            dto.setMessage("迁并机构"+toOrganCode+"不存在，请检查后再次导入.");
                            return dto;
                        }
                        //根据用户名找出这笔数据进行机构的迁移
                        UserDto userDto = userService.findByUsername(userExcelVo.getUsername());
                        userDto.setOrgId(toOrgan.getId());
                        userService.save(userDto);
                    }
                    log.info("用户数据迁移结束......");
                    dto.setCode(ResultCode.ACK);
                    dto.setMessage("迁移结束.");
                }

            } else if ("2".equals(moveTemplate)) {
                log.info("客户迁并导入开始......");
                ImportExcel importExcel = new ImportExcel(file, 0, 0);
                if (importExcel.getRow(0) != null && importExcel.getRow(0).getPhysicalNumberOfCells() != 6) {
                    ResultDtoFactory.toNack("导入失败，错误的模板");
                    dto.setCode(ResultCode.NACK);
                    dto.setMessage("导入失败，错误的模板");
                    log.info("导入模版错误...");
                } else {
                    log.info("开始迁移客户数据");
                    long starttime = System.currentTimeMillis();
                    List<CustomerExcelVo> dataList = importExcel.getDataList(CustomerExcelVo.class);
                    for (CustomerExcelVo customerExcelVo : dataList) {
                        if(StringUtils.isEmpty(customerExcelVo.getCustomerId()) || StringUtils.isEmpty(customerExcelVo.getOrganCode())
                            || StringUtils.isEmpty(customerExcelVo.getCustomerNo()) || StringUtils.isEmpty(customerExcelVo.getDepositorName()) || StringUtils.isEmpty(customerExcelVo.getOrgName())){
                            dto.setCode(ResultCode.NACK);
                            dto.setMessage("除“迁移机构（内部机构号）”外字段必填。请不要篡改除“迁移机构（内部机构号）” 外的其他数据，如有篡改请重新下载。");
                            return dto;
                        }
                    }
                    long endtime = System.currentTimeMillis();
                    log.info("用时:" + (endtime - starttime) + "");
                    if(CollectionUtils.isEmpty(dataList)){
                        dto.setCode(ResultCode.NACK);
                        dto.setMessage("导入文件为空，请检查后再次导入......");
                        return dto;
                    }
                    for (CustomerExcelVo customerExcelVo : dataList) {

                        if(StringUtils.isBlank(customerExcelVo.getToOrganCode())){
                            //没有填写迁并机构进行下一条
                            continue;
                        }
                        //要迁移到的机构
                        String toOrganCode = customerExcelVo.getToOrganCode();
                        Long customerId = Long.parseLong(customerExcelVo.getCustomerId());
                        //查找迁移到的机构
                        OrganizationDto toOrganDto = organizationService.findByCode(toOrganCode);
                        if(toOrganDto == null){
                            dto.setCode(ResultCode.NACK);
                            dto.setMessage("迁并机构"+toOrganCode+"不存在，请检查后再次导入.");
                            return dto;
                        }

                        log.info("开始迁移CustomersAll数据......");
                        CustomersAllInfo customersAllInfo = customersAllService.findOne(customerId);
                        if(customersAllInfo != null){
                            customersAllInfo.setOrganFullId(toOrganDto.getFullId());
                            customersAllInfo.setOrganName(toOrganDto.getName());
                            customersAllService.save(customersAllInfo);
                        }

                        log.info("开始迁移CustomerPublicMid数据......");
                        List<CustomerPublicMidInfo> list = customerPublicMidService.findByCustomerId(customerId);
                        if(CollectionUtils.isNotEmpty(list)){
                            for(CustomerPublicMidInfo customerPublicMidInfo : list){
                                customerPublicMidInfo.setOrganFullId(toOrganDto.getFullId());
                                customerPublicMidService.save(customerPublicMidInfo);
                                log.info("开始迁移customerMidId关联CustomerBillsAll数据......");
                                //跟mid表关联的CustomerBillsAll
                                List<CustomerBillsAllInfo> CustomerBillsAllInfoList = customerBillsAllService.findByCustomerId(customerId);
                                if(CollectionUtils.isNotEmpty(CustomerBillsAllInfoList)){
                                    for(CustomerBillsAllInfo customerBillsAllInfo : CustomerBillsAllInfoList){
                                        customerBillsAllInfo.setOrganFullId(toOrganDto.getFullId());
                                        customerBillsAllService.save(customerBillsAllInfo);
                                    }
                                }
                            }
                        }

                        log.info("开始迁移CustomerPublicLog数据......");
                        List<CustomerPublicLogInfo> customerPublicLogInfoList = customerPublicLogService.getByCustomerId(customerId);
                        if(CollectionUtils.isNotEmpty(customerPublicLogInfoList)){
                            for (CustomerPublicLogInfo customerPublicLogInfo : customerPublicLogInfoList) {
                                customerPublicLogInfo.setOrganFullId(toOrganDto.getFullId());
                                customerPublicLogService.save(customerPublicLogInfo);
                            }
                        }


                        log.info("开始迁移CustomerTuneSearchHistoryDto数据");
                        //根据导出文件机构号找出原来的机构，根据organFullId关联查找数据
                        OrganizationDto organizationDto = organizationService.findByCode(customerExcelVo.getOrganCode());
                        if(organizationDto != null){
                            List<CustomerTuneSearchHistoryDto> customerTuneSearchHistoryDtoList = customerTuneSearchHistoryService.findByOrganFullIdLike(organizationDto.getFullId());
                            for (CustomerTuneSearchHistoryDto customerTuneSearchHistoryDto : customerTuneSearchHistoryDtoList) {
                                customerTuneSearchHistoryDto.setOrganFullId(toOrganDto.getFullId());
                                customerTuneSearchHistoryDto.setOrganId(toOrganDto.getId());
                                customerTuneSearchHistoryService.save(customerTuneSearchHistoryDto);
                            }
                        }
                    }
                    log.info("客户数据迁移结束......");
                    dto.setCode(ResultCode.ACK);
                    dto.setMessage("迁移结束.");
                }

            } else if ("3".equals(moveTemplate)) {
                //账户迁并导入
                log.info("账户迁并导入开始......");
                ImportExcel importExcel = new ImportExcel(file, 0, 0);
                if (importExcel.getRow(0) != null && importExcel.getRow(0).getPhysicalNumberOfCells() != 5) {
                    ResultDtoFactory.toNack("导入失败，错误的模板");
                    dto.setCode(ResultCode.NACK);
                    dto.setMessage("导入失败，错误的模板");
                    log.info("导入模版错误...");
                } else {
                    log.info("开始迁移账户数据......");
                    long starttime = System.currentTimeMillis();
                    List<AccountExcelVo> dataList = importExcel.getDataList(AccountExcelVo.class);
                    long endtime = System.currentTimeMillis();
                    log.info("用时:" + (endtime - starttime) + "");
                    if(CollectionUtils.isEmpty(dataList)){
                        dto.setCode(ResultCode.NACK);
                        dto.setMessage("导入文件为空，请检查后再次导入......");
                        return dto;
                    }

                    for (AccountExcelVo accountExcelVo : dataList) {
                        if(StringUtils.isEmpty(accountExcelVo.getAcctNo()) || StringUtils.isEmpty(accountExcelVo.getAcctName())
                            || StringUtils.isEmpty(accountExcelVo.getBankCode()) || StringUtils.isEmpty(accountExcelVo.getBankName())){
                            dto.setCode(ResultCode.NACK);
                            dto.setMessage("除“迁移机构（内部机构号）”外字段必填。请不要篡改除“迁移机构（内部机构号）” 外的其他数据，如有篡改请重新下载。");
                            return dto;
                        }
                    }

                    for (AccountExcelVo accountExcelVo : dataList) {

                        if(StringUtils.isBlank(accountExcelVo.getToOrganCode())){
                            //没有填写迁并机构进行下一条
                            continue;
                        }

                        String toOrganCode = accountExcelVo.getToOrganCode();
                        OrganizationDto toOrgan = organizationService.findByCode(toOrganCode);
                        if(toOrgan == null){
                            dto.setCode(ResultCode.NACK);
                            dto.setMessage("迁并机构"+toOrganCode+"不存在，请检查后再次导入.");
                            return dto;
                        }

                        log.info("开始迁移AccountBillsAll数据......");
                        log.info("根据账号查找数据......");
                        AccountsAllInfo accountsAllInfo = accountsAllService.findByAcctNo(accountExcelVo.getAcctNo());
                        if(accountsAllInfo != null){
                            accountsAllInfo.setOrganFullId(toOrgan.getFullId());
                            accountsAllInfo.setBankName(toOrgan.getName());
                            accountsAllInfo.setBankCode(toOrgan.getPbcCode());
                            accountsAllService.save(accountsAllInfo);
                        }

                        //根据迁移机构查找AccountPublicLog数据
                        log.info("开始迁移AccountPublicLog数据......");
                        log.info("根据accountId查找log数据......");
                        List<AccountPublicLogInfo> accountPublicLogInfoList = accountPublicLogService.findByAccountId(accountsAllInfo.getId());
                        if(CollectionUtils.isNotEmpty(accountPublicLogInfoList)){
                            for (AccountPublicLogInfo accountPublicLogInfo : accountPublicLogInfoList) {
                                accountPublicLogInfo.setOrganFullId(toOrgan.getFullId());
                                accountPublicLogInfo.setBankCode(toOrgan.getPbcCode());
                                accountPublicLogInfo.setBankName(toOrgan.getName());
                                accountPublicLogService.saveOrUpdate(accountPublicLogInfo);
                            }
                        }

                        //根据迁移机构查找AccountPublic数据
                        log.info("开始迁移AccountPublic数据......");
                        log.info("根据accountId查找AccountPublicInfo数据......");
                        AccountPublicInfo accountPublicInfo = accountPublicService.findByAccountId(accountsAllInfo.getId());
                        if(accountPublicInfo != null){
                            accountPublicInfo.setOrganFullId(toOrgan.getFullId());
                            accountPublicService.save(accountPublicInfo);
                        }

                        log.info("开始迁移AccountBillsAll数据......");
                        log.info("根据accountId查找AccountBillsAllInfo数据......");
                        List<AccountBillsAllInfo> accountBillsAllList = accountBillsAllService.findByAccountId(accountsAllInfo.getId());
                        if(CollectionUtils.isNotEmpty(accountBillsAllList)){
                            for (AccountBillsAllInfo accountBillsAllInfo : accountBillsAllList) {
                                accountBillsAllInfo.setOrganFullId(toOrgan.getFullId());
                                accountBillsAllService.save(accountBillsAllInfo);
                                //根据billsId查找accountChangeSummery对象修改organFullid
                                AccountChangeSummary accountChangeSummary = accountChangeSummaryDao.findByRefBillId(accountBillsAllInfo.getId());
                                if(accountChangeSummary != null){
                                    accountChangeSummary.setOrganFullId(toOrgan.getFullId());
                                    accountChangeSummaryDao.save(accountChangeSummary);
                                }
                            }
                        }
                    }
                    log.info("账户数据迁移结束......");
                    dto.setCode(ResultCode.ACK);
                    dto.setMessage("迁移结束.");
                }

            }
        } catch (Exception e) {
            if ("1".equals(moveTemplate)) {
                log.error("用户部分迁移数据迁移失败......", e);
            } else if ("2".equals(moveTemplate)) {
                log.error("客户部分迁移数据迁移失败......", e);
            } else if ("3".equals(moveTemplate)) {
                log.error("账户部分迁移数据迁移失败......", e);
            }

        }
        return dto;
    }
}
