package com.ideatech.ams.kyc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import com.ideatech.ams.kyc.dto.BeneficiaryDto;
import com.ideatech.ams.kyc.dto.OutBaseAccountDto;
import com.ideatech.ams.kyc.entity.Beneficiary;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ideatech.ams.kyc.dao.BaseAccountDao;
import com.ideatech.ams.kyc.dao.SaicInfoDao;
import com.ideatech.ams.kyc.dto.BaseAccountDto;
import com.ideatech.ams.kyc.entity.BaseAccount;
import com.ideatech.ams.kyc.entity.SaicInfo;
import com.ideatech.ams.kyc.entity.po.OutBaseAccountInfo;
import com.ideatech.ams.kyc.enums.SearchStatus;
import com.ideatech.ams.kyc.enums.SearchType;
import com.ideatech.common.exception.EacException;
import com.ideatech.common.util.BeanCopierUtils;
import com.ideatech.common.util.HttpRequest;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class BaseAccountServiceImpl implements BaseAccountService{

	@Autowired
	private BaseAccountDao baseAccountDao;
    
    @Autowired
    private SaicInfoDao saicInfoDao;
	
    /**
     * 基本户履历数据存量接口
     */
    @Value("${saic.url.baseAccount}")
    private String baseAccountUrl;

    @Autowired
    private HttpRequest httpRequest;
    
    @Autowired
    private SaicSearchHistoryService saicSearchHistoryService;

    @Autowired
    private SaicRequestService saicRequestService;
    
	@Override
	public List<BaseAccountDto> getBaseAccountListBySaicInfoId(String username, Long saicInfoId, String orgfullid) {

        List<BaseAccount> baseAccountList = baseAccountDao.findBySaicinfoId(saicInfoId);
        
        if(baseAccountList.size()==0){

            SaicInfo saicInfo = saicInfoDao.findById(saicInfoId);
            if(saicInfo == null){
                // 抛出异常 由于查询的工商基础数据不存在
                throw new EacException("工商号不存在");
            }

            //从IDP获取基本户履历
            baseAccountList = getBaseAccountListFromIdp(saicInfoId, username,saicInfo.getName(), orgfullid);

            if(baseAccountList==null){
                //IDP这边没有数据,直接返回空
                return null;
            }

            insertBatch(saicInfoId,baseAccountList);
        }
        
        List<BaseAccountDto> dtoList = new ArrayList<BaseAccountDto>();
        for (BaseAccount baseAccount : baseAccountList) {
        	BaseAccountDto dto = new BaseAccountDto();
            BeanCopierUtils.copyProperties(baseAccount, dto);
            dtoList.add(dto);
		}

        return dtoList;
	}
	

    /**
     * 从IDP获取基本户履历
     * @param name
     * @return
     */
    private List<BaseAccount> getBaseAccountListFromIdp(Long saicInfoId, String username,String name, String orgfullid){
        OutBaseAccountDto outBaseAccountDto = saicRequestService.getOutBaseAccountDto(name);

        if(outBaseAccountDto != null) {

            saicSearchHistoryService.save(name, username,baseAccountUrl, SearchStatus.SUCCESS,
                    SearchType.KHJD, orgfullid);

            List<BaseAccount> list = new ArrayList<BaseAccount>();
            for (BaseAccountDto item : outBaseAccountDto.getItems()) {
                BaseAccount baseAccount = new BaseAccount();
                BeanUtils.copyProperties(item,baseAccount);
                list.add(baseAccount);
            }

            return list;

        } else {
        	saicSearchHistoryService.save(name,username,baseAccountUrl, SearchStatus.FAIL,
                    SearchType.KHJD, orgfullid);

            return null;
        }
    }
    

    /**
     * 批量插入基本户履历
     * @param baseAccountList
     */
    public void insertBatch(Long saicInfoId,List<BaseAccount> baseAccountList){

        int size = baseAccountList.size();
        if(size == 0){
            return;
        }

        //设置主键
        for (int i = 0; i < size; i++) {
            baseAccountList.get(i).setSaicinfoId(saicInfoId);
            baseAccountDao.save(baseAccountList.get(i));
        }

    }

}
