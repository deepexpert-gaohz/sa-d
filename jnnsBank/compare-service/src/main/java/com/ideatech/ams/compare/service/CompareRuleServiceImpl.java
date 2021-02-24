package com.ideatech.ams.compare.service;


import com.ideatech.ams.compare.dao.*;
import com.ideatech.ams.compare.dao.spec.CompareRuleSpec;
import com.ideatech.ams.compare.dto.CompareRuleDto;
import com.ideatech.ams.compare.dto.CompareRuleSearchDto;
import com.ideatech.ams.compare.dto.CompareTaskDto;
import com.ideatech.ams.compare.dto.DataSourceDto;
import com.ideatech.ams.compare.entity.*;
import com.ideatech.ams.compare.enums.CollectType;
import com.ideatech.ams.system.org.utils.OrganUtils;
import com.ideatech.ams.system.user.service.UserService;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.service.BaseServiceImpl;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.SecurityUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author jzh
 * @date 2019/1/16.
 */
@Service("moduleCompareRuleService")
@Transactional
public class CompareRuleServiceImpl extends BaseServiceImpl<CompareRuleDao, CompareRule, CompareRuleDto> implements CompareRuleService {

    @Autowired
    private DataSourceDao dataSourceDao;

    @Autowired
    private CompareRuleDataSourceDao compareRuleDataSourceDao;

    @Autowired
    private CompareRuleFieldsDao compareRuleFieldsDao;

    @Autowired
    private CompareDefineDao compareDefineDao;

    @Autowired
    private UserService userService;

    @Autowired
    private CompareTaskService compareTaskService;

    @Override
    public CompareRuleSearchDto search(CompareRuleSearchDto compareRuleSearchDto, Pageable pageable) {
        if (StringUtils.isBlank(compareRuleSearchDto.getBeginDate())) {
            compareRuleSearchDto.setBeginDate("00000000");
        }
        if (StringUtils.isBlank(compareRuleSearchDto.getEndDate())) {
            compareRuleSearchDto.setEndDate("99999999");
        }
        List<String> fullIdList = OrganUtils.getFullIdsByFullId(SecurityUtils.getCurrentOrgFullId());
        if(CollectionUtils.isNotEmpty(fullIdList)){
            compareRuleSearchDto.setFullIdList(fullIdList);
        }

        //过滤虚拟用户创建的数据
        compareRuleSearchDto.setCreatedBy("2");

        Page<CompareRule> compareRulePage = getBaseDao().findAll(new CompareRuleSpec(compareRuleSearchDto), pageable);
        List<CompareRuleDto> compareRuleDtoList = ConverterService.convertToList(compareRulePage.getContent(), CompareRuleDto.class);
        compareRuleSearchDto.setList(compareRuleDtoList);
        compareRuleSearchDto.setTotalRecord(compareRulePage.getTotalElements());
        compareRuleSearchDto.setTotalPages(compareRulePage.getTotalPages());
        return compareRuleSearchDto;
    }

    /**
     *
     * @param compareRuleDto 比对规则基本对象
     * @param dataIds 数据源id集合
     * @param fields 比对字段集合
     * @param parentIds 数据源依赖关系集合
     * @param httpServletRequest httpServletRequest
     */
    @Override
    public void saveCompareRuleDetail(CompareRuleDto compareRuleDto, Long[] dataIds, Long[] fields, String[] parentIds, HttpServletRequest httpServletRequest) {
        //判断比对规则名称是否存在
        if (compareRuleDto.getId() == null) {
            CompareRule compareRule1 = getBaseDao().findByName(compareRuleDto.getName());
            if (compareRule1 != null) {
                throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "比对规则名称重复，请重新录入！");
            }
        }

