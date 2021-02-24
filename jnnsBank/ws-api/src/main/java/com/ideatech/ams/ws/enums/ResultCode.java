package com.ideatech.ams.ws.enums;

import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.DisplayableError;

/**
 * @author vantoo
 * @date 14:03 2018/5/20
 */
public enum ResultCode {

    /* 成功状态码 */
    SUCCESS("0", "成功"),

    /* 参数错误：10001-19999 */
    PARAM_IS_INVALID("10001", "参数无效"),
    PARAM_IS_BLANK("10002", "参数为空"),
    PARAM_TYPE_BIND_ERROR("10003", "参数类型错误"),
    PARAM_NOT_COMPLETE("10004", "参数缺失"),

    /* 系统基础信息错误-start：20001-29999*/
    USER_NOT_LOGGED_IN("20001", "用户未登录"),
    USER_LOGIN_ERROR("20002", "账号不存在或密码错误"),
    USER_ACCOUNT_FORBIDDEN("20003", "账号已被禁用"),
    USER_NOT_EXIST("20004", "用户不存在"),
    USER_HAS_EXISTED("20005", "用户已存在"),

    /* 机构错误：21001-21999 */
    ORGAN_NOT_CONFIG("21001", "机构未配置"),

    /* 系统基础信息错误-end */

    /* 业务错误-start：30001-39999 */
    BUSINESS_ERROR("30001", "业务模块异常"),

    /* 具体的业务错误在这里体现（公共部分） */
    ORGAN_NOT_CONFIG_SYNC_USER("30002", "机构未配置人行用户"),
    ORGAN_NOT_CONFIG_IP("31008", "机构未配置的人行ip"),
    ORGAN_NOT_CONFIG_SYNC_ECCS_USER("31009", "机构未配置机构信用代码用户"),
    SYNC_FAILURE("30003", "上报失败"),
    DATA_SAVE_FAILURE("30004", "数据保存失败"),
    BILL_TYPE_MUST_NOT_BE_EMPTY("30005", "单据类型不可为空"),
    UN_FINISHED_BILL("30006", "存在未完成流水"),
    UPDATE_BILL_STATUS_FAILURE("30007", "更新流水最终状态异常"),
    ACCT_DATA_NOT_EXIST("30008","本地账户数据不存在"),
    ACCT_DATA_NOT_NORMAL("30009","本地账户状态非正常"),
    BILL_STATUS_ERROR("30010", "流水审核状态不正确"),
    IMAGE_RELEV_FAIL("30011","影像关联失败"),

    /* 工商错误：31001-31999 */
    SAIC_STATUS_REVOKE("31001", "工商状态吊销"),
    SAIC_STATUS_CANCEL("31002", "工商状态注销"),
    SAIC_STATUS_QUIT("31003", "工商状态迁出"),
    SAIC_INCLUDE_ILLEGAL("31004", "工商存在严重违法记录"),
    SAIC_INCLUDE_ABNORMAL_OPERATION("31005", "工商存在经营异常记录"),
    SAIC_QUERY_ERROR("31006", "工商查询异常"),
    SAIC_BUSINESS_LICENSE_EXPIRED("31007", "工商营业执照到期"),
    SAIC_NOT_FOUND("31008","无法获取工商信息"),

    /* 人行账管错误：32001-32999 */
    PBC_VALIDATION_NOT_PASS("32001", "人行校验不通过"),
    PBC_CHECKDETAIL_FAILURE("32002", "人行查询失败"),
    PBC_QUERY_ERROR("31003", "工商查询异常"),
    PBC_NOT_FOUND("31008","无法获取人行信息"),
    PBC_HAVE_NORMAL("31009","存在正常账户"),
    PBC_HAVE_SUSPEND("31010","基本户下存在久悬账户"),

    /* 信用代码证错误：33001-33999 */
    ECCS_NONSUPPORT_OPERATETYPE("33001", "不支持此种操作类型"),

