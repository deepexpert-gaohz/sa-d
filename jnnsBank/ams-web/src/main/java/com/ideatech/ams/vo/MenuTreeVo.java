package com.ideatech.ams.vo;

import com.ideatech.common.vo.TreeNodeVo;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author liangding
 * @create 2018-05-07 上午9:55
 **/
@Data
@AllArgsConstructor
public class MenuTreeVo extends TreeNodeVo {
    private String icon;
    private String title;
    private String href;
    private Boolean spread = Boolean.FALSE;
}
