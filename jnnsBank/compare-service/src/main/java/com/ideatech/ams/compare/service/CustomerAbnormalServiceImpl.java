package com.ideatech.ams.compare.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.ideatech.ams.compare.dao.*;
import com.ideatech.ams.compare.dto.CompareResultDto;
import com.ideatech.ams.compare.dto.CompareResultSaicCheckDto;
import com.ideatech.ams.compare.dto.CustomerAbnormalDto;
import com.ideatech.ams.compare.dto.CustomerAbnormalSearchDto;
import com.ideatech.ams.compare.entity.CompareResultSaicCheck;
import com.ideatech.ams.compare.entity.CompareTask;
import com.ideatech.ams.compare.poi.CustomerAbnormalExport;
import com.ideatech.ams.compare.poi.CustomerAbnormalPoi;
import com.ideatech.ams.compare.spec.CustomerAbnormalSpec;
import com.ideatech.ams.compare.view.CrscNewAllHistoryView;
import com.ideatech.ams.compare.view.CrscNewAllView;
import com.ideatech.ams.kyc.dto.ChangeMessDto;
import com.ideatech.ams.kyc.dto.IllegalDto;
import com.ideatech.ams.kyc.dto.SaicIdpInfo;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.permission.service.PermissionService;
import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.ams.system.user.service.UserService;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.*;

/**
 * @author jzh
 * @date 2019/6/13.
 */

@Service
@Slf4j
public class CustomerAbnormalServiceImpl implements CustomerAbnormalService {

    @Autowired
    @Qualifier("compareResultDao")
    private CompareResultDao compareResultDao;

    @Autowired
    private CompareResultSaicCheckDao compareResultSaicCheckDao;

    @Autowired
    private CrscNewAllViewDao crscNewAllViewDao;

    @Autowired
    private CrscNewAllHistoryViewDao crscNewAllHistoryViewDao;

    @Autowired
    private CompareTaskDao compareTaskDao;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationService organizationService;

    @PersistenceContext
    private EntityManager em; //注入EntityManager

    @Override
    public TableResultResponse<CustomerAbnormalDto> page(final CustomerAbnormalSearchDto customerAbnormalSearchDto, Pageable pageable) {
        final String organFullId = SecurityUtils.getCurrentOrgFullId();
        Specification<CrscNewAllView> specification = new Specification<CrscNewAllView>() {
            @Override
            public Predicate toPredicate(Root<CrscNewAllView> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();

                Path<String> organFullIdPath = root.get("organFullId");
                Path<Boolean> abnormalPath = root.get("abnormal");
                Path<String> depositorNamePath = root.get("depositorName");//客户名称
                Path<String> organNamePath = root.get("organName");//银行机构名称
                Path<String> codePath = root.get("code");//银行机构代码
                Path<String> abnormalTimePath = root.get("abnormalTime");//系统异动时间
                Path<Boolean> illegalPath = root.get("illegal");//严重违法
                Path<Boolean> changeMessPath = root.get("changeMess");//经营异常
                Path<Boolean> businessExpiresPath = root.get("businessExpires");//经营到期
                Path<Boolean> abnormalStatePath = root.get("abnormalState");//工商状态异常
                Path<Boolean> changedPath = root.get("changed");//登记信息异动
                Path<String> processStatePath = root.get("processState");//处理状态
                Path<String> processTimePath = root.get("processTime");//处理时间
                Path<String> processerPath = root.get("processer");//处理人

                expressions.add(cb.like(organFullIdPath, organFullId + "%"));
                expressions.add(cb.equal(abnormalPath, true));

                if (StringUtils.isNotBlank(customerAbnormalSearchDto.getDepositorName())) {//客户名称
                    expressions.add(cb.like(depositorNamePath, "%" + customerAbnormalSearchDto.getDepositorName() + "%"));
                }
                if (StringUtils.isNotBlank(customerAbnormalSearchDto.getOrganName())) {//银行机构名称
                    expressions.add(cb.like(organNamePath, "%" + customerAbnormalSearchDto.getOrganName() + "%"));
                }
                if (StringUtils.isNotBlank(customerAbnormalSearchDto.getCode())) {//银行机构代码
                    expressions.add(cb.like(codePath, "%" + customerAbnormalSearchDto.getCode() + "%"));
                }
                if (StringUtils.isNotBlank(customerAbnormalSearchDto.getBeginDate())) {//系统异动开始时间
                    expressions.add(cb.greaterThanOrEqualTo(abnormalTimePath, customerAbnormalSearchDto.getBeginDate()));
                }
                if (StringUtils.isNotBlank(customerAbnormalSearchDto.getEndDate())) {//系统异动结束时间
                    expressions.add(cb.lessThanOrEqualTo(abnormalTimePath, customerAbnormalSearchDto.getEndDate()));
                }
                if (customerAbnormalSearchDto.getIllegal() != null) {//严重违法
                    expressions.add(cb.equal(illegalPath, customerAbnormalSearchDto.getIllegal()));
                }
                if (customerAbnormalSearchDto.getChangeMess() != null) {//经营异常
                    expressions.add(cb.equal(changeMessPath, customerAbnormalSearchDto.getChangeMess()));
                }
                if (customerAbnormalSearchDto.getBusinessExpires() != null) {//经营到期
                    expressions.add(cb.equal(businessExpiresPath, customerAbnormalSearchDto.getBusinessExpires()));
                }
                if (customerAbnormalSearchDto.getAbnormalState() != null) {//工商状态异常
                    expressions.add(cb.equal(abnormalStatePath, customerAbnormalSearchDto.getAbnormalState()));
                }
                if (customerAbnormalSearchDto.getChanged() != null) {//登记信息异动
                    expressions.add(cb.equal(changedPath, customerAbnormalSearchDto.getChanged()));
                }
                if (StringUtils.isNotBlank(customerAbnormalSearchDto.getProcessState())) {//处理状态
                    if (customerAbnormalSearchDto.getProcessState().equals("1")) {//待处理
                        expressions.add(cb.isNull(processStatePath));
                    } else if (customerAbnormalSearchDto.getProcessState().equals("2")) {//处理中
                        expressions.add(cb.equal(processStatePath, "underway"));
                    } else if (customerAbnormalSearchDto.getProcessState().equals("3")) {//已处理
                        expressions.add(cb.equal(processStatePath, "finish"));
                    }
                }
                if (StringUtils.isNotBlank(customerAbnormalSearchDto.getProcessTimeBeginDate())) {//处理开始时间
                    expressions.add(cb.greaterThanOrEqualTo(processTimePath, customerAbnormalSearchDto.getProcessTimeBeginDate()));
                }
                if (StringUtils.isNotBlank(customerAbnormalSearchDto.getProcessTimeEndDate())) {//处理结束时间
                    expressions.add(cb.lessThanOrEqualTo(processTimePath, customerAbnormalSearchDto.getProcessTimeEndDate()));
                }
                if (StringUtils.isNotBlank(customerAbnormalSearchDto.getProcesser())) {//处理人
                    expressions.add(cb.like(processerPath, "%" + customerAbnormalSearchDto.getProcesser() + "%"));
                }
                return predicate;
            }
        };
        Page<CrscNewAllView> compareResultSaicCheckPage = crscNewAllViewDao.findAll(specification, pageable);
        return new TableResultResponse<>((int)compareResultSaicCheckPage.getTotalElements(),
                ConverterService.convertToList(compareResultSaicCheckPage.getContent(),CustomerAbnormalDto.class));
    }

