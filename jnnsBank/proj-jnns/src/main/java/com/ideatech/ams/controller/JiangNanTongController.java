package com.ideatech.ams.controller;

import com.alibaba.fastjson.JSON;
import com.ideatech.ams.dao.ZhjnCustomerDao;
import com.ideatech.ams.domain.zhjn.ZhjnCustomerInfo;
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
@RequestMapping("/jiangNanTong")
@Slf4j
public class JiangNanTongController  {

    @Autowired
    private ZhjnCustomerService zhjnCustomerService;


    @Autowired
    private ZhjnCustomerDao zhjnCustomerDao;




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
    public TableResultResponse<ZhjnCustomerDto> list(ZhjnCustomerDto dto, @PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) {

        TableResultResponse<ZhjnCustomerDto> tableResultResponse = zhjnCustomerService.query(dto, pageable);

        return tableResultResponse;
    }


    public static void main(String[] args) {
        System.out.println(UUID.randomUUID());
    }
}
