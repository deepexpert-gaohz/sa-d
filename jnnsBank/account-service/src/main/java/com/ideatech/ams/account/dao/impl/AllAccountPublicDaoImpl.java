package com.ideatech.ams.account.dao.impl;

import com.ideatech.ams.account.dao.AllAccountPublicDao;
import com.ideatech.ams.account.dto.AllAccountPublicDTO;
import com.ideatech.ams.account.util.Map2DomainUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AllAccountPublicDaoImpl extends BaseRepositoryImpl implements AllAccountPublicDao {

    /**
     * 取消yd_public_accountallinfo_v
     * 已经废弃
     * @param id
     * @return
     */
    @Override
    @Deprecated
    public AllAccountPublicDTO findById(Long id) {
        String sql = "select * from yd_public_accountallinfo_v where yd_id = :id";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);

        Object object = getSingleResult(sql, param);
        try {
            Map<String, String> map = (Map<String, String>) object;
            AllAccountPublicDTO allAccountPublicDTO = (AllAccountPublicDTO) Map2DomainUtils.converter(map,
                    AllAccountPublicDTO.class);

            Map<String, Object> setParam = new HashMap<String, Object>();
            setParam.put("customerId", allAccountPublicDTO.getCustomerId());
            setParam.put("refBillId", allAccountPublicDTO.getRefBillId());
//            setRelate(setParam, allAccountPublicDTO);
//            setPartner(setParam, allAccountPublicDTO);
            return allAccountPublicDTO;
        } catch (Exception e) {
            LOG.error("map转换AllBillsPublicDTO错误", e);
        }
        return null;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Page<AllAccountPublicDTO> findPage(String sql, Map<String, Object> param, Pageable pageable) {
        LOG.info("输出的SQL:{}", sql);
        Page page = getPageResultList(sql, param, pageable);
        List<AllAccountPublicDTO> result = new ArrayList<AllAccountPublicDTO>();
        AllAccountPublicDTO allAccountPublicDTO = null;
        for (Object object : page.getContent()) {
            try {
                Map<String, String> map = (Map<String, String>) object;
                allAccountPublicDTO = (AllAccountPublicDTO) Map2DomainUtils.converter(map, AllAccountPublicDTO.class);
                result.add(allAccountPublicDTO);
            } catch (Exception e) {
                LOG.error("map转换AllBillsPublicDTO错误", e);
            }
        }
        return new PageImpl<AllAccountPublicDTO>(result, pageable, page.getTotalElements());
    }

}