    @Override
    public TableResultResponse<CustomerAbnormalDto> pageHistory(final CustomerAbnormalSearchDto customerAbnormalSearchDto, Pageable pageable) {
        final String organFullId = SecurityUtils.getCurrentOrgFullId();
        Specification<CrscNewAllHistoryView> specification = new Specification<CrscNewAllHistoryView>() {
            @Override
            public Predicate toPredicate(Root<CrscNewAllHistoryView> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                Path<String> depositorNamePath = root.get("depositorName");//客户名称
                Path<String> organFullIdPath = root.get("organFullId");
                Path<Boolean> abnormalPath = root.get("abnormal");

                expressions.add(cb.like(organFullIdPath, organFullId + "%"));
                expressions.add(cb.equal(abnormalPath, true));

                if (StringUtils.isNotBlank(customerAbnormalSearchDto.getDepositorName())) {//客户名称
                    expressions.add(cb.like(depositorNamePath, "%" + customerAbnormalSearchDto.getDepositorName() + "%"));
                }

                return predicate;
            }
        };
        Page<CrscNewAllHistoryView> compareResultSaicCheckPage = crscNewAllHistoryViewDao.findAll(specification, pageable);
        return new TableResultResponse<>((int)compareResultSaicCheckPage.getTotalElements(),
                ConverterService.convertToList(compareResultSaicCheckPage.getContent(),CustomerAbnormalDto.class));
    }

    @Override
    public List<CustomerAbnormalDto> list(Long taskId) {
        Long newTaskId = null;
        List<CompareTask> compareTaskList = compareTaskDao.findAllByNameLikeAndCreateNameOrderByCreatedDateDesc("%工商异常校验%","虚拟用户");
        if (compareTaskList.size()!=0) {
            newTaskId = compareTaskList.get(0).getId();
            if (newTaskId.equals(taskId)){
                return null;
            }
            List<CompareResultSaicCheck> compareResultSaicCheckList = compareResultSaicCheckDao.findAllByCompareTaskId(taskId);
            List<CompareResultSaicCheck> compareResultSaicCheckList2 = compareResultSaicCheckDao.findAllByCompareTaskId(newTaskId);
            return incrementalCheck(compareResultSaicCheckList,compareResultSaicCheckList2);
        }
        return null;
    }

    /**
     * 增量校验
     * @param oldList
     * @param newList
     * @return
     */
    private List<CustomerAbnormalDto> incrementalCheck(List<CompareResultSaicCheck> oldList,List<CompareResultSaicCheck> newList) {
        if (oldList==null||oldList.size()==0){
            return null;
        }
        if (newList==null||newList.size()==0){
            return null;
        }

        Iterator<CompareResultSaicCheck> checkIterator = newList.iterator();
        while (checkIterator.hasNext()){
            CompareResultSaicCheck check = checkIterator.next();
            for (CompareResultSaicCheck c : oldList){
                if (c.getDepositorName().equals(check.getDepositorName())){
                    checkIterator.remove();
                }
                //TODO 增加详细增量校验规则
            }
        }
        return ConverterService.convertToList(newList,CustomerAbnormalDto.class);
    }

    @Override
    public CustomerAbnormalDto findOneByCustomerName(String name) {
        CompareResultSaicCheck compareResultSaicCheck = compareResultSaicCheckDao.findTopByDepositorNameOrderByCreatedDateDesc(name);
        if (compareResultSaicCheck!=null){
            return ConverterService.convert(compareResultSaicCheck,CustomerAbnormalDto.class);
        }
        return null;
    }

