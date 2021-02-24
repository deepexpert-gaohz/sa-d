package com.ideatech.ams.controller.risk;

import com.alibaba.fastjson.JSON;
import com.ideatech.ams.account.dao.AccountsAllDao;
import com.ideatech.ams.account.service.AccountsAllService;
import com.ideatech.ams.risk.account.service.AccountRiskService;
import com.ideatech.ams.risk.highRisk.dao.HighRiskDataDao;
import com.ideatech.ams.risk.highRisk.dto.HighRiskApiDto;
import com.ideatech.ams.risk.highRisk.dto.HighRiskDataDto;
import com.ideatech.ams.risk.highRisk.dto.HighRiskListDto;
import com.ideatech.ams.risk.highRisk.entity.HighRisk;
import com.ideatech.ams.risk.highRisk.entity.HighRiskApi;
import com.ideatech.ams.risk.highRisk.entity.HighRiskRule;
import com.ideatech.ams.risk.highRisk.poi.FilePoi;
import com.ideatech.ams.risk.highRisk.service.ExternalDataService;
import com.ideatech.ams.risk.highRisk.service.HighRiskService;
import com.ideatech.ams.risk.model.dto.ModelDto;
import com.ideatech.ams.risk.model.service.ModelService;
import com.ideatech.ams.risk.riskdata.service.RiskApiService;
import com.ideatech.ams.risk.riskdata.service.RiskRegisterInfoService;
import com.ideatech.ams.risk.service.JnnsOpenAcctRiskFileService;
import com.ideatech.common.constant.ResultCode;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.excel.util.ExportExcel;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.util.HttpRequest;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.jws.WebMethod;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 高风险数据
 */
@RestController
@RequestMapping("highRisk")
public class HighRiskController {
    private static final Logger log = LoggerFactory.getLogger(HighRiskController.class);
    @Autowired
    HighRiskService highRiskService;
    @Autowired
    ModelService modelService;
    @Autowired
    RiskApiService riskApiService;
    @Autowired
    AccountRiskService accountRiskService;
    @Autowired
    HttpRequest httpRequest;
    @Autowired
    ExternalDataService externalDataService;
    @Autowired
    JnnsOpenAcctRiskFileService szsmFtpOdsFileService;
    @Autowired
    HighRiskDataDao highRiskDataDao;
    @Autowired
    AccountsAllService accountsAllService;

    @Autowired
    RiskRegisterInfoService riskRegisterInfoService;
    @Autowired
    AccountsAllDao accountsAllDao;

    /**
     * 高风险规则配置页面初始化
     *
     * @return
     */
    @GetMapping("highRiskInit")
    public String highRiskInit(HttpServletResponse response) {

        Map<String, Object> jsonMap = new HashMap();
        //查询黑名单数据是否存在数据
        List<HighRisk> highRisk = highRiskService.findAllHighRisk();
        //查询规则配置
        List<HighRiskRule> highRiskRuleList = highRiskService.findAllHighRiskRule();
        if (highRiskRuleList.size() == 0) {
            HighRiskRule highRiskRule = new HighRiskRule();
            highRiskService.saveCon(highRiskRule, "");
            highRiskRuleList = highRiskService.findAllHighRiskRule();
        }
        jsonMap.put("highRisk", highRisk);
        jsonMap.put("highRiskRule", highRiskRuleList);
        return JSON.toJSONString(jsonMap);

    }

    /**
     * 初始化高风险展示列表
     *
     * @return
     */
    @GetMapping("highRiskDataInit")
    public ResultDto highRiskDataInit(HighRiskDataDto highRiskDataDto) {
        //List<HighRiskData> list =highRiskService.findAllHighRiskData();
        return ResultDtoFactory.toAckData(highRiskService.queryHighRiskData(highRiskDataDto));

    }

    /**
     * 初始化高风险详细
     *
     * @return
     */
    @GetMapping("highRiskListInit")
    public ResultDto highRiskListInit(HighRiskListDto highRiskListDto) {
        return ResultDtoFactory.toAckData(highRiskService.queryHighRiskList(highRiskListDto));
    }

    /**
     * @return
     * @Description 查看外部数据接口
     * @author yangwz
     * @date 2019-10-29 11:14
     * @params * @param null
     */
    @GetMapping("getRiskApi")
    public ResultDto getRiskApi(HighRiskApiDto highRiskApiDto) {

        return ResultDtoFactory.toAckData(highRiskService.queryRiskApi(highRiskApiDto));

    }


    @GetMapping("findhighRiskskList")
    public ResultDto findhighRiskskList(HighRiskListDto highRiskListDto) {
        return ResultDtoFactory.toAckData(highRiskService.queryHighRiskList(highRiskListDto));
    }

