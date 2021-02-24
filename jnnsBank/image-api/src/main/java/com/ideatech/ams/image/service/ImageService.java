package com.ideatech.ams.image.service;

import com.alibaba.fastjson.JSONArray;
import com.ideatech.ams.image.dto.ImageInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;

public interface ImageService {

    /**
     * 影像记录保存
     */
    ImageInfo save(ImageInfo info);
    /**
     * 高拍仪base64字符串生成本地图片并保存
     */
    ImageInfo createImage(String base64);
    /**
     * 本地上传影像图片
     */
    ImageInfo uploadImage(InputStream is);
    /**
     * 根据id查影像
     */
    ImageInfo findImageById(Long id);

    /**
     * 设置类型
     * @param info
     */
    void setType(ImageInfo info);

    /**
     * 删除影像
     * @param id
     */
    void delete(Long id);

    /**
     *按流水查询影像
     * refBillId(流水id) 必填
     * docCode(影像种类code) 选填
     * @param info
     * @return
     * 返回ImageInfo的集合
     * ImageInfo包含了一条影像记录的所有信息
     * 前端显示提取imgPath属性的值
     */
    List<ImageInfo> query(ImageInfo info);

    /**
     * 上传到影像平台
     * refBillId(流水id) 必填
     * @param info
     * @return
     * 返回值是上传失败的数量
     */
    int uploadToImage(ImageInfo info);

    /**
     * 批量下载影像
     * @param refBillId
     * @return
     */
    void downLoadBatchImage(HttpServletResponse response,Long refBillId);

    /**
     *按账户查询影像
     * acctId(账户id) 必填
     * docCode(影像种类code) 选填
     * @param info
     * @return
     * 返回ImageInfo的集合
     * ImageInfo包含了一条影像记录的所有信息
     * 前端显示提取imgPath属性的值
     */
    List<ImageInfo> queryByAccount(ImageInfo info);
}
