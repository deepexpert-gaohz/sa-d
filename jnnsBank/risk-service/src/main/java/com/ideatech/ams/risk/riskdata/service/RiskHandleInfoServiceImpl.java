package com.ideatech.ams.risk.riskdata.service;

import com.ideatech.ams.risk.Constant.MysqlSQLConstant;
import com.ideatech.ams.risk.Constant.OracleSQLConstant;
import com.ideatech.ams.risk.riskdata.dao.RiskHandleInfoDao;
import com.ideatech.ams.risk.riskdata.dto.RiskDataInfoDto;
import com.ideatech.ams.risk.riskdata.dto.RiskDataSearchDto;
import com.ideatech.ams.risk.riskdata.dto.RiskHandleInfoDto;
import com.ideatech.ams.risk.riskdata.entity.RiskHandleInfo;
import com.ideatech.ams.risk.util.RiskUtil;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.util.SecurityUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 对风险处理结果处理
 * @author yangcq
 * @Date 20190623
 */
@Service
@Transactional
public class RiskHandleInfoServiceImpl implements RiskHandleInfoService {
    private static final Logger log = LoggerFactory.getLogger( RiskApiServiceImpl.class);

    @Autowired
    RiskHandleInfoDao riskHandleInfoDao;
    @Autowired
    private EntityManager entityManager;
    /**
     * 保存处理结果数据
     * @param riskHarndleInfoDto
     */
    @Override
    public void saveHandleInfo(RiskHandleInfoDto riskHarndleInfoDto) {
        RiskHandleInfo riskHandleInfo = new RiskHandleInfo();
        if(null != riskHarndleInfoDto.getId()){
            riskHandleInfo = riskHandleInfoDao.findOne(riskHarndleInfoDto.getId());
            if (riskHandleInfo == null){
                riskHandleInfo = new RiskHandleInfo();
            }
        }
        ConverterService.convert(riskHarndleInfoDto,riskHandleInfo);
        riskHandleInfoDao.save ( riskHandleInfo );
    }


    /**
     * 查询风险案例数据
     * @author yangcq
     * @date 20190625
     * @addres wulmq
     * @param riskDataSearchDto
     * @return
     */
    @Override
    public RiskDataSearchDto querytodoRiskInfo(RiskDataSearchDto riskDataSearchDto) {
        SecurityUtils.UserInfo user = SecurityUtils.getCurrentUser();
        String sql = OracleSQLConstant.querytodoRiskInfoSQL;
        //String sql = MysqlSQLConstant.querytodoRiskInfoSQL;
        String countSql = OracleSQLConstant.querytodoRiskInfoCountSQL;
        //String countSql = MysqlSQLConstant.querytodoRiskInfoCountSQL;
        String countStSql = MysqlSQLConstant.querytodoRiskInfoCountStSQL;
        String code = RiskUtil.getOrganizationCode();
//        sql = sql +"and og.yd_full_id like '%"+user.getOrgFullId ()+"%'";
//        sql = sql +"and m.yd_corporate_bank = \'"+code+"\'";
//        countSql = countSql +"and og.yd_full_id like '%"+user.getOrgFullId ()+"%'";
//        countSql = countSql +"and m.yd_corporate_bank = \'"+code+"\'";

//        countStSql = countStSql +"and og.yd_full_id like '%"+user.getOrgFullId ()+"%'";
//        countStSql = countStSql +"and m.yd_corporate_bank = \'"+code+"\'";
        return getRiskDataSearchDto ( riskDataSearchDto, sql, countSql,countStSql,"todoList" );
    }

