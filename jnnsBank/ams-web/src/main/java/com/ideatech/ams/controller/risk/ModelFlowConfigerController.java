package com.ideatech.ams.controller.risk;

import com.ideatech.ams.risk.model.dto.ModeAndKindlDto;
import com.ideatech.ams.risk.model.dto.ModelDto;
import com.ideatech.ams.risk.model.dto.ModelSearchDto;
import com.ideatech.ams.risk.model.service.ModelService;
import com.ideatech.ams.risk.modelFlow.dto.ModelFlowConfigerDto;
import com.ideatech.ams.risk.modelFlow.dto.ModelFlowConfigerSearchDto;
import com.ideatech.ams.risk.modelFlow.service.ModelFlowConfigerService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("ModelFlow")
public class ModelFlowConfigerController {

    @Autowired
    ModelFlowConfigerService modelFlowConfigerService;
    @Autowired
    ModelService modelService;
    /**
     * 分页模糊查询
     * @param modelFlowConfigerSearchDto
     * @return
     */
    @GetMapping("search")
    public ResultDto getAll(ModelFlowConfigerSearchDto modelFlowConfigerSearchDto){
        return ResultDtoFactory.toAckData(modelFlowConfigerService.searchData(modelFlowConfigerSearchDto));
    }

    /**
     * 保存模型流程配置
     * @param modelFlowConfigerDto
     * @return
     */
    @PostMapping("save")
    public ResultDto save(ModelFlowConfigerDto modelFlowConfigerDto){
            modelFlowConfigerService.saveModelFlowConfiger(modelFlowConfigerDto);
            return ResultDtoFactory.toAck();
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResultDto del(@PathVariable("id") Long id){
        modelFlowConfigerService.delModelFlowConfiger(id);
        return ResultDtoFactory.toAck();
    }

    /**
     * 修改
     * @param id
     * @param modelFlowConfigerDto
     * @return
     */
    @PutMapping("/{id}")
    public ResultDto update(@PathVariable("id") Long id, ModelFlowConfigerDto modelFlowConfigerDto){
        modelFlowConfigerDto.setId(id);
        modelFlowConfigerService.saveModelFlowConfiger(modelFlowConfigerDto);
        return ResultDtoFactory.toAck("更新成功");
    }

    /**
     * 通过id 查询
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResultDto getById(@PathVariable("id") Long id){
        return ResultDtoFactory.toAckData(modelFlowConfigerService.findById(id));
    }


    @GetMapping("/getModels")
    public ResultDto getModels(ModelSearchDto modelSearchDto){
            return ResultDtoFactory.toAckData(modelFlowConfigerService.getModels(modelSearchDto));
    }
    @GetMapping("/getEditModels/{id}")
    public ResultDto getEditModels(@PathVariable("id") Long id, ModelSearchDto modelSearchDto){
        ModelSearchDto models = modelFlowConfigerService.getModels(modelSearchDto);
        ModelFlowConfigerDto byId = modelFlowConfigerService.findById(id);
        ModelDto modelDto = modelService.findById(Long.parseLong(byId.getModelId()));
        List<ModeAndKindlDto> list = models.getList();
        ModeAndKindlDto m = new ModeAndKindlDto();
        m.setId(modelDto.getId());
        m.setName(modelDto.getName());
        list.add(m);
        modelSearchDto.setList(list);
        return ResultDtoFactory.toAckData(modelSearchDto);
    }


}
