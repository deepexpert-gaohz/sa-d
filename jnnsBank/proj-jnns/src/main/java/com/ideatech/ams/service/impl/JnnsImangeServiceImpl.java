package com.ideatech.ams.service.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.dao.AccountsBillsAllDao;
import com.ideatech.ams.dao.JnnsImageAllDao;
import com.ideatech.ams.image.utils.ImageUtils;
import com.ideatech.ams.service.JnnsImageService;
import com.ideatech.common.utils.StringUtils;
import com.sunyard.client.SunEcmClientApi;
import com.sunyard.client.bean.*;
import com.sunyard.client.impl.SunEcmClientSocketApiImpl;


import com.sunyard.dmserver.bean.BatchBean;
import com.sunyard.dmserver.bean.BatchFileBean;
import com.sunyard.dmserver.bean.FileBean;
import com.sunyard.dmserver.bean.HeightQuery;
import com.sunyard.util.TransOptionKey;
import com.sunyard.ws.utils.XMLUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ideatech.ams.utils.DateUtils.getNowDateShort;

@Service
public class JnnsImangeServiceImpl implements JnnsImageService {

    @Autowired
    private JnnsImageAllDao jnnsImageAllDao;


    private static final Logger log = LoggerFactory.getLogger(JnnsImangeServiceImpl.class);

    @Value("${ams.image.path}")
    private String filePath;
    //集中作业 影像模型
    @Value("${jnns.image.Model_Code}")
    private String modelCode;
    //本地授权 影像模型
    @Value("${jnns.image.Model_Codel}")
    private String modelCodel;
    @Value("${jnns.image.filePartName}")
    private String filePartName;
    @Value("${jnns.image.groupName}")
    private String groupName;
    @Value("${jnns.image.ip}")
    private String ip;
    @Value("${jnns.image.port}")
    private int socketPort;
    @Value("${jnns.image.userName}")
    private String userName;
    @Value("${jnns.image.passWord}")
    private String passWord;
    @Value("${jnns.image.dmsName}")
    private String dmsName;
    @Value("${jnns.image.fileFormat}")
    private String fileFormat;

    String docDate = getNowDateShort("yyyyMMdd");


    /**
     * 影像上传到影像平台
     *
     * @param list 影像图片地址，1个图片一个批次号
     * @return 影像批次号
     * @throws Exception
     */
    public String uploadDsFile(List<String> list) throws Exception {
        int AMOUNT = 0;
        String uploadReturnMessage;
        String[] filename;
        String filePath = createFilePath();
        File imageFolder = new File(filePath);//根据路径知道文件
        if (!imageFolder.exists()) {
            imageFolder.mkdirs();
        }
        File[] folderArray = imageFolder.listFiles();
        log.info("上传影像文件数量：" + folderArray.length + ",前台传输的文件数量：" + list.size());
        for (int i = 0; i < list.size(); i++) {
            if (!folderArray[i].isDirectory()) {
                filename = StringUtils.substringAfterLast(list.get(0), filePath).split("\\.");
                log.info("文件名：" + ArrayUtils.toString(list.get(i)));
                if (filename != null && filename.length > 1) {
                    if (filename[1].equals(fileFormat)) {
                        AMOUNT++;
                    }
                }
            }
        }
        //未存在符合的上传图片
        if (AMOUNT < 1) {
            throw new Exception("影像上传失败，未存在符合的上传文件");
        }
        SunEcmClientApi api = new SunEcmClientSocketApiImpl(ip, socketPort);
        ClientBatchBean batchBean = new ClientBatchBean();
        batchBean.setUser(userName);
        batchBean.setPassWord(passWord);
        batchBean.setBreakPoint(false);
        batchBean.setModelCode(modelCode);// 系统标识
        batchBean.setOwnMD5(false);
        // ---------------文件参数----------------
        ClientBatchIndexBean batchIndexBean = new ClientBatchIndexBean();
        batchIndexBean.addCustomMap("BUSI_START_DATE", docDate);// 业务日期
        log.info("上传业务日期"+docDate);
        batchIndexBean.addCustomMap("AMOUNT", String.valueOf(AMOUNT));// 文件总数
        batchIndexBean.addCustomMap("BUSI_SERIAL_NO", String.valueOf(UUID.randomUUID()));// 业务流水号
        batchIndexBean.addCustomMap("BUSI_FILE_SCANUSER", "TZ");// 扫描人
        //batchIndexBean.setVersion("1");
        // ---------------部件参数---------------
        ClientBatchFileBean batchFileBean = new ClientBatchFileBean();
        batchFileBean.setFilePartName(filePartName);
        // -------------------------------
        for (int i = 0; i < AMOUNT; i++) {// 循环文件数量，每个都需要加到部件信息中
            ClientFileBean bean = new ClientFileBean();
            bean.setFileFormat("jpg");// 文件类型
            bean.setFileName(list.get(i));
            bean.addOtherAtt("BUSI_FILE_PAGENUM", "1");
            bean.addOtherAtt("BUSI_FILE_TYPE", "");
            batchFileBean.addFile(bean);
        }
        batchBean.addDocument_Object(batchFileBean);
        batchBean.setIndex_Object(batchIndexBean);
        try {
            uploadReturnMessage = api.upload(batchBean, dmsName);
            log.info("上传文件后，影像接口返回内容：-----------------" + uploadReturnMessage);
        } catch (Exception e) {
            log.error("上传方法异常", e);
            throw new Exception("影像上传访问异常");
        }
        if (uploadReturnMessage.startsWith("SUCCESS")) {
            String imageCode = uploadReturnMessage.substring(13, uploadReturnMessage.length());
            log.info("上传成功后影像批次号：" + imageCode);
            return imageCode;
        } else {
            throw new Exception("影像上传失败，影像接口报文返回失败");
        }
    }