    /* 人行账管用户错误: 34001-34999 */
    PBC_LOGIN_ERROR("34001", "人行用户名或密码错误"),
    PBC_USER_LOCK("34002", "人行用户名被锁"),
    PBC_USER_NOT_EXIST("34003", "人行用户不存在"),
    PBC_USER_OR_PASSWORD_EMPTY("34004", "人行用户名或密码为空"),
    PBC_USER_NOT_2_LEVEL("34005", "人行用户名非2级操作员"),
    PBC_BASICACCT_IS_REVOKE("34006", "基本户已销户"),
    PBC_BASICACCT_INCLUDE_SUSPEND("34007", "基本户下存在其他久悬户"),

    /* 信用代码用户错误: 35001-35999 */


    //业务错误-end


    /* 系统错误：40001-49999 */
    UNKNOWN_SYSTEM_ERROR("40000", "未知异常"),
    SYSTEM_BUSY("40001", "系统繁忙，请稍后重试"),
    NETWORK_CONNECTION_ERROR("40002", "网络不通"),
    NETWORK_ERROR("40003", "网络异常"),
    NETWORK_TIMEOUT("40004", "网络超时"),

    /* 数据错误：50001-599999 */
    NO_DATA_EXIST("50001", "数据未找到"),
    DATA_IS_WRONG("50002", "数据有误"),
    DATA_ALREADY_EXISTED("50003", "数据已存在"),

    /* 接口错误：60001-69999 */
    INTERFACE_INNER_INVOKE_ERROR("60001", "内部系统接口调用异常"),
    INTERFACE_OUTER_INVOKE_ERROR("60002", "外部系统接口调用异常"),
    INTERFACE_FORBID_VISIT("60003", "该接口禁止访问"),
    INTERFACE_ADDRESS_INVALID("60004", "接口地址无效"),
    INTERFACE_REQUEST_TIMEOUT("60005", "接口请求超时"),
    INTERFACE_EXCEED_LOAD("60006", "接口负载过高"),

    /* 权限错误：70001-79999 */
    PERMISSION_NO_ACCESS("70001", "无访问权限"),

    /* 对象转换失败 */
    OBJ_CONVERT_FAIL("80001", "对象转换失败"),
    PBCACCOUNT_NOT_FOUND("80002", "机构代码对应人行配置为空"),
    ACCTNO_NOT_FOUND("80003", "该账号无存量数据"),
    NO_VALID_PBCACCOUNT("80004", "该机构维护的人行账号密码错误或密码已失效");


    private String code;

    private String message;

    ResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }

    public static String getMessage(String name) {
        for (ResultCode item : ResultCode.values()) {
            if (item.name().equals(name)) {
                return item.message;
            }
        }
        return name;
    }

    public static String getCode(String name) {
        for (ResultCode item : ResultCode.values()) {
            if (item.name().equals(name)) {
                return item.code;
            }
        }
        return null;
    }

    public static ResultCode getResultCodeByErrorCode(DisplayableError errorCode) {
        if (errorCode instanceof EErrorCode) {
            if (errorCode == EErrorCode.PBC_QUERY_PARAM_EMPTY) {
                return PARAM_IS_BLANK;
            } else if (errorCode == EErrorCode.ORGAN_NOTCONFIG) {
                return ORGAN_NOT_CONFIG;
            } else if (errorCode == EErrorCode.ORGAN_AMS_USER_NOTCONFIG) {
                return ORGAN_NOT_CONFIG_SYNC_USER;
            } else if (errorCode == EErrorCode.ORGAN_AMS_USER_EMPTY) {
                return ORGAN_NOT_CONFIG_SYNC_USER;
            } else if (errorCode == EErrorCode.ORGAN_AMS_USER_MUST2LEVEL) {
                return PBC_USER_NOT_2_LEVEL;
            } else if (errorCode == EErrorCode.ORGAN_AMS_USER_IP_EMPTY) {
                return ORGAN_NOT_CONFIG_IP;
            } else if (errorCode == EErrorCode.ORGAN_ECCS_USER_NOTCONFIG) {
                return ORGAN_NOT_CONFIG_SYNC_ECCS_USER;
            } else if (errorCode == EErrorCode.PBC_QUERY_ERROR) {
                return PBC_QUERY_ERROR;
            }
        }
        return UNKNOWN_SYSTEM_ERROR;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
