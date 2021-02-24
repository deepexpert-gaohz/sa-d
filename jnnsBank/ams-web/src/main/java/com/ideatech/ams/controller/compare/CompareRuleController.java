package com.ideatech.ams.controller.compare;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.compare.dto.*;
import com.ideatech.ams.compare.enums.CompareState;
import com.ideatech.ams.compare.service.*;
import com.ideatech.common.constant.ResultCode;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jzh
 * @date 2019/1/16.
 */

@Slf4j
@RestController
@RequestMapping("/compareRule")
public class CompareRuleController {

    @Autowired
    private CompareRuleService compareRuleService;

    @Autowired
    private CompareRuleDataSourceService compareRuleDataSourceService;

    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    private CompareRuleFieldsService compareRuleFieldsService;

    @Autowired
    private CompareFieldService compareFieldService;

    @Autowired
    private CompareDefineService compareDefineService;

    @Autowired
    private CompareTaskService compareTaskService;

    /**
     * 新建保存CompareRule
     * @return
     */
    @RequestMapping(value = "/save" ,method = RequestMethod.POST)
    public ResultDto saveCompareRule(CompareRuleDto compareRuleDto, Long [] dataIds, Long []fields,String[] parentIds, HttpServletRequest httpServletRequest){
        compareRuleService.saveCompareRuleDetail(compareRuleDto,dataIds,fields,parentIds,httpServletRequest);
        return ResultDtoFactory.toAck();
    }

    /**
     * CompareRule分页查询
     * @param compareRuleSearchDto
     * @return
     */
    @RequestMapping(value = "/list" , method = RequestMethod.GET)
    public ResultDto pageCompareRule(CompareRuleSearchDto compareRuleSearchDto,@PageableDefault(sort = {"lastUpdateDate"}, direction = Sort.Direction.DESC) Pageable pageable){
        return ResultDtoFactory.toAckData(compareRuleService.search(compareRuleSearchDto,pageable));
    }


    /**
     * 更新保存CompareRule
     * @return
     */
    @RequestMapping(value = "/save" ,method = RequestMethod.PUT)
    public ResultDto updateCompareRule(CompareRuleDto compareRuleDto, Long [] dataIds, Long []fields,String[] parentIds, HttpServletRequest httpServletRequest){
        if (compareRuleDto.getId()==null){
            return ResultDtoFactory.toNack("id为空，获取比对规则失败。");
        }
        ResultDto resultDto = this.checkRuleIsEdit(compareRuleDto.getId());
        if (!resultDto.getCode().equals(ResultCode.ACK)) {
            return resultDto;
        }
        compareRuleService.saveCompareRuleDetail(compareRuleDto,dataIds,fields,parentIds,httpServletRequest);
        return ResultDtoFactory.toAck();
    }

    /**
     * 判断该规则是否可以修改
     */
    @RequestMapping(value = "/checkRuleIsEdit", method = RequestMethod.GET)
    public ResultDto checkRuleIsEdit(Long id) {
        boolean b = true;
        List<String> taskNameList = new ArrayList<>();
        //先查询是否有任务使用了此规则
        List<CompareTaskDto> compareTaskList = compareTaskService.findByCompareRuleId(id);
        //此规则只是关联了任务，但未开始采集时，允许修改
        for (CompareTaskDto ctd : compareTaskList) {
            if (!ctd.getState().equals(CompareState.INIT)) {//已经被使用，不允许修改
                b = false;
                taskNameList.add("“" + ctd.getName() + "”");
            }
        }
        if (b) {
            return ResultDtoFactory.toAck();
        } else {
            return ResultDtoFactory.toNack(StringUtils.join(taskNameList.toArray(), ","));
        }
    }

