package com.ideatech.ams.kyc.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.customer.service.SaicMonitorService;
import com.ideatech.ams.kyc.constant.CommonConstant;
import com.ideatech.ams.kyc.dao.*;
import com.ideatech.ams.kyc.dto.*;
import com.ideatech.ams.kyc.dto.entrustupdate.EntrustUpdateHistoryDto;
import com.ideatech.ams.kyc.dto.poi.BeneficiaryPoi;
import com.ideatech.ams.kyc.dto.poi.SaicPoi;
import com.ideatech.ams.kyc.dto.saicentrust.EntrustResultDto;
import com.ideatech.ams.kyc.entity.*;
import com.ideatech.ams.kyc.enums.EmployeeType;
import com.ideatech.ams.kyc.enums.SearchStatus;
import com.ideatech.ams.kyc.enums.SearchType;
import com.ideatech.ams.kyc.executor.SaicInfoQueryExecutor;
import com.ideatech.ams.kyc.service.entrustupdate.EntrustUpdateHistoryService;
import com.ideatech.ams.kyc.service.poi.BeneficiaryRecordExport;
import com.ideatech.ams.kyc.service.poi.SaicRecordExport;
import com.ideatech.ams.system.batch.service.BatchService;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.exception.EacException;
import com.ideatech.common.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@Slf4j
public class SaicInfoServiceImpl implements SaicInfoService {

    @Autowired
    private SaicInfoDao saicInfoDao;
    
    @Autowired
    private StockHolderDao stockHolderDao;
    
    @Autowired
    private DirectorDao directorDao;
    
    @Autowired
    private SuperviseDao superviseDao;
    
    @Autowired
    private ManagementDao managementDao;
    
    @Autowired
    private BeneficiaryDao beneficiaryDao;

    @Autowired
    private SaicSearchHistoryService saicSearchHistoryService;

    @Autowired
    private KycSearchHistoryService kycSearchHistoryService;

    @Autowired
    private StockHolderService stockHolderService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ChangeMessService changeMessService;

    @Autowired
    private ChangeRecordService changeRecordService;

    @Autowired
    private DirectorService directorService;

    @Autowired
    private IllegalService illegalService;

    @Autowired
    private ManagementService managementService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private SuperviseService superviseService;

    @Autowired
    private SaicRequestService saicRequestService;

    @Autowired
    private UltimateOwnerDao ultimateOwnerDao;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    private List<Future<Long>> futureList;

    @Autowired
    private BatchService batchService;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Value("${ams.customerTuneImport.executor.num:10}")
    private int customerTuneExecutorNum;

    public static String[] STATE_NORMAL = {"存续","在营","开业","在册","在业","开业","正常"};
    /**
     * 基本工商数据实时接口
     */
    @Value("${saic.url.realTime}")
    private String realTimeUrl;

    @Value("${saic.url.exact}")
    private String exactUrl;

    /**
     * 本地数据实时查询有效期（天）
     */
    @Value("${saic.data.validDays}")
    private Integer realTimeValidDays;

    /**
     * 客户尽调操作本地数据实时查询有效期（天）
     */
    @Value("${saic.data.customTuneValidDays}")
    private Integer customTuneValidDays;

    @Autowired
    private HttpRequest httpRequest;

    @Autowired
    private SaicMonitorService saicMonitorService;

    @Autowired
    private EntrustUpdateHistoryService entrustUpdateHistoryService;

    @Override
    public SaicInfoDto getSaicInfoBase(SearchType type, String username, String keyword, String orgfullid) {
        Boolean isInValidDays = false;
        SaicIdpInfo saicIdpDbInfo = getSaicInfoLocal(keyword);
        //返回的信息
        SaicInfoDto saicInfoDto = new SaicInfoDto();
        if (saicIdpDbInfo == null) {
            log.info("本地没有[{}]工商基础数据", keyword);
        }

        if(type == SearchType.KHJD) {  //客户尽调数据有效期配置单独分离
            isInValidDays = saicIdpDbInfo != null && isCusTuneInValidDays(saicIdpDbInfo);
        } else {
            isInValidDays = saicIdpDbInfo != null && isInValidDays(saicIdpDbInfo);
        }

//        if (saicIdpDbInfo != null && isInValidDays(saicIdpDbInfo)) {
        if (isInValidDays) {
            //数据在有效期内
            BeanCopierUtils.copyProperties(saicIdpDbInfo, saicInfoDto);
            KycSearchHistoryDto kycSearchHistoryDto = new KycSearchHistoryDto();
            kycSearchHistoryDto.setEntname(saicInfoDto.getName());
            kycSearchHistoryDto.setUsername(username);
            kycSearchHistoryDto.setOrgfullid(orgfullid);
            kycSearchHistoryDto.setSaicinfoId(saicInfoDto.getId());
            kycSearchHistoryService.save(kycSearchHistoryDto);
            return saicInfoDto;

        } else {
            SaicIdpInfo saicIdpInfo = null;
            String url="";
            if (type == SearchType.REAL_TIME || type == SearchType.KHJD) {
                if(type == SearchType.KHJD){
                    saicIdpInfo = saicRequestService.getSaicInfoRealTime(keyword,"",type);
                }else{
                    saicIdpInfo = saicRequestService.getSaicInfoRealTime(keyword);
                }
                url = realTimeUrl;
            } else if (type == SearchType.EXACT) {
                saicIdpInfo = saicRequestService.getSaicInfoExact(keyword);
                url = exactUrl;
            }
            if(saicIdpInfo != null){
                inesrtSaicInfoByType(username, saicIdpInfo, url,type, orgfullid);
                BeanUtils.copyProperties(saicIdpInfo,saicInfoDto);
                return saicInfoDto;
            }else{
                inesrtSaicInfoByNull(username,keyword,url,type, orgfullid);
                return null;
            }
        }

    }