    /**
     * * 影像系统高级查询
     * * 根据 影像流水号查找影像批次号
     *
     * @param imageBillNo 影像流水号
     *
     * @return
     */
    public String heightQueryExample(String imageBillNo) {

        log.info("高级查询第一次进入==========================");

        String busiDate = "";
        String returnImageCode = "";
        String movieCode = "";
        SunEcmClientApi api = new SunEcmClientSocketApiImpl(ip, socketPort);
        ClientHeightQuery heightQuery1 = new ClientHeightQuery();
        heightQuery1.setUserName(userName);
        heightQuery1.setPassWord(passWord);
        heightQuery1.setLimit(5);
        heightQuery1.setPage(1);
        heightQuery1.setModelCode(modelCode);// 系统标识

        heightQuery1.addCustomAtt("BUSI_SERIAL_NO", imageBillNo);// 业务流水号，对应的是业务系统的索引（例如客户号）
        try {
            String resultMsg = api.heightQuery(heightQuery1, "group71_88");// 得出contentId和BUSI_START_DATE

            String regx = "CONTENT_ID=\"(.*?)\"";
            String regx1 = "BUSI_START_DATE" + ">(.*?)</" + "BUSI_START_DATE" + ">";
            Pattern pattern = Pattern.compile(regx);
            Pattern pattern1 = Pattern.compile(regx1);
            Matcher m = pattern1.matcher(resultMsg);
            if (m.find()){
                returnImageCode = m.group(1);
                log.info("-----高级查询到的影像批次号：" + returnImageCode);
            }
            Matcher m1 = pattern.matcher(resultMsg);
            if (m1.find()) {
                busiDate = m1.group(1);
                log.info("-----高级查询到的影业务日期：" + busiDate);
            }
            returnImageCode = returnImageCode.trim();
            String str2 = "";
            if (returnImageCode != null && !"".equals(returnImageCode)) {
                for (int i = 0; i < returnImageCode.length(); i++) {
                    if (returnImageCode.charAt(i) >= 48 && returnImageCode.charAt(i) <= 57) {
                        str2 += returnImageCode.charAt(i);
                    }
                }

            }
            movieCode = str2 + "," + busiDate;
        } catch (Exception e) {
            log.error("根据影像流水号查询影像批次号异常", e);
        }
        return movieCode;
    }

    @Override
    public void deleteByBillId(String acctBillsId) {
        jnnsImageAllDao.deleteAllByBillId(acctBillsId);

    }

