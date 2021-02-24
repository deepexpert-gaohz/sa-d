package com.ideatech.ams.annual.service;

import com.ideatech.ams.annual.dao.CollectConfigDao;
import com.ideatech.ams.annual.dto.CollectConfigDto;
import com.ideatech.ams.annual.entity.CollectConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @Description 采集配置服务层
 * @Author wanghongjie
 * @Date 2018/8/10
 **/
@Service
@Transactional
@Slf4j
public class CollectConfigServiceImpl implements CollectConfigService {
    @Autowired
    private CollectConfigDao collectConfigDao;

    @Autowired
    private AnnualTaskService annualTaskService;

    @Override
    public CollectConfigDto findByAnnualTaskId(Long annualTaskId) {
        List<CollectConfig> list = collectConfigDao.findByAnnualTaskIdOrderByCreatedDateDesc(annualTaskId);
        CollectConfigDto collectConfigDto = null;
        if (list != null && list.size() > 0) {
            collectConfigDto = new CollectConfigDto();
            BeanUtils.copyProperties(list.get(0), collectConfigDto);
        }
        return collectConfigDto;
    }

    @Override
    public void saveCollectConfig(CollectConfigDto collectConfigDto) {
        Long taskId = annualTaskService.initAnnualTask();
        collectConfigDto.setAnnualTaskId(taskId);

        //前端时间格式转换 TODO 方法待完善
        if (collectConfigDto.getPbcStartTime()!=null){
            //例如5:30-->05:30
            if (collectConfigDto.getPbcStartTime().length()==4){
                collectConfigDto.setPbcStartTime("0"+collectConfigDto.getPbcStartTime());
            }
            log.info("人行采集开始时间："+collectConfigDto.getPbcStartTime());
        }
        if (collectConfigDto.getPbcEndTime()!=null){
            if (collectConfigDto.getPbcEndTime().length()==4){
                collectConfigDto.setPbcEndTime("0"+collectConfigDto.getPbcEndTime());
            }
            log.info("人行采集结束时间："+collectConfigDto.getPbcEndTime());
        }


        List<CollectConfig> list = collectConfigDao.findByAnnualTaskIdOrderByCreatedDateDesc(taskId);
        CollectConfig collectConfig = new CollectConfig();
        if (list != null && list.size() > 0) {
            collectConfig = list.get(0);
        }
        BeanUtils.copyProperties(collectConfigDto, collectConfig);
        collectConfigDao.save(collectConfig);
        annualTaskService.createSaicTimedTask(taskId);
    }

}
