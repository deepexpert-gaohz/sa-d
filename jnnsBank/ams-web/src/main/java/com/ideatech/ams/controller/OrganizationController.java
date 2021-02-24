package com.ideatech.ams.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ideatech.ams.apply.service.SynchronizeOrgService;
import com.ideatech.ams.customer.dto.illegal.IllegalQueryBatchDto;
import com.ideatech.ams.customer.dto.illegal.IllegalQueryDto;
import com.ideatech.ams.service.MoveOrganDataService;
import com.ideatech.ams.system.org.dto.*;
import com.ideatech.ams.system.org.entity.OrganRegisterPo;
import com.ideatech.ams.system.org.entity.OrganizationPo;
import com.ideatech.ams.system.org.service.OrganRegisterService;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.org.service.OrganizationSyncService;
import com.ideatech.ams.system.pbc.dto.PbcAccountDto;
import com.ideatech.ams.system.pbc.enums.EAccountStatus;
import com.ideatech.ams.system.pbc.enums.EAccountType;
import com.ideatech.ams.system.pbc.service.PbcAccountService;
import com.ideatech.ams.system.sm4.service.EncryptPwdService;
import com.ideatech.ams.system.trace.aop.OperateLog;
import com.ideatech.ams.system.trace.enums.OperateModule;
import com.ideatech.ams.system.trace.enums.OperateType;
import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.ams.system.user.dto.UserSearchDto;
import com.ideatech.ams.system.user.service.UserService;
import com.ideatech.ams.vo.Illegal.IllegalQueryVo;
import com.ideatech.ams.vo.OrganizationExcelRowVo;
import com.ideatech.ams.vo.OrganizationPbcAccountExcelRowVo;
import com.ideatech.common.constant.IdeaConstant;
import com.ideatech.common.constant.ResultCode;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.excel.util.ExportExcel;
import com.ideatech.common.excel.util.ImportExcel;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.util.BrowserUtil;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.SecurityUtils;
import com.ideatech.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.jws.WebMethod;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * @author liangding
 * @create 2018-05-04 上午9:59
 **/
@RestController
@RequestMapping("/organization")
@Slf4j
public class OrganizationController {

    @Value("${apply.task.syncScheduletiming.flag}")
    private boolean syncFlag;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private UserService userService;

    @Autowired
    private PbcAccountService pbcAccountService;

    @Autowired
    private MoveOrganDataService moveOrganDataService;

    @Autowired
    private OrganizationSyncService organizationSyncService;

    @Autowired
    private SynchronizeOrgService synchronizeOrgService;

    @Autowired
    private OrganRegisterService organRegisterService;

    @Autowired
    private EncryptPwdService encryptPwdService;