    @Override
    public IExcelExport export(final CustomerAbnormalSearchDto customerAbnormalSearchDto) {
        IExcelExport iExcelExport = new CustomerAbnormalExport();
        List<CustomerAbnormalPoi> customerAbnormalPoiList = new ArrayList<>();

        final String organFullId = SecurityUtils.getCurrentOrgFullId();
        Specification<CrscNewAllView> specification = new Specification<CrscNewAllView>() {
            @Override
            public Predicate toPredicate(Root<CrscNewAllView> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();

                Path<String> organFullIdPath = root.get("organFullId");
                Path<Boolean> abnormalPath = root.get("abnormal");
                Path<String> depositorNamePath = root.get("depositorName");//客户名称
                Path<String> organNamePath = root.get("organName");//银行机构名称
                Path<String> codePath = root.get("code");//银行机构代码
                Path<String> abnormalTimePath = root.get("abnormalTime");//系统异动时间
                Path<Boolean> illegalPath = root.get("illegal");//严重违法
                Path<Boolean> changeMessPath = root.get("changeMess");//经营异常
                Path<Boolean> businessExpiresPath = root.get("businessExpires");//经营到期
                Path<Boolean> abnormalStatePath = root.get("abnormalState");//工商状态异常
                Path<Boolean> changedPath = root.get("changed");//登记信息异动
                Path<String> processStatePath = root.get("processState");//处理状态
                Path<String> processTimePath = root.get("processTime");//处理时间
                Path<String> processerPath = root.get("processer");//处理人

                expressions.add(cb.like(organFullIdPath, organFullId + "%"));
                expressions.add(cb.equal(abnormalPath, true));

                if (StringUtils.isNotBlank(customerAbnormalSearchDto.getDepositorName())) {//客户名称
                    expressions.add(cb.like(depositorNamePath, "%" + customerAbnormalSearchDto.getDepositorName() + "%"));
                }
                if (StringUtils.isNotBlank(customerAbnormalSearchDto.getOrganName())) {//银行机构名称
                    expressions.add(cb.like(organNamePath, "%" + customerAbnormalSearchDto.getOrganName() + "%"));
                }
                if (StringUtils.isNotBlank(customerAbnormalSearchDto.getCode())) {//银行机构代码
                    expressions.add(cb.like(codePath, "%" + customerAbnormalSearchDto.getCode() + "%"));
                }
                if (StringUtils.isNotBlank(customerAbnormalSearchDto.getBeginDate())) {//系统异动开始时间
                    expressions.add(cb.greaterThanOrEqualTo(abnormalTimePath, customerAbnormalSearchDto.getBeginDate()));
                }
                if (StringUtils.isNotBlank(customerAbnormalSearchDto.getEndDate())) {//系统异动结束时间
                    expressions.add(cb.lessThanOrEqualTo(abnormalTimePath, customerAbnormalSearchDto.getEndDate()));
                }
                if (customerAbnormalSearchDto.getIllegal() != null) {//严重违法
                    expressions.add(cb.equal(illegalPath, customerAbnormalSearchDto.getIllegal()));
                }
                if (customerAbnormalSearchDto.getChangeMess() != null) {//经营异常
                    expressions.add(cb.equal(changeMessPath, customerAbnormalSearchDto.getChangeMess()));
                }
                if (customerAbnormalSearchDto.getBusinessExpires() != null) {//经营到期
                    expressions.add(cb.equal(businessExpiresPath, customerAbnormalSearchDto.getBusinessExpires()));
                }
                if (customerAbnormalSearchDto.getAbnormalState() != null) {//工商状态异常
                    expressions.add(cb.equal(abnormalStatePath, customerAbnormalSearchDto.getAbnormalState()));
                }
                if (customerAbnormalSearchDto.getChanged() != null) {//登记信息异动
                    expressions.add(cb.equal(changedPath, customerAbnormalSearchDto.getChanged()));
                }
                if (StringUtils.isNotBlank(customerAbnormalSearchDto.getProcessState())) {//处理状态
                    if (customerAbnormalSearchDto.getProcessState().equals("1")) {//待处理
                        expressions.add(cb.isNull(processStatePath));
                    } else if (customerAbnormalSearchDto.getProcessState().equals("2")) {//处理中
                        expressions.add(cb.equal(processStatePath, "underway"));
                    } else if (customerAbnormalSearchDto.getProcessState().equals("3")) {//已处理
                        expressions.add(cb.equal(processStatePath, "finish"));
                    }
                }
                if (StringUtils.isNotBlank(customerAbnormalSearchDto.getProcessTimeBeginDate())) {//处理开始时间
                    expressions.add(cb.greaterThanOrEqualTo(processTimePath, customerAbnormalSearchDto.getProcessTimeBeginDate()));
                }
                if (StringUtils.isNotBlank(customerAbnormalSearchDto.getProcessTimeEndDate())) {//处理结束时间
                    expressions.add(cb.lessThanOrEqualTo(processTimePath, customerAbnormalSearchDto.getProcessTimeEndDate()));
                }
                if (StringUtils.isNotBlank(customerAbnormalSearchDto.getProcesser())) {//处理人
                    expressions.add(cb.like(processerPath, "%" + customerAbnormalSearchDto.getProcesser() + "%"));
                }
                return predicate;
            }
        };

        List<CrscNewAllView> crscNewAllViewList = crscNewAllViewDao.findAll(specification);

        for (CrscNewAllView c : crscNewAllViewList){
            CustomerAbnormalPoi customerAbnormalPoi = new CustomerAbnormalPoi();
            customerAbnormalPoi.setDepositorName(c.getDepositorName());
            customerAbnormalPoi.setCode(c.getCode());
            customerAbnormalPoi.setOrganName(c.getOrganName());
            customerAbnormalPoi.setIllegal(c.getIllegal() == null ? "" : (c.getIllegal() ? "是" : "否"));
            customerAbnormalPoi.setChangeMess(c.getChangeMess() == null ? "" : (c.getChangeMess() ? "是" : "否"));
            customerAbnormalPoi.setBusinessExpires(c.getBusinessExpires() == null ? "" : (c.getBusinessExpires() ? "是" : "否"));
            customerAbnormalPoi.setAbnormalState(c.getAbnormalState() == null ? "" : (c.getAbnormalState() ? "是" : "否"));
            customerAbnormalPoi.setChanged(c.getChanged() == null ? "" : (c.getChanged() ? "是" : "否"));
            customerAbnormalPoi.setAbnormalTime(c.getAbnormalTime());
            String state = "待处理";
            if ("finish".equals(c.getProcessState())){
                state = "已处理";
            }
            customerAbnormalPoi.setProcessState(state);
            customerAbnormalPoi.setProcessTime(c.getProcessTime());
            customerAbnormalPoi.setProcesser(c.getProcesser());
            String msg = "未发送";//（1发送成功 2发送失败 0未发送）
            if("1".equals(c.getMessage())){
                msg = "发送成功";
            }else if ("2".equals(c.getMessage())){
                msg = "发送失败";
            }
            customerAbnormalPoi.setMessage(msg);
            customerAbnormalPoiList.add(customerAbnormalPoi);
        }

        iExcelExport.setPoiList(customerAbnormalPoiList);
        return iExcelExport;
    }

