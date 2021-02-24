package com.ideatech.common.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * @author van
 * @date 16:50 2018/8/9
 */
@Getter
public enum SaicStatusEnum {

    REGISTERED("在册"), CANCEL("注销撤销"), REVOKE("吊销"), QUIT("迁出"), OTHER("其他"), NOTFOUND("未找到");

    private String name;

    SaicStatusEnum(String name) {
        this.name = name;
    }

    /**
     * 备案登记中
     * 被吊销
     * 变更登记中
     * 不明
     * 撤消登记
     * 撤销
     * 筹建
     * 存续
     * 存续(经营正常)
     * 存续（在营、开业、在册）
     * 待迁入
     * 登记成立
     * 吊销
     * 吊销，未注销
     * 吊销，已注销
     * 吊销后注销
     * 吊销企业
     * 吊销未注销
     * 吊销已注销
     * 经营期限届满
     * 开业
     * 开业/正常经营
     * 开业登记中
     * 名称核准
     * 拟注销
     * 年检中
     * 其他
     * 其它
     * 迁出
     * 迁入
     * 迁移异地
     * 清算
     * 清算中
     * 设立登记中
     * 数据补录中
     * 条线变出
     * 停业
     * 已撤销登记
     * 已撤销企业
     * 已吊销
     * 已开业
     * 已迁出
     * 已迁出企业
     * 已注销
     * 异地迁入
     * 在业
     * 在营（开业）
     * 正常
     * 正常在业
     * 证照管理登记中
     * 注吊销
     * 注销
     * 注销登记中
     * 注销企业
     * 存续(在营、开业、在册)
     * 在营（开业）企业
     *
     * @return
     */
    public static SaicStatusEnum saicState2Enum(String state) {
        //@formatter:off
        String[] registeredState = new String[]{"存续", "存续(经营正常)", "存续（在营、开业、在册）", "登记成立", "开业", "开业/正常经营", "开业登记中", "已开业", "在业", "在营（开业）", "正常", "正常在业", "证照管理登记中", "存续(在营、开业、在册)", "在营（开业）企业"};
        String[] cancelState = new String[]{"撤消登记", "撤销", "吊销后注销", "已撤销登记", "已撤销企业", "已注销", "注销", "注销登记中", "注销企业"};
        String[] revokeState = new String[]{"被吊销", "吊销", "吊销，未注销", "吊销已注销", "吊销，已注销", "吊销企业", "吊销未注销", "已吊销", "注吊销"};
        String[] quitState = new String[]{"迁出", "已迁出", "已迁出企业"};
        //@formatter:on

        state = StringUtils.trimToEmpty(state);

        if (Arrays.asList(registeredState).contains(state)) {
            return REGISTERED;
        } else if (Arrays.asList(cancelState).contains(state)) {
            return CANCEL;
        } else if (Arrays.asList(revokeState).contains(state)) {
            return REVOKE;
        } else if (Arrays.asList(quitState).contains(state)) {
            return QUIT;
        } else {
            //如果判断不出，则再根据关键字判断
            for (String s : registeredState) {
                if (StringUtils.indexOf(state, s) > -1) {
                    return REGISTERED;
                }
            }
            for (String s : revokeState) {
                if (StringUtils.indexOf(state, s) > -1) {
                    return REVOKE;
                }
            }
            for (String s : cancelState) {
                if (StringUtils.indexOf(state, s) > -1) {
                    return CANCEL;
                }
            }
            for (String s : quitState) {
                if (StringUtils.indexOf(state, s) > -1) {
                    return QUIT;
                }
            }
            return OTHER;
        }
    }

}
