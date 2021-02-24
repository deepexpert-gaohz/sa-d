package com.ideatech.ams.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.account.dao.AccountsAllDao;
import com.ideatech.ams.account.dao.HeGuiYuJingAllDao;
import com.ideatech.ams.account.entity.AccountsAll;
import com.ideatech.ams.account.entity.HeGuiYuJingAll;
import com.ideatech.ams.customer.dao.CustomerPublicDao;
import com.ideatech.ams.customer.dao.CustomerPublicLogDao;
import com.ideatech.ams.customer.entity.CustomerPublicLog;
import com.ideatech.ams.dao.BreakLawDao;
import com.ideatech.ams.dao.DeabbeatDao;
import com.ideatech.ams.dao.OwingDao;
import com.ideatech.ams.dto.SaicQuery.*;
import com.ideatech.ams.dto.esb.*;

import com.ideatech.ams.service.EsbService;
import com.ideatech.ams.service.JnnsSaicTestService;
import com.ideatech.ams.utils.HttpIntefaceUtils;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import static com.ideatech.ams.utils.DateUtils.getNowDateShort;

@Service
@Slf4j
public class JnnsSaicTestServiceImpl implements JnnsSaicTestService {

    @Autowired
    private EsbService esbService;

    @Value("${jnns.esb.url}")
    private String esbUrl;


    @Autowired
    private DeabbeatDao deabbeatDao;

    @Autowired
    private CustomerPublicDao customerPublicDao;

    @Autowired
    private OwingDao owingDao;

    @Autowired
    private BreakLawDao breakLawDao;

    @Autowired
    private CustomerPublicLogDao customerPublicLogDao;

    @Autowired
    private AccountsAllDao accountsAllDao;

    @Autowired
    private HeGuiYuJingAllDao heGuiYuJingAllDao;