    @GetMapping("/orgTree")
    public ResultDto list(@RequestParam(required = false) Long parentId, @RequestParam(required = false) String name, @RequestParam(required = false) String code) {
        SecurityUtils.UserInfo current = SecurityUtils.getCurrentUser();
        Long orgId = current.getOrgId();
        OrganizationDto organizationDto = organizationService.findById(orgId);

        Map<String, OrganizationDto> allList = organizationService.findAllInMap();
        Set<String> listSet = allList.keySet();

        for(String orgFullid : listSet){
            if(orgFullid.contains(organizationDto.getFullId() + "-")){
                organizationDto.setChilds(Boolean.TRUE);
                break;
            }else {
                organizationDto.setChilds(Boolean.FALSE);
            }
        }

        //找出取消核准的所有机构
        List<OrganRegisterDto> organRegisterDtos = organRegisterService.list();
        if (StringUtils.isNotBlank(name) || StringUtils.isNotBlank(code)) {
            List<OrganizationDto> organizationDtos = new ArrayList<>();
            // 根据名称搜索所有后代机构，将所有后代机构并行排列
            List<OrganizationDto> organs = organizationService.searchChildByNameAndCode(orgId, name, code);
            for(OrganizationDto organizationDto1 : organs){
                for(OrganRegisterDto organRegisterDto : organRegisterDtos){
                    if(organRegisterDto.getOrganId().equals(organizationDto1.getId())){
                        organizationDto1.setCancelHeZhun(true);
                        break;
                    }
                }
                organizationDtos.add(organizationDto1);
            }
            return ResultDtoFactory.toAckData(organizationDtos);
        }
        if (parentId == null || parentId < 0) {
            // 如果没有查询条件，默认带出用户所属机构
            List<OrganizationDto> organizationDtos = new ArrayList<>();
            for(OrganRegisterDto organRegisterDto : organRegisterDtos){
                if(organRegisterDto.getOrganId().equals(organizationDto.getId())){
                    organizationDto.setCancelHeZhun(true);
                    break;
                }
            }
            organizationDtos.add(organizationDto);
            return ResultDtoFactory.toAckData(organizationDtos);
        } else {
            // 根据parentId查询，只带出直接子机构
            List<OrganizationDto> organizationDtos = organizationService.searchChild(parentId, name);
            for(OrganizationDto organizationDto1 : organizationDtos){
                for(String orgFullid : listSet){
                    if(orgFullid.contains(organizationDto1.getFullId() + "-")){
                        organizationDto1.setChilds(Boolean.TRUE);
                        break;
                    }else {
                        organizationDto1.setChilds(Boolean.FALSE);
                    }
                }
                for(OrganRegisterDto organRegisterDto : organRegisterDtos){
                    if(organizationDto1.getId().equals(organRegisterDto.getOrganId())){
                        organizationDto1.setCancelHeZhun(true);
                        break;
                    }
                }
            }
            return ResultDtoFactory.toAckData(organizationDtos);
        }
    }

    @GetMapping("/{id}")
    public ResultDto<OrganizationDto> findById(@PathVariable(name = "id") Long id) {
        OrganizationDto byId = organizationService.findById(id);
        return ResultDtoFactory.toAckData(byId);
    }

    @GetMapping("/orgSyncFlag")
    public ResultDto<Boolean> orgSyncFlag() {
        return ResultDtoFactory.toAckData(syncFlag);
    }


    @OperateLog(operateModule = OperateModule.ORGANIZATION,operateType = OperateType.INSERT)
    @PostMapping("/")
    public ResultDto save(OrganizationDto organizationDto) {
        OrganizationDto dto = organizationService.findByName(organizationDto.getName());
        if(dto != null){
            return ResultDtoFactory.toNack("机构名称重复，请重新录入...");
        }
        dto = organizationService.findByCode(organizationDto.getCode());
        if(dto != null){
            return ResultDtoFactory.toNack("机构号重复，请重新录入...");
        }

        if(StringUtils.isNotBlank(organizationDto.getInstitutionCode())) {
            OrganizationDto byInstitutionCode = organizationService.findByInstitutionCode(organizationDto.getInstitutionCode());
            if(byInstitutionCode != null) {
                return ResultDtoFactory.toNack("金融机构编码重复，请重新录入...");
            }
        }

        organizationService.save(organizationDto);
        return ResultDtoFactory.toAck();
    }

    @OperateLog(operateModule = OperateModule.ORGANIZATION, operateType = OperateType.UPDATE)
    @PutMapping("/{id}")
    public ResultDto update(OrganizationDto organizationDto, @PathVariable("id") Long id) {
        OrganizationDto dto = organizationService.findByCodeAndIdNot(organizationDto.getCode(), id);
        if(dto != null) {
            return ResultDtoFactory.toNack("机构号重复，请重新录入...");
        }

        if(StringUtils.isNotBlank(organizationDto.getInstitutionCode())) {
            OrganizationDto organ = organizationService.findByInstitutionCodeAndIdNot(organizationDto.getInstitutionCode(), id);
            if(organ != null) {
                return ResultDtoFactory.toNack("金融机构编码重复，请重新录入...");
            }
        }

        organizationDto.setId(id);
        organizationService.save(organizationDto);
        return ResultDtoFactory.toAck();
    }

