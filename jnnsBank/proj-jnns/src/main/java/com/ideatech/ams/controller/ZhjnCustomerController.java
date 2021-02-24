package com.ideatech.ams.controller;

import com.alibaba.fastjson.JSON;
import com.ideatech.ams.dao.ZhjnCustomerDao;
import com.ideatech.ams.domain.zhjn.*;
import com.ideatech.ams.dto.JiangNanTongExcelRowVo;
import com.ideatech.ams.dto.ZhjnCustomerDto;
import com.ideatech.ams.service.ZhjnCustomerService;
import com.ideatech.common.constant.ResultCode;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.excel.util.ImportExcel;
import com.ideatech.common.msg.TableResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.jws.WebMethod;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/zhjn")
@Slf4j
public class ZhjnCustomerController {


    @Autowired
    private ZhjnCustomerService zhjnCustomerService;


    @Autowired
    private ZhjnCustomerDao zhjnCustomerDao;


    @GetMapping(value = "testAdd")
    public void testAdd() {
        System.out.println("testAdd------");
        ZhjnCustomerInfo info = new ZhjnCustomerInfo();
        info.setBankCode("117451100331222");
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        info.setOrderId(uuid);
        info.setCreateTime("2020-08-07");
        info.setClerkNo("00001");
        info.setClerkName("张三");
        info.setCheckName("李四");
        info.setCheckNo("00002");
        info.setCustomerStatus(0L);

        ZhjnCustomerInfo info2 = new ZhjnCustomerInfo();
        info2.setBankCode("23564870799999275");
        String uuid2 = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        info2.setOrderId(uuid2);
        info2.setCreateTime("2020-08-07");
        info2.setClerkNo("00001");
        info2.setCheckNo("00003");
        info2.setClerkName("张三");
        info2.setCheckName("王五");
        info2.setCustomerStatus(0L);
        zhjnCustomerService.testAdd(info, info2);
    }


    /**
     * 经办人查询列表接口
     *
     * @param req
     * @return
     */
    @PostMapping(value = "/getCustomerByClerk")
    public ResultDto getCustomerByClerk(@RequestBody ZhjnClerkListReq req) {
        ResultDto resultDto = new ResultDto();
        if (StringUtils.isBlank(req.getClerkNo())) {
            resultDto.setCode("1");
            resultDto.setMessage("行员号为空");
            return resultDto;
        }
        resultDto = zhjnCustomerService.getCustomerByClerk(req);
        return resultDto;
    }


    /**
     * 有权人查询列表接口
     *
     * @param req
     * @return
     */
    @PostMapping(value = "/getCustomerByCheck")
    public ResultDto getCustomerByCheck(@RequestBody ZhjnCheckListReq req) {
        ResultDto resultDto = new ResultDto();
        if (StringUtils.isBlank(req.getCheckNo())) {
            resultDto.setCode("1");
            resultDto.setMessage("行员号为空");
            return resultDto;
        }
        resultDto = zhjnCustomerService.getCustomerByCheck(req);
        return resultDto;
    }

    /**
     * 经办人办理接口
     *
     * @param req
     * @return
     */
    @PostMapping(value = "/clerkCustomer")
    public ResultDto clerkCustomer(@RequestBody ZhjnClerkReq req) {
        ResultDto resultDto = new ResultDto();
        if (StringUtils.isBlank(req.getOrderId())) {
            resultDto.setCode("1");
            resultDto.setMessage("任务编码为空");
            return resultDto;
        }
        if (StringUtils.isBlank(req.getLocation())) {
            resultDto.setCode("1");
            resultDto.setMessage("定位为空");
            return resultDto;
        }
        if (StringUtils.isBlank(req.getImageNo())) {
            resultDto.setCode("1");
            resultDto.setMessage("影像批次号为空");
            return resultDto;
        }
        //修改客户
        resultDto = zhjnCustomerService.clerkCustomer(req);
        return resultDto;
    }


