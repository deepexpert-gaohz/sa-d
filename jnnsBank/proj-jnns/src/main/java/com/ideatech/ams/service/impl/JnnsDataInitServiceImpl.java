package com.ideatech.ams.service.impl;
import com.ideatech.ams.dto.JnnsOrganizationExcelRowVo;
import com.ideatech.ams.dto.JnnsOrganizationPbcAccountExcelRowVo;
import com.ideatech.ams.dto.JnnsUserExcelRowVo;
import com.ideatech.ams.service.JnnsDataInitService;
import com.ideatech.ams.system.org.dto.OrganRegisterDto;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganRegisterService;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.pbc.dto.PbcAccountDto;
import com.ideatech.ams.system.pbc.enums.EAccountStatus;
import com.ideatech.ams.system.pbc.enums.EAccountType;
import com.ideatech.ams.system.pbc.service.PbcAccountService;
import com.ideatech.ams.system.role.dto.RoleDto;
import com.ideatech.ams.system.role.service.RoleService;
import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.ams.system.user.service.UserService;
import com.ideatech.common.constant.IdeaConstant;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.excel.util.ImportExcel;
import lombok.extern.log4j.Log4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;
import java.util.*;

/**
 * 江南农商数据初始化服务类
 *
 * @auther ideatech
 * @create 2019-06-17 9:29 AM
 **/
@Service
@Log4j
public class JnnsDataInitServiceImpl implements JnnsDataInitService {

    @Autowired
    OrganizationService organizationService;

    @Autowired
    private OrganRegisterService organRegisterService;

    @Autowired
    private PbcAccountService pbcAccountService;

    @Autowired
    RoleService roleService;

    @Autowired
    UserService userService;

