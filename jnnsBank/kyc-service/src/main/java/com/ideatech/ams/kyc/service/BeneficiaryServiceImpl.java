package com.ideatech.ams.kyc.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import com.ideatech.ams.kyc.dto.EquityShareDto;
import com.ideatech.ams.kyc.dto.OutBeneficiaryDto;
import com.ideatech.ams.kyc.entity.EquityShare;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ideatech.ams.kyc.dao.BeneficiaryDao;
import com.ideatech.ams.kyc.dao.SaicInfoDao;
import com.ideatech.ams.kyc.dto.BeneficiaryDto;
import com.ideatech.ams.kyc.entity.Beneficiary;
import com.ideatech.ams.kyc.entity.SaicInfo;
import com.ideatech.ams.kyc.entity.po.OutBeneficiaryInfo;
import com.ideatech.ams.kyc.enums.SearchStatus;
import com.ideatech.ams.kyc.enums.SearchType;
import com.ideatech.common.exception.EacException;
import com.ideatech.common.util.BeanCopierUtils;
import com.ideatech.common.util.HttpRequest;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class BeneficiaryServiceImpl implements BeneficiaryService{
	
	@Autowired
	private BeneficiaryDao beneficiaryDao;
    
    @Autowired
    private SaicInfoDao saicInfoDao;

    /**
     * 受益所有人数据存量接口
     */
    @Value("${saic.url.beneficiary}")
    private String beneficiaryUrl;

    @Autowired
    private HttpRequest httpRequest;

    @Autowired
    private SaicRequestService saicRequestService;
    
    @Autowired
    private SaicSearchHistoryService saicSearchHistoryService;
	
	@Override
	public List<BeneficiaryDto> getBeneficiaryListBySaicInfoId(String username, Long saicInfoId, String orgfullid) {
		 List<Beneficiary> beneficiaryList = getBeneficiaryListBySaicInfoIdInLocal(saicInfoId);

	        if(beneficiaryList.size()==0){
	            SaicInfo saicInfo = saicInfoDao.findById(saicInfoId);
	            if(saicInfo == null){
	                // 抛出异常 由于查询的工商基础数据不存在
	                throw new EacException("工商号不存在");
	            }
	            //从IDP获取受益人数据
                //一些企业通过该接口第一次调用时会返回null,取不到受益人数据
	            beneficiaryList = getBeneficiaryListFromIdp(saicInfoId,username,saicInfo.getName(), orgfullid);
	            if(beneficiaryList==null){
	                //IDP这边没有数据,直接返回空
	                return null;
	            }
	            insertBatch(saicInfoId,beneficiaryList);
	        }
	        //排序
	        sortBeneficiarieList(beneficiaryList);
	        
	        List<BeneficiaryDto> dtoList = new ArrayList<BeneficiaryDto>();
	        for (Beneficiary beneficiary : beneficiaryList) {
	        	BeneficiaryDto dto = new BeneficiaryDto();
	            BeanCopierUtils.copyProperties(beneficiary, dto);
	            dtoList.add(dto);
			}
	        
	        return dtoList;
	}

    @Override
    public List<BeneficiaryDto> getBeneficiaryListBySaicInfoId(String username, String orgfullid,String name) {
        List<Beneficiary> beneficiaryList = new ArrayList<>();

        //从IDP获取受益人数据
        //一些企业通过该接口第一次调用时会返回null,取不到受益人数据
        OutBeneficiaryDto outBeneficiaryDto = getBeneficiaryListFromIdp(username,name, orgfullid);

        if(outBeneficiaryDto != null && CollectionUtils.isNotEmpty(outBeneficiaryDto.getFinalBeneficiary())){
            for (BeneficiaryDto item : outBeneficiaryDto.getFinalBeneficiary()) {
                Beneficiary beneficiary = new Beneficiary();
                BeanUtils.copyProperties(item,beneficiary);
                beneficiaryList.add(beneficiary);
            }
        }

        if(CollectionUtils.isEmpty(beneficiaryList)){
            //IDP这边没有数据,直接返回空
            return null;
        }
//      insertBatch(saicInfoId,beneficiaryList);
        //排序
        sortBeneficiarieList(beneficiaryList);

        List<BeneficiaryDto> dtoList = new ArrayList<BeneficiaryDto>();
        for (Beneficiary beneficiary : beneficiaryList) {
            BeneficiaryDto dto = new BeneficiaryDto();
            BeanCopierUtils.copyProperties(beneficiary, dto);
            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public OutBeneficiaryDto getOutBeneficiaryDtoBySaicInfoId(String username, String orgfullid, String name) {

	    List<Beneficiary> beneficiaryList = new ArrayList<>();
        //从IDP获取受益人数据
        //一些企业通过该接口第一次调用时会返回null,取不到受益人数据
        OutBeneficiaryDto outBeneficiaryDto = getBeneficiaryListFromIdp(username,name, orgfullid);

        if(outBeneficiaryDto != null && StringUtils.isBlank(outBeneficiaryDto.getReason()) && CollectionUtils.isNotEmpty(outBeneficiaryDto.getFinalBeneficiary())){
            for (BeneficiaryDto item : outBeneficiaryDto.getFinalBeneficiary()) {
                Beneficiary beneficiary = new Beneficiary();
                BeanUtils.copyProperties(item,beneficiary);
                beneficiaryList.add(beneficiary);
            }
            sortBeneficiarieList(beneficiaryList);
            List<BeneficiaryDto> dtoList = new ArrayList<BeneficiaryDto>();
            for (Beneficiary beneficiary : beneficiaryList) {
                BeneficiaryDto dto = new BeneficiaryDto();
                BeanCopierUtils.copyProperties(beneficiary, dto);
                dtoList.add(dto);
            }
            outBeneficiaryDto.setFinalBeneficiary(dtoList);
            return outBeneficiaryDto;
        }
        return outBeneficiaryDto;
    }

    @Override
    public void insertBeneficiary(Long saicInfoId, List<BeneficiaryDto> beneficiaryList) {
        int size = beneficiaryList.size();
        //设置主键
        for (int i = 0; i < size; i++) {
            Beneficiary beneficiary = new Beneficiary();
            BeanCopierUtils.copyProperties(beneficiaryList.get(i), beneficiary);
            beneficiaryList.get(i).setSaicinfoId(saicInfoId);
            beneficiaryDao.save(beneficiary);
        }
    }

    /**
     * 根据工商ID在当前数据库中获取受益人
     * @param saicInfoId
     * @return
     */
    public List<Beneficiary> getBeneficiaryListBySaicInfoIdInLocal(Long saicInfoId){

        List<Beneficiary> beneficiaryList = beneficiaryDao.findBySaicinfoId(saicInfoId);

        //排序
        sortBeneficiarieList(beneficiaryList);

        return beneficiaryList;
    }
    

    /**
     * 受益人排序
     * @param beneficiarieList
     */
    private void sortBeneficiarieList(List<Beneficiary> beneficiarieList){
        //按照百分比倒序
        Collections.sort(beneficiarieList, new Comparator<Beneficiary>() {
            @Override
            public int compare(Beneficiary o1, Beneficiary o2) {
                try{
                    Double o1Percent = Double.valueOf(o1.getCapitalpercent());
                    Double o2Percent = Double.valueOf(o2.getCapitalpercent());
                    return o2Percent.compareTo(o1Percent);
                } catch (NumberFormatException e){
                    log.error("转换百分比出错了，错误原因是： ", e);
                    return o2.getCapitalpercent().compareTo(o1.getCapitalpercent());
                }
            }
        });
    }
    

    /**
     * 从IDP获取受益人
     * @param name
     * @return
     */
    private List<Beneficiary> getBeneficiaryListFromIdp(Long saicInfoId, String username,String name, String
            orgfullid){

        OutBeneficiaryDto outBeneficiaryDto = saicRequestService.getOutBeneficiaryDto(name);

        if(outBeneficiaryDto != null && StringUtils.isBlank(outBeneficiaryDto.getReason())) {
//            OutBeneficiaryInfo outBeneficiaryInfo = JSON.parseObject(jsonResult, OutBeneficiaryInfo.class);

            saicSearchHistoryService.save(name, username,beneficiaryUrl, SearchStatus.SUCCESS,
                    SearchType.KHJD, orgfullid);

            List<Beneficiary> list = new ArrayList<Beneficiary>();
            if(CollectionUtils.isNotEmpty(outBeneficiaryDto.getFinalBeneficiary())){
                for (BeneficiaryDto item : outBeneficiaryDto.getFinalBeneficiary()) {
                    Beneficiary beneficiary = new Beneficiary();
                    BeanUtils.copyProperties(item,beneficiary);
                    list.add(beneficiary);
                }
            }
            return list;

        } else {
        	saicSearchHistoryService.save(name,username,beneficiaryUrl, SearchStatus.FAIL,
                    SearchType.KHJD, orgfullid);

            return null;
        }
    }

    /**
     * 从IDP获取受益人
     * @param name
     * @return
     */
    private OutBeneficiaryDto getBeneficiaryListFromIdp(String username,String name, String orgfullid){

        OutBeneficiaryDto outBeneficiaryDto = saicRequestService.getOutBeneficiaryDto(name);

        //当Reason有值时，说明收益人需要时间计算
        if(outBeneficiaryDto != null && StringUtils.isEmpty(outBeneficiaryDto.getReason())) {
            saicSearchHistoryService.save(name, username,beneficiaryUrl, SearchStatus.SUCCESS,
                    SearchType.KHJD, orgfullid);
//            List<Beneficiary> list = new ArrayList<Beneficiary>();
//            for (BeneficiaryDto item : outBeneficiaryDto.getFinalBeneficiary()) {
//                Beneficiary beneficiary = new Beneficiary();
//                BeanUtils.copyProperties(item,beneficiary);
//                list.add(beneficiary);
//            }
        } else {
            saicSearchHistoryService.save(name,username,beneficiaryUrl, SearchStatus.FAIL,
                    SearchType.KHJD, orgfullid);
        }
        return outBeneficiaryDto;
    }


    /**
     * 批量插入受益人信息
     * @param beneficiaryList
     */
    public List<BeneficiaryDto> insertBatch(Long saicInfoId,List<Beneficiary> beneficiaryList){
    	List<BeneficiaryDto> list = new ArrayList<BeneficiaryDto>();
        int size = beneficiaryList.size();
        if(size == 0){
            return list;
        }

        //设置主键
        for (int i = 0; i < size; i++) {
        	beneficiaryList.get(i).setSaicinfoId(saicInfoId);
        	beneficiaryDao.save(beneficiaryList.get(i));
        	BeneficiaryDto beneficiaryDto = new BeneficiaryDto();
            BeanCopierUtils.copyProperties(beneficiaryList.get(i), beneficiaryDto);
            list.add(beneficiaryDto);
        }
        return list;
    }
}
