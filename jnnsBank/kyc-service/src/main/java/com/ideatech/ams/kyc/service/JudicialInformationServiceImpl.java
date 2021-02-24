package com.ideatech.ams.kyc.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.kyc.dao.JudicialInformationDao;
import com.ideatech.ams.kyc.dto.JudicialInformationDto;
import com.ideatech.ams.kyc.entity.JudicialInformation;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.proof.dto.ProofReportDto;
import com.ideatech.ams.system.proof.enums.ProofType;
import com.ideatech.ams.system.proof.service.ProofReportService;
import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.ams.system.user.service.UserService;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.util.SecurityUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class JudicialInformationServiceImpl implements JudicialInformationService{

    @Autowired
    private SaicRequestService saicRequestService;

    @Autowired
    private JudicialInformationDao judicialInformationDao;

    @Value("${ams.company.writeMoney}")
    private boolean writeMoney;

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private ProofReportService proofReportService;

    @Override
    public TableResultResponse<JudicialInformationDto> getDetailFromCompanyName(String companyName, Pageable pageable) {
        String responseStr = "";
        int count = 0;
        List<JudicialInformationDto> dtos = new ArrayList<>();
        // 加密
        try {
            companyName = URLDecoder.decode(companyName, "UTF-8");
            //代理模式访问idp接口
            responseStr = saicRequestService.getJudicialInformation(companyName,pageable.getPageNumber(),pageable.getPageSize());
//            responseStr = "{\"totalRecords\":12,\"data\":[{\"jtqx\":\"有履行能力而拒不履行生效法律文书确定义务\",\"pname\":\"鄂州市大宅房地产开发有限公司\",\"sortTime\":\"2019-01-07 00:00:00\",\"orgTypeName\":\"失信企业\",\"idcardNo\":\"72204142-9\",\"court\":\"鄂州市鄂城区人民法院\",\"caseNo\":\"(2019)鄂0704执89号\",\"yjCode\":\"（2016）鄂0704民初2095号\",\"orgType\":\"2\",\"postTime\":\"2019-06-19 00:00:00\",\"lxqk\":\"全部未履行\",\"province\":\"湖北\",\"yiwu\":\"（2016）鄂0704民初2095号判决书\",\"unperformpart\":\"暂无\",\"yjdw\":\"鄂州市鄂城区人民法院\",\"performedpart\":\"暂无\"},{\"jtqx\":\"违反财产报告制度\",\"pname\":\"鄂州市大宅房地产开发有限公司\",\"sortTime\":\"2019-01-23 00:00:00\",\"orgTypeName\":\"失信企业\",\"idcardNo\":\"72204142-9\",\"court\":\"鄂州市鄂城区人民法院\",\"caseNo\":\"(2019)鄂0704执195号\",\"yjCode\":\"（2019）鄂0704行审17号\",\"orgType\":\"2\",\"postTime\":\"2019-04-16 00:00:00\",\"lxqk\":\"全部未履行\",\"province\":\"湖北\",\"yiwu\":\"（2019）鄂0704行审17号裁定书\",\"unperformpart\":\"暂无\",\"yjdw\":\"鄂州市鄂城区人民法院\",\"performedpart\":\"暂无\"},{\"jtqx\":\"有履行能力而拒不履行生效法律文书确定义务\",\"pname\":\"鄂州市大宅房地产开发有限公司\",\"sortTime\":\"2017-06-06 00:00:00\",\"orgTypeName\":\"失信企业\",\"idcardNo\":\"72204142-9\",\"court\":\"鄂州市华容区人民法院\",\"caseNo\":\"(2017)鄂0703执254号\",\"yjCode\":\"（2017）鄂0703民初185号\",\"orgType\":\"2\",\"postTime\":\"2017-11-24 00:00:00\",\"lxqk\":\"全部未履行\",\"province\":\"湖北\",\"yiwu\":\"支付1160000元。\",\"unperformpart\":\"暂无\",\"yjdw\":\"鄂州市华容区人民法院\",\"performedpart\":\"暂无\"},{\"jtqx\":\"伪造证据妨碍、抗拒执行\",\"pname\":\"鄂州市大宅房地产开发有限公司\",\"sortTime\":\"2016-11-24 00:00:00\",\"orgTypeName\":\"失信企业\",\"idcardNo\":\"72204142-9\",\"court\":\"鄂州市鄂城区人民法院\",\"caseNo\":\"(2016)鄂0704执1245号\",\"yjCode\":\"（2016）鄂07民终272号\",\"orgType\":\"2\",\"postTime\":\"2017-04-10 00:00:00\",\"lxqk\":\"全部未履行\",\"province\":\"湖北\",\"yiwu\":\"（2016）鄂07民终272号\",\"unperformpart\":\"\",\"yjdw\":\"鄂城区人民法院\",\"performedpart\":\"\"},{\"jtqx\":\"有履行能力而拒不履行生效法律文书确定义务\",\"pname\":\"鄂州市大宅房地产开发有限公司\",\"sortTime\":\"2019-02-25 00:00:00\",\"orgTypeName\":\"失信企业\",\"idcardNo\":\"72204142-9\",\"court\":\"鄂州市鄂城区人民法院\",\"caseNo\":\"(2019)鄂0704执296号\",\"yjCode\":\"（2017）鄂0704民初1375号\",\"orgType\":\"2\",\"postTime\":\"2019-08-02 00:00:00\",\"lxqk\":\"全部未履行\",\"province\":\"湖北\",\"yiwu\":\"（2017）鄂0704民初1371号判决书\",\"unperformpart\":\"暂无\",\"yjdw\":\"鄂州市鄂城区人民法院\",\"performedpart\":\"暂无\"},{\"jtqx\":\"伪造证据妨碍、抗拒执行\",\"pname\":\"鄂州市大宅房地产开发有限公司\",\"sortTime\":\"2017-01-04 00:00:00\",\"orgTypeName\":\"失信企业\",\"idcardNo\":\"72204142-9\",\"court\":\"鄂州市鄂城区人民法院\",\"caseNo\":\"(2017)鄂0704执69号\",\"yjCode\":\"（2015）鄂鄂城民初字第00497号\",\"orgType\":\"2\",\"postTime\":\"2017-04-10 00:00:00\",\"lxqk\":\"全部未履行\",\"province\":\"湖北\",\"yiwu\":\"（2015）鄂鄂城民初字第00497号民事判决书\",\"unperformpart\":\"\",\"yjdw\":\"鄂城区人民法院\",\"performedpart\":\"\"},{\"jtqx\":\"有履行能力而拒不履行生效法律文书确定义务\",\"pname\":\"鄂州市大宅房地产开发有限公司\",\"sortTime\":\"2019-02-25 00:00:00\",\"orgTypeName\":\"失信企业\",\"idcardNo\":\"72204142-9\",\"court\":\"鄂州市鄂城区人民法院\",\"caseNo\":\"(2019)鄂0704执297号\",\"yjCode\":\"（2017）鄂0704民初1374号\",\"orgType\":\"2\",\"postTime\":\"2019-08-02 00:00:00\",\"lxqk\":\"全部未履行\",\"province\":\"湖北\",\"yiwu\":\"（2017）鄂0704民初1374号判决书\",\"unperformpart\":\"暂无\",\"yjdw\":\"鄂州市鄂城区人民法院\",\"performedpart\":\"暂无\"},{\"jtqx\":\"有履行能力而拒不履行生效法律文书确定义务\",\"pname\":\"鄂州市大宅房地产开发有限公司\",\"sortTime\":\"2019-02-25 00:00:00\",\"orgTypeName\":\"失信企业\",\"idcardNo\":\"72204142-9\",\"court\":\"鄂州市鄂城区人民法院\",\"caseNo\":\"(2019)鄂0704执295号\",\"yjCode\":\"（2017）鄂0704民初1371号\",\"orgType\":\"2\",\"postTime\":\"2019-08-02 00:00:00\",\"lxqk\":\"全部未履行\",\"province\":\"湖北\",\"yiwu\":\"（2017）鄂0704民初1371号判决书\",\"unperformpart\":\"暂无\",\"yjdw\":\"鄂州市鄂城区人民法院\",\"performedpart\":\"暂无\"},{\"jtqx\":\"伪造证据妨碍、抗拒执行\",\"pname\":\"鄂州市大宅房地产开发有限公司\",\"sortTime\":\"2017-02-21 00:00:00\",\"orgTypeName\":\"失信企业\",\"idcardNo\":\"72204142-9\",\"court\":\"鄂州市鄂城区人民法院\",\"caseNo\":\"(2017)鄂0704执恢35号\",\"yjCode\":\"(2016)鄂0704执1244号\",\"orgType\":\"2\",\"postTime\":\"2017-04-10 00:00:00\",\"lxqk\":\"全部未履行\",\"province\":\"湖北\",\"yiwu\":\"(2016)鄂0704执1244号执行裁定\",\"unperformpart\":\"\",\"yjdw\":\"鄂州市鄂城区人民法院\",\"performedpart\":\"\"},{\"jtqx\":\"有履行能力而拒不履行生效法律文书确定义务\",\"pname\":\"鄂州市大宅房地产开发有限公司\",\"sortTime\":\"2019-02-25 00:00:00\",\"orgTypeName\":\"失信企业\",\"idcardNo\":\"72204142-9\",\"court\":\"鄂州市鄂城区人民法院\",\"caseNo\":\"(2019)鄂0704执294号\",\"yjCode\":\"（2017）鄂0704民初1380号\",\"orgType\":\"2\",\"postTime\":\"2019-08-02 00:00:00\",\"lxqk\":\"全部未履行\",\"province\":\"湖北\",\"yiwu\":\"（2017）鄂0704民初1380号判决书\",\"unperformpart\":\"暂无\",\"yjdw\":\"鄂州市鄂城区人民法院\",\"performedpart\":\"暂无\"}],\"pageIndex\":1,\"pageSize\":10}";
            if(StringUtils.isNotBlank(responseStr) && !StringUtils.equals(responseStr,"{}")){
                JSONObject jsonObject = JSON.parseObject(responseStr);
                count = Integer.parseInt(jsonObject.getString("totalRecords"));
                String data = jsonObject.getString("data");
                dtos = JSONArray.parseArray(data,JudicialInformationDto.class);
                if(CollectionUtils.isNotEmpty(dtos)){
                    for (JudicialInformationDto dto : dtos){
                        save(dto);
                    }
                }
                //费用
                if(writeMoney){
                    ProofReportDto accountProofReportDto = new ProofReportDto();
                    String username =  SecurityUtils.getCurrentUsername();
                    if(StringUtils.isBlank(username)){
                        UserDto userDto = userService.findById(2L);
                        OrganizationDto organizationDto = organizationService.findById(userDto.getOrgId());
                        accountProofReportDto.setTypeDetil("接口方式司法信息查询");
                        accountProofReportDto.setOrganFullId(organizationDto.getFullId());
                        accountProofReportDto.setUsername(userDto.getUsername());
                        accountProofReportDto.setProofBankName(organizationDto.getName());
                    }else{
                        OrganizationDto organizationDto = organizationService.findByOrganFullId(SecurityUtils.getCurrentOrgFullId());
                        accountProofReportDto.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
                        accountProofReportDto.setUsername(username);
                        accountProofReportDto.setTypeDetil("账管系统司法信息查询");
                        accountProofReportDto.setProofBankName(organizationDto.getName());
                    }
                    ConfigDto configDto = configService.findOneByConfigKey("judicialInformationMoney");
                    if(configDto!=null){
                        accountProofReportDto.setPrice(configDto.getConfigValue());
                    }else{
                        accountProofReportDto.setPrice("0");
                    }
                    accountProofReportDto.setType(ProofType.JUDICIAL_INFORMATION);
                    accountProofReportDto.setEntname(companyName);
                    accountProofReportDto.setDateTime(DateFormatUtils.format(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss"));
                    proofReportService.save(accountProofReportDto);
                }
            }
            return new TableResultResponse<JudicialInformationDto>(count, dtos);
        } catch (Exception e) {
            if(e.getMessage().contains("获取数据超时")) {
                return null;
            }else{
                return null;
            }
        }
    }

    @Override
    public JudicialInformationDto findByCaseNo(String caseNo) {
        JudicialInformationDto judicialInformationDto = new JudicialInformationDto();
        JudicialInformation judicialInformation = judicialInformationDao.findByCaseNo(caseNo);
        if(judicialInformation != null){
            BeanUtils.copyProperties(judicialInformation,judicialInformationDto);
        }
        return judicialInformationDto;
    }

    public void save(JudicialInformationDto judicialInformationDto){
        String[] ig = {"id"};
        //根据案号进行查询  企业名称存在重复，案号不会重复
        JudicialInformation judicialInformation = judicialInformationDao.findByCaseNo(judicialInformationDto.getCaseNo());
        if(judicialInformation != null){
            BeanUtils.copyProperties(judicialInformationDto,judicialInformation,ig);
            judicialInformationDao.save(judicialInformation);
        }else{
            judicialInformation = new JudicialInformation();
            BeanUtils.copyProperties(judicialInformationDto,judicialInformation);
            judicialInformationDao.save(judicialInformation);
        }
    }
}