    /**
     * 基本信息
     * @param saicInfo
     * @param wb
     * @return
     */
    @Override
    public HSSFWorkbook getBaseInfoWorkbook(SaicIdpInfo saicInfo, HSSFWorkbook wb) {
        // 第1步，创建一个HSSFWorkbook，对应一个Excel文件
        if(wb == null){
            wb = new HSSFWorkbook();
        }

        // 第2步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.getSheet("基础信息");
        if (sheet==null){
            sheet = wb.createSheet("基础信息");
        }else {
            log.warn("HSSFWorkbook存在基础信息sheet页");
            return wb;
        }

        // 第3步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);// 水平居中格式
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        style.setWrapText(true);//自动换行
        //设置列宽
        sheet.setColumnWidth(0,5000);
        sheet.setColumnWidth(1,10000);

        //声明行对象
        HSSFRow row = null;
        //声明列对象
        HSSFCell cell = null;


        //第4步，填充数据
        int i = 0;
        row = sheet.createRow(i++);
        cell = row.createCell(0);
        cell.setCellValue("客户名称");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue(saicInfo.getName());
        cell.setCellStyle(style);

        row = sheet.createRow(i++);
        cell = row.createCell(0);
        cell.setCellValue("统一社会信用代码");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue(saicInfo.getUnitycreditcode());
        cell.setCellStyle(style);

        row = sheet.createRow(i++);
        cell = row.createCell(0);
        cell.setCellValue("注册号");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue(saicInfo.getRegistno());
        cell.setCellStyle(style);

        row = sheet.createRow(i++);
        cell = row.createCell(0);
        cell.setCellValue("公司类型");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue(saicInfo.getType());
        cell.setCellStyle(style);

        row = sheet.createRow(i++);
        cell = row.createCell(0);
        cell.setCellValue("法人代表");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue(saicInfo.getLegalperson());
        cell.setCellStyle(style);

        row = sheet.createRow(i++);
        cell = row.createCell(0);
        cell.setCellValue("经营状态");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue(saicInfo.getState());
        cell.setCellStyle(style);

        row = sheet.createRow(i++);
        cell = row.createCell(0);
        cell.setCellValue("成立日期");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue(saicInfo.getOpendate());
        cell.setCellStyle(style);

        row = sheet.createRow(i++);
        cell = row.createCell(0);
        cell.setCellValue("营业期限");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue(saicInfo.getStartdate()+"-"+saicInfo.getEnddate());
        cell.setCellStyle(style);

        row = sheet.createRow(i++);
        cell = row.createCell(0);
        cell.setCellValue("注册资本");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue(saicInfo.getRegistfund());
        cell.setCellStyle(style);

        row = sheet.createRow(i++);
        cell = row.createCell(0);
        cell.setCellValue("核准日期");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue(saicInfo.getLicensedate());
        cell.setCellStyle(style);

        row = sheet.createRow(i++);
        cell = row.createCell(0);
        cell.setCellValue("登记机关");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue(saicInfo.getRegistorgan());
        cell.setCellStyle(style);

        row = sheet.createRow(i++);
        cell = row.createCell(0);
        cell.setCellValue("企业地址");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue(saicInfo.getAddress());
        cell.setCellStyle(style);

        row = sheet.createRow(i++);
        cell = row.createCell(0);
        cell.setCellValue("经营范围");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue(saicInfo.getScope());
        cell.setCellStyle(style);

        return wb;
    }

    /**
     * 严重违法
     * @param saicInfo
     * @param wb
     * @return
     */
    @Override
    public HSSFWorkbook getIllegalsWorkbook(SaicIdpInfo saicInfo, HSSFWorkbook wb) {
        // 第1步，创建一个HSSFWorkbook，对应一个Excel文件
        if(wb == null){
            wb = new HSSFWorkbook();
        }

        // 第2步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.getSheet("严重违法");
        if (sheet==null){
            sheet = wb.createSheet("严重违法");
        }else {
            log.warn("HSSFWorkbook存在严重违法sheet页");
            return wb;
        }

        // 第3步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);// 水平居中格式
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        style.setWrapText(true);//自动换行

        //设置列宽
        sheet.setColumnWidth(1,10000);
        sheet.setColumnWidth(2,4000);
        sheet.setColumnWidth(3,8000);
        sheet.setColumnWidth(4,10000);
        sheet.setColumnWidth(5,4000);
        sheet.setColumnWidth(6,8000);

        //声明行对象
        HSSFRow row = null;
        //声明列对象
        HSSFCell cell = null;


        //第4步，填充数据
        String[] title = {"类别","列入严重违法\n失信企业名单（黑名单）原因","列入日期","做出决定机关（列入）",
                "移出严重违法\n失信企业名单（黑名单）原因","移出日期","做出决定机关（移出）"};
        row = sheet.createRow(0);
        for(int i=0;i<title.length;i++){
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }

        if (saicInfo.getIllegals()!=null && saicInfo.getIllegals().size()!=0){
            List<IllegalDto> illegalDtoList = saicInfo.getIllegals();
            for (int i = 0;i<illegalDtoList.size();i++){
                row = sheet.createRow(i+1);
                int j = 0;
                cell = row.createCell(j++);
                cell.setCellValue(illegalDtoList.get(i).getType());//类别
                cell.setCellStyle(style);

                cell = row.createCell(j++);
                cell.setCellValue(illegalDtoList.get(i).getReason());//列入严重违法失信企业名单（黑名单）原因
                cell.setCellStyle(style);

                cell = row.createCell(j++);
                cell.setCellValue(illegalDtoList.get(i).getDate());//列入日期
                cell.setCellStyle(style);

                cell = row.createCell(j++);
                cell.setCellValue(illegalDtoList.get(i).getOrgan());//做出决定机关(列入)
                cell.setCellStyle(style);

                cell = row.createCell(j++);
                cell.setCellValue(illegalDtoList.get(i).getReasonout());//移出严重违法失信企业名单(黑名单)原因
                cell.setCellStyle(style);

                cell = row.createCell(j++);
                cell.setCellValue(illegalDtoList.get(i).getDateout());//移出日期
                cell.setCellStyle(style);

                cell = row.createCell(j);
                cell.setCellValue(illegalDtoList.get(i).getOrganout());//作出决定机关(移出)
                cell.setCellStyle(style);
            }
        }
        return wb;
    }

