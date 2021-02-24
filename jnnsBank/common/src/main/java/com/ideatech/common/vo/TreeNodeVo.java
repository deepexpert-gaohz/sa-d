package com.ideatech.common.vo;

import lombok.Data;

import java.util.List;

/**
 * @author liangding
 * @create 2018-05-07 上午9:58
 **/
@Data
public class TreeNodeVo {
    private Long id;
    private Long parentId;
    private List<TreeNodeVo> children;

    public void add(TreeNodeVo node){
        children.add(node);
    }

}
