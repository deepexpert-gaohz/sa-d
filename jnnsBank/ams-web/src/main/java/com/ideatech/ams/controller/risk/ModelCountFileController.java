package com.ideatech.ams.controller.risk;


import com.ideatech.ams.risk.model.dto.ModelSearchExtendDto;
import com.ideatech.ams.risk.model.dto.ModelsExtendDto;
import com.ideatech.ams.risk.model.dto.RiskDataDto;
import com.ideatech.ams.risk.model.service.*;
import com.ideatech.ams.risk.riskdata.service.RiskDataService;
import com.ideatech.ams.risk.util.RiskUtil;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.util.FileUtils;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * author:liuz
 * 风险数据导出
 */
@RestController
@RequestMapping("/ModelCountFileController")
@Slf4j
public class ModelCountFileController {

    @Autowired
    ModelCountFileService modelCountFileService;
    @Autowired
    RiskDataService riskDataService;
    @Autowired
    RiskDataServiceToExp riskDataServiceToExp;
    @Autowired
    ModelFieldService modelFieldService;
    @Autowired
    OrganizationService organizationService;
    @Autowired
    ModelService modelService;


    @GetMapping("/expRiskDataDiyView")
    public ResultDto expRiskDataDiyView(ModelSearchExtendDto modelSearchExtendDto) {
        //拆解日期
        if (modelSearchExtendDto.getDataDate() != null && modelSearchExtendDto.getDataDate() != "") {
            String startEndTime[] = modelSearchExtendDto.getDataDate().split("~");
            modelSearchExtendDto.setDataDate(startEndTime[0].trim().replace("-", ""));
            modelSearchExtendDto.setEndDate(startEndTime[1].trim().replace("-", ""));
        } else {
            modelSearchExtendDto.setDataDate("199011");
            modelSearchExtendDto.setEndDate("21000101");
        }
        modelCountFileService.queryModelCountFile2(modelSearchExtendDto);

        return ResultDtoFactory.toAckData(modelCountFileService.queryModelCountFile2(modelSearchExtendDto));
    }

