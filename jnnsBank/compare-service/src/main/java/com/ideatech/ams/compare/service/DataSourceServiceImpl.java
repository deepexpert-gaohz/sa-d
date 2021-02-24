package com.ideatech.ams.compare.service;

import com.ideatech.ams.compare.dao.DataSourceDao;
import com.ideatech.ams.compare.dao.spec.DataSourceSpec;
import com.ideatech.ams.compare.domain.DataSourceDo;
import com.ideatech.ams.compare.dto.CompareRuleDataSourceDto;
import com.ideatech.ams.compare.dto.DataSourceDto;
import com.ideatech.ams.compare.entity.DataSource;
import com.ideatech.ams.system.org.utils.OrganUtils;
import com.ideatech.ams.system.user.dao.UserDao;
import com.ideatech.ams.system.user.entity.UserPo;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseServiceImpl;
import com.ideatech.common.util.BeanCopierUtils;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.util.FieldUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
@Slf4j
public class DataSourceServiceImpl extends BaseServiceImpl<DataSourceDao, DataSource, DataSourceDto> implements DataSourceService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private CompareRuleDataSourceService compareRuleDataSourceService;

    @PersistenceContext
    private EntityManager em; //注入EntityManager

    @Override
    public TableResultResponse<DataSourceDto> query(DataSourceDto dto, Pageable pageable) throws Exception {

        try {
            if (StringUtils.isNotBlank(dto.getBeginDateStr())) {
                String begin = dto.getBeginDateStr() + " 00:00:00";
                dto.setBeginDate(DateUtils.parse(begin, "yyyy-MM-dd HH:mm:ss"));
            }
            if (StringUtils.isNotBlank(dto.getEndDateStr())) {
                String end = dto.getEndDateStr() + " 23:59:59";
                dto.setEndDate(DateUtils.parse(end, "yyyy-MM-dd HH:mm:ss"));
            }
        } catch (ParseException e) {
            e.printStackTrace();
            throw new Exception("查询条件-日期格式错误");
        }

        String organFullId = SecurityUtils.getCurrentOrgFullId();
        List<String> fullIdList = OrganUtils.getFullIdsByFullId(organFullId);


        String sql = "select new com.ideatech.ams.compare.domain.DataSourceDo(t1,t2)";
        String countSql = "select count(*)";
        String from = " from DataSource t1, UserPo t2 where t1.createdBy=t2.id and t1.createdBy <> '2' ";
        sql += from;
        countSql += from;
        if (StringUtils.isNotBlank(dto.getName())) {
            sql += " and t1.name like ?1 ";
            countSql += " and t1.name like ?1 ";
        }
        if (dto.getBeginDate() != null) {
            sql += " and t1.createdDate >= ?2 ";
            countSql += " and t1.createdDate >= ?2 ";
        }
        if (dto.getEndDate() != null) {
            sql += " and t1.createdDate <= ?3 ";
            countSql += " and t1.createdDate <= ?3 ";
        }
        if (dto.getCollectType() != null) {
            sql += " and t1.collectType = ?4 ";
            countSql += " and t1.collectType = ?4 ";
        }
        if (dto.getCreatedBy() != null) {
            sql += " and t2.cname like ?5 ";
            countSql += " and t2.cname like ?5 ";
        }
        if (CollectionUtils.isNotEmpty(fullIdList)) {
            sql += " and t1.organFullId in (?6) ";
            countSql += " and t1.organFullId in (?6) ";
        }

        Query query = em.createQuery(sql);
        Query queryCount = em.createQuery(countSql);

        if (StringUtils.isNotBlank(dto.getName())) {
            query.setParameter(1, "%" + dto.getName() + "%");
            queryCount.setParameter(1, "%" + dto.getName() + "%");
        }
        if (dto.getBeginDate() != null) {
            query.setParameter(2, dto.getBeginDate());
            queryCount.setParameter(2, dto.getBeginDate());
        }
        if (dto.getEndDate() != null) {
            query.setParameter(3, dto.getEndDate());
            queryCount.setParameter(3, dto.getEndDate());
        }
        if (dto.getCollectType() != null) {
            query.setParameter(4, dto.getCollectType());
            queryCount.setParameter(4, dto.getCollectType());
        }
        if (dto.getCreatedBy() != null) {
            query.setParameter(5, "%" + dto.getCreatedBy() + "%");
            queryCount.setParameter(5, "%" + dto.getCreatedBy() + "%");
        }
        if (CollectionUtils.isNotEmpty(fullIdList)) {
            query.setParameter(6, fullIdList);
            queryCount.setParameter(6, fullIdList);
        }

        query.setFirstResult(pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        //查询
        List<DataSourceDo> list = query.getResultList();
        Long count = (Long) queryCount.getSingleResult();

        List<DataSourceDto> result = new ArrayList<>();

        for (DataSourceDo dsdo : list) {
            DataSourceDto dataSourceDto = new DataSourceDto();
            BeanUtils.copyProperties(dsdo.getDataSource(), dataSourceDto);
            dataSourceDto.setCreatedBy(dsdo.getUserPo().getCname());
            dataSourceDto.setCreatedDateStr(DateUtils.DateToStr(dsdo.getDataSource().getCreatedDate(),"yyyy-MM-dd HH:mm:ss"));
            result.add(dataSourceDto);
        }
        return new TableResultResponse<DataSourceDto>(count.intValue(), result);
    }

    @Override
    public void updateSource(DataSourceDto dto) {
        Long id = null;
        try {
            id = (Long) FieldUtils.getFieldValue(dto, "id");
        } catch (IllegalAccessException e) {
            log.warn("获取DTO ID失败", e);
        }
        if (id == null) {
            throw new BizServiceException(EErrorCode.TECH_DATA_INVALID, "ID不能为空");
        }
        DataSource source = getBaseDao().findOne(id);
        if (source == null) {
            throw new BizServiceException(EErrorCode.TECH_DATA_NOT_EXIST, "数据不存在");
        }
        if(StringUtils.isNotBlank(dto.getName())){
            DataSource isHave = getBaseDao().findByNameAndIdNot(dto.getName(),id);
            if(isHave!=null){
                throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "数据源名称已存在！");
            }else{
                source.setName(dto.getName());
            }
        }
        if(dto.getCollectType()!=null){
            source.setCollectType(dto.getCollectType());
        }
        if(dto.getDataType()!=null){
            source.setDataType(dto.getDataType());
        }
        if(StringUtils.isNotBlank(dto.getCode())){
            source.setCode(dto.getCode());
        }
        source.setPbcStartTime(dto.getPbcStartTime());
        source.setPbcEndTime(dto.getPbcEndTime());
        getBaseDao().save(source);
    }

    @Override
    public void DataSourceSave(DataSourceDto dto) {

        long count = getBaseDao().count();
        if(count == 9){
            throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "数据源创建已是最大值，请删除一个后再增加数据源");
        }

        if(dto.getName()==null||"".equals(dto.getName())){
            throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "数据名称未输入！");
        }
        if(dto.getCollectType()==null){
            throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "来源方式未输入！");
        }
        if(dto.getDataType()==null){
            throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "采集数据未输入！");
        }
        DataSource dataSource = null;
        if (dto.getCode()!=null){
            dataSource = getBaseDao().findByCode(dto.getCode());
        }
        DataSource dataName = getBaseDao().findByName(dto.getName());
        if(dataName!=null){
            throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "数据源名称已存在！");
        }
        if(dataSource != null){
            throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "数据编码已存在，不要重复新增已存在的数据源编码");
        }else{
            String indexs = "";
            List<DataSource> dataSources = getBaseDao().findAll(new Sort(Sort.Direction.DESC, "domain"));
            if(CollectionUtils.isNotEmpty(dataSources)){
                String preStruct = dataSources.get(0).getDomain();
                Integer index = new Integer(StringUtils.substring(preStruct, preStruct.length() - 1)) + 1;
                if(index >= 9 && count < 9){
                    for(DataSource dataSource1 : dataSources){
                        String a = dataSource1.getDomain().substring(11);
                        indexs += a + ",";
                    }
                    indexs = indexs.substring(0,indexs.length() -1);
                    for(int i = 1;i < 10;i++ ){
                        if(!indexs.contains(i+"")){
                            index = new Integer(i);
                            break;
                        }
                    }
                }
                String number = index.toString();
                dto.setDomain("CompareData"+number);
                save(dto);
            }else{
                dto.setDomain("CompareData1");
                save(dto);
            }
        }
    }

    @Override
    public void deleteDataSource(Long id) {
        DataSource dataSource = getBaseDao().findOne(id);

        if(StringUtils.equals("ams",dataSource.getCode()) || StringUtils.equals("code",dataSource.getCode())
                || StringUtils.equals("saic",dataSource.getCode())|| StringUtils.equals("pbc",dataSource.getCode())){
            throw new BizServiceException(EErrorCode.SYSTEM_ERROR,"不允许删除初始化比对数据源！");
        }

        List<CompareRuleDataSourceDto> list = compareRuleDataSourceService.findByDataSoureIdAndActive(id,true);
        if(CollectionUtils.isNotEmpty(list)){
            throw new BizServiceException(EErrorCode.SYSTEM_ERROR,"该数据源正在被使用中，请勿删除");
        }
        deleteById(id);
    }

    @Override
    public DataSourceDto findByName(String name) {
        DataSourceDto dto = new DataSourceDto();

        DataSource dataSource = getBaseDao().findByName(name);
        BeanCopierUtils.copyProperties(dataSource, dto);

        return dto;
    }

    private List<String> getUser(DataSourceDto dto,Pageable pageable){
        List<String> createdByList = new ArrayList<>();
        Page<UserPo> userData = userDao.findByCnameLike(dto.getCreatedBy(),pageable);
        if(CollectionUtils.isNotEmpty(userData.getContent())){
            for (UserPo po:userData.getContent()) {
                createdByList.add(String.valueOf(po.getId()));
            }
        }else{
            createdByList.add(dto.getCreatedBy());
        }

        return createdByList;
    }
}
