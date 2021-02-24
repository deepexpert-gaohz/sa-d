package com.ideatech.ams.controller.customer;

import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.compare.dto.*;
import com.ideatech.ams.compare.service.*;
import com.ideatech.ams.customer.dto.CustomerPublicInfo;
import com.ideatech.ams.customer.service.CustomerPublicService;
import com.ideatech.ams.kyc.dto.SaicIdpInfo;
import com.ideatech.ams.kyc.dto.SaicInfoDto;
import com.ideatech.ams.kyc.service.ChangeMessService;
import com.ideatech.ams.kyc.service.IllegalService;
import com.ideatech.ams.kyc.service.SaicInfoService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.excel.util.ExportExcel;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.util.BeanCopierUtils;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author jzh
 * @date 2019/6/13.
 */

@Slf4j
@RestController
@RequestMapping(value = "/customerAbnormal")
public class CustomerAbnormalController {

    @Autowired
    private CustomerAbnormalService customerAbnormalService;

    @Autowired
    private CompareResultSaicCheckService compareResultSaicCheckService;

    @Autowired
    private SaicInfoService saicInfoService;

    @Autowired
    private CompareResultService compareResultService;

    @Autowired
    private ChangeMessService changeMessService;

    @Autowired
    private IllegalService illegalService;

    @Value("${ams.customer.noticeMessage:短信模版}")
    private String noticeMessage;

    @Autowired
    private CustomerSmsService customerSmsService;

    @Autowired
    private CustomerPublicService customerPublicService;

    @Autowired
    private CsrMessageService csrMessageService;

