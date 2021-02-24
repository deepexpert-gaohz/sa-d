package com.ideatech.ams.risk.util;

import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.common.util.ApplicationContextUtil;
import com.ideatech.common.util.SecurityUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yangwz
 * @Description
 * @date 2019-11-06 12:41
 */
public class RiskUtil {

    public static String getOrganizationCode() {
        OrganizationService organizationService = ApplicationContextUtil.getBean( OrganizationService.class);
        String currentOrgFullId = SecurityUtils.getCurrentOrgFullId();
//        String obj[] = currentOrgFullId.split("-");
        OrganizationDto organizationPo = new OrganizationDto ();
//        if (obj.length > 3) {
//            String fullId = null;
//            fullId = obj[0] +"-"+ obj[1] +"-"+ obj[2];
//            organizationPo = organizationService.findByOrganFullId(fullId);
//        } else {
//            organizationPo = organizationService.findByOrganFullId(currentOrgFullId);
//        }
        organizationPo = organizationService.findByOrganFullId(currentOrgFullId);

        //若果code为支行code  则通过parentid反差法人code
        Pattern p = Pattern.compile("^[A-Za-z]+$");
        Matcher m = p.matcher(organizationPo.getCode());
        boolean isValid = m.matches();
        if(isValid==false){
            OrganizationDto byCode = organizationService.findByCode(organizationPo.getCode());
            OrganizationDto byParentIdLike = organizationService.findByParentIdLike("%" + byCode.getParentId());
            organizationPo.setCode(byParentIdLike.getCode());
        }
        return organizationPo.getCode();
    }

    public static String getCodeForSech(String code) {
        OrganizationService organizationService = ApplicationContextUtil.getBean( OrganizationService.class);
        Pattern p = Pattern.compile("^[A-Za-z]+$");
        Matcher m = p.matcher(code);
        boolean isValid = m.matches();
        if(isValid==false){
            OrganizationDto byCode = organizationService.findByCode(code);
            OrganizationDto byParentIdLike = organizationService.findByParentIdLike("%" + byCode.getParentId());
            return byParentIdLike.getCode();
        }
        return code;
    }
    public static String getOrganizationFullId() {
        OrganizationService organizationService = ApplicationContextUtil.getBean( OrganizationService.class);
        String currentOrgFullId = SecurityUtils.getCurrentOrgFullId();
        String obj[] = currentOrgFullId.split("-");
        OrganizationDto organizationPo = new OrganizationDto ();
//        if (obj.length > 3) {
//            String fullId = null;
//            fullId = obj[0] +"-"+ obj[1] +"-"+ obj[2];
//            organizationPo = organizationService.findByOrganFullId(fullId);
//        } else {
//            organizationPo = organizationService.findByOrganFullId(currentOrgFullId);
//        }
        organizationPo = organizationService.findByOrganFullId(currentOrgFullId);

        //若果code为支行code  则通过parentid反差法人code
        Pattern p = Pattern.compile("^[A-Za-z]+$");
        Matcher m = p.matcher(organizationPo.getCode());
        boolean isValid = m.matches();
        if(isValid==false){
            OrganizationDto byCode = organizationService.findByCode(organizationPo.getCode());
            OrganizationDto byParentIdLike = organizationService.findByParentIdLike("%" + byCode.getParentId());
            organizationPo.setCode(byParentIdLike.getCode());
        }
        return organizationPo.getFullId ();
    }
}
