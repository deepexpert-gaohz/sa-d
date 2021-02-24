package com.ideatech.ams.account.dao.impl;

import com.ideatech.ams.account.dao.AllBillsPublicDao;
import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.enums.bill.BillFromSource;
import com.ideatech.ams.account.enums.bill.BillType;
import com.ideatech.ams.account.util.Map2DomainUtils;
import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.ams.system.user.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.apache.commons.beanutils.BeanUtils;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AllBillsPublicDaoImpl extends BaseRepositoryImpl implements AllBillsPublicDao {
	
	@Autowired
	private UserService userService;

	@Override
	public Page<AllBillsPublicDTO> findPage(String sql, Map<String, Object> param, Pageable pageable) {
		Page page = getPageResultList(sql, param, pageable);
		List<AllBillsPublicDTO> result = new ArrayList<AllBillsPublicDTO>();
		AllBillsPublicDTO allBillsPublicDTO = null;
		for (Object object : page.getContent()) {
			try {
				Map<String, String> map = (Map<String, String>) object;
				allBillsPublicDTO = (AllBillsPublicDTO) Map2DomainUtils.converter(map, AllBillsPublicDTO.class);
				if(StringUtils.isNotBlank(allBillsPublicDTO.getCreatedBy())) {
					UserDto user = userService.findById(Long.parseLong(allBillsPublicDTO.getCreatedBy()));

					allBillsPublicDTO.setCreatedBy(user.getUsername());
				}
				result.add(allBillsPublicDTO);
			} catch (Exception e) {
				LOG.error("map转换AllBillsPublicDTO错误", e);
			}
		}
		return new PageImpl<AllBillsPublicDTO>(result, pageable, page.getTotalElements());
	}

	/**
	 * 取消视图YD_PUBLIC_BILLS_ALL_V
	 * 已经废弃,AllBillsPublicService.findByBillId(Long billId);
	 * @param id
	 * @return
	 */
	@Override
	@Deprecated
	public AllBillsPublicDTO findById(Long id) {
		String sql = "select * from YD_PUBLIC_BILLS_ALL_V where yd_id = :id";
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);

		Object object = getSingleResult(sql, param);
		try {
			Map<String, String> map = (Map<String, String>) object;
			AllBillsPublicDTO allBillsPublicDTO = (AllBillsPublicDTO) Map2DomainUtils.converter(map,
					AllBillsPublicDTO.class);
//			setRelateAndPartner(allBillsPublicDTO);
			return allBillsPublicDTO;
		} catch (Exception e) {
			LOG.error("map转换AllBillsPublicDTO错误", e);
		}
		return null;
	}

	@Override
	public Long getCount(String sql, Map<String, Object> param) {
		return count(sql, param);
	}


	/**
	 * 取消视图YD_PUBLIC_BILLS_ALL_V
	 * 已经废弃，使用AccountBillsAllServiceImpl.findUpdateAcctTypeList()方法
	 * @return
	 */
	@Override
	@Deprecated
	public List<AllBillsPublicDTO> findUpdateAcctTypeList() {
		String sql = "select * from YD_PUBLIC_BILLS_ALL_V where yd_bill_type = :billType and yd_from_source = :fromSource and yd_acct_type in (:acctType)";
		List<String> acctTypeList = new ArrayList<>();
		acctTypeList.add(CompanyAcctType.tempAcct.name());
		acctTypeList.add(CompanyAcctType.specialAcct.name());

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("billType", BillType.ACCT_INIT.name());
		param.put("fromSource", BillFromSource.INIT.name());
		param.put("acctType", acctTypeList);

		List list = getResultList(sql, param);

		List<AllBillsPublicDTO> billsPublicDTOList = new ArrayList<AllBillsPublicDTO>();
		for (Object object : list) {
			try {
				Map<String, String> map = (Map<String, String>) object;
				AllBillsPublicDTO allBillsPublicDTO = (AllBillsPublicDTO) Map2DomainUtils.converter(map, AllBillsPublicDTO.class);
				billsPublicDTOList.add(allBillsPublicDTO);
			} catch (Exception e) {
				LOG.error("map转换AllBillsPublicDTO错误", e);
			}
		}
		return billsPublicDTOList;
	}


	@Override
	public Page findPageMap(String sql, Map<String, Object> param, Pageable pageable) {
		return getPageResultList(sql, param, pageable);
	}
}
