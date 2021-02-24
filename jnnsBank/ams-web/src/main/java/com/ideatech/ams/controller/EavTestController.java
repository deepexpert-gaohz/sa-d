package com.ideatech.ams.controller;

import com.google.common.collect.Lists;
import com.ideatech.ams.system.eav.service.EavService;
import com.ideatech.ams.vo.EavTestResult;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author liangding
 * @create 2018-07-23 下午6:42
 **/
@RestController
@RequestMapping("/eavtest")
public class EavTestController {

    @Autowired
    private EavService eavService;

    @GetMapping("/get")
    public ResultDto<EavTestResult> test(Long id) {
        EavTestResult eavTestResult = new EavTestResult();
        eavTestResult.setId(id);
        List<String> strings = Lists.asList("A", new String[]{"B", "C"});
        eavTestResult.setStringList(strings);
        Map<String, String> test = eavService.findByEntityIdAndDocCode(id, "TEST");
        eavTestResult.setExt(test);
        return ResultDtoFactory.toAckData(eavTestResult);
    }


    @PostMapping(value = "/post")
    public ResultDto test(EavTestResult eavTestResult) {
        eavService.save(eavTestResult.getId(), "TEST", eavTestResult.getExt());
        return ResultDtoFactory.toAck();
    }
}
