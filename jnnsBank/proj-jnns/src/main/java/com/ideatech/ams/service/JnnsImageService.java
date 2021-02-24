package com.ideatech.ams.service;

import com.alibaba.fastjson.JSONArray;

import java.io.InputStream;
import java.util.List;

/**
 * 江南农商影像处理类
 *
 * @auther ideatech
 * @create 2019-04-25 7:19 PM
 **/
public interface JnnsImageService {

    String uploadDsFile(List<String> list)throws Exception ;

    String query(String imageCode, String busiStartDate)throws Exception;

    JSONArray formatImageJson(String imgPath);

    String uploadImage(InputStream is, String filename, String acctBillsId);

    String  heightQueryExample(String imageBillNo);

    void deleteByBillId(String acctBillsId);

    String heightQueryExample1(String imageBillNo);


    String heightQueryExamplSupper(String imageBillNo);

    String query1(String imageCode, String busiStartDate);

    String queryYiQiTong(String imageBillno);
//    String query(String imageCode,String busiStartDate)throws Exception;
}