    @OperateLog(operateModule = OperateModule.ORGANIZATION, operateType = OperateType.DELETE)
    @DeleteMapping("/{id}")
    public ResultDto delete(@PathVariable("id") Long id) {
        List<UserDto> byOrgId = userService.findByOrgId(id);
        if (byOrgId.size() > 0) {
            return ResultDtoFactory.toNack("当前机构下有用户存在，无法删除！");
        }
        List<OrganizationDto> organizationDtos = organizationService.searchChild(id, null);
        if (organizationDtos.size() > 0) {
            return ResultDtoFactory.toNack("当前机构下有子机构，无法删除！");
        }
        organizationService.delete(id);
        return ResultDtoFactory.toAck();
    }

    @GetMapping("/root")
    public ResultDto root() {
        SecurityUtils.UserInfo current = SecurityUtils.getCurrentUser();
        Long orgId = current.getOrgId();
        OrganizationDto organizationDto = organizationService.findById(orgId);
        return ResultDtoFactory.toAckData(organizationDto);
    }

    @GetMapping("/orgSyncRecord/page")
    public ResultDto userPage(OrganizationSyncSearchDto organizationSyncSearchDto) {
        return ResultDtoFactory.toAckData(organizationSyncService.search(organizationSyncSearchDto));
    }

    @GetMapping("/orgSyncRecord/recall/{id}")
    public ResultDto recall(@PathVariable("id") Long id) {
        OrganizationSyncDto organizationSyncDto = organizationSyncService.findById(id);
        if(organizationSyncDto != null){
            if(organizationSyncDto.getSyncFinishStatus() && !organizationSyncDto.getSyncSuccessStatus()){
                String result = synchronizeOrgService.syncOrg(organizationSyncDto);
                if(result == null){//成功
                    return ResultDtoFactory.toAck();
                }else{//失败
                    return ResultDtoFactory.toNack(result);
                }
            }else{
                return ResultDtoFactory.toNack("机构同步记录的状态不对，必须为同步完成且同步状态为失败");
            }
        }else{
            return ResultDtoFactory.toNack("无法找到对应的机构同步记录");
        }
    }

    @GetMapping("/user/page")
    public ResultDto userPage(UserSearchDto userSearchDto) {
        if (null == userSearchDto.getOrgId()) {
            return ResultDtoFactory.toAck();
        }
        return ResultDtoFactory.toAckData(userService.search(userSearchDto));
    }

    @DeleteMapping("/{orgId}/{userId}")
    public ResultDto unbind(@PathVariable("orgId") Long orgId, @PathVariable("userId") Long userId) {
        userService.unbind(userId, orgId);
        return ResultDtoFactory.toAck();
    }

    @GetMapping("/{id}/user")
    public ResultDto<List<UserDto>> getOrgUsers(@PathVariable("id") Long id) {
        List<UserDto> userDtos = userService.findByOrgId(id);
        return ResultDtoFactory.toAckData(userDtos);
    }

    @PutMapping("/{id}/user")
    public ResultDto updateOrgUser(@PathVariable("id") Long id, String members) {
        String[] split = StringUtils.split(members, ",");
        List<Long> userIds = new ArrayList<>();
        for (String s : split) {
            userIds.add(Long.parseLong(s));
        }
        userService.updateOrg(id, userIds);
        return ResultDtoFactory.toAck();
    }

