package com.ideatech.ams.controller;

import com.alibaba.fastjson.JSON;
import com.ideatech.ams.config.AmsWebProperties;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.role.dto.RoleDto;
import com.ideatech.ams.system.role.service.RoleService;
import com.ideatech.ams.system.trace.aop.OperateLog;
import com.ideatech.ams.system.trace.enums.OperateModule;
import com.ideatech.ams.system.trace.enums.OperateType;
import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.ams.system.user.dto.UserSearchDto;
import com.ideatech.ams.system.user.service.UserSendService;
import com.ideatech.ams.system.user.service.UserService;
import com.ideatech.ams.vo.UserExcelRowVo;
import com.ideatech.common.constant.ResultCode;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.excel.util.ExportExcel;
import com.ideatech.common.excel.util.ImportExcel;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.RegexUtils;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.jws.WebMethod;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * @author liangding
 * @create 2018-05-06 下午7:51
 **/
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AmsWebProperties amsWebProperties;

    @Autowired
    private RoleService roleService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private UserSendService userSendService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //账管登录密码校验
    @Value("${amsLoginPwdRule.enabled:false}")
    private Boolean amsLoginPwdRuleEnabled;

    @Value("${ams.web.defaultPassword}")
    private String defaultPassword;

    @GetMapping("/page")
    public ResultDto page(UserSearchDto userSearchDto) {
        SecurityUtils.UserInfo currentUser = SecurityUtils.getCurrentUser();
        RoleDto currentRole = roleService.findById(currentUser.getRoleId());
        String orgFullId = null;
        if(!"0".equalsIgnoreCase(currentRole.getLevel())) {
            orgFullId = currentUser.getOrgFullId();
            userSearchDto.setOrgFullId(orgFullId);
        }
        userSearchDto = userService.search(userSearchDto);
        for (UserDto userDto : userSearchDto.getList()) {
            Long roleId = userDto.getRoleId();
            Long orgId = userDto.getOrgId();

            if (roleId != null) {
                RoleDto byId = roleService.findById(roleId);
                if (byId != null) {
                    userDto.setRoleName(byId.getName());
                }else{
                    userDto.setRoleName("未定义");
                }
            }
            if (orgId != null) {
                OrganizationDto byId = organizationService.findById(orgId);
                if (byId != null) {
                    userDto.setOrgName(byId.getName());
                }
            }

            ConfigDto pwdExpireCheckConfig = configService.findOneByConfigKey("pwdExpireCheck");
            ConfigDto pwdExpireDayConfig = configService.findOneByConfigKey("pwdExpireDay");
            String pwdExpireDay = "";
            Integer betweenDate = null;
            Integer betweenDay = null;
            if(pwdExpireCheckConfig != null && pwdExpireDayConfig != null) {
                if("true".equals(pwdExpireCheckConfig.getConfigValue())) { //启用密码有效期控制
                    if(!"virtual".equals(userDto.getUsername()) && !"admin".equals(userDto.getUsername())) {
                        pwdExpireDay = pwdExpireDayConfig.getConfigValue();
                        if(userDto.getPwdUpdateDate() != null) {
                            try {
                                betweenDate = DateUtils.daysBetween(userDto.getPwdUpdateDate(), new Date());
                                betweenDay = Integer.parseInt(pwdExpireDay) - betweenDate;
                                userDto.setPwdExpireDay(betweenDay > 0 ? betweenDay : 0);
                            } catch (ParseException e) {
                                log.error("密码有效期转换错误", e);
                            }

                        }

                    }
                }
            }

        }
        return ResultDtoFactory.toAckData(userSearchDto);
    }

    @GetMapping("/{id}")
    public ResultDto<UserDto> findById(@PathVariable("id") Long id) {
        UserDto userDto = userService.findById(id);
        return ResultDtoFactory.toAckData(userDto);
    }

    @OperateLog(operateModule = OperateModule.USER,operateType = OperateType.UPDATE)
    @PostMapping("/{id}")
    public ResultDto update(@PathVariable("id") Long id,UserDto userDto) {
        if (userDto.getOrgId() == null) {
            return ResultDtoFactory.toNack("必须为用户指定一个机构！");
        }
        if (StringUtils.isNotBlank(userDto.getPassword())) {
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        userDto = userService.save(userDto);
        userService.grant(userDto.getId(), userDto.getRoleId());
        return ResultDtoFactory.toAck();
    }

    @OperateLog(operateModule = OperateModule.USER,operateType = OperateType.OTHER,operateContent = "密码重置",cover = true)
    @PutMapping("/{id}/reset")
    public ResultDto resetPasword(@PathVariable("id") Long id) {
        String defaultPassword;
        if(amsLoginPwdRuleEnabled) {  //账管登录密码复杂校验规则启用
            defaultPassword = amsWebProperties.getDefaultSM4Password();
        } else {
            defaultPassword = amsWebProperties.getDefaultPassword();
        }

        userService.changePassword(id, passwordEncoder.encode(defaultPassword));
        return ResultDtoFactory.toAck();
    }

    @PostMapping("/change")
    public ResultDto changePassword(@RequestParam(required = true) String oldPassword,
                                    @RequestParam(required = true) String password1,
                                    @RequestParam(required = true) String password2, String username) {
        Long currentUserId;
        String pwdValidateErrorMsg;
        try {
            oldPassword = desEncrypt(oldPassword, "ideatech20180223", "ideatech20180223");
            password1 = desEncrypt(password1, "ideatech20180223", "ideatech20180223");
            password2 = desEncrypt(password2, "ideatech20180223", "ideatech20180223");
        } catch (Exception e) {
           log.error("密码解码异常：{}",e);
        }
        if(!StringUtils.equals(password1, password2)){
            return ResultDtoFactory.toNack("两次输入的密码不一致，请检查后重新提交!");
        }
        if(StringUtils.equals(oldPassword, password1)){
            return ResultDtoFactory.toNack("新密码与原密码不能相同");
        }
        if(amsLoginPwdRuleEnabled) {  //是否启用账管密码校验
            pwdValidateErrorMsg = pwdValidateMsg(password1);
            if(StringUtils.isNotBlank(pwdValidateErrorMsg)) {
                return ResultDtoFactory.toNack(pwdValidateErrorMsg);
            }
        }

        if(StringUtils.isNotBlank(username)) {
            UserDto user = userService.findByUsername(username);
            currentUserId = user.getId();
        } else {
            currentUserId = SecurityUtils.getCurrentUserId();
        }
        String passwordByUserId = userService.findPasswordByUserId(currentUserId);
        if (passwordByUserId == null) {
            return ResultDtoFactory.toNack("当前用户状态异常，请检查后重新提交！");
        }

        if(!passwordEncoder.matches(oldPassword, passwordByUserId)){
            return ResultDtoFactory.toNack("当前密码输入错误，请检查后重新提交！");
        }

        userService.changePassword(currentUserId, passwordEncoder.encode(password1));
        return ResultDtoFactory.toAck();
    }

    @OperateLog(operateModule = OperateModule.USER,operateType = OperateType.DISABLE)
    @PutMapping("/{id}/disable")
    public ResultDto disable(@PathVariable("id") Long id){
        userService.disable(id);
        return ResultDtoFactory.toAck();
    }

    @OperateLog(operateModule = OperateModule.USER,operateType = OperateType.ENABLE)
    @PutMapping("/{id}/enable")
    public ResultDto enable(@PathVariable("id") Long id){
        userService.enable(id);
        return ResultDtoFactory.toAck();
    }

    /**
     * 初始化所有用户密码更新时间
     * @return
     */
    @PutMapping("/pwdUpdateDate")
    public ResultDto pwdUpdateDate(){
        userService.pwdUpdateDate();
        return ResultDtoFactory.toAck();
    }

    @OperateLog(operateModule = OperateModule.USER,operateType = OperateType.INSERT)
    @PostMapping("/")
    public ResultDto create(UserDto userDto) {
        String pwdValidateErrorMsg;

        if (userDto.getOrgId() == null) {
            return ResultDtoFactory.toNack("必须为用户指定一个机构！");
        }
        if (StringUtils.isNotBlank(userDto.getPassword())) {
            if(amsLoginPwdRuleEnabled) {  //是否启用账管密码校验
                pwdValidateErrorMsg = pwdValidateMsg(userDto.getPassword());
                if(StringUtils.isNotBlank(pwdValidateErrorMsg)) {
                    return ResultDtoFactory.toNack(pwdValidateErrorMsg);
                }
            }

            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }else{
            //用户名新增时为空就赋值默认值 123456
            userDto.setPassword(passwordEncoder.encode(amsWebProperties.getDefaultPassword()));
        }
        if (userService.findByUsername(userDto.getUsername()) != null) {
            return ResultDtoFactory.toNack(String.format("该用户名[%s]已存在", userDto.getUsername()));
        }
        // -- 海峡银行推送统一认证  start
        userDto.setUserStatus("create");
        String result = userSendService.sendUserDto2Tongyi(userDto);
        if(result.contains("失败")){
            return ResultDtoFactory.toNack(result);
        }
        // -- end
        userDto = userService.save(userDto);
        userService.grant(userDto.getId(), userDto.getRoleId());
        return ResultDtoFactory.toAck();
    }

    @OperateLog(operateModule = OperateModule.USER,operateType = OperateType.DELETE)
    @DeleteMapping("/{id}")
    public ResultDto delete(@PathVariable("id") Long id) {
        if (id.equals(SecurityUtils.getCurrentUserId())) {
            return ResultDtoFactory.toNack("用户不能删除自己。");
        }
        UserDto userDto = userService.findById(id);
        // -- 海峡银行推送统一认证  --start
        userDto.setUserStatus("delete");
        String result = userSendService.sendUserDto2Tongyi(userDto);
        if(result.contains("失败")){
            return ResultDtoFactory.toNack(result);
        }
        // -- end
        userService.deleteById(id);
        return ResultDtoFactory.toAck();
    }

    @OperateLog(operateModule = OperateModule.USER,operateType = OperateType.IMPORT,operateContent = "导入")
    @PostMapping(value = "/upload")
    @WebMethod(exclude = true)
    public void upload(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException {
        ResultDto dto = new ResultDto();
        String defaultPassword;

        try {
            ImportExcel importExcel = new ImportExcel(file, 0, 0);
            if (importExcel.getRow(0).getPhysicalNumberOfCells() < 5) {
                ResultDtoFactory.toNack("导入失败，错误的模板");
                dto.setCode(ResultCode.NACK);
                dto.setMessage("导入失败，错误的模板");
            } else {
                int success=0;
                int fail = 0;
                List<UserExcelRowVo> dataList = importExcel.getDataList(UserExcelRowVo.class);
                if(amsLoginPwdRuleEnabled) {
                    defaultPassword = passwordEncoder.encode(amsWebProperties.getDefaultSM4Password());
                } else {
                    defaultPassword = passwordEncoder.encode(amsWebProperties.getDefaultPassword());
                }

                for (UserExcelRowVo userExcelRowVo : dataList) {
                    if (StringUtils.isEmpty(userExcelRowVo.getRoleCode())) {
                        userExcelRowVo.setRoleCode(amsWebProperties.getDefaultRoleCode());
                    }
                    UserDto userDto = ConverterService.convert(userExcelRowVo, UserDto.class);
                    userDto.setPassword(defaultPassword);

                    if(!"virtual".equals(userDto.getUsername())) { //设置密码更新时间
                        userDto.setPwdUpdateDate(new Date());
                    }

                    OrganizationDto byCode = organizationService.findByCode(userExcelRowVo.getOrgCode());
                    RoleDto roleDto = roleService.findByCode(userExcelRowVo.getRoleCode());
                    if(StringUtils.isBlank(userDto.getUsername()) && StringUtils.isBlank(userExcelRowVo.getOrgCode())){
                        continue;
                    }
                    if(byCode == null){
                        log.info("【{}】没有找到所属机构，机构号;{}",userDto.getUsername(),userExcelRowVo.getOrgCode());
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
                    }else{
                        userDto.setRoleId(-1l);
                    }
                    userService.save(userDto);
                    success++;
                }
                dto.setCode(ResultCode.ACK);
                dto.setMessage("导入成功"+success+"条，失败"+fail+"条,失败详细请查看日志。");
            }

            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(dto));
//            return ResultDtoFactory.toAck();
        } catch (Exception e) {
            log.error("导入用户失败", e);
            dto.setCode(ResultCode.NACK);
            dto.setMessage("导入用户失败");
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(dto));
//            return ResultDtoFactory.toNack("导入用户失败");
        }
    }

    @OperateLog(operateModule = OperateModule.USER,operateType = OperateType.EXPORT,operateContent = "用户批量导出")
    @GetMapping("/export")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        //获取当前用户OrgId
        Long orgId = SecurityUtils.getCurrentUser().getOrgId();
        IExcelExport sysUserExcel = userService.exportUserByOrgId(orgId);
        StringBuilder fileName = new StringBuilder();
        fileName.append(System.currentTimeMillis());
        response.setHeader("Content-disposition", "attachment; filename="+fileName+".xls");
        response.setContentType("application/octet-stream");
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        ExportExcel.export(response.getOutputStream(),"yyyy-MM-dd",sysUserExcel);
        toClient.flush();

        response.getOutputStream().close();
    }

    private String pwdValidateMsg(String password) {
        if(password.length() < 8 || password.length() > 20) {
            return "密码长度至少8位，最多不超过20位";
        } else if(!RegexUtils.loginPasswordValidate(password)) {
            return "密码组成需要包含大写字母、小写字母、数字";
        }

        return "";
    }
    public static String desEncrypt(String data, String key, String iv) throws Exception {
        try {
            byte[] encrypted1 = new Base64().decode(data);

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original);
            return originalString.trim();
        } catch (Exception e) {
            return null;
        }
    }
}