    @Override
    public void uploadOrganization() {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            URL url = classLoader.getResource("init/initOrgans.xls");
            if (url == null || StringUtils.isBlank(url.getPath())) {
                return;
            }
            log.info("开始初始化机构数据");
            File file = new File(url.getPath());
            ImportExcel importExcel = new ImportExcel(file, 0, 0);
            List<JnnsOrganizationExcelRowVo> dataList = importExcel.getDataList(JnnsOrganizationExcelRowVo.class);
            //根据parentCode对组织列表进行排序，parent在前，children在后
            Collection roots = CollectionUtils.select(dataList, new Predicate() {
                @Override
                public boolean evaluate(Object object) {
                    JnnsOrganizationExcelRowVo vo = (JnnsOrganizationExcelRowVo) object;
                    return StringUtils.equalsIgnoreCase(vo.getParentCode(), IdeaConstant.ORG_ROOT_CODE);
                }
            });

            if (roots.isEmpty() || roots.size() > 1) {
                log.info("初始化机构导入失败，根组织必须有且只能有一个");
            } else {
                JnnsOrganizationExcelRowVo root = (JnnsOrganizationExcelRowVo) roots.toArray()[0];
                List<JnnsOrganizationExcelRowVo> reordered = reorder(root, dataList);

                for (JnnsOrganizationExcelRowVo JnnsOrganizationExcelRowVo : reordered) {
                    if (StringUtils.isNotEmpty(JnnsOrganizationExcelRowVo.getMobile()) && !JnnsOrganizationExcelRowVo.getMobile().matches("^1\\d{10}$")) {
                        log.info("导入组织失败:" + JnnsOrganizationExcelRowVo.getName() + "的联系人手机号格式不正确！");
                        return;
                    }
                }
                for (JnnsOrganizationExcelRowVo JnnsOrganizationExcelRowVo : reordered) {
                    if (StringUtils.isNotEmpty(JnnsOrganizationExcelRowVo.getPbcCode())) {
                        if (JnnsOrganizationExcelRowVo.getPbcCode().length() != 12) {
                            log.info("导入组织失败:" + "请填写" + JnnsOrganizationExcelRowVo.getName() + "机构正确的人行机构号！");
                            return;
                        }
                    } else {
                        JnnsOrganizationExcelRowVo.setPbcCode(null);
                    }
                }
                String message = this.validatePbcAccount(this.toPbcAccountExcelRowVo(reordered));
                if (StringUtils.isNotBlank(message)) {

                    return;
                }
                for (JnnsOrganizationExcelRowVo JnnsOrganizationExcelRowVo : reordered) {
                    String parentCode = JnnsOrganizationExcelRowVo.getParentCode();
                    OrganizationDto organizationDto = ConverterService.convert(JnnsOrganizationExcelRowVo, OrganizationDto.class);
                    OrganizationDto exist = organizationService.findByCode(JnnsOrganizationExcelRowVo.getCode());
                    if (exist != null) {
                        organizationDto.setId(exist.getId());
                    }
                    OrganizationDto parentOrgDto = organizationService.findByCode(parentCode);
                    organizationDto.setParentId(parentOrgDto.getId());
                    organizationDto = organizationService.save(organizationDto);

                    JnnsOrganizationPbcAccountExcelRowVo opaerv = new JnnsOrganizationPbcAccountExcelRowVo();
                    BeanUtils.copyProperties(JnnsOrganizationExcelRowVo, opaerv);
                    this.uploadSaveAccount(opaerv, organizationDto.getId());

                    //取消核准机构新增
                    if (StringUtils.isNotBlank(JnnsOrganizationExcelRowVo.getCancelHeZhunOrg())) {
                        if ("是".equals(JnnsOrganizationExcelRowVo.getCancelHeZhunOrg())) {
                            OrganRegisterDto organRegisterDto = new OrganRegisterDto();
                            organRegisterDto.setName(JnnsOrganizationExcelRowVo.getName());
                            organRegisterDto.setOrganId(organizationDto.getId());
                            organRegisterDto.setFullId(organizationDto.getFullId());
                            organRegisterDto.setPbcCode(organizationDto.getPbcCode());
                            organRegisterService.save(organRegisterDto);
                        }
                    }
                }
                log.info("机构初始化处理成功");
            }
        } catch (Exception e) {
            log.error("导入组织失败", e);
        }

    }

    @Override
    public void uploadUser() {
        int success = 0;
        int fail = 0;
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            URL url = classLoader.getResource("init/initUsers.xls");
            if (url == null || StringUtils.isBlank(url.getPath())) {
                return;
            }
            log.info("开始初始化用户数据");
            File file = new File(url.getPath());
            ImportExcel importExcel = new ImportExcel(file, 0, 0);
            List<JnnsUserExcelRowVo> dataList = importExcel.getDataList(JnnsUserExcelRowVo.class);
            String defaultPassword = new BCryptPasswordEncoder(12).encode("123456");
            for (JnnsUserExcelRowVo userExcelRowVo : dataList) {
                if (StringUtils.isEmpty(userExcelRowVo.getRoleCode())) {
                    userExcelRowVo.setRoleCode("0");
                }
                UserDto userDto = ConverterService.convert(userExcelRowVo, UserDto.class);
                userDto.setPassword(defaultPassword);
                OrganizationDto byCode = organizationService.findByCode(userExcelRowVo.getOrgCode());
                RoleDto roleDto = roleService.findByCode(userExcelRowVo.getRoleCode());
                if (StringUtils.isBlank(userDto.getUsername()) && StringUtils.isBlank(userExcelRowVo.getOrgCode())) {
                    continue;
                }
                if (byCode == null) {
                    log.info("【" + userDto.getUsername() + "】没有找到所属机构，机构号;" + userExcelRowVo.getOrgCode());
                    fail++;
                    continue;
                }
                if (byCode != null) {
                    userDto.setOrgId(byCode.getId());
                }
                userDto.setEnabled(Boolean.TRUE);
                UserDto exist = userService.findByUsername(userDto.getUsername());
                if (exist != null) {
                    userDto.setId(exist.getId());
                }
                if (roleDto != null) {
                    userDto.setRoleId(roleDto.getId());
                } else {
                    userDto.setRoleId(-1l);
                }
                userService.save(userDto);
                success++;
            }
            log.info("导入成功" + success + "条，失败" + fail + "条,失败详细请查看日志。");
        } catch (Exception e) {
            log.error("导入用户失败", e);
        }
    }


    private List<JnnsOrganizationExcelRowVo> reorder(JnnsOrganizationExcelRowVo root, List<JnnsOrganizationExcelRowVo> dataList) {
        List<JnnsOrganizationExcelRowVo> reorderedDataList = new LinkedList<>();
        final String code = root.getCode();
        Collection children = CollectionUtils.select(dataList, new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                JnnsOrganizationExcelRowVo vo = (JnnsOrganizationExcelRowVo) object;
                return StringUtils.equalsIgnoreCase(code, vo.getParentCode());
            }
        });
        if (children.isEmpty()) {
            reorderedDataList.add(root);
            return reorderedDataList;
        }
        reorderedDataList.add(root);
        for (Object child : children) {
            reorderedDataList.addAll(reorder((JnnsOrganizationExcelRowVo) child, dataList));
        }
        return reorderedDataList;
    }


    private String validatePbcAccount(List<JnnsOrganizationPbcAccountExcelRowVo> reordered) {
        String message = "";
        Set<String> pbcAmsErrorList = new HashSet<>();
        Set<String> pbcAmsFourErrorList = new HashSet<>();
        Set<String> picpErrorList = new HashSet<>();
        Set<String> eccsErrorList = new HashSet<>();
        for (JnnsOrganizationPbcAccountExcelRowVo opaerv : reordered) {
            //验证人行2级用户名密码
            String pbcAmsUsername = opaerv.getPbcAmsUsername();
            String pbcAmsPassword = opaerv.getPbcAmsPassword();
            if (StringUtils.isNotEmpty(pbcAmsUsername) && StringUtils.isNotEmpty(pbcAmsPassword)
                    && pbcAmsUsername.split(";").length != pbcAmsPassword.split(";").length) {
                pbcAmsErrorList.add(opaerv.getCode());
            }
            //验证人行4级用户名密码
            String pbcAmsFourUsername = opaerv.getPbcAmsFourUsername();
            String pbcAmsFourPassword = opaerv.getPbcAmsFourPassword();
            if (StringUtils.isNotEmpty(pbcAmsFourUsername) && StringUtils.isNotEmpty(pbcAmsFourPassword)
                    && pbcAmsFourUsername.split(";").length != pbcAmsFourPassword.split(";").length) {
                pbcAmsFourErrorList.add(opaerv.getCode());
            }
            //验证机构信用代码系统用户名密码
            String eccsUsername = opaerv.getEccsUsername();
            String eccsPassword = opaerv.getEccsPassword();
            if (StringUtils.isNotEmpty(eccsUsername) && StringUtils.isNotEmpty(eccsPassword)
                    && eccsUsername.split(";").length != eccsPassword.split(";").length) {
                eccsErrorList.add(opaerv.getCode());
            }
        }
        if (pbcAmsErrorList.size() > 0 || pbcAmsFourErrorList.size() > 0 || picpErrorList.size() > 0 || eccsErrorList.size() > 0) {
            String pbcAmsErrorStr = StringUtils.join(pbcAmsErrorList.toArray(), ";");
            String pbcAmsFourErrorStr = StringUtils.join(pbcAmsFourErrorList.toArray(), ";");
            String picpErrorStr = StringUtils.join(picpErrorList.toArray(), ";");
            String eccsErrorStr = StringUtils.join(eccsErrorList.toArray(), ";");
            if (pbcAmsErrorStr.length() > 0) {
                message += "核心机构号为：" + pbcAmsErrorStr + "的机构存在“人行2级用户名密码”对应个数错误！";
            }
            if (picpErrorStr.length() > 0) {
                message += "核心机构号为：" + picpErrorStr + "的机构存在“联网核查用户名密码”对应个数错误！";
            }
            if (pbcAmsFourErrorStr.length() > 0) {
                message += "核心机构号为：" + pbcAmsFourErrorStr + "的机构存在“人行4级用户名密码”对应个数错误！";
            }
            if (eccsErrorStr.length() > 0) {
                message += "核心机构号为：" + eccsErrorStr + "的机构存在“机构信用代码系统用户名密码”对应个数错误！";
            }
        }
        return message;
    }

    private void uploadSaveAccount(JnnsOrganizationPbcAccountExcelRowVo organizationExcelRowVo, Long organId) {
        //人行2级用户名密码
        //人行4级用户名密码
        if (StringUtils.isNotEmpty(organizationExcelRowVo.getPbcIp()) &&
                StringUtils.isNotEmpty(organizationExcelRowVo.getPbcAmsFourUsername()) &&
                StringUtils.isNotEmpty(organizationExcelRowVo.getPbcAmsFourPassword())) {
            String[] userNameArr = organizationExcelRowVo.getPbcAmsFourUsername().split(";");
            String[] passWordArr = organizationExcelRowVo.getPbcAmsFourPassword().split(";");
            log.info("需处理条数：" + userNameArr.length);
            for (int i = 0; i < userNameArr.length; i++) {
                PbcAccountDto amsAccount = new PbcAccountDto();
                amsAccount.setIp(organizationExcelRowVo.getPbcIp());
                amsAccount.setOrgId(organId);
                amsAccount.setAccount(userNameArr[i]);
                amsAccount.setPassword(passWordArr[i]);
                amsAccount.setAccountType(EAccountType.AMS);
                amsAccount.setAccountStatus(EAccountStatus.NEW);
                amsAccount.setEnabled(Boolean.TRUE);
                pbcAccountService.uploadSave(amsAccount);
            }
        }
        if (StringUtils.isNotEmpty(organizationExcelRowVo.getPbcIp()) &&
                StringUtils.isNotEmpty(organizationExcelRowVo.getPbcAmsUsername()) &&
                StringUtils.isNotEmpty(organizationExcelRowVo.getPbcAmsPassword())) {
            String[] userNameArr = organizationExcelRowVo.getPbcAmsUsername().split(";");
            String[] passWordArr = organizationExcelRowVo.getPbcAmsPassword().split(";");
            log.info("需处理条数：" + userNameArr.length);
            for (int i = 0; i < userNameArr.length; i++) {
                PbcAccountDto amsAccount = new PbcAccountDto();
                amsAccount.setAccountType(EAccountType.AMS);
                amsAccount.setOrgId(organId);
                amsAccount.setAccount(userNameArr[i]);
                amsAccount.setPassword(passWordArr[i]);
                amsAccount.setIp(organizationExcelRowVo.getPbcIp());
                amsAccount.setAccountStatus(EAccountStatus.NEW);
                amsAccount.setEnabled(Boolean.TRUE);
                pbcAccountService.uploadSave(amsAccount);
            }
        }
        //机构信用代码系统用户名密码
        if (StringUtils.isNotEmpty(organizationExcelRowVo.getPbcIp()) &&
                StringUtils.isNotEmpty(organizationExcelRowVo.getEccsUsername()) &&
                StringUtils.isNotEmpty(organizationExcelRowVo.getEccsPassword())) {
            String[] userNameArr = organizationExcelRowVo.getEccsUsername().split(";");
            String[] passWordArr = organizationExcelRowVo.getEccsPassword().split(";");
            log.info("需处理条数：" + userNameArr.length);
            for (int i = 0; i < userNameArr.length; i++) {
                PbcAccountDto eccsAccount = new PbcAccountDto();
                eccsAccount.setOrgId(organId);
                eccsAccount.setAccountStatus(EAccountStatus.NEW);
                eccsAccount.setIp(organizationExcelRowVo.getPbcIp());
                eccsAccount.setAccount(userNameArr[i]);
                eccsAccount.setPassword(passWordArr[i]);
                eccsAccount.setAccountType(EAccountType.ECCS);
                eccsAccount.setEnabled(Boolean.TRUE);
                pbcAccountService.uploadSave(eccsAccount);
            }
        }
    }

    private List<JnnsOrganizationPbcAccountExcelRowVo> toPbcAccountExcelRowVo(List<JnnsOrganizationExcelRowVo> reordered) {
        List<JnnsOrganizationPbcAccountExcelRowVo> list = new ArrayList<>();
        for (JnnsOrganizationExcelRowVo oerv : reordered) {
            JnnsOrganizationPbcAccountExcelRowVo opaerv = new JnnsOrganizationPbcAccountExcelRowVo();
            BeanUtils.copyProperties(oerv, opaerv);
            list.add(opaerv);
        }
        return list;
    }

}