	@Override
	public SaicIdpInfo getSaicInfoBaseLocal(String keyword) {
        SaicIdpInfo saicIdpDbInfo = getSaicInfoLocal(keyword);
        //返回的信息
        SaicInfoDto saicInfoDto = new SaicInfoDto();
        if (saicIdpDbInfo == null) {
            log.info("本地没有[{}]工商基础数据", keyword);
        }
        if (saicIdpDbInfo != null && (realTimeValidDays == -1 || isInValidDays(saicIdpDbInfo))) {
            //数据在有效期内,本地数据不保存查询历史
            return saicIdpDbInfo;
        }
        return null;
    }

    @Override
    public SaicIdpInfo getSaicInfoBaseLocalByIdNoValidTime(Long saicInfoId) {
        if(saicInfoId ==null || saicInfoId ==0){
            return null;
        }
        SaicInfo saicInfo = saicInfoDao.findById(saicInfoId);
        if(saicInfo == null){
            return null;
        }
        SaicIdpInfo saicIdpInfo = new SaicIdpInfo();
        BeanCopierUtils.copyProperties(saicInfo, saicIdpInfo);

        //查询其他信息
        saicIdpInfo.setStockholders(stockHolderService.findBySaicInfoId(saicInfo.getId()));
        //经营异常信息
        saicIdpInfo.setChangemess(changeMessService.findBySaicInfoId(saicInfo.getId()));
        saicIdpInfo.setIllegals(illegalService.findBySaicInfoId(saicInfo.getId()));
        //TODO 后续还需完善其他
//        saicIdpInfo.setEmployees(employeeService.findBySaicInfoId(saicInfo.getId()+""));
        return saicIdpInfo;
    }


    @Override
    public SaicIdpInfo getSaicInfoBaseLocalJustSaic(String keyword) {
        SaicIdpInfo saicIdpDbInfo = getSaicInfoLocalJustSaic(keyword);
        //返回的信息
        SaicInfoDto saicInfoDto = new SaicInfoDto();
        if (saicIdpDbInfo == null) {
            log.info("本地没有[{}]工商基础数据", keyword);
        }
        if (saicIdpDbInfo != null && isInValidDays(saicIdpDbInfo)) {
            //数据在有效期内,本地数据不保存查询历史
            return saicIdpDbInfo;
        }
        return null;
    }

	@Override
    public SaicIdpInfo getSaicInfoFull(SearchType type, String username, String keyword, String orgfullid) {
        SaicIdpInfo saicIdpDbInfo = getSaicInfoLocal(keyword);
        if (saicIdpDbInfo == null) {
            log.info("本地没有[{}]工商基础数据", keyword);
        }
        if (saicIdpDbInfo != null && isInValidDays(saicIdpDbInfo)) {
            KycSearchHistoryDto kycSearchHistoryDto = new KycSearchHistoryDto();
            kycSearchHistoryDto.setEntname(saicIdpDbInfo.getName());
            kycSearchHistoryDto.setUsername(username);
            kycSearchHistoryDto.setOrgfullid(orgfullid);
            kycSearchHistoryDto.setSaicinfoId(saicIdpDbInfo.getId());
            kycSearchHistoryService.save(kycSearchHistoryDto);
            //数据在有效期内
            return saicIdpDbInfo;
        } else {
            SaicIdpInfo saicIdpInfo = null;
            String url="";
            if (type == SearchType.REAL_TIME) {
                saicIdpInfo = saicRequestService.getSaicInfoRealTime(keyword, username);
                url = realTimeUrl;
            } else if (type == SearchType.EXACT) {
                saicIdpInfo = saicRequestService.getSaicInfoExact(keyword, username);
                url = exactUrl;
            }
            if(saicIdpInfo != null){
                inesrtSaicInfoByType(username, saicIdpInfo, url,type, orgfullid);
                return saicIdpInfo;
            }else{
                inesrtSaicInfoByNull(username,keyword,url,type, orgfullid);
                return null;
            }
        }
    }
    

    /**
     * 工商企业个人信息补录
     * @return
     */
    @Override
    public JSONObject getCompanyPeopleInformation(SupplementQueryDto supplementQueryVo){
        Long saicInfoId = supplementQueryVo.getSaicInfoId();
        Long id = supplementQueryVo.getId();
        String type = supplementQueryVo.getType();
        SaicInfo saicInfo = saicInfoDao.findById(saicInfoId);
        if(saicInfo == null){
            //工商信息不存在
            throw new EacException("工商号不存在");
        }

        if(StringUtils.isNotBlank(type)){
            if(CommonConstant.PEOPLE_TYPE_STOCKHOLDER.equals(type)){
                //股东
                StockHolder stockHolder = stockHolderDao.findById(id);
                if(!stockHolder.getSaicinfoId().equals(saicInfoId)){
                    //工商信息和个人信息不匹配
                    throw new EacException("工商信息和个人信息不匹配");
                }
                return (JSONObject) JSON.toJSON(stockHolder);
            } else if(CommonConstant.PEOPLE_TYPE_DIRECTORATE.equals(type)){
                //董事
                Director director = directorDao.findById(id);
                if(!director.getSaicinfoId().equals(saicInfoId)){
                    //工商信息和个人信息不匹配
                    throw new EacException("工商信息和个人信息不匹配");
                }
                return (JSONObject) JSON.toJSON(director);
            } else if(CommonConstant.PEOPLE_TYPE_SUPERVISOR.equals(type)){
                //监事
               Supervise supervise = superviseDao.findById(id);
                if(!supervise.getSaicinfoId().equals(saicInfoId)){
                    //工商信息和个人信息不匹配
                    throw new EacException("工商信息和个人信息不匹配");
                }
                return (JSONObject) JSON.toJSON(supervise);
            } else if(CommonConstant.PEOPLE_TYPE_SENIOR.equals(type)){
                //高管
                Management management = managementDao.findById(id);
                if(!management.getSaicinfoId().equals(saicInfoId)){
                    //工商信息和个人信息不匹配
                    throw new EacException("工商信息和个人信息不匹配");
                }
                return (JSONObject) JSON.toJSON(management);
            } else if(CommonConstant.PEOPLE_TYPE_BENEFICIARY.equals(type)){
                //受益所有人
                Beneficiary beneficiary = beneficiaryDao.findById(id);
                if(!beneficiary.getSaicinfoId().equals(saicInfoId)){
                    //工商信息和个人信息不匹配
                    throw new EacException("工商信息和个人信息不匹配");
                }
                return (JSONObject) JSON.toJSON(beneficiary);
            } else if (CommonConstant.PEOPLE_TYPE_ULTIMATE_OWNER.equals(type)) {
                UltimateOwnerPo ultimateOwnerPo = ultimateOwnerDao.findBySaicinfoId(saicInfoId);
                return (JSONObject) JSON.toJSON(ultimateOwnerPo);
            }
        }

        return null;
    }
    


