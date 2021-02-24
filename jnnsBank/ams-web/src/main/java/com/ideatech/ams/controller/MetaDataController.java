package com.ideatech.ams.controller;

import com.ideatech.ams.system.meta.dto.MetaAttrDto;
import com.ideatech.ams.system.meta.dto.MetaDocDto;
import com.ideatech.ams.system.meta.service.MetaDataService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liangding
 * @create 2018-06-07 下午7:26
 **/
@RestController
@RequestMapping("/meta/doc")
public class MetaDataController {

    @Autowired
    private MetaDataService metaDataService;

    @GetMapping("/")
    public ResultDto<List<MetaDocDto>> listDocTypes() {
        List<MetaDocDto> docs = metaDataService.listDocs();
        return ResultDtoFactory.toAckData(docs);
    }

    @PutMapping("/{id}")
    public ResultDto update(@PathVariable("id") Long id, MetaDocDto metaDocDto) {
        metaDocDto.setId(id);
        metaDataService.save(metaDocDto);
        return ResultDtoFactory.toAck();
    }

    @GetMapping("/{id}")
    public ResultDto getDocById(@PathVariable("id") Long id) {
        MetaDocDto metaDocById = metaDataService.findMetaDocById(id);
        return ResultDtoFactory.toAckData(metaDocById);
    }

    @PostMapping("/")
    public ResultDto create(MetaDocDto metaDocDto) {
        metaDataService.save(metaDocDto);
        return ResultDtoFactory.toAck();
    }

    @DeleteMapping("/{id}")
    public ResultDto deleteDoc(@PathVariable("id") Long id) {
        metaDataService.deleteDoc(id);
        return ResultDtoFactory.toAck();
    }


    @GetMapping("/{id}/attr/")
    public ResultDto<List<MetaAttrDto>> listAttrByDocId(@PathVariable("id") Long id) {
        List<MetaAttrDto> metaAttrDtos = metaDataService.listAttrByDocId(id);
        return ResultDtoFactory.toAckData(metaAttrDtos);
    }

    @PutMapping("/{docId}/attr/{attrId}")
    public ResultDto saveOption(@PathVariable("docId") Long docId, @PathVariable("attrId") Long attrId, MetaAttrDto metaAttrDto) {
        metaAttrDto.setId(attrId);
        metaAttrDto.setDocId(docId);
        metaDataService.saveAttr(metaAttrDto);
        return ResultDtoFactory.toAck();
    }

    @PostMapping("/{docId}/attr/")
    public ResultDto createOption(@PathVariable("docId") Long docId, MetaAttrDto metaAttrDto) {
        metaAttrDto.setDocId(docId);
        metaDataService.saveAttr(metaAttrDto);
        return ResultDtoFactory.toAck();
    }


    @DeleteMapping("/{docId}/attr/{attrId}")
    public ResultDto deleteOption(@PathVariable("docId") Long docId, @PathVariable("attrId") Long attrId) {
        metaDataService.deleteAttr(attrId);
        return ResultDtoFactory.toAck();
    }

    @GetMapping("/{docId}/attr/{attrId}")
    public ResultDto getOptionById(@PathVariable("docId") Long docId, @PathVariable("attrId") Long attrId) {
        MetaAttrDto metaAttrDto = metaDataService.findAttrById(attrId);
        return ResultDtoFactory.toAckData(metaAttrDto);
    }

}