    @Override
    public String heightQueryExample1(String imageBillNo) {

        log.info("高级查询第er次进入==========================");

        String busiDate = "";
        String returnImageCode = "";
        String movieCode = "";
        SunEcmClientApi api = new SunEcmClientSocketApiImpl(ip, socketPort);
        ClientHeightQuery heightQuery1 = new ClientHeightQuery();
        heightQuery1.setUserName(userName);
        heightQuery1.setPassWord(passWord);
        heightQuery1.setLimit(5);
        heightQuery1.setPage(1);
        heightQuery1.setModelCode(modelCodel);// 系统标识

        heightQuery1.addCustomAtt("BUSI_SERIAL_NO", imageBillNo);// 业务流水号，对应的是业务系统的索引（例如客户号）
        try {
            String resultMsg = api.heightQuery(heightQuery1, "group71_88");// 得出contentId和BUSI_START_DATE
            String regx = "CONTENT_ID=\"(.*?)\"";
            String regx1 = "BUSI_START_DATE" + ">(.*?)</" + "BUSI_START_DATE" + ">";
            Pattern pattern = Pattern.compile(regx);
            Pattern pattern1 = Pattern.compile(regx1);
            Matcher m = pattern1.matcher(resultMsg);
            if (m.find()){
                returnImageCode = m.group(1);
                log.info("-----高级查询到的影像批次号：" + returnImageCode);
            }
            Matcher m1 = pattern.matcher(resultMsg);
            if (m1.find()) {
                busiDate = m1.group(1);
                log.info("-----高级查询到的影业务日期：" + busiDate);
            }
            returnImageCode = returnImageCode.trim();
            String str2 = "";
            if (returnImageCode != null && !"".equals(returnImageCode)) {
                for (int i = 0; i < returnImageCode.length(); i++) {
                    if (returnImageCode.charAt(i) >= 48 && returnImageCode.charAt(i) <= 57) {
                        str2 += returnImageCode.charAt(i);
                    }
                }

            }
            movieCode = str2 + "," + busiDate;
            log.info("高级查询得到的影像批次号以及业务日期===="+movieCode);
        } catch (Exception e) {
            log.error("根据影像流水号查询影像批次号异常", e);
        }
        return movieCode;

    }

    @Override
    public String heightQueryExamplSupper(String imageBillNo) {


        String queryExample = heightQueryExample(imageBillNo);

        String trim = queryExample.trim();


        if (",".equals(trim)){

             queryExample = heightQueryExample1(imageBillNo);

        }

        return queryExample;
    }

    @Override
    public String query1(String imageCode, String busiStartDate) {

        String resultStr = "";
        String resultMsg="";
        log.info("查询方法进入---------------------");
        SunEcmClientApi api = new SunEcmClientSocketApiImpl(ip, socketPort);
        ClientBatchBean clientBatchBean = new ClientBatchBean();

        clientBatchBean.setModelCode(modelCodel);

        clientBatchBean.setUser(userName);
        clientBatchBean.setPassWord(passWord);
        clientBatchBean.getIndex_Object().setContentID(imageCode);
        clientBatchBean.getIndex_Object().addCustomMap("BUSI_START_DATE", busiStartDate);
        clientBatchBean.setDownLoad(true);
        StringBuffer urlStr = new StringBuffer("");
        try {
            resultMsg = api.queryBatch(clientBatchBean, dmsName);// 拿到影像系统返回的影像url和相关的影像信息
            log.info("resultMsg" + resultMsg);
            String batchStr = resultMsg.split(TransOptionKey.SPLITSYM)[1];
            log.info("batchStr" + batchStr);
            List<BatchBean> batchBeans = XMLUtil.xml2list(XMLUtil.removeHeadRoot(batchStr), BatchBean.class);
            log.info("batchBeans" + (batchBeans != null ? batchBeans.size() : 0));
            for (BatchBean batchBean : batchBeans) {
                List<BatchFileBean> fileBeans = batchBean.getDocument_Objects();
                for (BatchFileBean batchFileBean : fileBeans) {
                    List<FileBean> files = batchFileBean.getFiles();
                    for (FileBean fileBean : files) {
                        urlStr.append(fileBean.getUrl()).append(",");// 影像url,定义一个List，用于存放多个url
                    }
                }
            }
        } catch (Exception e) {
            log.error("影像接口查询异常", e);
        }
        resultStr = urlStr.toString();
        if (StringUtils.isNotEmpty(resultStr)) {
            resultStr = StringUtils.substringBeforeLast(resultStr, ",");
        }

        return resultStr;
    }


