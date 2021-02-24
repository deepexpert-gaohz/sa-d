package com.ideatech.ams.risk.modelFlow.service;

import com.ideatech.ams.risk.model.dto.ModeAndKindlDto;
import com.ideatech.ams.risk.model.dto.ModelSearchDto;
import com.ideatech.ams.risk.modelFlow.dao.ModelFlowConfigerDao;
import com.ideatech.ams.risk.modelFlow.dto.ModelFlowConfigerDo;
import com.ideatech.ams.risk.modelFlow.dto.ModelFlowConfigerDto;
import com.ideatech.ams.risk.modelFlow.dto.ModelFlowConfigerSearchDto;
import com.ideatech.ams.risk.modelFlow.entity.ModelFlowConfiger;
import com.ideatech.common.converter.ConverterService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.ArrayList;
import java.util.List;

@Service
public class ModelFlowConfigerServiceImpl implements ModelFlowConfigerService {

    @Autowired
    ModelFlowConfigerDao modelFlowConfigerDao;

    @Autowired
    EntityManager entityManager;

    @Override
    public void saveModelFlowConfiger(ModelFlowConfigerDto modelFlowConfigerDto) {
        ModelFlowConfiger modelFlowConfiger = new ModelFlowConfiger();
        if(null != modelFlowConfigerDto.getId()){
             modelFlowConfiger = modelFlowConfigerDao.findOne(modelFlowConfigerDto.getId());
             if(modelFlowConfiger == null){
                 modelFlowConfiger = new ModelFlowConfiger();
             }
        }
        ConverterService.convert(modelFlowConfigerDto,modelFlowConfiger);
        modelFlowConfigerDao.save(modelFlowConfiger);
    }

    @Override
    public void delModelFlowConfiger(Long id) {
        modelFlowConfigerDao.delete(id);
    }

