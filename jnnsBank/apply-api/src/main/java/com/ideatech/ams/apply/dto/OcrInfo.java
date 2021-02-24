package com.ideatech.ams.apply.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

/**
 * 开户影像信息
 * 
 * @author cxf
 * @company ydrx
 * @date Jan 3, 2018 9:25:43 AM
 */

@Data
public class OcrInfo extends BaseMaintainableDto {

  private Long id;

  private String applyId;// 预约编号

  private String three;// 是否三证合一

  private String businessImg;// 营业执照

  private String businessImgUrl;// 营业执照URL

  private String isMechanism;// 机构信用代码证

  private String mechanism;// 组织机构证

  private String mechanismUrl;// 组织机构证URL


  private String positive;// 法人身份证正面

  private String positiveUrl;// 法人身份证正面Url

  private String back;// 法人身份证背面

  private String backUrl;// 法人身份证背面Url

  private String basic;// 开户申请书

  private String basicUrl;// 开户申请书

}
