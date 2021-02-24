package com.ideatech.ams.kyc.util;

import com.ideatech.ams.kyc.dto.ChangeMessDto;
import com.ideatech.ams.kyc.dto.IllegalDto;
import com.ideatech.common.util.DateUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import java.util.Date;
import java.util.List;

public class SaicUtils {

    /**
     * 是否经营异常
     *
     * @param changeMessDtoList
     * @return
     */
    public static Boolean isAbnormalOperation(List<ChangeMessDto> changeMessDtoList) {
        if (CollectionUtils.isNotEmpty(changeMessDtoList)) {
            for (ChangeMessDto changeMessDto : changeMessDtoList) {
                if (StringUtils.isBlank(changeMessDto.getOutdate()) && StringUtils.isBlank(changeMessDto.getOutorgan()) && StringUtils.isBlank(changeMessDto.getOutreason())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否严重违法
     *
     * @param illegalList
     * @return
     */
    public static Boolean isIllegal(List<IllegalDto> illegalList) {
        if (CollectionUtils.isNotEmpty(illegalList)) {
            for (IllegalDto illegalDto : illegalList) {
                if (StringUtils.isBlank(illegalDto.getDateout()) && StringUtils.isBlank(illegalDto.getOrganout()) && StringUtils.isBlank(illegalDto.getReasonout())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否证件到期
     *
     * @param businessLicenseDateStr
     */
    public static Boolean isBusinessLicenseExpired(String businessLicenseDateStr) {
        Date date = new Date();
        if (StringUtils.isNotBlank(businessLicenseDateStr)) {
            try {
                Date businessLicenseDate = org.apache.commons.lang.time.DateUtils.parseDate(businessLicenseDateStr, new String[]{"yyyy年MM月dd日"});
                return !DateUtils.isSameDay(businessLicenseDate, date) && DateUtil.isBefore(businessLicenseDate, date);
            } catch (Exception e) {
                //不做处理
            }
        }
        return null;
    }

}
