package com.ideatech.ams.controller.compare;

import com.ideatech.ams.compare.dto.CompareFieldDto;
import com.ideatech.ams.compare.service.CompareFieldService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.msg.TableResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/compareField")
@Slf4j
public class CompareFieldController {
    @Autowired
    private CompareFieldService compareFieldService;
    @GetMapping("/list")
    public TableResultResponse list(CompareFieldDto dto,@PageableDefault(sort = {"lastUpdateDate"}, direction = Sort.Direction.DESC) Pageable pageable){
        return compareFieldService.query(dto,pageable);
    }
    @GetMapping("/")
    public ResultDto<List<CompareFieldDto>> getAll(){
        List<CompareFieldDto> list = compareFieldService.getAll();
        return ResultDtoFactory.toAckData(list);
    }
    @GetMapping("/{id}")
    public ResultDto<CompareFieldDto> findById(@PathVariable(name = "id") Long id){
        CompareFieldDto dto = compareFieldService.findById(id);
        return ResultDtoFactory.toAckData(dto);
    }
    @DeleteMapping("/{id}")
    public ResultDto del(@PathVariable("id") Long id){
        compareFieldService.deleteById(id);
        return ResultDtoFactory.toAck();
    }

}