    /**
     * 经营异常
     * @param saicInfo
     * @param wb
     * @return
     */
    @Override
    public HSSFWorkbook getChangeMessWorkbook(SaicIdpInfo saicInfo, HSSFWorkbook wb) {

        // 第1步，创建一个HSSFWorkbook，对应一个Excel文件
        if(wb == null){
            wb = new HSSFWorkbook();
        }

        // 第2步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.getSheet("经营异常");
        if (sheet==null){
            sheet = wb.createSheet("经营异常");
        }else {
            log.warn("HSSFWorkbook存在经营异常sheet页");
            return wb;
        }

        // 第3步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);// 水平居中格式
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        style.setWrapText(true);//自动换行

        //设置列宽
        sheet.setColumnWidth(0,10000);
        sheet.setColumnWidth(1,4000);
        sheet.setColumnWidth(2,8000);
        sheet.setColumnWidth(3,10000);
        sheet.setColumnWidth(4,4000);

        //声明行对象
        HSSFRow row = null;
        //声明列对象
        HSSFCell cell = null;


        //第4步，填充数据
        String[] title = {"列入经营异常名录原因","列入日期", "移出经营异常名录原因","移出日期","作出决定机关"};
        row = sheet.createRow(0);
        for(int i=0;i<title.length;i++){
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }

        if (saicInfo.getChangemess()!=null && saicInfo.getChangemess().size()!=0){
            List<ChangeMessDto> changeMessDtoList = saicInfo.getChangemess();
            for (int i = 0;i<changeMessDtoList.size();i++){
                row = sheet.createRow(i+1);
                int j = 0;
                cell = row.createCell(j++);
                cell.setCellValue(changeMessDtoList.get(i).getInreason());//列入经营异常名录原因
                cell.setCellStyle(style);

                cell = row.createCell(j++);
                cell.setCellValue(changeMessDtoList.get(i).getIndate());//列入日期
                cell.setCellStyle(style);

                cell = row.createCell(j++);
                cell.setCellValue(changeMessDtoList.get(i).getOutreason());//移出经营异常名录原因
                cell.setCellStyle(style);

                cell = row.createCell(j++);
                cell.setCellValue(changeMessDtoList.get(i).getOutdate());//移出日期
                cell.setCellStyle(style);

                cell = row.createCell(j);
                cell.setCellValue(changeMessDtoList.get(i).getBelongorg());//作出决定机关
                cell.setCellStyle(style);

            }
        }
        return wb;
    }

    /**
     * 营业到期
     * @param saicInfo
     * @param wb
     * @return
     */
    @Override
    public HSSFWorkbook getBusinessExpiresWorkbook(SaicIdpInfo saicInfo, CompareResultSaicCheckDto checkDto, HSSFWorkbook wb) {
        // 第1步，创建一个HSSFWorkbook，对应一个Excel文件
        if(wb == null){
            wb = new HSSFWorkbook();
        }

        // 第2步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.getSheet("营业到期");
        if (sheet==null){
            sheet = wb.createSheet("营业到期");
        }else {
            log.warn("HSSFWorkbook存在营业到期sheet页");
            return wb;
        }

        // 第3步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);// 水平居中格式
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        style.setWrapText(true);//自动换行

        //设置列宽
        sheet.setColumnWidth(0,10000);
        sheet.setColumnWidth(1,6000);
        sheet.setColumnWidth(2,6000);
        sheet.setColumnWidth(3,10000);

        //声明行对象
        HSSFRow row = null;
        //声明列对象
        HSSFCell cell = null;


        //第4步，填充数据
        String[] title = {"客户名称","所属银行名称", "系统异动日期","营业期限"};
        row = sheet.createRow(0);
        for(int i=0;i<title.length;i++){
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }

        if (checkDto.getBusinessExpires()){
            row = sheet.createRow(1);

            cell = row.createCell(0);
            cell.setCellValue(checkDto.getDepositorName());
            cell.setCellStyle(style);

            cell = row.createCell(1);
            cell.setCellValue(checkDto.getOrganName());
            cell.setCellStyle(style);

            cell = row.createCell(2);
            cell.setCellValue(checkDto.getAbnormalTime());
            cell.setCellStyle(style);

            cell = row.createCell(3);
            cell.setCellValue(saicInfo.getStartdate()+"-"+saicInfo.getEnddate());
            cell.setCellStyle(style);
        }

        return wb;
    }