    @Override
    public SupplementDto saveCompanyPeopleInformation(SupplementDto supplementDto){
        Long saicInfoId = supplementDto.getSaicInfoId();
        Long id = supplementDto.getId();
        String type = supplementDto.getType();
        SaicInfo saicInfo = saicInfoDao.findById(saicInfoId);
        if(saicInfo == null){
            //工商信息不存在
            throw new EacException("工商号不存在");
        }

        //
        if(StringUtils.isNotBlank(type)){
            if(CommonConstant.PEOPLE_TYPE_STOCKHOLDER.equals(type)){
                //股东
                StockHolder stockHolder = stockHolderDao.findById(id);
                if(!stockHolder.getSaicinfoId().equals(saicInfoId)){
                    //工商信息和个人信息不匹配
                    throw new EacException("工商信息和个人信息不匹配");
                }

                stockHolder.setAddress(supplementDto.getAddress());
                stockHolder.setIdcardtype(supplementDto.getIdCardType());
                stockHolder.setIdcardno(supplementDto.getIdCardNo());
                stockHolder.setIdcarddue(supplementDto.getIdCardDue());
                stockHolder.setTelephone(supplementDto.getTelephone());
                stockHolder.setSex(supplementDto.getSex());
                stockHolder.setIdCardStart(supplementDto.getIdCardStart());
                stockHolder.setNationality(supplementDto.getNationality());
                stockHolder.setDob(supplementDto.getDob());
                stockHolder.setCondate(supplementDto.getCondate());
                stockHolder.setFundedratio(supplementDto.getFundedratio());
                stockHolder.setRegcapcur(supplementDto.getRegcapcur());
                stockHolder.setRealamount(supplementDto.getRealamount());
                stockHolder.setRealtype(supplementDto.getRealtype());
                stockHolder.setRealdate(supplementDto.getRealdate());
                stockHolder.setSubconam(supplementDto.getSubconam());
                stockHolder.setInvesttype(supplementDto.getInvesttype());
                stockHolderDao.save(stockHolder);
            } else if(CommonConstant.PEOPLE_TYPE_DIRECTORATE.equals(type)){
                //董事
                Director director = directorDao.findById(id);
                if(!director.getSaicinfoId().equals(saicInfoId)){
                    //工商信息和个人信息不匹配
                    throw new EacException("工商信息和个人信息不匹配");
                }
                director.setAddress(supplementDto.getAddress());
                director.setIdcardtype(supplementDto.getIdCardType());
                director.setIdcardno(supplementDto.getIdCardNo());
                director.setIdcarddue(supplementDto.getIdCardDue());
                director.setTelephone(supplementDto.getTelephone());
                director.setSex(supplementDto.getSex());
                director.setIdCardStart(supplementDto.getIdCardStart());
                director.setNationality(supplementDto.getNationality());
                director.setDob(supplementDto.getDob());
                directorDao.save(director);
            } else if(CommonConstant.PEOPLE_TYPE_SUPERVISOR.equals(type)){
                //监事
                Supervise supervise = superviseDao.findById(id);
                if(!supervise.getSaicinfoId().equals(saicInfoId)){
                    //工商信息和个人信息不匹配
                    throw new EacException("工商信息和个人信息不匹配");
                }
                supervise.setAddress(supplementDto.getAddress());
                supervise.setIdcardtype(supplementDto.getIdCardType());
                supervise.setIdcardno(supplementDto.getIdCardNo());
                supervise.setIdcarddue(supplementDto.getIdCardDue());
                supervise.setTelephone(supplementDto.getTelephone());
                supervise.setSex(supplementDto.getSex());
                supervise.setIdCardStart(supplementDto.getIdCardStart());
                supervise.setNationality(supplementDto.getNationality());
                supervise.setDob(supplementDto.getDob());
                superviseDao.save(supervise);
            } else if(CommonConstant.PEOPLE_TYPE_SENIOR.equals(type)){
                //高管
                Management management = managementDao.findById(id);
                if(!management.getSaicinfoId().equals(saicInfoId)){
                    //工商信息和个人信息不匹配
                    throw new EacException("工商信息和个人信息不匹配");
                }
                management.setAddress(supplementDto.getAddress());
                management.setIdcardtype(supplementDto.getIdCardType());
                management.setIdcardno(supplementDto.getIdCardNo());
                management.setIdcarddue(supplementDto.getIdCardDue());
                management.setTelephone(supplementDto.getTelephone());
                management.setSex(supplementDto.getSex());
                management.setIdCardStart(supplementDto.getIdCardStart());
                management.setNationality(supplementDto.getNationality());
                management.setDob(supplementDto.getDob());
                managementDao.save(management);
            } else if(CommonConstant.PEOPLE_TYPE_BENEFICIARY.equals(type)){
                //受益所有人
                Beneficiary beneficiary;
                if (id!=null){
                    beneficiary = beneficiaryDao.findById(id);
                    if(!beneficiary.getSaicinfoId().equals(saicInfoId)){
                        //工商信息和个人信息不匹配
                        throw new EacException("工商信息和个人信息不匹配");
                    }
                }else {
                    beneficiary = new Beneficiary();
                }
                beneficiary.setAddress(supplementDto.getAddress());
                beneficiary.setIdentifytype(supplementDto.getIdCardType());
                beneficiary.setIdentifyno(supplementDto.getIdentifyno());
                beneficiary.setIdcarddue(supplementDto.getIdCardDue());
                beneficiary.setTelephone(supplementDto.getTelephone());
                beneficiary.setSex(supplementDto.getSex());
                beneficiary.setIdCardStart(supplementDto.getIdCardStart());
                beneficiary.setNationality(supplementDto.getNationality());
                beneficiary.setDob(supplementDto.getDob());
                beneficiary.setCapitalpercent(supplementDto.getCapitalpercent());
                beneficiary.setSaicinfoId(supplementDto.getSaicInfoId());
                beneficiary.setType(supplementDto.getBeneficiaryType());//Dto的tpye值被占用，所以用beneficiaryType字段代替
                beneficiary.setName(supplementDto.getName());
                beneficiary = beneficiaryDao.save(beneficiary);
                ConverterService.convert(beneficiary,supplementDto);
            } else if (CommonConstant.PEOPLE_TYPE_ULTIMATE_OWNER.equals(type)) {
                UltimateOwnerPo ultimateOwnerPo = new UltimateOwnerPo();
                if (supplementDto.getId() != null) {
                    ultimateOwnerPo = ultimateOwnerDao.findOne(supplementDto.getId());
                    if (ultimateOwnerPo == null) {
                        ultimateOwnerPo = new UltimateOwnerPo();
                    }
                }

                ultimateOwnerPo.setSaicinfoId(supplementDto.getSaicInfoId());
                ultimateOwnerPo.setName(supplementDto.getName());
                ultimateOwnerPo.setAddress(supplementDto.getAddress());
                ultimateOwnerPo.setIdcardtype(supplementDto.getIdCardType());
                ultimateOwnerPo.setIdcardno(supplementDto.getIdCardNo());
                ultimateOwnerPo.setIdcarddue(supplementDto.getIdCardDue());
                ultimateOwnerPo.setTelephone(supplementDto.getTelephone());
                ultimateOwnerPo.setSex(supplementDto.getSex());
                ultimateOwnerPo.setIdCardStart(supplementDto.getIdCardStart());
                ultimateOwnerPo.setNationality(supplementDto.getNationality());
                ultimateOwnerPo.setDob(supplementDto.getDob());
                ultimateOwnerDao.save(ultimateOwnerPo);
                supplementDto.setId(ultimateOwnerPo.getId());
            }
        }
        return supplementDto;
    }

