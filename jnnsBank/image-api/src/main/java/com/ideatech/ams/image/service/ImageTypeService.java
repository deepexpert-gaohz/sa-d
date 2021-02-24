package com.ideatech.ams.image.service;


import com.ideatech.ams.image.dto.ImageTypeInfo;

import com.ideatech.common.msg.TableResultResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ImageTypeService {
    /**
     * 影像配置保存
     */
    ImageTypeInfo save(ImageTypeInfo info);
    /**
     * 删除配置
     */
    void deleteConfig(Long id);
    /**
     * 查询所有配置
     */
    TableResultResponse query(ImageTypeInfo info,Pageable pageable);
    /**
     * ImageTypeInfo
     */
    ImageTypeInfo getById(Long id);

    /**
     * 根据业务类型、账户性质、存款人性质确定需要上传的影像类型
     * @param info
     * @return
     */
    List<ImageTypeInfo> getImageType(ImageTypeInfo info);

    /**
     * 获取该流水需要上传的影像类型
     * @param billId
     * @return
     */
    List<ImageTypeInfo> getByBillId(Long billId);

    /**
     * 获取
     * @param billId
     * @param docCode
     * @return
     */
    ImageTypeInfo getByBillIdAndDocCode(Long billId, String docCode);
}
