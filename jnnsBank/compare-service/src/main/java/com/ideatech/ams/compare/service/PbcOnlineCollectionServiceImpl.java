package com.ideatech.ams.compare.service;

import com.ideatech.ams.compare.enums.CollectType;
import com.ideatech.ams.compare.vo.CompareCollectRecordVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Description 比对管理--人行在线采集
 * @Author wanghongjie
 * @Date 2019/2/11
 **/
public class PbcOnlineCollectionServiceImpl{
    /*public static void main(String[] args) {
        Set<CompareCollectRecordVo> compareCollectRecordVosSet = new HashSet<>();
        CompareCollectRecordVo compareCollectRecordVo = new CompareCollectRecordVo();
        compareCollectRecordVo.setAcctNo("111");
        compareCollectRecordVo.setDepositorName("111");
        compareCollectRecordVo.setRegNo("111");
        compareCollectRecordVosSet.add(compareCollectRecordVo);
        CompareCollectRecordVo compareCollectRecordVo2 = new CompareCollectRecordVo();
        compareCollectRecordVo2.setAcctNo("111");
        compareCollectRecordVo2.setDepositorName("222");
        compareCollectRecordVo2.setRegNo("222");
//        compareCollectRecordVosSet.add(compareCollectRecordVo2);
//        ArrayList<CompareCollectRecordVo> compareCollectRecordVosList1 = new ArrayList<>();
        ArrayList<CompareCollectRecordVo> compareCollectRecordVosList2 = new ArrayList<>();
//        compareCollectRecordVosList1.add(compareCollectRecordVo);
        compareCollectRecordVosList2.add(compareCollectRecordVo2);
//        System.out.println(compareCollectRecordVosList1.size());
//        Collection<CompareCollectRecordVo> compareCollectRecordVosList1 = CollectionUtils.union(compareCollectRecordVosList1, compareCollectRecordVosList2);
//        CollectionUtils.addAll(compareCollectRecordVosList1,compareCollectRecordVosList2);
//        System.out.println(compareCollectRecordVosList1.size());
        System.out.println(compareCollectRecordVosSet.size());
        CollectionUtils.addAll(compareCollectRecordVosSet,compareCollectRecordVosList2);
        System.out.println(compareCollectRecordVosSet.size());

    }*/
}
