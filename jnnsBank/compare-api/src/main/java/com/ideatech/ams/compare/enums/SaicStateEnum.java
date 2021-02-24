package com.ideatech.ams.compare.enums;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * @author jzh
 * @date 2019/6/5.
 */

@Slf4j
@Getter
public enum SaicStateEnum {

    SUBSIST("续存"),EMPLOYED("在业"), QUIT("迁出"),REVOKE("吊销"), CANCEL("注销"),OTHER("其他"),UNKNOW("未知");

    private String name;

    SaicStateEnum(String name) {
        this.name = name;
    }

    public static SaicStateEnum saicState2Enum(String state) {

        String[] subsistState = new String[]{"存续（在营、开业、在册）", "登记成立","存续"};
        String[] employed = new String[]{"在营（开业）企业","在营（开业）","开业","在营","正常"};
        String[] quitState = new String[]{"迁移异地", "迁往市外", "市外迁出","已迁出企业"};
        String[] revokeState = new String[]{"吊销企业", "吊销，未注销", "吊销未注销", "已吊销", "吊销,已注销", "拟注销", "停业", "吊销,未注销", "吊销，已注销"};
        String[] cancelState = new String[]{"已注销", "注销企业", "撤销", "企业已注销", "企业直接申请注销"};
        String[] otherState = new String[] {"个体转企业","非正常户","撤销登记"};

        state = StringUtils.trimToEmpty(state);

        if (Arrays.asList(subsistState).contains(state)) {
            return SUBSIST;
        } else if (Arrays.asList(employed).contains(state)){
            return EMPLOYED;
        }else if (Arrays.asList(quitState).contains(state)) {
            return QUIT;
        } else if (Arrays.asList(revokeState).contains(state)) {
            return REVOKE;
        } else if (Arrays.asList(cancelState).contains(state)) {
            return CANCEL;
        } else if (Arrays.asList(otherState).contains(state)){
            return OTHER;
        }else {
            log.warn("工商状态转换失败，传入的工商状态不在所给的范围内，state："+state);
            return UNKNOW;
        }
    }
}
