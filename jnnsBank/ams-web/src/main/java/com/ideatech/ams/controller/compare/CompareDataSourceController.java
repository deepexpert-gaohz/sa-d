package com.ideatech.ams.controller.compare;

import com.ideatech.ams.compare.dto.CompareRuleDataSourceDto;
import com.ideatech.ams.compare.dto.DataSourceDto;
import com.ideatech.ams.compare.service.CompareRuleDataSourceService;
import com.ideatech.ams.compare.service.DataSourceService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/compareDataSource")
@Slf4j
public class CompareDataSourceController {
    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    private CompareRuleDataSourceService compareRuleDataSourceService;

    @GetMapping("/list")
    public TableResultResponse<DataSourceDto> list(DataSourceDto dto, @PageableDefault(sort = {"lastUpdateDate"}, direction = Sort.Direction.DESC) Pageable pageable) throws Exception {
        TableResultResponse<DataSourceDto> res = dataSourceService.query(dto, pageable);
        return res;
    }
    @PostMapping("/")
    public ResultDto save(DataSourceDto dto){
        dto.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
        dataSourceService.DataSourceSave(dto);
        return ResultDtoFactory.toAck();
    }
    @GetMapping("/{id}")
    public ResultDto<DataSourceDto> findById(@PathVariable(name = "id") Long id){
        DataSourceDto dto = dataSourceService.findById(id);
        return ResultDtoFactory.toAckData(dto);
    }
    @PutMapping("/{id}")
    public ResultDto update(DataSourceDto dto, @PathVariable("id") Long id) {
        List<CompareRuleDataSourceDto> list = compareRuleDataSourceService.findByDataSoureIdAndActive(id,true);
        if (CollectionUtils.isNotEmpty(list)) {
            return ResultDtoFactory.toNack("该数据源有比对规则正在使用，请先删除比对规则！");
        }
        dto.setId(id);
        dto.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
        dataSourceService.updateSource(dto);
        return ResultDtoFactory.toAck();
    }
    @DeleteMapping("/{id}")
    public ResultDto del(@PathVariable("id") Long id){
        dataSourceService.deleteDataSource(id);
        return ResultDtoFactory.toAck();
    }
}
