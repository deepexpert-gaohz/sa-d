package com.ideatech.ams.controller.risk;


import com.ideatech.ams.risk.model.dto.ModelFieldDto;
import com.ideatech.ams.risk.model.dto.RiskConfigerFieldDto;
import com.ideatech.ams.risk.model.service.ModelFieldService;
import com.ideatech.ams.risk.model.service.RiskConfigerFieldService;
import com.ideatech.ams.risk.util.RiskUtil;
import com.ideatech.ams.system.org.dao.OrganizationDao;
import com.ideatech.ams.system.org.entity.OrganizationPo;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.util.List;

/**
 * @Author: yinjie
 * @Date: 2019/4/25 20:53
 */


@RestController
@RequestMapping("/modelField")
public class ModelFieldController {

    @Autowired
    private ModelFieldService modelFieldService;
    @Autowired
    private RiskConfigerFieldService riskConfigerFieldService;
    @Autowired
    OrganizationDao organizationDao;

    //根据表名查找字段
    @GetMapping("queryModelField/{tableName}")
    public ResultDto searchModelField(@PathVariable("tableName")String tableName){
        return ResultDtoFactory.toAckData(modelFieldService.findAllByModelId(tableName));
    }

    //根据字段保存
    @GetMapping("saveModelField")
    public ResultDto saveModelField(ModelFieldDto modelFieldDto){
        modelFieldService.saveModelField(modelFieldDto);
        return ResultDtoFactory.toAck();
    }

    /**、
     * 获取所有字段
     * @return
     */
    @GetMapping("getFileds")
    public ResultDto getFileds(){
        List<RiskConfigerFieldDto> list = riskConfigerFieldService.findAll();
        return ResultDtoFactory.toAckData(list);
    }

    /**
     * 删除模型配置字段
     * @param id
     * @return
     */
    @GetMapping("deleteField/{id}")
    public ResultDto deleteField(@PathVariable("id")Long id){
        ModelFieldDto modelFieldDto = new ModelFieldDto();
        modelFieldDto.setId ( id );
        modelFieldService.delete(modelFieldDto);
        return ResultDtoFactory.toAck();
    }

    /**
     * 模型字段初始化
     * @param modelId
     * @return
     */
    @GetMapping("initModelFields/{modelId}")
    public ResultDto initModelFields(@PathVariable("modelId")String modelId){
        ModelFieldDto modelFieldDto = new ModelFieldDto();
        modelFieldDto.setModelId ( modelId );
        modelFieldService.initModelFields(modelId);
        return ResultDtoFactory.toAck();
    }
    /**
     *
     */
    @GetMapping("findByField/{field}")
    public ResultDto findByField(@PathVariable("field")String field){
        String code = RiskUtil.getOrganizationCode();
        RiskConfigerFieldDto riskConfigerFieldDto = riskConfigerFieldService.findByField(field,code);
        return ResultDtoFactory.toAckData(riskConfigerFieldDto);
    }
    /**
     * 保存风险模型字段
     */
    @GetMapping("saveConfigerField/{modelId}/{field}/{fieldName}")
    public ResultDto saveConfigerField(@PathVariable("modelId")String modelId, @PathVariable("field")String field, @PathVariable("fieldName")String fieldName){
        ModelFieldDto modelFieldDto = new ModelFieldDto();
        try{
        List<ModelFieldDto> modelFieldDtoList = modelFieldService.findAllByModelId ( modelId );
        if(modelFieldDtoList.size ()==0){
            modelFieldDto.setOrderFlag ( 0 );
        }else{
            int maxOrder = 0;
            for(int i=0 ;i < modelFieldDtoList.size (); i++){
                ModelFieldDto dto = modelFieldDtoList.get ( i );
                int orderFlag=0;
                if(i==0){
                    maxOrder = dto.getOrderFlag ();
                }
                if(maxOrder >dto.getOrderFlag ()){
                    maxOrder = maxOrder;
                }else{
                    maxOrder =dto.getOrderFlag ();
                }
            }
            modelFieldDto.setOrderFlag (maxOrder +1);
        }
        modelFieldDto.setModelId ( modelId );
        modelFieldDto.setFieldsEn ( field );
        String fieldNameVal = URLDecoder.decode(fieldName, "GBK");
        modelFieldDto.setFieldsZh ( fieldNameVal );

        modelFieldDto.setShowFlag ( 0);//0:显示 1：不显示
        modelFieldDto.setExportFlag ( 0 );//0：导出 1:不导出
        modelFieldService.saveModelField(modelFieldDto);
        }catch(Exception e){
            e.getStackTrace ();
        }
        return ResultDtoFactory.toAck();
    }
    /**
     * @Description 模型字段初始化
     * @author yangwz
     * @date 2019-11-08 10:10
     * @params * @param null
     * @return
     */
    @GetMapping("modelFieldInit")
    public ResultDto modelFieldInit(){
        ResultDto resultDto =new ResultDto();
        boolean b = modelFieldService.modelFieldInit();
        if(b){
            resultDto.setMessage("模型字段初始化成功!");
        }else{
            resultDto.setMessage("模型字段初始化失败!");
        }
        return resultDto;
    }
}