    /**
     * 根据id获取CompareRule
     * @param id
     * @return
     */
    @RequestMapping(value = "/getOne" ,method = RequestMethod.GET)
    public ResultDto getCompareRule(Long id){
        if (id==null){
            return ResultDtoFactory.toNack("id为空，获取比对规则失败。");
        }
        CompareRuleDto compareRuleDto = compareRuleService.findById(id);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name",compareRuleDto.getName());
        jsonObject.put("id",compareRuleDto.getId());
        jsonObject.put("bussBlackList",compareRuleDto.getBussBlackList());
        jsonObject.put("personBlackList",compareRuleDto.getPersonBlackList());

        //选中的数据源
        JSONArray jsonArray = new JSONArray();
        List<CompareRuleDataSourceDto> compareRuleDataSourceDtoList = compareRuleDataSourceService.findByCompareRuleId(id);
        for (CompareRuleDataSourceDto compareRuleDataSourceDto :compareRuleDataSourceDtoList){
            if (compareRuleDataSourceDto.getActive()){
                JSONObject jsonObject1 = new JSONObject();
                DataSourceDto dataSourceDto = dataSourceService.findById(compareRuleDataSourceDto.getDataSourceId());
                if (dataSourceDto!=null){
                    jsonObject1.put("name",dataSourceDto.getName()!=null?dataSourceDto.getName():"");
                    jsonObject1.put("collectType",dataSourceDto.getCollectType()!=null?dataSourceDto.getCollectType():"");
                }
                jsonObject1.put("id",compareRuleDataSourceDto.getDataSourceId());
                jsonObject1.put("parentIds",compareRuleDataSourceDto.getParentDataSourceIds());
                jsonArray.add(jsonObject1);
            }
        }
        jsonObject.put("checkedDataList",jsonArray);

        //选中的比较字段 checkedFieldList数组
        JSONArray checkedFieldList = new JSONArray();
        List<CompareRuleFieldsDto> compareRuleFieldsDtoList = compareRuleFieldsService.findByCompareRuleId(id);
        for (CompareRuleFieldsDto compareRuleFieldsDto : compareRuleFieldsDtoList) {
            //checkedFieldList数组中的对象jsonObject1
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("id",compareRuleFieldsDto.getCompareFieldId());
            //String name = compareFieldService.findById(compareRuleFieldsDto.getId()).getName();
            jsonObject1.put("name","");
           jsonObject1.put("isEnabled",compareRuleFieldsDto.getActive());
            //放入（CompareDefine）dataList
            JSONArray dataList = new JSONArray();
            for (CompareRuleDataSourceDto compareRuleDataSourceDto :compareRuleDataSourceDtoList){
                if (compareRuleDataSourceDto.getActive()){
                    CompareDefineDto compareDefineDto = compareDefineService.findByCompareRuleIdAndDataSourceIdAndCompareFieldId(id,compareRuleDataSourceDto.getDataSourceId(),compareRuleFieldsDto.getCompareFieldId());
                    if (compareDefineDto!=null){
                        JSONObject jsonObject2 = new JSONObject();
                        jsonObject2.put("id",compareDefineDto.getDataSourceId());
                        jsonObject2.put("use",compareDefineDto.getActive());
                        jsonObject2.put("empty",compareDefineDto.getNullpass());
                        dataList.add(jsonObject2);
                    }
                }
            }
            jsonObject1.put("dataList",dataList);
            checkedFieldList.add(jsonObject1);
        }
        jsonObject.put("checkedFieldList",checkedFieldList);
        return ResultDtoFactory.toAckData(jsonObject);
    }

    /**
     * 根据ID删除CompareRule
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    public ResultDto deleteCompareRule(Long id){
        if (id==null){
            return ResultDtoFactory.toNack("id为空，获取比对规则失败。");
        }
        compareRuleService.deleteCompareRuleDetail(id);
        return ResultDtoFactory.toAck();
    }
    @GetMapping("/getAll")
    public ResultDto getAll(){
        List<CompareRuleDto> data = compareRuleService.getAll();
        return ResultDtoFactory.toAckData(data);
    }

    /**
     * 查找所有上级机构及本机构创建的规则
     */
    @GetMapping("/getByOrganUpWard")
    public ResultDto getByOrganUpWard(){
        List<CompareRuleDto> data = compareRuleService.getByOrganUpWard();
        return ResultDtoFactory.toAckData(data);
    }
}