    /**
     * author:liuz
     * date:2019-4-23
     * 按模型类型导出所有风险数据
     */
    @GetMapping("/expRiskDataDiy")
    public void expRiskDataDiy(RiskDataDto riskDataDto, String ids, String dateStr, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //转换日期
        String code = RiskUtil.getOrganizationCode();
        String fullId = RiskUtil.getOrganizationFullId();
        if (dateStr != null && !dateStr.equalsIgnoreCase("")) {
            String[] split = dateStr.split("~");
            riskDataDto.setMinDate(split[0].replace("-", "").trim());
            riskDataDto.setMaxDate(split[1].replace("-", "").trim());
        }
        //将要导出的模型id转为数组
        ids = ids.replace("\"", "").replace("[", "").replace("]", "");
        String[] modelArray = ids.split(",");
        ExecutorService execPool = Executors.newFixedThreadPool(5);//线程数
        String StrDt = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        List<String> modelIdList = Arrays.asList(modelArray);//把id的数组转换成list
        //获取所有模型的模型类型
        List<HashMap<String, String>> modelList = modelCountFileService.getAllModelTypeList();
        String rootPath = request.getSession().getServletContext().getRealPath("/");
        String exportType = "byModel";
        String tempFilePath = rootPath + "exportExcelFile" + "/exportExcelFile" + "/" + SecurityUtils.getCurrentUsername();//路径为按机构导出
        String tempSourceFilePath = tempFilePath + "/" + SecurityUtils.getCurrentUsername() + "_" + StrDt;
        //删除历史下载文件
        FileUtils.delFile(tempSourceFilePath);//删除历史下载文件
        for (int j = 0; j < modelArray.length; j++) {
            String modelTypeName = "";//模型名称
            String riskTable = modelArray[j];
            //获取模型对应的模型类型
            for (int i = 0; i < modelList.size(); i++) {
                HashMap<String, String> map = modelList.get(i);
                for (String key : map.keySet()) {
                    if (key.equals(riskTable)) {
                        modelTypeName = map.get(key);
                        break;
                    }
                }
            }
            if ("".equalsIgnoreCase(modelTypeName)) {
                modelTypeName = "Other";
            }
            ModelsExtendDto modelsExtendDto = new ModelsExtendDto();
            modelsExtendDto.setMinDate(riskDataDto.getMinDate());
            modelsExtendDto.setMaxDate(riskDataDto.getMaxDate());
            modelsExtendDto.setModelId(riskTable);

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
                log.info("-------------线程已经结束---------");
                isClose = false;
            } else {
                log.info("--------线程还未结束-----------------");
            }
        }
        boolean flag = FileUtils.createZip(tempSourceFilePath, tempFilePath + "/" + SecurityUtils.getCurrentUsername() + "_" + StrDt + ".zip");
        if (flag) {
            //导出压缩文件
            FileUtils.downLoadFile(response, tempSourceFilePath + ".zip");
        }
    }

    /**
     * 按机构导出风险数据
     *
     * @param riskDataDto
     * @param ids
     * @param request
     * @param response
     * @author liuz 2019-4-25
     */
    @GetMapping("/expRiskDataByOrg")
    public void expRiskDataByOrg(ModelsExtendDto modelsExtendDto, RiskDataDto riskDataDto, String ids, String dateStr, HttpServletRequest request, HttpServletResponse response) {
        String code = RiskUtil.getOrganizationCode();
        String fullId = RiskUtil.getOrganizationFullId();
        if (dateStr != null && !dateStr.equalsIgnoreCase("")) {
            String[] split = this.dateToArray(dateStr);
            riskDataDto.setMinDate(split[0].replace("-", "").trim());
            riskDataDto.setMaxDate(split[1].replace("-", "").trim());
        }
        try {
            //获取当前操作用户的机构id
            modelsExtendDto.setOrgId(SecurityUtils.getCurrentUser().getOrgId().toString());
            //将要导出的模型id转为数组
            ids = ids.replace("\"", "").replace("[", "").replace("]", "");
            String[] modelArray = ids.split(",");
            ExecutorService execPool = Executors.newFixedThreadPool(5);//线程数
            String StrDt = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            List<String> modelIdList = Arrays.asList(modelArray);//把id的数组转换成list
            String rootPath = request.getSession().getServletContext().getRealPath("/");
            String exportType = "byOffice";
            String tempFilePath = rootPath + "exportExcelFile" + "/exportExcelFile" + "/" + SecurityUtils.getCurrentUsername();//路径为按机构导出
            String tempSourceFilePath = tempFilePath + "/" + SecurityUtils.getCurrentUsername() + "_" + StrDt;
            File dirFile = new File(tempSourceFilePath);
            //如果文件夹不存在则生成文件夹
            boolean bFile = dirFile.exists();
            if (!bFile) {
                dirFile.mkdirs();
            }
            List<OrganizationDto> orgList = new ArrayList<>();
            if (StringUtils.isNotEmpty(riskDataDto.getOrgId())) {
                OrganizationDto organizationDto = new OrganizationDto();
                organizationDto.setParentId(Long.parseLong(riskDataDto.getOrgId()));
                organizationDto.setId(Long.parseLong(riskDataDto.getOrgId()));
                orgList = modelCountFileService.findListByRisk(organizationDto);
            } else {
                OrganizationDto organizationDto = new OrganizationDto();
                organizationDto.setParentId(SecurityUtils.getCurrentUser().getOrgId());
                organizationDto.setId(SecurityUtils.getCurrentUser().getOrgId());
                orgList = modelCountFileService.findListByRisk(organizationDto);
            }
            if (orgList.size() == 0) {
                log.info("导出数据为空，请重新设置查询条件！");
                return;
            }
            for (String modelsId : modelIdList) {
                //根据查询该模型下的所有机构编号
                Map<String, String> smp = new HashMap<String, String>();
                smp.put("modelsId", modelsId);
                if (modelsExtendDto.getMaxDate() == null || modelsExtendDto.getMinDate() == null) {
                    modelsExtendDto.setMaxDate("21000101");
                    modelsExtendDto.setMinDate("19000101");
                }
                smp.put("minData", modelsExtendDto.getMinDate().replace("-", ""));
                smp.put("maxData", modelsExtendDto.getMaxDate().replace("-", ""));

                if (StringUtils.isNotEmpty(modelsExtendDto.getOrgId())) {
                    String pa = "";
                    Thread thread = new Thread();
                    for (OrganizationDto office : orgList) {
                        pa = office.getName();

                        pa = tempSourceFilePath + "/" + pa;
                        File dir = new File(pa);
                        boolean cFile = dir.exists();
                        //如果文件夹不存在则生成文件夹
                        if (!cFile) {
                            dir.mkdirs();
                        }
                        ModelsExtendDto modelsExtend2 = new ModelsExtendDto();
                        modelsExtend2.setMinDate(riskDataDto.getMinDate());
                        modelsExtend2.setMaxDate(riskDataDto.getMaxDate());
                        modelsExtend2.setOgId(office.getCode());
                        modelsExtend2.setModelId(modelsId);
                        thread = new Thread(new ExecutExcelDataServiceImpl(riskDataServiceToExp, modelFieldService, modelCountFileService, modelsExtend2, pa, office.getName(), exportType, code, fullId, modelService));
                        execPool.execute(thread);
                    }
                } else {
                    for (OrganizationDto org : orgList) {
                        //String orgId = org.getId().toString();
                        String pa = tempSourceFilePath + "/RISK_" + organizationService.findById(org.getId()).getName();
                        File dir = new File(pa);
                        boolean cFile = dir.exists();
                        //如果文件夹不存在则生成文件夹
                        if (!cFile) {
                            dir.mkdirs();
                        }
                        ModelsExtendDto modelsExtend2 = new ModelsExtendDto();
                        modelsExtend2.setMinDate(riskDataDto.getMinDate());
                        modelsExtend2.setMaxDate(riskDataDto.getMaxDate());
                        modelsExtend2.setOgId(org.getCode());
                        modelsExtend2.setModelId(modelsId);
                        Thread thread = new Thread(new ExecutExcelDataServiceImpl(riskDataServiceToExp, modelFieldService, modelCountFileService, modelsExtend2, pa, "/RISK_" + organizationService.findById(org.getId()).getName(), exportType, code, fullId, modelService));
                        execPool.execute(thread);
                    }
                }
            }
            execPool.shutdown();
            boolean isClose = true;
            while (isClose) {
                Thread.sleep(1000);
                if (execPool.isTerminated()) {
                    log.info("-------------线程已经结束---------");
                    isClose = false;
                } else {
                    log.info("--------线程还未结束-----------------");
                }
            }
            boolean flag = FileUtils.createZip(tempSourceFilePath, tempFilePath + "/" + SecurityUtils.getCurrentUsername() + "_" + StrDt + ".zip");
            if (flag) {
                //导出压缩文件
                FileUtils.downLoadFile(response, tempSourceFilePath + ".zip");
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }


    //日期转换
    public String[] dateToArray(String format) {
        if (format != null && !format.equalsIgnoreCase(""))
            format.replace("-", "").trim();
        String[] split = format.split("~");
        return split;
    }


}
