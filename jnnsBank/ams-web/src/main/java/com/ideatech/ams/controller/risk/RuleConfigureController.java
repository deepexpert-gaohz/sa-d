package com.ideatech.ams.controller.risk;

import com.ideatech.ams.risk.model.dto.ModelDto;
import com.ideatech.ams.risk.model.dto.ModelSearchDto;
import com.ideatech.ams.risk.model.service.ModelFieldService;
import com.ideatech.ams.risk.model.service.ModelService;
import com.ideatech.ams.risk.modelKind.dto.RiskTypeDto;
import com.ideatech.ams.risk.modelKind.service.RiskTypeService;
import com.ideatech.ams.risk.riskdata.dto.RiskDetailsReturnDto;
import com.ideatech.ams.risk.rule.dto.RuleConfigureDto;
import com.ideatech.ams.risk.rule.dto.RuleFieldDto;
import com.ideatech.ams.risk.rule.dto.RuleSearchDto;
import com.ideatech.ams.risk.rule.service.RuleConfigService;
import com.ideatech.ams.risk.util.RiskUtil;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * author liuz 2019-05-06
 */
@RestController
@RequestMapping("/ruleConfigure")
public class RuleConfigureController {
    @Autowired
    ModelService modelService;
    @Autowired
    RuleConfigService ruleConfigService;
    @Autowired
    RiskTypeService riskTypeService;
    @Autowired
    ModelFieldService modelFieldService;



    @GetMapping("/getModels")
    public RiskDetailsReturnDto modelList(ModelSearchDto modelSearchDto){
        // ModelSearchDto model = modelService.findModel(modelSearchDto);
        //获取模型类型

        List<RiskTypeDto> riskTypeAll = riskTypeService.findAll();
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (RiskTypeDto r : riskTypeAll) {
            Map<String, Object> map = new HashMap<String,Object>();
            map.put("id", r.getId());
            map.put("pId", r.getParentId());
            map.put("pIds", r.getParentIds());
            map.put("name", r.getTypeName());
            mapList.add(map);
        }
        List<ModelDto> allModel = modelService.findAllModel();
        for (ModelDto m:allModel){
            Map<String, Object> map = new HashMap<String,Object>();
            map.put("id", m.getId());
            map.put("pId", m.getTypeId());
            map.put("pIds", 0);
            map.put("name", m.getName());
            map.put("modelId",m.getModelId());
            mapList.add(map);
        }
        RiskDetailsReturnDto riskDetailsReturnDto = new RiskDetailsReturnDto();
        riskDetailsReturnDto.setList(mapList);
        riskDetailsReturnDto.setTotalRecord((long)mapList.size());
        riskDetailsReturnDto.setTotalPages(100);
        return riskDetailsReturnDto;
    }

    /**
     * 保存规则
     * @param ruleConfigureDto
     * @return
     */
    @PostMapping("/save")
    public ResultDto saveRule(RuleConfigureDto ruleConfigureDto){
        getVale ( ruleConfigureDto );
        return ResultDtoFactory.toAck();
    }

    private void getVale(RuleConfigureDto ruleConfigureDto) {
        String conAndVal="";
        String condition="";
        switch (ruleConfigureDto.getCondition()){
            case "请选择":
                conAndVal=ruleConfigureDto.getValue();
                condition=" ";
                break;
            case "":
                conAndVal=ruleConfigureDto.getValue();
                condition=" ";
                break;
            case "bigThan":
                conAndVal=">"+ruleConfigureDto.getValue();
                condition=">";
                break;
            case "lessThan":
                conAndVal="<"+ruleConfigureDto.getValue();
                condition="<";
                break;
            case "equal":
                conAndVal="="+ruleConfigureDto.getValue();
                condition="=";
                break;
            case "bigEqualThan":
                conAndVal=">="+ruleConfigureDto.getValue();
                condition=">=";
                break;
            case "lessEqualThan":
                conAndVal="<="+ruleConfigureDto.getValue();
                condition="<=";
                break;
            case "notEqual":
                conAndVal="!="+ruleConfigureDto.getValue();
                condition="!=";
                break;
            case "in":
                conAndVal="in ("+ruleConfigureDto.getValue()+")";
                condition="in";
                break;
            case "notIn":
                conAndVal="not in ("+ruleConfigureDto.getValue()+")";
                condition="not in";
                break;
            case "like":
                conAndVal="like"+ruleConfigureDto.getValue();
                condition="like";
                break;
            case "between":
                conAndVal="between "+ruleConfigureDto.getValue()+" and "+ruleConfigureDto.getInValueS();
                condition="between";
                ruleConfigureDto.setValue(ruleConfigureDto.getValue()+","+ruleConfigureDto.getInValueS());
                break;
            default:
                break;
        }
        ruleConfigureDto.setConAndVal(conAndVal);
        ruleConfigureDto.setCondition(condition);
        ruleConfigService.saveRuleConfiger(ruleConfigureDto);
    }


