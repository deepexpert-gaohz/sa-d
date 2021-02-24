package com.ideatech.ams.vo;

import com.ideatech.common.vo.TreeNodeVo;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liangding
 * @create 2018-05-10 上午12:42
 **/
@Data
public class AuthorityMenuTreeVo extends TreeNodeVo {
    private String text;
    private String icon;

    //前端菜单树勾选标识字段 put("checked", false) bootstrap-treeview 组件使用
    private Map<String, Object> state = new HashMap<String, Object>();

    //前端菜单树勾选标识字段 自定义tree组件使用。
    private boolean checked = false;

    public List<TreeNodeVo> getNodes() {
        return getChildren();
    }

    public AuthorityMenuTreeVo(String icon, String title) {
        setIcon(icon);
        setText(title);
        state.put("checked", false);//默认不勾选
    }
}
