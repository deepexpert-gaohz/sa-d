package com.ideatech.ams.controller.risk;

import com.ideatech.ams.risk.model.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: yinjie
 * @Date: 2019/4/28 18:27
 */
@RestController
@RequestMapping("/modelCount")
public class ModelCountController {


    @Autowired
    private ModelService modelService;


}