    /**
     * 修改
     * @param ruleConfigureDto
     * @return
     */
    @PutMapping("/{id}")
    public ResultDto saveRule(@PathVariable Long id, RuleConfigureDto ruleConfigureDto){
        ruleConfigureDto.setId(id);
        getVale ( ruleConfigureDto );
        return ResultDtoFactory.toAck("更新成功");
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResultDto del(@PathVariable("id") Long id){
        ruleConfigService.deleteConfiger(id);
        return ResultDtoFactory.toAck();
    }

    @GetMapping("/getField")
    public ResultDto getField(RuleSearchDto ruleSearchDto){
        if (ruleSearchDto.getId()!=null&&!ruleSearchDto.getId().equalsIgnoreCase("")){
            ModelDto byId = modelService.findById(Long.parseLong(ruleSearchDto.getId()));
            ruleSearchDto.setModelId(byId.getModelId());
        }
        // RuleSearchDto ruleFieldByModelId = ruleConfigService.findRuleFieldByModelId(ruleSearchDto);
        return  ResultDtoFactory.toAckData( ruleConfigService.findRuleFieldByModelId(ruleSearchDto));
    }

    /**
     * @author liuz 2019-05-07
     * 通过modelid 获取field
     */
    @GetMapping("/findField/{id}")
    public ResultDto<List<RuleFieldDto>> findField(@PathVariable("id") Long id){
        ModelDto byId = modelService.findById(id);
        List<RuleFieldDto> byModelId = new ArrayList<>();
        if(byId.getModelId()!=null&&!byId.getModelId().equalsIgnoreCase("")){
            byModelId=ruleConfigService.findByModelId(byId.getModelId().toUpperCase());
        }
        return  ResultDtoFactory.toAckData(byModelId);
    }
    /**
     * @author liuz 2019-05-07
     * 通过modelid 获取field
     */
    @GetMapping("/findEditField/{id}")
    public ResultDto<List<RuleFieldDto>> findEditField(@PathVariable("id") Long id){
        ModelDto byId = modelService.findById(id);
        List<RuleFieldDto> byModelId = new ArrayList<>();
        if(byId.getModelId()!=null&&!byId.getModelId().equalsIgnoreCase("")){
            byModelId=ruleConfigService.findByModelId(byId.getModelId().toUpperCase());
        }
        RuleFieldDto fieldById = ruleConfigService.findFieldById(id);
        byModelId.add(fieldById);
        return  ResultDtoFactory.toAckData(byModelId);
    }



    /**
     * @author liuz 2019-05-07
     * 通过id 获取修改时回显的数据
     */
    @GetMapping("/findConfigerById/{id}")
    public ResultDto<RuleConfigureDto> findConfiger(@PathVariable("id") Long id){
        RuleConfigureDto configerById = new RuleConfigureDto();
        configerById = ruleConfigService.findConfigerById(id);
        //如果是between  则将value中的值拆分到value 和 inValueF中
        if (configerById.getCondition().equalsIgnoreCase("between")){
            String[] split = configerById.getValue().split(",");
            configerById.setValue(split[0]);
            configerById.setInValueS(split[1]);
        }
        return  ResultDtoFactory.toAckData(configerById);
    }

    /**
     * author liuz 2019-05-8
     * 修改时通过id获取字段信息的回显数据
     * @param id
     * @return
     */
    @GetMapping("/findFieldById/{id}")
    public ResultDto<RuleFieldDto> findFieldByID(@PathVariable("id") Long id) {
        RuleFieldDto fieldById = new RuleFieldDto();
        if(id!=null){
            fieldById=ruleConfigService.findFieldById(id);
        }
        return ResultDtoFactory.toAckData(fieldById);
    }
    @GetMapping("/getModelRuleByModelId/{modelId}")
    public ResultDto getModelRuleByModelId(@PathVariable("modelId") String modelId) {
        List<RuleConfigureDto> list = ruleConfigService.getModelRuleByModelId(modelId, RiskUtil.getOrganizationCode());
        return ResultDtoFactory.toAckData(list);
    }

    /**
     * 模型规则初始化
     * liuz 20191114
     * @return
     */
    @GetMapping("/initRule")
    public ResultDto modelRuleInit(){
        ResultDto resultDto =new ResultDto();
        Boolean b = ruleConfigService.initRule();
        if(b){
            resultDto.setMessage("模型规则初始化成功!");
        }else{
            resultDto.setMessage("模型规则初始化失败!");
        }
        return resultDto;
    }
}
