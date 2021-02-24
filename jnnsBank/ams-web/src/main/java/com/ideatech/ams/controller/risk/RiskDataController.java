package com.ideatech.ams.controller.risk;

import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.risk.model.dto.ModelFieldDto;
import com.ideatech.ams.risk.model.dto.ModelSearchExtendDto;
import com.ideatech.ams.risk.model.dto.ModelsExtendDto;
import com.ideatech.ams.risk.model.dto.RiskDataDto;
import com.ideatech.ams.risk.model.service.*;
import com.ideatech.ams.risk.riskdata.dto.RiskDetailsReturnDto;
import com.ideatech.ams.risk.riskdata.dto.RiskDetailsSearchDto;
import com.ideatech.ams.risk.riskdata.service.ModelCountService;
import com.ideatech.ams.risk.riskdata.service.RiskApiService;
import com.ideatech.ams.risk.riskdata.service.RiskDataService;
import com.ideatech.ams.risk.service.JnnsOpenAcctRiskFileService;
import com.ideatech.ams.risk.service.JnnsRiskCompareAcctFileService;
import com.ideatech.ams.risk.util.RiskUtil;
import com.ideatech.ams.utils.DateUtils;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.util.DateUtil;
import com.ideatech.common.util.FileUtils;
import com.ideatech.common.util.SecurityUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



/**
 * 风险数据查询
 */
@RestController
@RequestMapping("/riskData")
public class RiskDataController {
    private static final Logger log = LoggerFactory.getLogger(RiskDataController.class);
    @Autowired
    private RiskDataService riskDataService;
    @Autowired
    private RiskDataServiceToExp riskDataServiceToExp;
    @Autowired
    private ModelService modelService;
    @Autowired
    private ModelCountService modelCountService;
    @Autowired
    private ModelFieldService modelFieldService;

    @Autowired
    private RiskApiService riskApiService;

    @Autowired
    private ModelCountFileService modelCountFileService;

    @Autowired
    private JnnsOpenAcctRiskFileService szsmFtpOdsFileService;

    @Autowired
    private JnnsRiskCompareAcctFileService yinQiDuiZhangFileService;



    @GetMapping("/testRunOpenRisk")
    public void testRunOpenRisk() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String currentDate = formatter.format(new Date());
        log.info("风险 数据生成任务开始。。。");
        riskApiService.syncRiskData(currentDate);
        log.info("风险 数据生成任务结束。。。");
    }

    @GetMapping("/testRunOds")
    public void testRunOds() {
        log.info("获取江南农商数仓的T+1文件的任务开启...");
        Date startDate = new Date();
        long startDateLong = startDate.getTime();
        szsmFtpOdsFileService.pullOdsFile(DateUtils.DateToStr(DateUtil.subDays(new Date(), 1), "yyyyMMdd"));
        Date endDate = new Date();
        long endDateLong = endDate.getTime();
        log.info("获取江南农商数仓的T+1文件的任务结束...共耗时" + (endDateLong - startDateLong) / 1000 + "秒");
    }

    @GetMapping("/testDuiZhang")
    public void testDuiZhang(){
        log.info("江南农商对账数据获取开始11!");
        Date startGenDate = new Date();
        long startGenDateLong = startGenDate.getTime();
        yinQiDuiZhangFileService.getFile();
        Date endGenDate = new Date();
        long endGenDateLong = endGenDate.getTime();
        log.info("江南农商对账数据获取结束...共耗时" + (endGenDateLong - startGenDateLong) / 1000 + "秒");
    }








    /**
     * 查询交易监测风险数据
     *
     * @param modelSearchExtendDto
     * @return
     */
    @GetMapping("queryRiskData")
    public ResultDto queryRiskData(ModelSearchExtendDto modelSearchExtendDto) {
        SecurityUtils.UserInfo user = SecurityUtils.getCurrentUser();
        return ResultDtoFactory.toAckData(riskDataService.queryTradeRiskData(modelSearchExtendDto));
    }

    /**
     * 查询对账监测风险数据
     *
     * @author yangcq
     * @date 2019-06-17
     * @address wulmq
     */
    @GetMapping("queryRiskDzData")
    public ResultDto queryDzData(ModelSearchExtendDto modelSearchExtendDto) {
        return ResultDtoFactory.toAckData(riskDataService.queryRiskDzData(modelSearchExtendDto));
    }

    /**
     * 查询开户监测风险数据
     *
     * @author yangcq
     * @date 2019-06-17
     * @address wulmqtestRunOpenRisk
     */
    @GetMapping("queryRiskStaticData")
    public ResultDto queryRiskStaticData(ModelSearchExtendDto modelSearchExtendDto) {
        return ResultDtoFactory.toAckData(riskDataService.queryRiskStaticData(modelSearchExtendDto));
    }