    @Override
    public SaicQuery doQuery() {
        log.debug("-----------------------------汇法数据接口进入--------------------------------------");
        JSONObject jsonObject = new JSONObject();

        List<AccountsAll> list = accountsAllDao.findAll();
        for(int j = 0;j < list.size();j++) {
            log.info(list.get(j).getAcctName());


            String name = null;
            try {
                name = URLEncoder.encode(list.get(j).getAcctName(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            String jsonStr = jsonObject.toJSONString(getRequestBody("P00001017181", "ShengLianSheCiNew", "shenglianshe_ci", "2", name, "", "001", "57182"));
            String jsonStr2 = jsonStr.replaceAll("\"body\"", "\"Body\"").replaceAll("\"header\"", "\"Header\"").replaceAll("\"transaction\"", "\"Transaction\"");
            //log.info("汇法接口请求报文----------------" + jsonStr2);
            String httpIntefaceUtils = HttpIntefaceUtils.send(esbUrl, jsonStr2);
            String ResponseCustom = esbService.getResponseCustom(httpIntefaceUtils);
            //log.info("汇法接口提取后的报文---------------- " + ResponseCustom);

            //解析返回json数据
            JSONObject jb = JSONObject.parseObject(ResponseCustom);
            //[{}]   数组格式

            /*----------------------违法----------------------*/
			//log.info(jb.getString("CI_XZWFJL"));
            if (StringUtils.isNotBlank(jb.getString("CI_XZWFJL")) && !jb.getString("CI_XZWFJL").equals("[]")) {
                String[] str = jb.getString("CI_XZWFJL").split("}},");
                //数组存在多个json对象
                if (str.length > 1) {
                    for (int i = 0; i < str.length; i++) {
                        if (i == 0) {
                            String first = new StringBuffer(str[i]).append("}}").substring(1, new StringBuffer(str[i]).append("}}").length());
                            JSONObject jbb = JSONObject.parseObject(first);
                            doBreak(jbb);
                        } else if (i == str.length - 1) {
                            String last = str[i].substring(0, str[i].length() - 1);
                            JSONObject jbb = JSONObject.parseObject(last);
                            doBreak(jbb);
                        } else {
                            String centre = new StringBuffer(str[i]).append("}}").toString();
                            JSONObject jbb = JSONObject.parseObject(centre);
                            doBreak(jbb);
                        }
                    }
                } else {
                    String array = jb.getString("CI_XZWFJL").substring(1, jb.getString("CI_XZWFJL").length() - 1);
                    JSONObject jbb = JSONObject.parseObject(array);
                    doBreak(jbb);
                }
            }


            /*-----------------------老赖---------------------------*/
            	//log.info(jb.getString("CI_SXLLMD"));
            if (StringUtils.isNotBlank(jb.getString("CI_SXLLMD")) && !jb.getString("CI_SXLLMD").equals("[]")) {

                String[] str = jb.getString("CI_SXLLMD").split("}},");
                if (str.length > 1) {
                    for (int i = 0; i < str.length; i++) {
                        if (i == 0) {
                            String first = new StringBuffer(str[i]).append("}}").substring(1, new StringBuffer(str[i]).append("}}").length());
                            JSONObject jbb = JSONObject.parseObject(first);
                            doDeabb(jbb);
                        } else if (i == str.length - 1) {
                            String last = str[i].substring(0, str[i].length() - 1);
                            JSONObject jbb = JSONObject.parseObject(last);
                            doDeabb(jbb);
                        } else {
                            String centre = new StringBuffer(str[i]).append("}}").toString();
                            JSONObject jbb = JSONObject.parseObject(centre);
                            doDeabb(jbb);
                        }
                    }
                } else {
                    String array = jb.getString("CI_SXLLMD").substring(1, jb.getString("CI_SXLLMD").length() - 1);
                    JSONObject jbb = JSONObject.parseObject(array);
                    doDeabb(jbb);
                }
            }
            /*---------------------欠税人名单-------------------------*/
           //	log.info(jb.getString("CI_QSMD"));
            if (StringUtils.isNotBlank(jb.getString("CI_QSMD")) && !jb.getString("CI_QSMD").equals("[]")) {
                String[] str = jb.getString("CI_QSMD").split("}},");
                if (str.length > 1) {
                    for (int i = 0; i < str.length; i++) {
                        if (i == 0) {
                            String first = new StringBuffer(str[i]).append("}}").substring(1, new StringBuffer(str[i]).append("}}").length());
                            JSONObject jbb = JSONObject.parseObject(first);
                            doMain(jbb);
                        } else if (i == str.length - 1) {
                            String last = str[i].substring(0, str[i].length() - 1);
                            JSONObject jbb = JSONObject.parseObject(last);
                            doMain(jbb);
                        } else {
                            String centre = new StringBuffer(str[i]).append("}}").toString();
                            JSONObject jbb = JSONObject.parseObject(centre);
                            doMain(jbb);
                        }
                    }
                } else {
                    String array = jb.getString("CI_QSMD").substring(1, jb.getString("CI_QSMD").length() - 1);
                    JSONObject jbb = JSONObject.parseObject(array);
                    doMain(jbb);
                }

            }
        }



        return null;
    }




    /**
     * 违法
     */
    public void doBreak(JSONObject jbb){
        //获取  行政执法结果
        String exresultStr = jbb.getString("exresult");
        JSONObject jbbb = JSONObject.parseObject(exresultStr);
        String exresult = jbbb.getString("content");

        //organ  主管税务机关
        String organStr = jbb.getString("organ");
        JSONObject jbbb1 = JSONObject.parseObject(organStr);
        String organ = jbbb1.getString("content");
        //datetype 时间类型
        String datetypeStr = jbb.getString("datetype");
        JSONObject jbbb2 = JSONObject.parseObject(datetypeStr);
        String datetype = jbbb2.getString("content");
        //typet
        String typetStr = jbb.getString("typet");
        JSONObject jbbb3 = JSONObject.parseObject(typetStr);
        String typet = jbbb3.getString("content");
        //origin 违法事由
        String originStr = jbb.getString("origin");
        JSONObject jbbb4 = JSONObject.parseObject(originStr);
        String origin = jbbb4.getString("content");
        //remark 备注
        String remarkStr = jbb.getString("remark");
        JSONObject jbbb5 = JSONObject.parseObject(remarkStr);
        String remark = jbbb5.getString("content");
        //title b标题
        String titleStr = jbb.getString("title");
        JSONObject jbbb6 = JSONObject.parseObject(titleStr);
        String title = jbbb6.getString("content");
        //caseno   案号
        String casenoStr = jbb.getString("caseno");
        JSONObject jbbb7 = JSONObject.parseObject(casenoStr);
        String caseno = jbbb7.getString("content");
        // judgeresult 法院审理结果
        String judgeresultStr = jbb.getString("judgeresult");
        JSONObject jbbb8 = JSONObject.parseObject(judgeresultStr);
        String judgeresult = jbbb8.getString("content");
        // name 当事人名字
        String nameStr = jbb.getString("name");
        JSONObject jbbb9 = JSONObject.parseObject(nameStr);
        String name = jbbb9.getString("content");
        // datetime 具体时间
        String datetimeStr = jbb.getString("datetime");
        JSONObject jbbb10 = JSONObject.parseObject(datetimeStr);
        String datetime = jbbb10.getString("content");
        // cid 证件号码
        String cidStr = jbb.getString("cid");
        JSONObject jbbb11 = JSONObject.parseObject(cidStr);
        String cid = jbbb11.getString("content");


        BreakLawInfo breakLawInfo = new BreakLawInfo();
        breakLawInfo.setCid(cid);
        breakLawInfo.setDatatinme(datetime);
        breakLawInfo.setName(name);
        breakLawInfo.setJudegeresult(judgeresult);
        breakLawInfo.setCaseno(caseno);
        breakLawInfo.setTitle(title);
        breakLawInfo.setOrigin(origin);
        breakLawInfo.setRemark(remark);
        breakLawInfo.setOrgan(organ);
        breakLawInfo.setDatatype(datetype);
        breakLawInfo.setEresult(exresult);




        List<BreakLawInfo> list = breakLawDao.findByName(breakLawInfo.getName());
        boolean flag = true;
        for(int i=0;i<list.size();i++) {
            if (breakLawInfo.getName().equals(list.get(i).getName()) && breakLawInfo.getDatatinme().equals(list.get(i).getDatatinme())){
                 flag = false;
                 break;
             }
            }

            if(flag){
                breakLawDao.save(breakLawInfo);

            }


    }


    public void doDeabb(JSONObject jbb){

        //获取备注字段
        String remarkStr = jbb.getString("remark");
        JSONObject jbbb = JSONObject.parseObject(remarkStr);
        String remark = jbbb.getString("content");
        //获取标题
        String titleStr = jbb.getString("title");
        JSONObject jbbb1 = JSONObject.parseObject(titleStr);
        String title = jbbb1.getString("content");
        //获取执行案号caseno
        String casenoStr = jbb.getString("caseno");
        JSONObject jbbb2 = JSONObject.parseObject(casenoStr);
        String caseno = jbbb2.getString("content");
        //获取具体时间datetime
        String datetimeStr = jbb.getString("datetime");
        JSONObject jbbb3 = JSONObject.parseObject(datetimeStr);
        String datetime = jbbb3.getString("content");
        //获取执行内容context
        String contextStr = jbb.getString("context");
        JSONObject jbbb4 = JSONObject.parseObject(contextStr);
        String context = jbbb4.getString("content");
        //获取执行状态state
        String stateStr = jbb.getString("state");
        JSONObject jbbb5 = JSONObject.parseObject(stateStr);
        String state = jbbb5.getString("content");
        //获取执行法院court
        String courtStr = jbb.getString("court");
        JSONObject jbbb6 = JSONObject.parseObject(courtStr);
        String court = jbbb6.getString("content");
        //获取证件号cid
        String cidStr = jbb.getString("cid");
        JSONObject jbbb7 = JSONObject.parseObject(cidStr);
        String cid = jbbb7.getString("content");

        DeabbeatInfo deabbeatInfo = new DeabbeatInfo();
        deabbeatInfo.setCid(cid);
        deabbeatInfo.setCourt(court);
        deabbeatInfo.setState(state);
        deabbeatInfo.setContext(context);
        deabbeatInfo.setDatatinme(datetime);
        deabbeatInfo.setCaseno(caseno);
        deabbeatInfo.setTitle(title);
        deabbeatInfo.setRemark(remark);



        List<DeabbeatInfo> list = deabbeatDao.findByName(deabbeatInfo.getName());

        boolean flag = true;
        for(int i=0;i<list.size();i++) {
            if (deabbeatInfo.getCid().equals(list.get(i).getCid()) && deabbeatInfo.getDatatinme().equals(list.get(i).getDatatinme())){
                flag = false;
                break;
            }
        }

        if(flag){
            deabbeatDao.save(deabbeatInfo);

        }

    }


    public void doMain( JSONObject jbb){
        //获取主管税务机关
        String organStr = jbb.getString("organ");
        JSONObject jbbb = JSONObject.parseObject(organStr);
        String organ = jbbb.getString("content");
        //period 欠税属期
        String periodStr = jbb.getString("period");
        JSONObject jbbb1 = JSONObject.parseObject(periodStr);
        String period = jbbb1.getString("content");
        //remark 备注
        String remarkStr = jbb.getString("remark");
        JSONObject jbbb2 = JSONObject.parseObject(remarkStr);
        String remark = jbbb2.getString("content");
        //标题 title
        String titleStr = jbb.getString("title");
        JSONObject jbbb3 = JSONObject.parseObject(remarkStr);
        String title = jbbb3.getString("content");
        //taxtype 所欠税种
        String taxtypeStr = jbb.getString("taxtype");
        JSONObject jbbb4 = JSONObject.parseObject(taxtypeStr);
        String taxtype = jbbb4.getString("content");
        // datetime   欠税发生时间
        String datetimeStr = jbb.getString("datetime");
        JSONObject jbbb5 = JSONObject.parseObject(datetimeStr);
        String datetime = jbbb5.getString("content");
        // balance   欠税金额
        String balanceStr = jbb.getString("balance");
        JSONObject jbbb6 = JSONObject.parseObject(balanceStr);
        String balance = jbbb6.getString("content");
        // name   当事人姓名
        String nameStr = jbb.getString("name");
        JSONObject jbbb7 = JSONObject.parseObject(nameStr);
        String name1 = jbbb7.getString("content");
        // cid   证件号码
        String cidStr = jbb.getString("cid");
        JSONObject jbbb8 = JSONObject.parseObject(cidStr);
        String cid = jbbb8.getString("content");

        OwingInfo owingInfo = new OwingInfo();
        owingInfo.setCid(cid);
        owingInfo.setName(name1);
        owingInfo.setBalance(balance);
        owingInfo.setDatatinme(datetime);
        owingInfo.setTaxtype(taxtype);
        owingInfo.setTitle(title);
        owingInfo.setRemark(remark);
        owingInfo.setPeriod(period);
        owingInfo.setOrgan(organ);


        List<OwingInfo> list = owingDao.findByName(owingInfo.getName());

        boolean flag = true;
        for(int i=0;i<list.size();i++) {
            if  (owingInfo.getName().equals(list.get(i).getName()) && owingInfo.getTaxtype().equals(list.get(i).getTaxtype()) ){
                flag = false;
                break;
            }
        }

        if(flag){
            owingDao.save(owingInfo);

        }





    }

    /**
     * 老赖  分页查询
     * @param dto
     * @param pageable
     * @return
     */
    @Override
    public TableResultResponse<Deabbeat> queryDeabbeat(Deabbeat dto, Pageable pageable) {


        long count = deabbeatDao.count(new DeabbeatSpec(dto));
        Page<DeabbeatInfo> page = deabbeatDao.findAll(new DeabbeatSpec(dto), pageable);
        List<DeabbeatInfo> list = page.getContent();
        log.info("页面获取到的数据为：" + list.toString());
        List<Deabbeat> listDto = ConverterService.convertToList(list, Deabbeat.class);
        for (Deabbeat deabbeat : listDto) {
            List<CustomerPublicLog> customList = customerPublicLogDao.findByFileNo(deabbeat.getCid());
            if (customList != null && customList.size() > 0) {
                List<AccountsAll> accountList = null;
                for (CustomerPublicLog customerPublicLog : customList) {
                    accountList = accountsAllDao.findByCustomerNo(customerPublicLog.getCustomerNo());
                }
                if (accountList != null && accountList.size() > 0) {
                    deabbeat.setCustomerNo(accountList.get(0).getCustomerNo());
                }
            }
        }
        return new TableResultResponse<Deabbeat>((int) count, listDto);
    }
    @Override
    public TableResultResponse<Deabbeat> queryDeabbeat1(Deabbeat dto, Pageable pageable) {
        dto.setCid("");
        long count=deabbeatDao.count(new DeabbeatSpec(dto));
        Page<DeabbeatInfo> page =deabbeatDao.findAll(new DeabbeatSpec(dto),pageable);
        if(page.getContent()!=null){
            List<DeabbeatInfo> all = deabbeatDao.findAll();
            List<Deabbeat> listDto = ConverterService.convertToList(all, Deabbeat.class);
            for (Deabbeat deabbeat: listDto) {
                List<CustomerPublicLog> customList =   customerPublicLogDao.findByFileNo(deabbeat.getCid());
                List<AccountsAll> accountList = null;
                if (customList != null && customList.size() > 0) {
                    for (CustomerPublicLog customerPublicLog : customList) {
                        accountList = accountsAllDao.findByCustomerNo(customerPublicLog.getCustomerNo());
                    }
                    if (accountList != null && accountList.size() > 0) {
                        deabbeat.setCustomerNo(accountList.get(0).getCustomerNo());
                    }
                }
            }
            heGuiYuJingAllDao.deleteAllByYuJingType("6");
        if(listDto!=null && listDto.size() > 0){
            for(Deabbeat    dtoo : listDto){
                HeGuiYuJingAll heGuiYuJingAll=new HeGuiYuJingAll();
                if(StringUtils.isNotBlank(dtoo.getCid())){
                    heGuiYuJingAll.setCid(dtoo.getCid());
                }
                if(StringUtils.isNotBlank(dtoo.getCaseno())){
                    heGuiYuJingAll.setCaseno(dtoo.getCaseno());
                }
                if(StringUtils.isNotBlank(dtoo.getContext())){
                    heGuiYuJingAll.setContext(dtoo.getContext());
                }
                if(StringUtils.isNotBlank(dtoo.getCourt())){
                    heGuiYuJingAll.setCourt(dtoo.getCourt());
                }
                if(StringUtils.isNotBlank(dtoo.getDatatinme())){
                    heGuiYuJingAll.setDatatinme(dtoo.getDatatinme());
                }
                if(StringUtils.isNotBlank(dtoo.getName())){
                    heGuiYuJingAll.setName(dtoo.getName());
                }
                if(StringUtils.isNotBlank(dtoo.getCustomerNo())){
                    heGuiYuJingAll.setCustomerNo(dtoo.getCustomerNo());
                }
                if(StringUtils.isNotBlank(dtoo.getTitle())){
                    heGuiYuJingAll.setTitle(dtoo.getTitle());
                }
                if(StringUtils.isNotBlank(dtoo.getRemark())){
                    heGuiYuJingAll.setRemark(dtoo.getRemark());
                }
                if(StringUtils.isNotBlank(dtoo.getState())){
                    heGuiYuJingAll.setState(dtoo.getState());
                }
                heGuiYuJingAll.setYuJingType("6");
                    heGuiYuJingAllDao.save(heGuiYuJingAll);
                }
            }
        }
        log.info("保存全量预警数据严重失信-----");
        List<DeabbeatInfo> list = page.getContent();
        log.info("页面获取到的数据为："+list.toString());
        List<Deabbeat> listDto = ConverterService.convertToList(list, Deabbeat.class);
        for (Deabbeat deabbeat: listDto) {
            List<CustomerPublicLog> customList =   customerPublicLogDao.findByFileNo(deabbeat.getCid());
            List<AccountsAll> accountList = null;
            if (customList != null && customList.size() > 0) {
                for (CustomerPublicLog customerPublicLog : customList) {
                    accountList = accountsAllDao.findByCustomerNo(customerPublicLog.getCustomerNo());
                }
                if (accountList != null && accountList.size() > 0) {
                    deabbeat.setCustomerNo(accountList.get(0).getCustomerNo());
                }
            }
        }
        return new TableResultResponse<Deabbeat>((int) count, listDto);
    }



    /**
     * 欠税  分页查询
     * @param dto
     * @param pageable
     * @return
     */
    @Override
    public TableResultResponse<Owing> queryOwing(Owing dto, Pageable pageable) {



        long count=owingDao.count(new OwingSpec(dto));
        Page<OwingInfo> page =owingDao.findAll(new OwingSpec(dto),pageable);
        List<OwingInfo> list = page.getContent();
        log.info("页面获取到的数据为："+list.toString());
        List<Owing> listDto = ConverterService.convertToList(list, Owing.class);
        return new TableResultResponse<Owing>((int) count, listDto);
    }
    @Override
    public TableResultResponse<Owing> queryOwing1(Owing dto, Pageable pageable) {
        dto.setCid("");
        dto.setOrgan("");
        dto.setName("");
        long count=owingDao.count(new OwingSpec(dto));
        List<OwingInfo> all = owingDao.findAll();
        if(all.size()>0){
            List<Owing> listDto = ConverterService.convertToList(all, Owing.class);
            heGuiYuJingAllDao.deleteAllByYuJingType("9");
        if(listDto!=null && listDto.size() > 0){
            for(Owing    dtoo : listDto){
                HeGuiYuJingAll heGuiYuJingAll=new HeGuiYuJingAll();
                if(StringUtils.isNotBlank(dtoo.getCid())){
                    heGuiYuJingAll.setCid(dtoo.getCid());
                }
                if(StringUtils.isNotBlank(dtoo.getBalance())){
                    heGuiYuJingAll.setBalance(dtoo.getBalance());
                }
                if(StringUtils.isNotBlank(dtoo.getDatatinme())){
                    heGuiYuJingAll.setDatatinme(dtoo.getDatatinme());
                }
                if(StringUtils.isNotBlank(dtoo.getName())){
                    heGuiYuJingAll.setName(dtoo.getName());
                }
                if(StringUtils.isNotBlank(dtoo.getOrgan())){
                    heGuiYuJingAll.setOrgan(dtoo.getOrgan());
                }
                if(StringUtils.isNotBlank(dtoo.getTaxtype())){
                    heGuiYuJingAll.setTaxtype(dtoo.getTaxtype());
                }
                if(StringUtils.isNotBlank(dtoo.getPeriod())){
                    heGuiYuJingAll.setPeriod(dtoo.getPeriod());
                }
                if(StringUtils.isNotBlank(dtoo.getTitle())){
                    heGuiYuJingAll.setTitle(dtoo.getTitle());
                }
                if(StringUtils.isNotBlank(dtoo.getRemark())){
                    heGuiYuJingAll.setRemark(dtoo.getRemark());
                }
                heGuiYuJingAll.setYuJingType("9");
                    heGuiYuJingAllDao.save(heGuiYuJingAll);
                }
            }
        }
        log.info("保存全量预警数据税务处罚-----");
        Page<OwingInfo> page =owingDao.findAll(new OwingSpec(dto),pageable);
        List<OwingInfo> list = page.getContent();
        log.info("页面获取到的数据为："+list.toString());
        List<Owing> listDto = ConverterService.convertToList(list, Owing.class);
        return new TableResultResponse<Owing>((int) count, listDto);
    }

    /**
     * w违法
     * @param dto
     * @param pageable
     * @return
     */
    @Override
    public TableResultResponse<BreakLaw> queryBreakLaw(BreakLaw dto, Pageable pageable) {

        long count=breakLawDao.count(new BreakLawSpec(dto));
        Page<BreakLawInfo> page =breakLawDao.findAll(new BreakLawSpec(dto),pageable);
        List<BreakLawInfo> list = page.getContent();
        log.info("页面获取到的数据为："+list.toString());
        List<BreakLaw> listDto = ConverterService.convertToList(list, BreakLaw.class);
        return new TableResultResponse<BreakLaw>((int) count, listDto);
    }
    @Override
    public TableResultResponse<BreakLaw> queryBreakLaw1(BreakLaw dto, Pageable pageable) {
        dto.setCid("");
        long count=breakLawDao.count(new BreakLawSpec(dto));
        List<BreakLawInfo> all = breakLawDao.findAll();
        if(all.size()>0){
            List<BreakLaw> listDto = ConverterService.convertToList(all, BreakLaw.class);
            heGuiYuJingAllDao.deleteAllByYuJingType("8");
        if(listDto!=null && listDto.size() > 0){
            for(BreakLaw    dtoo : listDto){
                HeGuiYuJingAll heGuiYuJingAll=new HeGuiYuJingAll();
                if(StringUtils.isNotBlank(dtoo.getCid())){
                    heGuiYuJingAll.setCid(dtoo.getCid());
                }
                if(StringUtils.isNotBlank(dtoo.getCaseno())){
                    heGuiYuJingAll.setCaseno(dtoo.getCaseno());
                }
                if(StringUtils.isNotBlank(dtoo.getDatatinme())){
                    heGuiYuJingAll.setDatatinme(dtoo.getDatatinme());
                }
                if(StringUtils.isNotBlank(dtoo.getName())){
                    heGuiYuJingAll.setName(dtoo.getName());
                }
                if(StringUtils.isNotBlank(dtoo.getOrgan())){
                    heGuiYuJingAll.setOrgan(dtoo.getOrgan());
                }
                if(StringUtils.isNotBlank(dtoo.getTitle())){
                    heGuiYuJingAll.setTitle(dtoo.getTitle());
                }
                if(StringUtils.isNotBlank(dtoo.getRemark())){
                    heGuiYuJingAll.setRemark(dtoo.getRemark());
                }
                if(StringUtils.isNotBlank(dtoo.getJudegeresult())){
                    heGuiYuJingAll.setJudegeresult(dtoo.getJudegeresult());
                }
                if(StringUtils.isNotBlank(dtoo.getEresult())){
                    heGuiYuJingAll.setEresult(dtoo.getEresult());
                }
                if(StringUtils.isNotBlank(dtoo.getOrigin())){
                    heGuiYuJingAll.setOrigin(dtoo.getOrigin());
                }
                heGuiYuJingAll.setYuJingType("8");
                    heGuiYuJingAllDao.save(heGuiYuJingAll);
                }
            }
        }
        log.info("保存全量预警数据法律涉案-----");
        Page<BreakLawInfo> page =breakLawDao.findAll(new BreakLawSpec(dto),pageable);
        List<BreakLawInfo> list = page.getContent();
        log.info("页面获取到的数据为："+list.toString());
        List<BreakLaw> listDto = ConverterService.convertToList(list, BreakLaw.class);
        return new TableResultResponse<BreakLaw>((int) count, listDto);
    }


    public RequestBody getRequestBody(String serviceCd, String operation, String tranCode, String stype, String n, String id, String frid, String userid) {
        RequestBody requestBody = new RequestBody();
        requestBody.setTransaction(getTransaction(serviceCd, operation, tranCode, stype,n,id,frid,userid));
        return requestBody;
    }

    public Transaction getTransaction(String serviceCd, String operation, String tranCode,String stype, String n, String id, String frid, String userid) {
        Transaction Transaction = new Transaction();
        Transaction.setBody(getBody(tranCode, stype, n, id, frid ,userid));
        Transaction.setHeader(getHeader(serviceCd, operation,  stype, n, id, frid ,userid));
        return Transaction;
    }

    public Header getHeader(String serviceCd, String operation, String stype, String n, String id, String frid, String userid) {
        Header Header = new Header();
        Header.setSysHeader(getSysHead(serviceCd, operation));
        return Header;
    }

    public Body getBody(String tranCode, String stype, String n, String id, String frid, String userid) {
        Body Body = new Body();
        Body.setRequest(getRequest(tranCode, stype, n ,id ,frid,userid));
        return Body;
    }

    public Request getRequest(String tranCode, String stype, String n, String id, String frid, String userid) {
        Request request = new Request();
        request.setBizBody(getBizBody(stype,n,id,frid,userid));
        request.setBizHeader(getBizHeader(tranCode, stype,n,id,frid,userid));
        return request;
    }

    public BizHeader getBizHeader(String tranCode,  String stype, String n, String id, String frid, String userid) {
        BizHeader bizHead = new BizHeader();
        bizHead.setInterfaceId(tranCode);//发起系统标示
        bizHead.setUserName("dgzh");
        bizHead.setUserKey("99f2e92ca7e14404b179af417b27aba6");
        bizHead.setVer("1");
        return bizHead;
    }

    public BizBody getBizBody( String stype, String n, String id, String frid, String userid) {
        BizBody bizBody = new BizBody();
        bizBody.setStype(stype);
        bizBody.setN(n);
        bizBody.setId(id);
        bizBody.setUserid(userid);
        bizBody.setFrid(frid);
        return bizBody;
    }

    public SysHeader getSysHead(String serviceCd, String operation) {
        String clientCd = "339";
        String serverCd = "261";
        SysHeader sysHeader = new SysHeader();
        sysHeader.setMsgId("0" + clientCd + getNowDateShort("yyyyMMdd") + getNowDateShort("HHmmssSSS") + (int) ((Math.random() * 9 + 1) * 1000));
        sysHeader.setServiceCd(serviceCd);
        sysHeader.setOperation(operation);
        sysHeader.setClientCd(clientCd);
        sysHeader.setServerCd(serverCd);
        sysHeader.setOrgCode("");
        sysHeader.setMsgDate("");
        sysHeader.setMsgTime("");
        return sysHeader;
    }


    public static void main(String[] args) {
  /*      String s = "{\"CI_QKQFMD\":[{\"datetype\":{\"type\":\"String\"},\"accuracy\":{\"type\":\"String\",\"content\":\"100%\"}},{\"datetype\":{\"type\":\"String\"},\"accuracy\":{\"type\":\"String\",\"content\":\"100%\"}}]}";
        String s1 = "{\"CI_QSMD\":[{\"organ\":{\"type\":\"String\",\"content\":\"常州市地方税务局\"}},{\"organ\":{\"type\":\"String\",\"content\":\"常州市地方税务局\"}},{\"organ\":{\"type\":\"String\",\"content\":\"常州市地方税务局\"}}]}";
        JSONObject jb = JSONObject.parseObject(s1);
        System.out.println(jb.getString("CI_QSMD"));

       String [] aa =  jb.getString("CI_QSMD").split("}},");
      System.out.println(Arrays.toString(aa));

     if(aa.length > 1) {
         for (int i = 0; i < aa.length; i++) {
             if(i != aa.length-1){
                  if(i==0){
                      System.out.println(new StringBuffer(aa[i]).append("}}").substring(1,new StringBuffer(aa[i]).append("}}").length()));
                  }else {

                      System.out.println(new StringBuffer(aa[i]).append("}}"));
                  }
             }else{
                 System.out.println(aa[i].substring(0,aa[i].length()-1));
             }
         }
     }*/















     /*   String ss = jb.getString("CI_QKQFMD");
       String aa = ss.substring(1,ss.length()-1);*/




/*

        JSONObject jb1 = JSONObject.parseObject(aa);
        System.out.println(jb1.getString("accuracy"));

        JSONObject jb11 = JSONObject.parseObject(jb1.getString("accuracy"));
        System.out.println(jb11.getString("content"));*/














    }




}