    /**
     * 根据影像批次号、交易日期和 影像模型查询影像文件url
     *
     * @param imageCode     影像皮抄
     * @param busiStartDate 影像交易日期 ，yyyyMMdd
     * @param
     * @return 影像url ，用逗号分隔
     * @throws Exception
     */
    public String query(String imageCode, String busiStartDate) throws Exception {
        String resultStr = "";
        String resultMsg="";
        log.info("查询方法进入---------------------");
        SunEcmClientApi api = new SunEcmClientSocketApiImpl(ip, socketPort);
        ClientBatchBean clientBatchBean = new ClientBatchBean();

            clientBatchBean.setModelCode(modelCode);

        clientBatchBean.setUser(userName);
        clientBatchBean.setPassWord(passWord);
        clientBatchBean.getIndex_Object().setContentID(imageCode);
        clientBatchBean.getIndex_Object().addCustomMap("BUSI_START_DATE", busiStartDate);
        clientBatchBean.setDownLoad(true);
        StringBuffer urlStr = new StringBuffer("");
        try {
             resultMsg = api.queryBatch(clientBatchBean, dmsName);// 拿到影像系统返回的影像url和相关的影像信息
            log.info("resultMsg" + resultMsg);
            String batchStr = resultMsg.split(TransOptionKey.SPLITSYM)[1];
            log.info("batchStr" + batchStr);
            List<BatchBean> batchBeans = XMLUtil.xml2list(XMLUtil.removeHeadRoot(batchStr), BatchBean.class);
            log.info("batchBeans" + (batchBeans != null ? batchBeans.size() : 0));
            for (BatchBean batchBean : batchBeans) {
                List<BatchFileBean> fileBeans = batchBean.getDocument_Objects();
                for (BatchFileBean batchFileBean : fileBeans) {
                    List<FileBean> files = batchFileBean.getFiles();
                    for (FileBean fileBean : files) {
                        urlStr.append(fileBean.getUrl()).append(",");// 影像url,定义一个List，用于存放多个url
                    }
                }
            }
        } catch (Exception e) {
            log.error("影像接口查询异常", e);
        }
        resultStr = urlStr.toString();
        if (StringUtils.isNotEmpty(resultStr)) {
            resultStr = StringUtils.substringBeforeLast(resultStr, ",");
        }

        return resultStr;
    }


    /**
     * 影像地址（用逗号分隔）格式转换成json,传递给页面
     *
     * @param imagePath 影像url，用都好分隔
     * @return json格式，没有影像图片，则使用本地默认图片
     */
    public JSONArray formatImageJson(String imagePath) {
        List<String> imageList = new ArrayList<String>();
        if (StringUtils.isNotEmpty(imagePath)) {
            String[] list = imagePath.split(",");
            for (int i = 0; i < list.length; i++) {
                if (list[i].contains("http")) {
                    imageList.add(list[i]);
                } else {
                    File file = new File(list[i]);
                    imageList.add(file.getPath());
                }
            }
        } else {
            imageList.add("../ag/jnnsImage/image.jpg");
        }
        JSONArray arr = new JSONArray();
        Map<String, JSONArray> map = new HashMap();
        for (String str : imageList) {
            if (StringUtils.isNotBlank(str)) {
                JSONObject j = new JSONObject();
                j.put("imgUrl", str);
                if (map.containsKey(str)) {
                    map.get(str).add(j);
                } else {
                    JSONArray pathJsonArr = new JSONArray();
                    pathJsonArr.add(j);
                    map.put(str, pathJsonArr);
                }
            }
        }
        int id = 1;
        for (String docName : map.keySet()) {
            JSONObject json = new JSONObject();
            json.put("type", docName);
            json.put("list", map.get(docName));
            json.put("url", "");
            json.put("id", id);
            for (String str1 : imageList) {
                if (StringUtils.isNotBlank(str1)) {
                    json.put("name", "");
                    json.put("no", "");
                    json.put("date", "");
                    break;
                }
            }
            arr.add(json);
            id++;
        }
        return arr;
    }

    /**
     * 影像文件导入后，上传到服务进行保存
     *
     * @param is
     * @param filename    文件明后才能
     * @param acctBillsId 业务流水Id
     * @return
     */
    public String uploadImage(InputStream is, String filename, String acctBillsId) {
        String suffixFilename = StringUtils.substringAfterLast(filename, ".");
        String url = this.createFilePath() + "/" + filename;
        OutputStream os = new ByteArrayOutputStream();
        File file = new File(this.createFilePath() + acctBillsId);
        if (!file.exists()) {
            log.info("开始创建影像文件夹：" + this.createFilePath());
            file.mkdirs();
        }

        byte[] byteArray = new byte[0];

        try {
            byteArray = IOUtils.toByteArray(is);
        } catch (Exception var11) {
            log.error("传输文件出现异常", var11);
        }

        ImageUtils.compressImage(byteArray, os, suffixFilename);
        File finalFile = new File(url);

        try {
            FileUtils.writeByteArrayToFile(finalFile, ((ByteArrayOutputStream) os).toByteArray());
        } catch (Exception var10) {
            log.error("影像保存异常", var10);
        }

        return url;
    }