    /**
     * 工商状态
     * @param checkDto
     * @param wb
     * @return
     */
    @Override
    public HSSFWorkbook getSaicStateWorkbook(CompareResultSaicCheckDto checkDto,HSSFWorkbook wb) {
        // 第1步，创建一个HSSFWorkbook，对应一个Excel文件
        if(wb == null){
            wb = new HSSFWorkbook();
        }

        // 第2步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.getSheet("工商状态异常");
        if (sheet==null){
            sheet = wb.createSheet("工商状态异常");
        }else {
            log.warn("HSSFWorkbook存在工商状态异常sheet页");
            return wb;
        }

        // 第3步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);// 水平居中格式
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        style.setWrapText(true);//自动换行

        //设置列宽
        sheet.setColumnWidth(0,10000);
        sheet.setColumnWidth(1,6000);
        sheet.setColumnWidth(2,6000);
        sheet.setColumnWidth(3,6000);

        //声明行对象
        HSSFRow row = null;
        //声明列对象
        HSSFCell cell = null;


        //第4步，填充数据
        String[] title = {"客户名称","所属银行名称", "系统异动日期","工商状态"};
        row = sheet.createRow(0);
        for(int i=0;i<title.length;i++){
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }

        if (checkDto.getAbnormalState()){
            row = sheet.createRow(1);

            cell = row.createCell(0);
            cell.setCellValue(checkDto.getDepositorName());
            cell.setCellStyle(style);

            cell = row.createCell(1);
            cell.setCellValue(checkDto.getOrganName());
            cell.setCellStyle(style);

            cell = row.createCell(2);
            cell.setCellValue(checkDto.getAbnormalTime());
            cell.setCellStyle(style);

            cell = row.createCell(3);
            cell.setCellValue(checkDto.getSaicState().getName());
            cell.setCellStyle(style);
        }

        return wb;
    }

