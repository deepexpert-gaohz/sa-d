package com.ideatech.ams.risk.tableManager.service;

import com.ideatech.ams.risk.tableManager.dao.TableInfoDao;
import com.ideatech.ams.risk.tableManager.dto.TableInfoDto;
import com.ideatech.ams.risk.tableManager.dto.TableInfoSearchDto;
import com.ideatech.ams.risk.tableManager.entity.TableInfo;
import com.ideatech.common.converter.ConverterService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class TableInfoServiceImpl implements TableInfoService {

    @Autowired
    TableInfoDao tableInfoDao;

    @Override
    public void saveTableInfo(TableInfoDto tableInfoDto) {
        TableInfo tableInfo = new TableInfo();
        if(null != tableInfoDto.getId()){
            tableInfo = tableInfoDao.findOne(tableInfoDto.getId());
            if(tableInfo == null){
                tableInfo = new TableInfo();
            }
        }
        ConverterService.convert(tableInfoDto,tableInfo);
        tableInfoDao.save(tableInfo);
    }

    @Override
    public TableInfoSearchDto searchTableInfoDto(final TableInfoSearchDto tableInfoSearchDto) {
        Specification<TableInfo> specification = new Specification<TableInfo>() {
            List<Predicate> predicateList = new ArrayList<>();
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder cb) {
                if(StringUtils.isNotBlank(tableInfoSearchDto.getCname())){
                    predicateList.add(cb.like(root.<String>get("cname").as(String.class),"%"+tableInfoSearchDto.getCname()+"%"));
                }
                if(StringUtils.isNotBlank(tableInfoSearchDto.getEname())){
                    predicateList.add(cb.like(root.<String>get("ename").as(String.class),"%"+tableInfoSearchDto.getEname()+"%"));
                }
                if(StringUtils.isNotBlank(tableInfoSearchDto.getXtly())){
                    predicateList.add(cb.like(root.<String>get("xtly").as(String.class),"%"+tableInfoSearchDto.getXtly()+"%"));
                }
                return cb.and(predicateList.toArray(new Predicate[0]));
            }
        };
        Pageable pageable = new PageRequest(Math.max(tableInfoSearchDto.getOffset()-1, 0), tableInfoSearchDto.getLimit());
        Page<TableInfo> all = tableInfoDao.findAll(specification, pageable);
        List<TableInfo> content = all.getContent();
        final List<TableInfoDto> tableInfoDtos = ConverterService.convertToList(content, TableInfoDto.class);
        tableInfoSearchDto.setList(tableInfoDtos);
        tableInfoSearchDto.setTotalPages(all.getTotalPages());
        tableInfoSearchDto.setTotalRecord(all.getTotalElements());
        return tableInfoSearchDto;
    }

    @Override
    public TableInfoDto findTableInfoDtoById(Long id) {
        TableInfoDto tableInfoDto = new TableInfoDto();
        TableInfo one = tableInfoDao.findOne(id);
        return ConverterService.convert(one,tableInfoDto);
    }

    @Override
    public void delTableInfo(Long id) {
        tableInfoDao.delete(id);
    }

    @Override
    public TableInfoDto findByEname(String ename) {
        TableInfoDto tableInfoDto = new TableInfoDto();
        TableInfo byCname = tableInfoDao.findByEname(ename);
        return ConverterService.convert(byCname,tableInfoDto);
    }
}