    /**
     * 从本地库中获取工商基本信息
     *
     * @param keyword
     * @return
     */
    private SaicIdpInfo getSaicInfoLocalJustSaic(String keyword) {
        if (StringUtils.isBlank(keyword)) {
            return null;
        }

        SaicInfo saicInfo = new SaicInfo();
        List<SaicInfo> saicInfoList = saicInfoDao.findSaicInfoByKeyword(keyword);
        if (saicInfoList != null && saicInfoList.size() > 0) {
            saicInfo = saicInfoList.get(0);
        }else {
            return null;
        }
        SaicIdpInfo saicIdpInfo = new SaicIdpInfo();
        BeanCopierUtils.copyProperties(saicInfo, saicIdpInfo);
        //年报
        saicIdpInfo.setReports(reportService.findBySaicInfoId(saicInfo.getId()));
        //经营异常信息
        saicIdpInfo.setChangemess(changeMessService.findBySaicInfoId(saicInfo.getId()));
        //严重违法
        saicIdpInfo.setIllegals(illegalService.findBySaicInfoId(saicInfo.getId()));

        return saicIdpInfo;
    }

    /**
     * 从本地库中获取工商基本信息
     *
     * @param keyword
     * @return
     */
    private SaicIdpInfo getSaicInfoLocal(String keyword) {
        if (StringUtils.isBlank(keyword)) {
            return null;
        }
        keyword = keyword.replaceAll("\\s*", "");
        SaicInfo saicInfo = new SaicInfo();
        List<SaicInfo> saicInfoList = saicInfoDao.findSaicInfoByKeyword(keyword);
        if (saicInfoList != null && saicInfoList.size() > 0) {
            saicInfo = saicInfoList.get(0);
        }else {
            return null;
        }
        SaicIdpInfo saicIdpInfo = new SaicIdpInfo();
        BeanCopierUtils.copyProperties(saicInfo, saicIdpInfo);

        //查询其他信息
        saicIdpInfo.setStockholders(stockHolderService.findBySaicInfoId(saicInfo.getId()));
        //经营异常信息
        saicIdpInfo.setChangemess(changeMessService.findBySaicInfoId(saicInfo.getId()));
        saicIdpInfo.setIllegals(illegalService.findBySaicInfoId(saicInfo.getId()));
        //TODO 后续还需完善其他
//        saicIdpInfo.setEmployees(employeeService.findBySaicInfoId(saicInfo.getId()+""));
        return saicIdpInfo;
    }

