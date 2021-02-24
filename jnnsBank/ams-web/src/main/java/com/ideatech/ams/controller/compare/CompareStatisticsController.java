package com.ideatech.ams.controller.compare;

import com.ideatech.ams.compare.service.CompareStatisticsService;
import com.ideatech.common.dto.TreeTable;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/compareStatistics")
public class CompareStatisticsController {

    @Autowired
    private CompareStatisticsService compareStatisticsService;

    @GetMapping("/list")
    public TreeTable menuList(Long pid, Long taskId){
        Long organId = null;
        if (pid == null) {
            organId = SecurityUtils.getCurrentUser().getOrgId();
        }

        return compareStatisticsService.query(pid, organId, taskId);
    }

}