    /**
     * 有权人审批接口
     *
     * @param req
     * @return
     */
    @PostMapping(value = "/checkCustomer")
    public ResultDto checkCustomer(@RequestBody ZhjnCheckReq req) {
        ResultDto resultDto = new ResultDto();
        System.out.println(req);
        if (StringUtils.isBlank(req.getOrderId())) {
            resultDto.setCode("1");
            resultDto.setMessage("任务编码为空");
            return resultDto;
        }
        if (req.getCustomerStatus()==null || (req.getCustomerStatus() != 2L && req.getCustomerStatus() != 3L) ) {
            resultDto.setCode("1");
            resultDto.setMessage("审核结论错误");
            return resultDto;
        }
        if (StringUtils.isBlank(req.getCheckMessage())) {
            resultDto.setCode("1");
            resultDto.setMessage("审核意见为空");
            return resultDto;
        }
        resultDto = zhjnCustomerService.checkCustomer(req);
        return resultDto;
    }




    @PostMapping(value = "/upload")
    @WebMethod(exclude = true)
    public void upload(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException {
        ResultDto dto = new ResultDto();
        try {
            ImportExcel importExcel = new ImportExcel(file, 0, 0);
            if (importExcel.getRow(0).getPhysicalNumberOfCells() < 5) {
                ResultDtoFactory.toNack("导入失败，错误的模板");
                dto.setCode(ResultCode.NACK);
                dto.setMessage("导入失败，错误的模板");
            } else {
                int success=0;
                int fail = 0;
                List<JiangNanTongExcelRowVo> dataList = importExcel.getDataList(JiangNanTongExcelRowVo.class);

                for (JiangNanTongExcelRowVo jiangNanTongExcelRowVo : dataList) {

                    ZhjnCustomerInfo zhjnCustomerInfo = ConverterService.convert(jiangNanTongExcelRowVo, ZhjnCustomerInfo.class);
                    zhjnCustomerInfo.setCheckNo(jiangNanTongExcelRowVo.getPersonNo());
                    zhjnCustomerInfo.setCheckName(jiangNanTongExcelRowVo.getPersonName());
                    zhjnCustomerInfo.setClerkName(jiangNanTongExcelRowVo.getOperatorName());
                    zhjnCustomerInfo.setClerkNo(jiangNanTongExcelRowVo.getOperatorNo());
                    zhjnCustomerInfo.setBankCode(jiangNanTongExcelRowVo.getAcctNo());
                    zhjnCustomerInfo.setOrderId(UUID.randomUUID().toString());
                    zhjnCustomerInfo.setCustomerStatus(0L);
                    Date date=new Date();
                    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    zhjnCustomerInfo.setCheckTime(format.format(date));
                    zhjnCustomerDao.save(zhjnCustomerInfo);
                    success++;
                }
                dto.setCode(ResultCode.ACK);
                dto.setMessage("导入成功"+success+"条，失败"+fail+"条,失败详细请查看日志。");
            }

            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(dto));
//            return ResultDtoFactory.toAck();
        } catch (Exception e) {
            log.error("导入用户失败", e);
            dto.setCode(ResultCode.NACK);
            dto.setMessage("导入用户失败");
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(dto));
//            return ResultDtoFactory.toNack("导入用户失败");
        }
    }

    @GetMapping("/queryList")
    public TableResultResponse<ZhjnCustomerDto> queryList(ZhjnCustomerDto dto, @PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) {

        TableResultResponse<ZhjnCustomerDto> tableResultResponse = zhjnCustomerService.query(dto, pageable);

        return tableResultResponse;
    }

    @GetMapping("/list")
    public TableResultResponse<ZhjnCustomerDto> list(ZhjnCustomerDto dto, @PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) {

        TableResultResponse<ZhjnCustomerDto> tableResultResponse = zhjnCustomerService.query1(dto, pageable);

        return tableResultResponse;
    }



}