    public ModelFlowConfigerSearchDto searchData(ModelFlowConfigerSearchDto modelFlowConfigerSearchDto){
        Pageable pageable = new PageRequest(Math.max(modelFlowConfigerSearchDto.getOffset() - 1, 0), modelFlowConfigerSearchDto.getLimit());
        String sql = " SELECT\n" +
                "\t mfc.YD_ID,\n" +
                "\t m.YD_MODEL_ID,\n" +
                "\t m.YD_NAME,\n" +
                "\t rp.KEY_,\n" +
                "\t rp.NAME_,\n" +
                "\t mfc.YD_IS_AUTO,\n" +
                "\t mfc.YD_LAST_UPDATE_DATE \n" +
                " FROM\n" +
                "\t YD_MODEL_FLOW_CONFIGER mfc \n" +
                "\t left join YD_RISK_MODELS m on mfc.YD_MODEL_ID = m.YD_ID \n" +
                "\t right join (select key_ as key_, max(version_) value, name_ from ACT_RE_PROCDEF group by key_, name_) rp on mfc.yd_flow_key=rp.key_  \n " +
                " WHERE 1=1\n" +
                "  and mfc.YD_DELETED = '0'";
        if(StringUtils.isNotBlank(modelFlowConfigerSearchDto.getModelId())){
            sql += " and m.YD_MODEL_ID like ?1 ";
        }
        if(StringUtils.isNotBlank(modelFlowConfigerSearchDto.getFlowKey())){
            sql += " and rp.KEY_ like ?2 ";
        }
        if(StringUtils.isNotBlank(modelFlowConfigerSearchDto.getIsAuto())){
            sql += " and mfc.YD_IS_AUTO like ?3 ";
        }
        Query query = entityManager.createNativeQuery(sql);
        if (StringUtils.isNotBlank(modelFlowConfigerSearchDto.getModelId())) {
            query.setParameter(1, "%" + modelFlowConfigerSearchDto.getModelId() + "%");
        }
        if (StringUtils.isNotBlank(modelFlowConfigerSearchDto.getFlowKey())) {
            query.setParameter(2, "%" + modelFlowConfigerSearchDto.getFlowKey() + "%");
        }
        if (StringUtils.isNotBlank(modelFlowConfigerSearchDto.getIsAuto())) {
            query.setParameter(3, "%" + modelFlowConfigerSearchDto.getIsAuto() + "%");
        }
        List resultList1 = query.getResultList();
        Long count = (long) resultList1.size();
        query.setFirstResult(pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<Object[]> resultList = query.getResultList();
        List<ModelFlowConfigerDo> modelFlowConfigerDos = new ArrayList<>();
        if(resultList!=null&&resultList.size()!=0){
            for (Object[] m: resultList) {
                ModelFlowConfigerDo  modelFlowConfigerDo = new ModelFlowConfigerDo(
                        (m[0]==null)?"":m[0].toString(),
                        (m[1]==null)?"":m[1].toString(),
                        (m[2]==null)?"":m[2].toString(),
                        (m[3]==null)?"":m[3].toString(),
                        (m[4]==null)?"":m[4].toString(),
                        (m[5]==null)?"":m[5].toString(),
                        (m[6]==null)?"":m[6].toString());
                modelFlowConfigerDos.add(modelFlowConfigerDo);
            }
        }
        modelFlowConfigerSearchDto.setList(modelFlowConfigerDos);
        modelFlowConfigerSearchDto.setTotalRecord(count);
        modelFlowConfigerSearchDto.setTotalPages((int) Math.ceil(count.intValue()/modelFlowConfigerSearchDto.getLimit()));
        return modelFlowConfigerSearchDto;

    }

    @Override
    public ModelFlowConfigerDto findById(Long id) {
        ModelFlowConfigerDto modelFlowConfigerDto = new ModelFlowConfigerDto();
        ModelFlowConfiger one = modelFlowConfigerDao.findOne(id);
        return ConverterService.convert(one,modelFlowConfigerDto);
    }

    @Override
    public ModelFlowConfigerDto findByModelId(String modelId) {
        ModelFlowConfigerDto modelFlowConfigerDto = new ModelFlowConfigerDto();
        ModelFlowConfiger byModelId = modelFlowConfigerDao.findByModelId(modelId);
        return ConverterService.convert(byModelId,modelFlowConfigerDto);
    }

    @Override
    public ModelFlowConfigerDto findFlowkeyAndNameByModelId(String key) {
        String sql ="select t.id_ as yd_flow_key ,t.name_,t.key_ from \n" +
                "  (select t1.id_,t1.name_,t1.key_ from ACT_RE_PROCDEF t1 , act_re_deployment t2 \n" +
                "  where t1.deployment_id_ = t2.id_ and t1.key_='"+key+"' \n" +
                "  order by t2.deploy_time_ DESC) t where rownum =1  ";
        Query query = entityManager.createNativeQuery(sql);
        List<Object[]> list = query.getResultList();
        ModelFlowConfigerDto modelFlowConfigerDto = new ModelFlowConfigerDto();
        modelFlowConfigerDto.setFlowKey(list.get(0)[0].toString());
        modelFlowConfigerDto.setFlowName(list.get(0)[1].toString());
        modelFlowConfigerDto.setModelName(list.get(0)[2].toString());
        return modelFlowConfigerDto;
    }



    @Override
    public ModelSearchDto getModels(ModelSearchDto modelSearchDto) {
        String sql = "select  ms.yd_id,ms.yd_name from yd_risk_models ms " +
                " where ms.yd_id  not in (select yd_model_id from yd_model_flow_configer c where c.yd_deleted='0') and ms.yd_deleted='0'";
        Query query = entityManager.createNativeQuery(sql);
        List<Object[]> list = query.getResultList();
        List<ModeAndKindlDto> result = new ArrayList<>();
        for (Object[] o :list) {
            ModeAndKindlDto m = new ModeAndKindlDto();
           m.setId(Long.parseLong((o[0]==null)?"":o[0].toString()));
            m.setName((o[1]==null)?"":o[1].toString());
            result.add(m);
        }
        modelSearchDto.setList(result);
        return modelSearchDto;

    }
}
