package com.ideatech.ams.controller;

import com.alibaba.fastjson.JSON;
import com.ideatech.ams.service.ConfigUpdateService;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.ams.system.trace.aop.OperateLog;
import com.ideatech.ams.system.trace.enums.OperateModule;
import com.ideatech.ams.system.trace.enums.OperateType;
import com.ideatech.ams.system.trace.service.ConfigOperateLogServiceImpl;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author liangding
 * @create 2018-05-15 下午4:31
 **/
@RestController
@RequestMapping("/config")
public class ConfigController {

    @Autowired
    private ConfigService configService;

    @Autowired
    private ConfigUpdateService configUpdateService;

    @Value("${ams.company.check:true}")
    private Boolean isCheck;

    @Value("${ams.company.pbc.eccs:true}")
    private boolean eccsSyncEnabled;

    @Value("${version.number:origin}")
    private String versionNumber;

    @Value("${identifyCode.enabled:false}")
    private Boolean identifyCodeEnabled;

    @GetMapping("/")
    public ResultDto list() {
        return ResultDtoFactory.toAckData(configService.list());
    }

    //@OperateLog(operateLogService = ConfigOperateLogServiceImpl.class,operateModule = OperateModule.CONFIG,operateType = OperateType.UPDATE)
    @PostMapping("/")
    public ResultDto save(@RequestBody List<ConfigDto> configs) {
        configService.save(configs);
        configUpdateService.updateRSACode(configs);
        return ResultDtoFactory.toAck();
    }

    //@OperateLog(operateLogService = ConfigOperateLogServiceImpl.class,operateModule = OperateModule.CONFIG,operateType = OperateType.UPDATE)
    @PostMapping("/imageVideoRemind")
    public ResultDto saveImageVideoRemind(@RequestBody List<ConfigDto> configs) {
        configService.saveImageVideoRemind(configs);
        return ResultDtoFactory.toAck();
    }

    /**
     * 新增&修改远程双录意愿提示接口
     * @param configs
     * @return
     */
    //@OperateLog(operateLogService = ConfigOperateLogServiceImpl.class,operateModule = OperateModule.CONFIG,operateType = OperateType.UPDATE)
    @PostMapping("/dualRecordRemind")
    public ResultDto saveDualRecordRemind(@RequestBody List<ConfigDto> configs) {
        configService.saveDualRecordRemind(configs);
        return ResultDtoFactory.toAck();
    }

    /**
     * 新增&修改 风险提示 接口
     * @param configs
     * @return
     */
    //@OperateLog(operateLogService = ConfigOperateLogServiceImpl.class,operateModule = OperateModule.CONFIG,operateType = OperateType.UPDATE)
    @PostMapping("/riskWarning")
    public ResultDto saveRiskWarning(@RequestBody List<ConfigDto> configs) {
        configService.saveRiskWarning(configs);
        return ResultDtoFactory.toAck();
    }

    @RequestMapping(value = "/isCheck", method = RequestMethod.GET)
    public Boolean getIsCheck() {
        return isCheck;
    }

    @RequestMapping(value = "/getDropdownSuspends", method = RequestMethod.GET)
    public Boolean getDropdownSuspends() {
        List<ConfigDto> configList = configService.findByKey("dropdownSuspends");
        if(CollectionUtils.isNotEmpty(configList)){
            return Boolean.valueOf(configList.get(0).getConfigValue());
        }
        return false;
    }

    @RequestMapping(value = "/findByKey", method = RequestMethod.GET)
    public Boolean findByKey(String configKey) {
        List<ConfigDto> configList = configService.findByKey(configKey);
        if (CollectionUtils.isNotEmpty(configList)) {
            return Boolean.valueOf(configList.get(0).getConfigValue());
        }
        return false;
    }

    @GetMapping(value = "/findByKeyLike")
    public ResultDto findByKeyLike(String configKey) {
        List<ConfigDto> configList = configService.findByKey(configKey);
        return ResultDtoFactory.toAckData(configList);
    }

    @DeleteMapping("/{configKey:.+}")
    public ResultDto deleteByConfigKey(@PathVariable("configKey") String configKey){
        configService.deleteByConfigKey(configKey);
        return ResultDtoFactory.toAck();
    }

    @PostMapping("/upload")
    public void upload(@RequestParam String configKey, MultipartFile file, HttpServletResponse response) throws IOException {
        ConfigDto configDto = new ConfigDto();
        configDto.setConfigKey(configKey);
        String base64Content = Base64Utils.encodeToString(IOUtils.toByteArray(file.getInputStream()));
        configDto.setConfigValue(base64Content);
        configService.save(configDto);
        ResultDto resultDto = ResultDtoFactory.toAck();
        response.setContentType("text/html; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(resultDto));
    }

    @GetMapping("/download")
    public void download(@RequestParam(required = true) String configKey, HttpServletResponse response) throws IOException {
        List<ConfigDto> byKey = configService.findByKey(configKey);
        if (CollectionUtils.isNotEmpty(byKey)) {
            ConfigDto configDto = byKey.get(0);
            if(configDto!=null){
                String base64Content = configDto.getConfigValue();
                byte[] bytes = Base64Utils.decodeFromString(base64Content);
                IOUtils.copy(new ByteArrayInputStream(bytes), response.getOutputStream());
                response.getOutputStream().flush();
            }
        }
    }

    @RequestMapping(value = "/isEccsSync", method = RequestMethod.GET)
    public Boolean getIsEccsSync() {
        return eccsSyncEnabled;
    }

    @RequestMapping(value = "/findValueByKey", method = RequestMethod.GET)
    public String findValueByKey(String configKey) {
        List<ConfigDto> configList = configService.findByKey(configKey);
        if (CollectionUtils.isNotEmpty(configList)) {
            return configList.get(0).getConfigValue();
        }
        return null;
    }

    @RequestMapping(value = "/getVersionNumber", method = RequestMethod.GET)
    public String getVersionNumber() {
        if("origin".equals(versionNumber)) {
            return "";
        }

        return versionNumber;
    }

    @RequestMapping(value = "/getIdentifyCodeEnabled", method = RequestMethod.GET)
    public Boolean getIdentifyCodeEnabled() {
        return identifyCodeEnabled;
    }

    @RequestMapping(value = "/findConfigValue", method = RequestMethod.GET)
    public String findConfigValue(String configKey) {
        List<ConfigDto> configList = configService.findByKey(configKey);
        if (CollectionUtils.isNotEmpty(configList)) {
            return configList.get(0).getConfigValue();
        }
        return null;
    }
    @RequestMapping(value = "/getImageVideoRemind", method = RequestMethod.GET)
    public ResultDto getImageVideoRemind(String configKey){
        List<ConfigDto> configList = configService.findImageVideoRemind(configKey);
       return ResultDtoFactory.toAck(null,configList);
    }
}
