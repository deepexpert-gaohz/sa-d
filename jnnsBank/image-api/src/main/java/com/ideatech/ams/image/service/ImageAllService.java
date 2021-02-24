package com.ideatech.ams.image.service;

import com.alibaba.fastjson.JSONArray;
import com.ideatech.ams.image.dto.ImageAllInfo;
import com.ideatech.common.dto.ResultDto;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;

public interface ImageAllService {
    /**
     * 影像记录保存
     * @param info
     * @return
     */
    ImageAllInfo save(ImageAllInfo info);
    /**
     * 高拍仪base64字符串生成本地图片并保存
     */
    ImageAllInfo createImage(String base64);
    /**
     * 本地上传影像图片
     */
    String uploadImage(InputStream is,String filename);
    /**
     * 根据id查影像
     */
    ImageAllInfo findImageById(Long imageId);

    /**
     * 编辑影像信息
     * 影像id不能为空
     * @param info
     * @return
     */
    ImageAllInfo editImageInfo(ImageAllInfo info);

    /**
     * 删除影像记录
     * @param imageId
     */
    void deleteById(Long imageId);
    /**
     * 按流水查询影像
     * (billsId: 流水id 必填;影像种类:code 选填)
     * @param billsId 流水id
     * @param docCode  影像种类code 可选填
     * @return
     * 返回ImageAllInfo的集合
     * ImageAllInfo包含了一条影像记录的所有信息
     * 前端显示提取imgPath属性的值
     */
    List<ImageAllInfo> queryByBillsId(Long billsId,String docCode);
    /**
     * 按账户id查询影像
     * (acctId: 账户id 必填;影像种类 : code 选填)
     * @param acctId 账户id
     * @param docCode  影像种类code 可选填
     * @return
     * 返回ImageAllInfo的集合
     * ImageAllInfo包含了一条影像记录的所有信息
     * 前端显示提取imgPath属性的值
     */
    List<ImageAllInfo> queryByAcctId(Long acctId,String docCode);
    /**
     * 按照客户id查询影像
     * (customerId : 客户id;影像种类:code 选填)
     * @param customerId 客户id
     * @param docCode  影像种类code 可选填
     * @return
     * 返回ImageAllInfo的集合
     * ImageAllInfo包含了一条影像记录的所有信息
     * 前端显示提取imgPath属性的值
     */
    List<ImageAllInfo> queryByCustomerId(Long customerId,String docCode);

    /**
     * 上传到影像平台
     * billsId(流水id) 必填
     * @return
     * 返回值是上传失败的数量
     */
    int uploadToImage(Long billsId);
    /**
     * 上传到影像平台
     * billsId(流水id) 必填
     * @return
     * 返回值是上传失败的数量
     */
    int uploadToImage(Long billsId,String docCode);

    /**
     *参数必填
     * @param billsId
     * @param docCode
     * @return
     */
    List<ImageAllInfo> findByBillsId(Long billsId,String docCode);

    /**
     * 批量下载
     * @param acctBillsId
     * @param imageTypeId
     * @param response
     */
    void downloadImageZip(Long acctBillsId,Long imageTypeId,HttpServletResponse response);

    /**
     * 批量下载（直接下载）
     * @param fileName  压缩包名称
     * @param filePath   影像存放路径
     * @param response
     */
    void downloadImageZip2(String fileName, String[] filePath, HttpServletResponse response);

    /**
     *
     * @param acctBillsId
     * @param customerId
     * @param acctId
     */
    void setCustomerIdAndAcctId(Long acctBillsId,Long customerId,Long acctId);

    /**
     *
     * @param imageTempNo
     * @param acctBillsId
     * @param customerId
     * @param acctId
     */
    void setCustomerIdAndAcctIdByImageTempNo(String imageTempNo,Long acctBillsId,Long customerId,Long acctId);

    /**
     * 提供给外围接口,配合上报接口使用；两个接口调用顺序会有影响
     * 1、先调用影像接口，后调用上报接口：此接口会返回一个临时编号，上报时必须将临时编号传入，以便做影像与流水之间的关联
     * 2、先调用上报接口，后调用影像接口：调用影像接口时必须传入流水id，做关联使用
     * @param info
     * @param flag 0 代表影像接口先调用
     *      *        1  代表上报接口先调用
     * @return
     */
    String saveImageFromOut(ImageAllInfo info,String flag);

    /**
     * 根据客户号获取客户最新影像信息
     * @param customerNo
     * @return
     */
    List<ImageAllInfo> getImageForCustomer(String customerNo);

    /**
     * 根据CustomerId获取客户最新影像信息
     * @param customerId
     */
    List<ImageAllInfo> getImageByCustomerId(Long customerId);

    /**
     * 根据客户号判断是否有流水
     * @param customerNo
     * @return
     */
    boolean isHaveBills(String customerNo);

    /**
     * 根据客户id判断是否有流水
     * @param customerId
     * @return
     */
    boolean isHaveBills2(Long customerId);

    /**
     * 根据账户id获取最新影像信息
     * @param acctId
     * @return
     */
    List<ImageAllInfo> getImageForAcctId(Long acctId);

    /**
     * 根据流水id获取最新影像信息
     * @param billId
     * @return
     */
    List<ImageAllInfo> getImageForBillId(Long billId);

    /**
     * 将影像数据格式化成前端需要的json格式
     * @param imageList
     * @return
     */
    JSONArray formatImageJson(List<ImageAllInfo> imageList);

    /**
     * 上报影像
     * @param billId
     */
    ResultDto sync(Long billId);

    /**
     * 更新单条数据
     * @param
     */
    ResultDto update(Long[] imgIds,Long imageTypeId);
}