        if (fields != null && dataIds == null) {
            throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "请先勾选比对数据源！");
        }

        if (dataIds.length == 1) {
            throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "请先勾两个及以上数据源！");
        }

        //判断先决数据源是否相互依赖(在线采集和手动导入都不允许相互依赖)
        if (parentIds != null) {
            for (String ids : parentIds) {
                String sourceId = ids.split("-")[0];
                String parentId = ids.split("-")[1];
                for (String ids1 : parentIds) {
                    String sourceId1 = ids1.split("-")[0];
                    String parentId1 = ids1.split("-")[1];
                    if (sourceId.equals(parentId1) && parentId.equals(sourceId1)) {
                        throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "先决数据源不能相互依赖，请重新选择！");
                    }
                }
            }
        }

        DataSourceDto zgSourceDto = ConverterService.convert(dataSourceDao.findOne(1001L),DataSourceDto.class);
        DataSourceDto pbcSourceDto = ConverterService.convert(dataSourceDao.findOne(1002L),DataSourceDto.class);
        DataSourceDto saicSourceDto = ConverterService.convert(dataSourceDao.findOne(1003L),DataSourceDto.class);
        DataSourceDto coreSourceDto = ConverterService.convert(dataSourceDao.findOne(1004L),DataSourceDto.class);

        /**
         *  （在线采集下）
         *  如果数据源是工商或者人行，必须存在先决数据源条件
         *  账管不能依赖任何数据源
         *  核心只能依赖账管或不依赖。
         *
         *  (手动导入)
         *  不允许依赖其他数据源（依赖了也没有意义，索性不让依赖。）
         */
        if (dataIds != null) {
            //1001账管 ，1002 人行，1003 工商，1004核心
            boolean res = false;
            for (Long id : dataIds) {
                if (id == 1001){//1001账管
                    if (parentIds != null) {
                        if (zgSourceDto.getCollectType()==CollectType.ONLINE){
                            for (String ids : parentIds) {
                                if (ids.startsWith("1001")) {
                                    throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "本账管系统数据禁止勾选先决数据源！");
                                }
                            }
                        }else {
                            for (String ids : parentIds) {
                                if (ids.startsWith("1001")) {
                                    throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "本账管系统数据为手动导入方式无需勾选先决数据源！");
                                }
                            }
                        }
                    }
                } else if (id == 1002) {//1002 人行
                    if (pbcSourceDto.getCollectType()==CollectType.ONLINE){
//                        if (parentIds == null) {
//                            throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "人行数据请勾选先决数据源！");
//                        }
//                        for (String ids : parentIds) {
//                            if (ids.startsWith("1002")) {
//                                res = true;
//                            }
//                        }
//                        if (!res) {
//                            throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "人行数据请勾选先决数据源！");
//                        }
                    }else {
                        if (parentIds != null) {
                            for (String ids : parentIds) {
                                if (ids.startsWith("1002")) {
                                    throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "人行数据为手动导入方式无需勾选先决数据源！");
                                }
                            }
                        }
                    }
                } else if (id == 1003) {//1003 工商
                    //如果是采集的状态需要勾选先决数据源
                    if(saicSourceDto.getCollectType() == CollectType.ONLINE){
                        if (parentIds == null) {
                            throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "工商数据请勾选先决数据源！");
                        }
                        for (String ids : parentIds) {
                            if (ids.startsWith("1003")) {
                                res = true;
                            }
                        }
                        if (!res) {
                            throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "工商数据请勾选先决数据源！");
                        }
                    }else {
                        if (parentIds != null) {
                            for (String ids : parentIds) {
                                if (ids.startsWith("1003")) {
                                    throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "工商数据为手动导入方式无需勾选先决数据源！");
                                }
                            }
                        }
                    }
                }else if (id == 1004){//1004核心
                    if (parentIds != null) {
                        if (coreSourceDto.getCollectType()==CollectType.ONLINE){
                            for (String ids : parentIds) {
                                if (ids.startsWith("1004") && !ids.endsWith("1001")) {
                                    throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "核心数据禁止依赖本账管系统以外数据！");
                                }
                            }
                        }else {
                            for (String ids : parentIds) {
                                if (ids.startsWith("1004")) {
                                    throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "核心数据为手动导入方式无需勾选先决数据源！");
                                }
                            }
                        }
                    }
                }
                res = false;
            }
        }


        Map<Long, List<Long>> parentIdsMap = new HashMap<>();
        if (parentIds != null) {
            parentIdsMap = convertMap(parentIds);
        }

        //1、操作CompareRule
        if (compareRuleDto.getId() == null) {
            //新建时
            compareRuleDto.setCreateTime(DateUtils.getNowDateShort());
            compareRuleDto.setCount(0);
            compareRuleDto.setCreater(userService.findByUsername(SecurityUtils.getCurrentUser().getUsername()).getCname());
            compareRuleDto.setBussBlackList(compareRuleDto.getBussBlackList() == null ? false : compareRuleDto.getBussBlackList());
            compareRuleDto.setPersonBlackList(compareRuleDto.getPersonBlackList() == null ? false : compareRuleDto.getPersonBlackList());
            compareRuleDto.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
        } else {
            CompareRule compareRule = getBaseDao().findOne(compareRuleDto.getId());
            compareRuleDto.setCreater(compareRule.getCreater());
            compareRuleDto.setCount(compareRule.getCount());
            compareRuleDto.setCreateTime(compareRule.getCreateTime());
            compareRuleDto.setCreatedBy(compareRule.getCreatedBy());
            compareRuleDto.setCreatedDate(compareRule.getCreatedDate());
            compareRuleDto.setBussBlackList(compareRuleDto.getBussBlackList() == null ? false : compareRuleDto.getBussBlackList());
            compareRuleDto.setPersonBlackList(compareRuleDto.getPersonBlackList() == null ? false : compareRuleDto.getPersonBlackList());
            compareRuleDto.setOrganFullId(StringUtils.isEmpty(compareRule.getOrganFullId()) ? SecurityUtils.getCurrentOrgFullId() : compareRule.getOrganFullId());
        }

        CompareRuleDto compareRuleDto1 = save(compareRuleDto);

        //2、操作CompareRuleDataSource
        //修改时，删除原有所有记录，然后保存新记录
        compareRuleDataSourceDao.deleteAllByCompareRuleId(compareRuleDto1.getId());
        List<DataSource> dataSourceList = dataSourceDao.findAll();
        for (DataSource dataSource : dataSourceList) {
            CompareRuleDataSource compareRuleDataSource = new CompareRuleDataSource();
            if (dataIds != null && Arrays.asList(dataIds).contains(dataSource.getId())) {
                //是否启用
                compareRuleDataSource.setActive(true);
            } else {
                compareRuleDataSource.setActive(false);
            }
            compareRuleDataSource.setCompareRuleId(compareRuleDto1.getId());
            compareRuleDataSource.setDataSourceId(dataSource.getId());
            if (parentIdsMap.containsKey(dataSource.getId())) {
                String join = StringUtils.join(parentIdsMap.get(dataSource.getId()), ",");
                compareRuleDataSource.setParentDataSourceIds(join);
            }
            compareRuleDataSourceDao.save(compareRuleDataSource);
        }
        if (fields == null) {
            throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "比对字段必须配置！");
        }
        //3、CompareRuleFields
        //修改时，删除原有所有记录，然后保存新记录
        compareRuleFieldsDao.deleteAllByCompareRuleId(compareRuleDto1.getId());
        for (Long field : fields) {
            CompareRuleFields compareRuleFields = new CompareRuleFields();
            compareRuleFields.setActive(true);
            compareRuleFields.setCompareFieldId(field);
            compareRuleFields.setCompareRuleId(compareRuleDto1.getId());
            compareRuleFieldsDao.save(compareRuleFields);
        }

        //4、httpServletRequest中拿CompareDefine所需字段
        //修改时，删除原有所有记录，然后保存新记录
        compareDefineDao.deleteAllByCompareRuleId(compareRuleDto1.getId());
        for (Long field : fields) {
            /**
             * 前端传回的数据格式
             * dataIds:1060313115684864
             * dataIds:1060256566882304
             * fields:1001
             * field1001_data1060313115684864_use:on
             * field1001_data1060313115684864_empty:on
             * field1001_data1060256566882304_use:on
             * field1001_data1060256566882304_empty:on
             * fields:1003
             * field1003_data1060313115684864_use:on
             * field1003_data1060313115684864_empty:on
             */
            for (Long dataId : dataIds) {
                String use = httpServletRequest.getParameter("field" + field + "_data" + dataId + "_use");
                CompareDefine compareDefine = new CompareDefine();
                compareDefine.setCompareRuleId(compareRuleDto1.getId());
                compareDefine.setCompareFieldId(field);
                compareDefine.setDataSourceId(dataId);
                if (use != null) {
                    compareDefine.setActive(true);
                } else {
                    compareDefine.setActive(false);
                }
                String empty = httpServletRequest.getParameter("field" + field + "_data" + dataId + "_empty");
                if (empty != null) {
                    compareDefine.setNullpass(true);
                } else {
                    compareDefine.setNullpass(false);
                }
                compareDefineDao.save(compareDefine);
            }

        }


    }

    @Override
    public void deleteCompareRuleDetail(Long id) {

        //先查询是否有任务使用了此规则  有的话不允许删除
        List<CompareTaskDto> list = compareTaskService.findByCompareRuleId(id);
        if (CollectionUtils.isNotEmpty(list)) {
            throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "该比对规则有比对任务正在使用，请先删除比对任务！");
        }

        //1、删CompareRule
        getBaseDao().delete(id);
        //2、删除CompareRuleDataSource
        compareRuleDataSourceDao.deleteAllByCompareRuleId(id);
        //3、删CompareRuleFields
        compareRuleFieldsDao.deleteAllByCompareRuleId(id);
        //4、删CompareDefine
        compareDefineDao.deleteAllByCompareRuleId(id);
    }

    @Override
    public List<CompareRuleDto> getAll() {
        List<CompareRule> data = getBaseDao().findAll();
        Iterator<CompareRule> iterable = data.iterator();

        //过滤虚拟用户创建的规则
        while (iterable.hasNext()){
            if (iterable.next().getCreatedBy().equals("2")){
                iterable.remove();
            }
        }

        List<CompareRuleDto> compareRuleDtoList = ConverterService.convertToList(data, CompareRuleDto.class);
        return compareRuleDtoList;
    }

    @Override
    public List<CompareRuleDto> getByOrganUpWard() {
        CompareRuleSearchDto dto = new CompareRuleSearchDto();
        List<String> fullIdList = OrganUtils.getFullIdsByFullId(SecurityUtils.getCurrentOrgFullId());
        if (CollectionUtils.isNotEmpty(fullIdList)) {
            dto.setFullIdList(fullIdList);
        }
        List<CompareRule> data = getBaseDao().findAll(new CompareRuleSpec(dto));
        Iterator<CompareRule> iterable = data.iterator();

        //过滤虚拟用户创建的规则
        while (iterable.hasNext()){
            if (iterable.next().getCreatedBy().equals("2")){
                iterable.remove();
            }
        }
        List<CompareRuleDto> compareRuleDtoList = ConverterService.convertToList(data, CompareRuleDto.class);
        return compareRuleDtoList;
    }

    /**
     * String数组切换为Map
     *
     * @param parentIds
     * @return
     */
    private Map<Long, List<Long>> convertMap(String[] parentIds) {
        Map<Long, List<Long>> result = new HashMap<>();
        for (String id : parentIds) {
            if (StringUtils.isNotBlank(id)) {
                String[] ids = StringUtils.split(id, "-");
                if (ids.length == 2) {
                    Long ids1 = Long.valueOf(ids[0]);
                    Long ids2 = Long.valueOf(ids[1]);
                    if (result.containsKey(ids1)) {
                        result.get(ids1).add(ids2);
                    } else {
                        List<Long> longs = new ArrayList<>();
                        longs.add(ids2);
                        result.put(ids1, longs);
                    }

                }
            }
        }
        return result;
    }
}
