package com.ideatech.ams.risk.model.service;


import com.ideatech.ams.risk.model.dto.ModelDto;
import com.ideatech.ams.risk.model.dto.ModelSearchDto;

import java.util.List;


public interface ModelService {

    void saveModel(ModelDto modelDto);


    ModelDto findById(Long id);

    void enable(Long id);

    void disabld(Long id);

    void deleteById(Long id);

    ModelDto findByName(String name);

    ModelDto findByModelId(String modelId);

    ModelDto findByModelId(String modelId, String code);

    ModelDto findByModelIdAndCode(String modelId);

    List<ModelDto> findByStatus();

    ModelSearchDto findModel(ModelSearchDto modelSearchDto);

    ModelSearchDto findTypeNameAsRisk(ModelSearchDto modelSearchDto);

    ModelSearchDto findTypeNameAsCode(ModelSearchDto modelSearchDto, String code);

    ModelSearchDto findTypeNameAsChange(ModelSearchDto modelSearchDto);

    ModelSearchDto findTypeNameAsDz(ModelSearchDto modelSearchDto);

    /**
     * 获取所有未停用的模型信息
     * @return
     */
    List<ModelDto> findAllModel();
    /*获取所有模型。包括停用和未停用的*/
    List<ModelDto> findAllMstModel();


    //List<ModelDto> findAllbyDeleted(long delete);

    /*List<ModelDto> findAllbyDeletedAndModelId(int i, String risk_2001);*/

    boolean modelInit();

    ModelDto findByModelIdForSechdule(String modelId, String code);

    /**
     * @Description 根據模型类型查询模型
     * @author yangwz
     * @date 2020/8/31 16:00
    */
    List<ModelDto> findModelNum(String typeId);
}
