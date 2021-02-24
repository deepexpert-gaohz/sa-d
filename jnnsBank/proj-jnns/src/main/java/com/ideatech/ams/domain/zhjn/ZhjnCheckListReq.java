package com.ideatech.ams.domain.zhjn;

import lombok.Data;

/**
 * 经办人列表请求体参数
 *
 * @auther yfy
 * @create 2020-8-6
 **/

@Data
public class ZhjnCheckListReq {

    private String checkNo;
    private String customerName;

}