    @GetMapping(value = "/page")
    public TableResultResponse<CustomerAbnormalDto> list(CustomerAbnormalSearchDto customerAbnormalSearchDto, @PageableDefault(sort = {"lastUpdateDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        TableResultResponse<CustomerAbnormalDto> tableResultResponse = customerAbnormalService.page(customerAbnormalSearchDto,pageable);
        return tableResultResponse;
    }

    @GetMapping(value = "/pageHistory")
    public TableResultResponse<CustomerAbnormalDto> listHistory(CustomerAbnormalSearchDto customerAbnormalSearchDto, @PageableDefault(sort = {"lastUpdateDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        TableResultResponse<CustomerAbnormalDto> tableResultResponse = customerAbnormalService.pageHistory(customerAbnormalSearchDto, pageable);
        return tableResultResponse;
    }

    @GetMapping(value = "/export")
    public void export(CustomerAbnormalSearchDto customerAbnormalSearchDto, HttpServletResponse response) throws IOException {

        IExcelExport iExcelExport = customerAbnormalService.export(customerAbnormalSearchDto);
        response.setHeader("Content-disposition", "attachment; filename="+ URLEncoder.encode("客户异动信息", "UTF-8")+".xls");
        response.setContentType("application/octet-stream");
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        ExportExcel.export(response.getOutputStream(),"yyyy-MM-dd",iExcelExport);
        toClient.flush();
        response.getOutputStream().close();

    }

    /**
     * 输入当前的任务id，获取与最新任务的增量数据
     * @param taskId
     * @return
     */
    @GetMapping(value = "/list")
    public ResultDto list(Long taskId){
        List<CustomerAbnormalDto> customerAbnormalDtoList= customerAbnormalService.list(taskId);
        if (customerAbnormalDtoList==null || customerAbnormalDtoList.size()==0){
            return ResultDtoFactory.toNack("查询不到增量的客户异动信息");
        }
        return ResultDtoFactory.toAckData(customerAbnormalDtoList);
    }

    @GetMapping(value = "/findOne")
    public ResultDto findOneByCustomerName(String customerName){
        CustomerAbnormalDto customerAbnormalDto = customerAbnormalService.findOneByCustomerName(customerName);
        if (customerAbnormalDto==null){
            return ResultDtoFactory.toNack("查询不到客户异动信息");
        }
        return ResultDtoFactory.toAckData(customerAbnormalDto);
    }

    /**
     * 首页异动统计
     */
    @RequestMapping(value = "/findAbnormalCounts", method = RequestMethod.GET)
    public JSONObject findAbnormal() {
        String organFullId = SecurityUtils.getCurrentOrgFullId();
        return customerAbnormalService.getIndexAbnormalCounts(organFullId);
    }

    /**
     * 异动处理
     * @param ids          要处理的id集合
     * @param processState 处理后的处理状态
     */
    @PostMapping("/abnormalProcess")
    public ResultDto process(@RequestParam("ids[]") Long[] ids, String processState) {
        try {
            customerAbnormalService.process(processState, ids);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultDto<>("","",false);
        }
        return new ResultDto<>("","",true);
    }

//    @GetMapping("/test")
//    public void test() throws IOException {
//        SaicIdpInfo saicInfo = null;
//
//        //南宁市琅丽桶装水经营部 佐源集团有限公司
//        saicInfo = saicInfoService.getSaicInfoBaseLocalJustSaic("佐源集团有限公司");
//        HSSFWorkbook workbook = customerAbnormalService.getBaseInfoWorkbook(saicInfo,null);
//        workbook = customerAbnormalService.getIllegalsWorkbook(saicInfo,workbook);
//
//        saicInfo = saicInfoService.getSaicInfoBaseLocalJustSaic("南宁市琅丽桶装水经营部");
//        workbook = customerAbnormalService.getChangeMessWorkbook(saicInfo,workbook);
//
//        CompareResultSaicCheckDto checkDto = compareResultSaicCheckService.findById(1112291538690048L);
//        workbook = customerAbnormalService.getBusinessExpiresWorkbook(saicInfo,checkDto,workbook);
//
//        workbook = customerAbnormalService.getSaicStateWorkbook(checkDto,workbook);
//
//        CompareResultDto compareResultDto = compareResultService.findById(checkDto.getCompareResultId());
//        workbook = customerAbnormalService.getChangedWorkbook(checkDto,compareResultDto,workbook);
//
//        File file = new File("D://a2.xls");
//        workbook.write(file);
//    }

    /**
     * 导出异动信息excel
     *
     * @param compareResultSaicCheckId 定时检查企业异常任务 获取的结果id
     * @param compareResultId
     * @param saicInfoId               工商查询结果id
     */
    @PostMapping(value = "/exportAbnormalExcel")
    public void exportAbnormalExcel(Long compareResultSaicCheckId, Long compareResultId, Long saicInfoId, HttpServletResponse response) {
        SaicInfoDto saicInfo = saicInfoService.findById(saicInfoId);
        SaicIdpInfo saicIdpInfo = new SaicIdpInfo();
        BeanCopierUtils.copyProperties(saicInfo, saicIdpInfo);
        //设置经营异常信息
        saicIdpInfo.setChangemess(changeMessService.findBySaicInfoId(saicInfo.getId()));
        //设置严重违法
        saicIdpInfo.setIllegals(illegalService.findBySaicInfoId(saicInfo.getId()));

        CompareResultDto compareResultDto = compareResultService.findById(compareResultId);

        CompareResultSaicCheckDto compareResultSaicCheckDto = compareResultSaicCheckService.findById(compareResultSaicCheckId);

        HSSFWorkbook workbook = customerAbnormalService.getBaseInfoWorkbook(saicIdpInfo, null);
        workbook = customerAbnormalService.getIllegalsWorkbook(saicIdpInfo, workbook);//严重违法
        workbook = customerAbnormalService.getChangeMessWorkbook(saicIdpInfo, workbook);//经营异常
        workbook = customerAbnormalService.getBusinessExpiresWorkbook(saicIdpInfo, compareResultSaicCheckDto, workbook);//营业到期
        workbook = customerAbnormalService.getSaicStateWorkbook(compareResultSaicCheckDto, workbook);//工商状态异常
        workbook = customerAbnormalService.getChangedWorkbook(compareResultSaicCheckDto, compareResultDto, workbook);//登记信息异常
        try {
            response.setHeader("Content-disposition", "attachment; filename="
                    + URLEncoder.encode(compareResultDto.getDepositorName() + compareResultSaicCheckDto.getAbnormalTime() + "异动信息", "UTF-8") + ".xls");
            response.setContentType("application/vnd.ms-excel");
            OutputStream fileOut = response.getOutputStream();
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 短信提醒
     * @param ids  要处理的id集合
     */
    @PostMapping("/sendMessage")
    public ResultDto setMessage(@RequestParam("ids[]") Long[] ids) {
        boolean flag = true;
        for (Long id :ids){
            boolean result = false;
            String message = noticeMessage;
            String error = "";

            //获取客户信息
            CustomerAbnormalDto customerAbnormalDto = customerAbnormalService.findOneById(id);
            CustomerPublicInfo customerPublicInfo = customerPublicService.getByDepositorName(customerAbnormalDto.getDepositorName());

            //短信模版转换 //TODO 待优化。
            if (customerPublicInfo!=null){
                message = message.replace("depositorName",customerPublicInfo.getDepositorName());
                message = message.replace("legalName",customerPublicInfo.getLegalName()==null?"null":customerPublicInfo.getLegalName());
            }else {
                log.warn("获取不到客户信息",customerAbnormalDto.getDepositorName());
                customerAbnormalService.chageMessage("0",id);
                flag = false;
                continue;
            }

            //发送短信
            String phoneNum = customerPublicInfo.getLegalTelephone();
            if (phoneNum!=null){

                result = customerSmsService.sendMessage(phoneNum,message);
                if (result){
                    //修改状态(改为已发送)
                    customerAbnormalService.chageMessage("1",id);
                }else {
                    customerAbnormalService.chageMessage("2",id);
                    log.warn("客户:{}，法人或负责人:{},手机号:{},信息发送失败!",
                            customerPublicInfo.getDepositorName(),customerPublicInfo.getLegalName(),customerPublicInfo.getLegalTelephone());
                    flag = false;
                    error = "短信发送异常";
                }



            }else {
                customerAbnormalService.chageMessage("2",id);
                log.warn("客户:{}，无法人手机号信息发送失败!:{}",customerPublicInfo.getDepositorName(),customerPublicInfo.toString());
                flag = false;
                error = "没有手机号信息";
            }

            // save短信发送历史
            CsrMessageDto csrMessageDto = new CsrMessageDto();
            csrMessageDto.setPhone(customerPublicInfo.getLegalTelephone());
            csrMessageDto.setCheckPass(result);
            csrMessageDto.setDepositorName(customerPublicInfo.getDepositorName());
            csrMessageDto.setMessage(message);
            csrMessageDto.setErrorMessage(error);

            csrMessageService.save(csrMessageDto);

        }
        if (flag){
            return ResultDtoFactory.toAck();
        }else {
            return ResultDtoFactory.toNack("发送失败");
        }
    }

    @GetMapping(value = "/message/list")
    public TableResultResponse<CsrMessageDto> list(CsrMessageDto csrMessageDto, @PageableDefault(sort = {"lastUpdateDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        TableResultResponse<CsrMessageDto> tableResultResponse = csrMessageService.page(csrMessageDto,pageable);
        return tableResultResponse;
    }

}
