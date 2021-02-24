package com.ideatech.ams.controller.customer.Illegal;

import com.alibaba.fastjson.JSON;
import com.ideatech.ams.customer.dto.illegal.IllegalQueryBatchDto;
import com.ideatech.ams.customer.dto.illegal.IllegalQueryDto;
import com.ideatech.ams.customer.service.illegal.IllegalQueryErrorService;
import com.ideatech.ams.customer.service.illegal.IllegalQueryService;
import com.ideatech.ams.vo.Illegal.IllegalQueryVo;
import com.ideatech.common.constant.ResultCode;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.excel.util.ExportExcel;
import com.ideatech.common.excel.util.ImportExcel;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.msg.ObjectRestResponse;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.util.BrowserUtil;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.jws.WebMethod;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Slf4j
@RestController
@RequestMapping(value = "/illegalQuery")
public class IllegalQueryController {

    @Autowired
    private IllegalQueryService illegalQueryService;

    @Autowired
    private IllegalQueryErrorService illegalQueryErrorService;

    @Value("${ams.saicCollection.executor.num:5}")
    private int saicCollectExecutorNum;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @GetMapping("/queryBatchList")
    public TableResultResponse<IllegalQueryBatchDto> batchList(IllegalQueryBatchDto dto, @PageableDefault(sort = {"lastUpdateDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        TableResultResponse<IllegalQueryBatchDto> tableResultResponse = illegalQueryService.queryBatch(dto, pageable);

        return tableResultResponse;
    }

    @GetMapping("/queryList")
    public TableResultResponse<IllegalQueryDto> list(IllegalQueryDto dto, @PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        dto.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
        TableResultResponse<IllegalQueryDto> tableResultResponse = illegalQueryService.query(dto, pageable);

        return tableResultResponse;
    }

    @PostMapping(value = "/upload")
    @WebMethod(exclude = true)
    public void upload(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException {
        log.info("开始导入严重违法数据...");
        ResultDto dto = new ResultDto();
        List<String> regNoList = new ArrayList<>();
        IllegalQueryBatchDto illegalQueryBatchDto = new IllegalQueryBatchDto();

        List<Integer> strNum = new ArrayList<>();
        //chongfu数据计数
        strNum.add(0);
        //nofind数据计数
        strNum.add(0);

        try {
            ImportExcel importExcel = new ImportExcel(file, 0, 0);
            if (importExcel.getRow(0) != null && importExcel.getRow(0).getPhysicalNumberOfCells() != 3 ) {
                ResultDtoFactory.toNack("导入失败，错误的模板");
                dto.setCode(ResultCode.NACK);
                dto.setMessage("导入失败，错误的模板");
                log.info("导入模版错误...");
            } else {
                long starttime = System.currentTimeMillis();
                List<IllegalQueryVo> dataList = importExcel.getDataList(IllegalQueryVo.class);
                if(CollectionUtils.isEmpty(dataList)) {
                    dto.setCode(ResultCode.NACK);
                    dto.setMessage("导入文件为空...请检查后再次导入...");
                    response.setContentType("text/html; charset=utf-8");
                    response.getWriter().write(JSON.toJSONString(dto));
                    return;
                }
                log.info("模版内数据行数："+dataList.size());
                long endtime = System.currentTimeMillis();
                log.info("用时:" + (endtime - starttime) +"");
                starttime = System.currentTimeMillis();
                //生成批次号
                String illegalBatchId = getBatchId();
                illegalQueryBatchDto.setIllegalbatchNo(illegalBatchId);
                illegalQueryBatchDto.setBatchDate(DateUtils.DateToStr(new Date(),"yyyy-MM-dd"));
                illegalQueryBatchDto.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
                illegalQueryBatchDto.setFileName(file.getOriginalFilename());
                illegalQueryBatchDto.setFileSize(file.getSize());
                Long batchId = illegalQueryService.saveIllegalQueryBatch(illegalQueryBatchDto);
                illegalQueryBatchDto.setId(batchId);

                if (CollectionUtils.isNotEmpty(dataList) && dataList.size() > 0) {
                    List<IllegalQueryDto> illegalQueryDtoList = ConverterService.convertToList(dataList, IllegalQueryDto.class);
                    illegalQueryService.saveIllegalQuery(illegalQueryDtoList, regNoList, batchId, strNum);
                }

                long endtime1 = System.currentTimeMillis();
                log.info("for用时:" + (endtime1 - starttime) +"");
                starttime = System.currentTimeMillis();

                //保存导入的数量
//                long illegalQueryNum = illegalQueryService.getIllegalQueryNum(batchId);
//                illegalQueryBatchDto.setBatchNum((int)illegalQueryNum);
                illegalQueryBatchDto.setBatchNum(regNoList.size());
                illegalQueryBatchDto.setProcess(false);
                illegalQueryService.saveIllegalQueryBatch(illegalQueryBatchDto);

                int totalCount = regNoList.size() + strNum.get(0);
                dto.setCode(batchId+"");
                dto.setMessage("导入成功,未查询到机构"+strNum.get(1)+"条，有效机构企业总数" + totalCount + "条：重复"+ strNum.get(0) + "条,去重后有效"+regNoList.size() + "条。");

                endtime1 = System.currentTimeMillis();
                log.info("保存用时:" + (endtime1 - starttime) +"");
            }
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(dto));
        } catch (Exception e) {
            log.error("导入Excel失败", e);
            dto.setCode(ResultCode.NACK);
            dto.setMessage("导入Excel失败");
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(dto));
        }
    }

    public String getBatchId(){
        String batchId= "";
        String timeMillis = System.currentTimeMillis()+"";
        timeMillis = timeMillis.substring(8);
        batchId = DateUtils.DateToStr(new Date(),"yyyyMMdd") + timeMillis;
        return batchId;
    }


    @GetMapping("/illegaListCheck")
    public void illegal2IDP(Long batchId){
        log.info("开始根据导入数据调用IDP接口查询...");
        illegalQueryService.illegalCheck(batchId);
        log.info("根据导入数据调用IDP接口查询结束...");
    }

    @GetMapping("/checkIllegalExpired")
    public Boolean checkIllegalExpired(Long batchId){
        log.info("查询是否存在营业证件到期企业......");
        Boolean res = illegalQueryService.checkIllegalExpired(batchId);
        return res;
    }

    @GetMapping("/illegalQueryCheck")
    public ObjectRestResponse<IllegalQueryDto> check(Long id, String keyword){
        log.info("单条调用IDP接口查询严重违法信息...");
        IllegalQueryDto illegalQueryDto = illegalQueryService.illegalQueryCheck(id, keyword);

        if(illegalQueryDto != null) {
            return new ObjectRestResponse<IllegalQueryDto>().rel(true).result(illegalQueryDto);
        } else {
            return new ObjectRestResponse<IllegalQueryDto>().rel(false);
        }

    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void downloadIllegalQueryResultExcel(IllegalQueryDto dto, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        dto.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
        IExcelExport illegalQueryExcel = illegalQueryService.exportIllegalQueryExcel(dto);
        response.setHeader("Content-disposition", "attachment; filename="+generateFileName("严重违法信息",request)+".xls");
        response.setContentType("application/octet-stream");
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        ExportExcel.export(response.getOutputStream(),"yyyy-MM-dd",illegalQueryExcel);
        toClient.flush();
        response.getOutputStream().close();
    }

    @RequestMapping(value = "/checkIllegalStatus", method = RequestMethod.GET)
    public ObjectRestResponse checkIllegalStatus(Long batchId) {
        Boolean b = illegalQueryService.checkIllegalStatus(batchId);

        if(b) {
            return new ObjectRestResponse().rel(true);
        }
        return new ObjectRestResponse().rel(false);
    }

    /**
     * 生成导出文件名
     * @param
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    private String generateFileName(String fileName1, HttpServletRequest request) throws UnsupportedEncodingException {
        StringBuilder fileName = new StringBuilder();
        fileName.append(BrowserUtil.generateFileName(fileName1,request))
                .append("-").append(DateUtils.DateToStr(new Date(),"yyyy-MM-dd"));
        return fileName.toString();
    }

}
