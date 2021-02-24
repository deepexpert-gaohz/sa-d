package com.ideatech.ams.kyc.service;

import java.util.*;

import javax.transaction.Transactional;

import com.ideatech.ams.kyc.dto.EquityShareDto;
import com.ideatech.ams.kyc.dto.OutEquityShareDto;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.kyc.dao.EquityShareDao;
import com.ideatech.ams.kyc.dao.SaicInfoDao;
import com.ideatech.ams.kyc.entity.EquityShare;
import com.ideatech.ams.kyc.entity.SaicInfo;
import com.ideatech.ams.kyc.entity.po.OutEquityShareInfo;
import com.ideatech.ams.kyc.enums.SearchStatus;
import com.ideatech.ams.kyc.enums.SearchType;
import com.ideatech.ams.kyc.util.RateUtils;
import com.ideatech.common.exception.EacException;
import com.ideatech.common.util.HttpRequest;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class EquityShareServiceImpl implements EquityShareService{
	/**
     * 工商股权结构数据接口
     */
    @Value("${saic.url.equityShareList}")
    private String equityShareListUrl;

    @Autowired
    private HttpRequest httpRequest;
    
    @Autowired
    private EquityShareDao equityShareDao;
    
    @Autowired
    private SaicInfoDao saicInfoDao;
    
    @Autowired
    private SaicSearchHistoryService saicSearchHistoryService;

    @Autowired
    private SaicRequestService saicRequestService;
	
	@Override
	public JSONObject getEquityShareTreeJsonObject(String username, Long saicInfoId, String orgfullid) {
        EquityShare equityShare = getEquityShareBySaicId(username,saicInfoId, orgfullid);

        if (equityShare == null) {
            return new JSONObject();
        }

        return parseToEquityShareTreeJsonObject(equityShare);
	}


    private EquityShare getEquityShareBySaicId(String username,Long saicInfoId, String orgfullid){
        List<EquityShare> equityShareList = equityShareDao.findBySaicinfoId(saicInfoId);
        
        if(equityShareList.size()==0){
            //没有股权结构数据,则从IDP取数据

            SaicInfo saicInfo = saicInfoDao.findById(saicInfoId);
            if(saicInfo == null){
                // 抛出异常 由于查询的工商基础数据不存在
                throw new EacException("工商号不存在");
            }

            equityShareList = getEquityShareListFromIdp(saicInfoId, username,saicInfo.getName(), orgfullid);
            
            if(equityShareList == null){
                // 返回NULL 便是查询失败 让前端轮询
                return null;
            }

            //排序
            sortEquityShareList(equityShareList);

            // 企业名称+_+层级-> ID
            Map<String, Long> stringNameIdMap = new HashMap<String, Long>();
            for (int i = 0; i < equityShareList.size(); i++) {
                // 设置初始id,用在界面GRID
                EquityShare shareholdingStructure = equityShareList.get(i);
                shareholdingStructure.setNodeid(Long.valueOf(i));

                String parent = shareholdingStructure.getParent();
                // 根节点不设置父节点
                if ("ROOT".equalsIgnoreCase(parent)) {
                    shareholdingStructure.setParentnodeid(null);
                } else {
                    Long parentId = stringNameIdMap.get(parent + "_" + (shareholdingStructure.getLayer() - 1));
                    shareholdingStructure.setParentnodeid(parentId);
                }

                stringNameIdMap.put(shareholdingStructure.getName() + "_" + shareholdingStructure.getLayer(), shareholdingStructure.getNodeid());
            }

            //保存股权结构
            insertBatch(saicInfoId,equityShareList);

        }

        return createEquityShareTree(equityShareList);
    }
    

    /**
     * 从IDP获取股权结构树
     * @param name
     * @return
     */
    private List<EquityShare> getEquityShareListFromIdp(Long saicInfoId, String username,String name, String
            orgfullid){
//        log.debug("获取股权结构");
//        Map<String,String> params = new HashMap<String,String>();
//        params.put("keyWord",name);
//        String jsonResult = httpRequest.getIdpRequest(equityShareListUrl,params);
        OutEquityShareDto outEquityShareDto= saicRequestService.getOutEquityShareDto(name);

        if(outEquityShareDto !=null) {
//            OutEquityShareInfo outEquityShareInfo = JSON.parseObject(jsonResult, OutEquityShareInfo.class);
            saicSearchHistoryService.save(name, username,equityShareListUrl, SearchStatus.SUCCESS,
                    SearchType.KHJD, orgfullid);

            List<EquityShare> list = new ArrayList<EquityShare>();
            for (EquityShareDto item : outEquityShareDto.getItem()) {
                EquityShare equityShare = new EquityShare();
                BeanUtils.copyProperties(item,equityShare);
                list.add(equityShare);
            }

            return list;

        } else {
        	saicSearchHistoryService.save(name,username,equityShareListUrl, SearchStatus.FAIL,
                    SearchType.KHJD, orgfullid);

            return null;
        }
    }
    

    /**
     * 股权结构排序
     * @param equityShareList
     */
    private void sortEquityShareList(List<EquityShare> equityShareList){
        log.debug("股权结构排序");
        // 现根据根据layer排序，然后根据percent排序
        Collections.sort(equityShareList, new Comparator<EquityShare>() {
            @Override
            public int compare(EquityShare o1, EquityShare o2) {
                if (o1.getLayer() > o2.getLayer()) {
                    return 1;
                } else if (o1.getLayer() < o2.getLayer()) {
                    return -1;
                } else {
                    return Double.valueOf(o2.getPercent()).compareTo(Double.valueOf(o1.getPercent()));
                }
            }
        });
    }

    /**
     * 批量插入股权结构信息
     * @param equityShareList
     */
    public void insertBatch(Long saicInfoId,List<EquityShare> equityShareList){

        int size = equityShareList.size();
        if(size == 0){
            return;
        }

        //设置主键
        for (int i = 0; i < size; i++) {
        	equityShareList.get(i).setSaicinfoId(saicInfoId);
            equityShareDao.save(equityShareList.get(i));
        }
    }
    

    /**
     * 生成股权结构树
     * @param equityShareList
     * @return
     */
    private EquityShare createEquityShareTree(List<EquityShare> equityShareList){
        // 生成股权结构树
        log.info("开始生成股权结构树");

        // NODEID->股权结构对象
        Map<Long, EquityShare> equityShareHashMap = new HashMap<Long, EquityShare>();
        // 初始化节点树
        for (int i = 0; i < equityShareList.size(); i++) {
        	EquityShare shareholdingStructure = equityShareList.get(i);
            equityShareHashMap.put(shareholdingStructure.getNodeid(), shareholdingStructure);
        }

        // 遍历添加子节点
        for (int i = 0; i < equityShareList.size(); i++) {
        	EquityShare shareholdingStructure = equityShareList.get(i);
            if (shareholdingStructure.getParentnodeid() != null) {
            	EquityShare shareholdingStructureParent =
                        equityShareHashMap.get(shareholdingStructure.getParentnodeid());
                shareholdingStructureParent.getChilds().add(shareholdingStructure);
            }
        }

        return equityShareHashMap.get(Long.valueOf(0));
    }
    
    private JSONObject parseToEquityShareTreeJsonObject(EquityShare shareHoldingStructure) {
        JSONObject jsonObject = new JSONObject();

        if (shareHoldingStructure == null) {
            return jsonObject;
        }

        jsonObject.put("id", shareHoldingStructure.getId().toString());
        jsonObject.put("text", "股权比例-[" + RateUtils.getRateString(shareHoldingStructure.getPercent())
                + "]--" + shareHoldingStructure.getName());

        JSONArray children = new JSONArray();
        for (EquityShare child : shareHoldingStructure.getChilds()) {
            children.add(parseToEquityShareTreeJsonObject(child));
        }

        jsonObject.put("children", children);

        return jsonObject;
    }
}
