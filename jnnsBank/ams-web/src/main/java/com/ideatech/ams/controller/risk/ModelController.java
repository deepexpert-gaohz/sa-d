package com.ideatech.ams.controller.risk;


import com.ideatech.ams.risk.model.dao.ModelDao;
import com.ideatech.ams.risk.model.dto.ModelDto;
import com.ideatech.ams.risk.model.dto.ModelSearchDto;
import com.ideatech.ams.risk.model.service.ModelService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/model")
public class ModelController {

    @Autowired
    ModelService modelService;
    @Autowired
    ModelDao modelDao;

    @PostMapping("/saveModel")
    public ResultDto saveModel(ModelDto modelDto){
        String modelId = modelDto.getModelId();
        ModelDto byId = modelService.findByModelId(modelId);
        if(byId != null){
            return ResultDtoFactory.toNack("模型编号已存在");
        }else {
            modelService.saveModel(modelDto);
            return ResultDtoFactory.toAck();
        }

    }

    @GetMapping("/getModels")
    public ResultDto getModels(ModelSearchDto modelSearchDto){

        return ResultDtoFactory.toAckData(modelService.findModel(modelSearchDto));
    }

    /**
     * @author:yinjie
     * @date:2019/7/2
     * @time:11:10
     * @description:查询交易监测类型的模型名称
     */
    @GetMapping("/findTypeNameAsRisk")
    public ResultDto findTypeNameAsRisk(ModelSearchDto modelSearchDto){
        return ResultDtoFactory.toAckData(modelService.findTypeNameAsRisk(modelSearchDto));
    }

    /**
     * @author:yinjie
     * @date:2019/7/2
     * @time:11:11
     * @description:查询开户变更类型的模型名称
     */
    @GetMapping("/findTypeNameAsChange")
    public ResultDto findTypeNameAsChange(ModelSearchDto modelSearchDto){
        return ResultDtoFactory.toAckData(modelService.findTypeNameAsChange(modelSearchDto));
    }

    /**
     * @author:yinjie
     * @date:2020/9/9
     * @time:11:11
     * @description:查询对账类型的模型名称
     */
    @GetMapping("/findTypeNameAsDz")
    public ResultDto findTypeNameAsDz(ModelSearchDto modelSearchDto){
        return ResultDtoFactory.toAckData(modelService.findTypeNameAsDz(modelSearchDto));
    }

    @GetMapping("/update/{id}")
    public ResultDto findById(@PathVariable("id") Long id){
        return ResultDtoFactory.toAckData(modelService.findById(id));
    }



    @PutMapping("/{id}")
    public ResultDto updateModel(@PathVariable("id") Long id, ModelDto modelDto){
        modelDto.setId(id);
        modelService.saveModel(modelDto);
        return ResultDtoFactory.toAck();
    }

    @DeleteMapping("/{id}")
    public ResultDto deleteById(@PathVariable("id") Long id){
        modelService.deleteById(id);
        return  ResultDtoFactory.toAck();
    }

    @GetMapping("getModelByModelId/{modelId}")
    public ResultDto getModelByModelId(@PathVariable("modelId") String modelId){
        ModelDto byModelId = modelService.findByModelId(modelId);
       return ResultDtoFactory.toAckData(byModelId);
    }

    /**
     * @Description 模型初始化
     * @author yangwz
     * @date 2019-11-08 10:10
     * @params * @param null
     * @return
    */
    @GetMapping("modelInit")
    public ResultDto modelInit(){
        ResultDto resultDto =new ResultDto();
        boolean b = modelService.modelInit();
        if(b){
            resultDto.setMessage("模型初始化成功!");
        }else{
            resultDto.setMessage("模型初始化失败!");
        }
        return resultDto;
    }
}
