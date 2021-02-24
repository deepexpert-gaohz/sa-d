package com.ideatech.ams.controller;

import com.alibaba.fastjson.JSON;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.whitelist.dto.WhiteListDto;
import com.ideatech.ams.system.whitelist.dto.WhiteListSearchDto;
import com.ideatech.ams.system.whitelist.enums.WhiteListEntrySource;
import com.ideatech.ams.system.whitelist.service.WhiteListService;
import com.ideatech.ams.vo.WhiteListExcelRowVo;
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
import java.util.List;

/**
 * @author liangding
 * @create 2018-06-26 上午11:08
 **/
@RestController
@RequestMapping("/whitelist")
@Slf4j
public class WhiteListController {

    @Autowired
    private WhiteListService whiteListService;

    @Autowired
    private OrganizationService organizationService;

    @GetMapping("/")
    public ResultDto<List<WhiteListDto>> list() {
        List<WhiteListDto> list = whiteListService.list();
        return ResultDtoFactory.toAckData(list);
    }

    @GetMapping("/search")
    public ResultDto<WhiteListSearchDto> search(WhiteListSearchDto whiteListSearchDto) {
        WhiteListSearchDto searchDto = whiteListService.search(whiteListSearchDto);
        return ResultDtoFactory.toAckData(searchDto);
    }

    @PostMapping("/")
    public ResultDto create(WhiteListDto whiteListDto) {
        ResultDto dto = whiteListService.create(whiteListDto);
        return dto;
    }

    @PutMapping("/{id}")
    public ResultDto update(@PathVariable("id") Long id, WhiteListDto whiteListDto) {
        whiteListDto.setId(id);
        ResultDto dto = whiteListService.update(whiteListDto);
        return dto;
    }

    @DeleteMapping("/{id}")
    public ResultDto delete(@PathVariable("id") Long id) {
        whiteListService.delete(id);
        return ResultDtoFactory.toAck();
    }


    @GetMapping("/{id}")
    public ResultDto<WhiteListDto> getById(@PathVariable("id") Long id) {
        WhiteListDto whiteListDto = whiteListService.getById(id);
        return ResultDtoFactory.toAckData(whiteListDto);
    }

    @GetMapping("/findByName")
    public boolean getById(String name) {
        WhiteListDto whiteListDto = whiteListService.getByEntName(name);
        if(whiteListDto != null && StringUtils.equals("normal",whiteListDto.getStatus())){
            return true;
        }else{
            return false;
        }
    }

    @PostMapping("/upload")
    @WebMethod(exclude = true)
    public void upload(@RequestParam("file") MultipartFile file,HttpServletResponse response) throws IOException {
        ResultDto dto = new ResultDto();
        try {
            ImportExcel importExcel = new ImportExcel(file, 0, 0);
            if (importExcel.getRow(0).getPhysicalNumberOfCells() != 2) {
                dto.setCode(ResultCode.NACK);
                dto.setMessage("导入失败，错误的模板");
                //return ResultDtoFactory.toNack("导入失败，错误的模板");
            }else{
                List<WhiteListExcelRowVo> dataList = importExcel.getDataList(WhiteListExcelRowVo.class);

                List<WhiteListDto> whiteListDtos = ConverterService.convertToList(dataList, WhiteListDto.class);
                for (WhiteListDto whiteListDto : whiteListDtos) {
                    if (StringUtils.isBlank(whiteListDto.getEntName())) {
                        continue;
                    }
                    OrganizationDto organizationDto = organizationService.findByCode(whiteListDto.getOrganCode());
                    if(organizationDto == null){
                        continue;
                    }
                    whiteListDto.setOrgId(organizationDto.getId());
                    whiteListDto.setOrgName(organizationDto.getName());
                    whiteListDto.setOrganCode(organizationDto.getCode());
                    whiteListDto.setOrganFullId(organizationDto.getFullId());
                    whiteListDto.setSource(WhiteListEntrySource.IMPORT);
                    whiteListService.create(whiteListDto);
                }
                dto.setCode(ResultCode.ACK);
                dto.setMessage("导入成功");
            }
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(dto));
        } catch (Exception e) {
            log.error("导入白名单失败", e);
            dto.setCode(ResultCode.NACK);
            dto.setMessage("导入白名单失败");
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(dto));
        }
    }
}
