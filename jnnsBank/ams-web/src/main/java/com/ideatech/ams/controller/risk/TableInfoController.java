package com.ideatech.ams.controller.risk;

import com.ideatech.ams.risk.tableManager.dto.TableInfoDto;
import com.ideatech.ams.risk.tableManager.dto.TableInfoSearchDto;
import com.ideatech.ams.risk.tableManager.service.TableInfoService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 源表列表
 */
@RestController
@RequestMapping("tableInfo")
public class TableInfoController {

    @Autowired
    TableInfoService tableInfoService;

    @PostMapping("saveTableInfo")
    public ResultDto saveTableInfo(TableInfoDto tableInfoDto){
        TableInfoDto byCname = tableInfoService.findByEname(tableInfoDto.getEname());
        if (null != byCname){
            return ResultDtoFactory.toNack("英文名称存在");
        }
        tableInfoService.saveTableInfo(tableInfoDto);
        return ResultDtoFactory.toAck();
    }

    @GetMapping("searchTableInfo")
    public ResultDto searchTableInfo(TableInfoSearchDto tableInfoSearchDto){
        return ResultDtoFactory.toAckData(tableInfoService.searchTableInfoDto(tableInfoSearchDto));
    }

    @GetMapping("/{id}")
    public ResultDto getById(@PathVariable("id") Long id){
        return ResultDtoFactory.toAckData(tableInfoService.findTableInfoDtoById(id));
    }

    @DeleteMapping("/{id}")
    public ResultDto delById(@PathVariable("id") Long id){
        tableInfoService.delTableInfo(id);
        return ResultDtoFactory.toAck();
    }

    @PutMapping("/{id}")
    public ResultDto updateTableInfo(@PathVariable("id") Long id, TableInfoDto tableInfoDto){
        tableInfoDto.setId(id);
        tableInfoService.saveTableInfo(tableInfoDto);
        return ResultDtoFactory.toAck();
    }

    @GetMapping("getByCname/{ename}")
    public ResultDto getByCname(@PathVariable("ename") String ename){
        TableInfoDto byEname = tableInfoService.findByEname(ename);
        return ResultDtoFactory.toAckData(byEname);
    }

}