    @Override
    public void inesrtSaicInfoByType(String username, SaicIdpInfo saicIdpInfo, String url,SearchType searchType, String orgfullid) {
        //客户尽调历史表
        KycSearchHistoryDto kycSearchHistoryDto = new KycSearchHistoryDto();

        SaicInfo saicInfo = new SaicInfo();
        BeanUtils.copyProperties(saicIdpInfo, saicInfo);

        //保存idp的id到其他字段，id置空
        saicInfo.setIdpId(saicInfo.getId()+"");
        saicInfo.setId(null);

        /**
         * 保存数据
         */
        //生成主键
//            Long saicInfoId = Calendar.getInstance().getTimeInMillis();
//            saicInfo.setId(saicInfoId);
        saicInfo.setCreatedDate(new Date());
        saicInfoDao.save(saicInfo);
        Long saicInfoId = saicInfo.getId();
        saicIdpInfo.setId(saicInfoId);

        //股东
        stockHolderService.insertBatch(saicInfoId, saicIdpInfo.getStockholders());

        //主要成员
        List<EmployeeDto> employeeList = saicIdpInfo.getEmployees();
        employeeService.insertBatch(saicInfoId, employeeList);

        //主要成员->董监高
        List<DirectorDto> directorList = new ArrayList<DirectorDto>();
        List<SuperviseDto> superviseList = new ArrayList<SuperviseDto>();
        List<ManagementDto> managementList = new ArrayList<ManagementDto>();

        for (EmployeeDto employee : employeeList) {
            String type = employee.getType();
            //IDP 返回董监高类型
            if (type != null) {
                if (type.contains(EmployeeType.DongShi.getType())) {
                    directorList.add(generateDirectors(employee));
                }
                if (type.contains(EmployeeType.JianShi.getType())) {
                    superviseList.add(generateSupervise(employee));
                }
                if (type.contains(EmployeeType.GaoGuan.getType())) {
                    managementList.add(generateManagement(employee));
                }
            } else if (employee.getJob() != null) {
                if (employee.getJob().contains(EmployeeType.DongShi.getValue())) {
                    directorList.add(generateDirectors(employee));
                }
                if (employee.getJob().contains(EmployeeType.JianShi.getValue())) {
                    superviseList.add(generateSupervise(employee));
                }
                if (employee.getJob().contains(EmployeeType.GaoGuan.getValue())) {
                    managementList.add(generateManagement(employee));
                }
            }
        }

        //董事
        directorService.insertBatch(saicInfoId, directorList);

        //监事
        superviseService.insertBatch(saicInfoId, superviseList);

        //高管
        managementService.insertBatch(saicInfoId, managementList);

        //经营异常信息
        changeMessService.insertBatch(saicInfoId, saicIdpInfo.getChangemess());

        //严重违法信息
        illegalService.insertBatch(saicInfoId, saicIdpInfo.getIllegals());

        //工商变更信息
        changeRecordService.insertBatch(saicInfoId, saicIdpInfo.getChanges());

        //年报信息
        reportService.insertBatch(saicInfoId,saicIdpInfo.getReports());
        //TODO 处理工商剩余数据

        //保存IDP查询记录
        saicSearchHistoryService.save(saicInfo.getName(), username, url, SearchStatus.SUCCESS,
                searchType, orgfullid);
        kycSearchHistoryDto.setEntname(saicInfo.getName());
        kycSearchHistoryDto.setUsername(username);
        kycSearchHistoryDto.setSearchurl(url);
        kycSearchHistoryDto.setQueryresult(SearchStatus.SUCCESS.name());
        kycSearchHistoryDto.setSearchtype(searchType.name());
        kycSearchHistoryDto.setOrgfullid(orgfullid);
        kycSearchHistoryDto.setSaicinfoId(saicInfoId);

        if(StringUtils.isNotBlank(saicIdpInfo.getBatchNo())) {
            kycSearchHistoryDto.setBatchNo(saicIdpInfo.getBatchNo());
        }

        kycSearchHistoryService.save(kycSearchHistoryDto);
    }


    @Override
    public void inesrtSaicInfoByNull(String username,String keyword, String url,SearchType searchType, String orgfullid){
        //客户尽调历史表
        KycSearchHistoryDto kycSearchHistoryDto = new KycSearchHistoryDto();
        saicSearchHistoryService.save(keyword, username, url, SearchStatus.FAIL,
                searchType, orgfullid);
        kycSearchHistoryDto.setEntname(keyword);
        kycSearchHistoryDto.setUsername(username);
        kycSearchHistoryDto.setSearchurl(url);
        kycSearchHistoryDto.setQueryresult(SearchStatus.SUCCESS.name());
        kycSearchHistoryDto.setSearchtype(searchType.name());
        kycSearchHistoryDto.setOrgfullid(orgfullid);
        kycSearchHistoryService.save(kycSearchHistoryDto);
    }

    @Override
    public void inesrtSaicInfoByNull(String username,String keyword, String url,SearchType searchType, String orgfullid, String batchNo){
        //客户尽调历史表
        KycSearchHistoryDto kycSearchHistoryDto = new KycSearchHistoryDto();
        saicSearchHistoryService.save(keyword, username, url, SearchStatus.FAIL,
                searchType, orgfullid);
        kycSearchHistoryDto.setEntname(keyword);
        kycSearchHistoryDto.setUsername(username);
        kycSearchHistoryDto.setSearchurl(url);
        kycSearchHistoryDto.setQueryresult(SearchStatus.SUCCESS.name());
        kycSearchHistoryDto.setSearchtype(searchType.name());
        kycSearchHistoryDto.setOrgfullid(orgfullid);
        kycSearchHistoryDto.setBatchNo(batchNo);
        kycSearchHistoryService.save(kycSearchHistoryDto);
    }

    /**
     * 生成董事
     *
     * @param employee
     * @return
     */
    private DirectorDto generateDirectors(EmployeeDto employee) {
        DirectorDto director = new DirectorDto();
        director.setName(employee.getName());
        director.setPosition(employee.getJob());
        director.setSex(employee.getSex());
        director.setIndex(employee.getIndex());
        return director;
    }

    /**
     * 生成监事
     *
     * @param employee
     * @return
     */
    private SuperviseDto generateSupervise(EmployeeDto employee) {
        SuperviseDto supervise = new SuperviseDto();
        supervise.setName(employee.getName());
        supervise.setPosition(employee.getJob());
        supervise.setSex(employee.getSex());
        supervise.setIndex(employee.getIndex());
        return supervise;
    }

    /**
     * 生成高管
     *
     * @param employee
     * @return
     */
    private ManagementDto generateManagement(EmployeeDto employee) {
        ManagementDto management = new ManagementDto();
        management.setName(employee.getName());
        management.setPosition(employee.getJob());
        management.setSex(employee.getSex());
        management.setIndex(employee.getIndex());
        return management;
    }

