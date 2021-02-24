package com.ideatech.common.service;

import com.google.common.reflect.TypeToken;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BizServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.util.FieldUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Slf4j
public abstract class BaseServiceImpl<DAO extends JpaRepository<ENTITY, Long>, ENTITY, DTO> {

    private final TypeToken<DTO> dtoTypeToken = new TypeToken<DTO>(getClass()) {
    };
    private final Class dtoType = dtoTypeToken.getRawType();
    private final TypeToken<ENTITY> entityTypeToken = new TypeToken<ENTITY>(getClass()) {
    };
    private final Class entityType = entityTypeToken.getRawType();

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DAO dao;

    public DTO findById(Long id) {
        ENTITY entity = dao.findOne(id);
        return (DTO) ConverterService.convert(entity, dtoType);
    }

    public DTO save(DTO dto) {
        Long id = null;
        try {
            id = (Long) FieldUtils.getFieldValue(dto, "id");
        } catch (IllegalAccessException e) {
            log.warn("获取DTO ID失败", e);
        }
        ENTITY entity = (ENTITY) BeanUtils.instantiate(entityType);
        if (id != null) {
            entity = dao.findOne(id);
            if (entity == null) {
                entity = (ENTITY) BeanUtils.instantiate(entityType);
            }
        }
        ConverterService.convert(dto, entity);
        dao.save(entity);
        ConverterService.convert(entity, dto);
        return dto;
    }

    public void deleteById(Long id) {
        dao.delete(id);
    }

    public List<DTO> list() {
        List<ENTITY> all = dao.findAll();
        return (List<DTO>) ConverterService.convertToList(all, dtoType);
    }

    public void insert(DTO dto) {
        ENTITY entity = (ENTITY) BeanUtils.instantiate(entityType);
        ConverterService.convert(dto, entity);
        dao.save(entity);
    }

    @Transactional
    public void insert(List<DTO> dtos) {
        for (DTO dto : dtos) {
            ENTITY entity = (ENTITY) BeanUtils.instantiate(entityType);
            ConverterService.convert(dto, entity);
            entityManager.persist(entity);
        }
        entityManager.flush();
        entityManager.clear();
    }

    public DTO update(DTO dto) {
        Long id = null;
        try {
            id = (Long) FieldUtils.getFieldValue(dto, "id");
        } catch (IllegalAccessException e) {
            log.warn("获取DTO ID失败", e);
        }
        if (id == null) {
            throw new BizServiceException(EErrorCode.TECH_DATA_INVALID, "ID不能为空");
        }
        ENTITY entity = dao.findOne(id);
        if (entity == null) {
            throw new BizServiceException(EErrorCode.TECH_DATA_NOT_EXIST, "数据不存在");
        }
        ConverterService.convert(dto, entity);
        dao.save(entity);
        return dto;
    }

    protected DAO getBaseDao() {
        return dao;
    }

}
