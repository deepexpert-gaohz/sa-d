package com.ideatech.ams.controller;

import com.ideatech.ams.system.dict.dto.DictionaryDto;
import com.ideatech.ams.system.dict.dto.OptionDto;
import com.ideatech.ams.system.dict.service.DictionaryService;
import com.ideatech.ams.system.trace.aop.OperateLog;
import com.ideatech.ams.system.trace.enums.OperateModule;
import com.ideatech.ams.system.trace.enums.OperateType;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liangding
 * @create 2018-05-24 下午7:33
 **/
@RestController
@RequestMapping("/dictionary")
public class DictionaryController {

    @Autowired
    private DictionaryService dictionaryService;

    @GetMapping("/")
    public ResultDto<List<DictionaryDto>> list() {
        List<DictionaryDto> dictionaryDtos = dictionaryService.listWithoutCascade();
        return ResultDtoFactory.toAckData(dictionaryDtos);
    }

    @OperateLog(operateModule = OperateModule.DICTIONARY,operateType = OperateType.UPDATE)
    @PutMapping("/{id}")
    public ResultDto update(@PathVariable("id") Long id, DictionaryDto dictionaryDto) {
        dictionaryDto.setId(id);
        dictionaryService.save(dictionaryDto);
        return ResultDtoFactory.toAck();
    }

    @GetMapping("/{id}")
    public ResultDto getDictionaryById(@PathVariable("id") Long id) {
        DictionaryDto dictionaryDto = dictionaryService.findDictionaryById(id);
        return ResultDtoFactory.toAckData(dictionaryDto);
    }

    @OperateLog(operateModule = OperateModule.DICTIONARY,operateType = OperateType.INSERT)
    @PostMapping("/")
    public ResultDto create(DictionaryDto dictionaryDto) {
        dictionaryService.save(dictionaryDto);
        return ResultDtoFactory.toAck();
    }

    @GetMapping("/{id}/option")
    public ResultDto<List<OptionDto>> findOptionsById(@PathVariable("id") Long id) {
        List<OptionDto> optionDtos = dictionaryService.listOptionByDictId(id);
        return ResultDtoFactory.toAckData(optionDtos);
    }

    @GetMapping("/findOptionsByDictionaryName")
    public ResultDto<List<OptionDto>> findOptionsByDictionaryName(String name) {
        List<OptionDto> optionDtos = dictionaryService.findOptionsByDictionaryName(name);
        return ResultDtoFactory.toAckData(optionDtos);
    }

    @GetMapping("/findOptionsByDictionaryNameStartWith")
    public ResultDto<List<OptionDto>> findOptionsByDictionaryNameStartWith(String name) {
        List<OptionDto> optionDtos = dictionaryService.findOptionsByDictionaryNameStartWith(name);
        return ResultDtoFactory.toAckData(optionDtos);
    }

    @OperateLog(operateModule = OperateModule.DICTIONARY,operateType = OperateType.UPDATE,operateContent = "字典项修改",cover = true)
    @PutMapping("/{dictionaryId}/option/{id}")
    public ResultDto saveOption(@PathVariable("dictionaryId") Long dictionaryId, @PathVariable("id") Long id, OptionDto optionDto) {
        optionDto.setId(id);
        optionDto.setDictionaryId(dictionaryId);
        dictionaryService.saveOption(optionDto);
        return ResultDtoFactory.toAck();
    }

    @OperateLog(operateModule = OperateModule.DICTIONARY,operateType = OperateType.INSERT,operateContent = "字典项新建")
    @PostMapping("/{dictionaryId}/option/")
    public ResultDto createOption(@PathVariable("dictionaryId") Long dictionaryId, OptionDto optionDto) {
        optionDto.setDictionaryId(dictionaryId);
        dictionaryService.saveOption(optionDto);
        return ResultDtoFactory.toAck();
    }

    @OperateLog(operateModule = OperateModule.DICTIONARY,operateType = OperateType.DELETE)
    @DeleteMapping("/{id}")
    public ResultDto deleteDictionary(@PathVariable("id") Long id) {
        dictionaryService.deleteDictionary(id);
        return ResultDtoFactory.toAck();
    }

    @OperateLog(operateModule = OperateModule.DICTIONARY,operateType = OperateType.DELETE,operateContent = "字典项删除",cover = true)
    @DeleteMapping("/{dictionaryId}/option/{id}")
    public ResultDto deleteOption(@PathVariable("dictionaryId") Long dictionaryId, @PathVariable("id") Long id) {
        dictionaryService.deleteOption(id);
        return ResultDtoFactory.toAck();
    }

    @GetMapping("/{dictionaryId}/option/{optionId}")
    public ResultDto getOptionById(@PathVariable("dictionaryId") Long dictionaryId, @PathVariable("optionId") Long optionId) {
        OptionDto optionDto = dictionaryService.findOptionById(optionId);
        return ResultDtoFactory.toAckData(optionDto);
    }
}