    @Override
    public RiskDataSearchDto getRiskDataSearchDto(RiskDataSearchDto riskDataSearchDto, String sql, String countSql,String countStSql,String flag) {
        if(StringUtils.isNotBlank(riskDataSearchDto.getModelId ())){
            sql += " and m.yd_model_id = ?1";
            countSql += " and m.yd_model_id = ?1";
            countStSql += " and m.yd_model_id = ?1";
        }
        if(StringUtils.isNotBlank(riskDataSearchDto.getAccountNo ())){
            sql += " and t2.yd_acct_no = ?2";
            countSql += " and t2.yd_acct_no = ?2";
            countStSql += " and t2.yd_acct_no = ?2";
        }
        if(StringUtils.isNotBlank(riskDataSearchDto.getAccountName ())){
            sql += " and t2.yd_acct_name like ?3";
            countSql += " and t2.yd_acct_name like ?3";
            countStSql += " and t2.yd_acct_name like ?3";
        }
        if(StringUtils.isNotBlank(riskDataSearchDto.getHandleMode ())){
            sql += " and t1.yd_handle_mode = ?4";
            countSql += " and t1.yd_handle_mode = ?4";
            countStSql += " and t1.yd_handle_mode = ?4";
        }
        if("todoList".equals ( flag )){
            if (StringUtils.isNotBlank(riskDataSearchDto.getStartEndTime())){
                //sql += " and to_char(t1.yd_risk_date,'yyyyMMdd') >= ?5";
                sql += OracleSQLConstant.getRiskDataSearchDtoTOCHAR1;
               // sql += MysqlSQLConstant.getRiskDataSearchDtoTOCHAR1;
                // countSql += " and to_char(t1.yd_risk_date,'yyyyMMdd') >= ?5";
                countSql += OracleSQLConstant.getRiskDataSearchDtoTOCHAR1;
                //countSql += MysqlSQLConstant.getRiskDataSearchDtoTOCHAR1;
                //countStSql += MysqlSQLConstant.getRiskDataSearchDtoTOCHAR1;
                countStSql+=OracleSQLConstant.getRiskDataSearchDtoTOCHAR1;
            }
            if (StringUtils.isNotBlank(riskDataSearchDto.getStartEndTime())){
                //sql += " and to_char(t1.yd_risk_date,'yyyyMMdd') <= ?6";
                sql += OracleSQLConstant.getRiskDataSearchDtoTOCHAR2;
                //sql += MysqlSQLConstant.getRiskDataSearchDtoTOCHAR2;
                //countSql += " and to_char(t1.yd_risk_date,'yyyyMMdd') <= ?6";
                 countSql += OracleSQLConstant.getRiskDataSearchDtoTOCHAR2;
                //countSql += MysqlSQLConstant.getRiskDataSearchDtoTOCHAR2;
                //countStSql += MysqlSQLConstant.getRiskDataSearchDtoTOCHAR2;
                countStSql+=OracleSQLConstant.getRiskDataSearchDtoTOCHAR2;

            }
        }else{
            if (StringUtils.isNotBlank(riskDataSearchDto.getStartEndTime())){
                //sql += " and to_char(t1.yd_handle_date,'yyyyMMdd') >= ?5";
                 sql += OracleSQLConstant.getRiskDataSearchDtoTOCHAR3;
                //sql += MysqlSQLConstant.getRiskDataSearchDtoTOCHAR3;
                //countSql += " and to_char(t1.yd_handle_date,'yyyyMMdd') >= ?5";
                 countSql += OracleSQLConstant.getRiskDataSearchDtoTOCHAR3;
                //countSql += MysqlSQLConstant.getRiskDataSearchDtoTOCHAR3;
                //countStSql += MysqlSQLConstant.getRiskDataSearchDtoTOCHAR3;
                countStSql+=OracleSQLConstant.getRiskDataSearchDtoTOCHAR3;

            }
            if (StringUtils.isNotBlank(riskDataSearchDto.getStartEndTime())){
                //sql += " and to_char(t1.yd_handle_date,'yyyyMMdd') <= ?6";
                sql += OracleSQLConstant.getRiskDataSearchDtoTOCHAR4;
                //sql += MysqlSQLConstant.getRiskDataSearchDtoTOCHAR4;
                //countSql += " and to_char(t1.yd_handle_date,'yyyyMMdd') <= ?6";
                countSql += OracleSQLConstant.getRiskDataSearchDtoTOCHAR4;
                //countSql += MysqlSQLConstant.getRiskDataSearchDtoTOCHAR4;
                //countStSql += MysqlSQLConstant.getRiskDataSearchDtoTOCHAR4;
                countStSql+=OracleSQLConstant.getRiskDataSearchDtoTOCHAR4;

            }
        }
        if(StringUtils.isNotBlank(riskDataSearchDto.getStatus ())){
            sql += " and t1.yd_status = ?7";
            countSql += " and t1.yd_status = ?7";
            countStSql += " and t1.yd_status = ?7";
        }
        if("handleList".equals ( flag )){
            sql =sql+" order by  t2.yd_acct_no,t2.yd_acct_name,t1.yd_risk_date DESC";
            countSql = countSql  +" group by t2.yd_acct_no,t2.yd_acct_name,t1.yd_risk_date) ts";
        }else{
            sql = sql +" order by t2.yd_acct_no,t2.yd_acct_name,t1.yd_risk_date DESC";
            countSql = countSql  +" group by t2.yd_acct_no,t2.yd_acct_name,t1.yd_risk_date) ts";

        }
        Query nativeQueryList = entityManager.createNativeQuery(sql);
        //  Query nativeQueryCountList = entityManager.createNativeQuery(countSql);
        Query nativeQueryCountStList = entityManager.createNativeQuery(countStSql);
        if(StringUtils.isNotBlank(riskDataSearchDto.getModelId())){
            nativeQueryList.setParameter(1,riskDataSearchDto.getModelId());
            //nativeQueryCountList.setParameter(1,riskDataSearchDto.getModelId());
            nativeQueryCountStList.setParameter(1,riskDataSearchDto.getModelId());
        }
        if(StringUtils.isNotBlank(riskDataSearchDto.getAccountNo ())){
            nativeQueryList.setParameter(2,riskDataSearchDto.getAccountNo());
            // nativeQueryCountList.setParameter(2,riskDataSearchDto.getAccountNo());
            nativeQueryCountStList.setParameter(2,riskDataSearchDto.getAccountNo());
        }
        if (StringUtils.isNotBlank(riskDataSearchDto.getAccountName ())) {
            nativeQueryList.setParameter(3, "%" + riskDataSearchDto.getAccountName() + "%");
            // nativeQueryCountList.setParameter(3, "%" + riskDataSearchDto.getAccountName() + "%");
            nativeQueryCountStList.setParameter(3, "%" + riskDataSearchDto.getAccountName() + "%");
        }
        if(StringUtils.isNotBlank(riskDataSearchDto.getHandleMode ())){
            nativeQueryList.setParameter(4,riskDataSearchDto.getHandleMode());
            // nativeQueryCountList.setParameter(4,riskDataSearchDto.getHandleMode());
            nativeQueryCountStList.setParameter(4,riskDataSearchDto.getHandleMode());
        }
        if(StringUtils.isNotBlank(riskDataSearchDto.getStartEndTime())){
            nativeQueryList.setParameter(5,riskDataSearchDto.getStartEndTime().split("~")[0].trim().replace("-",""));
            //  nativeQueryCountList.setParameter(5,riskDataSearchDto.getStartEndTime().split("~")[0].trim());
            nativeQueryCountStList.setParameter(5,riskDataSearchDto.getStartEndTime().split("~")[0].trim().replace("-",""));
        }
        if(StringUtils.isNotBlank(riskDataSearchDto.getStartEndTime())){
            nativeQueryList.setParameter(6,riskDataSearchDto.getStartEndTime().split("~")[1].trim().replace("-",""));
            // nativeQueryCountList.setParameter(6,riskDataSearchDto.getStartEndTime().split("~")[1].trim());
            nativeQueryCountStList.setParameter(6,riskDataSearchDto.getStartEndTime().split("~")[1].trim().replace("-",""));
        }
        if(StringUtils.isNotBlank(riskDataSearchDto.getStatus ())){
            nativeQueryList.setParameter(7,riskDataSearchDto.getStatus());
            // nativeQueryCountList.setParameter(7,riskDataSearchDto.getStatus());
            nativeQueryCountStList.setParameter(7,riskDataSearchDto.getStatus());
        }
        //  riskDataSearchDto.setLimit ( 30 );
        Pageable pageable = new PageRequest (Math.max(riskDataSearchDto.getOffset() - 1, 0), riskDataSearchDto.getLimit());
        nativeQueryList.setFirstResult(pageable.getOffset());
        nativeQueryList.setMaxResults(pageable.getPageSize ());
        List<Object[]> resultList = nativeQueryList.getResultList();
        // Object ojb = nativeQueryCountList.getSingleResult ();
        List<Object[]> resultList1 = nativeQueryCountStList.getResultList();
        Long countSt;
        if(resultList1 == null||resultList1.size ()<=0){
            countSt = 0L;
        }else {
            countSt = Long.parseLong(String.valueOf ( resultList1.size () ));
        }
        //获取分页页数
        float num =(float)countSt/30;
        int pageSzie_int = (int)Math.ceil(num);
        List<RiskDataInfoDto> list =new ArrayList ();
        for (Object[] obj:resultList) {
            RiskDataInfoDto e = new RiskDataInfoDto();
            e.setOrdNum ( (obj[0]==null)?"":obj[0].toString() );//序号
            e.setId ( Long.parseLong ( (obj[1]==null)?"":obj[1].toString() ) );
            e.setRiskDate ( (obj[2]==null)?"":obj[2].toString() );//风险日期
            e.setName ( (obj[3]==null)?"":obj[3].toString() );//模型名称
            e.setAccountNo ((obj[4]==null)?"":obj[4].toString());//账户号
            e.setAccountName ((obj[5]==null)?"":obj[5].toString());//账户名称
            e.setAccCreateDate ( (obj[6]==null)?"":obj[6].toString() );//开户日期
            e.setRiskDesc ( (obj[7]==null)?"":obj[7].toString()  );//风险描述
            e.setRiskId ( (obj[8]==null)?"":obj[8].toString()  );//风险编号
            e.setRiskPoint ( (obj[9]==null)?"":obj[9].toString()  );//风险点
            e.setBankCode ( (obj[10]==null)?"":obj[10].toString()  );//开户行
            e.setAccountStatus ( (obj[11]==null)?"":obj[11].toString()  );//账户类型
            e.setStatus ( (obj[12]==null)?"":obj[12].toString()  );//处理状态
            if("handleList".equals ( flag )){
                e.setHandleMode ( (obj[13]==null)?"":obj[13].toString()  );//处理方式
                e.setHandleDate ( (obj[14]==null)?"":obj[14].toString() );//处理时间
                e.setHandler ( (obj[15]==null)?"":obj[15].toString() );//处理人
                e.setDubiousReason ( (obj[16]==null)?"":obj[16].toString() );//暂停非柜面可疑原因
            }
            list.add(e);
        }
        riskDataSearchDto.setList(list);
        riskDataSearchDto.setTotalPages(pageSzie_int);
        riskDataSearchDto.setTotalRecord(countSt);
        return  riskDataSearchDto;
    }

    @Override
    public List<RiskHandleInfo> findIdByAccountNoAndStatus(String accountNo, String status) {
        return riskHandleInfoDao.findIdByAccountNoAndStatus ( accountNo,  status);
    }


    @Override
    public RiskHandleInfoDto findByRId(String id) {
        RiskHandleInfoDto riskHandleInfoDto = new RiskHandleInfoDto();
        Long idL = Long.parseLong ( id );
        RiskHandleInfo info = riskHandleInfoDao.findOne ( idL);
        ConverterService.convert(info,riskHandleInfoDto);
        return riskHandleInfoDto;
    }


    @Override
    public void deleteById(String id) {
        riskHandleInfoDao.delete(Long.parseLong(id));
    }


    public static void main(String args[]){
        float num =(float)164/30;

        DecimalFormat df = new DecimalFormat("0");

        System.out.println ( df.format(num) );

    }
}