    /**
     * 数据有效期判断
     *
     * @param saicIdpInfo
     * @return
     */
    @Override
    public boolean isInValidDays(SaicIdpInfo saicIdpInfo) {
        //计算有效期
        DateTime now = new DateTime();
        DateTime updateTime = new DateTime(saicIdpInfo.getCreatedDate());
        int days = Days.daysBetween(updateTime, now).getDays();
        if (days >= realTimeValidDays) {
            log.info("本地数据失效,天数{}", days);
            return false;
        } else {
            log.info("本地数据还在有效期内,天数{}", days);
            return true;
        }
    }

    /**
     * 客户尽调操作数据有效期判断
     *
     * @param saicIdpInfo
     * @return
     */
    public boolean isCusTuneInValidDays(SaicIdpInfo saicIdpInfo) {
        //计算有效期
        DateTime now = new DateTime();
        DateTime updateTime = new DateTime(saicIdpInfo.getCreatedDate());
        int days = Days.daysBetween(updateTime, now).getDays();
        if (days >= customTuneValidDays) {
            log.info("本地数据失效,天数{}", days);
            return false;
        } else {
            log.info("本地数据还在有效期内,天数{}", days);
            return true;
        }
    }

	@Override
	public SaicInfoDto findById(Long id) {
		SaicInfo saicInfo = saicInfoDao.findById(id);
		if(saicInfo ==null){
		    return null;
        }
		SaicInfoDto saicInfoDto = new SaicInfoDto();
        BeanUtils.copyProperties(saicInfo, saicInfoDto);
		return saicInfoDto;
	}
	

    /**
     * 基础信息-excel
     * @param saicInfoId
     * @param saicPoi
     * @return
     */
    public IExcelExport generateSaicReport(Long saicInfoId, SaicPoi saicPoi){
        //董监高
        List<Director> directors = directorDao.findBySaicinfoId(saicInfoId);
        List<Supervise> supervises = superviseDao.findBySaicinfoId(saicInfoId);
        List<Management> managements = managementDao.findBySaicinfoId(saicInfoId);

        StringBuilder directorsSb = new StringBuilder();
        for (Director director:directors) {
            directorsSb.append(director.getName()).append(":").append(director.getPosition()).append(";");
        }
        saicPoi.setDirectors(directorsSb.toString());

        StringBuilder superviseSb = new StringBuilder();
        for (Supervise supervise:supervises) {
            superviseSb.append(supervise.getName()).append(":").append(supervise.getPosition()).append(";");
        }
        saicPoi.setSupervises(superviseSb.toString());

        StringBuilder managementSb = new StringBuilder();
        for (Management management:managements) {
            managementSb.append(management.getName()).append(":").append(management.getPosition()).append(";");
        }
        saicPoi.setManagements(managementSb.toString());

        IExcelExport saicRecordExport = new SaicRecordExport();
        List<SaicPoi> recordSaicPoiList = new ArrayList<SaicPoi>();
        recordSaicPoiList.add(saicPoi);
        saicRecordExport.setPoiList(recordSaicPoiList);

        return saicRecordExport;
    }

    /**
     * 最终受益人-excel
     * @param saicInfoId
     * @return
     */
    public IExcelExport generateBeneficiaryReport(Long saicInfoId, String name){
        //受益人
        List<Beneficiary> beneficiaryList = beneficiaryDao.findBySaicinfoId(saicInfoId);

        IExcelExport excelExport = new BeneficiaryRecordExport();
        List<BeneficiaryPoi> recordPoiList = new ArrayList<BeneficiaryPoi>();
        for (Beneficiary beneficiary: beneficiaryList) {
            BeneficiaryPoi beneficiaryPoi = new BeneficiaryPoi();
            BeanUtils.copyProperties(beneficiary,beneficiaryPoi);

            beneficiaryPoi.setCompany(name);

            recordPoiList.add(beneficiaryPoi);
        }
        excelExport.setPoiList(recordPoiList);

        return excelExport;
    }
    
    /**
     * 判断企业是否存在严重违法行为
     * true : 不存在违法或者违法行为已经移除
     * false : 存在违法行为，且未移除
     * @param list
     * @return
     */
    @Override
    public boolean checkIfIllegals(List<IllegalDto> list) {
    	if(list.isEmpty()) {
    		return true;
    	}else {
    		for (IllegalDto illegalDto : list) {
				if(illegalDto !=null && illegalDto.getDateout() == null) {
					return false;
				}
			}
    	}
    	return true;
    }

    

    /**
     * 判断企业是否存在经营异常行为
     * true : 不存在经营异常行为或者经营异常行为已经移除
     * false : 存在经营异常行为，且未移除
     * @param list
     * @return
     */
    @Override
    public boolean checkIfChangeMess(List<ChangeMessDto> list) {
    	if(list.isEmpty()) {
    		return true;
    	}else {
    		for (ChangeMessDto changeMessDto : list) {
				if(changeMessDto !=null && changeMessDto.getOutdate() == null) {
					return false;
				}
			}
    	}
    	return true;
    }
    
    /**
     * 判断企业营业期限正常
     * true : 营业期限正常
     * false :营业期限不正常
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public boolean checkIfDateNormal(String startDate,String endDate) {
    	if(startDate == null || endDate == null) {
    		return true;
    	}else if(startDate.startsWith("9999年") && endDate.startsWith("9999年")) {
    		return true;
    	}else {
    		try {
				return DateUtils.isBetween(new Date(),DateUtils.parseDate(startDate), DateUtils.parseDate(endDate));
			} catch (ParseException e) {
				throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"时间状态出错");
			}
    	}
    	
    }

    /**
     * 判断企业营业状态是否正常
     * true : 营业状态正常
     * false : 营业状态不正常
     * @param state
     * @return
     */
	@Override
	public boolean checkIfStateNormal(String state) {
		if(state == null) {
			return false;
		}
		return StringUtil.containAnyOne(state, STATE_NORMAL);
	}