    /**
     * 机构导入
     */
    @OperateLog(operateModule = OperateModule.ORGANIZATION, operateType = OperateType.IMPORT,operateContent = "机构导入")
    @PostMapping("/upload")
    public void upload(MultipartFile file ,HttpServletResponse response) throws IOException {
        ResultDto dto = new ResultDto();
        try {
            ImportExcel importExcel = new ImportExcel(file, 0, 0);
            List<OrganizationExcelRowVo> dataList = importExcel.getDataList(OrganizationExcelRowVo.class);
            //根据parentCode对组织列表进行排序，parent在前，children在后

            Collection roots = CollectionUtils.select(dataList, new Predicate() {
                @Override
                public boolean evaluate(Object object) {
                    OrganizationExcelRowVo vo = (OrganizationExcelRowVo) object;
                    return StringUtils.equalsIgnoreCase(vo.getParentCode(), IdeaConstant.ORG_ROOT_CODE);
                }
            });

            if (roots.isEmpty() || roots.size() > 1) {
                //return ResultDtoFactory.toNack("根组织必须有且只能有一个");
                dto.setCode(ResultCode.NACK);
                dto.setMessage("根组织必须有且只能有一个");
            }else{
                OrganizationExcelRowVo root = (OrganizationExcelRowVo) roots.toArray()[0];
                List<OrganizationExcelRowVo> reordered = reorder(root, dataList);

                for (OrganizationExcelRowVo organizationExcelRowVo : reordered) {
                    if(StringUtils.isNotEmpty(organizationExcelRowVo.getMobile()) && !organizationExcelRowVo.getMobile().matches("^1\\d{10}$")){
                        dto.setCode(ResultCode.NACK);
                        dto.setMessage("导入组织失败:" + organizationExcelRowVo.getName() + "的联系人手机号格式不正确！");
                        response.setContentType("text/html; charset=utf-8");
                        response.getWriter().write(JSON.toJSONString(dto));
                        return;
                    }
                }

                for (OrganizationExcelRowVo organizationExcelRowVo : reordered) {
                    if(StringUtils.isNotEmpty(organizationExcelRowVo.getPbcCode())){
                        if(organizationExcelRowVo.getPbcCode().length() != 12){
                            dto.setCode(ResultCode.NACK);
                            dto.setMessage("导入组织失败:" + "请填写" + organizationExcelRowVo.getName() + "机构正确的人行机构号！");
                            response.setContentType("text/html; charset=utf-8");
                            response.getWriter().write(JSON.toJSONString(dto));
                            return;
                        }
                    }else{
                        organizationExcelRowVo.setPbcCode(null);
                    }
                }

                String message = this.validatePbcAccount(this.toPbcAccountExcelRowVo(reordered));
                if(StringUtils.isNotBlank(message)){
                    dto.setCode(ResultCode.NACK);
                    dto.setMessage("导入组织失败:" + message);
                    log.error(dto.getMessage());
                    response.setContentType("text/html; charset=utf-8");
                    response.getWriter().write(JSON.toJSONString(dto));
                    return;
                }

                for (OrganizationExcelRowVo organizationExcelRowVo : reordered) {
                    String parentCode = organizationExcelRowVo.getParentCode();
                    OrganizationDto organizationDto = ConverterService.convert(organizationExcelRowVo, OrganizationDto.class);
                    OrganizationDto exist = organizationService.findByCode(organizationExcelRowVo.getCode());
                    if (exist != null) {
                        organizationDto.setId(exist.getId());
                    }
                    OrganizationDto parentOrgDto = organizationService.findByCode(parentCode);
                    organizationDto.setParentId(parentOrgDto.getId());
                    organizationDto = organizationService.save(organizationDto);

                    OrganizationPbcAccountExcelRowVo opaerv = new OrganizationPbcAccountExcelRowVo();
                    BeanUtils.copyProperties(organizationExcelRowVo, opaerv);
                    this.uploadSaveAccount(opaerv, organizationDto.getId());

                    //取消核准机构新增
                    if(StringUtils.isNotBlank(organizationExcelRowVo.getCancelHeZhunOrg())){
                        if("是".equals(organizationExcelRowVo.getCancelHeZhunOrg())){
                            OrganRegisterDto organRegisterDto = new OrganRegisterDto();
                            organRegisterDto.setName(organizationExcelRowVo.getName());
                            organRegisterDto.setOrganId(organizationDto.getId());
                            organRegisterDto.setFullId(organizationDto.getFullId());
                            organRegisterDto.setPbcCode(organizationDto.getPbcCode());
                            organRegisterService.save(organRegisterDto);
                        }
                    }
                }
                dto.setCode(ResultCode.ACK);
                dto.setMessage("上传成功");
            }
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(dto));
        } catch (Exception e) {
            log.error("导入组织失败", e);
            dto.setCode(ResultCode.NACK);
            dto.setMessage("导入组织失败");
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(dto));
            //return ResultDtoFactory.toNack("导入组织失败");
        }
        //return ResultDtoFactory.toAck();
    }

    /**
     * 根据核心机构号导入人行2级4级用户、身份联网核查系统、机构信用代码系统账号密码
     */
    @OperateLog(operateModule = OperateModule.ORGANIZATION, operateType = OperateType.IMPORT,operateContent = "人行账号导入")
    @PostMapping("/uploadAccount")
    public void uploadAccount(MultipartFile file ,HttpServletResponse response) throws IOException {
        ResultDto dto = new ResultDto();
        try {
            ImportExcel importExcel = new ImportExcel(file, 0, 0);
            List<OrganizationPbcAccountExcelRowVo> dataList = importExcel.getDataList(OrganizationPbcAccountExcelRowVo.class);

            //验证账号密码是否一一对应
            String message = this.validatePbcAccount(dataList);
            if (StringUtils.isNotBlank(message)) {
                dto.setCode(ResultCode.NACK);
                dto.setMessage("导入组织失败:" + message);
                log.error(dto.getMessage());
                response.setContentType("text/html; charset=utf-8");
                response.getWriter().write(JSON.toJSONString(dto));
                return;
            }
            //验证核心机构号是否存在
            Map<String, Long> orgIdMap = new HashMap<>();//key：核心机构号。value：机构id
            Set<String> errorOrgCodeList = new HashSet<>();//错误的核心机构号
            for (OrganizationPbcAccountExcelRowVo opaerv : dataList) {
                OrganizationDto organizationDto = organizationService.findByCode(opaerv.getCode());
                if (organizationDto == null) {
                    errorOrgCodeList.add(opaerv.getCode());
                } else {
                    orgIdMap.put(opaerv.getCode(), organizationDto.getId());
                }
            }
            if (errorOrgCodeList.size() > 0) {
                dto.setCode(ResultCode.NACK);
                dto.setMessage("导入组织失败:" + "核心机构号为：" + StringUtils.join(errorOrgCodeList.toArray(), ";") + "的机构不存在！");
                log.error(dto.getMessage());
                response.setContentType("text/html; charset=utf-8");
                response.getWriter().write(JSON.toJSONString(dto));
                return;
            }
            //新增修改账号信息
            for (OrganizationPbcAccountExcelRowVo opaerv : dataList) {
                this.uploadSaveAccount(opaerv, orgIdMap.get(opaerv.getCode()));
            }
            dto.setCode(ResultCode.ACK);
            dto.setMessage("上传成功");
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(dto));
        } catch (Exception e) {
            log.error("导入组织失败", e);
            dto.setCode(ResultCode.NACK);
            dto.setMessage("导入组织失败");
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(dto));
        }
    }

    /**
     * List<OrganizationExcelRowVo>泛型转换List<OrganizationPbcAccountExcelRowVo>
     */
    private List<OrganizationPbcAccountExcelRowVo> toPbcAccountExcelRowVo(List<OrganizationExcelRowVo> reordered) {
        List<OrganizationPbcAccountExcelRowVo> list = new ArrayList<>();
        for (OrganizationExcelRowVo oerv : reordered) {
            OrganizationPbcAccountExcelRowVo opaerv = new OrganizationPbcAccountExcelRowVo();
            BeanUtils.copyProperties(oerv, opaerv);
            list.add(opaerv);
        }
        return list;
    }

    private String validatePbcAccount(List<OrganizationPbcAccountExcelRowVo> reordered) {
        String message = "";
        Set<String> pbcAmsErrorList = new HashSet<>();
        Set<String> pbcAmsFourErrorList = new HashSet<>();
        Set<String> picpErrorList = new HashSet<>();
        Set<String> eccsErrorList = new HashSet<>();
        for (OrganizationPbcAccountExcelRowVo opaerv : reordered) {
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
            //验证身份联网核查系统用户名密码
            String picpUsername = opaerv.getPicpUsername();
            String picpPassword = opaerv.getPicpPassord();
            if (StringUtils.isNotEmpty(picpUsername) && StringUtils.isNotEmpty(picpPassword)
                    && picpUsername.split(";").length != picpPassword.split(";").length) {
                picpErrorList.add(opaerv.getCode());
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
            if (pbcAmsFourErrorStr.length() > 0) {
                message += "核心机构号为：" + pbcAmsFourErrorStr + "的机构存在“人行4级用户名密码”对应个数错误！";
            }
            if (picpErrorStr.length() > 0) {
                message += "核心机构号为：" + picpErrorStr + "的机构存在“联网核查用户名密码”对应个数错误！";
            }
            if (eccsErrorStr.length() > 0) {
                message += "核心机构号为：" + eccsErrorStr + "的机构存在“机构信用代码系统用户名密码”对应个数错误！";
            }
        }
        return message;
    }

    private void uploadSaveAccount(OrganizationPbcAccountExcelRowVo organizationExcelRowVo, Long organId){
        //人行2级用户名密码
        if (StringUtils.isNotEmpty(organizationExcelRowVo.getPbcIp()) &&
                StringUtils.isNotEmpty(organizationExcelRowVo.getPbcAmsUsername()) &&
                StringUtils.isNotEmpty(organizationExcelRowVo.getPbcAmsPassword())) {
            String[] userNameArr = organizationExcelRowVo.getPbcAmsUsername().split(";");
            String[] passWordArr = organizationExcelRowVo.getPbcAmsPassword().split(";");
            for (int i = 0; i < userNameArr.length; i++) {
                PbcAccountDto amsAccount = new PbcAccountDto();
                amsAccount.setAccountType(EAccountType.AMS);
                amsAccount.setIp(organizationExcelRowVo.getPbcIp());
                amsAccount.setOrgId(organId);
                amsAccount.setAccount(userNameArr[i]);
                //密码是否加密判断
                amsAccount.setPassword(encryptPwdService.encryptPwd(passWordArr[i]));
                amsAccount.setAccountStatus(EAccountStatus.NEW);
                amsAccount.setEnabled(Boolean.TRUE);
                pbcAccountService.uploadSave(amsAccount);
            }
        }

        //人行4级用户名密码
        if (StringUtils.isNotEmpty(organizationExcelRowVo.getPbcIp()) &&
                StringUtils.isNotEmpty(organizationExcelRowVo.getPbcAmsFourUsername()) &&
                StringUtils.isNotEmpty(organizationExcelRowVo.getPbcAmsFourPassword())) {
            String[] userNameArr = organizationExcelRowVo.getPbcAmsFourUsername().split(";");
            String[] passWordArr = organizationExcelRowVo.getPbcAmsFourPassword().split(";");
            for (int i = 0; i < userNameArr.length; i++) {
                PbcAccountDto amsAccount = new PbcAccountDto();
                amsAccount.setAccountType(EAccountType.AMS);
                amsAccount.setIp(organizationExcelRowVo.getPbcIp());
                amsAccount.setOrgId(organId);
                amsAccount.setAccount(userNameArr[i]);
                //密码是否加密判断
                amsAccount.setPassword(encryptPwdService.encryptPwd(passWordArr[i]));
                amsAccount.setAccountStatus(EAccountStatus.NEW);
                amsAccount.setEnabled(Boolean.TRUE);
                pbcAccountService.uploadSave(amsAccount);
            }
        }

        //身份联网核查系统用户名密码
        if (StringUtils.isNotEmpty(organizationExcelRowVo.getPicpIp()) &&
                StringUtils.isNotEmpty(organizationExcelRowVo.getPicpUsername()) &&
                StringUtils.isNotEmpty(organizationExcelRowVo.getPicpPassord())) {
            String[] userNameArr = organizationExcelRowVo.getPicpUsername().split(";");
            String[] passWordArr = organizationExcelRowVo.getPicpPassord().split(";");
            for (int i = 0; i < userNameArr.length; i++) {
                PbcAccountDto picpAccount = new PbcAccountDto();
                picpAccount.setOrgId(organId);
                picpAccount.setIp(organizationExcelRowVo.getPicpIp());
                picpAccount.setAccount(userNameArr[i]);
                //密码是否加密判断
                picpAccount.setPassword(encryptPwdService.encryptPwd(passWordArr[i]));
                picpAccount.setAccountStatus(EAccountStatus.NEW);
                picpAccount.setAccountType(EAccountType.PICP);
                picpAccount.setEnabled(Boolean.TRUE);
                pbcAccountService.uploadSave(picpAccount);
            }
        }

        //机构信用代码系统用户名密码
        if (StringUtils.isNotEmpty(organizationExcelRowVo.getPbcIp()) &&
                StringUtils.isNotEmpty(organizationExcelRowVo.getEccsUsername()) &&
                StringUtils.isNotEmpty(organizationExcelRowVo.getEccsPassword())) {
            String[] userNameArr = organizationExcelRowVo.getEccsUsername().split(";");
            String[] passWordArr = organizationExcelRowVo.getEccsPassword().split(";");
            for (int i = 0; i < userNameArr.length; i++) {
                PbcAccountDto eccsAccount = new PbcAccountDto();
                eccsAccount.setOrgId(organId);
                eccsAccount.setAccountType(EAccountType.ECCS);
                eccsAccount.setAccountStatus(EAccountStatus.NEW);
                eccsAccount.setIp(organizationExcelRowVo.getPbcIp());
                eccsAccount.setAccount(userNameArr[i]);
                //密码是否加密判断
                eccsAccount.setPassword(encryptPwdService.encryptPwd(passWordArr[i]));
                eccsAccount.setEnabled(Boolean.TRUE);
                pbcAccountService.uploadSave(eccsAccount);
            }
        }
    }


    private List<OrganizationExcelRowVo> reorder(OrganizationExcelRowVo root, List<OrganizationExcelRowVo> dataList) {
        List<OrganizationExcelRowVo> reorderedDataList = new LinkedList<>();
        final String code = root.getCode();
        Collection children = CollectionUtils.select(dataList, new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                OrganizationExcelRowVo vo = (OrganizationExcelRowVo) object;
                return StringUtils.equalsIgnoreCase(code, vo.getParentCode());
            }
        });
        if (children.isEmpty()) {
            reorderedDataList.add(root);
            return reorderedDataList;
        }
        reorderedDataList.add(root);
        for (Object child : children) {
            reorderedDataList.addAll(reorder((OrganizationExcelRowVo) child, dataList));
        }
        return reorderedDataList;
    }

    @GetMapping("/all")
    public ResultDto<List<OrganizationDto>> listAll() {
        Long orgId = SecurityUtils.getCurrentUser().getOrgId();
        List<OrganizationDto> all = organizationService.listDescendant(orgId);
        return ResultDtoFactory.toAckData(all);
    }

    @OperateLog(operateModule = OperateModule.ORGANIZATION, operateType = OperateType.UPDATE,operateContent = "机构并迁")
    @PutMapping("/move")
    public ResultDto moveOrgan(MoveOrganDataDto moveOrganDataDto) {
        moveOrganDataService.moveOrganData(moveOrganDataDto);
        return ResultDtoFactory.toAck();
    }

    @GetMapping("/downLoadDataExcel")
    public void exportData(String moveTemplate, Long fromOrgId, HttpServletRequest request, HttpServletResponse response) throws Exception{
        log.info("迁移数据excel下载");
        IExcelExport excelExport = null;
        //用户数据下载
        if("1".equals(moveTemplate)){
            excelExport = moveOrganDataService.exportUserExcel(fromOrgId);
            response.setHeader("Content-disposition", "attachment; filename=" + generateFileName("用户信息", request) + ".xls");
        }else if("2".equals(moveTemplate)){
            //客户数据下载
            excelExport = moveOrganDataService.exportCustomerExcel(fromOrgId);
            response.setHeader("Content-disposition", "attachment; filename=" + generateFileName("客户信息", request) + ".xls");
        }else if("3".equals(moveTemplate)){
            //账户数据下载
            excelExport = moveOrganDataService.exportAccountExcel(fromOrgId);
            response.setHeader("Content-disposition", "attachment; filename=" + generateFileName("账户信息", request) + ".xls");
        }
        response.setContentType("application/octet-stream");
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        ExportExcel.export(response.getOutputStream(), "yyyy-MM-dd", excelExport);
        toClient.flush();
        response.getOutputStream().close();
    }

    /**
     * 生成导出文件名
     *
     * @param
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    private String generateFileName(String fileName1, HttpServletRequest request) throws UnsupportedEncodingException {
        StringBuilder fileName = new StringBuilder();
        fileName.append(BrowserUtil.generateFileName(fileName1, request))
                .append("-").append(DateUtils.DateToStr(new Date(), "yyyy-MM-dd"));
        return fileName.toString();
    }

    @PostMapping(value = "/moveOrganUpload")
    @WebMethod(exclude = true)
    public void moveOrganUpload(@RequestParam("file") MultipartFile file, HttpServletResponse response,String moveTemplate) throws IOException {
        log.info("开始导入迁并数据...");
        ResultDto dto = new ResultDto();
        List<String> regNoList = new ArrayList<>();

        try {
            dto = moveOrganDataService.importExcel(file,moveTemplate);
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(dto));
        } catch (Exception e) {
            log.error("导入Excel失败", e);
            dto.setCode(ResultCode.NACK);
            dto.setMessage("导入Excel失败");
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(dto));
        }
    }

    @OperateLog(operateModule = OperateModule.ORGANIZATION, operateType = OperateType.ENABLE,operateContent = "取消核准")
    @PostMapping("/cancelHeZhun")
    public ResultDto heZhun2BaoBei(@RequestParam("ids[]") String[] ids){
        ResultDto resultDto = organizationService.cancelHeZhun(ids);
        return resultDto;
    }

    @OperateLog(operateModule = OperateModule.ORGANIZATION, operateType = OperateType.DISABLE,operateContent = "撤回核准业务")
    @PostMapping("/recallCancelHeZhun")
    public ResultDto recallCancelHeZhun(@RequestParam("ids[]") String[] ids){
        ResultDto resultDto = organizationService.cancelHeZhunDel(ids);
        return resultDto;
    }


    @GetMapping("/organRegisterList")
    public TableResultResponse<OrganRegisterDto> batchList(OrganRegisterDto organRegisterDto,@PageableDefault(sort = {"lastUpdateDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        TableResultResponse<OrganRegisterDto> tableResultResponse = organRegisterService.queryList(organRegisterDto,pageable);
        return tableResultResponse;
    }

    @GetMapping("/checkOrganRegister")
    public Boolean checkOrganRegister(Long orgId) {
        OrganRegisterDto organRegisterDto = organRegisterService.findByOrganId(orgId);
        if(organRegisterDto != null){
            return true;
        }
        return false;
    }

    @GetMapping("/checkCancelOrganForPbcCode")
    public Boolean checkOrganRegister(String pbcCode) {
        OrganRegisterDto organRegisterDto = organRegisterService.query(pbcCode);
        if(organRegisterDto != null){
            return true;
        }
        return false;
    }

}
