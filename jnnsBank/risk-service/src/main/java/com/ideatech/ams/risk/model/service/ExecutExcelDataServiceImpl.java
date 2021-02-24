package com.ideatech.ams.risk.model.service;

import com.ideatech.ams.risk.model.dto.*;

import com.ideatech.ams.risk.model.poi.ExportExcel;
import com.ideatech.ams.risk.riskdata.dto.RiskDetailsSearchDto;
import com.ideatech.ams.risk.riskdata.entity.RiskTradeInfo;

import java.util.*;

public class ExecutExcelDataServiceImpl implements Runnable {
    private RiskDataServiceToExp riskDataService;
    private ModelFieldService modelFieldService;
    private ModelCountFileService modelCountFileService;
    private ModelsExtendDto modelsExtendDto;
    private String tempFilePath = "";
    private String fileName = "";
    private String exportType;
    private String code;
    private String fullId;
    private ModelService modelService;

    public ExecutExcelDataServiceImpl(RiskDataServiceToExp riskDataService, ModelFieldService modelFieldService, ModelCountFileService modelCountFileService, ModelsExtendDto modelsExtendDto, String tempFilePath, String fileName, String exportType, String code, String fullId, ModelService modelService) {
        this.riskDataService = riskDataService;
        this.modelFieldService = modelFieldService;
        this.modelCountFileService = modelCountFileService;
        this.modelsExtendDto = modelsExtendDto;
        this.tempFilePath = tempFilePath;
        this.fileName = fileName;
        this.exportType = exportType;
        this.code = code;
        this.fullId = fullId;
        this.modelService = modelService;
    }

    public boolean getExcelData() {
        boolean flag = true;
        System.out.println("into **********");
        try {

            ModelsExtendDto m = new ModelsExtendDto();
            m.setTableName(modelsExtendDto.getModelId());
            //通过tableName获取模型集合
            //获得模型
            List<ModelsExtendDto> findList = modelCountFileService.findModelExtendList(m, code);
            if (findList != null && findList.size() > 0) {
                m.setName(findList.get(0).getName());
            }
            ModelDto modelDto = modelService.findByModelId(modelsExtendDto.getModelId(), code);
            List<Object[]> list1 = new ArrayList<>();

            List<String> obj1 = null;
            if ("1002".equals(modelDto.getTypeId())) {
                List<Object> list = this.getRiskField();
                if (list.size() != 0) {
                    obj1 = new ArrayList<>();
                    //组装查询条件
                    RiskDetailsSearchDto riskDetailsSearchDto = new RiskDetailsSearchDto();
                    riskDetailsSearchDto.setOfield((String) list.get(1));
                    riskDetailsSearchDto.setOrgId(modelsExtendDto.getOrgId());
                    riskDetailsSearchDto.setMinDate(modelsExtendDto.getMinDate());
                    riskDetailsSearchDto.setMaxDate(modelsExtendDto.getMaxDate());
                    riskDetailsSearchDto.setTableName(modelsExtendDto.getTableName());
                    riskDetailsSearchDto.setRiskPoint(modelsExtendDto.getRiskPoint());
                    riskDetailsSearchDto.setModelId(modelsExtendDto.getModelId());
                    //获取导出excel的数据
                    //List<Object[]> list1 = riskDataService.findRiskListExpDetails(riskDetailsSearchDto).getList();
                    riskDetailsSearchDto.setFullId(fullId);
                    RiskDetailsSearchDto riskdate = riskDataService.findModelByRiskid(riskDetailsSearchDto, code);
                    list1 = riskdate.getList();
                    obj1 = (List<String>) list.get(0);
                }
            } else {
                obj1 = new ArrayList<>();
                List<Object> list = this.getRiskField();
                if (list.size() != 0) {
                    RiskDetailsSearchDto riskDetailsSearchDto = new RiskDetailsSearchDto();
                    riskDetailsSearchDto.setOfield((String) list.get(1));
                    riskDetailsSearchDto.setOrgId(modelsExtendDto.getOrgId());
                    riskDetailsSearchDto.setMinDate(modelsExtendDto.getMinDate());
                    riskDetailsSearchDto.setMaxDate(modelsExtendDto.getMaxDate());
                    riskDetailsSearchDto.setTableName(modelsExtendDto.getModelId());
                    riskDetailsSearchDto.setModelId(modelsExtendDto.getModelId());
                    riskDetailsSearchDto.setFullId(fullId);
                    RiskDetailsSearchDto riskdate = riskDataService.findModelByRiskid(riskDetailsSearchDto, code);
                    list1 = riskdate.getList();
                    obj1 = (List<String>) list.get(0);
                }
            }
            ExportExcel exportExcel2 = new ExportExcel(m.getName(), obj1);
            exportExcel2.getExcel(list1, tempFilePath, fileName);// 将数据生成Excel
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }

        return flag;

    }

    public List<Object> getRiskField() {
        List<Object> lists = new ArrayList<>();
        ModelFieldDto modelFieldDto = new ModelFieldDto();
        modelFieldDto.setModelId(modelsExtendDto.getModelId().toUpperCase());
        //获得查询字段
        List list = modelFieldService.findRiskDataFieldZH(modelFieldDto);
        Object obj1 = (List<String>) list.get(0);
        lists.add(obj1);
        Object obj = list.get(1);
        String[] split = obj.toString().split(",");
        String fields = "";
        List<String> eNameList = Arrays.asList(split);
        eNameList = new ArrayList<String>(eNameList);
        //拼接查询字段
        for (String s : eNameList) {
            fields += ',' + "T." + s;
        }
        fields = fields.substring(1);
        lists.add(fields);
        // 查询数
        if (list.size() == 0) {
            return lists;
        }
        return lists;
    }

    @Override
    public void run() {
        ModelsExtendDto m = new ModelsExtendDto();
        m.setTableName(modelsExtendDto.getModelId());
        m.setOrgId(modelsExtendDto.getOrgId());
        m.setMinDate(modelsExtendDto.getMinDate());
        m.setMaxDate(modelsExtendDto.getMaxDate());
        this.getExcelData();
    }
}
