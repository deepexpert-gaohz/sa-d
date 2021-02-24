package com.ideatech.ams.account.service;

import com.ideatech.ams.account.dao.AccountPublicDao;
import com.ideatech.ams.account.dao.AccountsAllDao;
import com.ideatech.ams.account.entity.AccountPublic;
import com.ideatech.ams.account.entity.AccountsAll;
import com.ideatech.ams.customer.service.CustomerPublicService;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.common.util.DateUtil;
import com.ideatech.common.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class BatchNoticeDueChangeServiceImpl implements BatchNoticeDueChangeService {

    @Autowired
    private AccountsAllDao accountsAllDao;

    @Autowired
    private AccountPublicDao accountPublicDao;

    @Autowired
    private ConfigService configService;

    @Autowired
    private CustomerPublicService customerPublicService;

    @Override
    public void noticeDueChange() {
        Long tempAcctOverNoticeDay = configService.findOneByKey("tempAcctOverNoticeDay");
        if(tempAcctOverNoticeDay == null) {
            tempAcctOverNoticeDay = 3L;
        }

        Date nowDate = DateUtil.beginOfDate(new Date());
        Date beforeDate = DateUtil.subDays(nowDate, (int) (tempAcctOverNoticeDay + 0));

        //临时户到期日更新是否超期状态
        List<AccountsAll> accountsAllList = accountsAllDao.findAll(new Specification<AccountsAll>() {
            @Override
            public Predicate toPredicate(Root<AccountsAll> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                expressions.add(cb.isNotNull(root.get("effectiveDate")));

                return predicate;
            }
        });

        if(CollectionUtils.isNotEmpty(accountsAllList)) {
            for (AccountsAll accountsAll : accountsAllList) {
                if (accountsAll.getIsEffectiveDateOver() == null || !accountsAll.getIsEffectiveDateOver()) {
                    try {
                        Date effectiveDate = DateUtils.parse(accountsAll.getEffectiveDate(), "yyyy-MM-dd");
                        if (effectiveDate.equals(beforeDate) || effectiveDate.before(beforeDate)) {  //超期
                            accountsAll.setIsEffectiveDateOver(true);
                        } else {
                            accountsAll.setIsEffectiveDateOver(false);
                        }

                    } catch (ParseException e) {
                        log.info("effectiveDate转换出错");
                        continue;
                    }

                    accountsAllDao.save(accountsAll);
                }

            }
        }

        //法人到期证件日期是否超期字段更新
        customerPublicService.updateCustomerNoticeDue("legalDueNotice");
        //证明文件到期证件日期是否超期字段更新
        customerPublicService.updateCustomerNoticeDue("fileDueNotice");

        //设置经办人是否超期字段
        //经办人证件到期提醒
        Long operatorOverNoticeDay = configService.findOneByKey("operatorOverNoticeDay");
        if(operatorOverNoticeDay == null) {
            operatorOverNoticeDay = 3L;
        }

        Date operatorDueBeforeDate = DateUtil.subDays(nowDate, (int) (operatorOverNoticeDay + 0));
        List<AccountPublic> accountPublicList = accountPublicDao.findAll(new Specification<AccountPublic>() {
            @Override
            public Predicate toPredicate(Root<AccountPublic> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                expressions.add(cb.isNotNull(root.get("operatorIdcardDue")));

                return predicate;
            }
        });

        if(CollectionUtils.isNotEmpty(accountPublicList)) {
            for (AccountPublic accountPublic : accountPublicList) {
                if (accountPublic.getIsOperatorIdcardDue() == null || !accountPublic.getIsOperatorIdcardDue()) {

                    if (StringUtils.isNotBlank(accountPublic.getOperatorIdcardDue())) {
                        try {
                            Date operatorIdcardDueDate = DateUtils.parse(accountPublic.getOperatorIdcardDue(), "yyyy-MM-dd");
                            if (operatorIdcardDueDate.equals(operatorDueBeforeDate) || operatorIdcardDueDate.before(operatorDueBeforeDate)) {  //超期
                                accountPublic.setIsOperatorIdcardDue(true);
                            } else {
                                accountPublic.setIsOperatorIdcardDue(false);
                            }
                        } catch (ParseException e) {
                            log.error("通知提醒operatorIdcardDueDate转换异常");
                            continue;
                        }

                        accountPublicDao.save(accountPublic);
                    }

                }
            }

        }

    }

}