    private String createFilePath() {
        String path = this.filePath + "/" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd");
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        return path;
    }

    public String queryYiQiTong(String imageBillno){
        String urlStr=null;
        String urlResult=null;
        SunEcmClientApi api = new SunEcmClientSocketApiImpl(ip, socketPort);
        //--------以下为高级查询部分--------
        ClientHeightQuery heightQuery1 = new ClientHeightQuery();
        // 必要信息,设置登录名+登录密码
        heightQuery1.setUserName(userName);
        heightQuery1.setPassWord(passWord);
        // 必要信息,设置每页显示最大的行数
        heightQuery1.setLimit(5);
        // 必要信息,设置显示的页数
        heightQuery1.setPage(1);
        // 必要信息,设置内容模型名
        heightQuery1.setModelCode(modelCodel);
        // 必要信息,设置影像流水号
        heightQuery1.addCustomAtt("BUSI_SERIAL_NO",imageBillno);

        try {
            // 调用高级查询接口
            String resultMsg = api.heightQuery(heightQuery1, groupName);
            log.info("api高级查询:resultMsg[ " + resultMsg+" ]");

            String batchStr = resultMsg.split(TransOptionKey.SPLITSYM)[1];
            HeightQuery h1 = XMLUtil.xml2Bean(XMLUtil.removeHeadRoot(batchStr), HeightQuery.class);
            if(h1.getIndexBeans()!=null){
                // 影像批次号
                String contentIDQuery = h1.getIndexBeans().get(0).getContentID();
                // 影像业务开始日期
                String busiStartDateQuery = h1.getIndexBeans().get(0).getCustomMap().get("BUSI_START_DATE");
                // 该批次最大版本号
                String maxVersionQuery = h1.getIndexBeans().get(0).getMaxVersion();
                log.info("api高级查询1：CONTENT_ID[ "+contentIDQuery+" ] BUSI_START_DATE["+busiStartDateQuery+"] MAX_VERSION["+maxVersionQuery+"]");

                //--------以下为查询批次部分--------
                ClientBatchBean clientBatchBean = new ClientBatchBean();
                // 必要信息，设置内容模型
                clientBatchBean.setModelCode(modelCodel);
                // 必要信息，设置登录名+登录密码
                clientBatchBean.setUser(userName);
                clientBatchBean.setPassWord(passWord);
                // 必要信息，需要查询的批次号
                clientBatchBean.getIndex_Object().setContentID(contentIDQuery);
                // 必要信息，此批次的业务开始时间(必要)
                clientBatchBean.getIndex_Object().addCustomMap("BUSI_START_DATE", busiStartDateQuery);
                // 必要信息，填写最大版本
                clientBatchBean.getIndex_Object().setVersion(maxVersionQuery);

                // 调用查询批次接口
                String queryMessage = api.queryBatch(clientBatchBean, groupName);
                log.info("api查询批次:[ "+queryMessage+" ]");

                String batchStr2 = queryMessage.split(TransOptionKey.SPLITSYM)[1];
                List<BatchBean> batchBeans = XMLUtil.xml2list(XMLUtil.removeHeadRoot(batchStr2),BatchBean.class);
                for (BatchBean batchBean : batchBeans) {
                    List<BatchFileBean> fileBeans = batchBean.getDocument_Objects();
                    for (BatchFileBean batchFileBean : fileBeans) {
                        List<FileBean> files = batchFileBean.getFiles();
                        for (FileBean fileBean : files) {
                            // 图片URL显示

                            urlStr = fileBean.getUrl();
                            // 文件ID
                            String file_no = fileBean.getFileNO();
                            // 文件格式
                            String file_format = fileBean.getFileFormat();
                            // 文件页码（IAS属性）
                            String busi_file_pagenum = fileBean.getOtherAtt().get("BUSI_FILE_PAGENUM");
                            // 文件类型条码（IAS属性）
                            String busi_file_type = fileBean.getOtherAtt().get("BUSI_FILE_TYPE");
                            log.info("api查询批次2：图片URL["+urlStr+"] 文件ID["+file_no+"] 文件格式["+file_format+	"] 文件页码["+busi_file_pagenum+"] 文件类型条码["+busi_file_type+"]");

                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        urlResult=urlStr.toString();
        log.info("返回的url压缩包=========="+urlResult);
        return urlResult;
    }



}

