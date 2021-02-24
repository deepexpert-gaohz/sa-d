package com.ideatech.ams.controller;

import com.alibaba.fastjson.JSON;
import com.ideatech.ams.system.blacklist.dto.BlackListEntryDto;
import com.ideatech.ams.system.blacklist.dto.BlackListSearchDto;
import com.ideatech.ams.system.blacklist.service.BlackListService;
import com.ideatech.ams.vo.BlackListExcelRowVo;
import com.ideatech.common.constant.ResultCode;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.excel.util.ImportExcel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.jws.WebMethod;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author liangding
 * @create 2018-06-26 上午11:08
 **/
@RestController
@RequestMapping("/blacklist")
@Slf4j
public class BlackListController {
    @Autowired
    private BlackListService blackListService;

    @GetMapping("/")
    public ResultDto<List<BlackListEntryDto>> list() {
        List<BlackListEntryDto> list = blackListService.list();
        return ResultDtoFactory.toAckData(list);
    }

    @GetMapping("/search")
    public ResultDto<BlackListSearchDto> search(BlackListSearchDto blackListSearchDto) {
        BlackListSearchDto searchDto = blackListService.search(blackListSearchDto);
        return ResultDtoFactory.toAckData(searchDto);
    }

    @PostMapping("/")
    public ResultDto create(BlackListEntryDto blackListEntryDto) {
//        blackListService.create(blackListEntryDto);
        return blackListService.create(blackListEntryDto);
    }

    @PutMapping("/{id}")
    public ResultDto update(@PathVariable("id") Long id, BlackListEntryDto blackListEntryDto) {
        blackListEntryDto.setId(id);
        blackListService.update(blackListEntryDto);
        return ResultDtoFactory.toAck();
    }

    @DeleteMapping("/{id}")
    public ResultDto delete(@PathVariable("id") Long id) {
        blackListService.delete(id);
        return ResultDtoFactory.toAck();
    }

    @GetMapping("/{id}")
    public ResultDto<BlackListEntryDto> getById(@PathVariable("id") Long id) {
        BlackListEntryDto blackListEntryDto = blackListService.getById(id);
        return ResultDtoFactory.toAckData(blackListEntryDto);
    }

    @PostMapping("/upload")
    @WebMethod(exclude = true)
    public void upload(@RequestParam("file") MultipartFile file,HttpServletResponse response) throws IOException {
        ResultDto dto = new ResultDto();
        try {
            ImportExcel importExcel = new ImportExcel(file, 0, 0);
            if (importExcel.getRow(0).getPhysicalNumberOfCells() != 4) {
                dto.setCode(ResultCode.NACK);
                dto.setMessage("导入失败，错误的模板");
                //return ResultDtoFactory.toNack("导入失败，错误的模板");
            }else{
                List<BlackListExcelRowVo> dataList = importExcel.getDataList(BlackListExcelRowVo.class);
                /*for(BlackListExcelRowVo blackListExcelRowVo : dataList){
                    if(StringUtils.equalsIgnoreCase(blackListExcelRowVo.getIsWhiteStr(),"是")){
                        blackListExcelRowVo.setIsWhite(true);
                    }else if(StringUtils.equalsIgnoreCase(blackListExcelRowVo.getIsWhiteStr(),"否")){
                        blackListExcelRowVo.setIsWhite(false);
                    }
                }*/
                //所有黑名单数据
                List<BlackListEntryDto> blackLists = blackListService.list();
                //导入黑名单数据
                List<BlackListEntryDto> blackListEntryDtos = ConverterService.convertToList(dataList, BlackListEntryDto.class);
                //去重
                Set<BlackListEntryDto> blackListEntryDtos1 = new HashSet<>();
                for (BlackListEntryDto blackListEntryDto : blackListEntryDtos) {
                    blackListEntryDtos1.add(blackListEntryDto);
                }
                //重复数据不增加
                for (BlackListEntryDto blackListEntryDto : blackListEntryDtos1) {
                    for(BlackListEntryDto blackListEntryDto1 : blackLists){
                        if(StringUtils.equals(blackListEntryDto.getEntName(),blackListEntryDto1.getEntName())){
                            continue;
                        }
                    }
                    blackListService.create(blackListEntryDto);
                }
//                for (BlackListEntryDto blackListEntryDto : blackListEntryDtos) {
//                    if (StringUtils.isBlank(blackListEntryDto.getEntName())) {
//                        continue;
//                    }
//                    blackListService.create(blackListEntryDto);
//                }
                dto.setCode(ResultCode.ACK);
                dto.setMessage("导入成功");
            }
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(dto));
        } catch (Exception e) {
            log.error("导入黑名单失败", e);
            dto.setCode(ResultCode.NACK);
            dto.setMessage("导入黑名单失败");
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(dto));
            //return ResultDtoFactory.toNack("导入黑名单失败");
        }

    }
}
