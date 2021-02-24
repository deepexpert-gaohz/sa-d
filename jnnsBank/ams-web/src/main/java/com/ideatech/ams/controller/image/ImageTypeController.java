package com.ideatech.ams.controller.image;


import com.ideatech.ams.image.dto.ImageTypeInfo;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.ams.system.dict.dto.OptionDto;
import com.ideatech.ams.system.dict.service.DictionaryService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.msg.TableResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import com.ideatech.ams.image.service.ImageTypeService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/imageType")
public class ImageTypeController {
    @Autowired
    private ImageTypeService imageTypeService;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private ConfigService configService;
    /**
     * 查询所有配置
     * @param info
     *
     * @return
     */
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public TableResultResponse query(ImageTypeInfo info){
        Pageable pageable = new PageRequest(info.getOffset()-1, info.getLimit());
        TableResultResponse response = imageTypeService.query(info,pageable);
        return  response;
    };

    /**
     * 删除配置
     * @param id
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResultDto delete(@PathVariable("id")Long id){
        imageTypeService.deleteConfig(id);
        return ResultDtoFactory.toAck();
    }

    /**
     * 保存更新
     * @param info
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResultDto saveAndUpdate(ImageTypeInfo info){
        imageTypeService.save(info);
        return ResultDtoFactory.toAck();
    }
    @RequestMapping(value = "/getImageOption", method = RequestMethod.GET)
    public ResultDto getImageOption(String name){
        List<OptionDto> all = dictionaryService.listOptionByDictName(name);
        return ResultDtoFactory.toAckData(all);
    }
    @RequestMapping(value = "/getDepositorTypeCode", method = RequestMethod.GET)
    public ResultDto getDepositorTypeCode() {
        List<OptionDto> all1 = dictionaryService.findOptionsByDictionaryName("存款人类别(基本户)");
        List<OptionDto> all2 = dictionaryService.findOptionsByDictionaryName("存款人类别(特殊户)");
        all1.addAll(all2);
        return ResultDtoFactory.toAckData(all1);
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResultDto<ImageTypeInfo> getById(@PathVariable("id") Long id){
        ImageTypeInfo info = imageTypeService.getById(id);
        return ResultDtoFactory.toAckData(info);
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResultDto update(ImageTypeInfo info,@PathVariable("id") Long id){
        info.setId(id);
        imageTypeService.save(info);
        return ResultDtoFactory.toAck();
    }
    @RequestMapping(value = "/getImageType", method = RequestMethod.GET)
    public ResultDto getImageType(ImageTypeInfo info){
        List<ImageTypeInfo> all = imageTypeService.getImageType(info);
        return ResultDtoFactory.toAckData(all);
    }
    @RequestMapping(value = "/getConfig", method = RequestMethod.GET)
    public ResultDto getConfig(){
        List<ConfigDto> list = configService.findByKey("imageCollect");
       return ResultDtoFactory.toAckData(configService.list());
    }
    @RequestMapping(value = "/getThirdUrl", method = RequestMethod.GET)
    public ResultDto getThirdUrl(){
        List<ConfigDto> list = configService.findByKey("thirdUrl");
        return ResultDtoFactory.toAckData(configService.list());
    }
}