//    /**
//     * 查询对账监测风险数据
//     *
//     * @param
//     * @return
//     */
//    @GetMapping("/queryCheckAcctData")
//    public ResultDto queryCheckAcctData(ModelSearchExtendDto modelSearchExtendDto) {
//        return ResultDtoFactory.toAckData(riskDataService.queryCheckAcctData(modelSearchExtendDto));
//
//    }

    /**
     * 通过模型id获取模型的名称
     *
     * @param
     * @return
     */
    @GetMapping("/queryRiskTableName")
    public ResultDto queryRiskTableName(String cjrq, String jgbh, String khh) {
        return ResultDtoFactory.toAckData(modelCountService.findModelCountByCjrq(cjrq, jgbh, khh));
    }


    /**
     * 风险数据详细信息查看
     *
     * @return
     */
    @GetMapping("/queryModelListDetails")
    public RiskDetailsReturnDto queryModelListDetails(RiskDetailsSearchDto riskDetailsSearchDto) {
        SecurityUtils.UserInfo user = SecurityUtils.getCurrentUser();
        JSONObject json = new JSONObject();
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        //根据登陆人的信息获取当前登陆人的机构行Code
        String currentCode = "";
        // List<OrganizationPo> byFullIdLike = organizationDao.findByFullIdLike("%" + user.getOrgFullId ()+"%");
        currentCode = RiskUtil.getOrganizationCode();

        //if (null!= riskDetailsSearchDto && StringUtils.isNotBlank(riskDetailsSearchDto.getModelId())){
        ModelFieldDto modelField = new ModelFieldDto();
        modelField.setModelId(riskDetailsSearchDto.getModelId());
        List list = modelFieldService.findRiskDataFieldZH(modelField);
        if (list.size() > 0) {
            //获取风险数据
            mapList = modelFieldService.getRiskMapList(list, riskDetailsSearchDto);
            json.put("rows", mapList);
            json.put("total", mapList.size());
        }
        //}
        //分页后
        RiskDetailsReturnDto riskDetailsReturnDto = new RiskDetailsReturnDto();
        riskDetailsReturnDto.setLimit(riskDetailsSearchDto.getLimit());
        riskDetailsReturnDto.setList(mapList);
        riskDetailsReturnDto.setTotalPages(riskDetailsSearchDto.getTotalPages());
        riskDetailsReturnDto.setOffset(riskDetailsSearchDto.getOffset());
        //riskDetailsReturnDto.setTotalRecord(new Long(mapList.size()));
        riskDetailsReturnDto.setTotalRecord(riskDetailsSearchDto.getTotalRecord());
        return riskDetailsReturnDto;
    }


    /**
     * 获取风险表字段
     *
     * @return
     * @author liuz 2019-4-29
     */
    @GetMapping("/getRiskDataColumns")
    public String getRiskDataColumns(String modelId) {
        String json = "";
        if (!modelId.equalsIgnoreCase("") && modelId != null) {
            //通过模型id获取模型
            ModelFieldDto modelField = new ModelFieldDto();
            modelField.setModelId(modelId);
            HashMap<Object, Object> maps = new HashMap<Object, Object>();
            //获取模型字段
            List<ModelFieldDto> list = modelFieldService.findAllByModelIdAndExportFlagOrderByOrderFlag(modelField);
            List<ModelFieldDto> li = new ArrayList<>();
            if (list.size() > 0) {
                for (ModelFieldDto m : list) {
                    ModelFieldDto mf = new ModelFieldDto();
                    //显示的情况才加入
                    if (m.getShowFlag() != null) {
                        if ("0".equals(String.valueOf(m.getShowFlag()))) {
                            if (m.getFieldsEn() != null) {
                                mf.setFieldsEn(m.getFieldsEn().toString());
                            }
                            if (m.getFieldsZh() != null) {
                                mf.setFieldsZh(m.getFieldsZh().toString());
                            }
                            li.add(mf);
                        }
                    }
                }
            }
            maps.put("data", li);
            json = JSONObject.toJSONString(maps);
        }
        return json;
    }

    /**
     * 导出开户风险数据
     *
     * @param
     * @return
     * @author:liuz 20190909
     */
    @GetMapping("/expRiskDataOpenAccount")
    public void expRiskDataOpenAccount(ModelSearchExtendDto modelSearchExtendDto, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RiskDataDto riskDataDto = new RiskDataDto();
        String code = RiskUtil.getOrganizationCode();
        String fullId = RiskUtil.getOrganizationFullId();
        ExecutorService execPool = Executors.newFixedThreadPool(5);//线程数
        String StrDt = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String rootPath = request.getSession().getServletContext().getRealPath("/");
        String exportType = "openAccount";
        String tempFilePath = rootPath + "exportExcelFile" + "/exportExcelFile" + "/" + SecurityUtils.getCurrentUsername();//路径为按机构导出
        String tempSourceFilePath = tempFilePath + "/" + SecurityUtils.getCurrentUsername() + "_" + StrDt;
        //删除历史下载文件
        FileUtils.delFile(tempSourceFilePath);//删除历史下载文件
        //获得所有的风险模型
        ModelSearchExtendDto allriskid = riskDataService.queryRiskid();
        boolean isexitsFlag = false;
        for (int j = 0; j < allriskid.getList().size(); j++) {
            ModelsExtendDto modelsExtendDto = allriskid.getList().get(j);
            String modelTypeName = modelsExtendDto.getModelId();
            if (StringUtils.isNotBlank(modelSearchExtendDto.getStartEndTime())) {
                String riskDate[] = modelSearchExtendDto.getStartEndTime().split("~");
                modelsExtendDto.setMaxDate(riskDate[1]);
                modelsExtendDto.setMinDate(riskDate[0]);
            }
            //判断是否导出该模型数据
            long count = riskDataService.findByCorporateBankAndRiskIdAndCjrqInAnd1002(code, modelsExtendDto);
            if (count == 0) {
                continue;
            }
            isexitsFlag = true;
            File dirFile = new File(tempSourceFilePath + "//" + modelTypeName);
            boolean bFile = dirFile.exists();
            //如果文件夹不存在则生成文件夹
            if (!bFile) {
                dirFile.mkdirs();
            }
            Thread thread = new Thread(new ExecutExcelDataServiceImpl(riskDataServiceToExp, modelFieldService, modelCountFileService, modelsExtendDto, tempSourceFilePath + "//" + modelTypeName, modelTypeName, exportType, code, fullId, modelService));
            execPool.execute(thread);
        }
        execPool.shutdown();
        boolean isClose = true;
        while (isClose) {
            Thread.sleep(1000);
            if (execPool.isTerminated()) {
                System.out.println("-------------线程已经结束---------");
                isClose = false;
            } else {
                System.out.println("--------线程还未结束-----------------");
            }
        }
        if (isexitsFlag) {
            boolean flag = FileUtils.createZip(tempSourceFilePath, tempFilePath + "/" + SecurityUtils.getCurrentUsername() + "_" + StrDt + ".zip");
            if (flag) {
                //导出压缩文件
                FileUtils.downLoadFile(response, tempSourceFilePath + ".zip");
            }
        }

    }

    /**
     * @return
     * @Description 导出交易风险数据
     * @author yangwz
     * @date 2019-10-22 15:28
     * @params * @param null
     */
    @GetMapping("/expRiskDataDeal")
    public void expRiskDataDeal(ModelSearchExtendDto modelSearchExtendDto, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String code = RiskUtil.getOrganizationCode();
        String fullId = RiskUtil.getOrganizationFullId();
        ExecutorService execPool = Executors.newFixedThreadPool(5);//线程数
        String StrDt = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String rootPath = request.getSession().getServletContext().getRealPath("/");
        String exportType = "openAccount";
        String tempFilePath = rootPath + "exportExcelFile" + "/exportExcelFile" + "/" + SecurityUtils.getCurrentUsername();//路径为按机构导出
        String tempSourceFilePath = tempFilePath + "/" + SecurityUtils.getCurrentUsername() + "_" + StrDt;
        //删除历史下载文件
        FileUtils.delFile(tempSourceFilePath);//删除历史下载文件
        //查询时间范围内所有的风险数据
        //获得所有的风险模型
        ModelSearchExtendDto allriskid = riskDataService.queryDealRiskid();
        //按照风险模型导出文件
        boolean isexitsFlag = false;
        for (int j = 0; j < allriskid.getList().size(); j++) {
            ModelsExtendDto modelsExtendDto = allriskid.getList().get(j);
            String modelTypeName = modelsExtendDto.getModelId();
            if (StringUtils.isNotBlank(modelSearchExtendDto.getStartEndTime())) {
                String riskDate[] = modelSearchExtendDto.getStartEndTime().split("~");
                modelsExtendDto.setMaxDate(riskDate[1]);
                modelsExtendDto.setMinDate(riskDate[0]);
            }
            //判断是否导出该模型数据
            long count = riskDataService.findByCorporateBankAndRiskIdAndCjrqIn1001(code, modelsExtendDto);
            if (count == 0) {
                continue;
            }
            isexitsFlag = true;
            File dirFile = new File(tempSourceFilePath + "//" + modelTypeName);
            boolean bFile = dirFile.exists();
            //如果文件夹不存在则生成文件夹
            if (!bFile) {
                dirFile.mkdirs();
            }
            Thread thread = new Thread(new ExecutExcelDataServiceImpl(riskDataServiceToExp, modelFieldService, modelCountFileService, modelsExtendDto, tempSourceFilePath + "//" + modelTypeName, modelTypeName, exportType, code, fullId, modelService));
            execPool.execute(thread);
        }
        execPool.shutdown();
        boolean isClose = true;
        while (isClose) {
            Thread.sleep(1000);
            if (execPool.isTerminated()) {
                System.out.println("-------------线程已经结束---------");
                isClose = false;
            } else {
                System.out.println("--------线程还未结束-----------------");
            }
        }
        if (isexitsFlag) {
            boolean flag = FileUtils.createZip(tempSourceFilePath, tempFilePath + "/" + SecurityUtils.getCurrentUsername() + "_" + StrDt + ".zip");
            if (flag) {
                //导出压缩文件
                FileUtils.downLoadFile(response, tempSourceFilePath + ".zip");
            }
        }

    }

    /**
     * @return
     * @Description 获得交易风险数据最新的日期
     * @author yangwz
     * @date 2019-11-20 14:14
     * @params *
     */
    @GetMapping("/findTradeNearDate")
    public ResultDto findTradeNearDate() {
        return ResultDtoFactory.toAckData(modelCountService.findTradeNearDate());
    }

    /**
     * @return
     * @Description 获得交易风险数据最新的日期
     * @author yangwz
     * @date 2019-11-20 14:14
     * @params *
     */
    @GetMapping("/findAccountNearDate")
    public ResultDto findAccountNearDate() {
        return ResultDtoFactory.toAckData(riskDataService.findAccountNearDate());
    }


    /**
     * 开户类模型数据补录
     *
     * @param date
     * @return 20191125
     */
    @GetMapping("/recendClear")
    public ResultDto recendClear(@RequestParam("date") String date) {
        String msg = "";
        try {
            if (date != null) {
                date = date.replace("-", "");
            } else {
                return ResultDtoFactory.toAck("日期未获取到！");
            }
            log.info("风险数据生成补录开始*******************************");
            riskApiService.syncRiskData(date);
            log.info("风险数据生成补录结束*******************************");
            log.info("统计账户信息补录开始***********************************");
//            riskApiService.syncAccountStaticData(date);
            log.info("统计账户信息补录结束***********************************");
        } catch (Exception e) {
            msg = "开户模型风险数据补录失败！";
            log.error("风险数据生成补录失败*******************************", e);
        }
        msg = "开户模型风险数据补录成功！";
        return ResultDtoFactory.toAck(msg);
    }
}