    /**
     * 判断企业是否正在进行简易注销
     * @param notice
     * @return
     */
    @Override
    public boolean checkIfSimpleCancel(String notice) {
        if ("正在进行简易注销公告".equals(notice)) {
            return false;
        }
        return true;
    }

    /**
     * 查询是否进行过客户尽调
     * @param name	企业名称(全称)
     * @return
     */
    public boolean queryIsKyc(String name) {

        long count = 0;
        if(RegexUtils.isExistChinese(name)){
            count = saicInfoDao.countByName(name);
        }else if(name.length() == 18){
            count = saicInfoDao.countByUnitycreditcode(name);
        }else if(name.length() == 15){
            count = saicInfoDao.countByRegistno(name);
        }
        return count > 0;
    }

    @Override
    public List<Future<Long>> getBatchSaicInfoBase(List<KycSearchHistoryDto> tokens, String batchNo) {
        clearFuture();

        if(tokens.size() > 500) {
            tokens = tokens.subList(0, 500);
        }
        if (CollectionUtils.isNotEmpty(tokens)) {
            Map<String, List<KycSearchHistoryDto>> batchTokens = getKycSearchHistoryTokens(tokens);
            if (MapUtils.isNotEmpty(batchTokens) && batchTokens.size() > 0) {
                SaicInfoQueryExecutor executor = null;
                for (String batch : batchTokens.keySet()) {
                    executor = new SaicInfoQueryExecutor(batchTokens.get(batch));
                    executor.setTransactionManager(transactionManager);
                    executor.setSaicInfoService(this);
                    executor.setBatchNo(batchNo);
                    executor.setUsername(SecurityUtils.getCurrentUsername());
                    executor.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
                    executor.setOrganId(SecurityUtils.getCurrentUser().getOrgId());
                    executor.setSaicMonitorService(saicMonitorService);
                    futureList.add(taskExecutor.submit(executor));
                }
            }
        }

        try {
            valiCollectCompleted();
            //更新最终状态
            log.info("批量客户尽调更新最终状态");
            batchService.finishBatch(batchNo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return futureList;
    }

    @Override
    public void clearFuture(){
        if(futureList !=null && futureList.size()>0){
            for (Iterator<Future<Long>> iterator = futureList.iterator(); iterator.hasNext();) {
                Future<Long> future = iterator.next();
                if(future.isDone()){
                    iterator.remove();
                }else{
                    future.cancel(true);
                    iterator.remove();
                }
            }
        }
        futureList = new ArrayList<Future<Long>>();
    }


    /**
     * 判断采集是否完成
     *
     * @param
     * @throws Exception
     */
    @Override
    public void valiCollectCompleted() throws Exception {
        while(futureList.size()>0){
            for (Iterator<Future<Long>> iterator = futureList.iterator(); iterator.hasNext();) {
                Future<Long> future = iterator.next();
                if(future.isDone()){
                    iterator.remove();
                }
            }
            // 暂停1分钟
            TimeUnit.MILLISECONDS.sleep(10000);
        }
    }

    @Override
    public SaicInfoDto getSaicInfoBase(SearchType type, String username, String keyword, String orgfullid, String batchNo) {
        SaicIdpInfo saicIdpDbInfo = getSaicInfoLocal(keyword);
        //返回的信息
        SaicInfoDto saicInfoDto = new SaicInfoDto();
        if (saicIdpDbInfo == null) {
            log.info("本地没有[{}]工商基础数据", keyword);
        }
        if (saicIdpDbInfo != null && isInValidDays(saicIdpDbInfo)) {
            //数据在有效期内
            BeanCopierUtils.copyProperties(saicIdpDbInfo, saicInfoDto);
            KycSearchHistoryDto kycSearchHistoryDto = new KycSearchHistoryDto();
            kycSearchHistoryDto.setEntname(saicInfoDto.getName());
            kycSearchHistoryDto.setUsername(username);
            kycSearchHistoryDto.setOrgfullid(orgfullid);
            kycSearchHistoryDto.setSaicinfoId(saicInfoDto.getId());
            kycSearchHistoryDto.setBatchNo(batchNo);

            kycSearchHistoryService.save(kycSearchHistoryDto);
            return saicInfoDto;
        } else {
            SaicIdpInfo saicIdpInfo = null;
            String url="";
            if (type == SearchType.REAL_TIME) {
                saicIdpInfo = saicRequestService.getSaicInfoRealTime(keyword);
                url = realTimeUrl;
            } else if (type == SearchType.EXACT) {
                saicIdpInfo = saicRequestService.getSaicInfoExact(keyword);
                url = exactUrl;
            }
            if(saicIdpInfo != null){
                saicIdpInfo.setBatchNo(batchNo);
                inesrtSaicInfoByType(username, saicIdpInfo, url,type, orgfullid);
                BeanUtils.copyProperties(saicIdpInfo,saicInfoDto);
                return saicInfoDto;
            }else{
                inesrtSaicInfoByNull(username,keyword,url,type, orgfullid, batchNo);
                return null;
            }
        }

    }

    @Override
    public void saveSaicInfo(SaicIdpInfo saicIdpInfo) {
        SaicIdpInfo saicIdpInfo1 = getSaicInfoBaseLocal(saicIdpInfo.getName());
        SaicInfo saicInfo = null;
        if(saicIdpInfo1 != null){
            saicInfo = saicInfoDao.findById(saicIdpInfo1.getId());
        }else{
            saicInfo = new SaicInfo();
        }
        BeanUtils.copyProperties(saicIdpInfo, saicInfo);

        //保存idp的id到其他字段，id置空
        saicInfo.setIdpId(saicInfo.getId()+"");
        if(saicInfo.getId() == null){
            saicInfo.setId(null);
        }
        /**
         * 保存数据
         */
        saicInfo.setCreatedDate(new Date());
        saicInfoDao.save(saicInfo);
        Long saicInfoId = saicInfo.getId();
        saicIdpInfo.setId(saicInfoId);

        //股东
        stockHolderService.insertBatch(saicInfoId, saicIdpInfo.getStockholders());

        //主要成员
        List<EmployeeDto> employeeList = saicIdpInfo.getEmployees();
        employeeService.insertBatch(saicInfoId, employeeList);

        //主要成员->董监高
        List<DirectorDto> directorList = new ArrayList<DirectorDto>();
        List<SuperviseDto> superviseList = new ArrayList<SuperviseDto>();
        List<ManagementDto> managementList = new ArrayList<ManagementDto>();

        for (EmployeeDto employee : employeeList) {
            String type = employee.getType();
            //IDP 返回董监高类型
            if (type != null) {
                if (type.contains(EmployeeType.DongShi.getType())) {
                    directorList.add(generateDirectors(employee));
                }
                if (type.contains(EmployeeType.JianShi.getType())) {
                    superviseList.add(generateSupervise(employee));
                }
                if (type.contains(EmployeeType.GaoGuan.getType())) {
                    managementList.add(generateManagement(employee));
                }
            } else if (employee.getJob() != null) {
                if (employee.getJob().contains(EmployeeType.DongShi.getValue())) {
                    directorList.add(generateDirectors(employee));
                }
                if (employee.getJob().contains(EmployeeType.JianShi.getValue())) {
                    superviseList.add(generateSupervise(employee));
                }
                if (employee.getJob().contains(EmployeeType.GaoGuan.getValue())) {
                    managementList.add(generateManagement(employee));
                }
            }
        }

        //董事
        directorService.insertBatch(saicInfoId, directorList);

        //监事
        superviseService.insertBatch(saicInfoId, superviseList);

        //高管
        managementService.insertBatch(saicInfoId, managementList);

        //经营异常信息
        changeMessService.insertBatch(saicInfoId, saicIdpInfo.getChangemess());

        //严重违法信息
        illegalService.insertBatch(saicInfoId, saicIdpInfo.getIllegals());

        //工商变更信息
        changeRecordService.insertBatch(saicInfoId, saicIdpInfo.getChanges());

        //年报信息
        reportService.insertBatch(saicInfoId,saicIdpInfo.getReports());
    }

    @Override
    public EntrustResultDto entrustUpdate(String keyword) {
        EntrustResultDto entrustResultDto = new EntrustResultDto();
        keyword = keyword.replaceAll("\\s*", "");

        try {
            keyword = URLDecoder.decode(keyword, "UTF-8");
            entrustResultDto = saicRequestService.entrustUpdate(keyword);
        } catch (Exception e) {
            log.error(e.getMessage());
            entrustResultDto.setDetails("查询异常");
        }

        if("1".equals(entrustResultDto.getState())) {  //成功发起委托更新
            EntrustUpdateHistoryDto dto = new EntrustUpdateHistoryDto();
            dto.setCompanyName(keyword);
            entrustUpdateHistoryService.save(dto);
        }

        return entrustResultDto;
    }

    @Override
    public EntrustResultDto getEntrustUpdateResult(String keyword) {
        EntrustResultDto entrustUpdateResult = new EntrustResultDto();
        keyword = keyword.replaceAll("\\s*", "");

        try {
            keyword = URLDecoder.decode(keyword, "UTF-8");
            entrustUpdateResult = saicRequestService.getEntrustUpdateResult(keyword);
        } catch (Exception e) {
            log.error(e.getMessage());
            entrustUpdateResult.setState("0");
            entrustUpdateResult.setDetails("查询异常");
        }

        if("1".equals(entrustUpdateResult.getState())) { //委托更新成功后,发起存量查询接口,更新本地最新工商数据
            List<EntrustUpdateHistoryDto> historyDtos = entrustUpdateHistoryService.listByCompanyNameAndUpdateStatus(keyword, false);

            if(CollectionUtils.isNotEmpty(historyDtos)) {  //存在委托更新中的记录
                SaicIdpInfo saicIdpInfo = saicRequestService.getSaicInfoExact(keyword);
                if(saicIdpInfo != null){
                    inesrtSaicInfoByType(SecurityUtils.getCurrentUsername(), saicIdpInfo, exactUrl, SearchType.EXACT,
                            SecurityUtils.getCurrentOrgFullId());
                }else{
                    inesrtSaicInfoByNull(SecurityUtils.getCurrentUsername(), keyword, exactUrl, SearchType.EXACT,
                            SecurityUtils.getCurrentOrgFullId());
                }

                EntrustUpdateHistoryDto entrustUpdateHistoryDto = historyDtos.get(0);
                entrustUpdateHistoryDto.setUpdateStatus(true);
                entrustUpdateHistoryService.save(entrustUpdateHistoryDto);
            }

        }

        return entrustUpdateResult;
    }

    private Map<String, List<KycSearchHistoryDto>> getKycSearchHistoryTokens(List<KycSearchHistoryDto> tokens) {
        Map<String, List<KycSearchHistoryDto>> returnMap = new HashMap<>(16);
        if (tokens != null && tokens.size() > 0) {
            int allLeafSum = tokens.size();
            int tokensNum = (allLeafSum / customerTuneExecutorNum) + 1;
            int num = 0;
            int batchNum = 0;
            List<KycSearchHistoryDto> batchTokens = new ArrayList<>();
            for (KycSearchHistoryDto kycSearchHistoryDto : tokens) {
                if (num > 0 && num % tokensNum == 0) {
                    batchNum++;
                    returnMap.put("第" + batchNum + "线程", batchTokens);
                    batchTokens = new ArrayList<>();
                }
                batchTokens.add(kycSearchHistoryDto);
                num++;
            }
            returnMap.put("第" + (batchNum + 1) + "线程", batchTokens);
        }
        return returnMap;
    }

}
