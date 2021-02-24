package com.ideatech.ams.compare.initializer;

import com.ideatech.ams.compare.dao.*;
import com.ideatech.ams.compare.entity.*;
import com.ideatech.ams.compare.enums.TaskRate;
import com.ideatech.ams.compare.enums.TaskType;
import com.ideatech.ams.compare.service.CompareTaskService;
import com.ideatech.common.constant.DataInitializerConstant;
import com.ideatech.common.initializer.AbstractDataInitializer;
import com.ideatech.common.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * @author jzh
 * @date 2019/6/6.
 */
@Slf4j
@Component
public class AbnormalCheckInitializer extends AbstractDataInitializer {

    @Autowired
    private CompareTaskDao compareTaskDao;

    @Autowired
    private CompareCollectTaskDao compareCollectTaskDao;

    @Autowired
    private CompareRuleDao compareRuleDao;

    @Autowired
    private CompareRuleDataSourceDao compareRuleDataSourceDao;

    @Autowired
    private CompareRuleFieldsDao compareRuleFieldsDao;

    @Autowired
    private CompareDefineDao compareDefineDao;

    @Autowired
    private CompareTaskService compareTaskService;

    /**
     * 是否启用定时任务
     */
    @Value("${compare.saicCheck.use:false}")
    private boolean isUse;

    @Override
    protected void doInit() throws Exception {
        if (isUse){
            if (compareRuleDao.findOne(1001L)==null){
                createRule();
                log.info("初始化工商异常校验规则");
            }else {
                log.info("指定规则存在，无需初始化工商异常校验规则");
            }
        }else {
            log.info("不启用客户异动功能，不初始化工商异常校验规则");
        }
    }

    private void createRule() {

        //1、新建规则 CompareRule
        CompareRule compareRule = new CompareRule();
        compareRule.setId(1001L);
        compareRule.setCreateTime(DateUtils.getNowDateShort());
        compareRule.setCount(0);
        compareRule.setCreater("虚拟用户");
        compareRule.setBussBlackList(false);
        compareRule.setPersonBlackList(false);
        compareRule.setOrganFullId("1");
        compareRule.setName("工商校验规则");
        compareRuleDao.save(compareRule);

        //2、操作CompareRuleDataSource 比对规则数据源
        compareRuleDataSourceDao.deleteAllByCompareRuleId(compareRule.getId());

        //2.1 新建工商数据源规则
        Long [] dataIds = new Long[]{1006L,1007L};//数据源ID 见 CompareDataSourceInitializer
        CompareRuleDataSource compareRuleDataSource = new CompareRuleDataSource();
        compareRuleDataSource.setActive(true);
        compareRuleDataSource.setCompareRuleId(1001L);
        compareRuleDataSource.setDataSourceId(1006L);//工商数据
        compareRuleDataSource.setParentDataSourceIds("1007");//客户数据
        compareRuleDataSourceDao.save(compareRuleDataSource);

        //2.2 新建客户数据规则
        CompareRuleDataSource compareRuleDataSource2 = new CompareRuleDataSource();
        compareRuleDataSource2.setActive(true);
        compareRuleDataSource2.setCompareRuleId(1001L);
        compareRuleDataSource2.setDataSourceId(1007L);//客户数据
        compareRuleDataSourceDao.save(compareRuleDataSource2);

        //3、 操作CompareRuleFields 比对规则字段
        compareRuleFieldsDao.deleteAllByCompareRuleId(compareRule.getId());
        //比对规则字段ID
        Long [] fields = new Long[]{1002L,1003L,1005L,1006L,1008L,1009L,1030L,1031L};// 见 CompareFieldsInitializer
        for (Long field : fields) {
            CompareRuleFields compareRuleFields = new CompareRuleFields();
            compareRuleFields.setActive(true);
            compareRuleFields.setCompareFieldId(field);
            compareRuleFields.setCompareRuleId(compareRule.getId());
            compareRuleFieldsDao.save(compareRuleFields);
        }

        //4、CompareDefine 比对规则字段详情
        compareDefineDao.deleteAllByCompareRuleId(compareRule.getId());
        for (Long field : fields) {
            for (Long dataId : dataIds) {
                CompareDefine compareDefine = new CompareDefine();
                compareDefine.setCompareRuleId(compareRule.getId());
                compareDefine.setCompareFieldId(field);
                compareDefine.setDataSourceId(dataId);
                compareDefine.setActive(true);
                compareDefine.setNullpass(false);
                compareDefineDao.save(compareDefine);
            }
        }

    }

    @Override
    protected boolean isNeedInit() {
        return true;
    }

    @Override
    public Integer getIndex() {
        return DataInitializerConstant.OPTION_INITIALIZER_INDEX;
    }
}
