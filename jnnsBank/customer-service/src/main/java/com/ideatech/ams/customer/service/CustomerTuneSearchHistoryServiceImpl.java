package com.ideatech.ams.customer.service;

import com.ideatech.ams.customer.dao.CustomerTuneSearchHistoryDao;
import com.ideatech.ams.customer.domain.CustomerTuneSearchHistoryDo;
import com.ideatech.ams.customer.dto.CustomerTuneSearchHistoryDto;
import com.ideatech.ams.customer.entity.CustomerTuneSearchHistory;
import com.ideatech.ams.system.dict.dto.OptionDto;
import com.ideatech.ams.system.dict.service.DictionaryService;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseServiceImpl;
import com.ideatech.common.util.BeanCopierUtils;
import com.ideatech.common.util.SecurityUtils;
import com.ideatech.common.util.SensitiveInfoUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class CustomerTuneSearchHistoryServiceImpl extends BaseServiceImpl<CustomerTuneSearchHistoryDao, CustomerTuneSearchHistory, CustomerTuneSearchHistoryDto> implements CustomerTuneSearchHistoryService {

    @Autowired
    private CustomerTuneSearchHistoryDao customerTuneSearchHistoryDao;
    @Autowired
    private DictionaryService dictionaryService;

    @PersistenceContext
    private EntityManager em; //注入EntityManager

//    @Override
//    public void save(CustomerTuneSearchHistoryDto customerTuneSearchHistoryDto) {
//        CustomerTuneSearchHistory customerTuneSearchHistory = new CustomerTuneSearchHistory();
//        BeanCopierUtils.copyProperties(customerTuneSearchHistoryDto, customerTuneSearchHistory);
//        customerTuneSearchHistoryDao.save(customerTuneSearchHistory);
//    }

    @Override
    public TableResultResponse<CustomerTuneSearchHistoryDto> query(CustomerTuneSearchHistoryDto dto, Pageable pageable) {
        String sql = "select new com.ideatech.ams.customer.domain.CustomerTuneSearchHistoryDo(t1,t2,t3) from CustomerTuneSearchHistory t1, OrganizationPo t2, UserPo t3 where t1.organFullId=t2.fullId and t1.createdBy=t3.id ";
        String countSql = "select count(*) from CustomerTuneSearchHistory t1, OrganizationPo t2, UserPo t3 where t1.organFullId=t2.fullId and t1.createdBy=t3.id";
        if (StringUtils.isNotBlank(dto.getType().name())) {
            sql += " and t1.type = ?1 ";
            countSql += " and t1.type = ?1 ";
        }
        if (dto.getBeginDate() != null) {
            sql += " and t1.createdDate >= ?2 ";
            countSql += " and t1.createdDate >= ?2 ";
        }
        if (dto.getEndDate() != null) {
            sql += " and t1.createdDate <= ?3 ";
            countSql += " and t1.createdDate <= ?3 ";
        }
        if (StringUtils.isNotBlank(dto.getOrgName())) {
            sql += " and t2.name like ?4 ";
            countSql += " and t2.name like ?4 ";
        }
        if(StringUtils.isNotBlank(dto.getCreatedUserName()) && dto.getCreatedUserName().contains("-")){
            String[] name = dto.getCreatedUserName().split("-");
            if(name.length > 1){
                sql += " and (t3.cname like ?5 and t3.username like ?14)";
                countSql += " and (t3.cname like ?5 and t3.username like ?14) ";
            }else{
                if(name.length == 1){
                    sql += " and (t3.cname like ?5 or t3.username like ?5)";
                    countSql += " and (t3.cname like ?5 or t3.username like ?5) ";
                }
            }
        }else{
            if (StringUtils.isNotBlank(dto.getCreatedUserName())) {
                sql += " and (t3.cname like ?5 or t3.username like ?5)";
                countSql += " and (t3.cname like ?5 or t3.username like ?5) ";
            }
        }
        if (StringUtils.isNotBlank(dto.getCustomerName())) {
            sql += " and t1.customerName like ?6 ";
            countSql += " and t1.customerName like ?6 ";
        }
        if (StringUtils.isNotBlank(dto.getResult())) {
            sql += " and t1.result like ?7 ";
            countSql += " and t1.result like ?7 ";
        }
        if (StringUtils.isNotBlank(dto.getPbcAccount())) {
            sql += " and t1.pbcAccount like ?8 ";
            countSql += " and t1.pbcAccount like ?8 ";
        }
        if (StringUtils.isNotBlank(dto.getAcctNo())) {
            sql += " and t1.acctNo like ?9 ";
            countSql += " and t1.acctNo like ?9 ";
        }
        if (StringUtils.isNotBlank(dto.getDepositorName())) {
            sql += " and t1.depositorName like ?10 ";
            countSql += " and t1.depositorName like ?10 ";
        }
        if (StringUtils.isNotBlank(dto.getOrgCode())) {
            sql += " and t1.orgCode like ?11 ";
            countSql += " and t1.orgCode like ?11 ";
        }
        if (StringUtils.isNotBlank(dto.getAccountKey())) {
            sql += " and t1.accountKey like ?12 ";
            countSql += " and t1.accountKey like ?12 ";
        }
        if (StringUtils.isNotBlank(dto.getIdCardedType())) {
            sql += " and t1.idCardedType like ?13 ";
            countSql += " and t1.idCardedType like ?13 ";
        }
        //分行支行数据过滤
        sql += " and t1.organFullId like ?14 ";
        countSql += " and t1.organFullId like ?14 ";

        Query query = em.createQuery(sql + "order by t1.createdDate desc ");
        Query queryCount = em.createQuery(countSql);

        if (StringUtils.isNotBlank(dto.getType().name())) {
            query.setParameter(1, dto.getType());
            queryCount.setParameter(1, dto.getType());
        }
        if (dto.getBeginDate() != null) {
            query.setParameter(2, dto.getBeginDate());
            queryCount.setParameter(2, dto.getBeginDate());
        }
        if (dto.getEndDate() != null) {
            query.setParameter(3, dto.getEndDate());
            queryCount.setParameter(3, dto.getEndDate());
        }
        if (StringUtils.isNotBlank(dto.getOrgName())) {
            query.setParameter(4, "%" + dto.getOrgName() + "%");
            queryCount.setParameter(4, "%" + dto.getOrgName() + "%");
        }
        if(StringUtils.isNotBlank(dto.getCreatedUserName()) && dto.getCreatedUserName().contains("-")){
            String[] name = dto.getCreatedUserName().split("-");
            if(name.length > 1){
                query.setParameter(5, "%" + name[1] + "%");
                queryCount.setParameter(5, "%" + name[1] + "%");
                query.setParameter(14, "%" + name[0] + "%");
                queryCount.setParameter(14, "%" + name[0] + "%");
            }else{
                if(name.length == 1){
                    query.setParameter(5, "%" + name[0] + "%");
                    queryCount.setParameter(5, "%" + name[0] + "%");
                }
            }
        }else{
            if (StringUtils.isNotBlank(dto.getCreatedUserName())) {
                query.setParameter(5, "%" + dto.getCreatedUserName() + "%");
                queryCount.setParameter(5, "%" + dto.getCreatedUserName() + "%");
            }
        }
        if (StringUtils.isNotBlank(dto.getCustomerName())) {
            query.setParameter(6, "%" + dto.getCustomerName() + "%");
            queryCount.setParameter(6, "%" + dto.getCustomerName() + "%");
        }
        if (StringUtils.isNotBlank(dto.getResult())) {
            query.setParameter(7, "%" + dto.getResult() + "%");
            queryCount.setParameter(7, "%" + dto.getResult() + "%");
        }
        if (StringUtils.isNotBlank(dto.getPbcAccount())) {
            query.setParameter(8, "%" + dto.getPbcAccount() + "%");
            queryCount.setParameter(8, "%" + dto.getPbcAccount() + "%");
        }
        if (StringUtils.isNotBlank(dto.getAcctNo())) {
            query.setParameter(9, "%" + dto.getAcctNo() + "%");
            queryCount.setParameter(9, "%" + dto.getAcctNo() + "%");
        }
        if (StringUtils.isNotBlank(dto.getDepositorName())) {
            query.setParameter(10, "%" + dto.getDepositorName() + "%");
            queryCount.setParameter(10, "%" + dto.getDepositorName() + "%");
        }
        if (StringUtils.isNotBlank(dto.getOrgCode())) {
            query.setParameter(11, "%" + dto.getOrgCode() + "%");
            queryCount.setParameter(11, "%" + dto.getOrgCode() + "%");
        }
        if (StringUtils.isNotBlank(dto.getAccountKey())) {
            query.setParameter(12, "%" + dto.getAccountKey() + "%");
            queryCount.setParameter(12, "%" + dto.getAccountKey() + "%");
        }
        if (StringUtils.isNotBlank(dto.getIdCardedType())) {
            query.setParameter(13, "%" + dto.getIdCardedType() + "%");
            queryCount.setParameter(13, "%" + dto.getIdCardedType() + "%");
        }

        //分行支行数据过滤
        query.setParameter(14, SecurityUtils.getCurrentOrgFullId()+ "%");
        queryCount.setParameter(14, SecurityUtils.getCurrentOrgFullId()+ "%");

        query.setFirstResult(pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        //查询
        List<CustomerTuneSearchHistoryDo> list = query.getResultList();
        Long count = (Long) queryCount.getSingleResult();

        List<CustomerTuneSearchHistoryDto> dtoList = new ArrayList<>();

        List<OptionDto> optionDtoList = dictionaryService.listOptionByDictId(32119222946719L);
        for (CustomerTuneSearchHistoryDo cd : list) {
            CustomerTuneSearchHistoryDto ctshd = new CustomerTuneSearchHistoryDto();
            BeanUtils.copyProperties(cd.getCustomerTuneSearchHistory(), ctshd);
            ctshd.setOrgName(cd.getOrganizationPo().getName());
            ctshd.setCreatedUserName(cd.getUserPo().getUsername() + "-" + cd.getUserPo().getCname());
            for (OptionDto od : optionDtoList) {
                if (od.getValue().equals(ctshd.getIdCardedType())) {
                    ctshd.setIdCardedTypeStr(od.getName());
                    break;
                }
            }
            //数据脱敏
            ctshd.setName(SensitiveInfoUtils.chineseName(ctshd.getName()));
            ctshd.setMobile(SensitiveInfoUtils.mobilePhone(ctshd.getMobile()));
            ctshd.setCardNo(SensitiveInfoUtils.idCardNum(ctshd.getCardNo()));
            dtoList.add(ctshd);
        }

        return new TableResultResponse<>(count.intValue(), dtoList);
    }

    @Override
    public List<CustomerTuneSearchHistoryDto> findByOrganFullIdLike(String organFullId) {
        return ConverterService.convertToList(customerTuneSearchHistoryDao.findByOrganFullIdLike(organFullId),CustomerTuneSearchHistoryDto.class);
    }

}
