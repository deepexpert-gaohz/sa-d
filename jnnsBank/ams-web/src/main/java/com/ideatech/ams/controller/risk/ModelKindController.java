package com.ideatech.ams.controller.risk;

import com.ideatech.ams.risk.modelKind.dto.RiskLevelDto;
import com.ideatech.ams.risk.modelKind.dto.RiskLevelSearchDto;
import com.ideatech.ams.risk.modelKind.dto.RiskTypeDto;
import com.ideatech.ams.risk.modelKind.dto.RiskTypeSearchDto;
import com.ideatech.ams.risk.modelKind.service.RiskLevelService;
import com.ideatech.ams.risk.modelKind.service.RiskTypeService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @auther zhuqr
 * @date ${date} ${time}
 */
@RestController
@RequestMapping("/modelKind")
public class ModelKindController {

    @Autowired
    RiskLevelService riskLevelService;



    @Autowired
    RiskTypeService riskTypeService;

    /**
     * 分页查询模型属性
     * @param riskLevelSearchDto
     * @return
     */
    @GetMapping("/levelAll")
    public ResultDto getAll(RiskLevelSearchDto riskLevelSearchDto){
        return ResultDtoFactory.toAckData(riskLevelService.search(riskLevelSearchDto));
    }

    /**
     * 保存风险等级
     * @param riskLevelDto
     * @return
     */
    @PostMapping("/saveLevel")
    public ResultDto saveLevel(RiskLevelDto riskLevelDto){
        RiskLevelDto byLevelName = riskLevelService.findByLevelName(riskLevelDto.getLevelName());
        if(byLevelName != null){
            return ResultDtoFactory.toNack("风险等级已经存在");
        }
        riskLevelService.save(riskLevelDto);
        return ResultDtoFactory.toAck();
    }

    @GetMapping("/getLevel/{id}")
    public ResultDto getLevelById(@PathVariable("id") Long id){
        RiskLevelDto byId = riskLevelService.findById(id);
        return ResultDtoFactory.toAckData(byId);
    }

    @PutMapping("/editLevel/{id}")
    public ResultDto editLevel(@PathVariable("id") Long id, RiskLevelDto riskLevelDto){
        riskLevelDto.setId(id);
        riskLevelService.save(riskLevelDto);
        return ResultDtoFactory.toAck();
    }

    @DeleteMapping("/delLevel/{id}")
    public ResultDto delLevel(@PathVariable("id") Long id){
       riskLevelService.delLevel(id);
        return ResultDtoFactory.toAck();
    }

    /**
     * 分页模糊查询风险类型
     * @param riskTypeSearchDto
     * @return
     */
    @GetMapping("/typeAll")
    public ResultDto getAllType(RiskTypeSearchDto riskTypeSearchDto){
        return ResultDtoFactory.toAckData(riskTypeService.search(riskTypeSearchDto));
    }

    /**
     * 保存风险类型
     * @param riskTypeDto
     * @return
     */
    @PostMapping("/saveType")
    public ResultDto saveType(RiskTypeDto riskTypeDto){
        RiskTypeDto byTypeName = riskTypeService.findByTypeName(riskTypeDto.getTypeName());
        if(byTypeName != null){
            return ResultDtoFactory.toNack("风险类型已存在");
        }
        riskTypeService.save(riskTypeDto);
        return ResultDtoFactory.toAck();
    }

    @GetMapping("/getType/{id}")
    public ResultDto getTypeById(@PathVariable("id") Long id){
        RiskTypeDto byId = riskTypeService.getById(id);
        return ResultDtoFactory.toAckData(byId);
    }

    @PutMapping("/editType/{id}")
    public ResultDto editType(@PathVariable("id") Long id, RiskTypeDto riskTypeDto){
        riskTypeDto.setId(id);
        riskTypeService.save(riskTypeDto);
        return ResultDtoFactory.toAck();
    }

    @DeleteMapping("/delType/{id}")
    public ResultDto delType(@PathVariable("id") Long id){
        riskTypeService.delType(id);
        return ResultDtoFactory.toAck();
    }

    /**
     * 查询所有风险等级
     * @return
     */
    @GetMapping("findLevelAll")
    public ResultDto findLevelAll(){
        List<RiskLevelDto> all = riskLevelService.findAll();
        return ResultDtoFactory.toAckData(all);
    }

    /**
     * 查询所有风险类型
     * @return
     */
    @GetMapping("findTypeAll")
    public ResultDto findTypeAll() {
        List<RiskTypeDto> all = riskTypeService.findAll ();
        return ResultDtoFactory.toAckData ( all );
    }

}