    /**
     * 工商登记信息异动
     * @param compareResultDto
     * @param wb
     * @return
     */
    @Override
    public HSSFWorkbook getChangedWorkbook(CompareResultSaicCheckDto checkDto,CompareResultDto compareResultDto, HSSFWorkbook wb) {
        // 第1步，创建一个HSSFWorkbook，对应一个Excel文件
        if(wb == null){
            wb = new HSSFWorkbook();
        }

        // 第2步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.getSheet("登记信息异动");
        if (sheet==null){
            sheet = wb.createSheet("登记信息异动");
        }else {
            log.warn("HSSFWorkbook存在登记信息异动sheet页");
            return wb;
        }

        // 第3步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);// 水平居中格式
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        style.setWrapText(true);//自动换行

        //设置列宽
        sheet.setColumnWidth(0,10000);
        sheet.setColumnWidth(1,6000);
        sheet.setColumnWidth(2,6000);
        sheet.setColumnWidth(3,6000);
        sheet.setColumnWidth(4,10000);
        sheet.setColumnWidth(5,10000);

        //声明行对象
        HSSFRow row = null;
        //声明列对象
        HSSFCell cell = null;


        //第4步，填充数据
        String[] title = {"客户名称","所属银行名称", "系统异动日期","异动内容","异动前内容","异动后内容"};
        row = sheet.createRow(0);
        for(int i=0;i<title.length;i++){
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }
        String details = compareResultDto.getDetails();
        if (details!=null && !"".equals(details)){
            JSONObject jsonObject = JSONObject.parseObject(details);
            JSONObject data = jsonObject.getJSONObject("data");
            JSONObject match = jsonObject.getJSONObject("match");
            JSONObject before = data.getJSONObject("客户异动数据");
            JSONObject after = data.getJSONObject("工商异动数据");

            int i = 1;

            //start 工商异常校验比对时，比对规则勾选的字段 见AbnormalCheckInitializer start
            if (!match.getBoolean("depositorName")){
                row = sheet.createRow(i++);
                int j = 0;
                cell = row.createCell(j++);
                cell.setCellValue(checkDto.getDepositorName());//客户名称
                cell.setCellStyle(style);
                cell = row.createCell(j++);
                cell.setCellValue(checkDto.getOrganName());//所属银行名称
                cell.setCellStyle(style);
                cell = row.createCell(j++);
                cell.setCellValue(checkDto.getAbnormalTime());//系统异动时间
                cell.setCellStyle(style);
                cell = row.createCell(j++);
                cell.setCellValue("企业名称");//异动内容
                cell.setCellStyle(style);

                cell = row.createCell(j++);
                cell.setCellValue(before.getString("depositorName"));//异动前
                cell.setCellStyle(style);

                cell = row.createCell(j);
                cell.setCellValue(after.getString("depositorName"));//异动后
                cell.setCellStyle(style);
            }

            if (!match.getBoolean("legalName")){
                row = sheet.createRow(i++);
                int j = 0;
                cell = row.createCell(j++);
                cell.setCellValue(checkDto.getDepositorName());//客户名称
                cell.setCellStyle(style);
                cell = row.createCell(j++);
                cell.setCellValue(checkDto.getOrganName());//所属银行名称
                cell.setCellStyle(style);
                cell = row.createCell(j++);
                cell.setCellValue(checkDto.getAbnormalTime());//系统异动时间
                cell.setCellStyle(style);
                cell = row.createCell(j++);
                cell.setCellValue("法定代表人");//异动内容
                cell.setCellStyle(style);

                cell = row.createCell(j++);
                cell.setCellValue(before.getString("legalName"));//异动前
                cell.setCellStyle(style);

                cell = row.createCell(j);
                cell.setCellValue(after.getString("legalName"));//异动后
                cell.setCellStyle(style);
            }
            if (!match.getBoolean("regNo")){
                row = sheet.createRow(i++);
                int j = 0;
                cell = row.createCell(j++);
                cell.setCellValue(checkDto.getDepositorName());//客户名称
                cell.setCellStyle(style);
                cell = row.createCell(j++);
                cell.setCellValue(checkDto.getOrganName());//所属银行名称
                cell.setCellStyle(style);
                cell = row.createCell(j++);
                cell.setCellValue(checkDto.getAbnormalTime());//系统异动时间
                cell.setCellStyle(style);
                cell = row.createCell(j++);
                cell.setCellValue("统一社会信用代码");//异动内容
                cell.setCellStyle(style);

                cell = row.createCell(j++);
                cell.setCellValue(before.getString("regNo"));//异动前
                cell.setCellStyle(style);

                cell = row.createCell(j);
                cell.setCellValue(after.getString("regNo"));//异动后
                cell.setCellStyle(style);
            }
            if (!match.getBoolean("enddate")){
                row = sheet.createRow(i++);
                int j = 0;
                cell = row.createCell(j++);
                cell.setCellValue(checkDto.getDepositorName());//客户名称
                cell.setCellStyle(style);
                cell = row.createCell(j++);
                cell.setCellValue(checkDto.getOrganName());//所属银行名称
                cell.setCellStyle(style);
                cell = row.createCell(j++);
                cell.setCellValue(checkDto.getAbnormalTime());//系统异动时间
                cell.setCellStyle(style);
                cell = row.createCell(j++);
                cell.setCellValue("营业期限到期日");//异动内容
                cell.setCellStyle(style);

                cell = row.createCell(j++);
                cell.setCellValue(before.getString("enddate"));//异动前
                cell.setCellStyle(style);

                cell = row.createCell(j);
                cell.setCellValue(after.getString("enddate"));//异动后
                cell.setCellStyle(style);
            }
            if (!match.getBoolean("registeredCapital")){
                row = sheet.createRow(i++);
                int j = 0;
                cell = row.createCell(j++);
                cell.setCellValue(checkDto.getDepositorName());//客户名称
                cell.setCellStyle(style);
                cell = row.createCell(j++);
                cell.setCellValue(checkDto.getOrganName());//所属银行名称
                cell.setCellStyle(style);
                cell = row.createCell(j++);
                cell.setCellValue(checkDto.getAbnormalTime());//系统异动时间
                cell.setCellStyle(style);
                cell = row.createCell(j++);
                cell.setCellValue("注册资本");//异动内容
                cell.setCellStyle(style);

                cell = row.createCell(j++);
                cell.setCellValue(before.getString("registeredCapital"));//异动前
                cell.setCellStyle(style);

                cell = row.createCell(j);
                cell.setCellValue(after.getString("registeredCapital"));//异动后
                cell.setCellStyle(style);
            }

            if (!match.getBoolean("opendate")){
                row = sheet.createRow(i++);
                int j = 0;
                cell = row.createCell(j++);
                cell.setCellValue(checkDto.getDepositorName());//客户名称
                cell.setCellStyle(style);
                cell = row.createCell(j++);
                cell.setCellValue(checkDto.getOrganName());//所属银行名称
                cell.setCellStyle(style);
                cell = row.createCell(j++);
                cell.setCellValue(checkDto.getAbnormalTime());//系统异动时间
                cell.setCellStyle(style);
                cell = row.createCell(j++);
                cell.setCellValue("成立日期");//异动内容
                cell.setCellStyle(style);

                cell = row.createCell(j++);
                cell.setCellValue(before.getString("opendate"));//异动前
                cell.setCellStyle(style);

                cell = row.createCell(j);
                cell.setCellValue(after.getString("opendate"));//异动后
                cell.setCellStyle(style);
            }

            if (!match.getBoolean("regAddress")){
                row = sheet.createRow(i++);
                int j = 0;
                cell = row.createCell(j++);
                cell.setCellValue(checkDto.getDepositorName());//客户名称
                cell.setCellStyle(style);
                cell = row.createCell(j++);
                cell.setCellValue(checkDto.getOrganName());//所属银行名称
                cell.setCellStyle(style);
                cell = row.createCell(j++);
                cell.setCellValue(checkDto.getAbnormalTime());//系统异动时间
                cell.setCellStyle(style);
                cell = row.createCell(j++);
                cell.setCellValue("企业地址");//异动内容
                cell.setCellStyle(style);

                cell = row.createCell(j++);
                cell.setCellValue(before.getString("regAddress"));//异动前
                cell.setCellStyle(style);

                cell = row.createCell(j);
                cell.setCellValue(after.getString("regAddress"));//异动后
                cell.setCellStyle(style);
            }
            //end 工商异常校验比对时，比对规则勾选的字段 见AbnormalCheckInitializer end

            log.info(before.toJSONString());
            log.info(after.toJSONString());
            log.info(match.toJSONString());
        }

        return wb;
    }

    @Override
    public JSONArray getChangedList(CompareResultDto compareResultDto) {
        JSONArray array = new JSONArray();
        if (compareResultDto == null) {
            return array;
        }

        Map<String, String> map = new HashMap<>();
        map.put("depositorName", "企业名称");
        map.put("legalName", "法定代表人");
        map.put("regNo", "统一社会信用代码");
        map.put("enddate", "营业期限到期日");
        map.put("registeredCapital", "注册资本");
        map.put("opendate", "成立日期");
        map.put("regAddress", "企业地址");

        String details = compareResultDto.getDetails();
        if (StringUtils.isNotBlank(details)) {
            JSONObject jsonObject = JSONObject.parseObject(details);
            JSONObject data = jsonObject.getJSONObject("data");
            JSONObject match = jsonObject.getJSONObject("match");
            JSONObject before = data.getJSONObject("客户异动数据");
            JSONObject after = data.getJSONObject("工商异动数据");
            for (String key : map.keySet()) {
                if (!match.getBoolean(key)) {
                    JSONObject json = new JSONObject();
                    json.put("name", map.get(key));//异动内容
                    json.put("before", before.getString(key));//异动前
                    json.put("after", after.getString(key));//异动后
                    array.add(json);
                }
            }
        }
        return array;
    }