    /**
     * 高风险数据模板下载
     *
     * @param response
     * @throws IOException
     */
    @GetMapping("download")
    public void downloadCoreExcel(HttpServletResponse response) throws IOException {
//        IExcelExport excelExport = coreCollectionService.generateAnnualCompanyReport();
        IExcelExport excelExport = highRiskService.generateAnnualCompanyReport();
        response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode("高风险导入模板文件", "UTF-8") + ".xls");
        response.setContentType("application/octet-stream");
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        ExportExcel.export(response.getOutputStream(), "yyyy-MM-dd", excelExport);
        toClient.flush();
        response.getOutputStream().close();
    }

    /**
     * 行内数据文件上传
     *
     * @param file
     * @param response
     * @throws IOException
     */
    @PostMapping(value = "upload")
    @WebMethod(exclude = true)
    public void upload(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException {
        ResultDto dto = new ResultDto();
        CommonsMultipartFile cf = (CommonsMultipartFile) file;
        DiskFileItem fi = (DiskFileItem) cf.getFileItem();
        File f = fi.getStoreLocation();
        InputStream in = null;
        try {
            in = new FileInputStream(f);
            List<HighRisk> list = new FilePoi().getFileList(in);
            highRiskService.updateHighRisk(list);
            dto.setCode(ResultCode.ACK);
            dto.setMessage("导入成功");
        } catch (Exception e) {
            log.error("导入数据失败", e);
            dto.setCode(ResultCode.NACK);
            dto.setMessage("导入数据失败");
        } finally {
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(dto));
        }

    }

    /**
     * 查找所有的模型规则
     */
    @GetMapping("findmodel")
    public ResultDto findmodel() {

        List<ModelDto> list = modelService.findByStatus();
        return ResultDtoFactory.toAckData(list);
    }

    /**
     * 查找所有的模型规则
     */
    @GetMapping("findHighRiskDate")
    public ResultDto findHighRiskDate() {
        List<ModelDto> list = modelService.findAllModel();
        return ResultDtoFactory.toAckData(list);
    }

    /**
     * 查询所有外部数据接口
     *
     * @return
     * @author yangwz
     * @date 2019-10-14 17:04
     */
    @GetMapping("findDataApi")
    public ResultDto findDataApi() {
        List<HighRiskApi> list = highRiskService.findDataApi();
        return ResultDtoFactory.toAckData(list);
    }

    /**
     * 更新高风险配置
     */
    @GetMapping("saveCon")
    public void saveCon(@RequestParam("value") String value) {
        HighRiskRule highRiskRule = new HighRiskRule();
        String str = "modelRule";
        highRiskRule.setRuleModel(value);
        highRiskService.saveCon(highRiskRule, str);
    }

    /**
     * 更新已选择外部数据接口
     */
    @GetMapping("saveApi")
    public void saveApi(@RequestParam("externalData") String externalData) {
        HighRiskRule highRiskRule = new HighRiskRule();
        String str = "extData";
        highRiskRule.setExternalData(externalData);
        highRiskService.saveCon(highRiskRule, str);
    }

    /**
     * 开始跑批
     */
    @GetMapping(value = "runData")
    public ResultDto runData() {
        ResultDto resultDto = new ResultDto();
        HighRiskRule highRiskRule = new HighRiskRule();
        String str = "runState";

        //查询规则配置,判断是否正在跑批
        List<HighRiskRule> ruleList = highRiskService.findAllHighRiskRule();
        if (!ruleList.get(0).getRunState().equals("0")) {
            resultDto.setCode(ResultCode.NACK);
            resultDto.setMessage("正在跑批中......");
            return resultDto;
        }
        //查看是否有行内数据
        List<HighRisk> highRiskList = highRiskService.findAllHighRisk();
        //捕捉异常,程序错误时状态为0
        try {
            if (highRiskList.size() == 0 && !StringUtils.isNotBlank(ruleList.get(0).getRuleModel())) {
                resultDto.setCode(ResultCode.NACK);
                resultDto.setMessage("未选择规则模型或行内数据");
                return resultDto;
            } else {
                highRiskRule.setRunState("1");
                highRiskService.saveCon(highRiskRule, str);
                //跑批
                highRiskService.syncHighRiskData(ruleList.get(0).getRuleModel());
                resultDto.setCode(ResultCode.ACK);
                resultDto.setMessage("跑批成功！");
            }
        } catch (Exception e) {
            resultDto.setCode(ResultCode.NACK);
            resultDto.setMessage("跑批失败！");
            log.error("高风险跑批失败", e);

        } finally {
            //跑批狀態為0
            highRiskRule.setRunState("0");
            highRiskService.saveCon(highRiskRule, str);
        }
        return resultDto;
    }

    /**
     * 规则重置
     */
    @GetMapping(value = "resetData")
    public ResultDto resetData() {
        ResultDto resultDto = new ResultDto();
        highRiskService.resetData();
        resultDto.setCode(ResultCode.ACK);
        resultDto.setMessage("重置成功");
        return resultDto;
    }

    /**
     * 处理风险数据 分为止付、不处理和暂停非柜面
     *
     * @return accountNo：账号，handleType：处理方式，
     * @author yangwz
     */
    @GetMapping("/disHighRisk/{id}/{handleType}/{accountNo}/{choo}/{customerNo}")
    public ResultDto disHighRisk(@PathVariable("id") String id, @PathVariable("handleType") String handleType,
                                 @PathVariable("accountNo") String accountNo, @PathVariable("choo") String choo, @PathVariable("customerNo") String customerNo) {
        ResultDto resultDto = new ResultDto();
        try {
            highRiskService.disdisHighRisk(id, accountNo, handleType, choo, customerNo);
            resultDto.setCode(ResultCode.ACK);
            resultDto.setMessage("风险数据处理成功!");
        } catch (Exception e) {
            resultDto.setCode(ResultCode.NACK);
            resultDto.setMessage("风险数据处理失败!");
        }


        return resultDto;
    }

    /**
     * 所有的账号类型
     *
     * @author yangwz
     */
    @GetMapping("findAcctType")
    public ResultDto findAcctType() {

        return ResultDtoFactory.toAckData(highRiskService.findAcctType());
    }

    /**
     * @return
     * @Description 导出高风险详情数据
     * @author yangwz
     * @date 2019-10-24 16:03
     * @params * @param null
     */
    @GetMapping("exportRiskList")
    public ResultDto exportRiskList(HighRiskListDto highRiskListDto, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HighRiskListDto highDto = highRiskService.queryHighRiskList(highRiskListDto);
        ResultDto resultDto = new ResultDto();
        String StrDt = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String rootPath = request.getSession().getServletContext().getRealPath("/");
        rootPath += "RiskList";
        File dirFile = new File(rootPath);
        boolean bFile = dirFile.exists();
        //如果文件夹不存在则生成文件夹
        if (!bFile) {
            dirFile.mkdirs();
        }
        try {
            highRiskService.exportRiskList(highDto, rootPath, StrDt);
            resultDto.setCode("ACK");
            resultDto.setMessage("导出成功!");
        } catch (Exception e) {
            resultDto.setCode("NACK");
            resultDto.setMessage("导出失败!");
            log.error("高风险详情导出失败", e);
        }
        return resultDto;
    }

    /**
     * @return
     * @Description 新增一条接口
     * @author yangwz
     * @date 2019-10-29 13:46
     * @params * @param null
     */
    @PostMapping("addRiskApi")
    public ResultDto addRiskApi(HighRiskApi highRiskApi) {

        ResultDto resultDto = new ResultDto();
        try {
            boolean choose = highRiskService.addRiskApi(highRiskApi);
            if (choose) {
                resultDto.setCode("ACK");
                resultDto.setMessage("保存成功!");
            } else {
                resultDto.setCode("NACK");
                resultDto.setMessage("已存在相同接口编号!");
            }
        } catch (Exception e) {
            resultDto.setCode("NACK");
            resultDto.setMessage("保存失败!");
            log.error("保存外部数据接口错误", e);
        }

        return resultDto;
    }

    /**
     * @return
     * @Description 编辑修改接口信息
     * @author yangwz
     * @date 2019-10-29 15:01
     */
    @PostMapping("saveRiskApi/{id}")
    public ResultDto saveRiskApi(@PathVariable("id") Long id, HighRiskApi highRiskApi) {

        ResultDto resultDto = new ResultDto();
        try {
            highRiskApi.setId(id);
            resultDto = highRiskService.saveRiskApi(highRiskApi);
        } catch (Exception e) {
            resultDto.setCode("NACK");
            resultDto.setMessage("修改失败!");
            log.error("保存外部数据接口错误", e);
        }

        return resultDto;
    }

    /**
     * @return
     * @Description 根据id获得外部接口数据
     * @author yangwz
     * @date 2019-10-29 15:13
     */
    @GetMapping("/inEdit/{id}")
    public ResultDto inEdit(@PathVariable("id") Long id) {
        return ResultDtoFactory.toAckData(highRiskService.findApiById(id));
    }

    /**
     * @return
     * @Description 根据id删除
     * @author yangwz
     * @date 2019-10-29 15:20
     */
    @DeleteMapping("/delRiskApi/{id}")
    public ResultDto delRiskApi(@PathVariable("id") Long id) {
        ResultDto resultDto = new ResultDto();
        try {
            highRiskService.delRiskApiById(id);
            resultDto.setCode("ACK");
            resultDto.setMessage("删除成功!");
        } catch (Exception e) {
            resultDto.setCode("NACK");
            resultDto.setMessage("删除失败!");
        }
        return resultDto;
    }


}