    @Override
    public JSONObject getIndexAbnormalCounts(String organFullId) {
        JSONObject jsonObject = new JSONObject();
        Long abnormalAllCount = null;
        Long illegalCount = null;
        Long changeMessCount = null;
        Long businessExpiresCount = null;
        Long abnormalStateCount = null;
        Long changedCount = null;
        if (permissionService.findByCode("abnormalStatistics_div")) {
            if (permissionService.findByCode("abnormalAll_div")) {//企业异动总数
                abnormalAllCount = this.statisticsAbnormalAllCount(organFullId);
            }
            if (permissionService.findByCode("illegal_div")) {//严重违法数
                illegalCount = this.statisticsIllegalCount(organFullId);
            }
            if (permissionService.findByCode("changeMess_div")) {//经营异常数
                changeMessCount = this.statisticsChangeMessCount(organFullId);
            }
            if (permissionService.findByCode("businessExpires_div")) {//营业到期数
                businessExpiresCount = this.statisticsBusinessExpiresCount(organFullId);
            }
            if (permissionService.findByCode("abnormalState_div")) {//工商异常状态数
                abnormalStateCount = this.statisticsAbnormalStateCount(organFullId);
            }
            if (permissionService.findByCode("changed_div")) {//工商登记信息异动数
                changedCount = this.statisticsChangedCount(organFullId);
            }
        }
        jsonObject.put("abnormalAllCount", abnormalAllCount);
        jsonObject.put("illegalCount", illegalCount);
        jsonObject.put("changeMessCount", changeMessCount);
        jsonObject.put("businessExpiresCount", businessExpiresCount);
        jsonObject.put("abnormalStateCount", abnormalStateCount);
        jsonObject.put("changedCount", changedCount);
        return jsonObject;
    }

    @Override
    @Transactional
    public void process(String processState, Long[] ids) {
        UserDto user = userService.findById(SecurityUtils.getCurrentUserId());
        OrganizationDto organ = organizationService.findByOrganFullId(SecurityUtils.getCurrentOrgFullId());
        String processTime = DateUtils.DateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
        compareResultSaicCheckDao.process(processState, organ.getName() + "-" + user.getCname(), processTime, ids);
    }

    @Override
    public CustomerAbnormalDto findOneById(Long id) {
        if (id==null){
            return null;
        }
        return ConverterService.convert(compareResultSaicCheckDao.findOne(id),CustomerAbnormalDto.class);
    }

    @Override
    @Transactional
    public void chageMessage(String flag,Long id) {
        if(id==null){
            return;
        }
        compareResultSaicCheckDao.chageMessage(flag,id);
    }

    /**
     * 统计企业异动总数
     */
    private Long statisticsAbnormalAllCount(String organFullId) {
        return this.statisticsAbnormalCount(organFullId, true, null, null, null, null, null);
    }

    /**
     * 统计企业严重违法数
     */
    private Long statisticsIllegalCount(String organFullId) {
        return this.statisticsAbnormalCount(organFullId, null, true, null, null, null, null);
    }

    /**
     * 统计企业经营异常数
     */
    private Long statisticsChangeMessCount(String organFullId) {
        return this.statisticsAbnormalCount(organFullId, null, null, true, null, null, null);
    }

    /**
     * 统计企业营业到期数
     */
    private Long statisticsBusinessExpiresCount(String organFullId) {
        return this.statisticsAbnormalCount(organFullId, null, null, null, true, null, null);
    }

    /**
     * 统计企业工商异常状态数
     */
    private Long statisticsAbnormalStateCount(String organFullId) {
        return this.statisticsAbnormalCount(organFullId, null, null, null, null, true, null);
    }

    /**
     * 统计企业工商登记信息异动数
     */
    private Long statisticsChangedCount(String organFullId) {
        return this.statisticsAbnormalCount(organFullId, null, null, null, null, null, true);
    }

    /**
     *
     * @param organFullId
     * @param abnormal 是否异动
     * @param abnormal 是否异动
     */
    private Long statisticsAbnormalCount(String organFullId, Boolean abnormal, Boolean illegal, Boolean changeMess, Boolean businessExpires, Boolean abnormalState, Boolean changed) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        //OrderSum指定了查询结果返回至自定义对象
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<CrscNewAllView> root = query.from(CrscNewAllView.class);
        Path<String> organFullIdPath = root.get("organFullId");
        Path<Boolean> abnormalPath = root.get("abnormal");
        Path<Boolean> illegalPath = root.get("illegal");
        Path<Boolean> changeMessPath = root.get("changeMess");
        Path<Boolean> businessExpiresPath = root.get("businessExpires");
        Path<Boolean> abnormalStatePath = root.get("abnormalState");
        Path<Boolean> changedPath = root.get("changed");

        List<Predicate> predicateList = new ArrayList<Predicate>();
        predicateList.add(cb.like(organFullIdPath, organFullId + "%"));
        if (abnormal != null) {
            predicateList.add(cb.equal(abnormalPath, abnormal));
        }
        if (illegal != null) {
            predicateList.add(cb.equal(illegalPath, illegal));
        }
        if (changeMess != null) {
            predicateList.add(cb.equal(changeMessPath, changeMess));
        }
        if (businessExpires != null) {
            predicateList.add(cb.equal(businessExpiresPath, businessExpires));
        }
        if (abnormalState != null) {
            predicateList.add(cb.equal(abnormalStatePath, abnormalState));
        }
        if (changed != null) {
            predicateList.add(cb.equal(changedPath, changed));
        }
        Predicate[] predicates = new Predicate[predicateList.size()];
        predicates = predicateList.toArray(predicates);
        query.where(predicates);

        query.multiselect(cb.count(root).as(Long.class));
        TypedQuery<Long> typedQuery = em.createQuery(query);
        Long resultCount = typedQuery.getSingleResult();
        return resultCount;
    }
}
